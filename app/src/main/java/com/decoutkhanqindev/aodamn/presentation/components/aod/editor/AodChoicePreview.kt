package com.decoutkhanqindev.aodamn.presentation.components.aod.editor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodDate
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodEvents
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodMediaControls
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodMemo
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodNotificationContent
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodNotificationIcons
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatus
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.rememberAodPreviewSamples
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.classic.AodClassicFacePreview
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.ClockFaceValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.CalendarEventUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationIconUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WeatherConditionValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WeatherUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodBatteryLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodDateLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodEventsLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodMediaLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodMemoLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodNotificationsLayoutValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodWeatherLayoutValue
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

// Ảnh xem trước của một lựa chọn ở danh sách tùy chọn (font, mặt đồng hồ, kiểu từng khối), vẽ bằng chính composable của AOD với dữ liệu
// mẫu. Ảnh đứng yên ở font / màu / cỡ mặc định của khối: đổi font, màu khi chỉnh chỉ hiện ở khung xem trước AOD. Lựa chọn không có ảnh xem
// trước (kiểu khối đặc / viền, hiệu ứng...) thì null, danh sách dùng chip chữ.
fun aodChoicePreview(entry: AodThemeChoice): (@Composable () -> Unit)? = when (entry) {
    is ClockFontValue -> {
        { FontPreview(font = entry) }
    }

    is ClockFaceValue -> {
        { AodClassicFacePreview(face = entry) }
    }

    is AodDateLayoutValue -> {
        {
            ScaledPreview(scale = 0.5f) {
                AodDate(
                    nowMillis = rememberAodPreviewSamples().nowMillis,
                    style = defaultStyle(AodComponentValue.DATE),
                    layout = entry,
                )
            }
        }
    }

    is AodBatteryLayoutValue -> {
        {
            ScaledPreview(scale = 0.8f) {
                AodStatus(
                    battery = BatteryUiModel(percent = 72, isCharging = false),
                    isBatteryEnabled = true,
                    isFlashlightOn = false,
                    style = defaultStyle(AodComponentValue.BATTERY),
                    layout = entry,
                )
            }
        }
    }

    is AodWeatherLayoutValue -> {
        {
            ScaledPreview(scale = 0.8f) {
                AodWeather(
                    weather = WeatherUiModel(temperature = 28, condition = WeatherConditionValue.CLEAR, isDay = true, updatedAtMillis = 0L),
                    style = defaultStyle(AodComponentValue.WEATHER),
                    layout = entry,
                )
            }
        }
    }

    is AodEventsLayoutValue -> {
        { EventsPreview(layout = entry, style = defaultStyle(AodComponentValue.EVENTS)) }
    }

    is AodMemoLayoutValue -> {
        {
            ScaledPreview(scale = 0.38f) {
                AodMemo(
                    memo = rememberAodPreviewSamples().memo,
                    style = defaultStyle(AodComponentValue.MEMO),
                    layout = entry,
                )
            }
        }
    }

    is AodNotificationsLayoutValue -> {
        { NotificationsPreview(layout = entry, style = defaultStyle(AodComponentValue.NOTIFICATIONS)) }
    }

    is AodMediaLayoutValue -> {
        { MediaPreview(layout = entry, style = defaultStyle(AodComponentValue.MEDIA)) }
    }

    else -> null
}

// Font / màu / cỡ mặc định của khối; ảnh xem trước tự thu nhỏ cho vừa ô.
private fun defaultStyle(component: AodComponentValue): AodComponentStyle = AodComponentsUiModel().styleOf(component)

// Vẽ nội dung ở cỡ thật rồi thu theo scale cho vừa ô xem trước 112 × 64dp.
@Composable
private fun ScaledPreview(
    scale: Float,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .requiredSize(width = 112.dp / scale, height = 64.dp / scale)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun FontPreview(font: ClockFontValue) {
    Text(
        text = stringResource(R.string.aod_font_sample),
        color = GreyED,
        fontSize = 22.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = font.fontFamily,
    )
}

@Composable
private fun EventsPreview(
    layout: AodEventsLayoutValue,
    style: AodComponentStyle,
) {
    val samples = rememberAodPreviewSamples()
    val events = remember(samples) {
        val nowMillis = samples.nowMillis
        persistentListOf(
            CalendarEventUiModel(title = samples.eventTitle, beginMillis = nowMillis, endMillis = nowMillis, isAllDay = false),
            CalendarEventUiModel(title = samples.eventTitle, beginMillis = nowMillis + 1, endMillis = nowMillis + 1, isAllDay = true),
        )
    }

    ScaledPreview(scale = 0.38f) {
        AodEvents(events = events, style = style, layout = layout)
    }
}

@Composable
private fun NotificationsPreview(
    layout: AodNotificationsLayoutValue,
    style: AodComponentStyle,
) {
    val samples = rememberAodPreviewSamples()
    val notifications = remember(samples) {
        AodNotificationsUiModel(
            icons = samples.icons
                .mapIndexed { index, icon -> NotificationIconUiModel(packageName = "sample.$index", icon = icon) }
                .toImmutableList(),
            latest = NotificationContentUiModel(
                key = "sample",
                icon = samples.icons.first(),
                title = samples.notificationTitle,
                text = samples.notificationText,
                isHidden = false,
            ),
        )
    }

    ScaledPreview(scale = 0.38f) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AodNotificationIcons(notifications = notifications, style = style, layout = layout)
            AodNotificationContent(content = notifications.latest, style = style, layout = layout)
        }
    }
}

@Composable
private fun MediaPreview(
    layout: AodMediaLayoutValue,
    style: AodComponentStyle,
) {
    val samples = rememberAodPreviewSamples()
    val media = remember(samples) {
        MediaUiModel(
            title = samples.mediaTitle,
            artist = samples.mediaArtist,
            isPlaying = true,
            canSkipToPrevious = true,
            canSkipToNext = true,
            progress = MediaProgressUiModel(durationMillis = 210_000L, positionMillis = 130_000L, updatedAtMillis = 0L, speed = 0f),
        )
    }

    ScaledPreview(scale = 0.36f) {
        AodMediaControls(media = media, theme = AodThemeUiModel(), style = style, layout = layout, onIntent = {})
    }
}
