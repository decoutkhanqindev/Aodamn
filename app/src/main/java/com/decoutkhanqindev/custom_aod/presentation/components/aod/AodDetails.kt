package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.AodThemeStatus
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState

// isDateShown = false: dòng ngày nằm chỗ khác (vd dưới đồng hồ ở màn ngang Cổ điển), ở đây chỉ còn thời tiết.
@Composable
fun AodDetails(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    isDateShown: Boolean = true,
) {
    val components = state.appearance.components
    val theme = state.appearance.theme
    val spec = theme.value.spec

    // Khối chủ đề không có (AodThemeSpec.components) thì không vẽ dù đang bật.
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Chủ đề tự vẽ dòng ngày cạnh đồng hồ thì ở đây không vẽ lại.
            AnimatedVisibility(visible = isDateShown && state.extras.isDateEnabled && AodBlockValue.DATE !in spec.ownBlocks) {
                AodDate(
                    nowMillis = state.nowMillis,
                    style = components.styleOf(AodComponentValue.DATE),
                    layout = components.dateLayout,
                )
            }

            if (AodComponentValue.WEATHER in spec.components) {
                AodWeather(
                    weather = state.weather,
                    style = components.styleOf(AodComponentValue.WEATHER),
                    layout = components.weatherLayout,
                )
            }
        }

        if (AodComponentValue.EVENTS in spec.components) {
            AodEvents(
                events = state.events,
                style = components.styleOf(AodComponentValue.EVENTS),
                layout = components.eventsLayout,
            )
        }

        if (AodComponentValue.MEMO in spec.components) {
            AodMemo(
                memo = state.extras.memo,
                style = components.styleOf(AodComponentValue.MEMO),
                layout = components.memoLayout,
            )
        }

        if (AodComponentValue.DRAWING in spec.components) {
            AodDrawing(drawing = state.drawing, style = components.styleOf(AodComponentValue.DRAWING))
        }

        if (AodBlockValue.STATUS in spec.ownBlocks) {
            AodThemeStatus(
                theme = theme,
                battery = state.battery,
                isBatteryEnabled = state.extras.isBatteryEnabled,
                isFlashlightOn = state.isFlashlightOn,
                notifications = state.notifications,
            )
        } else {
            if (AodComponentValue.NOTIFICATIONS in spec.components) {
                AodNotificationIcons(
                    notifications = state.notifications,
                    style = components.styleOf(AodComponentValue.NOTIFICATIONS),
                    layout = components.notificationsLayout,
                )

                AodNotificationContent(
                    content = state.notifications.latest,
                    style = components.styleOf(AodComponentValue.NOTIFICATIONS),
                    layout = components.notificationsLayout,
                )
            }

            if (AodComponentValue.BATTERY in spec.components) {
                AodStatus(
                    battery = state.battery,
                    isBatteryEnabled = state.extras.isBatteryEnabled,
                    isFlashlightOn = state.isFlashlightOn,
                    style = components.styleOf(AodComponentValue.BATTERY),
                    layout = components.batteryLayout,
                )
            }
        }

        if (AodComponentValue.MEDIA in spec.components) {
            AodMediaControls(
                media = state.media,
                theme = theme,
                style = components.styleOf(AodComponentValue.MEDIA),
                layout = components.mediaLayout,
                onIntent = onIntent,
            )
        }
    }
}

// Có khối nào của AodDetails đang hiện không (cùng điều kiện với AodDetails), để bố cục ngang bỏ cột / vạch ngăn khi không có gì.
fun AodState.hasDetails(isDateShown: Boolean = true): Boolean {
    val spec = appearance.theme.value.spec
    val shows = { component: AodComponentValue -> component in spec.components }
    val hasPower = (extras.isBatteryEnabled && battery != null) || isFlashlightOn
    val hasStatus = if (AodBlockValue.STATUS in spec.ownBlocks) {
        hasPower || notifications.icons.isNotEmpty()
    } else {
        (shows(AodComponentValue.NOTIFICATIONS) && (notifications.icons.isNotEmpty() || notifications.latest != null)) ||
            (shows(AodComponentValue.BATTERY) && hasPower)
    }
    return (isDateShown && extras.isDateEnabled && AodBlockValue.DATE !in spec.ownBlocks) ||
        (shows(AodComponentValue.WEATHER) && weather != null) ||
        (shows(AodComponentValue.EVENTS) && events.isNotEmpty()) ||
        (shows(AodComponentValue.MEMO) && extras.memo.isNotBlank()) ||
        (shows(AodComponentValue.DRAWING) && drawing != null) ||
        hasStatus ||
        (shows(AodComponentValue.MEDIA) && media != null)
}
