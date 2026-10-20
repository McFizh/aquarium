package org.mcfish.aquarium.data

data class KioskSettings(
    val url: String = "https://example.com",
    val dimmingEnabled: Boolean = true,
    val dimStartHour: Int = 22,
    val dimStartMinute: Int = 0,
    val dimEndHour: Int = 7,
    val dimEndMinute: Int = 0,
    val normalBrightness: Float = 1.0f,
    val dimmedBrightness: Float = 0.1f
)
