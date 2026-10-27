package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBadgeLatestStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.ripple.RippleThemeSpec

object RippleMetrics {
    const val FACE_RADIUS = 0.9f
    const val WAVE_STEP_DEGREES = 15
    const val WAVE_DEPTH = 0.06f
    val OutlineWidth = 2.dp

    const val MINUTE_LENGTH = 0.5f
    const val HOUR_LENGTH = 0.3f
    const val HAND_WIDTH = 0.1f
    const val SECOND_ORBIT = 0.71f
    const val SECOND_RADIUS = 0.05f

    const val DATE_ARC = 0.75f
    const val DATE_SIZE = 0.085f

    val TopZone = 200.dp
    val PlayerTop = 10.dp
    val PlayerHeight = 65.dp
    val PlayerBottom = 30.dp
    val PlayerMargin = 40.dp
    val BottomMargin = 30.dp

    val LandscapeGap = 24.dp
    val LandscapeItemGap = 22.dp

    val IconRow = 18.dp
    val LatestBadge = 28.dp
    val LatestIcon = 18.dp
    val LatestGap = 12.dp
    val LatestTitleSize = 16.sp
    val LatestTextSize = 14.sp
    val LatestTextGap = 5.dp
    const val TEXT_DARKEN = 0.4f

    val StatusGlyph = 16.dp
    const val IDLE_BATTERY_ALPHA = 0.8f

    val CardPadding = 5.dp
    val AlbumArt = 55.dp
    val TextGap = 12.dp
    val TitleSize = 15.sp
    val ArtistSize = 14.sp
    val ArtistGap = 2.dp
    val PlayButton = 60.dp
    val PlayButtonGap = 15.dp
    val PlayIcon = 16.dp
    val FallbackIcon = 24.dp
    val SwipeThreshold = 48.dp

    val BadgeLatest = AodBadgeLatestStyle(
        font = RippleThemeSpec.Font,
        badgeSize = LatestBadge,
        iconSize = LatestIcon,
        textStartGap = LatestGap,
        titleSize = LatestTitleSize,
        textSize = LatestTextSize,
        textGap = LatestTextGap,
        textDarken = TEXT_DARKEN,
        textMaxLines = 2,
        textWeight = FontWeight.Normal,
    )
}
