package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.classic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.rememberAodPreviewSamples
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.ClockFaceValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.classic.ClassicThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Grey6E

@Composable
fun AodClassicFacePreview(face: ClockFaceValue) {
    val nowMillis = rememberAodPreviewSamples().nowMillis
    val hourColor = ClassicThemeSpec.HourColor.default
    val minuteColor = ClassicThemeSpec.MinuteColor.default
    val hourBrush = SolidColor(hourColor)
    val minuteBrush = SolidColor(minuteColor)
    val fontFamily = ClassicThemeSpec.Font.default.fontFamily

    when (face) {
        ClockFaceValue.DIGITAL -> AodDigitalClock(
            nowMillis = nowMillis,
            hourBrush = hourBrush,
            minuteBrush = minuteBrush,
            scale = 0.3f,
            fontFamily = fontFamily,
        )

        ClockFaceValue.STACKED -> AodStackedClock(
            nowMillis = nowMillis,
            hourBrush = hourBrush,
            minuteBrush = minuteBrush,
            scale = 0.24f,
            fontFamily = fontFamily,
        )

        ClockFaceValue.ANALOG, ClockFaceValue.ANALOG_MINIMAL -> AodAnalogClock(
            nowMillis = nowMillis,
            hourColors = listOf(hourColor),
            minuteColors = listOf(minuteColor),
            scale = 0.27f,
            hasTicks = face == ClockFaceValue.ANALOG,
        )
    }
}

@Composable
fun AodClassicClock(
    nowMillis: Long,
    config: AodThemeConfig,
) {
    val hourBrush = config.brushOf(ClassicThemeSpec.HourColor)
    val minuteBrush = config.brushOf(ClassicThemeSpec.MinuteColor)
    val hourColors = config.colorsOf(ClassicThemeSpec.HourColor)
    val minuteColors = config.colorsOf(ClassicThemeSpec.MinuteColor)
    val scale = config[ClassicThemeSpec.Size] / 100f
    val fontFamily = config[ClassicThemeSpec.Font].fontFamily

    when (config[ClassicThemeSpec.Face]) {
        ClockFaceValue.DIGITAL -> AodDigitalClock(
            nowMillis = nowMillis,
            hourBrush = hourBrush,
            minuteBrush = minuteBrush,
            scale = scale,
            fontFamily = fontFamily,
        )

        ClockFaceValue.STACKED -> AodStackedClock(
            nowMillis = nowMillis,
            hourBrush = hourBrush,
            minuteBrush = minuteBrush,
            scale = scale,
            fontFamily = fontFamily,
        )

        ClockFaceValue.ANALOG -> AodAnalogClock(
            nowMillis = nowMillis,
            hourColors = hourColors,
            minuteColors = minuteColors,
            scale = scale,
            hasTicks = true,
        )

        ClockFaceValue.ANALOG_MINIMAL -> AodAnalogClock(
            nowMillis = nowMillis,
            hourColors = hourColors,
            minuteColors = minuteColors,
            scale = scale,
            hasTicks = false,
        )
    }
}

@Composable
private fun AodDigitalClock(
    nowMillis: Long,
    hourBrush: Brush,
    minuteBrush: Brush,
    scale: Float,
    fontFamily: FontFamily,
) {
    val now = rememberZonedDateTime(nowMillis)
    val timeFormatter = rememberTimeFormatter()
    val time = now.format(timeFormatter)

    val colon = time.indexOf(':')
    val text = remember(time, hourBrush, minuteBrush) {
        buildAnnotatedString {
            if (colon < 0) {
                withStyle(SpanStyle(brush = hourBrush)) { append(time) }
            } else {
                withStyle(SpanStyle(brush = hourBrush)) { append(time.substring(0, colon)) }
                withStyle(SpanStyle(brush = minuteBrush)) { append(time.substring(colon)) }
            }
        }
    }

    Text(
        text = text,
        fontSize = 76.sp * scale,
        fontWeight = FontWeight.Thin,
        fontFamily = fontFamily,
    )
}

@Composable
private fun AodStackedClock(
    nowMillis: Long,
    hourBrush: Brush,
    minuteBrush: Brush,
    scale: Float,
    fontFamily: FontFamily,
) {
    val now = rememberZonedDateTime(nowMillis)
    val hourFormatter = rememberFormatter(
        if (is24HourFormat()) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h,
    )
    val minuteFormatter = rememberFormatter(R.string.aod_minute_pattern)
    val fontSize = 96.sp * scale

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = now.format(hourFormatter),
            style = LocalTextStyle.current.merge(TextStyle(brush = hourBrush)),
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            fontFamily = fontFamily,
            lineHeight = fontSize,
        )

        Text(
            text = now.format(minuteFormatter),
            style = LocalTextStyle.current.merge(TextStyle(brush = minuteBrush)),
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            fontFamily = fontFamily,
            lineHeight = fontSize,
        )
    }
}

@Composable
private fun AodAnalogClock(
    nowMillis: Long,
    hourColors: List<Color>,
    minuteColors: List<Color>,
    scale: Float,
    hasTicks: Boolean,
) {
    val now = rememberZonedDateTime(nowMillis)
    val timeDescription = now.format(rememberTimeFormatter())

    Canvas(
        modifier = Modifier
            .size(200.dp * scale)
            .semantics { contentDescription = timeDescription },
    ) {
        val radius = size.minDimension / 2
        if (hasTicks) {
            repeat(12) { index ->
                val isMajor = index % 3 == 0
                rotate(degrees = index * 30f) {
                    drawGradientLine(
                        colors = if (isMajor) hourColors else listOf(Grey6E),
                        start = Offset(x = center.x, y = center.y - radius),
                        end = Offset(
                            x = center.x,
                            y = center.y - radius +
                                    (if (isMajor) 16.dp else 8.dp).toPx(),
                        ),
                        strokeWidth = (if (isMajor) 3.dp else 2.dp).toPx(),
                    )
                }
            }
        }
        rotate(degrees = now.hour % 12 * 30f + now.minute * 0.5f) {
            drawGradientLine(
                colors = hourColors,
                start = center,
                end = Offset(x = center.x, y = center.y - radius * 0.5f),
                strokeWidth = 6.dp.toPx(),
            )
        }
        rotate(degrees = now.minute * 6f) {
            drawGradientLine(
                colors = minuteColors,
                start = center,
                end = Offset(x = center.x, y = center.y - radius * 0.78f),
                strokeWidth = 3.dp.toPx(),
            )
        }
        drawCircle(color = hourColors.first(), radius = 5.dp.toPx(), center = center)
    }
}

private fun DrawScope.drawGradientLine(
    colors: List<Color>,
    start: Offset,
    end: Offset,
    strokeWidth: Float,
) {
    if (colors.size == 1) {
        drawLine(color = colors.first(), start = start, end = end, strokeWidth = strokeWidth, cap = StrokeCap.Round)
    } else {
        drawLine(
            brush = Brush.linearGradient(colors = colors, start = start, end = end),
            start = start,
            end = end,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}
