package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.sector

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodCompactPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.sector.SectorThemeSpec

object SectorMetrics {
    const val NECK_END = 0.2f
    const val NECK_WIDTH = 0.013f
    const val BODY_START = 0.15f
    const val BODY_WIDTH = 0.0333f
    const val HOUR_END = 0.45f
    const val MINUTE_END = 0.75f
    const val SECOND_START = -0.1f
    const val SECOND_END = 0.75f
    const val SECOND_WIDTH = 0.0074f
    const val DOT_RADIUS = 0.0333f
    const val HOLE_RADIUS = 0.0185f
    const val RING_WIDTH = 0.0093f
    const val SHADOW_RADIUS = 0.0185f
    const val SECTOR_ALPHA = 0.8f
    const val FADE_MIDDLE = 0.5f

    const val MINUTE_MILLIS = 60_000L
    const val HOUR_MILLIS = 3_600_000L
    const val HALF_DAY_MILLIS = 43_200_000L

    val BlockPadding = 70.dp
    val SidePadding = 30.dp
    val RowPadding = 10.dp
    val DateSize = 18.sp
    val WeatherGap = 14.dp
    val SeparatorWidth = 0.5.dp
    val SeparatorHeight = 21.6.dp
    val SeparatorGap = 12.dp
    val IconRow = 18.dp
    const val ICON_GAP_SCALE = 0.492f
    val LatestGap = 6.dp
    val LatestBadge = 28.dp
    val LatestIcon = 18.dp
    val LatestTextGap = 12.dp
    val TitleSize = 16.sp
    val TextSize = 15.sp
    val TextGap = 5.dp
    const val TEXT_DARKEN = 0.4f
    const val LATEST_MAX_LINES = 2

    val PlayerMargin = 40.dp
    val BatterySize = 14.sp
    val BatteryBottom = 30.dp
    val FlashlightIcon = 14.dp
    val FlashlightGap = 8.dp
    const val IDLE_BATTERY_ALPHA = 0.8f

    val LandscapeMargin = 40.dp
    val LandscapeGap = 36.dp
    val LandscapeItemGap = 14.dp
    val LandscapePlayerGap = 24.dp

    val ArtistGap = 8.dp
    val ButtonGap = 10.dp
    val PlayButton = 42.dp
    val PlayIcon = 22.dp
    val NextButton = 36.dp
    val NextIcon = 16.dp
    val ProgressGap = 20.dp
    val TrackHeight = 1.dp
    const val TRACK_ALPHA = 0.3f

    val CompactPlayer = AodCompactPlayerStyle(
        font = SectorThemeSpec.Font,
        titleSize = TitleSize,
        artistSize = TextSize,
        artistGap = ArtistGap,
        artistDarken = TEXT_DARKEN,
        playButton = PlayButton,
        playIcon = PlayIcon,
        nextButton = NextButton,
        nextIcon = NextIcon,
        progressGap = ProgressGap,
        trackHeight = TrackHeight,
        trackAlpha = TRACK_ALPHA,
        playStartGap = ButtonGap,
        nextStartGap = ButtonGap,
    )
}
