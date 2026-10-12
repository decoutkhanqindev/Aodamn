package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.pebble.AodShapeValue
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val UnitCos = FloatArray(AodShapeValue.POINTS) { index -> cos(2 * PI * index / AodShapeValue.POINTS).toFloat() }
private val UnitSin = FloatArray(AodShapeValue.POINTS) { index -> sin(2 * PI * index / AodShapeValue.POINTS).toFloat() }

// Path khép kín qua AodShapeValue.POINTS bán kính bằng spline Catmull-Rom, vừa khít khung size; inset chừa chỗ cho nét viền.
// Truyền path cũ vào để dùng lại khi vẽ từng khung hình của animation.
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

// Hình giữa from và to: nội suy từng bán kính, ghi vào out để không cấp phát mỗi khung hình.
fun lerpRadii(
    from: FloatArray,
    to: FloatArray,
    fraction: Float,
    out: FloatArray,
): FloatArray {
    for (index in out.indices) out[index] = from[index] + (to[index] - from[index]) * fraction
    return out
}

// Hình khối dùng làm Shape của Compose (nền, viền, clip), vd hình thu nhỏ ở màn sửa chủ đề.
class AodShapeOutline(private val shape: AodShapeValue) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline =
        Outline.Generic(radialShapePath(shape.radii, size))
}
