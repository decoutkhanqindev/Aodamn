package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.dot

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

object DotMetrics {
    const val REFERENCE_SIDE = 393f
    const val PORTRAIT_TOP = .15f
    const val PORTRAIT_CLOCK = .55f
    const val PORTRAIT_BOTTOM = .3f
    const val DIGIT_HEIGHT = .778f
    const val SEPARATOR_DOT = .09f
    const val SEPARATOR_OFFSET = .27f
    const val LANDSCAPE_CLOCK = .9f
    const val ARTIST_DARKEN = .3f
    const val BATTERY_IDLE_ALPHA = .8f
    const val ICON_GRID = 7
    const val ICON_DOT = .72f
    const val ICON_ALPHA_THRESHOLD = .3f
    const val ICON_SAMPLE_CELL = 4
    const val MEDIA_GRID_ROWS = 11
    const val MEDIA_DOT = .7f
    const val PROGRESS_REST_ALPHA = .3f
    const val SWAP_MILLIS = 300
    const val FADE_MILLIS = 300

    val DigitDotGap = 15.dp
    val SidePadding = 40.dp
    val DateSize = 20.sp
    val DateDetailsGap = 8.dp
    val LatestSize = 17.sp
    val NotificationIcon = 18.dp
    val NotificationIconGap = 10.dp
    val LatestIconGap = 7.dp
    val TitleSize = 17.sp
    val TitleSpacing = .05.em
    val ArtistSize = 15.sp
    val ArtistSpacing = .07.em
    val ArtistTop = 4.dp
    val PlayButton = 46.dp
    val PlayIcon = 26.dp
    val NextButton = 41.dp
    val NextIcon = 21.dp
    val ButtonMargin = 10.dp
    val ProgressTop = 15.dp
    val ProgressDotPitch = 5.dp
    val ProgressDot = 3.6.dp
    val PlayerBottom = 35.dp
    val BatterySize = 15.sp
    val BatterySpacing = .03.em
    val BatteryBottom = 35.dp
    val BoltHeight = 11.dp
    val LandscapeGap = 48.dp
    val LandscapeLineGap = 18.dp
    val SwapSlide = 17.dp

    fun scale(maxWidthDp: Float, maxHeightDp: Float): Float = minOf(maxWidthDp, maxHeightDp) / REFERENCE_SIDE
}
