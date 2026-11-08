package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pixel

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodTextPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSans
import kotlin.math.min

object PixelMetrics {
    const val MEDIA_HOLD_MILLIS = 30_000L
    const val LATEST_HOLD_MILLIS = 10_000L
    const val SWAP_MILLIS = 200
    const val FADE_MILLIS = 300
    const val MINUTE_MILLIS = 60_000L
    const val TICK_SLACK_MILLIS = 50L
    const val SIZE_FACTOR = .18f
    const val LANDSCAPE_WIDTH_FACTOR = .4f
    const val LETTER_SPACING = .02f
    const val OUTLINE_FRACTION = .025f
    const val PORTRAIT_CLOCK_BOTTOM = .27f
    const val DATE_SPACING = .48f
    const val PORTRAIT_BOTTOM = .95f
    const val LANDSCAPE_COLUMN = .45f
    const val ARTIST_DARKEN = .3f
    const val PAUSED_ALPHA = .6f
    const val TRACK_ALPHA = .3f
    const val STATUS_GAP_SCALE = .87f

    val LandscapePadding = 40.dp
    val PortraitDetailsTop = 40.dp
    val PortraitDetailsBottom = 10.dp
    val LandscapeDetailsTop = 30.dp
    val LandscapeBottom = 35.dp
    val ProgressGap = 30.dp
    val ProgressInset = 30.dp
    val PlayerMargin = 20.dp
    val TrackHeight = 2.dp
    val StatusGlyph = 15.3.dp
    val LatestMargin = 25.dp
    val SwapSlide = 17.dp
    val DateGap = 23.dp
    val DateWeatherGap = 12.dp
    val DateSize = 13.sp

    val TextPlayer = AodTextPlayerStyle(
        fontFamily = FontFamilyGoogleSans,
        titleSize = 13.sp,
        artistSize = 11.5.sp,
        artistGap = 8.dp,
        buttonSize = 40.dp,
        buttonIconSize = 12.dp,
        pausedAlpha = PAUSED_ALPHA,
        textGap = 10.dp,
        letterSpacing = .38.em,
        isUppercase = true,
        previousIconRes = R.drawable.ic_caret_left_light,
        nextIconRes = R.drawable.ic_caret_right_light,
    )

    val LatestLine = AodLatestLineStyle(
        font = FontFamilyGoogleSans,
        iconSize = 15.dp,
        iconGap = 15.dp,
        textSize = 12.sp,
        letterSpacing = .2.em,
        isUppercase = true,
    )

    fun clockSizePx(
        widthPx: Float,
        heightPx: Float,
        isLandscape: Boolean,
        sizePercent: Int,
    ): Float = min(if (isLandscape) widthPx * LANDSCAPE_WIDTH_FACTOR else widthPx, heightPx) * sizePercent / 100f * SIZE_FACTOR

    fun blockTop(
        isLandscape: Boolean,
        heightPx: Float,
        blockHeight: Float,
    ): Float = if (isLandscape) (heightPx - blockHeight) / 2f else heightPx * PORTRAIT_CLOCK_BOTTOM - blockHeight

    fun centerX(
        isLandscape: Boolean,
        widthPx: Float,
    ): Float = if (isLandscape) widthPx * LANDSCAPE_COLUMN / 2f else widthPx / 2f
}
