package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.orbit

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodTextPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.orbit.OrbitThemeSpec

object OrbitMetrics {
    const val SIZE_DIVISOR = 110f
    const val INNER_ORBIT = 0.56f
    const val MIDDLE_ORBIT = 0.78f
    const val HOUR_PLANET = 0.0733f
    const val MINUTE_PLANET = 0.0489f
    const val SECOND_PLANET = 0.0293f
    const val DISC_ALPHA = 0.3f
    const val INNER_ALPHA = 150f / 255f
    const val MIDDLE_ALPHA = 100f / 255f
    const val OUTER_ALPHA = 50f / 255f
    const val DIGITAL_SIZE = 28f

    const val MINUTE_MILLIS = 60_000L
    const val HOUR_MILLIS = 3_600_000L
    const val HALF_DAY_MILLIS = 43_200_000L

    val OrbitLine = 1.dp
    val DigitalSpacing = 0.2.em

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
    val ExtrasGap = 24.dp
    val LandscapeMargin = 40.dp
    val LandscapeGap = 36.dp

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
        fontFamily = OrbitThemeSpec.Font,
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
        font = OrbitThemeSpec.Font,
        iconSize = LatestIcon,
        iconGap = LatestIconGap,
        textSize = LatestSize,
        letterSpacing = LatestSpacing,
        isUppercase = true,
    )

    fun radius(width: Dp, height: Dp, size: Int): Dp = min(width, height) / 2 * (size / SIZE_DIVISOR)
}
