package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.meridian

import android.graphics.Paint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MeridianDial(
    hour: Int,
    minute: Int,
    color: Color,
    gradient: AodGradient?,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier.drawWithCache {
            val half = size.minDimension / 2f
            val stroke = half * MeridianMetrics.DIAL_STROKE
            val brush = gradient?.toBrush() ?: SolidColor(color)
            val center = Offset(size.width / 2f, size.height / 2f)
            val minuteRadians = Math.toRadians(MeridianMetrics.minuteAngle(minute).toDouble())
            val minuteDirection = Offset(sin(minuteRadians).toFloat(), -cos(minuteRadians).toFloat())
            val minuteStart = center - minuteDirection * (MeridianMetrics.HAND_TAIL * half)
            val minuteEnd = center + minuteDirection * (MeridianMetrics.MINUTE_HAND * half)
            val hourRadians = Math.toRadians(MeridianMetrics.hourAngle(hour, minute).toDouble())
            val hourDirection = Offset(sin(hourRadians).toFloat(), -cos(hourRadians).toFloat())
            val hourStart = center - hourDirection * (MeridianMetrics.HAND_TAIL * half)
            val hourEnd = center + hourDirection * (MeridianMetrics.HOUR_HAND * half)
            val argb = color.toArgb()
            val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
                strokeWidth = stroke
                setShadowLayer(stroke, 0f, 0f, MeridianMetrics.SHADOW_COLOR)
                if (gradient != null) {
                    shader = gradient.toShader(0f, 0f, size.width, size.height)
                } else {
                    this.color = argb
                }
            }
            val ringStroke = Stroke(width = stroke)

            onDrawBehind {
                drawCircle(
                    brush = brush,
                    radius = half - stroke,
                    style = ringStroke,
                )
                drawLine(
                    brush = brush,
                    start = minuteStart,
                    end = minuteEnd,
                    strokeWidth = stroke,
                    cap = StrokeCap.Round,
                )
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawLine(hourStart.x, hourStart.y, hourEnd.x, hourEnd.y, shadowPaint)
                }
            }
        },
    )
}
