package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bezel

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.Typeface
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.theme.BlackAlpha30

// Bút vẽ của Vành bằng Paint của Android: số căn giữa theo nét chữ (getTextBounds) như app mẫu, và kim có bóng mờ (setShadowLayer, Compose chưa
// có bóng cho nét). Typeface là file TTF tĩnh của Zalando Sans Expanded: Paint không đặt trục độ đậm nào (máy Xiaomi thay font khi có trục).
// Màu gradient là shader: số tô theo khung nét chữ của chính số đó theo hướng đã chọn, kim tô dọc theo kim (from → to) như Cổ điển.
@Stable
class BezelPaints(typeface: Typeface) {
    private val number = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.typeface = typeface
        isSubpixelText = true
    }
    private val hand = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private val bounds = Rect()
    private val shadowColor = BlackAlpha30.toArgb()

    fun drawNumber(canvas: Canvas, text: String, center: Offset, size: Float, color: Color, gradient: AodGradient?) {
        number.textSize = size
        number.color = color.toArgb()
        number.getTextBounds(text, 0, text.length, bounds)
        val x = center.x - bounds.exactCenterX()
        val y = center.y - bounds.exactCenterY()
        number.shader = gradient?.let {
            val left = x + bounds.left
            val top = y + bounds.top
            val right = x + bounds.right
            val bottom = y + bounds.bottom
            val direction = gradient.direction
            LinearGradient(
                if (direction.start.x.isInfinite()) right else left + direction.start.x,
                if (direction.start.y.isInfinite()) bottom else top + direction.start.y,
                if (direction.end.x.isInfinite()) right else left + direction.end.x,
                if (direction.end.y.isInfinite()) bottom else top + direction.end.y,
                gradient.colors.toArgbArray(),
                null,
                Shader.TileMode.CLAMP,
            )
        }
        canvas.drawText(text, x, y, number)
    }

    fun drawHand(
        canvas: Canvas,
        start: Offset,
        end: Offset,
        width: Float,
        shadowRadius: Float,
        colors: List<Color>,
        from: Offset,
        to: Offset,
    ) {
        hand.strokeWidth = width
        hand.color = colors.first().toArgb()
        hand.shader = if (colors.size > 1) {
            LinearGradient(from.x, from.y, to.x, to.y, colors.toArgbArray(), null, Shader.TileMode.CLAMP)
        } else {
            null
        }
        hand.setShadowLayer(shadowRadius, 0f, 0f, shadowColor)
        canvas.drawLine(start.x, start.y, end.x, end.y, hand)
    }

    private fun List<Color>.toArgbArray(): IntArray = IntArray(size) { index -> this[index].toArgb() }
}
