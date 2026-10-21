package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import java.util.TimeZone
import kotlin.math.cos
import kotlin.math.sin

// Góc kim theo giờ địa phương như setTime của Android12Clock2: giây có phần nghìn giây, phút có phần giây, giờ có phần phút nên kim và chấm
// giây trôi liên tục. Góc tính theo chiều kim đồng hồ từ 12 giờ.
class RippleTime(epochMillis: Long) {
    private val dayMillis = Math.floorMod(epochMillis + TimeZone.getDefault().getOffset(epochMillis), DAY_MILLIS)
    private val seconds = dayMillis % MINUTE_MILLIS / 1000f
    private val minutes = dayMillis % HOUR_MILLIS / MINUTE_MILLIS.toFloat()
    private val hours = dayMillis % HALF_DAY_MILLIS / HOUR_MILLIS.toFloat()

    val secondDegrees: Float = seconds * 6f
    val minuteDegrees: Float = minutes * 6f
    val hourDegrees: Float = hours * 30f

    private companion object {
        const val MINUTE_MILLIS = 60_000L
        const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        const val HALF_DAY_MILLIS = 12 * HOUR_MILLIS
        const val DAY_MILLIS = 24 * HOUR_MILLIS
    }
}

// Điểm cách tâm radius theo góc degrees (chiều kim đồng hồ từ 12 giờ).
fun ripplePoint(center: Offset, degrees: Float, radius: Float): Offset {
    val radians = Math.toRadians(degrees.toDouble())
    return Offset(center.x + sin(radians).toFloat() * radius, center.y - cos(radians).toFloat() * radius)
}

// Viền 12 gợn như Android12ClockBg2: đi từ 12 giờ, mỗi 15° một đường cong bậc hai, điểm điều khiển ở giữa đoạn, lần lượt ra ngoài / vào trong
// WAVE_DEPTH bán kính.
fun rippleFacePath(center: Offset, radius: Float): Path = Path().apply {
    val start = ripplePoint(center, 0f, radius)
    moveTo(start.x, start.y)
    var sign = 1
    for (degrees in 0 until 360 step RippleMetrics.WAVE_STEP_DEGREES) {
        val control = ripplePoint(
            center = center,
            degrees = degrees + RippleMetrics.WAVE_STEP_DEGREES / 2f,
            radius = radius * (1f + sign * RippleMetrics.WAVE_DEPTH),
        )
        val end = ripplePoint(center, (degrees + RippleMetrics.WAVE_STEP_DEGREES).toFloat(), radius)
        quadraticTo(control.x, control.y, end.x, end.y)
        sign = -sign
    }
    close()
}
