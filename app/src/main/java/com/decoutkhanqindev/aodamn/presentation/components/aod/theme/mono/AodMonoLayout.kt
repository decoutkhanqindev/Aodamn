package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.mono

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodLatestLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.lineText
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.mono.MonoThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodMonoPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height((maxHeight - MonoMetrics.BottomMargin) * MonoMetrics.TOP_WEIGHT)
                .padding(horizontal = MonoMetrics.SideMargin),
            contentAlignment = Alignment.Center,
        ) {
            MonoClockColumn(state = state, scale = 1f)
        }

        MonoInfo(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = MonoMetrics.SideMargin, end = MonoMetrics.SideMargin, bottom = MonoMetrics.BottomMargin),
        )
    }
}

@Composable
fun AodMonoLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val sideWeight by animateFloatAsState(
        targetValue = if (state.hasMonoInfo()) MonoMetrics.LANDSCAPE_SIDE_WEIGHT else 0f,
        label = "AodMonoLandscapeSide",
    )

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(MonoMetrics.LANDSCAPE_CLOCK_WEIGHT)
                .fillMaxHeight()
                .padding(horizontal = MonoMetrics.SideMargin),
            contentAlignment = Alignment.Center,
        ) {
            MonoClockColumn(state = state, scale = MonoMetrics.LANDSCAPE_SCALE)
        }

        if (sideWeight > 0.01f) {
            Box(
                modifier = Modifier
                    .weight(sideWeight)
                    .fillMaxHeight()
                    .padding(horizontal = MonoMetrics.SideMargin),
                contentAlignment = Alignment.Center,
            ) {
                MonoInfo(state = state, onIntent = onIntent, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun MonoClockColumn(
    state: AodState,
    scale: Float,
) {
    val config = state.appearance.theme.config
    val size = config[MonoThemeSpec.Size] * scale / 100f

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AodMonoClock(nowMillis = state.nowMillis, config = config, scale = scale)

        AnimatedVisibility(visible = state.extras.isDateEnabled) {
            MonoDate(state = state, fontScale = size)
        }

        AnimatedVisibility(visible = state.isMonoWeatherShown()) {
            Box(modifier = Modifier.padding(top = MonoMetrics.WeatherGap)) {
                AodWeather(
                    weather = state.weather,
                    style = state.appearance.components.styleOf(AodComponentValue.WEATHER),
                    layout = state.appearance.components.weatherLayout,
                )
            }
        }
    }
}

@Composable
private fun MonoDate(
    state: AodState,
    fontScale: Float,
) {
    val time = rememberZonedDateTime(state.nowMillis)
    val locale = LocalConfiguration.current.locales[0]

    Text(
        text = time.format(rememberFormatter(R.string.aod_mono_date_pattern)).uppercase(locale),
        modifier = Modifier.padding(top = MonoMetrics.NumberGap),
        style = TextStyle(
            brush = state.appearance.theme.config.brushOf(MonoThemeSpec.DateColor),
            fontSize = (MonoMetrics.DATE_SIZE * fontScale).sp,
            fontFamily = MonoThemeSpec.Font,
        ),
        maxLines = 1,
    )
}

@Composable
private fun MonoInfo(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(visible = state.hasMonoExtras()) {
            Column(
                modifier = Modifier.padding(bottom = MonoMetrics.ExtrasGap),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AodPanelBlock(block = AodPanelBlockValue.EVENTS, state = state, onIntent = onIntent)

                AodPanelBlock(block = AodPanelBlockValue.MEMO, state = state, onIntent = onIntent)
            }
        }

        MonoStatus(state = state)

        AnimatedVisibility(visible = state.isMonoMediaShown()) {
            Box(
                modifier = Modifier
                    .padding(top = MonoMetrics.PlayerGap)
                    .overhangEnd(MonoMetrics.PlayerEndOverhang),
            ) {
                AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
            }
        }
    }
}

@Composable
private fun MonoStatus(state: AodState) {
    val config = state.appearance.theme.config
    val color = config[MonoThemeSpec.StatusColor]
    val brush = config.gradientOf(MonoThemeSpec.StatusColor)?.toBrush()
    val latest = state.notifications.latest

    AodStatusLine(
        battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
        isFlashlightOn = state.isFlashlightOn,
        notifications = state.notifications,
        color = color,
        glyphHeight = MonoMetrics.StatusGlyph,
        fontFamily = MonoThemeSpec.Font,
        gapScale = MonoMetrics.STATUS_GAP_SCALE,
        brush = brush,
    )

    AnimatedVisibility(
        visible = latest != null,
        enter = slideInVertically { height -> -height } + fadeIn(),
        exit = fadeOut(),
    ) {
        if (latest != null) {
            AodLatestLine(
                text = latest.lineText(R.string.aod_mono_notification),
                icon = latest.icon,
                color = color,
                brush = brush,
                style = MonoMetrics.LatestLine,
                modifier = Modifier.padding(top = MonoMetrics.LatestGap),
            )
        }
    }
}

private fun Modifier.overhangEnd(amount: Dp): Modifier = layout { measurable, constraints ->
    val extra = amount.roundToPx()
    val placeable = measurable.measure(
        constraints.copy(
            minWidth = constraints.minWidth + extra,
            maxWidth = if (constraints.hasBoundedWidth) constraints.maxWidth + extra else constraints.maxWidth,
        ),
    )
    val width = (placeable.width - extra).coerceAtLeast(0)

    layout(width = width, height = placeable.height) {
        placeable.place(x = 0, y = 0)
    }
}

private fun AodState.isMonoWeatherShown(): Boolean =
    weather != null && AodComponentValue.WEATHER in appearance.theme.value.spec.components

private fun AodState.isMonoMediaShown(): Boolean =
    media != null && AodComponentValue.MEDIA in appearance.theme.value.spec.components

private fun AodState.hasMonoExtras(): Boolean {
    val components = appearance.theme.value.spec.components
    val hasEvents = AodComponentValue.EVENTS in components && extras.isCalendarEnabled && events.isNotEmpty()
    val hasMemo = AodComponentValue.MEMO in components && extras.memo.isNotBlank()
    return hasEvents || hasMemo
}

private fun AodState.hasMonoInfo(): Boolean =
    (battery != null && extras.isBatteryEnabled) ||
        isFlashlightOn ||
        notifications.icons.isNotEmpty() ||
        notifications.latest != null ||
        isMonoMediaShown() ||
        hasMonoExtras()

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodMonoPortraitPreview() {
    AodMonoPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.MONO)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodMonoLandscapePreview() {
    AodMonoLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.MONO)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
