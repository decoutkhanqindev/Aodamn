package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.glass

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyInter

object GlassMetrics {
    const val REFERENCE_SIDE = 393f
    const val PORTRAIT_TOP = .4f
    const val DATE_ALPHA = .9f
    const val CLOCK_ALPHA = .9f
    const val RAIL_FRACTION = .02f
    const val BATTERY_ALPHA = .9f
    const val ARTIST_ALPHA = .6f
    const val TIME_ALPHA = .6f
    const val ELAPSED_WEIGHT = .13f
    const val BAR_WEIGHT = .73f
    const val REMAINING_WEIGHT = .14f
    const val SWAP_MILLIS = 300
    const val FADE_MILLIS = 300
    const val ART_FADE_MILLIS = 300
    const val POSITION_TICK_MILLIS = 1_000L
    const val LANDSCAPE_LEFT = .5f

    val DateSize = 22.sp
    val DateClockGap = 22.dp
    val ClockEventsGap = 18.dp
    val CardMargin = 18.dp
    val NotificationCardCorner = 14.dp
    val MediaCardCorner = 20.dp
    val CardPadding = 14.dp
    val CardGap = 36.dp
    val MediaBatteryGap = 28.dp
    val BatteryBottom = 30.dp
    val BatterySize = 16.sp
    val NotificationIcon = 18.dp
    val NotificationIconGap = 10.dp
    val AlbumArt = 60.dp
    val AlbumCorner = 7.dp
    val AlbumGap = 14.dp
    val TitleSize = 18.sp
    val ArtistSize = 18.sp
    val TrackInfoLineHeight = 22.sp
    val TitleSpacing = .02.em
    val ProgressTop = 20.dp
    val TimeSize = 12.sp
    val TimeSpacing = .1.em
    val TrackHeight = 6.dp
    val ButtonsTop = 20.dp
    val Button = 55.dp
    val ButtonIcon = 35.dp
    val ButtonGap = 45.dp
    val SwapSlide = 17.dp
    val LandscapeBatteryGap = 22.dp

    val LatestLine = AodLatestLineStyle(
        font = FontFamilyInter,
        iconSize = 18.dp,
        iconGap = 7.dp,
        textSize = 16.sp,
        fontWeight = FontWeight.Medium,
    )

    fun scale(minSideDp: Float): Float = minSideDp / REFERENCE_SIDE
}
