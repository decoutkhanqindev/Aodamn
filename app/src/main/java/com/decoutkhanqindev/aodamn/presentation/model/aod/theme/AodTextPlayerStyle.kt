package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Immutable
data class AodTextPlayerStyle(
    val fontFamily: FontFamily,
    val titleSize: TextUnit,
    val artistSize: TextUnit,
    val artistGap: Dp,
    val buttonSize: Dp,
    val buttonIconSize: Dp,
    val pausedAlpha: Float,
    val textGap: Dp = 0.dp,
    val titleWeight: FontWeight? = null,
    val artistWeight: FontWeight? = null,
    val letterSpacing: TextUnit = TextUnit.Unspecified,
    val artistAlpha: Float = 1f,
    val buttonAlpha: Float = 1f,
    val isUppercase: Boolean = false,
)
