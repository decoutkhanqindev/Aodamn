package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.withRotation
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import kotlin.math.cos
import kotlin.math.sin

// Bút vẽ số trên các vòng của La bàn bằng Paint của Android: căn giữa theo nét chữ (getTextBounds) và xoay quanh tâm nhãn như app mẫu. Typeface là
// file TTF tĩnh Krona One: Paint không đặt trục độ đậm nào (máy Xiaomi thay font khi có trục). Màu gradient là shader theo khung nét chữ của nhãn,
// xoay cùng nhãn nên mỗi nhãn đọc theo cùng một hướng gradient.
@Stable
class CompassPaints(typeface: Typeface) {
    private val text = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.typeface = typeface
        isSubpixelText = true
    }
    private val bounds = Rect()

    fun drawLabel(
        scope: DrawScope,
        label: String,
        center: Offset,
        size: Float,
        rotation: Float,
        color: Color,
        gradient: AodGradient?,
    ) {
        text.textSize = size
        text.color = color.toArgb()
        text.getTextBounds(label, 0, label.length, bounds)
        val x = center.x - bounds.exactCenterX()
        val y = center.y - bounds.exactCenterY()
        text.shader = gradient?.toShader(x + bounds.left, y + bounds.top, x + bounds.right, y + bounds.bottom)
        scope.drawIntoCanvas { canvas ->
            canvas.nativeCanvas.withRotation(rotation, center.x, center.y) {
                drawText(label, x, y, text)
            }
        }
    }
}

// Các vòng vẽ ở thời điểm 0 giờ 0 phút 0 giây quanh tâm vùng vẽ; mặt số xoay cả vòng theo giờ thật (xoay cứng nên vị trí và hướng chữ cùng đổi).
// Góc theo app mẫu: điểm ở góc θ là (sin θ, cos θ) · r tính từ tâm (θ = 90° là đường chỉ bên phải), chữ xoay 90 − θ nên đọc theo bán kính ra ngoài.
fun DrawScope.drawCompassHourRing(
    paints: CompassPaints,
    unit: Float,
    hourColor: Color,
    hourGradient: AodGradient?,
    halfHourColor: Color,
    halfHourGradient: AodGradient?,
) {
    for (index in 0 until 24) {
        val degrees = CompassMetrics.HOUR_START + 15f * index
        val hour = index / 2 + 1
        val center = ringPoint(degrees, CompassMetrics.HOUR_RADIUS * unit)
        if (index % 2 == 0) {
            paints.drawLabel(this, hour.toString(), center, CompassMetrics.HOUR_SIZE * unit, 90f - degrees, hourColor, hourGradient)
        } else {
            val label = "${hour.toString().padStart(2, '0')}:30"
            paints.drawLabel(this, label, center, CompassMetrics.HALF_HOUR_SIZE * unit, 90f - degrees, halfHourColor, halfHourGradient)
        }
    }
}

// Vạch phút tô theo khung cả vòng nên gradient đổi màu theo chỗ quanh vòng và xoay cùng vòng.
fun DrawScope.drawCompassMinuteRing(paints: CompassPaints, unit: Float, color: Color, gradient: AodGradient?, tickWidth: Float) {
    val tickBrush = gradient?.toBrush() ?: SolidColor(color)
    for (index in 0 until 24) {
        val degrees = 90f + 15f * index
        if (index % 2 == 0) {
            val center = ringPoint(degrees, CompassMetrics.MINUTE_RADIUS * unit)
            paints.drawLabel(this, minuteLabel(index), center, CompassMetrics.MINUTE_SIZE * unit, 90f - degrees, color, gradient)
        } else {
            drawLine(
                brush = tickBrush,
                start = ringPoint(degrees, CompassMetrics.TICK_START * unit),
                end = ringPoint(degrees, CompassMetrics.TICK_END * unit),
                strokeWidth = tickWidth,
                cap = StrokeCap.Round,
                alpha = CompassMetrics.TICK_ALPHA,
            )
        }
    }
}

fun DrawScope.drawCompassSecondRing(paints: CompassPaints, unit: Float, color: Color, gradient: AodGradient?) {
    for (index in 0 until 24 step 2) {
        val degrees = 90f + 15f * index
        val center = ringPoint(degrees, CompassMetrics.SECOND_RADIUS * unit)
        paints.drawLabel(this, minuteLabel(index), center, CompassMetrics.SECOND_SIZE * unit, 90f - degrees, color, gradient)
    }
}

// Nhãn 00, 05 … 55 ở vị trí chẵn (cách nhau 30°).
private fun minuteLabel(index: Int): String = (index * 5 / 2).toString().padStart(2, '0')

private fun DrawScope.ringPoint(degrees: Float, radius: Float): Offset {
    val radians = Math.toRadians(degrees.toDouble())
    return Offset(center.x + sin(radians).toFloat() * radius, center.y + cos(radians).toFloat() * radius)
}
