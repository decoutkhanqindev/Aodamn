package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.clip

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodTextPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.clip.ClipThemeSpec

object ClipMetrics {
    const val SIZE_DIVISOR = 110f
    const val HAND_HALF_HEIGHT = 0.07f
    const val HAND_STROKE = 0.021f
    const val HOUR_END = 0.55f
    const val MINUTE_END = 0.85f
    const val DOT_RADIUS = 0.021f
    const val SHADOW_RADIUS = 0.07f
    const val STEP_MILLIS = 400

    const val HOUR_MILLIS = 3_600_000L
    const val HALF_DAY_MILLIS = 43_200_000L

    val BottomMargin = 60.dp
    val SideMargin = 30.dp
    val DateSize = 15.sp
    val LandscapeDateSize = 14.sp
    val DateSpacing = 0.25.em
    val DateGap = 10.dp
    val WeatherGap = 12.dp
    val StatusGlyph = 15.5.dp
    const val STATUS_GAP_SCALE = 0.6f
    val LatestGap = 10.dp
    val LatestIcon = 17.dp
    val LatestIconGap = 10.dp
    val LatestSize = 13.sp
    val LatestSpacing = 0.1.em
    val MediaGap = 24.dp
    val PlayerMargin = 40.dp
    val ZoneInset = 24.dp

    val ButtonSize = 40.dp
    val ButtonIconSize = 12.dp
    val TextGap = 10.dp
    val TitleSize = 13.sp
    val ArtistSize = 11.5.sp
    val TrackSpacing = 0.25.em
    val ArtistGap = 4.dp
    const val TEXT_DARKEN = 0.3f
    const val PAUSED_ALPHA = 0.6f

    val TextPlayer = AodTextPlayerStyle(
        fontFamily = ClipThemeSpec.Font,
        titleSize = TitleSize,
        artistSize = ArtistSize,
        artistGap = ArtistGap,
        buttonSize = ButtonSize,
        buttonIconSize = ButtonIconSize,
        pausedAlpha = PAUSED_ALPHA,
        textGap = TextGap,
        titleWeight = FontWeight.SemiBold,
        artistWeight = FontWeight.Normal,
        letterSpacing = TrackSpacing,
        isUppercase = true,
    )

    val LatestLine = AodLatestLineStyle(
        font = ClipThemeSpec.Font,
        iconSize = LatestIcon,
        iconGap = LatestIconGap,
        textSize = LatestSize,
        letterSpacing = LatestSpacing,
        isUppercase = true,
    )
}
