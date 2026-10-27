package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.spine

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodCompactPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.spine.SpineThemeSpec

object SpineMetrics {
    const val BASE_WIDTH = 393f
    const val CLOCK_SIZE = 40f
    const val DATE_SCALE = 0.5f

    const val COLUMN_START = 18f
    const val DATE_GAP = 30f
    const val PORTRAIT_CLOCK_BOTTOM = 45f
    const val LANDSCAPE_CLOCK_BOTTOM = 30f
    val LandscapeFitMargin = 24.dp

    const val TOP_WEIGHT = 0.25f
    const val INFO_WEIGHT = 0.75f
    val InfoStart = 40.dp
    val InfoEnd = 20.dp
    val DetailsBottom = 40.dp
    val ExtrasGap = 28.dp

    const val LANDSCAPE_EDGE_WEIGHT = 0.05f
    const val LANDSCAPE_GAP_WEIGHT = 0.1f
    val LandscapeInfoMargin = 30.dp

    val StatusGlyph = 17.dp
    const val STATUS_GAP_SCALE = 0.52f
    val DotGap = 12.dp
    const val DOT_SCALE = 0.228f

    val LatestGap = 10.dp
    val LatestIcon = 17.dp
    val LatestIconGap = 10.dp
    val LatestSize = 17.sp

    val PlayerMaxWidth = 344.dp
    val PlayerEnd = 30.dp
    val TitleSize = 18.sp
    val ArtistSize = 16.sp
    val ArtistGap = 2.dp
    val PlayButton = 42.dp
    val PlayIcon = 22.dp
    val PlayGap = 30.dp
    val NextButton = 37.dp
    val NextIcon = 17.dp
    val NextGap = 5.dp

    val CompactPlayer = AodCompactPlayerStyle(
        font = SpineThemeSpec.Font,
        titleSize = TitleSize,
        artistSize = ArtistSize,
        artistGap = ArtistGap,
        artistDarken = 0f,
        playButton = PlayButton,
        playIcon = PlayIcon,
        nextButton = NextButton,
        nextIcon = NextIcon,
        progressGap = 0.dp,
        trackHeight = 0.dp,
        trackAlpha = 0f,
        playStartGap = PlayGap,
        nextStartGap = NextGap,
        artistWeight = FontWeight.Light,
        isProgressShown = false,
    )

    val LatestLine = AodLatestLineStyle(
        font = SpineThemeSpec.Font,
        iconSize = LatestIcon,
        iconGap = LatestIconGap,
        textSize = LatestSize,
    )
}
