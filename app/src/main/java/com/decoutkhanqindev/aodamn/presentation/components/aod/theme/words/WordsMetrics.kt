package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.words

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBadgeLatestStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodCompactPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.words.AodWordsWeightValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.words.WordsThemeSpec

object WordsMetrics {
    const val WORD_MILLIS = 300
    const val TOP_FRACTION = 0.25f

    val TopGap = 20.dp
    val BottomReserve = 45.4.dp
    val SideMargin = 40.dp

    val DateGap = 12.dp
    val DateSlot = 20.dp
    val WeatherGap = 14.dp

    val NotificationsGap = 40.dp
    val IconRow = 18.dp
    const val ICON_GAP_SCALE = 0.492f
    val RuleGap = 15.dp
    val LatestBadge = 28.dp
    val LatestIcon = 18.dp
    val LatestTextGap = 12.dp
    val TitleSize = 16.sp
    val TextSize = 15.sp
    val TextGap = 7.dp
    const val TEXT_DARKEN = 0.4f
    const val LATEST_MAX_LINES = 2

    val PlayerGap = 30.dp
    val BatterySize = 14.sp
    val BatteryBottom = 30.dp
    val FlashlightIcon = 14.dp
    val FlashlightGap = 8.dp
    const val IDLE_BATTERY_ALPHA = 0.8f

    const val LANDSCAPE_EDGE_WEIGHT = 0.05f
    const val LANDSCAPE_TEXT_WEIGHT = 0.5f
    const val LANDSCAPE_SIDE_WEIGHT = 0.45f
    val LandscapeMargin = 40.dp
    val LandscapeIconsGap = 15.dp
    val LandscapeLatestGap = 24.dp
    val LandscapePlayerGap = 20.dp

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
        font = WordsThemeSpec.Font,
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
        font = WordsThemeSpec.Font,
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

    fun lineTop(weight: AodWordsWeightValue): Float = when (weight) {
        AodWordsWeightValue.THIN -> 0.88f
        AodWordsWeightValue.LIGHT -> 0.8945f
        AodWordsWeightValue.REGULAR -> 0.9185f
        AodWordsWeightValue.BOLD -> 0.937f
    }

    fun lineBox(weight: AodWordsWeightValue): Float = lineTop(weight) + when (weight) {
        AodWordsWeightValue.THIN -> 0.19f
        AodWordsWeightValue.LIGHT -> 0.186f
        AodWordsWeightValue.REGULAR -> 0.1825f
        AodWordsWeightValue.BOLD -> 0.1805f
    }

    fun blockTop(height: Dp): Dp = max(height - BottomReserve, 0.dp) * TOP_FRACTION + TopGap
}
