package com.decoutkhanqindev.aodamn.presentation.components.aod.block

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
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodDateLayoutValue
import com.decoutkhanqindev.aodamn.presentation.theme.GreyB4

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
        AodDateLayoutValue.SPACED -> Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = weekday.uppercase(),
                modifier = Modifier.aodTint(style),
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
                    .aodTint(style)
                    .background(color = style.color, shape = CircleShape),
            )

            Text(
                text = now.format(rememberFormatter(R.string.aod_date_day_pattern)),
                modifier = Modifier.aodTint(style),
                color = style.color,
                fontSize = 15.sp * scale,
                fontFamily = style.fontFamily,
            )
        }

        AodDateLayoutValue.SENTENCE -> Text(
            text = now.format(rememberFormatter(R.string.aod_date_full_pattern)),
            modifier = modifier.aodTint(style),
            color = style.color,
            fontSize = 16.sp * scale,
            fontFamily = style.fontFamily,
        )

        AodDateLayoutValue.CALENDAR -> Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = weekday.uppercase(),
                modifier = Modifier.aodTint(style),
                color = style.color,
                fontSize = 11.sp * scale,
                fontWeight = FontWeight.SemiBold,
                fontFamily = style.fontFamily,
                letterSpacing = 2.sp * scale,
            )

            Text(
                text = now.format(rememberFormatter(R.string.aod_date_number_pattern)),
                modifier = Modifier.aodTint(style),
                color = style.color,
                fontSize = 40.sp * scale,
                fontWeight = FontWeight.Light,
                fontFamily = style.fontFamily,
                lineHeight = 44.sp * scale,
            )

            Text(
                text = now.format(rememberFormatter(R.string.aod_date_month_pattern)).uppercase(),
                modifier = Modifier.aodTint(style),
                color = style.color,
                fontSize = 11.sp * scale,
                fontFamily = style.fontFamily,
                letterSpacing = 2.sp * scale,
            )
        }

        AodDateLayoutValue.SPLIT -> Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = now.format(rememberFormatter(R.string.aod_date_number_pattern)),
                modifier = Modifier.aodTint(style),
                color = style.color,
                fontSize = 40.sp * scale,
                fontWeight = FontWeight.Light,
                fontFamily = style.fontFamily,
            )

            Column(modifier = Modifier.padding(start = 10.dp * scale)) {
                Text(
                    text = weekday,
                    modifier = Modifier.aodTint(style),
                    color = style.color,
                    fontSize = 14.sp * scale,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = style.fontFamily,
                )

                Text(
                    text = now.format(rememberFormatter(R.string.aod_date_month_year_pattern)),
                    modifier = Modifier.aodTint(style),
                    color = style.color,
                    fontSize = 12.sp * scale,
                    fontFamily = style.fontFamily,
                )
            }
        }

        AodDateLayoutValue.NUMERIC -> Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = now.format(rememberFormatter(R.string.aod_date_weekday_short_pattern)).uppercase(),
                modifier = Modifier.aodTint(style),
                color = style.color,
                fontSize = 14.sp * scale,
                fontWeight = FontWeight.SemiBold,
                fontFamily = style.fontFamily,
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp * scale)
                    .size(4.dp * scale)
                    .aodTint(style)
                    .background(color = style.color, shape = CircleShape),
            )

            Text(
                text = now.format(rememberFormatter(R.string.aod_date_numeric_pattern)),
                modifier = Modifier.aodTint(style),
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
