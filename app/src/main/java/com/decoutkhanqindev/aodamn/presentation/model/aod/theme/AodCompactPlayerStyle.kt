package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Immutable
data class AodCompactPlayerStyle(
    val font: FontFamily,
    val titleSize: TextUnit,
    val artistSize: TextUnit,
    val artistGap: Dp,
    val artistDarken: Float,
    val playButton: Dp,
    val playIcon: Dp,
    val nextButton: Dp,
    val nextIcon: Dp,
    val progressGap: Dp,
    val trackHeight: Dp,
    val trackAlpha: Float,
    val letterSpacing: TextUnit = TextUnit.Unspecified,
    val playStartGap: Dp = 0.dp,
    val playEndGap: Dp = 0.dp,
    val nextStartGap: Dp = 0.dp,
    val artistWeight: FontWeight? = null,
    val isProgressShown: Boolean = true,
    val previousButton: Dp? = null,
    val previousIcon: Dp = 0.dp,
    val isProgressOnTop: Boolean = false,
    val isUppercase: Boolean = false,
)
