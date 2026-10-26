package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bezel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bezel.AodBezelTicksValue
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class BezelFace(size: Size, ticks: AodBezelTicksValue) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val unit = min(size.width, size.height) / 2f
    private val far = max(size.width, size.height) / 2f
    private val isLandscape = size.width > size.height
    private val reach = max(size.width, size.height)

    val minuteBand = band(size, BezelMetrics.MINUTE_X, BezelMetrics.MINUTE_Y)
    val hourBand = band(size, BezelMetrics.HOUR_X, BezelMetrics.HOUR_Y)
    val majorTicks = rays((0 until 12).map { hour -> hour * 30f })
    val minorTicks = if (ticks == AodBezelTicksValue.MINUTES) {
        rays((0 until 60).filter { minute -> minute % 5 != 0 }.map { minute -> minute * 6f })
    } else {
        emptyList()
    }

    val longTicks = rays(
        (1..12).filter { hour -> if (isLandscape) hour != 6 && hour != 12 else hour != 3 && hour != 9 }.map { hour -> hour * 30f },
    )
    val numberSize = BezelMetrics.NUMBER_SIZE * min(size.width, size.height)
    val numbers = listOf("12" to 0f, "3" to 90f, "6" to 180f, "9" to 270f).map { (text, degrees) ->
        val isNear = if (isLandscape) degrees == 0f || degrees == 180f else degrees == 90f || degrees == 270f
        text to point(degrees, if (isNear) BezelMetrics.NUMBER_NEAR * unit else BezelMetrics.NUMBER_FAR * far)
    }

    fun point(degrees: Float, radius: Float): Offset {
        val radians = Math.toRadians(degrees.toDouble())
        return Offset(center.x + sin(radians).toFloat() * radius, center.y - cos(radians).toFloat() * radius)
    }

    private fun rays(degrees: List<Float>): List<Pair<Offset, Offset>> =
        degrees.map { angle -> point(angle, BezelMetrics.RAY_START * unit) to point(angle, reach) }

    private fun band(size: Size, innerX: Float, innerY: Float): Path = Path().apply {
        fillType = PathFillType.EvenOdd
        addRect(inset(size, BezelMetrics.OUTER_X * unit, BezelMetrics.OUTER_Y * far))
        addRect(inset(size, innerX * unit, innerY * far))
    }

    private fun inset(size: Size, x: Float, y: Float): Rect = Rect(left = x, top = y, right = size.width - x, bottom = size.height - y)
}

fun DrawScope.drawBezelFace(
    face: BezelFace,
    paints: BezelPaints,
    handBrush: Brush,
    numberColor: Color,
    numberGradient: AodGradient?,
) {
    clipPath(face.minuteBand) {
        face.majorTicks.forEach { (start, end) -> drawLine(handBrush, start, end, strokeWidth = BezelMetrics.MAJOR_STROKE * face.unit) }
        face.minorTicks.forEach { (start, end) ->
            drawLine(handBrush, start, end, strokeWidth = BezelMetrics.MINOR_STROKE * face.unit, alpha = BezelMetrics.MINOR_ALPHA)
        }
    }
    clipPath(face.hourBand) {
        face.longTicks.forEach { (start, end) -> drawLine(handBrush, start, end, strokeWidth = BezelMetrics.MAJOR_STROKE * face.unit) }
    }
    drawIntoCanvas { canvas ->
        face.numbers.forEach { (text, center) ->
            paints.drawNumber(canvas.nativeCanvas, text, center, face.numberSize, numberColor, numberGradient)
        }
    }
}

fun DrawScope.drawBezelHands(
    face: BezelFace,
    paints: BezelPaints,
    minuteDegrees: Float,
    hourDegrees: Float,
    handColors: List<Color>,
    accentColor: Color,
) {
    val unit = face.unit
    val shadow = BezelMetrics.SHADOW_RADIUS * unit
    drawIntoCanvas { canvas ->
        listOf(minuteDegrees to BezelMetrics.MINUTE_LENGTH, hourDegrees to BezelMetrics.HOUR_LENGTH).forEach { (degrees, length) ->
            val tip = face.point(degrees, length * unit)
            paints.drawHand(
                canvas = canvas.nativeCanvas,
                start = face.center,
                end = face.point(degrees, BezelMetrics.HAND_NECK * unit),
                width = BezelMetrics.HAND_THIN * unit,
                shadowRadius = shadow,
                colors = handColors,
                from = face.center,
                to = tip,
            )
            paints.drawHand(
                canvas = canvas.nativeCanvas,
                start = face.point(degrees, BezelMetrics.HAND_START * unit),
                end = tip,
                width = BezelMetrics.HAND_THICK * unit,
                shadowRadius = shadow,
                colors = handColors,
                from = face.center,
                to = tip,
            )
        }
    }
    drawCircle(color = handColors.first(), radius = BezelMetrics.PIN * unit, center = face.center)
    drawCircle(color = Black, radius = BezelMetrics.PIN_HOLE * unit, center = face.center)
    drawCircle(
        color = accentColor,
        radius = BezelMetrics.PIN_RING * unit,
        center = face.center,
        style = Stroke(width = BezelMetrics.PIN_RING_STROKE * unit),
    )
}
