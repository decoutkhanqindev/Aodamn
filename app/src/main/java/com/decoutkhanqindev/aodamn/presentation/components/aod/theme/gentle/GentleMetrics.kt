package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gentle

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodCompactPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gentle.GentleThemeSpec

object GentleMetrics {
    const val CLOCK_SIZE = 0.229f
    const val CLOCK_OVERLAP = 0.222f
    const val CLOCK_ANIMATION_MILLIS = 300
    const val PORTRAIT_CLOCK_WEIGHT = 0.58f
    const val LANDSCAPE_CLOCK_WEIGHT = 0.34f
    const val LANDSCAPE_SIDE_WEIGHT = 0.33f

    val PortraitTop = 56.dp
    val PortraitSide = 40.dp
    val PortraitBottom = 42.dp
    val DetailsGap = 14.dp
    val IconRow = 17.dp
    const val ICON_GAP_SCALE = 0.55f
    val LatestGap = 12.dp
    val LatestIcon = 17.dp
    val LatestIconGap = 10.dp
    val LatestSize = 12.5.sp
    val DateSize = 13.sp
    val DateSpacing = 0.05.em
    val WeatherGap = 12.dp

    val LandscapeSide = 32.dp
    val LandscapeDetailsGap = 14.dp
    val LandscapeItemGap = 18.dp
    val LandscapeDateSize = 13.sp

    val BatteryGlyph = 15.dp
    const val IDLE_BATTERY_ALPHA = 0.8f

    val TitleSize = 13.sp
    val ArtistSize = 11.5.sp
    val ArtistGap = 8.dp
    const val TEXT_DARKEN = 0.3f
    val TrackSpacing = 0.2.em
    val PlayButton = 45.dp
    val PlayIcon = 22.dp
    val PreviousButton = 38.dp
    val PreviousIcon = 16.dp
    val NextButton = 38.dp
    val NextIcon = 16.dp
    val PlayGap = 10.dp
    val ProgressGap = 12.dp
    val TrackHeight = 1.5.dp
    const val TRACK_ALPHA = 0.3f

    val CompactPlayer = AodCompactPlayerStyle(
        font = GentleThemeSpec.Font,
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
        nextStartGap = PlayGap,
        previousButton = PreviousButton,
        previousIcon = PreviousIcon,
        isUppercase = true,
    )

    val LatestLine = AodLatestLineStyle(
        font = GentleThemeSpec.Font,
        iconSize = LatestIcon,
        iconGap = LatestIconGap,
        textSize = LatestSize,
        letterSpacing = 0.05.em,
        isUppercase = true,
    )
}
