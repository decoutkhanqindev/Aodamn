package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.frame

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
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
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.frame.FrameThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodFramePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        FrameZone(weight = FrameMetrics.TOP_WEIGHT) {
            Column(
                modifier = Modifier.padding(horizontal = FrameMetrics.ZonePadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FrameDateLine(state = state, gap = FrameMetrics.DateGap)

                FrameStatus(state = state, latestGap = FrameMetrics.LatestGap)
            }
        }

        FrameZone(weight = FrameMetrics.MIDDLE_WEIGHT) {
            AodFrameClock(nowMillis = state.nowMillis, config = state.appearance.theme.config)
        }

        FrameZone(weight = FrameMetrics.BOTTOM_WEIGHT) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FrameExtras(state = state, onIntent = onIntent)

                FramePlayer(
                    state = state,
                    onIntent = onIntent,
                    modifier = Modifier.padding(start = FrameMetrics.PlayerMargin, top = FrameMetrics.ExtrasGap, end = FrameMetrics.PlayerMargin),
                )
            }
        }
    }
}

@Composable
fun AodFrameLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = FrameMetrics.LandscapeMargin),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FrameDateLine(state = state, gap = FrameMetrics.LandscapePlayerGap)

                FrameExtras(state = state, onIntent = onIntent)
            }
        }

        AodFrameClock(
            nowMillis = state.nowMillis,
            config = state.appearance.theme.config,
            modifier = Modifier.padding(horizontal = FrameMetrics.LandscapeGap),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FrameStatus(state = state, latestGap = FrameMetrics.LandscapeItemGap)

                FramePlayer(state = state, onIntent = onIntent, modifier = Modifier.padding(top = FrameMetrics.LandscapePlayerGap))
            }
        }
    }
}

@Composable
private fun ColumnScope.FrameZone(
    weight: Float,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(weight)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun FrameDateLine(
    state: AodState,
    gap: Dp,
) {
    val isDateShown = state.extras.isDateEnabled
    val isWeatherShown = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components

    AnimatedVisibility(visible = isDateShown || isWeatherShown) {
        Row(
            modifier = Modifier.padding(bottom = gap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = isDateShown) {
                FrameDate(state = state)
            }

            AnimatedVisibility(visible = isWeatherShown) {
                Box(modifier = Modifier.padding(start = if (isDateShown) FrameMetrics.WeatherGap else 0.dp)) {
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
private fun FrameDate(state: AodState) {
    val time = rememberZonedDateTime(state.nowMillis)

    Text(
        text = time.format(rememberFormatter(R.string.aod_frame_date_pattern)),
        style = TextStyle(
            brush = state.appearance.theme.config.brushOf(FrameThemeSpec.DateColor),
            fontSize = FrameMetrics.DateSize,
            fontFamily = FrameThemeSpec.Font,
        ),
        maxLines = 1,
    )
}

@Composable
private fun FrameStatus(
    state: AodState,
    latestGap: Dp,
) {
    val config = state.appearance.theme.config
    val color = config[FrameThemeSpec.StatusColor]
    val brush = config.gradientOf(FrameThemeSpec.StatusColor)?.toBrush()
    val latest = state.notifications.latest

    AodStatusLine(
        battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
        isFlashlightOn = state.isFlashlightOn,
        notifications = state.notifications,
        color = color,
        glyphHeight = FrameMetrics.StatusGlyph,
        fontFamily = FrameThemeSpec.Font,
        gapScale = FrameMetrics.STATUS_GAP_SCALE,
        brush = brush,
    )

    AnimatedVisibility(
        visible = latest != null,
        enter = slideInVertically { height -> -height } + fadeIn(),
        exit = fadeOut(),
    ) {
        if (latest != null) {
            AodLatestLine(
                text = latest.lineText(R.string.aod_frame_notification),
                icon = latest.icon,
                color = color,
                brush = brush,
                style = FrameMetrics.LatestLine,
                modifier = Modifier.padding(top = latestGap),
            )
        }
    }
}

@Composable
private fun FrameExtras(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    AodPanelBlock(block = AodPanelBlockValue.EVENTS, state = state, onIntent = onIntent)

    AodPanelBlock(block = AodPanelBlockValue.MEMO, state = state, onIntent = onIntent)
}

@Composable
private fun FramePlayer(
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

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodFramePortraitPreview() {
    AodFramePortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.FRAME)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodFrameLandscapePreview() {
    AodFrameLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.FRAME)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
