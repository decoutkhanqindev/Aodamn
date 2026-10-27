package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.quarters

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.theme.Black

fun AodThemeConfig.quartersSecondColorsOf(option: AodColorOption): List<Color> =
    colorsOf(option).map { color -> lerp(color, Black, QuartersMetrics.SECOND_DARKEN) }
