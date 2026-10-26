package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass

import java.util.TimeZone

class CompassTime(epochMillis: Long) {
    private val dayMillis = Math.floorMod(epochMillis + TimeZone.getDefault().getOffset(epochMillis), DAY_MILLIS)

    val seconds: Float = dayMillis % MINUTE_MILLIS / 1000f
    val minutes: Float = dayMillis % HOUR_MILLIS / MINUTE_MILLIS.toFloat()
    val hours: Float = dayMillis % HALF_DAY_MILLIS / HOUR_MILLIS.toFloat()

    companion object {
        private const val MINUTE_MILLIS = 60_000L
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val HALF_DAY_MILLIS = 12 * HOUR_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS

        fun millisToNextMinute(epochMillis: Long): Long = MINUTE_MILLIS - Math.floorMod(epochMillis, MINUTE_MILLIS)
    }
}
