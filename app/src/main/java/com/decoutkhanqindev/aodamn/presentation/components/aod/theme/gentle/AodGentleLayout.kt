package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gentle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodLatestLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.lineText
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gentle.GentleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodGentlePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AodGentleClock(
            nowMillis = state.nowMillis,
            config = state.appearance.theme.config,
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = GentleMetrics.PortraitTop, start = GentleMetrics.PortraitSide, end = GentleMetrics.PortraitSide),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GentleDetails(state = state, dateSize = GentleMetrics.DateSize, detailsGap = GentleMetrics.DetailsGap)
        }

        GentleBottom(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = GentleMetrics.PortraitSide, end = GentleMetrics.PortraitSide, bottom = GentleMetrics.PortraitBottom),
        )
    }
}

@Composable
fun AodGentleLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(GentleMetrics.LANDSCAPE_SIDE_WEIGHT)
                .fillMaxHeight()
                .padding(horizontal = GentleMetrics.LandscapeSide),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GentleDetails(
                state = state,
                dateSize = GentleMetrics.LandscapeDateSize,
                detailsGap = GentleMetrics.LandscapeDetailsGap,
            )
        }

        AodGentleClock(
            nowMillis = state.nowMillis,
            config = state.appearance.theme.config,
            modifier = Modifier
                .weight(GentleMetrics.LANDSCAPE_CLOCK_WEIGHT)
                .fillMaxHeight(),
        )

        GentleBottom(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .weight(GentleMetrics.LANDSCAPE_SIDE_WEIGHT)
                .fillMaxHeight()
                .padding(horizontal = GentleMetrics.LandscapeSide),
        )
    }
}

@Composable
private fun GentleDetails(
    state: AodState,
    dateSize: androidx.compose.ui.unit.TextUnit,
    detailsGap: Dp,
) {
    val config = state.appearance.theme.config
    val isDateShown = state.extras.isDateEnabled
    val isWeatherShown = state.weather != null
    val hasIcons = state.notifications.icons.isNotEmpty()
    val latest = state.notifications.latest

    AnimatedVisibility(visible = isDateShown || isWeatherShown) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedVisibility(visible = isDateShown) {
                val time = rememberZonedDateTime(state.nowMillis)
                Text(
                    text = time.format(rememberFormatter(R.string.aod_gentle_date_pattern)),
                    style = TextStyle(
                        brush = config.brushOf(GentleThemeSpec.NotificationsColor),
                        fontSize = dateSize,
                        fontFamily = GentleThemeSpec.Font,
                        letterSpacing = GentleMetrics.DateSpacing,
                    ),
                    maxLines = 1,
                )
            }

            AnimatedVisibility(visible = isWeatherShown) {
                Box(modifier = Modifier.padding(start = if (isDateShown) GentleMetrics.WeatherGap else 0.dp)) {
                    AodWeather(
                        weather = state.weather,
                        style = state.appearance.components.styleOf(AodComponentValue.WEATHER),
                        layout = state.appearance.components.weatherLayout,
                    )
                }
            }
        }
    }

    AnimatedVisibility(visible = hasIcons) {
        AodStatusLine(
            battery = null,
            isFlashlightOn = false,
            notifications = state.notifications.copy(latest = null),
            color = config[GentleThemeSpec.NotificationsColor],
            glyphHeight = GentleMetrics.IconRow,
            fontFamily = GentleThemeSpec.Font,
            modifier = Modifier.padding(top = if (isDateShown || isWeatherShown) detailsGap else 0.dp),
            gapScale = GentleMetrics.ICON_GAP_SCALE,
            brush = config.gradientOf(GentleThemeSpec.NotificationsColor)?.toBrush(),
        )
    }

    AnimatedVisibility(
        visible = latest != null,
        enter = slideInVertically { height -> -height } + fadeIn(),
        exit = fadeOut(),
    ) {
        if (latest != null) {
            AodLatestLine(
                text = latest.lineText(R.string.aod_gentle_notification),
                icon = latest.icon,
                color = config[GentleThemeSpec.NotificationsColor],
                brush = config.gradientOf(GentleThemeSpec.NotificationsColor)?.toBrush(),
                style = GentleMetrics.LatestLine,
                modifier = Modifier.padding(top = if (hasIcons || isDateShown || isWeatherShown) GentleMetrics.LatestGap else 0.dp),
            )
        }
    }
}

@Composable
private fun GentleBottom(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasMedia = state.media != null
    val hasBattery = state.battery != null && state.extras.isBatteryEnabled

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AnimatedVisibility(visible = hasMedia) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }

        GentleBattery(
            state = state,
            modifier = Modifier.padding(top = if (hasMedia && hasBattery) GentleMetrics.LandscapeItemGap else 0.dp),
        )
    }
}

@Composable
private fun GentleBattery(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val alpha by animateFloatAsState(
        targetValue = if (battery?.isCharging == true) 1f else GentleMetrics.IDLE_BATTERY_ALPHA,
        label = "GentleBatteryAlpha",
    )

    AodStatusLine(
        battery = battery,
        isFlashlightOn = state.isFlashlightOn,
        notifications = state.notifications.copy(icons = kotlinx.collections.immutable.persistentListOf(), overflowCount = 0, latest = null),
        color = state.appearance.theme.config[GentleThemeSpec.NotificationsColor],
        glyphHeight = GentleMetrics.BatteryGlyph,
        fontFamily = GentleThemeSpec.Font,
        modifier = modifier.graphicsLayer { this.alpha = alpha },
        brush = state.appearance.theme.config.gradientOf(GentleThemeSpec.NotificationsColor)?.toBrush(),
    )
}
