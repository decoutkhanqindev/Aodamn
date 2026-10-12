package com.decoutkhanqindev.custom_aod.presentation.components.aod

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.CalendarEventUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.WeatherConditionValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.WeatherUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodComponentStyle
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodEventsLayoutValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodMemoLayoutValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodWeatherLayoutValue
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

// Thời tiết cạnh dòng ngày, font / cỡ / màu theo khối thời tiết; mỗi kiểu một bố cục.
@Composable
fun AodWeather(
    weather: WeatherUiModel?,
    style: AodComponentStyle,
    layout: AodWeatherLayoutValue,
) {
    AnimatedContent(
        targetState = weather,
        contentKey = { it != null },
        label = "AodWeather",
    ) { target ->
        if (target != null) {
            val temperature = stringResource(R.string.aod_weather_temperature, target.temperature)
            val icon = @Composable { size: Int ->
                Icon(
                    painter = painterResource(target.iconRes),
                    contentDescription = stringResource(target.condition.labelRes),
                    modifier = Modifier.size(size.dp * style.scale),
                    tint = style.color,
                )
            }

            when (layout) {
                AodWeatherLayoutValue.INLINE -> Row(
                    modifier = Modifier.padding(start = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    icon(16)
                    WeatherText(text = temperature, style = style, fontSize = 14, modifier = Modifier.padding(start = 6.dp))
                }

                AodWeatherLayoutValue.TEMPERATURE -> WeatherText(
                    text = temperature,
                    style = style,
                    fontSize = 22,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(start = 12.dp),
                )

                AodWeatherLayoutValue.STACKED -> Column(
                    modifier = Modifier.padding(start = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    icon(18)
                    WeatherText(text = temperature, style = style, fontSize = 13)
                }

                AodWeatherLayoutValue.DETAIL -> Row(
                    modifier = Modifier.padding(start = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    icon(16)
                    WeatherText(text = temperature, style = style, fontSize = 14, modifier = Modifier.padding(start = 6.dp))
                    WeatherText(
                        text = stringResource(target.condition.labelRes),
                        style = style,
                        fontSize = 12,
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .alpha(0.7f),
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherText(
    text: String,
    style: AodComponentStyle,
    fontSize: Int,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    Text(
        text = text,
        modifier = modifier,
        color = style.color,
        fontSize = fontSize.sp * style.scale,
        fontWeight = fontWeight,
        fontFamily = style.fontFamily,
    )
}

// Sự kiện còn lại trong hôm nay, font / cỡ / màu theo khối sự kiện; mỗi kiểu một bố cục.
@Composable
fun AodEvents(
    events: ImmutableList<CalendarEventUiModel>,
    style: AodComponentStyle,
    layout: AodEventsLayoutValue,
) {
    val timeFormatter = rememberTimeFormatter()

    AnimatedContent(
        targetState = events,
        contentKey = { it.isEmpty() },
        label = "AodEvents",
    ) { target ->
        if (target.isNotEmpty()) {
            val timeOf = @Composable { event: CalendarEventUiModel ->
                if (event.isAllDay) {
                    stringResource(R.string.aod_event_all_day)
                } else {
                    rememberZonedDateTime(event.beginMillis).format(timeFormatter)
                }
            }

            when (layout) {
                // Icon lịch bên trái, mỗi sự kiện một dòng giờ + tên
                AodEventsLayoutValue.LIST -> AodBlock(modifier = Modifier.padding(top = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_calendar_check_light),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp * style.scale),
                            tint = style.color,
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                                .animateContentSize(),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            target.forEach { event ->
                                key(event.beginMillis, event.title) {
                                    EventLine(time = timeOf(event), title = event.title, style = style)
                                }
                            }
                        }
                    }
                }

                // Chấm nối nhau bằng vạch mảnh như dòng thời gian, giờ trên tên
                AodEventsLayoutValue.TIMELINE -> AodBlock(modifier = Modifier.padding(top = 16.dp)) {
                    target.forEachIndexed { index, event ->
                        key(event.beginMillis, event.title) {
                            Row {
                                Column(
                                    modifier = Modifier.padding(top = 5.dp * style.scale),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp * style.scale)
                                            .background(color = style.color, shape = CircleShape),
                                    )

                                    if (index < target.lastIndex) {
                                        Box(
                                            modifier = Modifier
                                                .padding(vertical = 3.dp)
                                                .width(1.dp)
                                                .height(26.dp * style.scale)
                                                .alpha(0.4f)
                                                .background(color = style.color),
                                        )
                                    }
                                }

                                Column(modifier = Modifier.padding(start = 12.dp)) {
                                    EventText(text = timeOf(event), style = style, fontSize = 12, isBold = true)
                                    EventText(text = event.title, style = style, fontSize = 14)
                                }
                            }
                        }
                    }
                }

                // Chỉ sự kiện gần nhất: giờ lớn, tên dưới, còn bao nhiêu sự kiện nữa
                AodEventsLayoutValue.NEXT -> Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .widthIn(max = 280.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val next = target.first()
                    EventText(text = timeOf(next), style = style, fontSize = 26, fontWeight = FontWeight.Light)
                    EventText(text = next.title, style = style, fontSize = 14)

                    if (target.size > 1) {
                        EventText(
                            text = stringResource(R.string.aod_events_more, target.size - 1),
                            style = style,
                            fontSize = 12,
                            modifier = Modifier.alpha(0.7f),
                        )
                    }
                }

                // Mỗi sự kiện một dòng căn giữa: giờ · tên
                AodEventsLayoutValue.INLINE -> Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .widthIn(max = 280.dp)
                        .animateContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    target.forEach { event ->
                        key(event.beginMillis, event.title) {
                            EventLine(time = timeOf(event), title = event.title, style = style, separator = true)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EventLine(
    time: String,
    title: String,
    style: AodComponentStyle,
    separator: Boolean = false,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        EventText(text = time, style = style, fontSize = 13, isBold = true)

        EventText(
            text = if (separator) "·  $title" else title,
            style = style,
            fontSize = 14,
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(start = 8.dp),
        )
    }
}

@Composable
private fun EventText(
    text: String,
    style: AodComponentStyle,
    fontSize: Int,
    modifier: Modifier = Modifier,
    isBold: Boolean = false,
    fontWeight: FontWeight = if (isBold) FontWeight.SemiBold else FontWeight.Normal,
) {
    Text(
        text = text,
        modifier = modifier,
        color = style.color,
        fontSize = fontSize.sp * style.scale,
        fontWeight = fontWeight,
        fontFamily = style.fontFamily,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

// Ghi nhớ, font / cỡ / màu theo khối ghi nhớ; mỗi kiểu một bố cục.
@Composable
fun AodMemo(
    memo: String,
    style: AodComponentStyle,
    layout: AodMemoLayoutValue,
) {
    AnimatedContent(
        targetState = memo.trim(),
        contentKey = { it.isEmpty() },
        label = "AodMemo",
    ) { text ->
        if (text.isNotEmpty()) {
            when (layout) {
                AodMemoLayoutValue.ICON -> AodBlock(modifier = Modifier.padding(top = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.ic_note_pencil_light),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp * style.scale),
                            tint = style.color,
                        )

                        MemoText(text = text, style = style, modifier = Modifier.padding(start = 12.dp))
                    }
                }

                // Dấu ngoặc kép lớn phía trên, chữ căn giữa
                AodMemoLayoutValue.QUOTE -> Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .widthIn(max = 280.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "“",
                        color = style.color,
                        fontSize = 36.sp * style.scale,
                        fontFamily = style.fontFamily,
                        lineHeight = 30.sp * style.scale,
                    )

                    MemoText(text = text, style = style, textAlign = TextAlign.Center)
                }

                AodMemoLayoutValue.PLAIN -> MemoText(
                    text = text,
                    style = style,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .widthIn(max = 280.dp),
                )

                // Nhãn "GHI NHỚ" giãn chữ phía trên
                AodMemoLayoutValue.TAG -> Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .widthIn(max = 280.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(R.string.aod_memo_label),
                        modifier = Modifier.alpha(0.7f),
                        color = style.color,
                        fontSize = 11.sp * style.scale,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = style.fontFamily,
                        letterSpacing = 2.sp * style.scale,
                    )

                    MemoText(text = text, style = style, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}

@Composable
private fun MemoText(
    text: String,
    style: AodComponentStyle,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        color = style.color,
        fontSize = 14.sp * style.scale,
        fontFamily = style.fontFamily,
        textAlign = textAlign,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun AodDrawing(
    drawing: Bitmap?,
    style: AodComponentStyle,
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
                    .size(120.dp * style.scale),
                colorFilter = ColorFilter.tint(style.color),
            )
        }
    }
}

@Preview(widthDp = 360)
@Composable
private fun AodExtrasPreview() {
    val style = AodComponentStyle(font = ClockFontValue.DEFAULT, scale = 1f, color = GreyB4)
    val events = persistentListOf(
        CalendarEventUiModel(title = "Team meeting", beginMillis = 0L, endMillis = 0L, isAllDay = false),
        CalendarEventUiModel(title = "Birthday", beginMillis = 1L, endMillis = 1L, isAllDay = true),
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AodWeatherLayoutValue.entries.forEach { layout ->
            AodWeather(
                weather = WeatherUiModel(
                    temperature = 28,
                    condition = WeatherConditionValue.CLEAR,
                    isDay = true,
                    updatedAtMillis = 0L,
                ),
                style = style,
                layout = layout,
            )
        }

        AodEventsLayoutValue.entries.forEach { layout -> AodEvents(events = events, style = style, layout = layout) }

        AodMemoLayoutValue.entries.forEach { layout -> AodMemo(memo = "Buy milk", style = style, layout = layout) }
    }
}
