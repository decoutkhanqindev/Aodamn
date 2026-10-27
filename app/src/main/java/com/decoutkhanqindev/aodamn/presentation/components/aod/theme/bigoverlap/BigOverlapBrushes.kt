package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bigoverlap

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import kotlinx.collections.immutable.toImmutableList

fun AodThemeConfig.digitBrushOf(option: AodColorOption, bounds: Rect, transform: (Color) -> Color): Brush {
    val gradient = gradientOf(option) ?: return SolidColor(transform(get(option)))
    return gradient.copy(colors = gradient.colors.map(transform).toImmutableList()).toBrush(bounds)
}
