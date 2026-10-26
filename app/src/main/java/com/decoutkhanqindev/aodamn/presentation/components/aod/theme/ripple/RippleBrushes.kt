package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.lerp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlinx.collections.immutable.toImmutableList

fun AodThemeConfig.darkerBrushOf(option: AodColorOption): Brush {
    val gradient = gradientOf(option) ?: return SolidColor(lerp(get(option), Black, RippleMetrics.TEXT_DARKEN))
    return gradient.copy(colors = gradient.colors.map { color -> lerp(color, Black, RippleMetrics.TEXT_DARKEN) }.toImmutableList()).toBrush()
}
