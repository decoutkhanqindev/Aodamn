package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline

import android.graphics.Typeface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.min
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gradientline.GradientLineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import kotlin.math.roundToInt

// Vạch gradient + giờ + dòng ngày của Dải màu trong vùng được cho. isVertical: màn dọc (vạch dọc giữa vùng, như app mẫu), màn ngang (vạch
// ngang, mock đã duyệt). Giờ vẽ trên vạch, căn theo nét chữ như app mẫu; dòng ngày (+ thời tiết) là composable đặt theo giờ. Vị trí theo phút
// trong ngày (hoặc vị trí ghim) nên chỉ đổi mỗi phút như nhịp của AOD.
@Composable
fun AodGradientLineClock(
    state: AodState,
    isVertical: Boolean,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val density = LocalDensity.current
    val paints = rememberGradientLinePaints()
    val textMeasurer = rememberTextMeasurer()
    val time = rememberZonedDateTime(state.nowMillis)
    val timeText = time.format(
        rememberFormatter(if (is24HourFormat()) R.string.aod_gradient_line_time_24h else R.string.aod_gradient_line_time_12h),
    )
    val description = time.format(rememberTimeFormatter())
    val position = config[GradientLineThemeSpec.Position]
    val fraction = if (position == AodRangeOption.AUTO) {
        (time.hour * 60 + time.minute) / GradientLineMetrics.MINUTES_PER_DAY
    } else {
        position / 100f
    }
    val scale = config[GradientLineThemeSpec.Size] / 100f
    val lineColors = config.colorsOf(GradientLineThemeSpec.LineColor)
    val timeBrush = config.brushOf(GradientLineThemeSpec.ClockColor)
    val geometry = remember { mutableStateOf<GradientLineGeometry?>(null) }

    BoxWithConstraints(modifier = modifier) {
        val side = min(maxWidth, maxHeight)
        val timeSize = side * GradientLineMetrics.TIME_SIZE * scale
        val dateSize = with(density) { (side * GradientLineMetrics.DATE_SIZE * scale).toSp() }

        Layout(
            content = { GradientLineDateRow(state = state, fontSize = dateSize) },
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = description }
                .drawWithCache {
                    val shape = geometry.value
                    val textSize = timeSize.toPx()
                    val ink = paints.inkBounds(timeText, textSize)
                    val layout = textMeasurer.measure(
                        text = timeText,
                        style = TextStyle(
                            fontFamily = GradientLineThemeSpec.Font,
                            fontWeight = FontWeight.Normal,
                            fontSize = textSize.toSp(),
                        ),
                    )
                    val lineWidth = side.toPx() * GradientLineMetrics.LINE_WIDTH
                    val lineBrush = when {
                        shape == null -> SolidColor(lineColors.first())
                        lineColors.size > 1 -> Brush.linearGradient(colors = lineColors, start = shape.lineStart, end = shape.lineEnd)
                        else -> SolidColor(lineColors.first())
                    }

                    onDrawBehind {
                        if (shape == null) return@onDrawBehind
                        clipRect(
                            left = shape.cut.left,
                            top = shape.cut.top,
                            right = shape.cut.right,
                            bottom = shape.cut.bottom,
                            clipOp = ClipOp.Difference,
                        ) {
                            drawLine(
                                brush = lineBrush,
                                start = shape.lineStart,
                                end = shape.lineEnd,
                                strokeWidth = lineWidth,
                                cap = StrokeCap.Round,
                            )
                        }
                        drawText(
                            textLayoutResult = layout,
                            brush = timeBrush,
                            topLeft = Offset(
                                x = shape.timeCenter.x - ink.center.x,
                                y = shape.timeCenter.y - layout.firstBaseline - ink.center.y,
                            ),
                        )
                    }
                },
        ) { measurables, constraints ->
            val row = measurables.first().measure(Constraints(maxWidth = constraints.maxWidth))
            val width = constraints.maxWidth.toFloat()
            val height = constraints.maxHeight.toFloat()
            val ink = paints.inkBounds(timeText, timeSize.toPx())
            val lineWidth = side.toPx() * GradientLineMetrics.LINE_WIDTH
            val rowHeight = if (row.width > 0) row.height.toFloat() else 0f
            val shape = if (isVertical) {
                GradientLineGeometry.vertical(
                    width = width,
                    height = height,
                    lineWidth = lineWidth,
                    textHeight = ink.height,
                    rowHeight = rowHeight,
                    fraction = fraction,
                )
            } else {
                GradientLineGeometry.horizontal(
                    width = width,
                    height = height,
                    lineWidth = lineWidth,
                    textHeight = ink.height,
                    textWidth = ink.width,
                    rowWidth = row.width.toFloat(),
                    rowHeight = rowHeight,
                    fraction = fraction,
                )
            }
            geometry.value = shape

            layout(constraints.maxWidth, constraints.maxHeight) {
                row.place(
                    x = (shape.rowCenter.x - row.width / 2f).roundToInt(),
                    y = (shape.rowCenter.y - row.height / 2f).roundToInt(),
                )
            }
        }
    }
}

// Ngày "EEEE, dd MMM" mờ 78% như app mẫu, thời tiết (khối dùng chung) cạnh ngày; tắt cả hai thì dòng rỗng, vạch chỉ khoét quanh giờ.
@Composable
private fun GradientLineDateRow(
    state: AodState,
    fontSize: TextUnit,
) {
    val config = state.appearance.theme.config
    val components = state.appearance.components
    val date = rememberZonedDateTime(state.nowMillis).format(rememberFormatter(R.string.aod_gradient_line_date_pattern))
    val isWeatherShown = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components

    Row(verticalAlignment = Alignment.CenterVertically) {
        AnimatedVisibility(visible = state.extras.isDateEnabled) {
            Text(
                text = date,
                style = TextStyle(
                    brush = config.brushOf(GradientLineThemeSpec.DateColor),
                    alpha = GradientLineMetrics.DATE_ALPHA,
                    fontSize = fontSize,
                    fontFamily = GradientLineThemeSpec.Font,
                    fontWeight = FontWeight.Normal,
                ),
                maxLines = 1,
                softWrap = false,
            )
        }

        AnimatedVisibility(visible = isWeatherShown) {
            AodWeather(
                weather = state.weather,
                style = components.styleOf(AodComponentValue.WEATHER),
                layout = components.weatherLayout,
            )
        }
    }
}

@Composable
private fun rememberGradientLinePaints(): GradientLinePaints {
    val typeface = LocalFontFamilyResolver.current
        .resolve(fontFamily = GradientLineThemeSpec.Font, fontWeight = FontWeight.Normal)
        .value as? Typeface ?: Typeface.DEFAULT
    return remember(typeface) { GradientLinePaints(typeface) }
}

@Preview(widthDp = 313, heightDp = 640)
@Composable
private fun AodGradientLineClockPreview() {
    AodGradientLineClock(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.GRADIENT_LINE)),
            isPreview = true,
        ),
        isVertical = true,
    )
}
