package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.quarters

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodCompactPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodHeroInfoStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.quarters.QuartersThemeSpec

object QuartersMetrics {
    const val TEXT_SIZE = 0.72f
    const val SIDE_DIGIT_OFFSET = 0.6f
    const val END_DIGIT_OFFSET = 0.52f
    val DigitOutline = 2.dp
    const val STYLE_MILLIS = 500

    const val HOUR_START = -0.085f
    const val HOUR_END = 0.615f
    const val HOUR_WIDTH = 0.15f
    const val HOUR_OUTLINE = 0.0175f
    const val MINUTE_LENGTH = 0.86f
    const val MINUTE_WIDTH = 0.035f
    const val SECOND_LENGTH = 0.91f
    const val SECOND_WIDTH = 0.013f
    const val DOT_RADIUS = 0.0234f
    const val SECOND_DARKEN = 0.25f
    const val SHADOW_RADIUS = 0.1f

    const val MINUTE_MILLIS = 60_000L
    const val HOUR_MILLIS = 3_600_000L
    const val HALF_DAY_MILLIS = 43_200_000L

    val TopMinHeight = 130.dp
    val SideMargin = 40.dp
    const val TOP_SPACE_WEIGHT = 0.08f
    const val CLOCK_WEIGHT = 0.8f
    const val BOTTOM_SPACE_WEIGHT = 0.12f
    val BatteryPadding = 40.dp

    val DateSize = 20.sp
    val LandscapeDateSize = 18.sp
    val WeatherGap = 14.dp
    val DetailsGap = 18.dp
    val IconRow = 18.dp
    const val ICON_GAP_SCALE = 0.492f
    val LatestIcon = 18.dp
    val LatestIconGap = 7.dp
    val LatestSize = 16.sp
    val LatestGap = 10.dp
    const val TEXT_DARKEN = 0.3f

    val BatteryGlyph = 15.3.dp
    val LandscapeBatteryGlyph = 14.2.dp
    const val IDLE_BATTERY_ALPHA = 0.8f

    val LandscapeGap = 36.dp
    val LandscapeItemGap = 24.dp

    val TitleSize = 16.sp
    val ArtistSize = 14.sp
    val ArtistGap = 4.dp
    val TrackSpacing = 0.02.em
    val PlayButton = 40.dp
    val PlayIcon = 20.dp
    val PlayGap = 10.dp
    val NextButton = 35.dp
    val NextIcon = 15.dp
    val ProgressGap = 15.dp
    val TrackHeight = 1.5.dp
    const val TRACK_ALPHA = 0.3f

    val CompactPlayer = AodCompactPlayerStyle(
        font = QuartersThemeSpec.Font,
        titleSize = TitleSize,
        artistSize = ArtistSize,
        artistGap = ArtistGap,
        artistDarken = TEXT_DARKEN,
        playButton = PlayButton,
        playIcon = PlayIcon,
        nextButton = NextButton,
        nextIcon = NextIcon,
        progressGap = ProgressGap,
        trackHeight = TrackHeight,
        trackAlpha = TRACK_ALPHA,
        letterSpacing = TrackSpacing,
        playStartGap = PlayGap,
        playEndGap = PlayGap,
    )

    val HeroInfo = AodHeroInfoStyle(
        font = QuartersThemeSpec.Font,
        dateColor = QuartersThemeSpec.DateColor,
        notificationsColor = QuartersThemeSpec.NotificationsColor,
        batteryColor = QuartersThemeSpec.BatteryColor,
        topMinHeight = TopMinHeight,
        sideMargin = SideMargin,
        topSpaceWeight = TOP_SPACE_WEIGHT,
        heroWeight = CLOCK_WEIGHT,
        bottomSpaceWeight = BOTTOM_SPACE_WEIGHT,
        batteryPadding = BatteryPadding,
        dateSize = DateSize,
        landscapeDateSize = LandscapeDateSize,
        weatherGap = WeatherGap,
        detailsGap = DetailsGap,
        iconRow = IconRow,
        iconGapScale = ICON_GAP_SCALE,
        latestIcon = LatestIcon,
        latestIconGap = LatestIconGap,
        latestSize = LatestSize,
        latestGap = LatestGap,
        textDarken = TEXT_DARKEN,
        batteryGlyph = BatteryGlyph,
        landscapeBatteryGlyph = LandscapeBatteryGlyph,
        idleBatteryAlpha = IDLE_BATTERY_ALPHA,
        landscapeGap = LandscapeGap,
        landscapeItemGap = LandscapeItemGap,
    )
}
