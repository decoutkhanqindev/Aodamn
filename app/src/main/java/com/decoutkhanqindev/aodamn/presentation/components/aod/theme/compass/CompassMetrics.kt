package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodTextPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.CompassThemeSpec

object CompassMetrics {
    const val DIAL_FRACTION = 0.75f

    const val HOUR_RADIUS = 0.84f
    const val HOUR_SIZE = 0.165f
    const val HALF_HOUR_SIZE = 0.066f
    const val HOUR_START = 120f

    const val MINUTE_RADIUS = 0.55f
    const val MINUTE_SIZE = 0.11f
    const val TICK_START = 0.475f
    const val TICK_END = 0.625f
    const val TICK_ALPHA = 0.6f

    const val SECOND_RADIUS = 0.3f
    const val SECOND_SIZE = 0.09f

    const val CIRCLE_OUTER = 0.68f
    const val CIRCLE_INNER = 0.42f
    const val CIRCLE_ALPHA = 40f / 255f

    const val POINTER_HEAD = 0.05f
    const val POINTER_SPREAD = 0.03f
    const val POINTER_GAP = 0.28f
    const val STYLE_MILLIS = 300
    const val STEP_MILLIS = 400
    val LineWidth = 1.3.dp
    val CircleWidth = 1.dp

    val DateSize = 14.sp
    val DateSpacing = 0.25.em
    val DateGap = 15.dp
    val WeatherGap = 12.dp
    val StatusGlyph = 14.dp
    val LatestGap = 12.dp
    val LatestIcon = 16.dp
    val LatestIconGap = 10.dp
    val LatestSize = 12.5.sp
    val LatestSpacing = 0.1.em
    val MediaGap = 24.dp
    val SideMargin = 30.dp

    val PlayerMargin = 40.dp
    val PlayerInset = 24.dp
    val ButtonSize = 40.dp
    val ButtonIconSize = 12.dp
    val TextGap = 10.dp
    val TitleSize = 13.sp
    val ArtistSize = 11.5.sp
    val TrackSpacing = 0.25.em
    val ArtistGap = 8.dp
    const val ARTIST_ALPHA = 0.7f
    const val PAUSED_ALPHA = 0.6f

    val TextPlayer = AodTextPlayerStyle(
        fontFamily = CompassThemeSpec.Font,
        titleSize = TitleSize,
        artistSize = ArtistSize,
        artistGap = ArtistGap,
        buttonSize = ButtonSize,
        buttonIconSize = ButtonIconSize,
        pausedAlpha = PAUSED_ALPHA,
        textGap = TextGap,
        titleWeight = FontWeight.Bold,
        artistWeight = FontWeight.Normal,
        letterSpacing = TrackSpacing,
        artistAlpha = ARTIST_ALPHA,
        isUppercase = true,
    )

    val LatestLine = AodLatestLineStyle(
        font = CompassThemeSpec.Font,
        iconSize = LatestIcon,
        iconGap = LatestIconGap,
        textSize = LatestSize,
        letterSpacing = LatestSpacing,
        isUppercase = true,
    )
}
