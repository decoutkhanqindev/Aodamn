package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.frame

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodTextPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.frame.FrameThemeSpec

object FrameMetrics {
    const val FONT_TOP = 1.096f
    const val FONT_BOTTOM = 0.233f
    const val DIGIT_MILLIS = 300
    const val TOP_WEIGHT = 0.25f
    const val MIDDLE_WEIGHT = 0.5f
    const val BOTTOM_WEIGHT = 0.25f

    val BoxSidePadding = 20.dp
    val BoxVerticalPadding = 15.dp
    val NumberLift = 12.dp
    val SeparatorMargin = 5.dp
    val Line = 1.dp

    val ZonePadding = 20.dp
    val DateSize = 24.sp
    val DateGap = 18.dp
    val WeatherGap = 14.dp
    val StatusGlyph = 17.5.dp
    const val STATUS_GAP_SCALE = 0.55f
    val LatestGap = 10.dp
    val LatestIcon = 18.dp
    val LatestIconGap = 10.dp
    val LatestSize = 17.sp

    val ExtrasGap = 24.dp
    val PlayerMargin = 30.dp

    val LandscapeMargin = 40.dp
    val LandscapeGap = 36.dp
    val LandscapeItemGap = 14.dp
    val LandscapePlayerGap = 24.dp

    val TitleSize = 18.sp
    val ArtistSize = 15.sp
    val ArtistGap = 4.dp
    val ButtonSize = 35.dp
    val ButtonIconSize = 15.dp
    const val PAUSED_ALPHA = 0.6f

    val TextPlayer = AodTextPlayerStyle(
        fontFamily = FrameThemeSpec.Font,
        titleSize = TitleSize,
        artistSize = ArtistSize,
        artistGap = ArtistGap,
        buttonSize = ButtonSize,
        buttonIconSize = ButtonIconSize,
        pausedAlpha = PAUSED_ALPHA,
        textGap = PlayerMargin,
        titleWeight = FontWeight.Normal,
        artistWeight = FontWeight.Light,
    )

    val LatestLine = AodLatestLineStyle(
        font = FrameThemeSpec.Font,
        iconSize = LatestIcon,
        iconGap = LatestIconGap,
        textSize = LatestSize,
    )

    fun lineBox(em: Dp): Dp = em * (FONT_TOP + FONT_BOTTOM)

    fun boxHeight(em: Dp): Dp = (BoxVerticalPadding - NumberLift) * 2 + SeparatorMargin * 2 + Line + lineBox(em) * 2

    fun hourTop(): Dp = BoxVerticalPadding - NumberLift

    fun minuteTop(em: Dp): Dp = hourTop() + lineBox(em) + SeparatorMargin * 2 + Line - NumberLift

    fun separatorTop(em: Dp): Dp = hourTop() + lineBox(em) + SeparatorMargin
}
