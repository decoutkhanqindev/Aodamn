package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodTextPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gradientline.GradientLineThemeSpec

object GradientLineMetrics {
    const val TIME_SIZE = 0.11f
    const val DATE_SIZE = 0.043f
    const val DATE_ALPHA = 200f / 255f
    const val LINE_WIDTH = 0.008f

    const val TRAVEL_START = 1f
    const val TRAVEL_END = 2f
    const val CUT_BEFORE = 1.1f
    const val CUT_AFTER = 1.1f
    const val CUT_AFTER_DATE = 2f
    const val DATE_OFFSET = 1.2f
    const val ROW_GAP = 0.35f
    const val DATE_MARGIN = 0.45f
    const val MINUTES_PER_DAY = 24f * 60f

    const val LANDSCAPE_LINE_SHIFT = 0.35f
    const val LANDSCAPE_CUT = 0.6f

    val SideMargin = 40.dp
    val VerticalMargin = 60.dp
    val PlayerGap = 30.dp
    val ClockGap = 15.dp
    val DetailsPadding = 15.dp
    val LandscapeTopMargin = 30.dp
    val LandscapeBottomMargin = 32.dp
    val LandscapePlayerWidth = 360.dp

    val StatusGlyph = 16.dp
    const val STATUS_ALPHA = 0.9f
    val LatestIcon = 20.dp
    val LatestIconGap = 10.dp
    val LatestSize = 16.sp
    val LatestGap = 10.dp

    val ButtonSize = 35.dp
    val ButtonIconSize = 9.dp
    const val BUTTON_ALPHA = 0.7f
    val TitleSize = 16.sp
    val ArtistSize = 13.sp
    val ArtistGap = 4.dp
    const val ARTIST_ALPHA = 0.7f
    const val PAUSED_ALPHA = 0.6f

    val TextPlayer = AodTextPlayerStyle(
        fontFamily = GradientLineThemeSpec.Font,
        titleSize = TitleSize,
        artistSize = ArtistSize,
        artistGap = ArtistGap,
        buttonSize = ButtonSize,
        buttonIconSize = ButtonIconSize,
        pausedAlpha = PAUSED_ALPHA,
        artistAlpha = ARTIST_ALPHA,
        buttonAlpha = BUTTON_ALPHA,
    )

    val LatestLine = AodLatestLineStyle(
        font = GradientLineThemeSpec.Font,
        iconSize = LatestIcon,
        iconGap = LatestIconGap,
        textSize = LatestSize,
    )
}
