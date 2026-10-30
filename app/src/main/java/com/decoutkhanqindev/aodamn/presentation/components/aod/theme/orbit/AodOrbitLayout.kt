package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.orbit

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.orbit.OrbitThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodOrbitPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val radius = OrbitMetrics.radius(width = maxWidth, height = maxHeight, size = state.appearance.theme.config[OrbitThemeSpec.Size])
        val infoWidth = maxWidth - OrbitMetrics.SideMargin * 2
        val playerWidth = maxWidth - OrbitMetrics.PlayerMargin * 2

        AodOrbitClock(
            nowMillis = state.nowMillis,
            isLive = state.isClockLive,
            config = state.appearance.theme.config,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(max(maxHeight / 2 - radius, 0.dp))
                .padding(horizontal = OrbitMetrics.SideMargin),
            contentAlignment = Alignment.Center,
        ) {
            OrbitExtras(state = state, onIntent = onIntent)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = OrbitMetrics.BottomMargin),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OrbitDateRow(state = state, dateSize = OrbitMetrics.DateSize)

            OrbitStatus(
                state = state,
                maxWidth = infoWidth,
                modifier = Modifier.padding(top = if (state.isOrbitDateRowShown() && state.hasOrbitStatus()) OrbitMetrics.DateGap else 0.dp),
            )

            OrbitPlayer(
                state = state,
                width = playerWidth,
                onIntent = onIntent,
                modifier = Modifier.padding(top = if (state.isOrbitDateRowShown() || state.hasOrbitStatus()) OrbitMetrics.MediaGap else 0.dp),
            )
        }
    }
}

@Composable
fun AodOrbitLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val radius = OrbitMetrics.radius(width = maxWidth, height = maxHeight, size = state.appearance.theme.config[OrbitThemeSpec.Size])
        val zone = max(maxWidth / 2 - radius - OrbitMetrics.LandscapeGap, 0.dp)
        val inner = max(zone - OrbitMetrics.LandscapeMargin, 0.dp)

        AodOrbitClock(
            nowMillis = state.nowMillis,
            isLive = state.isClockLive,
            config = state.appearance.theme.config,
            modifier = Modifier.fillMaxSize(),
        )

        Row(modifier = Modifier.fillMaxSize()) {
            OrbitZone(width = zone, modifier = Modifier.padding(start = OrbitMetrics.LandscapeMargin)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OrbitDateRow(state = state, dateSize = OrbitMetrics.LandscapeDateSize)

                    OrbitExtras(
                        state = state,
                        onIntent = onIntent,
                        modifier = Modifier.padding(top = if (state.isOrbitDateRowShown() && state.hasOrbitExtras()) OrbitMetrics.ExtrasGap else 0.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            OrbitZone(width = zone, modifier = Modifier.padding(end = OrbitMetrics.LandscapeMargin)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OrbitStatus(state = state, maxWidth = inner)

                    OrbitPlayer(
                        state = state,
                        width = inner,
                        onIntent = onIntent,
                        modifier = Modifier.padding(top = if (state.hasOrbitStatus()) OrbitMetrics.MediaGap else 0.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun OrbitZone(
    width: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun OrbitExtras(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AodPanelBlock(block = AodPanelBlockValue.EVENTS, state = state, onIntent = onIntent)

        AodPanelBlock(block = AodPanelBlockValue.MEMO, state = state, onIntent = onIntent)
    }
}

@Composable
private fun OrbitStatus(
    state: AodState,
    maxWidth: Dp,
    modifier: Modifier = Modifier,
) {
    val color = state.appearance.theme.config[OrbitThemeSpec.StatusColor]
    val brush = state.appearance.theme.config.gradientOf(OrbitThemeSpec.StatusColor)?.toBrush()
    val latest = state.notifications.latest

    AodStatusLine(
        battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
        isFlashlightOn = state.isFlashlightOn,
        notifications = state.notifications,
        color = color,
        glyphHeight = OrbitMetrics.StatusGlyph,
        fontFamily = OrbitThemeSpec.Font,
        modifier = modifier,
        gapScale = OrbitMetrics.STATUS_GAP_SCALE,
        brush = brush,
    )

    AnimatedVisibility(visible = latest != null) {
        if (latest != null) {
            AodLatestLine(
                text = latest.lineText(R.string.aod_orbit_notification),
                icon = latest.icon,
                color = color,
                brush = brush,
                style = OrbitMetrics.LatestLine,
                modifier = Modifier
                    .padding(top = OrbitMetrics.LatestGap)
                    .widthIn(max = maxWidth),
            )
        }
    }
}

@Composable
private fun OrbitDateRow(
    state: AodState,
    dateSize: TextUnit,
) {
    val isWeatherShown = AodComponentValue.WEATHER in state.appearance.theme.value.spec.components
    val components = state.appearance.components

    Row(verticalAlignment = Alignment.CenterVertically) {
        AnimatedVisibility(visible = state.extras.isDateEnabled) {
            OrbitDate(
                nowMillis = state.nowMillis,
                brush = state.appearance.theme.config.brushOf(OrbitThemeSpec.DateColor),
                size = dateSize,
                modifier = Modifier.padding(
                    end = if (isWeatherShown && state.weather != null) OrbitMetrics.WeatherGap else 0.dp,
                ),
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
private fun OrbitDate(
    nowMillis: Long,
    brush: Brush,
    size: TextUnit,
    modifier: Modifier = Modifier,
) {
    val date = rememberZonedDateTime(nowMillis).format(rememberFormatter(R.string.aod_orbit_date_pattern)).uppercase()

    Text(
        text = date,
        modifier = modifier,
        style = LocalTextStyle.current.merge(TextStyle(brush = brush)),
        fontSize = size,
        fontWeight = FontWeight.SemiBold,
        fontFamily = OrbitThemeSpec.Font,
        letterSpacing = OrbitMetrics.DateSpacing,
        maxLines = 1,
        softWrap = false,
    )
}

@Composable
private fun OrbitPlayer(
    state: AodState,
    width: Dp,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.width(width)) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }
    }
}

private fun AodState.isOrbitDateRowShown(): Boolean =
    extras.isDateEnabled || (weather != null && AodComponentValue.WEATHER in appearance.theme.value.spec.components)

private fun AodState.hasOrbitStatus(): Boolean =
    (battery != null && extras.isBatteryEnabled) || isFlashlightOn || notifications.icons.isNotEmpty() || notifications.latest != null

private fun AodState.hasOrbitExtras(): Boolean {
    val components = appearance.theme.value.spec.components
    return (AodComponentValue.EVENTS in components && events.isNotEmpty()) || (AodComponentValue.MEMO in components && extras.memo.isNotBlank())
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodOrbitPortraitPreview() {
    AodOrbitPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.ORBIT)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodOrbitLandscapePreview() {
    AodOrbitLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.ORBIT)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
