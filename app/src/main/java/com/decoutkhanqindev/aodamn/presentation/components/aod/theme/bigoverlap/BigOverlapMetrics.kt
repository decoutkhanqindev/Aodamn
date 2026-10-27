package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bigoverlap

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodCompactPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodHeroInfoStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap.BigOverlapThemeSpec

object BigOverlapMetrics {
    const val TEXT_SIZE = 0.5f
    const val STROKE = 0.07f
    const val OUTLINE_THINNING = 1.5f

    const val OVERLAP_X_FILLED = 0.17f
    const val OVERLAP_X_OUTLINE = 0.2f
    const val OVERLAP_Y_FILLED = 0.04f
    const val OVERLAP_Y_OUTLINE = 0.06f
    const val STYLE_MILLIS = 500

    const val ZERO_WIDTH = 0.679f

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
        font = BigOverlapThemeSpec.Font,
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
        font = BigOverlapThemeSpec.Font,
        dateColor = BigOverlapThemeSpec.DateColor,
        notificationsColor = BigOverlapThemeSpec.NotificationsColor,
        batteryColor = BigOverlapThemeSpec.BatteryColor,
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
