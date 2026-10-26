package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.halfmoon

import java.util.TimeZone

class HalfMoonTime(epochMillis: Long) {
    private val dayMillis = Math.floorMod(epochMillis + TimeZone.getDefault().getOffset(epochMillis), DAY_MILLIS)
    private val hourOfDay = (dayMillis / HOUR_MILLIS).toInt()

    val seconds: Float = dayMillis % MINUTE_MILLIS / 1000f
    val minutes: Float = dayMillis % HOUR_MILLIS / MINUTE_MILLIS.toFloat()
    val minuteText: String = (dayMillis % HOUR_MILLIS / MINUTE_MILLIS).toString().padStart(2, '0')

    fun hourText(is24Hour: Boolean): String = (if (is24Hour) hourOfDay else (hourOfDay + 11) % 12 + 1).toString().padStart(2, '0')

    companion object {
        private const val MINUTE_MILLIS = 60_000L
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS

        val Labels: List<String> = (0 until 60 step 5).map { value -> value.toString().padStart(2, '0') }
    }
}
