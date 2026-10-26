package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tower

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object TowerMetrics {
    const val SHORT_SIDE_RATIO = 0.96f
    const val LANDSCAPE_LONG_SIDE_RATIO = 0.3f

    const val CONDENSE = 0.312f
    const val PITCH = 0.215f
    const val INK = 0.193f
    const val PAIR_GAP = 0.0265f
    const val MINUTE_LEFT = 2f * PITCH + PAIR_GAP - (PITCH - INK)
    const val CLOCK_WIDTH = MINUTE_LEFT + 2f * PITCH
    const val STROKE = 0.0065f
    val MinStroke = 0.6.dp
    const val DIGIT_CHANGE_MILLIS = 400

    const val FADE_EDGE_ALPHA = 15f / 255f
    const val FADE_QUARTER_ALPHA = 60f / 255f

    const val TOP_RATIO = 0.06f
    const val DATE_GAP = 0.0796f
    const val DATE_FONT = 0.0345f
    const val DATE_TRACKING = 0.48f
    const val BOTTOM_WEIGHT = 0.8f
    const val BLOCKS_TOP = 0.064f
    const val STATUS_GLYPH = 0.0398f
    const val STATUS_TOP = 0.074f
    const val MIN_SCALE = 0.6f
    const val MAX_WIDTH_RATIO = 0.92f

    const val LANDSCAPE_DATE_GAP = 0.0797f
    const val LANDSCAPE_DATE_FONT = 0.0478f
    const val LANDSCAPE_STATUS_TOP = 0.1036f
    const val LANDSCAPE_STATUS_GLYPH = 0.0598f
    const val LANDSCAPE_HEIGHT_FILL = 0.9f
    const val LANDSCAPE_COLUMN_HEIGHT = 1f + LANDSCAPE_DATE_GAP + LANDSCAPE_DATE_FONT + LANDSCAPE_STATUS_TOP + LANDSCAPE_STATUS_GLYPH

    const val HEAD_HEIGHT = 1f + DATE_GAP + DATE_FONT
    const val BOTTOM_FIXED = BLOCKS_TOP + STATUS_TOP + STATUS_GLYPH

    fun unit(width: Dp, height: Dp, scale: Float): Dp = minOf(width, height) * SHORT_SIDE_RATIO * scale

    fun landscapeUnit(width: Dp, height: Dp, scale: Float): Dp =
        minOf(maxOf(width, height) * LANDSCAPE_LONG_SIDE_RATIO, minOf(width, height)) * SHORT_SIDE_RATIO * scale
}
