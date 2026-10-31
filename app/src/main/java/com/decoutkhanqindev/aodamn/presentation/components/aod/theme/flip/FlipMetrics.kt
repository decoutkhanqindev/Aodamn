package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.flip

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min

object FlipMetrics {
    const val PORTRAIT_WIDTH = 0.8f
    const val PORTRAIT_HEIGHT = 0.4f
    const val LANDSCAPE_WIDTH = 0.4f
    const val LANDSCAPE_HEIGHT = 0.8f
    const val CHARACTER_DEFAULT = 55f
    const val DEFAULT_STYLE_SCALE = 1.05f
    const val TEXT_SIZE = 1.12f
    const val OUTER_OUTLINE = 0.038f
    const val INNER_OUTLINE = 0.016f
    const val FLAP_SHADE = 0.24f
    const val ANIMATION_MILLIS = 400
    const val MINUTE_MILLIS = 60_000L
    const val TICK_SLACK_MILLIS = 50L

    val CardGap = 25.dp
    val PieceGap = 4.dp
    val OuterCorner = 20.dp
    val InnerCorner = 2.dp
    val DetailsGap = 4.dp

    fun cardSize(width: Dp, height: Dp, size: Int, isLandscape: Boolean): Dp =
        min(
            width * if (isLandscape) LANDSCAPE_WIDTH else PORTRAIT_WIDTH,
            height * if (isLandscape) LANDSCAPE_HEIGHT else PORTRAIT_HEIGHT,
        ) * (size / 100f)

    fun outerCorner(cardSize: Dp): Dp = min(OuterCorner, cardSize / 10)

    fun innerCorner(cardSize: Dp): Dp = min(InnerCorner, cardSize / 50)
}