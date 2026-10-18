package com.decoutkhanqindev.aodamn.presentation.components.aod.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.theme.Grey3A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyB4
import kotlin.math.PI
import kotlin.math.sin

// Thanh tiến trình nhạc gợn sóng của các chủ đề có trình phát riêng (Sỏi, Thẻ): phần đã nghe là sóng sin biên độ wave() × (height −
// strokeWidth) / 2 (0 là thẳng), cách gap rồi tới phần còn lại thẳng màu trackColor. fraction và wave chỉ đọc ở pha vẽ.
@Composable
fun AodWaveTrack(
    fraction: () -> Float,
    wave: () -> Float,
    color: Color,
    trackColor: Color,
    height: Dp,
    strokeWidth: Dp,
    wavelength: Dp,
    gap: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .drawWithCache {
                val stroke = strokeWidth.toPx()
                val amplitude = (size.height - stroke) / 2f
                val length = wavelength.toPx()
                val gapPx = gap.toPx()
                val sampleStep = 1.5.dp.toPx()
                val style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
                val path = Path()
                val isRtl = layoutDirection == LayoutDirection.Rtl

                onDrawBehind {
                    val centerY = size.height / 2f
                    val start = stroke / 2f
                    val end = size.width - stroke / 2f
                    val activeEnd = start + (end - start) * fraction()
                    val restStart = if (activeEnd > start) activeEnd + gapPx else start
                    val waveHeight = amplitude * wave()

                    withTransform({ if (isRtl) scale(scaleX = -1f, scaleY = 1f, pivot = center) }) {
                        if (restStart < end) {
                            drawLine(
                                color = trackColor,
                                start = Offset(restStart, centerY),
                                end = Offset(end, centerY),
                                strokeWidth = stroke,
                                cap = StrokeCap.Round,
                            )
                        }
                        if (activeEnd > start) {
                            path.reset()
                            path.moveTo(start, centerY)
                            var x = start
                            while (x < activeEnd) {
                                x = minOf(x + sampleStep, activeEnd)
                                path.lineTo(x, centerY + waveHeight * sin(2f * PI.toFloat() * (x - start) / length))
                            }
                            drawPath(path = path, color = color, style = style)
                        }
                    }
                }
            },
    )
}

@Preview(widthDp = 300)
@Composable
private fun AodWaveTrackPreview() {
    AodWaveTrack(
        fraction = { 0.6f },
        wave = { 1f },
        color = GreyB4,
        trackColor = Grey3A,
        height = 12.dp,
        strokeWidth = 4.dp,
        wavelength = 26.dp,
        gap = 6.dp,
    )
}
