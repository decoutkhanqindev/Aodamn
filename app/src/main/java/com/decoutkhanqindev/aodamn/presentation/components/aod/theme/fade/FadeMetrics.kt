package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.fade

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBadgeLatestStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodCompactPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.fade.AodFadeWeightValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.fade.FadeThemeSpec

object FadeMetrics {
    const val ADVANCE = 0.58f
    const val CAP = 0.708f
    const val FADE_END_X = 0.83f
    const val FADE_END_Y = 1f
    const val FADE_STEPS = 10
    const val SIZE_DIVISOR = 200f
    const val DIGIT_MILLIS = 300
    const val APPEAR_MILLIS = 500

    val TopReserve = 179.8.dp
    val BottomReserve = 188.5.dp
    val Overlap = 15.dp
    val MaxEm = 250.dp

    val BlockPadding = 70.dp
    val SidePadding = 30.dp
    val RowPadding = 10.dp
    val DateSize = 18.sp
    val WeatherGap = 14.dp
    val SeparatorWidth = 0.5.dp
    val SeparatorHeight = 19.8.dp
    val SeparatorGap = 12.dp
    val IconRow = 18.dp
    const val ICON_GAP_SCALE = 0.492f
    val LatestGap = 6.dp
    val LatestBadge = 28.dp
    val LatestIcon = 18.dp
    val LatestTextGap = 12.dp
    val TitleSize = 16.sp
    val TextSize = 15.sp
    val TextGap = 5.dp
    const val TEXT_DARKEN = 0.4f
    const val LATEST_MAX_LINES = 2

    val PlayerMargin = 40.dp
    val BatterySize = 14.sp
    val BatteryBottom = 30.dp
    val FlashlightIcon = 14.dp
    val FlashlightGap = 8.dp
    const val IDLE_BATTERY_ALPHA = 0.8f

    val LandscapeEdge = 24.dp
    val LandscapeMargin = 40.dp
    val LandscapeGap = 36.dp
    val LandscapeItemGap = 14.dp
    val LandscapeBlockGap = 18.dp
    val LandscapePlayerGap = 28.dp

    val ArtistGap = 8.dp
    val ButtonGap = 10.dp
    val PlayButton = 42.dp
    val PlayIcon = 22.dp
    val NextButton = 36.dp
    val NextIcon = 16.dp
    val ProgressGap = 20.dp
    val TrackHeight = 1.dp
    const val TRACK_ALPHA = 0.3f

    val CompactPlayer = AodCompactPlayerStyle(
        font = FadeThemeSpec.Font,
        titleSize = TitleSize,
        artistSize = TextSize,
        artistGap = ArtistGap,
        artistDarken = TEXT_DARKEN,
        playButton = PlayButton,
        playIcon = PlayIcon,
        nextButton = NextButton,
        nextIcon = NextIcon,
        progressGap = ProgressGap,
        trackHeight = TrackHeight,
        trackAlpha = TRACK_ALPHA,
        playStartGap = ButtonGap,
        nextStartGap = ButtonGap,
    )

    val BadgeLatest = AodBadgeLatestStyle(
        font = FadeThemeSpec.Font,
        badgeSize = LatestBadge,
        iconSize = LatestIcon,
        textStartGap = LatestTextGap,
        titleSize = TitleSize,
        textSize = TextSize,
        textGap = TextGap,
        textDarken = TEXT_DARKEN,
        textMaxLines = LATEST_MAX_LINES,
        isTitleMarquee = true,
        isTextShrinkable = true,
    )

    fun lineTop(weight: AodFadeWeightValue): Float = when (weight) {
        AodFadeWeightValue.THIN -> 0.88f
        AodFadeWeightValue.LIGHT -> 0.8945f
        AodFadeWeightValue.REGULAR -> 0.9185f
        AodFadeWeightValue.BOLD -> 0.937f
    }

    fun lineBox(weight: AodFadeWeightValue): Float = lineTop(weight) + when (weight) {
        AodFadeWeightValue.THIN -> 0.19f
        AodFadeWeightValue.LIGHT -> 0.186f
        AodFadeWeightValue.REGULAR -> 0.1825f
        AodFadeWeightValue.BOLD -> 0.1805f
    }

    fun portraitEm(height: Dp, size: Int, weight: AodFadeWeightValue): Dp =
        min(middle(height) * (size / SIZE_DIVISOR) / lineBox(weight), MaxEm)

    fun portraitHourBottom(height: Dp, size: Int): Dp =
        TopReserve + middle(height) * ((1f - size * 2f / SIZE_DIVISOR) / 2f) + middle(height) * (size / SIZE_DIVISOR)

    fun landscapeEm(width: Dp, height: Dp, size: Int, weight: AodFadeWeightValue): Dp =
        min(portraitEm(height = width, size = size, weight = weight), (height - LandscapeEdge * 2 + Overlap) / reach(weight))

    fun landscapeHourTop(height: Dp, em: Dp, weight: AodFadeWeightValue): Dp =
        height / 2 - (em * reach(weight) - Overlap) / 2 - em * (lineTop(weight) - CAP)

    private fun middle(height: Dp): Dp = max(height - TopReserve - BottomReserve + Overlap, 0.dp)

    private fun reach(weight: AodFadeWeightValue): Float = lineBox(weight) * 2 - (lineTop(weight) - CAP) - (lineBox(weight) - lineTop(weight))
}
