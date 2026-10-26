package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.quarters

import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin

class QuartersDigit(val text: String, val degrees: Float, val offset: Float)

class QuartersGlyph(val path: Path, val bounds: RectF)

fun Paint.quartersGlyph(digit: QuartersDigit, center: Offset, radius: Float): QuartersGlyph {
    val raw = Path()
    getTextPath(digit.text, 0, digit.text.length, 0f, 0f, raw)
    val path = Path().takeIf { merged -> merged.op(raw, Path(), Path.Op.UNION) } ?: raw
    val bounds = RectF().also { rect -> path.computeBounds(rect, true) }
    val angle = Math.toRadians(digit.degrees.toDouble())
    val x = center.x + (sin(angle) * digit.offset * radius).toFloat() - bounds.centerX()
    val y = center.y - (cos(angle) * digit.offset * radius).toFloat() - bounds.centerY()
    path.offset(x, y)
    bounds.offset(x, y)
    return QuartersGlyph(path = path, bounds = bounds)
}
