package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.flip

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodDate
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodNotificationIcons
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatus
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.flip.FlipThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import kotlin.math.max
import kotlin.math.min

@Composable
fun AodFlipPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    AodFlipLayout(state = state, isLandscape = false)
}

@Composable
fun AodFlipLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    AodFlipLayout(state = state, isLandscape = true)
}

@Composable
private fun AodFlipLayout(
    state: AodState,
    isLandscape: Boolean,
) {
    val config = state.appearance.theme.config

    SubcomposeLayout { constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val top = subcompose("top") { FlipTopDetails(state = state) }.single().measure(loose)
        val bottom = subcompose("bottom") { FlipBottomDetails(state = state) }.single().measure(loose)
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val margin = FlipMetrics.BlockMargin.roundToPx()
        val band = max(top.height, bottom.height) + margin * 2
        val cardGap = FlipMetrics.CardGap.roundToPx()
        val desired = FlipMetrics.cardSize(
            width = width.toDp(),
            height = height.toDp(),
            size = config[FlipThemeSpec.Size],
            isLandscape = isLandscape,
        ).roundToPx()
        val heightFit = if (isLandscape) height - band * 2 else (height - band * 2 - cardGap) / 2
        val widthFit = if (isLandscape) {
            (width - cardGap) / 2
        } else {
            width
        }
        val minimumCard = FlipMetrics.PieceGap.roundToPx() + 2
        val cardSize = min(desired, min(heightFit, widthFit)).coerceAtLeast(minimumCard)
        val clock = subcompose("clock") {
            AodFlipClock(
                nowMillis = state.nowMillis,
                isLandscape = isLandscape,
                isPreviewLive = state.isPreview && state.isClockLive,
                isAppearing = !state.isPreview,
                cardSize = cardSize.toDp(),
                config = config,
            )
        }.single().measure(Constraints())
        val clockX = (width - clock.width) / 2
        val clockY = (height - clock.height) / 2
        val topX = (width - top.width) / 2
        val topY = (clockY - top.height) / 2
        val bottomX = (width - bottom.width) / 2
        val bottomY = clockY + clock.height + (height - clockY - clock.height - bottom.height) / 2

        layout(width, height) {
            top.placeRelative(topX, topY)
            clock.placeRelative(clockX, clockY)
            bottom.placeRelative(bottomX, bottomY)
        }
    }
}

@Composable
private fun FlipTopDetails(state: AodState) {
    val components = state.appearance.components
    val isDateShown = state.extras.isDateEnabled
    val isWeatherShown = state.weather != null

    Row(verticalAlignment = Alignment.CenterVertically) {
        AnimatedVisibility(visible = isDateShown) {
            AodDate(
                nowMillis = state.nowMillis,
                style = components.styleOf(AodComponentValue.DATE),
                layout = components.dateLayout,
            )
        }

        Box(modifier = Modifier.padding(start = if (isDateShown && isWeatherShown) FlipMetrics.WeatherGap else 0.dp)) {
            AodWeather(
                weather = state.weather,
                style = components.styleOf(AodComponentValue.WEATHER),
                layout = components.weatherLayout,
            )
        }
    }
}

@Composable
private fun FlipBottomDetails(state: AodState) {
    val components = state.appearance.components
    val hasPower = (state.battery != null && state.extras.isBatteryEnabled) || state.isFlashlightOn
    val hasIcons = state.notifications.icons.isNotEmpty()

    Row(verticalAlignment = Alignment.CenterVertically) {
        AodStatus(
            battery = state.battery,
            isBatteryEnabled = state.extras.isBatteryEnabled,
            isFlashlightOn = state.isFlashlightOn,
            style = components.styleOf(AodComponentValue.BATTERY),
            layout = components.batteryLayout,
        )

        Box(modifier = Modifier.padding(start = if (hasPower && hasIcons) FlipMetrics.StatusGap else 0.dp)) {
            AodNotificationIcons(
                notifications = state.notifications,
                style = components.styleOf(AodComponentValue.NOTIFICATIONS),
                layout = components.notificationsLayout,
            )
        }
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodFlipPortraitPreview() {
    AodFlipPortrait(
        state = AodState(
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.FLIP)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodFlipLandscapePreview() {
    AodFlipLandscape(
        state = AodState(
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.FLIP)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
        ),
        onIntent = {},
    )
}
