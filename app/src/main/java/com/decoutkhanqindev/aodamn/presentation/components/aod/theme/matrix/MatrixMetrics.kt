package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.matrix

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

object MatrixMetrics {
    const val REFERENCE_SIDE = 393f
    const val PORTRAIT_ANCHOR = .35f
    const val DIGIT_HEIGHT = 1.4f
    const val UNLIT_ALPHA = .12f
    const val UNLIT_SIZE = .7f
    const val ICON_GRID = 7
    const val ICON_SAMPLE_CELL = 4
    const val ICON_ALPHA_THRESHOLD = .3f
    const val MEDIA_GRID_ROWS = 11
    const val NEXT_BAR_COLUMN = 6.3f
    const val ARTIST_DARKEN = .3f
    const val SEPARATOR_DARKEN = .5f
    const val BATTERY_IDLE_ALPHA = .8f
    const val PROGRESS_REST_ALPHA = .3f
    const val SWAP_MILLIS = 300
    const val FADE_MILLIS = 300
    const val LANDSCAPE_LEFT = .5f

    val SidePadding = 40.dp
    val ClockInfoGap = 28.dp
    val LineGap = 8.dp
    val DetailsGap = 12.dp
    val AnchorBelowClock = 84.dp
    val DateSize = 20.sp
    val LineSize = 18.sp
    val IconRowHeight = 22.dp
    val NotificationIcon = 18.dp
    val NotificationIconGap = 10.dp
    val LatestIconGap = 7.dp
    val LineIcon = 16.dp
    val LineIconGap = 8.dp
    val TitleSize = 18.sp
    val TitleSpacing = .05.em
    val ArtistSize = 16.sp
    val ArtistSpacing = .07.em
    val ArtistTop = 4.dp
    val PlayButton = 48.dp
    val PlayIcon = 28.dp
    val NextButton = 43.dp
    val NextIcon = 23.dp
    val ButtonMargin = 10.dp
    val ProgressTop = 15.dp
    val ProgressDotPitch = 5.dp
    val ProgressDot = 3.6.dp
    val PlayerBottom = 35.dp
    val BatterySize = 16.sp
    val BatterySpacing = .03.em
    val BatteryBottom = 35.dp
    val BoltHeight = 12.dp
    val SeparatorTop = 20.dp
    val Separator = .5.dp
    val DetailsMediaGap = 35.dp
    val SwapSlide = 17.dp

    fun scale(maxWidthDp: Float, maxHeightDp: Float): Float = minOf(maxWidthDp, maxHeightDp) / REFERENCE_SIDE
}
