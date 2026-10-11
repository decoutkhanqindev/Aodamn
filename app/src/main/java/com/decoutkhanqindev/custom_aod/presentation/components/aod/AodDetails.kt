package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.rememberFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.custom_aod.presentation.theme.AccentGradient
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey5A
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey8A

@Composable
fun AodDetails(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Chủ đề tự vẽ dòng ngày cạnh đồng hồ thì ở đây không vẽ lại.
            AnimatedVisibility(visible = state.extras.isDateEnabled && !state.appearance.theme.value.spec.isDateBuiltIn) {
                AodDate(
                    nowMillis = state.nowMillis,
                    accent = state.appearance.color,
                    fontFamily = state.appearance.font.fontFamily,
                )
            }

            AodWeather(weather = state.weather, fontFamily = state.appearance.font.fontFamily)
        }

        AodEvents(events = state.events)

        AodMemo(memo = state.extras.memo, fontFamily = state.appearance.font.fontFamily)

        AodDrawing(drawing = state.drawing, color = state.appearance.color)

        AodNotificationIcons(notifications = state.notifications)

        AodNotificationContent(content = state.notifications.latest)

        AodStatus(
            battery = state.battery,
            isBatteryEnabled = state.extras.isBatteryEnabled,
            isFlashlightOn = state.isFlashlightOn,
        )

        AodMediaControls(
            media = state.media,
            theme = state.appearance.theme,
            onIntent = onIntent,
        )

        Spacer(modifier = Modifier.height(28.dp))
        AodExitHint(isVisible = state.isExitHintVisible)
    }
}

// Thứ in hoa theo màu chủ đề, chấm gradient của app, ngày tháng xám.
@Composable
private fun AodDate(
    nowMillis: Long,
    accent: Color,
    fontFamily: FontFamily,
) {
    val now = rememberZonedDateTime(nowMillis)
    val weekdayFormatter = rememberFormatter(R.string.aod_date_weekday_pattern)
    val dayFormatter = rememberFormatter(R.string.aod_date_day_pattern)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = now.format(weekdayFormatter).uppercase(),
            color = accent,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = fontFamily,
            letterSpacing = 1.5.sp,
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(5.dp)
                .background(brush = AccentGradient, shape = CircleShape),
        )

        Text(
            text = now.format(dayFormatter),
            color = Grey8A,
            fontSize = 15.sp,
            fontFamily = fontFamily,
        )
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
