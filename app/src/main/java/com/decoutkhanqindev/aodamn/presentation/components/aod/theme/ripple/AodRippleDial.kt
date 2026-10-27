package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple

import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.rememberLiveClockMillis
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.ripple.RippleThemeSpec
import kotlin.math.PI
import kotlin.math.min

@Composable
fun AodRippleDial(
    nowMillis: Long,
    isLive: Boolean,
    isDateShown: Boolean,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val isOutline = config[RippleThemeSpec.Style] == AodTileStyleValue.OUTLINE
    val scale = config[RippleThemeSpec.Size] / 100f
    val faceGradient = config.gradientOf(RippleThemeSpec.FaceColor)
    val faceColor = config[RippleThemeSpec.FaceColor]
    val hourColors = config.colorsOf(RippleThemeSpec.HourColor)
    val minuteColors = config.colorsOf(RippleThemeSpec.MinuteColor)
    val secondGradient = config.gradientOf(RippleThemeSpec.SecondColor)
    val secondColor = config[RippleThemeSpec.SecondColor]
    val dateGradient = config.gradientOf(RippleThemeSpec.DateColor)
    val dateColor = config[RippleThemeSpec.DateColor]
    val time = rememberZonedDateTime(nowMillis)
    val date = if (isDateShown) time.format(rememberFormatter(R.string.aod_ripple_date_pattern)) else ""
    val description = time.format(rememberTimeFormatter())
    val datePaint = rememberRippleDatePaint()
    val clockMillis = rememberLiveClockMillis(isLive)

    Spacer(
        modifier = modifier
            .fillMaxSize()
            .clearAndSetSemantics { contentDescription = description }
            .drawWithCache {
                val middle = size.center
                val half = min(size.width, size.height) / 2f
                val handBase = scale * half
                val faceRadius = RippleMetrics.FACE_RADIUS * handBase
                val face = rippleFacePath(middle, faceRadius)
                val faceBrush = faceGradient?.toBrush(Rect(middle, faceRadius * (1f + RippleMetrics.WAVE_DEPTH)))
                    ?: SolidColor(faceColor)
                val outline = Stroke(width = RippleMetrics.OutlineWidth.toPx())
                val handWidth = RippleMetrics.HAND_WIDTH * handBase
                val secondRadius = RippleMetrics.SECOND_RADIUS * handBase

                val dateRadius = RippleMetrics.DATE_ARC * faceRadius
                val dateSize = RippleMetrics.DATE_SIZE * half
                val datePath = android.graphics.Path().apply {
                    addArc(
                        RectF(middle.x - dateRadius, middle.y - dateRadius, middle.x + dateRadius, middle.y + dateRadius),
                        180f,
                        180f,
                    )
                }
                datePaint.textSize = dateSize
                datePaint.color = dateColor.toArgb()
                datePaint.shader = dateGradient?.toShader(
                    left = middle.x - dateRadius,
                    top = middle.y - dateRadius - dateSize,
                    right = middle.x + dateRadius,
                    bottom = middle.y - dateRadius,
                )
                val dateOffset = ((PI * dateRadius).toFloat() - datePaint.measureText(date)) / 2f

                onDrawBehind {
                    val clock = RippleTime(if (isLive) clockMillis.longValue else nowMillis)

                    if (isOutline) drawPath(face, faceBrush, style = outline) else drawPath(face, faceBrush)
                    if (date.isNotEmpty()) {
                        drawIntoCanvas { canvas -> canvas.nativeCanvas.drawTextOnPath(date, datePath, dateOffset, 0f, datePaint) }
                    }
                    drawHand(clock.minuteDegrees, RippleMetrics.MINUTE_LENGTH * handBase, minuteColors, handWidth)
                    drawHand(clock.hourDegrees, RippleMetrics.HOUR_LENGTH * handBase, hourColors, handWidth)

                    val dot = ripplePoint(center, clock.secondDegrees, RippleMetrics.SECOND_ORBIT * handBase)
                    drawCircle(
                        brush = secondGradient?.toBrush(Rect(dot, secondRadius)) ?: SolidColor(secondColor),
                        radius = secondRadius,
                        center = dot,
                    )
                }
            },
    )
}

private fun DrawScope.drawHand(degrees: Float, length: Float, colors: List<Color>, width: Float) {
    val tip = ripplePoint(center, degrees, length)
    val brush = if (colors.size > 1) Brush.linearGradient(colors = colors, start = center, end = tip) else SolidColor(colors.first())
    drawLine(brush = brush, start = center, end = tip, strokeWidth = width, cap = StrokeCap.Round)
}

@Composable
private fun rememberRippleDatePaint(): Paint {
    val typeface = resolveTypeface(fontFamily = RippleThemeSpec.Font, fontWeight = FontWeight.Medium, fallback = Typeface.DEFAULT)
    return remember(typeface) {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.typeface = typeface
            isSubpixelText = true
        }
    }
}

@Preview(widthDp = 393, heightDp = 521)
@Composable
private fun AodRippleDialPreview() {
    AodRippleDial(
        nowMillis = System.currentTimeMillis(),
        isLive = false,
        isDateShown = true,
        config = AodThemeConfig(),
    )
}
