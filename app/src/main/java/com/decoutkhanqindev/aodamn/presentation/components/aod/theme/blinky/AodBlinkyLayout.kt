package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.blinky

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.blinky.BlinkyThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Grey3A
import androidx.compose.ui.unit.min

@Composable
fun AodBlinkyPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val shortSide = min(maxWidth, maxHeight)
        val config = state.appearance.theme.config
        val eyeSize = BlinkyMetrics.eyeSize(shortSide, config[BlinkyThemeSpec.CharacterSize])
        val timeSize = BlinkyMetrics.clockSize(shortSide, config[BlinkyThemeSpec.ClockSize])

        BlinkyHeader(
            state = state,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = maxHeight * BlinkyMetrics.PORTRAIT_HEADER_TOP),
        )
        AodBlinkyEyes(
            eyeSize = eyeSize,
            color = BlinkyThemeSpec.characterColor(config),
            gradient = BlinkyThemeSpec.characterGradient(config),
            nowMillis = state.nowMillis,
            isLive = state.isClockLive,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = maxHeight * BlinkyMetrics.PORTRAIT_EYES_TOP),
        )
        AodBlinkyTime(
            nowMillis = state.nowMillis,
            color = BlinkyThemeSpec.clockColor(config),
            gradient = BlinkyThemeSpec.clockGradient(config),
            size = timeSize,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = maxHeight * BlinkyMetrics.PORTRAIT_CLOCK_TOP),
        )
        BlinkyDetails(
            state = state,
            onIntent = onIntent,
            modifier = Modifier.fillMaxSize(),
            statusTop = maxHeight * BlinkyMetrics.PORTRAIT_STATUS_TOP,
            latestTop = maxHeight * BlinkyMetrics.PORTRAIT_LATEST_TOP,
            mediaTop = maxHeight * BlinkyMetrics.PORTRAIT_MEDIA_TOP,
        )
    }
}

@Composable
fun AodBlinkyLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                onDrawBehind {
                    val divider = size.width * BlinkyMetrics.LANDSCAPE_DETAILS_LEFT
                    drawLine(color = Grey3A, start = Offset(divider, size.height * 0.148f), end = Offset(divider, size.height * 0.83f))
                }
            },
    ) {
        val shortSide = min(maxWidth, maxHeight)
        val config = state.appearance.theme.config
        val eyeSize = BlinkyMetrics.eyeSize(shortSide, config[BlinkyThemeSpec.CharacterSize])
        val timeSize = BlinkyMetrics.clockSize(shortSide, config[BlinkyThemeSpec.ClockSize])
        val detailWidth = maxWidth * (1f - BlinkyMetrics.LANDSCAPE_DETAILS_LEFT)

        AodBlinkyEyes(
            eyeSize = eyeSize,
            color = BlinkyThemeSpec.characterColor(config),
            gradient = BlinkyThemeSpec.characterGradient(config),
            nowMillis = state.nowMillis,
            isLive = state.isClockLive,
            modifier = Modifier.offset(
                x = maxWidth * BlinkyMetrics.LANDSCAPE_EYES_LEFT,
                y = maxHeight * BlinkyMetrics.LANDSCAPE_EYES_TOP,
            ),
        )
        AodBlinkyTime(
            nowMillis = state.nowMillis,
            color = BlinkyThemeSpec.clockColor(config),
            gradient = BlinkyThemeSpec.clockGradient(config),
            size = timeSize,
            modifier = Modifier.offset(
                x = maxWidth * BlinkyMetrics.LANDSCAPE_CLOCK_LEFT,
                y = maxHeight * BlinkyMetrics.LANDSCAPE_CLOCK_TOP,
            ),
        )
        BlinkyHeader(
            state = state,
            modifier = Modifier
                .width(detailWidth)
                .offset(
                    x = maxWidth * BlinkyMetrics.LANDSCAPE_DETAILS_LEFT,
                    y = maxHeight * BlinkyMetrics.LANDSCAPE_HEADER_TOP,
                ),
        )
        BlinkyDetails(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .width(detailWidth)
                .offset(x = maxWidth * BlinkyMetrics.LANDSCAPE_DETAILS_LEFT),
            statusTop = maxHeight * BlinkyMetrics.LANDSCAPE_STATUS_TOP,
            latestTop = maxHeight * BlinkyMetrics.LANDSCAPE_LATEST_TOP,
            mediaTop = maxHeight * BlinkyMetrics.LANDSCAPE_MEDIA_TOP,
        )
    }
}

@Composable
private fun BlinkyHeader(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val components = state.appearance.components

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
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
}

@Composable
private fun BlinkyDetails(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    statusTop: androidx.compose.ui.unit.Dp,
    latestTop: androidx.compose.ui.unit.Dp,
    mediaTop: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    val components = state.appearance.components

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = statusTop),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AodStatus(
                battery = state.battery,
                isBatteryEnabled = state.extras.isBatteryEnabled,
                isFlashlightOn = state.isFlashlightOn,
                style = components.styleOf(AodComponentValue.BATTERY),
                layout = components.batteryLayout,
            )
            AodNotificationIcons(
                notifications = state.notifications,
                style = components.styleOf(AodComponentValue.NOTIFICATIONS),
                layout = components.notificationsLayout,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = latestTop),
            contentAlignment = Alignment.Center,
        ) {
            AodPanelBlock(block = AodPanelBlockValue.NOTIFICATION, state = state, onIntent = onIntent)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = mediaTop),
            contentAlignment = Alignment.Center,
        ) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodBlinkyPortraitPreview() {
    AodBlinkyPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.BLINKY)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodBlinkyLandscapePreview() {
    AodBlinkyLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.BLINKY)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
