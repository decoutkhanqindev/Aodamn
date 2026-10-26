package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bigoverlap

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.lerp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlinx.collections.immutable.toImmutableList

// Bút tô một chữ số: gradient trải theo khung của chính số đó, mỗi màu qua transform (nét đen / ruột trong suốt khi đổi kiểu đặc ↔ viền).
fun AodThemeConfig.digitBrushOf(option: AodColorOption, bounds: Rect, transform: (Color) -> Color): Brush {
    val gradient = gradientOf(option) ?: return SolidColor(transform(get(option)))
    return gradient.copy(colors = gradient.colors.map(transform).toImmutableList()).toBrush(bounds)
}

// Chữ phụ tối hơn 30% như app mẫu (nghệ sĩ, nội dung thông báo); gradient thì tối từng màu, giữ hướng.
fun AodThemeConfig.dimBrushOf(option: AodColorOption): Brush {
    val gradient = gradientOf(option) ?: return SolidColor(lerp(get(option), Black, BigOverlapMetrics.TEXT_DARKEN))
    return gradient.copy(colors = gradient.colors.map { color -> lerp(color, Black, BigOverlapMetrics.TEXT_DARKEN) }.toImmutableList()).toBrush()
}
