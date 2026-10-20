package org.mcfish.aquarium.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import org.mcfish.aquarium.data.KioskSettings
import org.mcfish.aquarium.data.KioskSettingsRepository
import java.time.LocalTime

class KioskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = KioskSettingsRepository(application)
    val settings: StateFlow<KioskSettings> = repository.settings

    private val currentTimeFlow = flow {
        while (true) {
            emit(LocalTime.now())
            delay(1000)
        }
    }

    // Initial values are computed from the current settings and time so the first
    // brightness sent to the window is already correct (no full-brightness flash at night).
    val isDimmed: StateFlow<Boolean> = combine(settings, currentTimeFlow) { currentSettings, time ->
        isDimmedAt(currentSettings, time)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = isDimmedAt(settings.value, LocalTime.now())
    )

    val currentBrightness: StateFlow<Float> = combine(settings, isDimmed) { currentSettings, dimmed ->
        brightnessFor(currentSettings, dimmed)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = brightnessFor(settings.value, isDimmed.value)
    )

    fun saveSettings(newSettings: KioskSettings) {
        repository.saveSettings(newSettings)
    }

    companion object {
        fun isDimmedAt(settings: KioskSettings, time: LocalTime): Boolean {
            if (!settings.dimmingEnabled) return false
            return isTimeInDimmingPeriod(
                currentHour = time.hour,
                currentMinute = time.minute,
                startHour = settings.dimStartHour,
                startMinute = settings.dimStartMinute,
                endHour = settings.dimEndHour,
                endMinute = settings.dimEndMinute
            )
        }

        fun brightnessFor(settings: KioskSettings, dimmed: Boolean): Float =
            if (dimmed) settings.dimmedBrightness else settings.normalBrightness

        fun isTimeInDimmingPeriod(
            currentHour: Int,
            currentMinute: Int,
            startHour: Int,
            startMinute: Int,
            endHour: Int,
            endMinute: Int
        ): Boolean {
            val currentMins = currentHour * 60 + currentMinute
            val startMins = startHour * 60 + startMinute
            val endMins = endHour * 60 + endMinute

            return if (startMins == endMins) {
                false
            } else if (startMins < endMins) {
                currentMins in startMins until endMins
            } else {
                // Spans midnight, e.g. 22:00 to 07:00
                currentMins >= startMins || currentMins < endMins
            }
        }
    }
}
