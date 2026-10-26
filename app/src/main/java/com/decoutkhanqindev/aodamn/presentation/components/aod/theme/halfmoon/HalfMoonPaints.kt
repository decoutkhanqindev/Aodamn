package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.halfmoon

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Stable
class HalfMoonPaints(typeface: Typeface) {
    private val text = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.typeface = typeface
        isSubpixelText = true
    }
    private val bounds = Rect()

    fun textHeight(label: String, size: Float): Float {
        text.textSize = size
        text.getTextBounds(label, 0, label.length, bounds)
        return bounds.height().toFloat()
    }

    fun drawLabel(
        scope: DrawScope,
        label: String,
        center: Offset,
        size: Float,
        color: Color,
        alpha: Float = 1f,
        gradient: AodGradient? = null,
    ) {
        text.textSize = size
        text.color = color.toArgb()
        text.alpha = (color.alpha * alpha * 255f).roundToInt()
        text.getTextBounds(label, 0, label.length, bounds)
        val x = center.x - bounds.exactCenterX()
        val y = center.y - bounds.exactCenterY()
        text.shader = gradient?.toShader(x + bounds.left, y + bounds.top, x + bounds.right, y + bounds.bottom)
        scope.drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawText(label, x, y, text)
        }
    }
}

fun halfMoonPoint(center: Offset, degrees: Float, radius: Float): Offset {
    val radians = Math.toRadians(degrees.toDouble())
    return Offset(center.x + sin(radians).toFloat() * radius, center.y + cos(radians).toFloat() * radius)
}

fun DrawScope.drawHalfMoonTicks(radius: Float, major: Float, minor: Float, end: Float, brush: Brush, width: Float) {
    for (index in 0 until 60) {
        val degrees = 90f + 6f * index
        val start = (if (index % 5 == 0) major else minor) * radius
        drawLine(
            brush = brush,
            start = halfMoonPoint(center, degrees, start),
            end = halfMoonPoint(center, degrees, end * radius),
            strokeWidth = width,
            cap = StrokeCap.Round,
        )
    }
}

fun DrawScope.drawHalfMoonLabels(
    paints: HalfMoonPaints,
    center: Offset,
    value: Float,
    radius: Float,
    size: Float,
    color: Color,
    alpha: Float = 1f,
    gradient: AodGradient? = null,
) {
    HalfMoonTime.Labels.forEachIndexed { index, label ->
        val degrees = 90f - 6f * value + 30f * index
        paints.drawLabel(this, label, halfMoonPoint(center, degrees, radius), size, color, alpha, gradient)
    }
}
