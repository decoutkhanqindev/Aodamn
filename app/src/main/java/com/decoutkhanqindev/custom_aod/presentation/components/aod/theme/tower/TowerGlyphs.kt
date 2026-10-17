package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.tower

import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import android.graphics.Path as AndroidPath

// Đường viền chữ số của Tháp lấy từ font (Paint.getTextPath) rồi kéo cao, co ngang theo TowerMetrics.CONDENSE trước khi tô hay vẽ nét, nên kiểu
// viền có nét dày đều chứ không dày ngang mỏng dọc như khi nén cả chữ đã vẽ (cách làm như CardGlyphs của Thẻ, độ nén khác).
@Stable
class TowerGlyphs(typeface: Typeface) {
    private val digits: Map<Char, AndroidPath>
    private val digitBox: RectF

    init {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.typeface = typeface
            textSize = GLYPH_TEXT_SIZE
        }
        digits = DIGITS.associateWith { digit ->
            AndroidPath().also { path -> paint.getTextPath(digit.toString(), 0, 1, 0f, 0f, path) }
        }
        digitBox = bounds(digits.getValue('0'))
    }

    // Chữ số cao đúng size.height (số 0 chạm đỉnh và chân ô), căn giữa ô theo bề ngang.
    fun digit(digit: Char, size: Size): Path {
        val source = digits[digit] ?: return Path()
        val box = bounds(source)
        val scaleY = size.height / digitBox.height()
        val scaleX = scaleY * TowerMetrics.CONDENSE
        val matrix = Matrix().apply {
            setScale(scaleX, scaleY)
            postTranslate(size.width / 2f - box.centerX() * scaleX, -digitBox.top * scaleY)
        }
        return AndroidPath(source).apply { transform(matrix) }.asComposePath()
    }

    private fun bounds(path: AndroidPath): RectF = RectF().also { path.computeBounds(it, true) }

    private companion object {
        const val DIGITS = "0123456789"
        const val GLYPH_TEXT_SIZE = 1000f
    }
}
