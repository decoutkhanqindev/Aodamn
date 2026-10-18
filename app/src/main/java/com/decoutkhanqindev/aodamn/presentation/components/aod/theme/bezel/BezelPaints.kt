package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bezel

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.decoutkhanqindev.aodamn.presentation.theme.BlackAlpha30

// Bút vẽ của Vành bằng Paint của Android: số căn giữa theo nét chữ (getTextBounds) như app mẫu, và kim có bóng mờ (setShadowLayer, Compose chưa
// có bóng cho nét). Typeface là file TTF tĩnh của Zalando Sans Expanded: Paint không đặt trục độ đậm nào (máy Xiaomi thay font khi có trục).
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

    fun drawNumber(canvas: Canvas, text: String, center: Offset, size: Float, color: Color) {
        number.textSize = size
        number.color = color.toArgb()
        number.getTextBounds(text, 0, text.length, bounds)
        canvas.drawText(text, center.x - bounds.exactCenterX(), center.y - bounds.exactCenterY(), number)
    }

    fun drawHand(canvas: Canvas, start: Offset, end: Offset, width: Float, shadowRadius: Float, color: Color) {
        hand.strokeWidth = width
        hand.color = color.toArgb()
        hand.setShadowLayer(shadowRadius, 0f, 0f, shadowColor)
        canvas.drawLine(start.x, start.y, end.x, end.y, hand)
    }
}
