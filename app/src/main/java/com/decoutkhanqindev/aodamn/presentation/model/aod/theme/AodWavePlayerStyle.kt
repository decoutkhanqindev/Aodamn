package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Immutable
data class AodWavePlayerStyle(
    val width: Dp,
    val titleFont: FontFamily,
    val artistFont: FontFamily,
    val titleSize: TextUnit,
    val artistSize: TextUnit,
    val artistGap: Dp,
    val artistAlpha: Float,
    val buttonSize: Dp,
    val buttonIconSize: Dp,
    val pausedAlpha: Float,
    val trackHeight: Dp,
    val trackAlpha: Float,
    val progressGap: Dp,
    val textWeight: FontWeight? = null,
    val titleSpacing: TextUnit = TextUnit.Unspecified,
    val artistSpacing: TextUnit = TextUnit.Unspecified,
    val isUppercase: Boolean = true,
    val buttonOuterGap: Dp = 0.dp,
    val buttonInnerGap: Dp = 0.dp,
    val progressInset: Dp = 0.dp,
)
