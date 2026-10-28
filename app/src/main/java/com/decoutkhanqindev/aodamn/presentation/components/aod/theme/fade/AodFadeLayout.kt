package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.fade

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodBadgeLatest
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.fade.FadeThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Mint

@Composable
fun AodFadePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val config = state.appearance.theme.config

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val weight = config[FadeThemeSpec.Weight]
        val size = config[FadeThemeSpec.Size]
        val em = FadeMetrics.portraitEm(height = maxHeight, size = size, weight = weight)

        AodFadeClock(
            nowMillis = state.nowMillis,
            config = config,
            em = em,
            hourTop = FadeMetrics.portraitHourBottom(height = maxHeight, size = size) - em * FadeMetrics.lineBox(weight),
            isAppearing = !state.isPreview,
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(vertical = FadeMetrics.BlockPadding, horizontal = FadeMetrics.SidePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FadeRow(state = state, modifier = Modifier.padding(FadeMetrics.RowPadding))

            FadeLatest(state = state, hasTopGap = state.hasFadeRow())
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FadePlayer(state = state, onIntent = onIntent, modifier = Modifier.padding(FadeMetrics.PlayerMargin))

            FadeBattery(state = state, modifier = Modifier.padding(bottom = FadeMetrics.BatteryBottom))
        }
    }
}

@Composable
fun AodFadeLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val config = state.appearance.theme.config
    val hasMedia = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val weight = config[FadeThemeSpec.Weight]
        val em = FadeMetrics.landscapeEm(width = maxWidth, height = maxHeight, size = config[FadeThemeSpec.Size], weight = weight)
        val zone = (maxWidth / 2 - em * FadeMetrics.ADVANCE).coerceAtLeast(0.dp)

        AodFadeClock(
            nowMillis = state.nowMillis,
            config = config,
            em = em,
            hourTop = FadeMetrics.landscapeHourTop(height = maxHeight, em = em, weight = weight),
            isAppearing = !state.isPreview,
            modifier = Modifier.fillMaxSize(),
        )

        Crossfade(targetState = hasMedia, label = "AodFadeLandscape") { isMediaShown ->
            Row(modifier = Modifier.fillMaxSize()) {
                FadeZone(width = zone, start = FadeMetrics.LandscapeMargin, end = FadeMetrics.LandscapeGap) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (isMediaShown) {
                            FadeFitRow(state = state)

                            FadeLatest(state = state, hasTopGap = state.hasFadeRow(), gap = FadeMetrics.LandscapeItemGap)
                        } else {
                            FadeRow(state = state, hasIcons = false)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                FadeZone(width = zone, start = FadeMetrics.LandscapeGap, end = FadeMetrics.LandscapeMargin) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (isMediaShown) {
                            FadePlayer(state = state, onIntent = onIntent, modifier = Modifier.padding(bottom = FadeMetrics.LandscapePlayerGap))
                        } else {
                            FadeIcons(state = state)

                            FadeLatest(
                                state = state,
                                hasTopGap = state.notifications.icons.isNotEmpty(),
                                gap = FadeMetrics.LandscapeBlockGap,
                            )
                        }

                        FadeBattery(
                            state = state,
                            modifier = Modifier.padding(
                                top = if (!isMediaShown && state.hasFadeStatus()) FadeMetrics.LandscapeBlockGap else 0.dp,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FadeZone(
    width: Dp,
    start: Dp,
    end: Dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .padding(start = start, end = end),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun FadeFitRow(state: AodState) {
    val line: @Composable () -> Unit = { FadeRow(state = state) }
    val stacked: @Composable () -> Unit = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FadeRow(state = state, hasIcons = false)

            FadeIcons(
                state = state,
                modifier = Modifier.padding(top = if (state.hasFadeDateLine()) FadeMetrics.LandscapeItemGap else 0.dp),
            )
        }
    }

    Layout(contents = listOf(line, stacked)) { (single, wrapped), constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val line = single.first().measure(loose.copy(maxWidth = Constraints.Infinity))
        val shown = if (line.width <= constraints.maxWidth) line else wrapped.first().measure(loose)

        layout(width = shown.width, height = shown.height) {
            shown.place(x = 0, y = 0)
        }
    }
}

@Composable
private fun FadeRow(
    state: AodState,
    modifier: Modifier = Modifier,
    hasIcons: Boolean = true,
) {
    val config = state.appearance.theme.config
    val isDateShown = state.extras.isDateEnabled
    val isWeatherShown = state.isFadeWeatherShown()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(visible = isDateShown) {
            FadeDate(state = state)
        }

        AnimatedVisibility(visible = isWeatherShown) {
            Box(modifier = Modifier.padding(start = if (isDateShown) FadeMetrics.WeatherGap else 0.dp)) {
                AodWeather(
                    weather = state.weather,
                    style = state.appearance.components.styleOf(AodComponentValue.WEATHER),
                    layout = state.appearance.components.weatherLayout,
                )
            }
        }

        if (hasIcons) {
            AnimatedVisibility(visible = state.notifications.icons.isNotEmpty() && state.hasFadeDateLine()) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = FadeMetrics.SeparatorGap)
                        .size(width = FadeMetrics.SeparatorWidth, height = FadeMetrics.SeparatorHeight)
                        .background(brush = config.brushOf(FadeThemeSpec.NotificationsColor)),
                )
            }

            FadeIcons(state = state)
        }
    }
}

