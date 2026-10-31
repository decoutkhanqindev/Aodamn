package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass

import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.AodCompassStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.CompassThemeSpec
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.min

@Composable
fun AodCompassDial(
    nowMillis: Long,
    isLive: Boolean,
    config: AodThemeConfig,
    paints: CompassPaints,
    modifier: Modifier = Modifier,
) {
    val hasSeconds = config[CompassThemeSpec.Style] == AodCompassStyleValue.SECONDS
    val sizePercent = config[CompassThemeSpec.Size]
    val hourColor = config[CompassThemeSpec.HourColor]
    val hourGradient = config.gradientOf(CompassThemeSpec.HourColor)
    val halfHourColor = config[CompassThemeSpec.HalfHourColor]
    val halfHourGradient = config.gradientOf(CompassThemeSpec.HalfHourColor)
    val minuteColor = config[CompassThemeSpec.MinuteColor]
    val minuteGradient = config.gradientOf(CompassThemeSpec.MinuteColor)
    val secondColor = config[CompassThemeSpec.SecondColor]
    val secondGradient = config.gradientOf(CompassThemeSpec.SecondColor)
    val description = rememberZonedDateTime(nowMillis).format(rememberTimeFormatter())
    val clockMillis = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val secondsFraction = animateFloatAsState(
        targetValue = if (hasSeconds) 1f else 0f,
        animationSpec = tween(durationMillis = CompassMetrics.STYLE_MILLIS),
        label = "CompassSeconds",
    )

    if (isLive) {
        LaunchedEffect(hasSeconds) {
            while (true) {
                val now = System.currentTimeMillis()
                clockMillis.longValue = now
                delay(
                    if (hasSeconds) {
                        100L - now % 100L
                    } else {
                        CompassTime.millisToNextMinute(now)
                    }
                )
            }
        }
    }

    val stepMillis = if (hasSeconds) 0L else if (isLive) clockMillis.longValue else nowMillis
    val stepped = remember { CompassTime(if (isLive) System.currentTimeMillis() else nowMillis) }
    val minuteDegrees = remember { Animatable(stepped.minutes * 6f) }
    val hourDegrees = remember { Animatable(stepped.hours * 30f) }
    val wasSeconds = remember { mutableStateOf(hasSeconds) }

    LaunchedEffect(hasSeconds, stepMillis) {
        if (hasSeconds) {
            wasSeconds.value = true
            return@LaunchedEffect
        }
        if (wasSeconds.value) {
            val current = CompassTime(if (isLive) System.currentTimeMillis() else nowMillis)
            minuteDegrees.snapTo(current.minutes * 6f)
            hourDegrees.snapTo(current.hours * 30f)
            wasSeconds.value = false
        }
        val target = CompassTime(stepMillis)
        launch { minuteDegrees.turnTo(target.minutes * 6f) }
        launch { hourDegrees.turnTo(target.hours * 30f) }
    }

    Spacer(
        modifier = modifier
            .fillMaxSize()
            .clearAndSetSemantics { contentDescription = description }
            .drawWithCache {
                val center = size.center
                val unit = min(size.width, size.height) * CompassMetrics.DIAL_FRACTION * sizePercent / 100f / 2f
                val ringSize = ceil(unit * 2f).toInt().coerceAtLeast(1).let { side -> IntSize(side, side) }
                val lineWidth = CompassMetrics.LineWidth.toPx()
                val secondRing = obtainGraphicsLayer().apply {
                    record(size = ringSize) { drawCompassSecondRing(paints, unit, secondColor, secondGradient) }
                }
                val minuteRing = obtainGraphicsLayer().apply {
                    record(size = ringSize) { drawCompassMinuteRing(paints, unit, minuteColor, minuteGradient, lineWidth) }
                }
                val hourRing = obtainGraphicsLayer().apply {
                    record(size = ringSize) { drawCompassHourRing(paints, unit, hourColor, hourGradient, halfHourColor, halfHourGradient) }
                }
                val pointerEnd = Offset(center.x + unit, center.y)
                val circleStroke = Stroke(width = CompassMetrics.CircleWidth.toPx())
                val outer = CompassMetrics.CIRCLE_OUTER * unit
                val circleBrush = minuteGradient?.toBrush(Rect(center = center, radius = outer)) ?: SolidColor(minuteColor)

                onDrawBehind {
                    val fraction = secondsFraction.value
                    val time = CompassTime(if (isLive) clockMillis.longValue else nowMillis)
                    val pointerStart = Offset(center.x + (1f - fraction) * CompassMetrics.POINTER_GAP * unit, center.y)
                    val headEnd = pointerStart.x + CompassMetrics.POINTER_HEAD * unit
                    val headSpread = CompassMetrics.POINTER_SPREAD * unit

                    val pointerBrush = hourGradient?.let { gradient ->
                        Brush.linearGradient(colors = gradient.colors, start = pointerStart, end = pointerEnd)
                    } ?: SolidColor(hourColor)

                    drawLine(pointerBrush, pointerStart, pointerEnd, strokeWidth = lineWidth, cap = StrokeCap.Round)
                    drawLine(pointerBrush, pointerStart, Offset(headEnd, center.y - headSpread), strokeWidth = lineWidth, cap = StrokeCap.Round)
                    drawLine(pointerBrush, pointerStart, Offset(headEnd, center.y + headSpread), strokeWidth = lineWidth, cap = StrokeCap.Round)
                    if (fraction > 0f) {
                        secondRing.alpha = fraction
                        drawRing(secondRing, ringSize, time.seconds * 6f)
                    }
                    drawRing(minuteRing, ringSize, if (hasSeconds) time.minutes * 6f else minuteDegrees.value)
                    drawRing(hourRing, ringSize, if (hasSeconds) time.hours * 30f else hourDegrees.value)
                    drawCircle(circleBrush, outer, alpha = CompassMetrics.CIRCLE_ALPHA, style = circleStroke)
                    drawCircle(circleBrush, CompassMetrics.CIRCLE_INNER * unit, alpha = CompassMetrics.CIRCLE_ALPHA, style = circleStroke)
                }
            },
    )
}

@Composable
fun rememberCompassPaints(): CompassPaints {
    val typeface = resolveTypeface(fontFamily = CompassThemeSpec.NumberFont, fontWeight = FontWeight.Normal, fallback = Typeface.DEFAULT)
    return remember(typeface) { CompassPaints(typeface) }
}

private suspend fun Animatable<Float, *>.turnTo(degrees: Float) {
    val delta = ((degrees - value) % 360f + 540f) % 360f - 180f
    animateTo(targetValue = value + delta, animationSpec = tween(durationMillis = CompassMetrics.STEP_MILLIS))
}

private fun DrawScope.drawRing(ring: GraphicsLayer, ringSize: IntSize, degrees: Float) {
    rotate(degrees = degrees, pivot = center) {
        translate(left = center.x - ringSize.width / 2f, top = center.y - ringSize.height / 2f) {
            drawLayer(ring)
        }
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodCompassDialPreview() {
    AodCompassDial(
        nowMillis = System.currentTimeMillis(),
        isLive = false,
        config = AodThemeConfig(),
        paints = rememberCompassPaints(),
    )
}
