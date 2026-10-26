package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.capsule

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.capsule.CapsuleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue

@Composable
fun AodCapsuleClock(
    nowMillis: Long,
    config: AodThemeConfig,
    unit: Dp,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val hourFormatter = rememberFormatter(if (is24HourFormat()) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h)
    val minuteFormatter = rememberFormatter(R.string.aod_minute_pattern)
    val digits = now.format(hourFormatter) + now.format(minuteFormatter)
    val description = now.format(rememberTimeFormatter())
    val textMeasurer = rememberTextMeasurer()
    val isFilled = config[CapsuleThemeSpec.Style] == AodTileStyleValue.FILLED
    val capsuleBrush = config.brushOf(CapsuleThemeSpec.CapsuleColor)
    val digitBrush = config.brushOf(CapsuleThemeSpec.DigitColor)

    Spacer(
        modifier = modifier
            .size(width = unit * CapsuleMetrics.CAPSULE_WIDTH, height = unit * CapsuleMetrics.CAPSULE_HEIGHT)
            .clearAndSetSemantics { contentDescription = description }
            .drawWithCache {
                val r = unit.toPx()
                val style = TextStyle(
                    brush = digitBrush,
                    fontSize = (r * CapsuleMetrics.DIGIT_FONT).toSp(),
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = CapsuleThemeSpec.Font,
                )
                val layouts = digits.map { digit -> textMeasurer.measure(text = digit.toString(), style = style) }
                val cell = r * CapsuleMetrics.DIGIT_PITCH
                val colon = r * CapsuleMetrics.COLON_WIDTH
                val left = (size.width - cell * layouts.size - colon) / 2f
                val centerY = size.height / 2f
                val baseline = centerY + r / 2f
                val outline = r * CapsuleMetrics.OUTLINE
                val colonX = left + cell * 2 + colon / 2f

                onDrawBehind {
                    if (isFilled) {
                        drawRoundRect(
                            brush = capsuleBrush,
                            alpha = CapsuleMetrics.FILL_ALPHA,
                            cornerRadius = CornerRadius(size.height / 2f),
                        )
                    } else {
                        drawRoundRect(
                            brush = capsuleBrush,
                            topLeft = Offset(outline / 2f, outline / 2f),
                            size = Size(size.width - outline, size.height - outline),
                            cornerRadius = CornerRadius((size.height - outline) / 2f),
                            style = Stroke(width = outline),
                        )
                    }

                    layouts.forEachIndexed { index, layout ->
                        val cellLeft = left + cell * index + if (index >= 2) colon else 0f
                        drawText(
                            textLayoutResult = layout,
                            topLeft = Offset(cellLeft + (cell - layout.size.width) / 2f, baseline - layout.firstBaseline),
                        )
                    }

                    drawCircle(
                        brush = digitBrush,
                        radius = r * CapsuleMetrics.COLON_DOT / 2f,
                        center = Offset(colonX, centerY - r * CapsuleMetrics.COLON_OFFSET),
                    )
                    drawCircle(
                        brush = digitBrush,
                        radius = r * CapsuleMetrics.COLON_DOT / 2f,
                        center = Offset(colonX, centerY + r * CapsuleMetrics.COLON_OFFSET),
                    )
                }
            },
    )
}

@Composable
fun CapsuleDate(
    nowMillis: Long,
    brush: Brush,
    unit: Dp,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val formatter = rememberFormatter(R.string.aod_theme_date_pattern)
    val fontSize = with(LocalDensity.current) { (unit * CapsuleMetrics.DATE_FONT).toSp() }

    Text(
        text = now.format(formatter).uppercase(),
        modifier = modifier,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        fontFamily = CapsuleThemeSpec.Font,
        maxLines = 1,
        softWrap = false,
        style = TextStyle(
            brush = brush,
            lineHeight = fontSize,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
        ),
    )
}

@Preview(widthDp = 393, heightDp = 160)
@Composable
private fun AodCapsuleClockPreview() {
    AodCapsuleClock(
        nowMillis = System.currentTimeMillis(),
        config = AodThemeConfig(),
        unit = 47.6.dp,
    )
}