@Composable
private fun FadeDate(state: AodState) {
    val time = rememberZonedDateTime(state.nowMillis)

    Text(
        text = time.format(rememberFormatter(R.string.aod_fade_date_pattern)),
        style = TextStyle(
            brush = state.appearance.theme.config.brushOf(FadeThemeSpec.DateColor),
            fontSize = FadeMetrics.DateSize,
            fontFamily = FadeThemeSpec.Font,
        ),
        maxLines = 1,
    )
}

@Composable
private fun FadeIcons(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config

    AodStatusLine(
        battery = null,
        isFlashlightOn = false,
        notifications = state.notifications.copy(latest = null),
        color = config[FadeThemeSpec.NotificationsColor],
        glyphHeight = FadeMetrics.IconRow,
        fontFamily = FadeThemeSpec.Font,
        modifier = modifier,
        gapScale = FadeMetrics.ICON_GAP_SCALE,
        brush = config.gradientOf(FadeThemeSpec.NotificationsColor)?.toBrush(),
    )
}

@Composable
private fun FadeLatest(
    state: AodState,
    hasTopGap: Boolean,
    gap: Dp = FadeMetrics.LatestGap,
) {
    val latest = state.notifications.latest

    AnimatedVisibility(
        visible = latest != null,
        enter = slideInVertically { height -> -height } + fadeIn(),
        exit = fadeOut(),
    ) {
        if (latest != null) {
            AodBadgeLatest(
                latest = latest,
                config = state.appearance.theme.config,
                colorOption = FadeThemeSpec.NotificationsColor,
                style = FadeMetrics.BadgeLatest,
                modifier = Modifier.padding(top = if (hasTopGap) gap else 0.dp),
            )
        }
    }
}

@Composable
private fun FadeBattery(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val alpha by animateFloatAsState(
        targetValue = if (battery?.isCharging == true) 1f else FadeMetrics.IDLE_BATTERY_ALPHA,
        label = "FadeBatteryAlpha",
    )

    Row(
        modifier = modifier.graphicsLayer { this.alpha = alpha },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(visible = battery != null) {
            if (battery != null) {
                val textRes = when {
                    battery.isCharging && battery.percent >= 100 -> R.string.aod_fade_battery_full
                    battery.isCharging -> R.string.aod_fade_battery_charging
                    else -> R.string.aod_battery
                }

                Text(
                    text = stringResource(textRes, battery.percent),
                    style = TextStyle(
                        brush = state.appearance.theme.config.brushOf(FadeThemeSpec.BatteryColor),
                        fontSize = FadeMetrics.BatterySize,
                        fontFamily = FadeThemeSpec.Font,
                    ),
                    maxLines = 1,
                )
            }
        }

        AnimatedVisibility(visible = state.isFlashlightOn) {
            Icon(
                painter = painterResource(R.drawable.ic_flashlight_light),
                contentDescription = stringResource(R.string.aod_flashlight_on),
                modifier = Modifier
                    .padding(start = if (battery != null) FadeMetrics.FlashlightGap else 0.dp)
                    .size(FadeMetrics.FlashlightIcon),
                tint = Mint,
            )
        }
    }
}

@Composable
private fun FadePlayer(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(visible = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components) {
        Box(modifier = modifier) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }
    }
}

private fun AodState.isFadeWeatherShown(): Boolean =
    weather != null && AodComponentValue.WEATHER in appearance.theme.value.spec.components

private fun AodState.hasFadeDateLine(): Boolean = extras.isDateEnabled || isFadeWeatherShown()

private fun AodState.hasFadeRow(): Boolean = hasFadeDateLine() || notifications.icons.isNotEmpty()

private fun AodState.hasFadeStatus(): Boolean = notifications.icons.isNotEmpty() || notifications.latest != null

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodFadePortraitPreview() {
    AodFadePortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.FADE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodFadeLandscapePreview() {
    AodFadeLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.FADE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
