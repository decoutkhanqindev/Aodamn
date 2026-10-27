package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodBadgeLatest
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.ripple.RippleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodRipplePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(RippleMetrics.TopZone),
            contentAlignment = Alignment.Center,
        ) {
            RippleNotifications(state = state, maxWidth = maxWidth - RippleMetrics.PlayerMargin * 2)
        }

        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            val faceBottom = rippleFaceReach(state, minOf(maxWidth, maxHeight))

            RippleDial(state = state)

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height((maxHeight / 2 - faceBottom).coerceAtLeast(0.dp)),
                contentAlignment = Alignment.Center,
            ) {
                RippleWeather(state = state)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(RippleMetrics.PlayerTop + RippleMetrics.PlayerHeight + RippleMetrics.PlayerBottom)
                .padding(horizontal = RippleMetrics.PlayerMargin)
                .padding(top = RippleMetrics.PlayerTop),
        ) {
            RipplePlayer(state = state, onIntent = onIntent)
        }

        RippleBattery(state = state)

        Spacer(modifier = Modifier.height(RippleMetrics.BottomMargin))
    }
}

@Composable
fun AodRippleLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val reach = rippleFaceReach(state, minOf(maxWidth, maxHeight))
        val side = (maxWidth / 2 - reach - RippleMetrics.LandscapeGap * 2).coerceAtLeast(0.dp)
        val hasMedia = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components

        RippleDial(state = state)

        Crossfade(targetState = hasMedia, label = "RippleLandscape") { isMediaShown ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = RippleMetrics.LandscapeGap)
                        .width(side),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    RippleNotifications(state = state, maxWidth = side)

                    if (isMediaShown) {
                        RippleWeather(state = state, modifier = Modifier.padding(vertical = RippleMetrics.LandscapeItemGap / 2))
                        RippleBattery(state = state, modifier = Modifier.padding(vertical = RippleMetrics.LandscapeItemGap / 2))
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = RippleMetrics.LandscapeGap)
                        .width(side),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isMediaShown) {
                        RipplePlayer(state = state, onIntent = onIntent)
                    } else {
                        RippleWeather(state = state, modifier = Modifier.padding(vertical = RippleMetrics.LandscapeItemGap / 2))
                        RippleBattery(state = state, modifier = Modifier.padding(vertical = RippleMetrics.LandscapeItemGap / 2))
                    }
                }
            }
        }
    }
}

@Composable
private fun RippleDial(state: AodState) {
    AodRippleDial(
        nowMillis = state.nowMillis,
        isLive = state.isClockLive,
        isDateShown = state.extras.isDateEnabled,
        config = state.appearance.theme.config,
    )
}

private fun rippleFaceReach(state: AodState, side: Dp): Dp =
    side / 2 * (state.appearance.theme.config[RippleThemeSpec.Size] / 100f) * RippleMetrics.FACE_RADIUS * (1f + RippleMetrics.WAVE_DEPTH)

@Composable
private fun RippleNotifications(
    state: AodState,
    maxWidth: Dp,
) {
    val config = state.appearance.theme.config
    val latest = state.notifications.latest
    val hasIcons = state.notifications.icons.isNotEmpty()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AodStatusLine(
            battery = null,
            isFlashlightOn = false,
            notifications = state.notifications.copy(latest = null),
            color = config[RippleThemeSpec.NotificationsColor],
            glyphHeight = RippleMetrics.IconRow,
            fontFamily = RippleThemeSpec.Font,
            brush = config.gradientOf(RippleThemeSpec.NotificationsColor)?.toBrush(),
        )

        AnimatedVisibility(visible = latest != null) {
            if (latest != null) {
                AodBadgeLatest(
                    latest = latest,
                    config = state.appearance.theme.config,
                    colorOption = RippleThemeSpec.NotificationsColor,
                    style = RippleMetrics.BadgeLatest,
                    modifier = Modifier
                        .padding(top = if (hasIcons) RippleMetrics.LatestGap else 0.dp)
                        .widthIn(max = maxWidth),
                )
            }
        }
    }
}

@Composable
private fun RippleWeather(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val components = state.appearance.components

    AnimatedVisibility(
        visible = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components,
        modifier = modifier,
    ) {
        AodWeather(
            weather = state.weather,
            style = components.styleOf(AodComponentValue.WEATHER),
            layout = components.weatherLayout,
        )
    }
}

@Composable
private fun RippleBattery(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val alpha by animateFloatAsState(
        targetValue = if (battery?.isCharging == true) 1f else RippleMetrics.IDLE_BATTERY_ALPHA,
        label = "RippleBatteryAlpha",
    )

    AodStatusLine(
        battery = battery,
        isFlashlightOn = state.isFlashlightOn,
        notifications = AodNotificationsUiModel(),
        color = config[RippleThemeSpec.BatteryColor],
        glyphHeight = RippleMetrics.StatusGlyph,
        fontFamily = RippleThemeSpec.Font,
        modifier = modifier.graphicsLayer { this.alpha = alpha },
        brush = config.gradientOf(RippleThemeSpec.BatteryColor)?.toBrush(),
    )
}

@Composable
private fun RipplePlayer(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    AnimatedVisibility(visible = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components) {
        AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodRipplePortraitPreview() {
    AodRipplePortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.RIPPLE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodRippleLandscapePreview() {
    AodRippleLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.RIPPLE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
