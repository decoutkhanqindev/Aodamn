package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Immutable
data class AodBadgeLatestStyle(
    val font: FontFamily,
    val badgeSize: Dp,
    val iconSize: Dp,
    val textStartGap: Dp,
    val titleSize: TextUnit,
    val textSize: TextUnit,
    val textGap: Dp,
    val textDarken: Float,
    val textMaxLines: Int,
    val textWeight: FontWeight? = null,
    val isTitleMarquee: Boolean = false,
    val isTextShrinkable: Boolean = false,
)
