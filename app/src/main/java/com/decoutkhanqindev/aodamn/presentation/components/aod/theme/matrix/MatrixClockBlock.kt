package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.matrix

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix.MatrixGlyphs
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix.MatrixThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodMatrixClock(
    state: AodState,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    val text = rememberZonedDateTime(state.nowMillis).format(
        rememberFormatter(if (is24HourFormat()) R.string.aod_matrix_time_24h else R.string.aod_matrix_time_12h),
    )
    val cells = remember(text) { MatrixGlyphs.cells(text) }
    val config = state.appearance.theme.config
    val dot = config[MatrixThemeSpec.DotStyle]
    val isUnlitShown = config[MatrixThemeSpec.UnlitDots]
    val brush = config.brushOf(MatrixThemeSpec.ClockColor)
    val pitch = size * MatrixMetrics.DIGIT_HEIGHT / MatrixGlyphs.ROWS

    Spacer(
        modifier = modifier
            .size(width = pitch * cells.columns, height = pitch * cells.rows)
            .drawWithCache {
                val pitchPx = pitch.toPx()
                val lit = cells.lit.toPoints(Offset.Zero, pitchPx)
                val unlit = cells.unlit.toPoints(Offset.Zero, pitchPx)

                onDrawBehind {
                    if (isUnlitShown) {
                        drawMatrixPoints(
                            points = unlit,
                            pitch = pitchPx,
                            dot = dot,
                            brush = brush,
                            sizeRatio = MatrixMetrics.UNLIT_SIZE,
                            alpha = MatrixMetrics.UNLIT_ALPHA,
                        )
                    }
                    drawMatrixPoints(points = lit, pitch = pitchPx, dot = dot, brush = brush)
                }
            },
    )
}
