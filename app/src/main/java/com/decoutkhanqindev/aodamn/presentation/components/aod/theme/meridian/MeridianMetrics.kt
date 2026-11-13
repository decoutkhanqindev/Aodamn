package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.meridian

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSans
import java.time.DateTimeException
import java.time.ZoneId

object MeridianMetrics {
    const val MEDIA_HOLD_MILLIS = 30_000L
    const val LATEST_HOLD_MILLIS = 10_000L
    const val SWAP_MILLIS = 200
    const val FADE_MILLIS = 300
    const val MINUTE_MILLIS = 60_000L
    const val TICK_SLACK_MILLIS = 50L
    const val REFERENCE_SIDE = 393f
    const val LANDSCAPE_LEFT = .55f
    const val FILL_ALPHA = .118f
    const val LABEL_ALPHA = .65f
    const val DATE_ALPHA = .9f
    const val DIAL_STROKE = .045f
    const val MINUTE_HAND = .8f
    const val HOUR_HAND = .5f
    const val HAND_TAIL = .1f
    const val SHADOW_COLOR = 0xFF444444.toInt()
    const val ARTIST_DARKEN = .3f
    const val ARTIST_ALPHA = .8f
    const val SKIP_ALPHA = .8f
    const val TRACK_DARKEN = .7f

    val CardWidth = 150.dp
    val CardPadding = 15.dp
    val DialSize = 100.dp
    val DialMargin = 10.dp
    val CardCorner = 40.dp
    val CardStroke = 2.dp
    val TimeSize = 18.sp
    val LabelSize = 11.sp
    val DateSize = 18.sp

    val PortraitDateTop = 60.dp
    val DateWeatherGap = 12.dp
    val InlineWeatherGap = 14.dp
    val PortraitSide = 40.dp
    val PortraitBottom = 40.dp
    val PortraitMediaGap = 40.dp
    val LandscapePairPadding = 20.dp
    val LandscapeEnd = 40.dp
    val LandscapeLineGap = 14.dp
    val LandscapeMediaGap = 24.dp
    val StatusGlyph = 17.dp
    val SwapSlide = 17.dp
    val LatestMargin = 25.dp
    val TitleSize = 17.sp
    val ArtistSize = 14.sp
    val ArtistGap = 2.dp
    val TitleSpacing = .03.em
    val SkipButton = 43.dp
    val SkipIcon = 19.dp
    val PlayButton = 48.dp
    val PlayDisc = 36.dp
    val PlayIcon = 16.dp
    val PlayGap = 5.dp
    val ProgressTop = 12.dp
    val TrackHeight = 6.dp
    val TrackGap = 5.dp

    val LatestLine = AodLatestLineStyle(
        font = FontFamilyGoogleSans,
        iconSize = 17.dp,
        iconGap = 8.dp,
        textSize = 15.sp,
    )

    fun scale(minSideDp: Float): Float = minSideDp / REFERENCE_SIDE

    fun minuteAngle(minute: Int): Float = minute * 6f

    fun hourAngle(hour: Int, minute: Int): Float = (hour % 12 + minute / 60f) * 30f

    fun zoneIdOf(id: String): ZoneId = try {
        ZoneId.of(id)
    } catch (_: DateTimeException) {
        ZoneId.systemDefault()
    }
}
