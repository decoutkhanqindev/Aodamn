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
        val gap = FlipMetrics.DetailsGap.roundToPx()
        val cardGap = FlipMetrics.CardGap.roundToPx()
        val desired = FlipMetrics.cardSize(
            width = constraints.maxWidth.toDp(),
            height = constraints.maxHeight.toDp(),
            size = config[FlipThemeSpec.Size],
            isLandscape = isLandscape,
        ).roundToPx()
        val heightFit = if (isLandscape) {
            constraints.maxHeight - top.height - bottom.height - gap * 2
        } else {
            (constraints.maxHeight - top.height - bottom.height - gap * 2 - cardGap) / 2
        }
        val widthFit = if (isLandscape) {
            (constraints.maxWidth - cardGap) / 2
        } else {
            constraints.maxWidth
        }
        val minimumCard = FlipMetrics.PieceGap.roundToPx() + 2
        val cardSize = min(desired, min(heightFit, widthFit)).coerceAtLeast(minimumCard)
        val clock = subcompose("clock") {
            AodFlipClock(
                nowMillis = state.nowMillis,
                isLandscape = isLandscape,
                isPreviewLive = state.isPreview && state.isClockLive,
                cardSize = cardSize.toDp(),
                config = config,
            )
        }.single().measure(Constraints())
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val clockX = (width - clock.width) / 2
        val clockY = (height - clock.height) / 2
        val topX = (width - top.width) / 2
        val topY = (clockY - gap - top.height).coerceAtLeast(0)
        val bottomX = (width - bottom.width) / 2
        val bottomY = (clockY + clock.height + gap).coerceAtMost(height - bottom.height)

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
}

@Composable
private fun FlipBottomDetails(state: AodState) {
    val components = state.appearance.components
    val hasIcons = state.notifications.icons.isNotEmpty()

    Row(verticalAlignment = Alignment.CenterVertically) {
        AodNotificationIcons(
            notifications = state.notifications,
            style = components.styleOf(AodComponentValue.NOTIFICATIONS),
            layout = components.notificationsLayout,
        )

        Box(modifier = Modifier.padding(start = if (hasIcons) 16.dp else 0.dp)) {
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