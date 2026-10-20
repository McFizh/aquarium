package org.mcfish.aquarium.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class KioskSettingsRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("kiosk_settings", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<KioskSettings> = _settings.asStateFlow()

    private fun loadSettings(): KioskSettings {
        return KioskSettings(
            url = prefs.getString(KEY_URL, "https://example.com") ?: "https://example.com",
            dimmingEnabled = prefs.getBoolean(KEY_DIMMING_ENABLED, true),
            dimStartHour = prefs.getInt(KEY_DIM_START_HOUR, 22),
            dimStartMinute = prefs.getInt(KEY_DIM_START_MINUTE, 0),
            dimEndHour = prefs.getInt(KEY_DIM_END_HOUR, 7),
            dimEndMinute = prefs.getInt(KEY_DIM_END_MINUTE, 0),
            normalBrightness = prefs.getFloat(KEY_NORMAL_BRIGHTNESS, 1.0f),
            dimmedBrightness = prefs.getFloat(KEY_DIMMED_BRIGHTNESS, 0.1f)
        )
    }

    fun saveSettings(newSettings: KioskSettings) {
        prefs.edit().apply {
            putString(KEY_URL, newSettings.url)
            putBoolean(KEY_DIMMING_ENABLED, newSettings.dimmingEnabled)
            putInt(KEY_DIM_START_HOUR, newSettings.dimStartHour)
            putInt(KEY_DIM_START_MINUTE, newSettings.dimStartMinute)
            putInt(KEY_DIM_END_HOUR, newSettings.dimEndHour)
            putInt(KEY_DIM_END_MINUTE, newSettings.dimEndMinute)
            putFloat(KEY_NORMAL_BRIGHTNESS, newSettings.normalBrightness)
            putFloat(KEY_DIMMED_BRIGHTNESS, newSettings.dimmedBrightness)
            apply()
        }
        _settings.value = newSettings
    }

    companion object {
        private const val KEY_URL = "url"
        private const val KEY_DIMMING_ENABLED = "dimming_enabled"
        private const val KEY_DIM_START_HOUR = "dim_start_hour"
        private const val KEY_DIM_START_MINUTE = "dim_start_minute"
        private const val KEY_DIM_END_HOUR = "dim_end_hour"
        private const val KEY_DIM_END_MINUTE = "dim_end_minute"
        private const val KEY_NORMAL_BRIGHTNESS = "normal_brightness"
        private const val KEY_DIMMED_BRIGHTNESS = "dimmed_brightness"
    }
}
