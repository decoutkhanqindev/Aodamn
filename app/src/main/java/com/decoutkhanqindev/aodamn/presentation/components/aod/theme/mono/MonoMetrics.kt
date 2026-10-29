package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.mono

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodCompactPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.mono.MonoThemeSpec

object MonoMetrics {
    const val CLOCK_SIZE = 160f
    const val DATE_SIZE = 20f
    const val LANDSCAPE_SCALE = 0.9f
    const val DIGIT_MILLIS = 300
    const val TOP_WEIGHT = 0.7f
    const val LANDSCAPE_CLOCK_WEIGHT = 0.45f
    const val LANDSCAPE_SIDE_WEIGHT = 0.55f

    val SideMargin = 40.dp
    val BottomMargin = 35.dp
    val NumberGap = 28.dp
    val WeatherGap = 10.dp

    val ExtrasGap = 20.dp
    val StatusGlyph = 17.5.dp
    const val STATUS_GAP_SCALE = 0.55f
    val LatestGap = 8.dp
    val LatestIcon = 18.dp
    val LatestIconGap = 10.dp
    val LatestSize = 16.sp
    val PlayerGap = 25.dp
    val PlayerEndOverhang = 13.dp

    val TitleSize = 16.sp
    val ArtistSize = 14.sp
    const val ARTIST_DARKEN = 0.3f
    val PreviousButton = 43.dp
    val PreviousIcon = 17.dp
    val PlayButton = 48.dp
    val PlayIcon = 28.dp
    val PlayGap = 10.dp
    val NextButton = 43.dp
    val NextIcon = 17.dp
    val ProgressGap = 15.dp
    val TrackHeight = 1.5.dp
    const val TRACK_ALPHA = 0.3f

    val CompactPlayer = AodCompactPlayerStyle(
        font = MonoThemeSpec.Font,
        titleSize = TitleSize,
        artistSize = ArtistSize,
        artistGap = 0.dp,
        artistDarken = ARTIST_DARKEN,
        playButton = PlayButton,
        playIcon = PlayIcon,
        nextButton = NextButton,
        nextIcon = NextIcon,
        progressGap = ProgressGap,
        trackHeight = TrackHeight,
        trackAlpha = TRACK_ALPHA,
        playStartGap = PlayGap,
        playEndGap = PlayGap,
        previousButton = PreviousButton,
        previousIcon = PreviousIcon,
        isProgressOnTop = true,
        isUppercase = true,
    )

    val LatestLine = AodLatestLineStyle(
        font = MonoThemeSpec.Font,
        iconSize = LatestIcon,
        iconGap = LatestIconGap,
        textSize = LatestSize,
        isUppercase = true,
    )
}
