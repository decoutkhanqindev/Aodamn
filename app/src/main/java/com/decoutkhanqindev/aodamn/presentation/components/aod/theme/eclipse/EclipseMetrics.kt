package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.eclipse

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodTextPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.eclipse.EclipseThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.White

object EclipseMetrics {
    const val DISC_RADIUS = 0.25f
    const val CORONA_RADIUS = 1f / 3f
    const val CORONA_SHIFT = 0.1f
    const val CORONA_PEEK_SHIFT = 0.06f
    const val INNER_PEEK_OFFSET = 0.005f
    const val INNER_PEEK_SHRINK = 0.003f
    const val INNER_SEAM_PX = 0.3f
    const val RIM_BLUR = 0.014f
    const val DISC_ALPHA = 180
    const val DRAW_ROTATION_BASE = 90f
    const val RING_LIGHTEN = 0.8f
    const val RING_DARKEN = 0.8f
    const val SHADOW_MAX_LIGHTNESS = 0.15f
    const val SHADOW_MID_ALPHA = 0.5f
    const val TEXT_DARKEN = 0.4f
    const val PAUSED_ALPHA = 0.6f
    const val OPEN_MILLIS = 2_000
    const val SPACING_START = 1.8f
    const val SPACING_END = 2.1f
    const val TEXT_HALF_WIDTH_EM = 6.5f
    const val MINUTE_MILLIS = 60_000L
    const val TICK_SLACK_MILLIS = 50L
    const val MEDIA_HOLD_MILLIS = 30_000L
    const val LATEST_HOLD_MILLIS = 10_000L
    const val SWAP_MILLIS = 200
    const val FADE_MILLIS = 300
    const val GAP_SCALE = 0.73f

    val PortraitDigit = 25.sp
    val LandscapeDigit = 22.sp
    val PortraitDate = 14.sp
    val LandscapeDate = 13.sp
    val DateSpacing = 0.4.em
    val PortraitDateGap = 18.dp
    val LandscapeDateGap = 14.dp
    val PortraitBottomMargin = 60.dp
    val LandscapeInset = 30.dp
    val LandscapeBandGap = 30.dp
    val LandscapeBand = 46.dp
    val DetailsPadding = 30.dp
    val StatusGlyph = 14.5.dp
    val SwapSlide = 17.dp
    val PlayerMargin = 20.dp

    val CoronaStops = floatArrayOf(0.35f, 0.4f, 0.45f, 0.7f, 0.95f)

    val TextPlayer = AodTextPlayerStyle(
        fontFamily = EclipseThemeSpec.Font,
        titleSize = 12.sp,
        artistSize = 11.sp,
        artistGap = 8.dp,
        buttonSize = 40.dp,
        buttonIconSize = 16.dp,
        pausedAlpha = PAUSED_ALPHA,
        textGap = 10.dp,
        letterSpacing = 0.4.em,
        isUppercase = true,
        previousIconRes = R.drawable.ic_caret_left_light,
        nextIconRes = R.drawable.ic_caret_right_light,
    )

    val LatestLine = AodLatestLineStyle(
        font = EclipseThemeSpec.Font,
        iconSize = 16.dp,
        iconGap = 10.dp,
        textSize = 12.sp,
        letterSpacing = 0.2.em,
        isUppercase = true,
    )

    fun hourFraction(hourOfDay: Int, minute: Int): Float = (hourOfDay % 12 + minute / 60f) / 12f

    fun rotationDegrees(hourFraction: Float, pinnedDegrees: Int): Float =
        DRAW_ROTATION_BASE + if (pinnedDegrees == AodRangeOption.AUTO) hourFraction * 360f else pinnedDegrees.toFloat()

    fun peek(hourFraction: Float, hourOfDay: Int, pinnedPercent: Int): Float = when {
        pinnedPercent != AodRangeOption.AUTO -> pinnedPercent / 100f
        hourOfDay >= 12 -> 1f - hourFraction
        else -> hourFraction
    }

    fun innerOffset(span: Float, peek: Float): Float = span * INNER_PEEK_OFFSET * peek

    fun innerRadius(span: Float, peek: Float): Float =
        span * DISC_RADIUS - span * INNER_PEEK_SHRINK * peek - INNER_SEAM_PX

    fun coronaX(centerX: Float, span: Float, peek: Float): Float =
        centerX - span * CORONA_SHIFT - span * CORONA_PEEK_SHIFT * peek

    fun letterSpacing(reveal: Float): Float = SPACING_START + (SPACING_END - SPACING_START) * reveal

    fun landscapeCenterY(height: Float, inset: Float, bandGap: Float, band: Float): Float =
        inset + (height - inset * 2f - bandGap - band) / 2f

    fun mix(from: Color, to: Color, fraction: Float): Color = Color(
        red = from.red + (to.red - from.red) * fraction,
        green = from.green + (to.green - from.green) * fraction,
        blue = from.blue + (to.blue - from.blue) * fraction,
        alpha = from.alpha + (to.alpha - from.alpha) * fraction,
    )

    fun capLightness(color: Color, maxLightness: Float): Color {
        val high = maxOf(color.red, color.green, color.blue)
        val low = minOf(color.red, color.green, color.blue)
        val lightness = (high + low) / 2f
        if (lightness <= maxLightness) return color
        val chroma = high - low
        if (chroma == 0f) return Color(maxLightness, maxLightness, maxLightness, color.alpha)
        val saturation = if (lightness <= 0.5f) chroma / (2f * lightness) else chroma / (2f - 2f * lightness)
        val newChroma = saturation * 2f * maxLightness
        return Color(
            red = maxLightness + (color.red - lightness) * newChroma / chroma,
            green = maxLightness + (color.green - lightness) * newChroma / chroma,
            blue = maxLightness + (color.blue - lightness) * newChroma / chroma,
            alpha = color.alpha,
        )
    }

    fun coronaColors(ring: List<Color>, shadow: List<Color>): IntArray = intArrayOf(
        mix(ring.first(), White, RING_LIGHTEN).toArgb(),
        ring.last().toArgb(),
        capLightness(shadow.first(), SHADOW_MAX_LIGHTNESS).toArgb(),
        capLightness(shadow.last(), SHADOW_MAX_LIGHTNESS).copy(alpha = SHADOW_MID_ALPHA).toArgb(),
        Color.Transparent.toArgb(),
    )

    fun rimColors(ring: List<Color>): IntArray = intArrayOf(
        mix(ring.first(), White, RING_LIGHTEN).toArgb(),
        mix(ring.last(), Black, RING_DARKEN).toArgb(),
    )
}
