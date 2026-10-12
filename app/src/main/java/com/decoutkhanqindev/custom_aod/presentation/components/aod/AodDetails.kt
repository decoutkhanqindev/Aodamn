package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodComponentValue
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey5A

@Composable
fun AodDetails(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val components = state.appearance.components

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Chủ đề tự vẽ dòng ngày cạnh đồng hồ thì ở đây không vẽ lại.
            AnimatedVisibility(visible = state.extras.isDateEnabled && AodBlockValue.DATE !in state.appearance.theme.value.spec.ownBlocks) {
                AodDate(
                    nowMillis = state.nowMillis,
                    style = components.styleOf(AodComponentValue.DATE),
                    layout = components.dateLayout,
                )
            }

            AodWeather(
                weather = state.weather,
                style = components.styleOf(AodComponentValue.WEATHER),
                layout = components.weatherLayout,
            )
        }

        AodEvents(
            events = state.events,
            style = components.styleOf(AodComponentValue.EVENTS),
            layout = components.eventsLayout,
        )

        AodMemo(
            memo = state.extras.memo,
            style = components.styleOf(AodComponentValue.MEMO),
            layout = components.memoLayout,
        )

        AodDrawing(drawing = state.drawing, style = components.styleOf(AodComponentValue.DRAWING))

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

        AodStatus(
            battery = state.battery,
            isBatteryEnabled = state.extras.isBatteryEnabled,
            isFlashlightOn = state.isFlashlightOn,
            style = components.styleOf(AodComponentValue.BATTERY),
            layout = components.batteryLayout,
        )

        AodMediaControls(
            media = state.media,
            theme = state.appearance.theme,
            style = components.styleOf(AodComponentValue.MEDIA),
            layout = components.mediaLayout,
            onIntent = onIntent,
        )

        Spacer(modifier = Modifier.height(28.dp))
        AodExitHint(isVisible = state.isExitHintVisible)
    }
}

@Composable
private fun AodExitHint(isVisible: Boolean) {
    val alpha = animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "AodExitHint",
    )

    Text(
        text = stringResource(R.string.aod_hint),
        modifier = Modifier.alpha(alpha.value),
        color = Grey5A,
        fontSize = 12.sp,
    )
}
