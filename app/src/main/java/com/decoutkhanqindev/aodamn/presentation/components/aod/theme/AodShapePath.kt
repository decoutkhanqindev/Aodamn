package com.decoutkhanqindev.aodamn.presentation.components.aod.theme

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodShapeValue
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val UnitCos = FloatArray(AodShapeValue.POINTS) { index -> cos(2 * PI * index / AodShapeValue.POINTS).toFloat() }
private val UnitSin = FloatArray(AodShapeValue.POINTS) { index -> sin(2 * PI * index / AodShapeValue.POINTS).toFloat() }

fun radialShapePath(
    radii: FloatArray,
    size: Size,
    inset: Float = 0f,
    path: Path = Path(),
): Path {
    val count = AodShapeValue.POINTS
    val centerX = size.width / 2f
    val centerY = size.height / 2f
    val radiusX = centerX - inset
    val radiusY = centerY - inset

    fun x(index: Int): Float {
        val wrapped = (index % count + count) % count
        return centerX + radiusX * radii[wrapped] * UnitCos[wrapped]
    }

    fun y(index: Int): Float {
        val wrapped = (index % count + count) % count
        return centerY + radiusY * radii[wrapped] * UnitSin[wrapped]
    }

    path.reset()
    path.moveTo(x(0), y(0))
    for (index in 0 until count) {
        path.cubicTo(
            x(index) + (x(index + 1) - x(index - 1)) / 6f,
            y(index) + (y(index + 1) - y(index - 1)) / 6f,
            x(index + 1) - (x(index + 2) - x(index)) / 6f,
            y(index + 1) - (y(index + 2) - y(index)) / 6f,
            x(index + 1),
            y(index + 1),
        )
    }
    path.close()
    return path
}

fun lerpRadii(
    from: FloatArray,
    to: FloatArray,
    fraction: Float,
    out: FloatArray,
): FloatArray {
    for (index in out.indices) out[index] = from[index] + (to[index] - from[index]) * fraction
    return out
}

class AodShapeOutline(private val shape: AodShapeValue) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline =
        Outline.Generic(radialShapePath(shape.radii, size))
}
