package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.flip

import androidx.compose.animation.core.Easing
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import kotlin.math.PI
import kotlin.math.cos

object FlipMetrics {
    const val PORTRAIT_WIDTH = 0.8f
    const val PORTRAIT_HEIGHT = 0.4f
    const val LANDSCAPE_WIDTH = 0.4f
    const val LANDSCAPE_HEIGHT = 0.8f
    const val TEXT_SCALE = 1.6f
    const val OUTLINE = 0.028f
    const val FLAP_SHADE = 0.24f
    const val ANIMATION_MILLIS = 400
    const val MINUTE_MILLIS = 60_000L
    const val TICK_SLACK_MILLIS = 50L

    val CardGap = 25.dp
    val PieceGap = 4.dp
    val OuterCorner = 20.dp
    val InnerCorner = 2.dp
    val BlockMargin = 8.dp
    val WeatherGap = 14.dp
    val StatusGap = 16.dp
    val FlipEasing = Easing { fraction -> (cos((fraction + 1f) * PI) / 2.0 + 0.5).toFloat() }

    fun cardSize(width: Dp, height: Dp, size: Int, isLandscape: Boolean): Dp =
        min(
            width * if (isLandscape) LANDSCAPE_WIDTH else PORTRAIT_WIDTH,
            height * if (isLandscape) LANDSCAPE_HEIGHT else PORTRAIT_HEIGHT,
        ) * (size / 100f)

    fun textSize(card: Float, characterSize: Int, styleScale: Float): Float =
        card * TEXT_SCALE * styleScale * characterSize / 100f

    fun outerCorner(cardSize: Dp): Dp = min(OuterCorner, cardSize / 8)

    fun innerCorner(cardSize: Dp): Dp = min(InnerCorner, cardSize / 50)
}
