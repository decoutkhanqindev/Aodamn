package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gentle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodLatestLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.lineText
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gentle.GentleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodGentlePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val config = state.appearance.theme.config
        val em = rememberGentleEm(config[GentleThemeSpec.Size], maxHeight - (GentleMetrics.PortraitBottomReserve + GentleMetrics.ClockGap) * 2)

        AodGentleClock(
            nowMillis = state.nowMillis,
            config = config,
            em = em,
            modifier = Modifier.align(Alignment.Center),
        )

        GentleDetails(
            state = state,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(start = GentleMetrics.SideMargin, top = GentleMetrics.TopMargin, end = GentleMetrics.SideMargin),
        )

        GentleAction(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(start = GentleMetrics.SideMargin, end = GentleMetrics.SideMargin, bottom = GentleMetrics.BottomMargin),
        )
    }
}

@Composable
fun AodGentleLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val config = state.appearance.theme.config
        val em = rememberGentleEm(config[GentleThemeSpec.Size], maxHeight - GentleMetrics.LandscapeClockMargin * 2)
        val edge = maxWidth * GentleMetrics.LANDSCAPE_EDGE
        val side = (maxWidth / 2 - em * GentleMetrics.DIGITS_WIDTH / 2 - GentleMetrics.LandscapeGap - edge).coerceAtLeast(0.dp)

        AodGentleClock(
            nowMillis = state.nowMillis,
            config = config,
            em = em,
            modifier = Modifier.align(Alignment.Center),
        )

        GentleDetails(
            state = state,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = edge)
                .width(side),
        )

        GentleAction(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = edge)
                .width(side),
        )
    }
}

@Composable
private fun GentleDetails(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val isDateEnabled = state.extras.isDateEnabled
    val isGentleWeatherShown = state.isGentleWeatherShown()
    val latest = state.shownLatest

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedVisibility(visible = state.hasGentleDateRow()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedVisibility(visible = isDateEnabled) {
                    GentleDate(state = state)
                }

                AnimatedVisibility(visible = isDateEnabled && isGentleWeatherShown) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = GentleMetrics.SeparatorGap)
                            .size(GentleMetrics.SeparatorDot)
                            .aodTint(config.gradientOf(GentleThemeSpec.DateColor)?.toBrush())
                            .background(color = config[GentleThemeSpec.DateColor], shape = CircleShape),
                    )
                }

                AnimatedVisibility(visible = isGentleWeatherShown) {
                    AodWeather(
                        weather = state.weather,
                        style = state.appearance.components.styleOf(AodComponentValue.WEATHER),
                        layout = state.appearance.components.weatherLayout,
                    )
                }
            }
        }

        AodStatusLine(
            battery = null,
            isFlashlightOn = false,
            notifications = state.notifications.copy(latest = null),
            color = config[GentleThemeSpec.StatusColor],
            glyphHeight = GentleMetrics.IconRow,
            fontFamily = GentleThemeSpec.Font,
            modifier = Modifier.padding(top = if (state.hasGentleDateRow()) GentleMetrics.RowGap else 0.dp),
            gapScale = GentleMetrics.ICON_GAP_SCALE,
            brush = config.gradientOf(GentleThemeSpec.StatusColor)?.toBrush(),
        )

        AnimatedVisibility(
            visible = latest != null,
            enter = slideInVertically { -it } + fadeIn(),
            exit = fadeOut(),
        ) {
            if (latest != null) {
                AodLatestLine(
                    text = latest.lineText(R.string.aod_gentle_notification),
                    icon = latest.icon,
                    color = config[GentleThemeSpec.StatusColor],
                    brush = config.gradientOf(GentleThemeSpec.StatusColor)?.toBrush(),
                    style = GentleMetrics.LatestLine,
                    modifier = Modifier.padding(
                        top = if (state.hasGentleDateRow() || state.notifications.icons.isNotEmpty()) GentleMetrics.LatestGap else 0.dp,
                    ),
                )
            }
        }
    }
}

@Composable
private fun GentleDate(state: AodState) {
    val config = state.appearance.theme.config
    val locale = LocalConfiguration.current.locales[0]

    Text(
        text = rememberZonedDateTime(state.nowMillis).format(rememberFormatter(R.string.aod_gentle_date_pattern)).uppercase(locale),
        style = TextStyle(
            brush = config.brushOf(GentleThemeSpec.DateColor),
            fontSize = GentleMetrics.DateSize,
            fontWeight = FontWeight.Medium,
            fontFamily = GentleThemeSpec.Font,
            letterSpacing = GentleMetrics.DateSpacing,
        ),
        maxLines = 1,
    )
}

@Composable
private fun GentleAction(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedVisibility(visible = state.isGentleMediaShown()) {
            Box(
                modifier = Modifier
                    .widthIn(max = GentleMetrics.PlayerMaxWidth)
                    .padding(bottom = if (state.isGentleBatteryShown()) GentleMetrics.BatteryGap else 0.dp),
            ) {
                AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
            }
        }

        AodStatusLine(
            battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
            isFlashlightOn = state.isFlashlightOn,
            notifications = AodNotificationsUiModel(),
            color = config[GentleThemeSpec.StatusColor],
            glyphHeight = GentleMetrics.BatteryGlyph,
            fontFamily = GentleThemeSpec.Font,
            brush = config.gradientOf(GentleThemeSpec.StatusColor)?.toBrush(),
        )
    }
}

private fun AodState.isGentleWeatherShown(): Boolean =
    weather != null && AodComponentValue.WEATHER in appearance.theme.value.spec.components

private fun AodState.isGentleMediaShown(): Boolean =
    media != null && AodComponentValue.MEDIA in appearance.theme.value.spec.components

private fun AodState.isGentleBatteryShown(): Boolean =
    (battery != null && extras.isBatteryEnabled) || isFlashlightOn

private fun AodState.hasGentleDateRow(): Boolean = extras.isDateEnabled || isGentleWeatherShown()

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodGentlePortraitPreview() {
    AodGentlePortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.GENTLE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodGentleLandscapePreview() {
    AodGentleLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.GENTLE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
