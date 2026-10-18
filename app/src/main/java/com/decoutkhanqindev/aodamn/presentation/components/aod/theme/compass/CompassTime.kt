package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass

import java.util.TimeZone

// Giờ địa phương (múi giờ của máy, có giờ mùa hè) tách thành giây / phút / giờ có phần lẻ như AmbitioClock của app mẫu: giây có phần nghìn giây,
// phút có phần giây, giờ 12 tiếng có phần phút, nên ba vòng trôi liên tục chứ không nhảy từng nấc.
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

        // Kiểu không vòng giây đổi mặt số đúng đầu mỗi phút như app mẫu (múi giờ lệch theo phút chẵn nên đầu phút giờ máy = đầu phút UTC).
        fun millisToNextMinute(epochMillis: Long): Long = MINUTE_MILLIS - Math.floorMod(epochMillis, MINUTE_MILLIS)
    }
}
