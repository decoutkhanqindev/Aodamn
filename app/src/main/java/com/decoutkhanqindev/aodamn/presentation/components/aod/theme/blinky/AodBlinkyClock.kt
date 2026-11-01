package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.blinky

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.theme.White
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun AodBlinkyEyes(
    eyeSize: Dp,
    color: Color,
    gradient: AodGradient?,
    nowMillis: Long,
    isLive: Boolean,
    modifier: Modifier = Modifier,
) {
    val openness = remember { Animatable(BlinkyMetrics.OPENNESS) }
    var isInitial by remember { mutableStateOf(true) }

    LaunchedEffect(isLive, nowMillis) {
        if (!isLive) {
            openness.snapTo(BlinkyMetrics.OPENNESS)
            isInitial = true
        } else {
            if (isInitial) {
                isInitial = false
                delay(BlinkyMetrics.INITIAL_DELAY_MILLIS)
            }
            while (true) {
                animateBlinky(openness)
                delay(nextBlinkyDelayMillis())
            }
        }
    }

    Canvas(
        modifier = modifier
            .size(width = eyeSize * BlinkyMetrics.EYE_WIDTH, height = eyeSize * BlinkyMetrics.EYE_HEIGHT)
            .drawWithCache {
                val height = size.height * (openness.value + BlinkyMetrics.EYE_CORNER)
                val corner = eyeSize.toPx() * BlinkyMetrics.EYE_CORNER
                val left = Rect(
                    left = 0f,
                    top = (size.height - height) / 2f,
                    right = eyeSize.toPx(),
                    bottom = (size.height + height) / 2f,
                )
                val right = left.translate(eyeSize.toPx() * 2f, 0f)
                val leftPath = Path().apply { addRoundRect(RoundRect(left, CornerRadius(corner))) }
                val rightPath = Path().apply { addRoundRect(RoundRect(right, CornerRadius(corner))) }
                val leftBrush = gradient?.toBrush(left) ?: Brush.linearGradient(listOf(color, color))
                val rightBrush = gradient?.toBrush(right) ?: Brush.linearGradient(listOf(color, color))

                onDrawBehind {
                    clipPath(leftPath) { drawPath(leftPath, brush = leftBrush) }
                    clipPath(rightPath) { drawPath(rightPath, brush = rightBrush) }
                }
            },
    ) {}
}

@Composable
fun AodBlinkyTime(
    nowMillis: Long,
    color: Color,
    gradient: AodGradient?,
    size: Float,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val format = rememberFormatter(if (is24HourFormat()) R.string.aod_time_pattern_24h else R.string.aod_time_pattern_12h)

    Text(
        text = now.format(format),
        modifier = modifier,
        color = color,
        style = TextStyle(brush = gradient?.toBrush()),
        fontSize = size.sp,
    )
}

@Composable
private fun BlinkyTimePreview() {
    Box(contentAlignment = Alignment.Center) {
        AodBlinkyTime(nowMillis = System.currentTimeMillis(), color = White, gradient = null, size = 28.6f)
    }
}

private suspend fun animateBlinky(openness: Animatable<Float, *>) {
    openness.animateTo(BlinkyMetrics.CLOSED_OPENNESS, tween(BlinkyMetrics.BLINK_MILLIS))
    openness.animateTo(BlinkyMetrics.OPENNESS, tween(BlinkyMetrics.BLINK_MILLIS))
}

private fun nextBlinkyDelayMillis(): Long {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    val seconds = if (hour >= 22 || hour <= 6) {
        Random.nextInt(BlinkyMetrics.NIGHT_DELAY_MIN_SECONDS, BlinkyMetrics.NIGHT_DELAY_MAX_SECONDS + 1)
    } else {
        Random.nextInt(BlinkyMetrics.DAY_DELAY_MIN_SECONDS, BlinkyMetrics.DAY_DELAY_MAX_SECONDS + 1)
    }
    return seconds * 1_000L
}
