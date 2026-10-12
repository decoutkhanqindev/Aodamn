package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.rememberFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodComponentStyle
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodDateLayoutValue
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4

// Dòng ngày dưới đồng hồ (chủ đề không tự vẽ ngày), font / cỡ / màu theo khối ngày; mỗi kiểu một bố cục, không viền.
@Composable
fun AodDate(
    nowMillis: Long,
    style: AodComponentStyle,
    layout: AodDateLayoutValue,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val weekday = now.format(rememberFormatter(R.string.aod_date_weekday_pattern))
    val scale = style.scale

    when (layout) {
        // THỨ BẢY • 11 tháng 10
        AodDateLayoutValue.SPACED -> Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = weekday.uppercase(),
                color = style.color,
                fontSize = 13.sp * scale,
                fontWeight = FontWeight.SemiBold,
                fontFamily = style.fontFamily,
                letterSpacing = 1.5.sp * scale,
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp * scale)
                    .size(4.dp * scale)
                    .background(color = style.color, shape = CircleShape),
            )

            Text(
                text = now.format(rememberFormatter(R.string.aod_date_day_pattern)),
                color = style.color,
                fontSize = 15.sp * scale,
                fontFamily = style.fontFamily,
            )
        }

        // Thứ Bảy, 11 tháng 10
        AodDateLayoutValue.SENTENCE -> Text(
            text = now.format(rememberFormatter(R.string.aod_date_full_pattern)),
            modifier = modifier,
            color = style.color,
            fontSize = 16.sp * scale,
            fontFamily = style.fontFamily,
        )

        // Tờ lịch: thứ nhỏ, số ngày lớn, tháng nhỏ
        AodDateLayoutValue.CALENDAR -> Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = weekday.uppercase(),
                color = style.color,
                fontSize = 11.sp * scale,
                fontWeight = FontWeight.SemiBold,
                fontFamily = style.fontFamily,
                letterSpacing = 2.sp * scale,
            )

            Text(
                text = now.format(rememberFormatter(R.string.aod_date_number_pattern)),
                color = style.color,
                fontSize = 40.sp * scale,
                fontWeight = FontWeight.Light,
                fontFamily = style.fontFamily,
                lineHeight = 44.sp * scale,
            )

            Text(
                text = now.format(rememberFormatter(R.string.aod_date_month_pattern)).uppercase(),
                color = style.color,
                fontSize = 11.sp * scale,
                fontFamily = style.fontFamily,
                letterSpacing = 2.sp * scale,
            )
        }

        // Số ngày lớn bên trái, thứ và tháng năm bên phải
        AodDateLayoutValue.SPLIT -> Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = now.format(rememberFormatter(R.string.aod_date_number_pattern)),
                color = style.color,
                fontSize = 40.sp * scale,
                fontWeight = FontWeight.Light,
                fontFamily = style.fontFamily,
            )

            Column(modifier = Modifier.padding(start = 10.dp * scale)) {
                Text(
                    text = weekday,
                    color = style.color,
                    fontSize = 14.sp * scale,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = style.fontFamily,
                )

                Text(
                    text = now.format(rememberFormatter(R.string.aod_date_month_year_pattern)),
                    color = style.color,
                    fontSize = 12.sp * scale,
                    fontFamily = style.fontFamily,
                )
            }
        }

        // T7 · 11.10.2026
        AodDateLayoutValue.NUMERIC -> Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = now.format(rememberFormatter(R.string.aod_date_weekday_short_pattern)).uppercase(),
                color = style.color,
                fontSize = 14.sp * scale,
                fontWeight = FontWeight.SemiBold,
                fontFamily = style.fontFamily,
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp * scale)
                    .size(4.dp * scale)
                    .background(color = style.color, shape = CircleShape),
            )

            Text(
                text = now.format(rememberFormatter(R.string.aod_date_numeric_pattern)),
                color = style.color,
                fontSize = 15.sp * scale,
                fontFamily = style.fontFamily,
                letterSpacing = 1.5.sp * scale,
            )
        }
    }
}

@Preview
@Composable
private fun AodDatePreview() {
    Column {
        AodDateLayoutValue.entries.forEach { layout ->
            AodDate(
                nowMillis = System.currentTimeMillis(),
                style = AodComponentStyle(font = ClockFontValue.DEFAULT, scale = 1f, color = GreyB4),
                layout = layout,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
