package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.eclipse

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodDate
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodNotificationIcons
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatus
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodEclipsePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AodEclipseClock(
            nowMillis = state.nowMillis,
            isLive = state.isClockLive,
            isPreview = state.isPreview,
            isStill = state.isStill,
            isLandscape = false,
            config = state.appearance.theme.config,
        )

        EclipseLatest(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = EclipseMetrics.PortraitTopInset),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = EclipseMetrics.PortraitBottomInset),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            EclipseMedia(
                state = state,
                onIntent = onIntent,
                modifier = Modifier.padding(bottom = EclipseMetrics.PortraitMediaGap),
            )
            EclipseDetails(state = state)
        }
    }
}

@Composable
fun AodEclipseLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        AodEclipseClock(
            nowMillis = state.nowMillis,
            isLive = state.isClockLive,
            isPreview = state.isPreview,
            isStill = state.isStill,
            isLandscape = true,
            config = state.appearance.theme.config,
        )

        EclipseLatest(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = EclipseMetrics.LandscapeTopInset, start = EclipseMetrics.LandscapeMediaMargin),
        )

        EclipseDetails(
            state = state,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = maxHeight * EclipseMetrics.LandscapeDetailsOffset),
        )

        EclipseMedia(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(maxWidth - EclipseMetrics.LandscapeMediaMargin * 2)
                .padding(bottom = EclipseMetrics.LandscapeBottomInset),
        )
    }
}

@Composable
private fun EclipseLatest(
    modifier: Modifier = Modifier,
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    AnimatedVisibility(
        visible = state.notifications.latest != null,
        modifier = modifier,
    ) {
        AodPanelBlock(block = AodPanelBlockValue.NOTIFICATION, state = state, onIntent = onIntent)
    }
}

@Composable
private fun EclipseDetails(
    modifier: Modifier = Modifier,
    state: AodState,
) {
    val components = state.appearance.components
    val hasIcons = state.notifications.icons.isNotEmpty()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedVisibility(visible = state.extras.isDateEnabled) {
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

        Row(
            modifier = Modifier.padding(top = EclipseMetrics.StatusGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AodNotificationIcons(
                notifications = state.notifications,
                style = components.styleOf(AodComponentValue.NOTIFICATIONS),
                layout = components.notificationsLayout,
            )

            Box(modifier = Modifier.padding(start = if (hasIcons) EclipseMetrics.IconGap else 0.dp)) {
                AodStatus(
                    battery = state.battery,
                    isBatteryEnabled = state.extras.isBatteryEnabled,
                    isFlashlightOn = state.isFlashlightOn,
                    style = components.styleOf(AodComponentValue.BATTERY),
                    layout = components.batteryLayout,
                )
            }
        }
    }
}

@Composable
private fun EclipseMedia(
    modifier: Modifier = Modifier,
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    AnimatedVisibility(
        visible = state.media != null,
        modifier = modifier,
    ) {
        AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodEclipsePortraitPreview() {
    AodEclipsePortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.ECLIPSE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodEclipseLandscapePreview() {
    AodEclipseLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.ECLIPSE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
