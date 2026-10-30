package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.words

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.words.AodWordsWeightValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.words.WordsThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Grey3A
import com.decoutkhanqindev.aodamn.presentation.theme.Mint

@Composable
fun AodWordsPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = WordsMetrics.blockTop(maxHeight), start = WordsMetrics.SideMargin, end = WordsMetrics.SideMargin),
        ) {
            AodWordsClock(nowMillis = state.nowMillis, config = state.appearance.theme.config)

            WordsDateLine(state = state)

            Column(modifier = Modifier.padding(top = WordsMetrics.NotificationsGap)) {
                WordsIcons(state = state)

                WordsLatest(state = state, gap = WordsMetrics.RuleGap)
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WordsPlayer(
                state = state,
                onIntent = onIntent,
                modifier = Modifier.padding(start = WordsMetrics.SideMargin, end = WordsMetrics.SideMargin, bottom = if (state.isWordsBatteryShown()) WordsMetrics.PlayerGap else 0.dp),
            )

            WordsBattery(state = state, modifier = Modifier.padding(bottom = WordsMetrics.BatteryBottom))
        }
    }
}

@Composable
fun AodWordsLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val hasIcons = state.notifications.icons.isNotEmpty()
    val hasLatest = state.notifications.latest != null
    val hasMedia = state.isWordsMediaShown()
    val playerGap = when {
        hasLatest -> WordsMetrics.LandscapeLatestGap
        hasIcons -> WordsMetrics.LandscapeIconsGap
        else -> 0.dp
    }
    val batteryGap = when {
        !state.isWordsBatteryShown() -> 0.dp
        hasMedia -> WordsMetrics.LandscapePlayerGap
        else -> playerGap
    }

    Row(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.weight(WordsMetrics.LANDSCAPE_EDGE_WEIGHT))

        Column(
            modifier = Modifier
                .weight(WordsMetrics.LANDSCAPE_TEXT_WEIGHT)
                .fillMaxHeight()
                .padding(horizontal = WordsMetrics.LandscapeMargin),
            verticalArrangement = Arrangement.Center,
        ) {
            AodWordsClock(nowMillis = state.nowMillis, config = state.appearance.theme.config)

            WordsDateLine(state = state)
        }

        Column(
            modifier = Modifier
                .weight(WordsMetrics.LANDSCAPE_SIDE_WEIGHT)
                .fillMaxHeight()
                .padding(horizontal = WordsMetrics.LandscapeMargin),
            verticalArrangement = Arrangement.Center,
        ) {
            WordsIcons(state = state)

            WordsLatest(state = state, gap = WordsMetrics.LandscapeIconsGap)

            WordsPlayer(state = state, onIntent = onIntent, modifier = Modifier.padding(top = playerGap))

            WordsBattery(
                state = state,
                modifier = Modifier.padding(top = batteryGap),
            )
        }

        Spacer(modifier = Modifier.weight(WordsMetrics.LANDSCAPE_EDGE_WEIGHT))
    }
}

@Composable
private fun WordsDateLine(state: AodState) {
    val isDateShown = state.extras.isDateEnabled
    val isWeatherShown = state.isWordsWeatherShown()

    AnimatedVisibility(visible = isDateShown || isWeatherShown) {
        Row(
            modifier = Modifier.padding(top = WordsMetrics.DateGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = isDateShown, modifier = Modifier.weight(1f, fill = false)) {
                WordsDate(state = state)
            }

            AnimatedVisibility(visible = isWeatherShown) {
                Box(modifier = Modifier.padding(start = if (isDateShown) WordsMetrics.WeatherGap else 0.dp)) {
                    AodWeather(
                        weather = state.weather,
                        style = state.appearance.components.styleOf(AodComponentValue.WEATHER),
                        layout = state.appearance.components.weatherLayout,
                    )
                }
            }
        }
    }
}

@Composable
private fun WordsDate(state: AodState) {
    val time = rememberZonedDateTime(state.nowMillis)

    AodWordsLine(
        text = time.format(rememberFormatter(R.string.aod_words_date_pattern)),
        slot = WordsMetrics.DateSlot,
        weight = AodWordsWeightValue.REGULAR,
        config = state.appearance.theme.config,
        colorOption = WordsThemeSpec.DateColor,
    )
}

@Composable
private fun WordsIcons(state: AodState) {
    val config = state.appearance.theme.config

    AodStatusLine(
        battery = null,
        isFlashlightOn = false,
        notifications = state.notifications.copy(latest = null),
        color = config[WordsThemeSpec.NotificationsColor],
        glyphHeight = WordsMetrics.IconRow,
        fontFamily = WordsThemeSpec.Font,
        gapScale = WordsMetrics.ICON_GAP_SCALE,
        brush = config.gradientOf(WordsThemeSpec.NotificationsColor)?.toBrush(),
    )
}

@Composable
private fun WordsLatest(
    state: AodState,
    gap: Dp,
) {
    val latest = state.notifications.latest

    AnimatedVisibility(
        visible = latest != null,
        enter = slideInVertically { height -> -height } + fadeIn(),
        exit = fadeOut(),
    ) {
        if (latest != null) {
            Column(modifier = Modifier.padding(top = if (state.notifications.icons.isNotEmpty()) gap else 0.dp)) {
                WordsRule()

                AodBadgeLatest(
                    latest = latest,
                    config = state.appearance.theme.config,
                    colorOption = WordsThemeSpec.NotificationsColor,
                    style = WordsMetrics.BadgeLatest,
                    modifier = Modifier.padding(vertical = WordsMetrics.RuleGap),
                )

                WordsRule()
            }
        }
    }
}

@Composable
private fun WordsRule() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(LocalDensity.current) { 1.toDp() })
            .background(Grey3A),
    )
}

@Composable
private fun WordsBattery(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val alpha by animateFloatAsState(
        targetValue = if (battery?.isCharging == true) 1f else WordsMetrics.IDLE_BATTERY_ALPHA,
        label = "WordsBatteryAlpha",
    )

    Row(
        modifier = modifier.graphicsLayer { this.alpha = alpha },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(visible = battery != null) {
            if (battery != null) {
                val textRes = when {
                    battery.isCharging && battery.percent >= 100 -> R.string.aod_words_battery_full
                    battery.isCharging -> R.string.aod_words_battery_charging
                    else -> R.string.aod_battery
                }

                Text(
                    text = stringResource(textRes, battery.percent),
                    style = TextStyle(
                        brush = state.appearance.theme.config.brushOf(WordsThemeSpec.BatteryColor),
                        fontSize = WordsMetrics.BatterySize,
                        fontFamily = WordsThemeSpec.Font,
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
                    .padding(start = if (battery != null) WordsMetrics.FlashlightGap else 0.dp)
                    .size(WordsMetrics.FlashlightIcon),
                tint = Mint,
            )
        }
    }
}

@Composable
private fun WordsPlayer(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(visible = state.isWordsMediaShown()) {
        Box(modifier = modifier) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }
    }
}

private fun AodState.isWordsWeatherShown(): Boolean =
    weather != null && AodComponentValue.WEATHER in appearance.theme.value.spec.components

private fun AodState.isWordsMediaShown(): Boolean =
    media != null && AodComponentValue.MEDIA in appearance.theme.value.spec.components

private fun AodState.isWordsBatteryShown(): Boolean = (battery != null && extras.isBatteryEnabled) || isFlashlightOn

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodWordsPortraitPreview() {
    AodWordsPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.WORDS)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodWordsLandscapePreview() {
    AodWordsLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.WORDS)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
