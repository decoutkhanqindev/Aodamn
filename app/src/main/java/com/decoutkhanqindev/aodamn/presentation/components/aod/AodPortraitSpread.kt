package com.decoutkhanqindev.aodamn.presentation.components.aod

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Cụm trên từ trên xuống, cụm đáy thẳng đáy (AodSpreadMetrics), mỗi phần giữa bề ngang. Khối hiện / ẩn có animation nên hai cụm co giãn
// theo chứ không nhảy: cụm trên dài xuống, cụm đáy dài lên.
@Composable
fun AodPortraitSpread(
    top: @Composable () -> Unit,
    bottom: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    minGap: Dp = 24.dp,
) {
    Layout(
        contents = listOf(top, bottom),
        modifier = modifier.fillMaxSize(),
    ) { (topMeasurables, bottomMeasurables), constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val topPlaceables = topMeasurables.map { measurable -> measurable.measure(loose) }
        val bottomPlaceables = bottomMeasurables.map { measurable -> measurable.measure(loose) }
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val bottomY = AodSpreadMetrics.bottomY(
            height = height,
            topHeight = topPlaceables.sumOf { placeable -> placeable.height },
            bottomHeight = bottomPlaceables.sumOf { placeable -> placeable.height },
            minGap = minGap.roundToPx(),
        )

        layout(width = width, height = height) {
            var y = AodSpreadMetrics.topY(height)
            topPlaceables.forEach { placeable ->
                placeable.place(x = (width - placeable.width) / 2, y = y)
                y += placeable.height
            }
            y = bottomY
            bottomPlaceables.forEach { placeable ->
                placeable.place(x = (width - placeable.width) / 2, y = y)
                y += placeable.height
            }
        }
    }
}
