package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gentle

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gentle.GentleThemeSpec

object GentleMetrics {
    const val CLOCK_SIZE = 90f
    const val LINE_BOX = 1.315f
    const val CAP = 0.714f
    const val DIGITS_WIDTH = 1.2f
    val NumberOverlap = 20.dp
    const val DIGIT_MILLIS = 300

    val TopMargin = 60.dp
    val BottomMargin = 40.dp
    val SideMargin = 40.dp
    val ClockGap = 20.dp
    val PortraitBottomReserve = 200.dp

    const val LANDSCAPE_EDGE = 0.05f
    val LandscapeGap = 40.dp
    val LandscapeClockMargin = 24.dp
    val PlayerMaxWidth = 313.dp

    val DateSize = 13.sp
    val DateSpacing = 0.05.em
    val SeparatorDot = 3.2.dp
    val SeparatorGap = 12.dp
    val RowGap = 14.dp
    val LatestGap = 12.dp

    val IconRow = 17.38.dp
    const val ICON_GAP_SCALE = 0.509f
    val BatteryGlyph = 15.dp
    val BatteryGap = 20.dp

    val TitleSize = 13.sp
    val ArtistSize = 11.5.sp
    val TrackSpacing = 0.2.em
    val ArtistGap = 4.dp
    const val TEXT_DARKEN = 0.3f
    const val PAUSED_ALPHA = 0.6f
    const val ARTIST_DELAY_MILLIS = 50
    const val TRACK_MILLIS = 300

    val ProgressGap = 20.dp
    val ControlsGap = 20.dp
    val TrackHeight = 1.5.dp
    const val TRACK_ALPHA = 0.3f

    val PlayButton = 45.dp
    val PlayIcon = 25.dp
    val SkipButton = 38.dp
    val SkipIcon = 18.dp
    val PlaySideGap = 40.dp

    val LatestLine = AodLatestLineStyle(
        font = GentleThemeSpec.Font,
        iconSize = 17.dp,
        iconGap = 10.dp,
        textSize = 12.5.sp,
        letterSpacing = 0.05.em,
        isUppercase = true,
        fontWeight = FontWeight.Medium,
    )

    fun fitEm(em: Dp, maxHeight: Dp): Dp =
        minOf(em, ((maxHeight + NumberOverlap) / (LINE_BOX + CAP)).coerceAtLeast(1.dp))

    fun numberGap(em: Dp): Dp = (em * LINE_BOX - NumberOverlap - em * CAP).coerceAtLeast(0.dp)
}
