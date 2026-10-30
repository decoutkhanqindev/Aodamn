package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.halfmoon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
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
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.halfmoon.HalfMoonThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlinx.collections.immutable.toImmutableList

@Composable
fun AodHalfMoonPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val paints = rememberHalfMoonPaints()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val half = maxWidth / 2
        val zone = (maxHeight / 2 - minOf(maxWidth, maxHeight) * HalfMoonMetrics.PORTRAIT_RADIUS).coerceAtLeast(0.dp)
        val playerWidth = maxWidth - HalfMoonMetrics.PlayerMargin * 2

        HalfMoonDial(state = state, radiusFraction = HalfMoonMetrics.PORTRAIT_RADIUS, paints = paints)

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(half)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            HalfMoonColumn(
                state = state,
                dateSize = HalfMoonMetrics.PortraitDateSize,
                daySize = HalfMoonMetrics.PortraitDaySize,
                maxWidth = half - HalfMoonMetrics.ColumnInset,
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(zone),
            contentAlignment = Alignment.Center,
        ) {
            HalfMoonPlayer(state = state, width = playerWidth, onIntent = onIntent)
        }
    }
}

@Composable
fun AodHalfMoonLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val paints = rememberHalfMoonPaints()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val radius = minOf(maxWidth, maxHeight) * HalfMoonMetrics.LANDSCAPE_RADIUS
        val area = (maxWidth - radius * (2f - HalfMoonMetrics.CENTER_SHIFT)).coerceAtLeast(0.dp)
        val inner = (area - HalfMoonMetrics.ColumnInset * 2).coerceAtLeast(0.dp)

        HalfMoonDial(state = state, radiusFraction = HalfMoonMetrics.LANDSCAPE_RADIUS, paints = paints)

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(area)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HalfMoonColumn(
                    state = state,
                    dateSize = HalfMoonMetrics.LandscapeDateSize,
                    daySize = HalfMoonMetrics.LandscapeDaySize,
                    maxWidth = minOf(HalfMoonMetrics.LandscapeColumnWidth, inner),
                )

                HalfMoonPlayer(
                    state = state,
                    width = minOf(HalfMoonMetrics.LandscapePlayerWidth, inner),
                    onIntent = onIntent,
                    modifier = Modifier.padding(top = if (state.hasHalfMoonColumn()) HalfMoonMetrics.LandscapePlayerGap else 0.dp),
                )
            }
        }
    }
}

@Composable
private fun HalfMoonDial(
    state: AodState,
    radiusFraction: Float,
    paints: HalfMoonPaints,
) {
    AodHalfMoonDial(
        nowMillis = state.nowMillis,
        isLive = state.isClockLive,
        radiusFraction = radiusFraction,
        config = state.appearance.theme.config,
        paints = paints,
    )
}

@Composable
private fun HalfMoonColumn(
    state: AodState,
    dateSize: TextUnit,
    daySize: TextUnit,
    maxWidth: Dp,
) {
    val config = state.appearance.theme.config
    val components = state.appearance.components
    val isDateShown = state.extras.isDateEnabled
    val isWeatherShown = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components
    val statusColor = config[HalfMoonThemeSpec.StatusColor]
    val statusBrush = config.gradientOf(HalfMoonThemeSpec.StatusColor)?.toBrush()
    val latest = state.notifications.latest

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedVisibility(visible = isDateShown) {
            HalfMoonDate(
                nowMillis = state.nowMillis,
                color = config[HalfMoonThemeSpec.DateColor],
                gradient = config.gradientOf(HalfMoonThemeSpec.DateColor),
                dateSize = dateSize,
                daySize = daySize,
            )
        }

        AnimatedVisibility(
            visible = isWeatherShown,
            modifier = Modifier.padding(top = if (isDateShown) HalfMoonMetrics.WeatherGap else 0.dp),
        ) {
            AodWeather(
                weather = state.weather,
                style = components.styleOf(AodComponentValue.WEATHER),
                layout = components.weatherLayout,
            )
        }

        AodStatusLine(
            battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
            isFlashlightOn = state.isFlashlightOn,
            notifications = state.notifications,
            color = statusColor,
            glyphHeight = HalfMoonMetrics.StatusGlyph,
            brush = statusBrush,
            fontFamily = HalfMoonThemeSpec.Font,
            modifier = Modifier.padding(top = if (isDateShown || isWeatherShown) HalfMoonMetrics.StatusGap else 0.dp),
        )

        AnimatedVisibility(visible = latest != null) {
            if (latest != null) {
                AodLatestLine(
                    text = latest.lineText(R.string.aod_half_moon_notification),
                    icon = latest.icon,
                    color = statusColor,
                    brush = statusBrush,
                    style = HalfMoonMetrics.LatestLine,
                    modifier = Modifier
                        .padding(top = HalfMoonMetrics.LatestGap)
                        .widthIn(max = maxWidth),
                )
            }
        }
    }
}

@Composable
private fun HalfMoonDate(
    nowMillis: Long,
    color: Color,
    gradient: AodGradient?,
    dateSize: TextUnit,
    daySize: TextUnit,
) {
    val time = rememberZonedDateTime(nowMillis)
    val date = time.format(rememberFormatter(R.string.aod_half_moon_date_pattern)).uppercase()
    val day = time.format(rememberFormatter(R.string.aod_half_moon_day_pattern)).uppercase()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HalfMoonDateText(
            text = date,
            brush = gradient?.let {
                gradient.copy(colors = gradient.colors.map { stop -> lerp(stop, Black, HalfMoonMetrics.DATE_DARKEN) }.toImmutableList())
                    .toBrush()
            } ?: SolidColor(lerp(color, Black, HalfMoonMetrics.DATE_DARKEN)),
            fontSize = dateSize,
        )

        HalfMoonDateText(
            text = day,
            brush = gradient?.toBrush() ?: SolidColor(color),
            fontSize = daySize,
            modifier = Modifier.padding(top = HalfMoonMetrics.DayGap),
        )
    }
}

@Composable
private fun HalfMoonDateText(
    text: String,
    brush: Brush,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = LocalTextStyle.current.merge(TextStyle(brush = brush)),
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        fontFamily = HalfMoonThemeSpec.Font,
        letterSpacing = HalfMoonMetrics.DateSpacing,
        maxLines = 1,
        softWrap = false,
    )
}

@Composable
private fun HalfMoonPlayer(
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

private fun AodState.hasHalfMoonColumn(): Boolean =
    extras.isDateEnabled ||
        (weather != null && AodComponentValue.WEATHER in appearance.theme.value.spec.components) ||
        (battery != null && extras.isBatteryEnabled) ||
        isFlashlightOn ||
        notifications.icons.isNotEmpty() ||
        notifications.latest != null

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodHalfMoonPortraitPreview() {
    AodHalfMoonPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.HALF_MOON)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodHalfMoonLandscapePreview() {
    AodHalfMoonLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.HALF_MOON)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
