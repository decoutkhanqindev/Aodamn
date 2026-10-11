package com.decoutkhanqindev.custom_aod.presentation.components.aod

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.CalendarEventUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.WeatherConditionValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.WeatherUiModel
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey8A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

// Viên viền mảnh cạnh dòng ngày, như viên pin.
@Composable
fun AodWeather(
    weather: WeatherUiModel?,
    fontFamily: FontFamily,
) {
    AnimatedContent(
        targetState = weather,
        contentKey = { it != null },
        label = "AodWeather",
    ) { target ->
        if (target != null) {
            AodPill(modifier = Modifier.padding(start = 12.dp)) {
                Icon(
                    painter = painterResource(target.iconRes),
                    contentDescription = stringResource(target.condition.labelRes),
                    modifier = Modifier.size(16.dp),
                    tint = Grey8A,
                )

                Text(
                    text = stringResource(R.string.aod_weather_temperature, target.temperature),
                    color = GreyB4,
                    fontSize = 14.sp,
                    fontFamily = fontFamily,
                )
            }
        }
    }
}

// Thẻ như thẻ thông báo: vòng icon lịch, mỗi sự kiện một dòng giờ bắt đầu + tên.
@Composable
fun AodEvents(events: ImmutableList<CalendarEventUiModel>) {
    val timeFormatter = rememberTimeFormatter()

    AnimatedContent(
        targetState = events,
        contentKey = { it.isEmpty() },
        label = "AodEvents",
    ) { target ->
        if (target.isNotEmpty()) {
            AodCard(modifier = Modifier.padding(top = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AodIconRing(size = 32.dp) {
                        Icon(
                            painter = painterResource(R.drawable.ic_calendar_check_light),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = GreyB4,
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .animateContentSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        target.forEach { event ->
                            key(event.beginMillis, event.title) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (event.isAllDay) {
                                            stringResource(R.string.aod_event_all_day)
                                        } else {
                                            rememberZonedDateTime(event.beginMillis).format(timeFormatter)
                                        },
                                        color = GreyB4,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = FontFamilyPlusJakartaSans,
                                    )

                                    Text(
                                        text = event.title,
                                        modifier = Modifier.padding(start = 8.dp),
                                        color = Grey8A,
                                        fontSize = 14.sp,
                                        fontFamily = FontFamilyPlusJakartaSans,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Thẻ như thẻ thông báo: vòng icon ghi chú, chữ theo font chủ đề.
@Composable
fun AodMemo(
    memo: String,
    fontFamily: FontFamily,
) {
    AnimatedContent(
        targetState = memo.trim(),
        contentKey = { it.isEmpty() },
        label = "AodMemo",
    ) { text ->
        if (text.isNotEmpty()) {
            AodCard(modifier = Modifier.padding(top = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AodIconRing(size = 32.dp) {
                        Icon(
                            painter = painterResource(R.drawable.ic_note_pencil_light),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = GreyB4,
                        )
                    }

                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 12.dp),
                        color = GreyB4,
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
fun AodDrawing(
    drawing: Bitmap?,
    color: Color,
) {
    AnimatedContent(
        targetState = drawing,
        contentKey = { it != null },
        label = "AodDrawing",
    ) { bitmap ->
        if (bitmap != null) {
            Image(
                bitmap = remember(bitmap) { bitmap.asImageBitmap() },
                contentDescription = stringResource(R.string.aod_drawing),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(120.dp),
                colorFilter = ColorFilter.tint(color),
            )
        }
    }
}


@Preview(widthDp = 360)
@Composable
private fun AodExtrasPreview() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AodWeather(
            weather = WeatherUiModel(
                temperature = 28,
                condition = WeatherConditionValue.CLEAR,
                isDay = true,
                updatedAtMillis = 0L,
            ),
            fontFamily = FontFamilyPlusJakartaSans,
        )

        AodEvents(
            events = persistentListOf(
                CalendarEventUiModel(title = "Team meeting", beginMillis = 0L, endMillis = 0L, isAllDay = false),
                CalendarEventUiModel(title = "Birthday", beginMillis = 1L, endMillis = 1L, isAllDay = true),
            ),
        )

        AodMemo(memo = "Buy milk", fontFamily = FontFamilyPlusJakartaSans)
    }
}
