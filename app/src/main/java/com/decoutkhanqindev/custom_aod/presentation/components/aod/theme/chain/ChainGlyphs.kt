package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.chain

import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import android.graphics.Path as AndroidPath

// Đường viền chữ số và dấu hai chấm của Chuỗi lấy từ font (Paint.getTextPath), để khoét giờ khỏi viên thuốc như Aug23Clock của app mẫu
// (clipPath DIFFERENCE). Mỗi ký tự một đường riêng để số không đổi giữ nguyên chỗ khi phút đổi (cách làm như TowerGlyphs của Tháp).
@Stable
class ChainGlyphs(typeface: Typeface) {
    private val glyphs: Map<Char, AndroidPath>
    private val advances: Map<Char, Float>
    private val digitBox: RectF

    init {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.typeface = typeface
            textSize = GLYPH_TEXT_SIZE
        }
        glyphs = CHARS.associateWith { char ->
            AndroidPath().also { path -> paint.getTextPath(char.toString(), 0, 1, 0f, 0f, path) }
        }
        advances = CHARS.associateWith { char -> paint.measureText(char.toString()) }
        digitBox = bounds(glyphs.getValue('0'))
    }

    // Từng ký tự đã đặt chỗ: chữ số cao digitHeight, đỉnh chữ số ở y = 0, mép trái nét chữ đầu tiên ở x = 0, giãn chữ tracking (px).
    fun layout(text: String, digitHeight: Float, tracking: Float): List<ChainGlyph> {
        val scale = digitHeight / digitBox.height()
        val inkLeft = text.firstOrNull()?.let { first -> glyphs[first]?.let { path -> bounds(path).left } } ?: 0f
        var pen = -inkLeft * scale
        return text.map { char ->
            val source = glyphs[char]
            val path = if (source == null) {
                Path()
            } else {
                val matrix = Matrix().apply {
                    setScale(scale, scale)
                    postTranslate(pen, -digitBox.top * scale)
                }
                AndroidPath(source).apply { transform(matrix) }.asComposePath()
            }
            ChainGlyph(char = char, left = pen, path = path).also {
                pen += (advances[char] ?: 0f) * scale + tracking
            }
        }
    }

    private fun bounds(path: AndroidPath): RectF = RectF().also { path.computeBounds(it, true) }

    private companion object {
        const val CHARS = "0123456789:"
        const val GLYPH_TEXT_SIZE = 1000f
    }
}

// Một ký tự của giờ: left là chỗ đặt (để biết ký tự có giữ nguyên chỗ khi giờ đổi không).
@Stable
class ChainGlyph(
    val char: Char,
    val left: Float,
    val path: Path,
)
