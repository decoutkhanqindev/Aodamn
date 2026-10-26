package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.max

@Immutable
data class GradientLineGeometry(
    val lineStart: Offset,
    val lineEnd: Offset,
    val cut: Rect,
    val timeCenter: Offset,
    val rowCenter: Offset,
) {
    companion object {
        fun vertical(
            width: Float,
            height: Float,
            lineWidth: Float,
            textHeight: Float,
            rowHeight: Float,
            fraction: Float,
        ): GradientLineGeometry {
            val axis = width / 2f
            val hasRow = rowHeight > 0f
            val rowOffset = rowOffset(textHeight, rowHeight)
            val travelEnd = max(GradientLineMetrics.TRAVEL_END * textHeight, if (hasRow) rowOffset + rowHeight / 2f else 0f)
            val travelStart = GradientLineMetrics.TRAVEL_START * textHeight
            val center = travelStart + (height - travelStart - travelEnd).coerceAtLeast(0f) * fraction
            val cutAfter = if (hasRow) {
                max(GradientLineMetrics.CUT_AFTER_DATE * textHeight, rowOffset + rowHeight / 2f + GradientLineMetrics.DATE_MARGIN * textHeight)
            } else {
                GradientLineMetrics.CUT_AFTER * textHeight
            }

            return GradientLineGeometry(
                lineStart = Offset(axis, lineWidth),
                lineEnd = Offset(axis, height - lineWidth),
                cut = Rect(0f, center - GradientLineMetrics.CUT_BEFORE * textHeight, width, center + cutAfter),
                timeCenter = Offset(axis, center),
                rowCenter = Offset(axis, center + rowOffset),
            )
        }

        fun horizontal(
            width: Float,
            height: Float,
            lineWidth: Float,
            textHeight: Float,
            textWidth: Float,
            rowWidth: Float,
            rowHeight: Float,
            fraction: Float,
        ): GradientLineGeometry {
            val axis = height / 2f - GradientLineMetrics.LANDSCAPE_LINE_SHIFT * textHeight
            val labelWidth = max(textWidth, rowWidth)
            val center = labelWidth / 2f + (width - labelWidth).coerceAtLeast(0f) * fraction
            val cutHalf = textWidth / 2f + GradientLineMetrics.LANDSCAPE_CUT * textHeight

            return GradientLineGeometry(
                lineStart = Offset(lineWidth, axis),
                lineEnd = Offset(width - lineWidth, axis),
                cut = Rect(center - cutHalf, 0f, center + cutHalf, height),
                timeCenter = Offset(center, axis),
                rowCenter = Offset(center, axis + rowOffset(textHeight, rowHeight)),
            )
        }

        private fun rowOffset(textHeight: Float, rowHeight: Float): Float =
            max(GradientLineMetrics.DATE_OFFSET * textHeight, (0.5f + GradientLineMetrics.ROW_GAP) * textHeight + rowHeight / 2f)
    }
}
