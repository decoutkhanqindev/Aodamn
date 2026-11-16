package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.dot

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

object DotGrids {
    val Play = List(DotMetrics.MEDIA_GRID_ROWS) { row ->
        buildString {
            repeat(6) { column -> append(if (row in column..(10 - column)) '#' else ' ') }
        }
    }
    val Pause = List(DotMetrics.MEDIA_GRID_ROWS) { "##  ##" }
    val Bolt = listOf("   ##", "  ## ", " ##  ", "#####", "  ## ", " ##  ", "##   ")
}

fun DrawScope.drawDotGrid(
    rows: List<String>,
    topLeft: Offset,
    pitch: Float,
    brush: Brush,
    dotRatio: Float,
    alpha: Float = 1f,
) {
    val radius = pitch * dotRatio / 2f
    rows.forEachIndexed { row, cells ->
        cells.forEachIndexed { column, cell ->
            if (cell == '#') {
                drawCircle(
                    brush = brush,
                    radius = radius,
                    center = Offset(topLeft.x + column * pitch, topLeft.y + row * pitch),
                    alpha = alpha,
                )
            }
        }
    }
}

fun DrawScope.drawDotGrid(
    rows: List<String>,
    topLeft: Offset,
    pitch: Float,
    color: Color,
    dotRatio: Float,
    alpha: Float = 1f,
) {
    val radius = pitch * dotRatio / 2f
    rows.forEachIndexed { row, cells ->
        cells.forEachIndexed { column, cell ->
            if (cell == '#') {
                drawCircle(
                    color = color,
                    radius = radius,
                    center = Offset(topLeft.x + column * pitch, topLeft.y + row * pitch),
                    alpha = alpha,
                )
            }
        }
    }
}

fun sampleDots(bitmap: Bitmap, grid: Int, threshold: Float): BooleanArray {
    val cell = DotMetrics.ICON_SAMPLE_CELL
    val side = grid * cell
    val scaled = Bitmap.createScaledBitmap(bitmap, side, side, true)
    val pixels = IntArray(side * side)
    scaled.getPixels(pixels, 0, side, 0, 0, side, side)
    if (scaled !== bitmap) scaled.recycle()
    return BooleanArray(grid * grid) { index ->
        val left = index % grid * cell
        val top = index / grid * cell
        var sum = 0
        for (y in top until top + cell) {
            for (x in left until left + cell) {
                sum += android.graphics.Color.alpha(pixels[y * side + x])
            }
        }
        sum / (cell * cell * 255f) > threshold
    }
}
