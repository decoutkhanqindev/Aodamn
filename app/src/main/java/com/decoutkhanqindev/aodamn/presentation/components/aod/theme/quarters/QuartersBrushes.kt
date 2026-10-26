package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.quarters

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.lerp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlinx.collections.immutable.toImmutableList

fun AodThemeConfig.quartersDimBrushOf(option: AodColorOption): Brush {
    val gradient = gradientOf(option) ?: return SolidColor(lerp(get(option), Black, QuartersMetrics.TEXT_DARKEN))
    return gradient.copy(colors = gradient.colors.map { color -> lerp(color, Black, QuartersMetrics.TEXT_DARKEN) }.toImmutableList()).toBrush()
}

fun AodThemeConfig.quartersSecondColorsOf(option: AodColorOption): List<Color> =
    colorsOf(option).map { color -> lerp(color, Black, QuartersMetrics.SECOND_DARKEN) }
