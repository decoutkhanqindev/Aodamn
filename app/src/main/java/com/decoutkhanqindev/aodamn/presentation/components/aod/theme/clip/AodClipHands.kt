package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.clip

import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toLineShader
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.clip.ClipThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Grey44
import kotlinx.coroutines.launch
import java.util.TimeZone
import kotlin.math.min

@Composable
fun AodClipHands(
    nowMillis: Long,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val scale = config[ClipThemeSpec.Size] / ClipMetrics.SIZE_DIVISOR
    val hourColors = config.colorsOf(ClipThemeSpec.HourColor)
    val minuteColors = config.colorsOf(ClipThemeSpec.MinuteColor)
    val dotColors = config.colorsOf(ClipThemeSpec.DotColor)
    val description = rememberZonedDateTime(nowMillis).format(rememberTimeFormatter())
    val minuteDegrees = remember { Animatable(degreesOf(nowMillis, ClipMetrics.HOUR_MILLIS)) }
    val hourDegrees = remember { Animatable(degreesOf(nowMillis, ClipMetrics.HALF_DAY_MILLIS)) }

    LaunchedEffect(nowMillis) {
        launch { minuteDegrees.turnTo(degreesOf(nowMillis, ClipMetrics.HOUR_MILLIS)) }
        launch { hourDegrees.turnTo(degreesOf(nowMillis, ClipMetrics.HALF_DAY_MILLIS)) }
    }

    Spacer(
        modifier = modifier
            .clearAndSetSemantics { contentDescription = description }
            .drawWithCache {
                val middle = size.center
                val radius = scale * min(size.width, size.height) / 2f
                val half = ClipMetrics.HAND_HALF_HEIGHT * radius
                val stroke = ClipMetrics.HAND_STROKE * radius
                val tail = Offset(middle.x, middle.y + half)
                val minuteTip = Offset(middle.x, middle.y - ClipMetrics.MINUTE_END * radius)
                val hourTip = Offset(middle.x, middle.y - ClipMetrics.HOUR_END * radius)
                val minuteRect = RectF(middle.x - half, minuteTip.y, middle.x + half, tail.y)
                val hourRect = RectF(middle.x - half, hourTip.y, middle.x + half, tail.y)
                val minutePaint = clipPaint(minuteColors, tail, minuteTip, Paint.Style.STROKE, stroke, 0f)
                val hourPaint = clipPaint(hourColors, tail, hourTip, Paint.Style.STROKE, stroke, ClipMetrics.SHADOW_RADIUS * radius)
                val dotRadius = ClipMetrics.DOT_RADIUS * radius
                val dotPaint = clipPaint(
                    dotColors,
                    Offset(middle.x, middle.y - dotRadius),
                    Offset(middle.x, middle.y + dotRadius),
                    Paint.Style.FILL,
                    0f,
                    0f,
                )

                onDrawBehind {
                    drawIntoCanvas { canvas ->
                        val native = canvas.nativeCanvas

                        native.drawCircle(middle.x, middle.y, dotRadius, dotPaint)

                        native.save()
                        native.rotate(minuteDegrees.value, middle.x, middle.y)
                        native.drawRoundRect(minuteRect, half, half, minutePaint)
                        native.restore()

                        native.save()
                        native.rotate(hourDegrees.value, middle.x, middle.y)
                        native.drawRoundRect(hourRect, half, half, hourPaint)
                        native.restore()
                    }
                }
            },
    )
}

private suspend fun Animatable<Float, *>.turnTo(degrees: Float) {
    val delta = ((degrees - value) % 360f + 540f) % 360f - 180f
    animateTo(targetValue = value + delta, animationSpec = tween(durationMillis = ClipMetrics.STEP_MILLIS))
}

private fun clipPaint(colors: List<Color>, from: Offset, to: Offset, style: Paint.Style, width: Float, shadow: Float): Paint =
    Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.style = style
        strokeWidth = width
        color = colors.first().toArgb()
        shader = colors.toLineShader(from, to)
        if (shadow > 0f) setShadowLayer(shadow, 0f, 0f, Grey44.toArgb())
    }

private fun degreesOf(epochMillis: Long, periodMillis: Long): Float {
    val local = epochMillis + TimeZone.getDefault().getOffset(epochMillis)
    return Math.floorMod(local, periodMillis) * 360f / periodMillis
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodClipHandsPreview() {
    AodClipHands(
        nowMillis = System.currentTimeMillis(),
        config = AodThemeConfig(),
        modifier = Modifier.fillMaxSize(),
    )
}
