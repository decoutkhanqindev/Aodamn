package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Immutable
data class AodLatestLineStyle(
    val font: FontFamily,
    val iconSize: Dp,
    val iconGap: Dp,
    val textSize: TextUnit,
    val letterSpacing: TextUnit = TextUnit.Unspecified,
    val isUppercase: Boolean = false,
)
