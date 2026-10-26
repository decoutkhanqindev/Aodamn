package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.card

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object CardMetrics {
    const val SHORT_SIDE_RATIO = 0.4835f
    const val LANDSCAPE_SCALE = 0.9f

    const val CONDENSE = 0.55f
    const val PITCH = 0.358f
    const val INK = 0.284f
    const val OUTLINE = 0.01f
    val MinStroke = 0.6.dp
    const val DIGIT_CHANGE_MILLIS = 400

    const val PADDING = 0.15f
    const val CORNER = 0.21f
    const val CARD_FILL_ALPHA = 0.118f
    const val CARD_STROKE = 0.013f
    const val CARD_STROKE_ALPHA = 0.235f

    const val COLUMN_GAP = 0.073f
    const val TAG_WIDTH = 0.33f
    const val MINUTE = 0.5f
    const val TAG_TOP = 0.58f
    const val TAG_CORNER = 0.079f
    const val TAG_TEXT = 0.207f
    const val TAG_FILL_ALPHA = 0.196f
    const val TAG_STROKE = 0.0105f
    const val TAG_STROKE_ALPHA = 0.31f

    const val HOUR_LEFT = PADDING - (PITCH - INK) / 2f
    const val COLUMN_LEFT = PADDING + PITCH + INK + COLUMN_GAP
    const val MINUTE_LEFT = COLUMN_LEFT + (TAG_WIDTH - (PITCH + INK) * MINUTE) / 2f - (PITCH - INK) / 2f * MINUTE
    const val CARD_WIDTH = COLUMN_LEFT + TAG_WIDTH + PADDING
    const val CARD_HEIGHT = 1f + 2f * PADDING

    const val DATE_FONT = 0.0792f
    const val DATE_TOP = 0.108f

    const val BLOCKS_TOP = 0.08f
    const val STATUS_GLYPH = 0.082f
    const val STATUS_TOP = 0.21f
    const val LANDSCAPE_STATUS_TOP = 0.15f
    const val STATUS_GAP_SCALE = 0.77f
    const val HEIGHT_FILL = 0.92f
    const val MIN_SCALE = 0.55f

    const val COLUMN_HEIGHT = CARD_HEIGHT + DATE_TOP + DATE_FONT + BLOCKS_TOP + STATUS_TOP + STATUS_GLYPH
    const val LANDSCAPE_COLUMN_HEIGHT = CARD_HEIGHT + DATE_TOP + DATE_FONT + LANDSCAPE_STATUS_TOP + STATUS_GLYPH

    fun unit(width: Dp, height: Dp, scale: Float): Dp = minOf(width, height) * SHORT_SIDE_RATIO * scale
}
