package org.mcfish.aquarium.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mcfish.aquarium.data.KioskSettings
import java.time.LocalTime

class KioskViewModelTest {

    @Test
    fun testSameDayDimmingPeriod() {
        // Dimming from 08:30 to 17:30
        val startHour = 8
        val startMinute = 30
        val endHour = 17
        val endMinute = 30

        // Before start
        assertFalse(
            KioskViewModel.isTimeInDimmingPeriod(8, 29, startHour, startMinute, endHour, endMinute)
        )
        // Exactly at start
        assertTrue(
            KioskViewModel.isTimeInDimmingPeriod(8, 30, startHour, startMinute, endHour, endMinute)
        )
        // Mid-day
        assertTrue(
            KioskViewModel.isTimeInDimmingPeriod(12, 0, startHour, startMinute, endHour, endMinute)
        )
        // Just before end
        assertTrue(
            KioskViewModel.isTimeInDimmingPeriod(17, 29, startHour, startMinute, endHour, endMinute)
        )
        // Exactly at end
        assertFalse(
            KioskViewModel.isTimeInDimmingPeriod(17, 30, startHour, startMinute, endHour, endMinute)
        )
    }

    @Test
    fun testOvernightDimmingPeriod() {
        // Dimming from 22:00 to 07:00 (spans midnight)
        val startHour = 22
        val startMinute = 0
        val endHour = 7
        val endMinute = 0

        // Late evening before start (21:59)
        assertFalse(
            KioskViewModel.isTimeInDimmingPeriod(21, 59, startHour, startMinute, endHour, endMinute)
        )
        // Exactly at start (22:00)
        assertTrue(
            KioskViewModel.isTimeInDimmingPeriod(22, 0, startHour, startMinute, endHour, endMinute)
        )
        // Midnight (00:00)
        assertTrue(
            KioskViewModel.isTimeInDimmingPeriod(0, 0, startHour, startMinute, endHour, endMinute)
        )
        // Early morning (06:59)
        assertTrue(
            KioskViewModel.isTimeInDimmingPeriod(6, 59, startHour, startMinute, endHour, endMinute)
        )
        // At end time (07:00)
        assertFalse(
            KioskViewModel.isTimeInDimmingPeriod(7, 0, startHour, startMinute, endHour, endMinute)
        )
        // Afternoon (14:00)
        assertFalse(
            KioskViewModel.isTimeInDimmingPeriod(14, 0, startHour, startMinute, endHour, endMinute)
        )
    }

    @Test
    fun testEqualStartAndEndTime() {
        // Same start and end time -> no dimming period
        assertFalse(
            KioskViewModel.isTimeInDimmingPeriod(12, 0, 10, 0, 10, 0)
        )
    }

    @Test
    fun testIsDimmedAtRespectsSchedule() {
        val settings = KioskSettings() // 22:00 to 07:00
        assertTrue(KioskViewModel.isDimmedAt(settings, LocalTime.of(23, 0)))
        assertFalse(KioskViewModel.isDimmedAt(settings, LocalTime.of(12, 0)))
        assertFalse(KioskViewModel.isDimmedAt(settings.copy(dimmingEnabled = false), LocalTime.of(23, 0)))
    }

    @Test
    fun testBrightnessFor() {
        val settings = KioskSettings(normalBrightness = 0.8f, dimmedBrightness = 0.05f)
        assertEquals(0.05f, KioskViewModel.brightnessFor(settings, dimmed = true), 0f)
        assertEquals(0.8f, KioskViewModel.brightnessFor(settings, dimmed = false), 0f)
    }
}
