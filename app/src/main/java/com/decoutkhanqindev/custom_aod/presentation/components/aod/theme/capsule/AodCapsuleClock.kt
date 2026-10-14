package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.capsule

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
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
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.is24HourFormat
import com.decoutkhanqindev.custom_aod.presentation.components.rememberFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.capsule.CapsuleThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.pebble.AodTileStyleValue

// Đồng hồ Viên nang: giờ và phút trong viên nang 4,76 R × 1,81 R, mỗi chữ số căn giữa một ô rộng cố định nên giờ nào viên cũng như nhau. Vẽ
// trong một lớp (đo chữ số bằng TextMeasurer, đặt theo đường chân chữ) để số đúng giữa viên theo chiều cao chữ số chứ không theo dòng chữ.
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
    val capsuleColor = config[CapsuleThemeSpec.CapsuleColor]
    val digitColor = config[CapsuleThemeSpec.DigitColor]

    Spacer(
        modifier = modifier
            .size(width = unit * CapsuleMetrics.CAPSULE_WIDTH, height = unit * CapsuleMetrics.CAPSULE_HEIGHT)
            .clearAndSetSemantics { contentDescription = description }
            .drawWithCache {
                val r = unit.toPx()
                val style = TextStyle(
                    color = digitColor,
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
                            color = capsuleColor,
                            alpha = CapsuleMetrics.FILL_ALPHA,
                            cornerRadius = CornerRadius(size.height / 2f),
                        )
                    } else {
                        drawRoundRect(
                            color = capsuleColor,
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
                        color = digitColor,
                        radius = r * CapsuleMetrics.COLON_DOT / 2f,
                        center = Offset(colonX, centerY - r * CapsuleMetrics.COLON_OFFSET),
                    )
                    drawCircle(
                        color = digitColor,
                        radius = r * CapsuleMetrics.COLON_DOT / 2f,
                        center = Offset(colonX, centerY + r * CapsuleMetrics.COLON_OFFSET),
                    )
                }
            },
    )
}

// Ngày dưới viên nang: thứ • ngày tháng in hoa đậm, chiều cao dòng bằng cỡ chữ để khoảng cách tới viên đúng như ảnh mẫu.
@Composable
fun CapsuleDate(
    nowMillis: Long,
    color: Color,
    unit: Dp,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val formatter = rememberFormatter(R.string.aod_theme_date_pattern)
    val fontSize = with(LocalDensity.current) { (unit * CapsuleMetrics.DATE_FONT).toSp() }

    Text(
        text = now.format(formatter).uppercase(),
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        fontFamily = CapsuleThemeSpec.Font,
        maxLines = 1,
        softWrap = false,
        style = TextStyle(
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
