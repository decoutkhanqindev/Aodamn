package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bigoverlap

import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.util.lerp

// Một chữ số đã đặt chỗ: hình (toạ độ vùng vẽ) và khung nét chữ.
class BigOverlapDigit(val path: Path, val bounds: RectF)

// Bốn chữ số (giờ chục, giờ đơn vị, phút chục, phút đơn vị) đặt như BigPixel2Clock quanh center: cột trái căn trái, cột phải căn phải trong ô rộng
// bằng số 0, hai cột lấn qua tâm, giờ trên / phút dưới lấn qua tâm; outline là độ viền 0 → 1 (độ lấn trượt theo khi đổi kiểu).
fun Paint.bigOverlapDigits(text: String, center: Offset, outline: Float): List<BigOverlapDigit> {
    val zero = glyph('0').bounds
    val overlapX = lerp(BigOverlapMetrics.OVERLAP_X_FILLED, BigOverlapMetrics.OVERLAP_X_OUTLINE, outline) * zero.width()
    val overlapY = lerp(BigOverlapMetrics.OVERLAP_Y_FILLED, BigOverlapMetrics.OVERLAP_Y_OUTLINE, outline) * zero.height()
    return text.mapIndexed { index, digit ->
        val glyph = glyph(digit)
        val bounds = glyph.bounds
        val shift = overlapX - (zero.width() - bounds.width())
        val x = if (index % 2 == 0) center.x - (bounds.left + bounds.right) + shift else center.x - shift
        val y = if (index < 2) center.y + overlapY else center.y - (bounds.top + bounds.bottom) - overlapY
        glyph.path.offset(x, y)
        bounds.offset(x, y)
        glyph
    }
}

// Hình các số vẽ sau số thứ index, gộp làm một để khoét số đó (số sau nằm trên); số cuối không bị khoét. Gộp bằng Path.op chứ không addPath:
// addPath để lỗ của số này trùng thân số kia thành không khoét.
fun List<BigOverlapDigit>.coverAbove(index: Int): Path? =
    drop(index + 1).map { digit -> digit.path }.reduceOrNull { cover, path ->
        Path(cover).apply { if (!op(path, Path.Op.UNION)) addPath(path) }
    }

// Hình một chữ số tại gốc (đường chân chữ y = 0). Số 4 của Google Sans Flex là một đường viền tự cắt (thanh ngang chồng lên thân, lỗ tam giác
// do đường vòng ngược): tô thì đúng nhưng vẽ nét thì lộ cả các cạnh bên trong, nên gộp phần chồng trước (font mẫu không có chỗ chồng).
private fun Paint.glyph(digit: Char): BigOverlapDigit {
    val raw = Path()
    getTextPath(digit.toString(), 0, 1, 0f, 0f, raw)
    val path = Path().takeIf { merged -> merged.op(raw, Path(), Path.Op.UNION) } ?: raw
    val bounds = RectF().also { rect -> path.computeBounds(rect, true) }
    return BigOverlapDigit(path = path, bounds = bounds)
}
