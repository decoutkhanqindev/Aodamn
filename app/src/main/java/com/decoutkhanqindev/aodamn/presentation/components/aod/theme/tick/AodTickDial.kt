package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tick

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tick.TickThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

@Composable
fun AodTickDial(
    nowMillis: Long,
    isLive: Boolean,
    isDateShown: Boolean,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
    below: @Composable () -> Unit = {},
) {
    val clockMillis = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val minuteMillis by remember(isLive, nowMillis) {
        derivedStateOf { if (isLive) clockMillis.longValue / TickMetrics.MINUTE_MILLIS * TickMetrics.MINUTE_MILLIS else nowMillis }
    }

    if (isLive) {
        LaunchedEffect(Unit) {
            while (true) {
                withFrameMillis { clockMillis.longValue = System.currentTimeMillis() }
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val unit = min(maxWidth, maxHeight) / 2

        TickRing(
            secondsMillis = { if (isLive) clockMillis.longValue else nowMillis },
            config = config,
            fadeOffset = max(maxWidth, maxHeight) / 2 * TickMetrics.FADE_OFFSET,
            modifier = Modifier.size(unit * 2),
        )

        TickFace(
            unit = unit,
            epochMillis = minuteMillis,
            isDateShown = isDateShown,
            config = config,
            below = below,
        )
    }
}

@Composable
private fun TickRing(
    secondsMillis: () -> Long,
    config: AodThemeConfig,
    fadeOffset: Dp,
    modifier: Modifier = Modifier,
) {
    val ringGradient = config.gradientOf(TickThemeSpec.RingColor)
    val ringColor = config[TickThemeSpec.RingColor]
    val clockGradient = config.gradientOf(TickThemeSpec.ClockColor)
    val clockColor = config[TickThemeSpec.ClockColor]
    val secondPaint = rememberTickSecondPaint()
    val labels = remember { List(TickMetrics.TICK_COUNT) { second -> second.toString().padStart(2, '0') } }

    Spacer(
        modifier = modifier
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithCache {
                val middle = size.center
                val unit = min(size.width, size.height) / 2f
                val inner = TickMetrics.TICK_INNER * unit
                val outer = TickMetrics.TICK_OUTER * unit
                val stroke = TickMetrics.STROKE * unit
                val reach = TickMetrics.GRADIENT_REACH * unit
                val ringBrush = ringGradient?.toBrush(Rect(middle.x - reach, middle.y - reach, middle.x + reach, middle.y + reach))
                    ?: SolidColor(ringColor)
                val offset = fadeOffset.toPx()
                val fadeTop = Offset(middle.x, middle.y - offset)
                val fadeBottom = Offset(middle.x, middle.y + offset)
                val fadeTopBrush = fadeBrush(fadeTop, unit)
                val fadeBottomBrush = fadeBrush(fadeBottom, unit)
                val bubble = Offset(middle.x + TickMetrics.SECOND_OFFSET * unit, middle.y)
                val bubbleRadius = TickMetrics.SECOND_RADIUS * unit
                val bubbleBounds = Rect(bubble, bubbleRadius)
                val bubbleBrush = clockGradient?.toBrush(bubbleBounds) ?: SolidColor(clockColor)
                val bubbleStroke = Stroke(width = stroke)
                val textBounds = android.graphics.Rect()
                secondPaint.textSize = TickMetrics.SECOND_SIZE * unit
                secondPaint.color = clockColor.toArgb()
                secondPaint.shader = clockGradient?.toShader(bubbleBounds.left, bubbleBounds.top, bubbleBounds.right, bubbleBounds.bottom)

                onDrawBehind {
                    val seconds = Math.floorMod(secondsMillis(), TickMetrics.MINUTE_MILLIS) / 1000f
                    val turn = seconds * TickMetrics.DEGREES_PER_SECOND

                    for (index in 0 until TickMetrics.TICK_COUNT) {
                        val radians = Math.toRadians((turn + index * TickMetrics.DEGREES_PER_SECOND).toDouble())
                        val dx = sin(radians).toFloat()
                        val dy = -cos(radians).toFloat()
                        drawLine(
                            brush = ringBrush,
                            start = Offset(middle.x + dx * inner, middle.y + dy * inner),
                            end = Offset(middle.x + dx * outer, middle.y + dy * outer),
                            strokeWidth = stroke,
                        )
                    }
                    drawCircle(brush = fadeTopBrush, radius = unit, center = fadeTop, blendMode = BlendMode.DstOut)
                    drawCircle(brush = fadeBottomBrush, radius = unit, center = fadeBottom, blendMode = BlendMode.DstOut)
                    drawCircle(color = Black, radius = bubbleRadius, center = bubble, blendMode = BlendMode.Clear)
                    drawCircle(brush = bubbleBrush, radius = bubbleRadius, center = bubble, style = bubbleStroke)

                    val label = labels[seconds.toInt().coerceIn(0, TickMetrics.TICK_COUNT - 1)]
                    secondPaint.getTextBounds(label, 0, label.length, textBounds)
                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawText(
                            label,
                            bubble.x - textBounds.exactCenterX(),
                            bubble.y - textBounds.exactCenterY(),
                            secondPaint,
                        )
                    }
                }
            },
    )
}

private fun fadeBrush(center: Offset, radius: Float): Brush = Brush.radialGradient(
    TickMetrics.FADE_SOLID to Black,
    1f to Color.Transparent,
    center = center,
    radius = radius,
)

@Composable
private fun TickFace(
    unit: Dp,
    epochMillis: Long,
    isDateShown: Boolean,
    config: AodThemeConfig,
    below: @Composable () -> Unit,
) {
    val scale = config[TickThemeSpec.Size] / 100f
    val time = rememberZonedDateTime(epochMillis)
    val clock = time.format(rememberFormatter(if (is24HourFormat()) R.string.aod_tick_time_24h else R.string.aod_tick_time_12h))
    val date = time.format(rememberFormatter(R.string.aod_tick_date_pattern)).uppercase()
    val density = LocalDensity.current
    val clockSize = with(density) { (unit * TickMetrics.CLOCK_SIZE * scale).toSp() }
    val dateSize = with(density) { (unit * TickMetrics.DATE_SIZE * scale).toSp() }
    val gap = with(density) { TickMetrics.WeatherGap.roundToPx() }

    Layout(
        content = {
            Box {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = clock,
                        style = LocalTextStyle.current.merge(TextStyle(brush = config.brushOf(TickThemeSpec.ClockColor))),
                        fontSize = clockSize,
                        fontFamily = TickThemeSpec.Font,
                        letterSpacing = TickMetrics.ClockSpacing,
                        maxLines = 1,
                        softWrap = false,
                    )

                    AnimatedVisibility(visible = isDateShown) {
                        Text(
                            text = date,
                            style = LocalTextStyle.current.merge(TextStyle(brush = config.brushOf(TickThemeSpec.DateColor))),
                            fontSize = dateSize,
                            fontFamily = TickThemeSpec.Font,
                            letterSpacing = TickMetrics.DateSpacing,
                            maxLines = 1,
                            softWrap = false,
                        )
                    }
                }
            }

            Box { below() }
        },
        modifier = Modifier.fillMaxSize(),
    ) { measurables, constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val face = measurables[0].measure(loose)
        val extra = measurables[1].measure(loose)

        layout(constraints.maxWidth, constraints.maxHeight) {
            val centerX = constraints.maxWidth / 2
            val centerY = constraints.maxHeight / 2
            face.place(centerX - face.width / 2, centerY - face.height / 2)
            extra.place(centerX - extra.width / 2, centerY + face.height / 2 + gap)
        }
    }
}

@Composable
private fun rememberTickSecondPaint(): Paint {
    val typeface = LocalFontFamilyResolver.current
        .resolve(fontFamily = TickThemeSpec.Font, fontWeight = FontWeight.Normal)
        .value as? Typeface ?: Typeface.DEFAULT
    return remember(typeface) {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.typeface = typeface
            isSubpixelText = true
        }
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodTickDialPreview() {
    AodTickDial(
        nowMillis = System.currentTimeMillis(),
        isLive = false,
        isDateShown = true,
        config = AodThemeConfig(),
    )
}
