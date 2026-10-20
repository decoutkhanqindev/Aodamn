package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Rect

// Đo nét chữ của giờ bằng Paint của Android (getTextBounds) như app mẫu để căn giờ theo nét chữ, không theo khung dòng. Typeface là file TTF
// tĩnh Google Sans Regular: Paint không đặt trục độ đậm nào (máy Xiaomi thay font khi có trục).
@Stable
class GradientLinePaints(typeface: Typeface) {
    private val text = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.typeface = typeface
        isSubpixelText = true
    }
    private val bounds = android.graphics.Rect()

    // Khung nét chữ so với gốc chữ (y âm phía trên đường gốc).
    fun inkBounds(label: String, size: Float): Rect {
        text.textSize = size
        text.getTextBounds(label, 0, label.length, bounds)
        return Rect(bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat(), bounds.bottom.toFloat())
    }
}
