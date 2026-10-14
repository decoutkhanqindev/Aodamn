package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodDate
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodDrawing
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodEvents
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodMediaControls
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodMemo
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodNotificationContent
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodNotificationIcons
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodStatus
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodLandscapePlan
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

// Các khối thông tin dưới đồng hồ của màn dọc (Cổ điển, Sỏi). Khối chủ đề không có (AodThemeSpec.components) thì không vẽ dù đang bật.
@Composable
fun AodDetails(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val spec = state.appearance.theme.value.spec

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AodDateAndWeather(state = state, isWeatherShown = AodComponentValue.WEATHER in spec.components)

        if (AodComponentValue.EVENTS in spec.components) {
            AodPanelBlock(block = AodPanelBlockValue.EVENTS, state = state, onIntent = onIntent)
        }

        if (AodComponentValue.MEMO in spec.components) {
            AodPanelBlock(block = AodPanelBlockValue.MEMO, state = state, onIntent = onIntent)
        }

        if (AodComponentValue.DRAWING in spec.components) {
            AodPanelBlock(block = AodPanelBlockValue.DRAWING, state = state, onIntent = onIntent)
        }

        // Chủ đề tự vẽ dòng trạng thái (AodBlockValue.STATUS) thì pin, đèn pin, icon thông báo nằm trong bố cục của chủ đề.
        if (AodBlockValue.STATUS !in spec.ownBlocks) {
            if (AodComponentValue.NOTIFICATIONS in spec.components) {
                AodNotificationIconsBlock(state = state)

                AodPanelBlock(block = AodPanelBlockValue.NOTIFICATION, state = state, onIntent = onIntent)
            }

            if (AodComponentValue.BATTERY in spec.components) {
                AodBatteryBlock(state = state)
            }
        }

        if (AodComponentValue.MEDIA in spec.components) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }
    }
}

// Phần dưới đồng hồ của bố cục ngang: ngày (thời tiết cạnh ngày khi không có sự kiện), rồi icon thông báo và pin chung một dòng.
@Composable
fun AodHeroDetails(
    state: AodState,
    isWeatherShown: Boolean,
) {
    val spec = state.appearance.theme.value.spec

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AodDateAndWeather(state = state, isWeatherShown = isWeatherShown)

        if (AodBlockValue.STATUS !in spec.ownBlocks) {
            val hasIcons = AodComponentValue.NOTIFICATIONS in spec.components && state.notifications.icons.isNotEmpty()

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (AodComponentValue.NOTIFICATIONS in spec.components) {
                    AodNotificationIconsBlock(state = state)
                }

                // Khoảng cách chỉ khi có icon, để pin đứng một mình vẫn đúng giữa.
                if (AodComponentValue.BATTERY in spec.components) {
                    Box(modifier = Modifier.padding(start = if (hasIcons) 16.dp else 0.dp)) {
                        AodBatteryBlock(state = state)
                    }
                }
            }
        }
    }
}

// Một khối dùng chung theo kiểu, font, cỡ, màu user chọn ở Tùy chỉnh; khối tự ẩn khi không có dữ liệu.
@Composable
fun AodPanelBlock(
    block: AodPanelBlockValue,
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val components = state.appearance.components

    when (block) {
        AodPanelBlockValue.WEATHER -> AodWeather(
            weather = state.weather,
            style = components.styleOf(AodComponentValue.WEATHER),
            layout = components.weatherLayout,
        )

        AodPanelBlockValue.EVENTS -> AodEvents(
            events = state.events,
            style = components.styleOf(AodComponentValue.EVENTS),
            layout = components.eventsLayout,
        )

        AodPanelBlockValue.MEMO -> AodMemo(
            memo = state.extras.memo,
            style = components.styleOf(AodComponentValue.MEMO),
            layout = components.memoLayout,
        )

        AodPanelBlockValue.DRAWING -> AodDrawing(
            drawing = state.drawing,
            style = components.styleOf(AodComponentValue.DRAWING),
        )

        AodPanelBlockValue.NOTIFICATION -> AodNotificationContent(
            content = state.notifications.latest,
            style = components.styleOf(AodComponentValue.NOTIFICATIONS),
            layout = components.notificationsLayout,
        )

        AodPanelBlockValue.MEDIA -> AodMediaControls(
            media = state.media,
            theme = state.appearance.theme,
            style = components.styleOf(AodComponentValue.MEDIA),
            layout = components.mediaLayout,
            onIntent = onIntent,
        )
    }
}

// Ngày dùng chung (chủ đề tự vẽ ngày thì không vẽ lại) và thời tiết cạnh ngày.
@Composable
private fun AodDateAndWeather(
    state: AodState,
    isWeatherShown: Boolean,
) {
    val components = state.appearance.components
    val spec = state.appearance.theme.value.spec

    Spacer(modifier = Modifier.height(8.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        AnimatedVisibility(visible = state.extras.isDateEnabled && AodBlockValue.DATE !in spec.ownBlocks) {
            AodDate(
                nowMillis = state.nowMillis,
                style = components.styleOf(AodComponentValue.DATE),
                layout = components.dateLayout,
            )
        }

        if (isWeatherShown) {
            AodWeather(
                weather = state.weather,
                style = components.styleOf(AodComponentValue.WEATHER),
                layout = components.weatherLayout,
            )
        }
    }
}

@Composable
private fun AodNotificationIconsBlock(state: AodState) {
    val components = state.appearance.components

    AodNotificationIcons(
        notifications = state.notifications,
        style = components.styleOf(AodComponentValue.NOTIFICATIONS),
        layout = components.notificationsLayout,
    )
}

@Composable
private fun AodBatteryBlock(state: AodState) {
    val components = state.appearance.components

    AodStatus(
        battery = state.battery,
        isBatteryEnabled = state.extras.isBatteryEnabled,
        isFlashlightOn = state.isFlashlightOn,
        style = components.styleOf(AodComponentValue.BATTERY),
        layout = components.batteryLayout,
    )
}

// Khối dùng chung chủ đề có (AodThemeSpec.components), theo thứ tự màn dọc; chủ đề tự vẽ dòng trạng thái thì không có thông báo mới nhất.
fun AodThemeSpec.panelBlocks(): ImmutableList<AodPanelBlockValue> =
    AodPanelBlockValue.entries
        .filter { block ->
            block.component in components && (block != AodPanelBlockValue.NOTIFICATION || AodBlockValue.STATUS !in ownBlocks)
        }
        .toImmutableList()

// Khối dùng chung đang có dữ liệu để hiện (cùng điều kiện với AodPanelBlock), theo thứ tự màn dọc.
fun AodState.shownPanelBlocks(): ImmutableList<AodPanelBlockValue> =
    appearance.theme.value.spec.panelBlocks()
        .filter { block ->
            when (block) {
                AodPanelBlockValue.WEATHER -> weather != null
                AodPanelBlockValue.EVENTS -> events.isNotEmpty()
                AodPanelBlockValue.MEMO -> extras.memo.isNotBlank()
                AodPanelBlockValue.DRAWING -> drawing != null
                AodPanelBlockValue.NOTIFICATION -> notifications.latest != null
                AodPanelBlockValue.MEDIA -> media != null
            }
        }
        .toImmutableList()

fun AodState.landscapePlan(): AodLandscapePlan = AodLandscapePlan.of(shownPanelBlocks())
