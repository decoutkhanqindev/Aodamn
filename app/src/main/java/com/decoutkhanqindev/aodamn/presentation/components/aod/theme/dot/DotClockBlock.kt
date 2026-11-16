package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.dot

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.dot.DotThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyDoto
import kotlin.math.ceil
import kotlin.math.roundToInt

@Composable
fun AodDotClock(
    state: AodState,
    size: Dp,
    scale: Float,
    modifier: Modifier = Modifier,
) {
    val time = rememberZonedDateTime(state.nowMillis)
    val hour = time.format(rememberFormatter(if (is24HourFormat()) R.string.aod_dot_hour_24h else R.string.aod_dot_hour_12h))
    val minute = time.format(rememberFormatter(R.string.aod_dot_minute))
    val typeface = resolveTypeface(FontFamilyDoto, FontWeight.Black, Typeface.DEFAULT)
    val density = LocalDensity.current
    val sizePx = with(density) { size.toPx() }
    val gapPx = with(density) { (DotMetrics.DigitDotGap * scale).toPx() }
    val dotDiameter = sizePx * DotMetrics.SEPARATOR_DOT
    val dotOffset = sizePx * DotMetrics.SEPARATOR_OFFSET
    val measurement = remember(hour, minute, sizePx, typeface) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = sizePx
            this.typeface = typeface
        }
        val zeroBounds = Rect()
        paint.getTextBounds("0", 0, 1, zeroBounds)
        paint.textSize = sizePx * (sizePx * DotMetrics.DIGIT_HEIGHT) / zeroBounds.height().coerceAtLeast(1)
        val hourBounds = Rect()
        val minuteBounds = Rect()
        paint.getTextBounds(hour, 0, hour.length, hourBounds)
        paint.getTextBounds(minute, 0, minute.length, minuteBounds)
        paint.textSize to arrayOf(hourBounds, minuteBounds)
    }
    val textSizePx = measurement.first
    val hourBounds = measurement.second[0]
    val minuteBounds = measurement.second[1]
    val widthPx = maxOf(hourBounds.width().toFloat(), minuteBounds.width().toFloat(), dotOffset * 2f + dotDiameter)
    val heightPx = hourBounds.height() + gapPx + dotDiameter + gapPx + minuteBounds.height()
    val config = state.appearance.theme.config
    val hourColor = config[DotThemeSpec.HourColor]
    val minuteColor = config[DotThemeSpec.MinuteColor]
    val dotsColor = config[DotThemeSpec.DotsColor]
    val hourGradient = config.gradientOf(DotThemeSpec.HourColor)
    val minuteGradient = config.gradientOf(DotThemeSpec.MinuteColor)
    val dotsGradient = config.gradientOf(DotThemeSpec.DotsColor)

    Box(
        modifier = modifier
            .layout { measurable, constraints ->
                val width = ceil(widthPx).toInt().coerceIn(constraints.minWidth, constraints.maxWidth)
                val height = ceil(heightPx).toInt().coerceIn(constraints.minHeight, constraints.maxHeight)
                val placeable = measurable.measure(
                    constraints.copy(minWidth = width, maxWidth = width, minHeight = height, maxHeight = height),
                )
                layout(width, height) { placeable.place(0, 0) }
            }
            .drawWithCache {
                val hourLeft = (this.size.width - hourBounds.width()) / 2f
                val hourTop = 0f
                val separatorTop = hourBounds.height() + gapPx
                val separatorCenterY = separatorTop + dotDiameter / 2f
                val minuteLeft = (this.size.width - minuteBounds.width()) / 2f
                val minuteTop = separatorTop + dotDiameter + gapPx
                val dotsLeft = this.size.width / 2f - dotOffset - dotDiameter / 2f
                val dotsRight = this.size.width / 2f + dotOffset + dotDiameter / 2f
                val hourPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    textSize = textSizePx
                    this.typeface = typeface
                    color = hourColor.toArgb()
                    shader = hourGradient?.toShader(
                        hourLeft,
                        hourTop,
                        hourLeft + hourBounds.width(),
                        hourTop + hourBounds.height(),
                    )
                }
                val minutePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    textSize = textSizePx
                    this.typeface = typeface
                    color = minuteColor.toArgb()
                    shader = minuteGradient?.toShader(
                        minuteLeft,
                        minuteTop,
                        minuteLeft + minuteBounds.width(),
                        minuteTop + minuteBounds.height(),
                    )
                }
                val dotsPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = dotsColor.toArgb()
                    shader = dotsGradient?.toShader(dotsLeft, separatorTop, dotsRight, separatorTop + dotDiameter)
                }

                onDrawBehind {
                    drawContext.canvas.nativeCanvas.drawText(
                        hour,
                        hourLeft - hourBounds.left,
                        hourTop - hourBounds.top,
                        hourPaint,
                    )
                    drawContext.canvas.nativeCanvas.drawCircle(center.x - dotOffset, separatorCenterY, dotDiameter / 2f, dotsPaint)
                    drawContext.canvas.nativeCanvas.drawCircle(center.x + dotOffset, separatorCenterY, dotDiameter / 2f, dotsPaint)
                    drawContext.canvas.nativeCanvas.drawText(
                        minute,
                        minuteLeft - minuteBounds.left,
                        minuteTop - minuteBounds.top,
                        minutePaint,
                    )
                }
            },
    )
}
