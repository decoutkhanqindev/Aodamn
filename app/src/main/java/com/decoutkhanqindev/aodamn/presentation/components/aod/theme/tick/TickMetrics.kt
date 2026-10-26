package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tick

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

object TickMetrics {
    const val TICK_COUNT = 60
    const val DEGREES_PER_SECOND = 6f
    const val MINUTE_MILLIS = 60_000L
    const val TICK_INNER = 0.8f
    const val TICK_OUTER = 0.9f
    const val STROKE = 0.007f
    const val GRADIENT_REACH = 0.7f

    const val FADE_OFFSET = 0.7f
    const val FADE_SOLID = 0.65f

    const val SECOND_OFFSET = 0.84f
    const val SECOND_RADIUS = 0.12f
    const val SECOND_SIZE = 0.115f

    const val CLOCK_SIZE = 72f / 196.5f
    const val DATE_SIZE = 23f / 196.5f
    val ClockSpacing = 0.02.em
    val DateSpacing = 0.12.em

    val WeatherGap = 8.dp

    val EdgeMargin = 70.dp
    val SideMargin = 30.dp
    val PlayerGap = 16.dp
    val PlayerMargin = 40.dp

    const val SECOND_REACH = SECOND_OFFSET + SECOND_RADIUS
    val LandscapeGap = 15.dp
    val LandscapeMargin = 24.dp

    val IconRow = 17.dp
    val LatestGap = 12.dp
    val LatestIcon = 17.dp
    val LatestIconGap = 10.dp
    val LatestSize = 13.sp
    val LatestSpacing = 0.1.em
    val StatusGlyph = 15.dp

    val ButtonSize = 40.dp
    val ButtonIconSize = 12.dp
    val TextGap = 10.dp
    val TitleSize = 13.sp
    val ArtistSize = 11.5.sp
    val TrackSpacing = 0.25.em
    val ArtistGap = 8.dp
    const val TEXT_DARKEN = 0.3f
    const val PAUSED_ALPHA = 0.6f
}
