package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.blinky

import androidx.compose.ui.unit.Dp

object BlinkyMetrics {
    const val REFERENCE_SHORT_SIDE = 393f
    const val OPENNESS = 0.7f
    const val CLOSED_OPENNESS = 0.05f
    const val EYE_WIDTH = 3f
    const val EYE_HEIGHT = 0.8f
    const val EYE_CORNER = 0.1f
    const val PORTRAIT_EYES_TOP = 0.3093f
    const val PORTRAIT_CLOCK_TOP = 0.516f
    const val PORTRAIT_HEADER_TOP = 0.0825f
    const val PORTRAIT_STATUS_TOP = 0.779f
    const val PORTRAIT_LATEST_TOP = 0.835f
    const val PORTRAIT_MEDIA_TOP = 0.88f
    const val LANDSCAPE_EYES_LEFT = 0.0641f
    const val LANDSCAPE_EYES_TOP = 0.2824f
    const val LANDSCAPE_CLOCK_LEFT = 0.236f
    const val LANDSCAPE_CLOCK_TOP = 0.636f
    const val LANDSCAPE_DETAILS_LEFT = 0.5842f
    const val LANDSCAPE_HEADER_TOP = 0.229f
    const val LANDSCAPE_STATUS_TOP = 0.34f
    const val LANDSCAPE_LATEST_TOP = 0.47f
    const val LANDSCAPE_MEDIA_TOP = 0.61f
    const val CLOCK_SCALE = 1.3f
    const val BLINK_MILLIS = 300
    const val INITIAL_DELAY_MILLIS = 2_000L
    const val NIGHT_DELAY_MIN_SECONDS = 11
    const val NIGHT_DELAY_MAX_SECONDS = 60
    const val DAY_DELAY_MIN_SECONDS = 60
    const val DAY_DELAY_MAX_SECONDS = 359

    fun eyeSize(shortSide: Dp, option: Int): Dp = shortSide * (option / REFERENCE_SHORT_SIDE)

    fun clockSize(shortSide: Dp, option: Int): Float = shortSide.value * option / REFERENCE_SHORT_SIDE * CLOCK_SCALE
}
