package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin

// Hình khối của chủ đề Sỏi. Mỗi hình là hàm bán kính quanh tâm r(góc), nên đổi giữa hai hình (animation) chỉ là nội suy bán kính.
@Immutable
enum class AodShapeValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    SQUIRCLE(code = 0, labelRes = R.string.shape_squircle),
    PEBBLE(code = 1, labelRes = R.string.shape_pebble),
    HEXAGON(code = 2, labelRes = R.string.shape_hexagon),
    CLOVER(code = 3, labelRes = R.string.shape_clover),
    CIRCLE(code = 4, labelRes = R.string.shape_circle),
    COOKIE(code = 5, labelRes = R.string.shape_cookie),
    FLOWER(code = 6, labelRes = R.string.shape_flower),
    STAR(code = 7, labelRes = R.string.shape_star);

    // POINTS bán kính tại các góc đều, đi theo chiều kim đồng hồ từ hướng 3 giờ, chuẩn hoá để hình vừa khít ô vuông [-1, 1].
    val radii: FloatArray by lazy { buildRadii() }

    companion object {
        const val POINTS = 120
    }
}

private const val SAMPLES = 360

private fun AodShapeValue.radiusAt(angle: Double): Double = when (this) {
    AodShapeValue.SQUIRCLE -> superellipse(angle, exponent = 5.0)
    AodShapeValue.PEBBLE -> superellipse(angle + PI / 4, exponent = 2.3, minorAxis = 0.74)
    AodShapeValue.HEXAGON -> cos(PI / 6) / cos(angle % (PI / 3) - PI / 6)
    AodShapeValue.CLOVER -> superellipse(angle, exponent = 3.2) * (1 - 0.12 * (1 + cos(4 * angle)) / 2)
    AodShapeValue.CIRCLE -> 1.0
    AodShapeValue.COOKIE -> 1 + 0.06 * cos(12 * angle)
    AodShapeValue.FLOWER -> 1 + 0.13 * cos(8 * angle)
    AodShapeValue.STAR -> 1 + 0.18 * cos(5 * (angle + PI / 2))
}

// Làm mềm góc nhọn (lục giác, cánh sao) bằng trung bình Gauss của bán kính quanh mỗi góc.
private val AodShapeValue.smoothDegrees: Int
    get() = when (this) {
        AodShapeValue.HEXAGON -> 40
        AodShapeValue.CLOVER -> 10
        AodShapeValue.FLOWER -> 6
        AodShapeValue.STAR -> 16
        else -> 0
    }

private fun superellipse(angle: Double, exponent: Double, minorAxis: Double = 1.0): Double =
    (abs(cos(angle)).pow(exponent) + (abs(sin(angle)) / minorAxis).pow(exponent)).pow(-1 / exponent)

private fun AodShapeValue.buildRadii(): FloatArray {
    var radii = DoubleArray(SAMPLES) { index -> radiusAt(2 * PI * index / SAMPLES) }
    val half = smoothDegrees * SAMPLES / 360 / 2
    if (half > 0) {
        val sigma = half / 2.0
        val weights = DoubleArray(2 * half + 1) { j -> exp(-((j - half) * (j - half)) / (2 * sigma * sigma)) }
        val total = weights.sum()
        val source = radii
        radii = DoubleArray(SAMPLES) { index ->
            weights.indices.sumOf { j -> source[(index + j - half + SAMPLES) % SAMPLES] * weights[j] } / total
        }
    }
    var extent = 0.0
    radii.forEachIndexed { index, radius ->
        val angle = 2 * PI * index / SAMPLES
        extent = max(extent, max(abs(radius * cos(angle)), abs(radius * sin(angle))))
    }
    val stride = SAMPLES / AodShapeValue.POINTS
    return FloatArray(AodShapeValue.POINTS) { index -> (radii[index * stride] / extent).toFloat() }
}
