package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.matrix

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix.AodMatrixDotValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix.MatrixCell

fun List<MatrixCell>.toPoints(origin: Offset, pitch: Float): List<Offset> = map { cell ->
    Offset(
        x = origin.x + (cell.column + .5f) * pitch,
        y = origin.y + (cell.row + .5f) * pitch,
    )
}

fun DrawScope.drawMatrixPoints(
    points: List<Offset>,
    pitch: Float,
    dot: AodMatrixDotValue,
    brush: Brush,
    sizeRatio: Float = 1f,
    alpha: Float = 1f,
) {
    drawPoints(
        points = points,
        pointMode = PointMode.Points,
        brush = brush,
        strokeWidth = pitch * dot.dotRatio * sizeRatio,
        cap = dot.cap,
        alpha = alpha,
    )
}

fun sampleMatrixDots(bitmap: Bitmap, grid: Int): List<MatrixCell> {
    val cell = MatrixMetrics.ICON_SAMPLE_CELL
    val side = grid * cell
    val scaled = Bitmap.createScaledBitmap(bitmap, side, side, true)
    val pixels = IntArray(side * side)
    scaled.getPixels(pixels, 0, side, 0, 0, side, side)
    if (scaled !== bitmap) scaled.recycle()
    return buildList {
        repeat(grid * grid) { index ->
            val left = index % grid * cell
            val top = index / grid * cell
            var alpha = 0
            for (y in top until top + cell) {
                for (x in left until left + cell) {
                    alpha += Color.alpha(pixels[y * side + x])
                }
            }
            if (alpha / (cell * cell * 255f) > MatrixMetrics.ICON_ALPHA_THRESHOLD) {
                add(MatrixCell(column = index % grid, row = index / grid))
            }
        }
    }
}
