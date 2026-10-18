package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.classic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
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

// Hình thu nhỏ một mặt đồng hồ Cổ điển ở danh sách chọn mặt: màu / font mặc định (không theo màu, font đang chỉnh), giờ mẫu cố định 08:30.
@Composable
fun AodClassicFacePreview(face: ClockFaceValue) {
    val nowMillis = rememberAodPreviewSamples().nowMillis
    val hourColor = ClassicThemeSpec.HourColor.default
    val minuteColor = ClassicThemeSpec.MinuteColor.default
    val fontFamily = ClassicThemeSpec.Font.default.fontFamily

    when (face) {
        ClockFaceValue.DIGITAL -> AodDigitalClock(
            nowMillis = nowMillis,
            hourColor = hourColor,
            minuteColor = minuteColor,
            scale = 0.3f,
            fontFamily = fontFamily,
        )

        ClockFaceValue.STACKED -> AodStackedClock(
            nowMillis = nowMillis,
            hourColor = hourColor,
            minuteColor = minuteColor,
            scale = 0.24f,
            fontFamily = fontFamily,
        )

        ClockFaceValue.ANALOG, ClockFaceValue.ANALOG_MINIMAL -> AodAnalogClock(
            nowMillis = nowMillis,
            hourColor = hourColor,
            minuteColor = minuteColor,
            scale = 0.27f,
            hasTicks = face == ClockFaceValue.ANALOG,
        )
    }
}

// Chủ đề Cổ điển: 4 mặt đồng hồ (số, số xếp chồng, kim, kim tối giản), giờ và phút màu riêng.
@Composable
fun AodClassicClock(
    nowMillis: Long,
    config: AodThemeConfig,
) {
    val hourColor = config[ClassicThemeSpec.HourColor]
    val minuteColor = config[ClassicThemeSpec.MinuteColor]
    val scale = config[ClassicThemeSpec.Size] / 100f
    val fontFamily = config[ClassicThemeSpec.Font].fontFamily

    when (config[ClassicThemeSpec.Face]) {
        ClockFaceValue.DIGITAL -> AodDigitalClock(
            nowMillis = nowMillis,
            hourColor = hourColor,
            minuteColor = minuteColor,
            scale = scale,
            fontFamily = fontFamily,
        )

        ClockFaceValue.STACKED -> AodStackedClock(
            nowMillis = nowMillis,
            hourColor = hourColor,
            minuteColor = minuteColor,
            scale = scale,
            fontFamily = fontFamily,
        )

        ClockFaceValue.ANALOG -> AodAnalogClock(
            nowMillis = nowMillis,
            hourColor = hourColor,
            minuteColor = minuteColor,
            scale = scale,
            hasTicks = true,
        )

        ClockFaceValue.ANALOG_MINIMAL -> AodAnalogClock(
            nowMillis = nowMillis,
            hourColor = hourColor,
            minuteColor = minuteColor,
            scale = scale,
            hasTicks = false,
        )
    }
}

@Composable
private fun AodDigitalClock(
    nowMillis: Long,
    hourColor: Color,
    minuteColor: Color,
    scale: Float,
    fontFamily: FontFamily,
) {
    val now = rememberZonedDateTime(nowMillis)
    val timeFormatter = rememberTimeFormatter()
    val time = now.format(timeFormatter)

    // Phần trước dấu ':' là giờ, từ dấu ':' trở đi là phút (kèm AM / PM); định dạng giờ không có ':' thì cả chuỗi theo màu giờ.
    val colon = time.indexOf(':')
    val text = remember(time, hourColor, minuteColor) {
        buildAnnotatedString {
            if (colon < 0) {
                withStyle(SpanStyle(color = hourColor)) { append(time) }
            } else {
                withStyle(SpanStyle(color = hourColor)) { append(time.substring(0, colon)) }
                withStyle(SpanStyle(color = minuteColor)) { append(time.substring(colon)) }
            }
        }
    }

    Text(
        text = text,
        color = hourColor,
        fontSize = 76.sp * scale,
        fontWeight = FontWeight.Thin,
        fontFamily = fontFamily,
    )
}

@Composable
private fun AodStackedClock(
    nowMillis: Long,
    hourColor: Color,
    minuteColor: Color,
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
            color = hourColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            fontFamily = fontFamily,
            lineHeight = fontSize,
        )

        Text(
            text = now.format(minuteFormatter),
            color = minuteColor,
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
    hourColor: Color,
    minuteColor: Color,
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
                    drawLine(
                        color = if (isMajor) hourColor else Grey6E,
                        start = Offset(x = center.x, y = center.y - radius),
                        end = Offset(
                            x = center.x,
                            y = center.y - radius +
                                    (if (isMajor) 16.dp else 8.dp).toPx(),
                        ),
                        strokeWidth = (if (isMajor) 3.dp else 2.dp).toPx(),
                        cap = StrokeCap.Round,
                    )
                }
            }
        }
        rotate(degrees = now.hour % 12 * 30f + now.minute * 0.5f) {
            drawLine(
                color = hourColor,
                start = center,
                end = Offset(x = center.x, y = center.y - radius * 0.5f),
                strokeWidth = 6.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
        rotate(degrees = now.minute * 6f) {
            drawLine(
                color = minuteColor,
                start = center,
                end = Offset(x = center.x, y = center.y - radius * 0.78f),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }
        drawCircle(color = hourColor, radius = 5.dp.toPx(), center = center)
    }
}
