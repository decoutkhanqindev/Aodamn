package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.quarters

import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.rememberLiveClockMillis
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toLineShader
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.quarters.QuartersThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.BlackAlpha10
import com.decoutkhanqindev.aodamn.presentation.theme.BlackAlpha50
import java.util.TimeZone
import kotlin.math.min

@Composable
fun AodQuartersDial(
    nowMillis: Long,
    isLive: Boolean,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val style = config[QuartersThemeSpec.DigitStyle]
    val scale = config[QuartersThemeSpec.Size] / 100f
    val handOutline = animateFloatAsState(
        targetValue = if (config[QuartersThemeSpec.HandStyle] == AodTileStyleValue.OUTLINE) 1f else 0f,
        animationSpec = tween(durationMillis = QuartersMetrics.STYLE_MILLIS),
        label = "QuartersHandOutline",
    )
    val description = rememberZonedDateTime(nowMillis).format(rememberTimeFormatter())
    val sharpPaint = rememberQuartersPaint(QuartersThemeSpec.DigitFont)
    val roundedPaint = rememberQuartersPaint(QuartersThemeSpec.RoundedDigitFont)
    val clockMillis = rememberLiveClockMillis(isLive)

    Box(modifier = modifier.clearAndSetSemantics { contentDescription = description }) {
        Crossfade(
            targetState = style,
            modifier = Modifier.fillMaxSize(),
            animationSpec = tween(durationMillis = QuartersMetrics.STYLE_MILLIS),
            label = "QuartersDigitStyle",
        ) { digitStyle ->
            QuartersDigits(
                paint = if (digitStyle.isRounded) roundedPaint else sharpPaint,
                isOutline = digitStyle.isOutline,
                scale = scale,
                config = config,
                modifier = Modifier.fillMaxSize(),
            )
        }

        QuartersHands(
            epochMillis = { if (isLive) clockMillis.longValue else nowMillis },
            outline = { handOutline.value },
            scale = scale,
            config = config,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun QuartersDigits(
    paint: Paint,
    isOutline: Boolean,
    scale: Float,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val gradient = config.gradientOf(QuartersThemeSpec.DigitColor)
    val color = config[QuartersThemeSpec.DigitColor]

    Spacer(
        modifier = modifier.drawWithCache {
            val radius = scale * min(size.width, size.height) / 2f
            paint.textSize = QuartersMetrics.TEXT_SIZE * radius
            val digits = listOf(
                QuartersDigit(text = "12", degrees = 0f, offset = QuartersMetrics.END_DIGIT_OFFSET),
                QuartersDigit(text = "3", degrees = 90f, offset = QuartersMetrics.SIDE_DIGIT_OFFSET),
                QuartersDigit(text = "6", degrees = 180f, offset = QuartersMetrics.END_DIGIT_OFFSET),
                QuartersDigit(text = "9", degrees = 270f, offset = QuartersMetrics.SIDE_DIGIT_OFFSET),
            ).map { digit -> paint.quartersGlyph(digit, size.center, radius) }
            val shapes = digits.map { digit -> digit.path.asComposePath() }
            val brushes = digits.map { digit ->
                val bounds = digit.bounds
                gradient?.toBrush(Rect(bounds.left, bounds.top, bounds.right, bounds.bottom)) ?: SolidColor(color)
            }
            val stroke = Stroke(width = QuartersMetrics.DigitOutline.toPx())

            onDrawBehind {
                shapes.forEachIndexed { index, shape ->
                    if (isOutline) drawPath(shape, brushes[index], style = stroke) else drawPath(shape, brushes[index])
                }
            }
        },
    )
}

@Composable
private fun QuartersHands(
    epochMillis: () -> Long,
    outline: () -> Float,
    scale: Float,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val hourColors = config.colorsOf(QuartersThemeSpec.HourColor)
    val minuteColors = config.colorsOf(QuartersThemeSpec.MinuteColor)
    val secondColors = config.quartersSecondColorsOf(QuartersThemeSpec.HourColor)

    Spacer(
        modifier = modifier.drawWithCache {
            val middle = size.center
            val radius = scale * min(size.width, size.height) / 2f
            val shadow = QuartersMetrics.SHADOW_RADIUS * radius
            val pill = RectF(
                middle.x + QuartersMetrics.HOUR_START * radius,
                middle.y - QuartersMetrics.HOUR_WIDTH * radius / 2f,
                middle.x + QuartersMetrics.HOUR_END * radius,
                middle.y + QuartersMetrics.HOUR_WIDTH * radius / 2f,
            )
            val corner = pill.height() / 2f
            val pillStart = Offset(pill.left, middle.y)
            val pillEnd = Offset(pill.right, middle.y)
            val hourFill = handPaint(hourColors, pillStart, pillEnd, shadow, Paint.Style.FILL, 0f)
            val hourStroke = handPaint(hourColors, pillStart, pillEnd, shadow, Paint.Style.STROKE, QuartersMetrics.HOUR_OUTLINE * radius)
            val hourDim = handPaint(listOf(BlackAlpha10), pillStart, pillEnd, shadow, Paint.Style.FILL, 0f)
            val minuteTip = Offset(middle.x, middle.y - QuartersMetrics.MINUTE_LENGTH * radius)
            val minute = handPaint(minuteColors, middle, minuteTip, shadow, Paint.Style.STROKE, QuartersMetrics.MINUTE_WIDTH * radius)
            val secondTip = Offset(middle.x, middle.y - QuartersMetrics.SECOND_LENGTH * radius)
            val second = handPaint(secondColors, middle, secondTip, shadow, Paint.Style.STROKE, QuartersMetrics.SECOND_WIDTH * radius)
            val dotRadius = QuartersMetrics.DOT_RADIUS * radius
            val dot = handPaint(secondColors, Offset(middle.x, middle.y - dotRadius), Offset(middle.x, middle.y + dotRadius), 0f, Paint.Style.FILL, 0f)

            onDrawBehind {
                val millis = epochMillis()
                val local = millis + TimeZone.getDefault().getOffset(millis)
                val fraction = outline()
                drawIntoCanvas { canvas ->
                    val native = canvas.nativeCanvas

                    native.save()
                    native.rotate(degreesOf(local, QuartersMetrics.HALF_DAY_MILLIS) - 90f, middle.x, middle.y)
                    if (fraction < 1f) {
                        hourFill.alpha = ((1f - fraction) * 255).toInt()
                        native.drawRoundRect(pill, corner, corner, hourFill)
                    }
                    if (fraction > 0f) {
                        hourDim.alpha = (fraction * BlackAlpha10.alpha * 255).toInt()
                        hourStroke.alpha = (fraction * 255).toInt()
                        native.drawRoundRect(pill, corner, corner, hourDim)
                        native.drawRoundRect(pill, corner, corner, hourStroke)
                    }
                    native.restore()

                    native.save()
                    native.rotate(degreesOf(local, QuartersMetrics.HOUR_MILLIS), middle.x, middle.y)
                    native.drawLine(middle.x, middle.y, minuteTip.x, minuteTip.y, minute)
                    native.restore()

                    native.save()
                    native.rotate(degreesOf(local, QuartersMetrics.MINUTE_MILLIS), middle.x, middle.y)
                    native.drawLine(middle.x, middle.y, secondTip.x, secondTip.y, second)
                    native.restore()

                    native.drawCircle(middle.x, middle.y, dotRadius, dot)
                }
            }
        },
    )
}

private fun handPaint(colors: List<Color>, from: Offset, to: Offset, shadow: Float, style: Paint.Style, width: Float): Paint =
    Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.style = style
        strokeWidth = width
        strokeCap = Paint.Cap.ROUND
        color = colors.first().toArgb()
        shader = colors.toLineShader(from, to)
        if (shadow > 0f) setShadowLayer(shadow, 0f, 0f, BlackAlpha50.toArgb())
    }

private fun degreesOf(localMillis: Long, periodMillis: Long): Float = Math.floorMod(localMillis, periodMillis) * 360f / periodMillis

@Composable
private fun rememberQuartersPaint(fontFamily: FontFamily): Paint {
    val typeface = resolveTypeface(fontFamily = fontFamily, fontWeight = QuartersThemeSpec.DigitWeight, fallback = Typeface.DEFAULT_BOLD)
    return remember(typeface) {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.typeface = typeface
        }
    }
}

@Preview(widthDp = 393, heightDp = 470)
@Composable
private fun AodQuartersDialPreview() {
    AodQuartersDial(
        nowMillis = System.currentTimeMillis(),
        isLive = false,
        config = AodThemeConfig(),
    )
}
