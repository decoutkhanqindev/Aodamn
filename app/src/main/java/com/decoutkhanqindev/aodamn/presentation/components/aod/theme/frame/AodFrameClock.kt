package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.frame

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.frame.FrameThemeSpec

@Composable
fun AodFrameClock(
    nowMillis: Long,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val time = rememberZonedDateTime(nowMillis)
    val hours = time.format(rememberFormatter(if (is24HourFormat()) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h))
    val minutes = time.format(rememberFormatter(R.string.aod_minute_pattern))
    val description = time.format(rememberTimeFormatter())
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val style = TextStyle(fontFamily = FrameThemeSpec.Font, fontWeight = FontWeight.Normal, fontSize = config[FrameThemeSpec.Size].sp)
    val em = with(density) { style.fontSize.toDp() }
    val textWidth = remember(hours, minutes, style, density) {
        with(density) { maxOf(measurer.measure(hours, style).size.width, measurer.measure(minutes, style).size.width).toDp() }
    }
    val width by animateDpAsState(targetValue = textWidth + FrameMetrics.BoxSidePadding * 2, label = "AodFrameClockWidth")
    val height = FrameMetrics.boxHeight(em)

    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clearAndSetSemantics { contentDescription = description }
            .drawWithCache {
                val brush = config.gradientOf(FrameThemeSpec.ClockColor)?.toBrush(Rect(Offset.Zero, size))
                    ?: SolidColor(config[FrameThemeSpec.ClockColor])
                val line = FrameMetrics.Line.toPx()
                val inset = (FrameMetrics.BoxSidePadding + FrameMetrics.SeparatorMargin).toPx()
                val separatorTop = FrameMetrics.separatorTop(em).toPx()

                onDrawBehind {
                    drawRect(
                        brush = brush,
                        topLeft = Offset(line / 2f, line / 2f),
                        size = Size(size.width - line, size.height - line),
                        style = Stroke(width = line),
                    )
                    drawRect(
                        brush = brush,
                        topLeft = Offset(inset, separatorTop),
                        size = Size((size.width - inset * 2).coerceAtLeast(0f), line),
                    )
                }
            },
    ) {
        FrameNumber(text = hours, top = FrameMetrics.hourTop(), em = em, boxHeight = height, style = style, config = config)

        FrameNumber(text = minutes, top = FrameMetrics.minuteTop(em), em = em, boxHeight = height, style = style, config = config)
    }
}

@Composable
private fun FrameNumber(
    text: String,
    top: Dp,
    em: Dp,
    boxHeight: Dp,
    style: TextStyle,
    config: AodThemeConfig,
) {
    val measurer = rememberTextMeasurer()

    Crossfade(
        targetState = text,
        modifier = Modifier
            .offset(y = top)
            .fillMaxWidth()
            .height(FrameMetrics.lineBox(em)),
        animationSpec = tween(durationMillis = FrameMetrics.DIGIT_MILLIS),
        label = "FrameNumber",
    ) { digits ->
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(FrameMetrics.lineBox(em))
                .drawWithCache {
                    val layout = measurer.measure(text = digits, style = style)
                    val origin = Offset(
                        x = (size.width - layout.size.width) / 2f,
                        y = FrameMetrics.FONT_TOP * em.toPx() - layout.firstBaseline,
                    )
                    val boxTop = top.toPx() + origin.y
                    val bounds = Rect(-origin.x, -boxTop, size.width - origin.x, boxHeight.toPx() - boxTop)
                    val brush = config.gradientOf(FrameThemeSpec.ClockColor)?.toBrush(bounds) ?: SolidColor(config[FrameThemeSpec.ClockColor])

                    onDrawBehind {
                        drawText(textLayoutResult = layout, brush = brush, topLeft = origin)
                    }
                },
        )
    }
}

@Preview
@Composable
private fun AodFrameClockPreview() {
    AodFrameClock(nowMillis = System.currentTimeMillis(), config = AodThemeConfig())
}
