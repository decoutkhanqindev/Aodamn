package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.card

import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import android.graphics.Path as AndroidPath

@Stable
class CardGlyphs(typeface: Typeface) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.typeface = typeface
        textSize = GLYPH_TEXT_SIZE
    }
    private val digits: Map<Char, AndroidPath> = DIGITS.associateWith { digit -> textPath(digit.toString()) }
    private val digitBox: RectF = bounds(digits.getValue('0'))
    private val capBox: RectF = bounds(textPath("H"))

    fun digit(digit: Char, size: Size): Path {
        val source = digits[digit] ?: return Path()
        val box = bounds(source)
        val scaleY = size.height / digitBox.height()
        val scaleX = scaleY * CardMetrics.CONDENSE
        val matrix = Matrix().apply {
            setScale(scaleX, scaleY)
            postTranslate(size.width / 2f - box.centerX() * scaleX, -digitBox.top * scaleY)
        }
        return AndroidPath(source).apply { transform(matrix) }.asComposePath()
    }

    fun word(text: String, capHeight: Float, width: Float): Path {
        val source = textPath(text)
        val box = bounds(source)
        val scaleY = capHeight / capBox.height()
        val scaleX = scaleY * CardMetrics.CONDENSE
        val matrix = Matrix().apply {
            setScale(scaleX, scaleY)
            postTranslate(width / 2f - box.centerX() * scaleX, -capBox.top * scaleY)
        }
        return AndroidPath(source).apply { transform(matrix) }.asComposePath()
    }

    private fun textPath(text: String): AndroidPath =
        AndroidPath().also { path -> paint.getTextPath(text, 0, text.length, 0f, 0f, path) }

    private fun bounds(path: AndroidPath): RectF = RectF().also { path.computeBounds(it, true) }

    private companion object {
        const val DIGITS = "0123456789"
        const val GLYPH_TEXT_SIZE = 1000f
    }
}
