package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.sector

import android.graphics.Paint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.sector.SectorThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.Grey44
import java.util.TimeZone
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

@Composable
fun rememberSectorMillis(
    nowMillis: Long,
    isLive: Boolean,
): () -> Long {
    val clockMillis = remember { mutableLongStateOf(System.currentTimeMillis()) }

    if (isLive) {
        LaunchedEffect(Unit) {
            while (true) {
                withFrameMillis { clockMillis.longValue = System.currentTimeMillis() }
            }
        }
    }

    return remember(nowMillis, isLive) { { if (isLive) clockMillis.longValue else nowMillis } }
}

@Composable
fun AodSectorFace(
    epochMillis: () -> Long,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val gradient = config.gradientOf(SectorThemeSpec.SectorColor)
    val color = config[SectorThemeSpec.SectorColor]

    Spacer(
        modifier = modifier.drawWithCache {
            val brush = gradient?.toBrush(Rect(Offset.Zero, size)) ?: SolidColor(color)
            val radius = hypot(size.width, size.height)
            val topLeft = Offset(size.center.x - radius, size.center.y - radius)
            val arcSize = Size(radius * 2f, radius * 2f)

            onDrawBehind {
                val local = localMillisOf(epochMillis())
                val hour = degreesOf(local, SectorMetrics.HALF_DAY_MILLIS)
                val minute = degreesOf(local, SectorMetrics.HOUR_MILLIS)
                val gap = abs(hour - minute)
                val start = if (gap <= 180f) max(hour, minute) else min(hour, minute)
                val sweep = if (gap <= 180f) 360f - gap else gap
                drawArc(
                    brush = brush,
                    startAngle = start - 90f,
                    sweepAngle = sweep,
                    useCenter = true,
                    topLeft = topLeft,
                    size = arcSize,
                    alpha = SectorMetrics.SECTOR_ALPHA,
                )
            }
        },
    )
}

@Composable
fun AodSectorHands(
    nowMillis: Long,
    epochMillis: () -> Long,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val scale = config[SectorThemeSpec.Size] / 100f
    val hourColors = config.colorsOf(SectorThemeSpec.HourColor)
    val minuteColors = config.colorsOf(SectorThemeSpec.MinuteColor)
    val secondColors = config.colorsOf(SectorThemeSpec.SecondColor)
    val description = rememberZonedDateTime(nowMillis).format(rememberTimeFormatter())

    Spacer(
        modifier = modifier
            .clearAndSetSemantics { contentDescription = description }
            .drawWithCache {
                val middle = size.center
                val radius = scale * min(size.width, size.height) / 2f
                val shadow = SectorMetrics.SHADOW_RADIUS * radius
                val neck = SectorMetrics.NECK_WIDTH * radius
                val body = SectorMetrics.BODY_WIDTH * radius
                val minuteTip = Offset(middle.x, middle.y - SectorMetrics.MINUTE_END * radius)
                val hourTip = Offset(middle.x, middle.y - SectorMetrics.HOUR_END * radius)
                val secondTail = Offset(middle.x, middle.y - SectorMetrics.SECOND_START * radius)
                val secondTip = Offset(middle.x, middle.y - SectorMetrics.SECOND_END * radius)
                val neckTip = middle.y - SectorMetrics.NECK_END * radius
                val bodyStart = middle.y - SectorMetrics.BODY_START * radius
                val minuteNeck = handPaint(minuteColors, middle, minuteTip, shadow, Paint.Style.STROKE, neck)
                val minuteBody = handPaint(minuteColors, middle, minuteTip, shadow, Paint.Style.STROKE, body)
                val hourNeck = handPaint(hourColors, middle, hourTip, shadow, Paint.Style.STROKE, neck)
                val hourBody = handPaint(hourColors, middle, hourTip, shadow, Paint.Style.STROKE, body)
                val second = handPaint(secondColors, secondTail, secondTip, shadow, Paint.Style.STROKE, SectorMetrics.SECOND_WIDTH * radius)
                val dotRadius = SectorMetrics.DOT_RADIUS * radius
                val dotTop = Offset(middle.x, middle.y - dotRadius)
                val dotBottom = Offset(middle.x, middle.y + dotRadius)
                val dot = handPaint(hourColors, dotTop, dotBottom, 0f, Paint.Style.FILL, 0f)
                val hole = handPaint(listOf(Black), dotTop, dotBottom, 0f, Paint.Style.FILL, 0f)
                val ring = handPaint(secondColors, dotTop, dotBottom, 0f, Paint.Style.STROKE, SectorMetrics.RING_WIDTH * radius)
                val holeRadius = SectorMetrics.HOLE_RADIUS * radius

                onDrawBehind {
                    val local = localMillisOf(epochMillis())
                    drawIntoCanvas { canvas ->
                        val native = canvas.nativeCanvas

                        native.save()
                        native.rotate(degreesOf(local, SectorMetrics.HOUR_MILLIS), middle.x, middle.y)
                        native.drawLine(middle.x, middle.y, middle.x, neckTip, minuteNeck)
                        native.drawLine(middle.x, bodyStart, minuteTip.x, minuteTip.y, minuteBody)
                        native.restore()

                        native.save()
                        native.rotate(degreesOf(local, SectorMetrics.MINUTE_MILLIS), middle.x, middle.y)
                        native.drawLine(secondTail.x, secondTail.y, secondTip.x, secondTip.y, second)
                        native.restore()

                        native.save()
                        native.rotate(degreesOf(local, SectorMetrics.HALF_DAY_MILLIS), middle.x, middle.y)
                        native.drawLine(middle.x, middle.y, middle.x, neckTip, hourNeck)
                        native.drawLine(middle.x, bodyStart, hourTip.x, hourTip.y, hourBody)
                        native.restore()

                        native.drawCircle(middle.x, middle.y, dotRadius, dot)
                        native.drawCircle(middle.x, middle.y, holeRadius, hole)
                        native.drawCircle(middle.x, middle.y, holeRadius, ring)
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
        if (shadow > 0f) setShadowLayer(shadow, 0f, 0f, Grey44.toArgb())
    }

private fun localMillisOf(epochMillis: Long): Long = epochMillis + TimeZone.getDefault().getOffset(epochMillis)

private fun degreesOf(localMillis: Long, periodMillis: Long): Float = Math.floorMod(localMillis, periodMillis) * 360f / periodMillis

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodSectorDialPreview() {
    val nowMillis = System.currentTimeMillis()

    Box(modifier = Modifier.fillMaxSize()) {
        AodSectorFace(epochMillis = { nowMillis }, config = AodThemeConfig(), modifier = Modifier.fillMaxSize())
        AodSectorHands(nowMillis = nowMillis, epochMillis = { nowMillis }, config = AodThemeConfig(), modifier = Modifier.fillMaxSize())
    }
}
