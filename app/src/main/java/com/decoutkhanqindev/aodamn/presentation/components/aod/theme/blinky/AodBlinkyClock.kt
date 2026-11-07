package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.blinky

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyRubik
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import java.time.LocalTime

@Composable
fun AodBlinkyEyes(
    eyeSize: Dp,
    color: Color,
    gradient: AodGradient?,
    isLive: Boolean,
    isSleepAllowed: Boolean,
    wakeKey: Any,
    onAsleepChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val openness = remember { mutableFloatStateOf(BlinkyMetrics.OPEN) }
    val currentOnAsleepChange by rememberUpdatedState(onAsleepChange)

    LaunchedEffect(isLive, isSleepAllowed, wakeKey) {
        currentOnAsleepChange(false)
        openness.floatValue = BlinkyMetrics.OPEN
        if (!isLive) return@LaunchedEffect
        val awakeMillis = if (isSleepAllowed) {
            BlinkyMetrics.sleepDelaySeconds(LocalTime.now().hour) * 1_000L
        } else {
            Long.MAX_VALUE
        }
        withTimeoutOrNull(awakeMillis) {
            while (true) {
                animate(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = tween(BlinkyMetrics.BLINK_MILLIS, easing = BlinkyMetrics.BlinkEasing),
                ) { value, _ ->
                    openness.floatValue = BlinkyMetrics.blinkOpenness(value)
                }
                delay(BlinkyMetrics.blinkGapMillis())
            }
        }
        currentOnAsleepChange(true)
        delay(BlinkyMetrics.SLEEP_WAIT_MILLIS)
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(BlinkyMetrics.SLEEP_MILLIS, easing = BlinkyMetrics.BlinkEasing),
        ) { value, _ ->
            openness.floatValue = BlinkyMetrics.sleepOpenness(value)
        }
    }

    Spacer(
        modifier = modifier
            .fillMaxSize()
            .drawWithCache {
                val eye = eyeSize.toPx()
                val centerY = size.height / 2f
                val bottom = centerY + eye * BlinkyMetrics.LOWER_LID
                val corner = CornerRadius(eye * BlinkyMetrics.EYE_CORNER)
                val leftEye = size.width / 2f - eye * 1.5f
                val rightEye = size.width / 2f + eye * 0.5f
                val openTop = centerY - eye * BlinkyMetrics.OPEN
                val leftBrush = gradient?.toBrush(Rect(leftEye, openTop, leftEye + eye, bottom)) ?: SolidColor(color)
                val rightBrush = gradient?.toBrush(Rect(rightEye, openTop, rightEye + eye, bottom)) ?: SolidColor(color)

                onDrawBehind {
                    val top = centerY - eye * openness.floatValue
                    val eyeSizePx = Size(eye, bottom - top)

                    drawRoundRect(brush = leftBrush, topLeft = Offset(leftEye, top), size = eyeSizePx, cornerRadius = corner)
                    drawRoundRect(brush = rightBrush, topLeft = Offset(rightEye, top), size = eyeSizePx, cornerRadius = corner)
                }
            },
    )
}

@Composable
fun AodBlinkyTime(
    nowMillis: Long,
    brush: Brush,
    sizeOption: Int,
    isHidden: Boolean,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val format = rememberFormatter(if (is24HourFormat()) R.string.aod_time_pattern_24h else R.string.aod_blinky_time_12h)

    Text(
        text = now.format(format),
        modifier = modifier.alpha(if (isHidden) 0f else 1f),
        style = TextStyle(
            brush = brush,
            fontSize = BlinkyMetrics.clockTextSize(sizeOption),
            fontFamily = FontFamilyRubik,
            fontWeight = FontWeight.Normal,
        ),
        maxLines = 1,
    )
}
