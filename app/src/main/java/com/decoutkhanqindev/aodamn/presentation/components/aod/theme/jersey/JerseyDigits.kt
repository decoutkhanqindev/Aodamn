package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.jersey

import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyBigShouldersDisplay
import kotlin.math.max
import android.graphics.Path as AndroidPath

// Một hàng chữ số: mỗi số một ô rộng cố định (JerseyMetrics.PITCH) nên hàng không đổi bề ngang theo giờ; số đổi thì mờ chéo trong ô.
@Composable
fun JerseyDigitRow(
    text: String,
    glyphs: JerseyGlyphs,
    rowHeight: Dp,
    brush: Brush,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        text.forEach { digit ->
            Crossfade(
                targetState = digit,
                modifier = Modifier.size(width = rowHeight * JerseyMetrics.PITCH, height = rowHeight),
                animationSpec = tween(durationMillis = JerseyMetrics.DIGIT_CHANGE_MILLIS),
                label = "JerseyDigit",
            ) { target ->
                JerseyDigit(digit = target, glyphs = glyphs, brush = brush)
            }
        }
    }
}

@Composable
private fun JerseyDigit(
    digit: Char,
    glyphs: JerseyGlyphs,
    brush: Brush,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                val strokeWidth = max(size.height * JerseyMetrics.STROKE, JerseyMetrics.MinStroke.toPx())
                val outline = glyphs.outline(digit = digit, size = size, stroke = strokeWidth)
                val stroke = Stroke(width = strokeWidth, join = StrokeJoin.Round)

                onDrawBehind {
                    drawPath(path = outline, brush = brush, style = stroke)
                }
            },
    )
}

@Composable
fun rememberJerseyGlyphs(): JerseyGlyphs {
    val typeface = LocalFontFamilyResolver.current
        .resolve(fontFamily = FontFamilyBigShouldersDisplay, fontWeight = FontWeight.Black)
        .value as? Typeface ?: Typeface.DEFAULT_BOLD
    return remember(typeface) { JerseyGlyphs(typeface) }
}

// Đường viền 10 chữ số lấy một lần từ font (Paint.getTextPath, gốc ở đường chân chữ, y hướng xuống). Lúc vẽ, đường viền được kéo cao (co ngang
// theo CONDENSE) cho nét ngoài vừa chiều cao ô rồi mới vẽ nét, nên nét dày đều chứ không dày ngang mỏng dọc như khi phóng cả chữ đã vẽ.
// Số 1 tự vẽ (cờ vuông như ảnh mẫu). Mỗi số nằm giữa ô.
@Stable
class JerseyGlyphs(typeface: Typeface) {
    private val outlines: Map<Char, AndroidPath>
    private val bounds: Map<Char, RectF>
    private val capHeight: Float

    init {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.typeface = typeface
            textSize = GLYPH_TEXT_SIZE
        }
        outlines = DIGITS.associateWith { digit ->
            AndroidPath().also { path -> paint.getTextPath(digit.toString(), 0, 1, 0f, 0f, path) }
        }
        bounds = outlines.mapValues { (_, path) -> RectF().also { path.computeBounds(it, true) } }
        // Số 1 phẳng cả đỉnh lẫn chân nên cao đúng bằng chiều cao chữ (số tròn tràn ra khoảng 1%).
        capHeight = bounds.getValue('1').height()
    }

    fun outline(digit: Char, size: Size, stroke: Float): Path =
        if (digit == '1') oneOutline(size, stroke) else glyphOutline(digit, size, stroke)

    private fun glyphOutline(digit: Char, size: Size, stroke: Float): Path {
        val source = outlines[digit] ?: return Path()
        val box = bounds.getValue(digit)
        val scaleY = (size.height - stroke) / capHeight
        val scaleX = scaleY * JerseyMetrics.CONDENSE
        val matrix = Matrix().apply {
            setScale(scaleX, scaleY)
            postTranslate(size.width / 2f - box.centerX() * scaleX, size.height - stroke / 2f)
        }
        return AndroidPath(source).apply { transform(matrix) }.asComposePath()
    }

    // Nét ngoài (kể cả bề dày nét) là hình chữ nhật ONE_WIDTH × cao ô trừ góc dưới trái; đường vẽ lùi vào trong nửa nét.
    private fun oneOutline(size: Size, stroke: Float): Path {
        val height = size.height
        val width = JerseyMetrics.ONE_WIDTH * height
        val flagWidth = JerseyMetrics.ONE_FLAG_WIDTH * height
        val flagHeight = JerseyMetrics.ONE_FLAG_HEIGHT * height
        val left = (size.width - width) / 2f
        val half = stroke / 2f
        return Path().apply {
            moveTo(left + half, half)
            lineTo(left + width - half, half)
            lineTo(left + width - half, height - half)
            lineTo(left + flagWidth + half, height - half)
            lineTo(left + flagWidth + half, flagHeight - half)
            lineTo(left + half, flagHeight - half)
            close()
        }
    }

    private companion object {
        const val DIGITS = "0123456789"
        const val GLYPH_TEXT_SIZE = 1000f
    }
}
