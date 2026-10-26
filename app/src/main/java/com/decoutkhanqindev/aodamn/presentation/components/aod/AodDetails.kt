package com.decoutkhanqindev.aodamn.presentation.components.aod

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodDate
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodDrawing
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodEvents
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodMediaControls
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodMemo
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodNotificationContent
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodNotificationIcons
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatus
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLandscapePlan
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun AodTopDetails(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val spec = state.appearance.theme.value.spec

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AodDateAndWeather(state = state, isWeatherShown = false)

        spec.topPanelBlocks().forEach { block ->
            key(block) {
                AodPanelBlock(block = block, state = state, onIntent = onIntent)
            }
        }
    }
}

@Composable
fun AodBottomDetails(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val spec = state.appearance.theme.value.spec

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (AodComponentValue.MEDIA in spec.components) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }

        if (AodBlockValue.STATUS !in spec.ownBlocks) {
            if (AodComponentValue.NOTIFICATIONS in spec.components) {
                AodNotificationIconsBlock(state = state)

                AodPanelBlock(block = AodPanelBlockValue.NOTIFICATION, state = state, onIntent = onIntent)
            }

            if (AodComponentValue.BATTERY in spec.components) {
                AodBatteryBlock(state = state)
            }
        }
    }
}

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

                if (AodComponentValue.BATTERY in spec.components) {
                    Box(modifier = Modifier.padding(start = if (hasIcons) 16.dp else 0.dp)) {
                        AodBatteryBlock(state = state)
                    }
                }
            }
        }
    }
}

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

fun AodThemeSpec.panelBlocks(): ImmutableList<AodPanelBlockValue> =
    AodPanelBlockValue.entries
        .filter { block ->
            block.component in components && (block != AodPanelBlockValue.NOTIFICATION || AodBlockValue.STATUS !in ownBlocks)
        }
        .toImmutableList()

fun AodThemeSpec.topPanelBlocks(): ImmutableList<AodPanelBlockValue> =
    panelBlocks().filter { block -> !block.isAtPortraitBottom() }.toImmutableList()

fun AodThemeSpec.bottomPanelBlocks(): ImmutableList<AodPanelBlockValue> =
    panelBlocks().filter { block -> block.isAtPortraitBottom() }.toImmutableList()

private fun AodPanelBlockValue.isAtPortraitBottom(): Boolean =
    this == AodPanelBlockValue.NOTIFICATION || this == AodPanelBlockValue.MEDIA

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
