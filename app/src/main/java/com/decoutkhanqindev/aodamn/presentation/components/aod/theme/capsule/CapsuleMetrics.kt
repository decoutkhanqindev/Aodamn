package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.capsule

import androidx.compose.ui.unit.Dp

object CapsuleMetrics {
    const val LONG_SIDE_RATIO = 0.0545f
    const val SHORT_SIDE_RATIO = 0.1212f

    const val CAPSULE_WIDTH = 4.76f
    const val CAPSULE_HEIGHT = 1.81f
    const val OUTLINE = 0.043f
    const val FILL_ALPHA = 0.12f
    const val DIGIT_FONT = 1.3f
    const val DIGIT_PITCH = 0.856f
    const val COLON_WIDTH = 0.434f
    const val COLON_DOT = 0.26f
    const val COLON_OFFSET = 0.22f

    const val DATE_FONT = 0.323f
    const val DATE_TOP = 0.37f

    const val BLOCKS_TOP = 0.4f
    const val STATUS_GLYPH = 0.327f
    const val STATUS_TOP = 0.84f
    const val LANDSCAPE_STATUS_TOP = 0.6f
    const val STATUS_GAP_SCALE = 0.77f

    fun unit(width: Dp, height: Dp, scale: Float): Dp =
        minOf(maxOf(width, height) * LONG_SIDE_RATIO, minOf(width, height) * SHORT_SIDE_RATIO) * scale
}
