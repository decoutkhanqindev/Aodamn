package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.quarters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.quarters.QuartersThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodQuartersPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = QuartersMetrics.TopMinHeight)
                .padding(horizontal = QuartersMetrics.SideMargin),
            contentAlignment = Alignment.BottomStart,
        ) {
            QuartersInfo(state = state, dateSize = QuartersMetrics.DateSize, isWeatherAtEnd = true)
        }

        Spacer(modifier = Modifier.weight(QuartersMetrics.TOP_SPACE_WEIGHT))

        QuartersDial(
            state = state,
            modifier = Modifier
                .weight(QuartersMetrics.CLOCK_WEIGHT)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(QuartersMetrics.BOTTOM_SPACE_WEIGHT))

        QuartersPlayer(
            state = state,
            onIntent = onIntent,
            modifier = Modifier.padding(horizontal = QuartersMetrics.SideMargin),
        )

        QuartersBattery(
            state = state,
            glyphHeight = QuartersMetrics.BatteryGlyph,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(QuartersMetrics.BatteryPadding),
        )
    }
}

@Composable
fun AodQuartersLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val radius = minOf(maxWidth, maxHeight) / 2 * (state.appearance.theme.config[QuartersThemeSpec.Size] / 100f)
        val side = (maxWidth / 2 - radius - QuartersMetrics.LandscapeGap - QuartersMetrics.SideMargin).coerceAtLeast(0.dp)
        val hasMedia = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components

        QuartersDial(state = state, modifier = Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = QuartersMetrics.SideMargin)
                .width(side),
        ) {
            QuartersInfo(state = state, dateSize = QuartersMetrics.LandscapeDateSize, isWeatherAtEnd = false)
        }

        Crossfade(
            targetState = hasMedia,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = QuartersMetrics.SideMargin)
                .width(side),
            label = "QuartersLandscape",
        ) { isMediaShown ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (isMediaShown) {
                    QuartersPlayer(
                        state = state,
                        onIntent = onIntent,
                        modifier = Modifier.padding(bottom = QuartersMetrics.LandscapeItemGap),
                    )
                }

                QuartersBattery(state = state, glyphHeight = QuartersMetrics.LandscapeBatteryGlyph)
            }
        }
    }
}

@Composable
private fun QuartersDial(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    AodQuartersDial(
        nowMillis = state.nowMillis,
        isLive = state.isClockLive,
        config = state.appearance.theme.config,
        modifier = modifier,
    )
}

@Composable
private fun QuartersInfo(
    state: AodState,
    dateSize: TextUnit,
    isWeatherAtEnd: Boolean,
) {
    val isDateShown = state.extras.isDateEnabled
    val isWeatherShown = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components
    val hasIcons = state.notifications.icons.isNotEmpty()
    val latest = state.notifications.latest

    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(visible = isDateShown || isWeatherShown) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(visible = isDateShown) {
                    QuartersDate(state = state, fontSize = dateSize)
                }

                if (isDateShown && isWeatherShown) {
                    if (isWeatherAtEnd) {
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        Spacer(modifier = Modifier.width(QuartersMetrics.WeatherGap))
                    }
                }

                QuartersWeather(state = state)
            }
        }

        AnimatedVisibility(visible = hasIcons) {
            AodStatusLine(
                battery = null,
                isFlashlightOn = false,
                notifications = state.notifications.copy(latest = null),
                color = state.appearance.theme.config[QuartersThemeSpec.NotificationsColor],
                glyphHeight = QuartersMetrics.IconRow,
                fontFamily = QuartersThemeSpec.Font,
                modifier = Modifier.padding(top = if (isDateShown || isWeatherShown) QuartersMetrics.DetailsGap else 0.dp),
                gapScale = QuartersMetrics.ICON_GAP_SCALE,
                brush = state.appearance.theme.config.gradientOf(QuartersThemeSpec.NotificationsColor)?.toBrush(),
            )
        }

        AnimatedVisibility(
            visible = latest != null,
            enter = slideInVertically { height -> -height } + fadeIn(),
            exit = fadeOut(),
        ) {
            if (latest != null) {
                val gap = when {
                    hasIcons -> QuartersMetrics.LatestGap
                    isDateShown || isWeatherShown -> QuartersMetrics.DetailsGap
                    else -> 0.dp
                }

                QuartersLatest(latest = latest, state = state, modifier = Modifier.padding(top = gap))
            }
        }
    }
}

@Composable
private fun QuartersDate(
    state: AodState,
    fontSize: TextUnit,
) {
    val time = rememberZonedDateTime(state.nowMillis)

    Text(
        text = time.format(rememberFormatter(R.string.aod_date_pattern)),
        style = TextStyle(
            brush = state.appearance.theme.config.brushOf(QuartersThemeSpec.DateColor),
            fontSize = fontSize,
            fontFamily = QuartersThemeSpec.Font,
        ),
        maxLines = 1,
    )
}

@Composable
private fun QuartersWeather(state: AodState) {
    val components = state.appearance.components

    AnimatedVisibility(visible = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components) {
        AodWeather(
            weather = state.weather,
            style = components.styleOf(AodComponentValue.WEATHER),
            layout = components.weatherLayout,
        )
    }
}

@Composable
private fun QuartersLatest(
    latest: NotificationContentUiModel,
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val brush = config.brushOf(QuartersThemeSpec.NotificationsColor)
    val dimBrush = config.quartersDimBrushOf(QuartersThemeSpec.NotificationsColor)
    val text = if (latest.isHidden) stringResource(R.string.aod_notification_content_hidden) else latest.text

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            bitmap = remember(latest.icon) { latest.icon.asImageBitmap() },
            contentDescription = null,
            modifier = Modifier
                .size(QuartersMetrics.LatestIcon)
                .aodTint(config.gradientOf(QuartersThemeSpec.NotificationsColor)?.toBrush()),
            colorFilter = ColorFilter.tint(config[QuartersThemeSpec.NotificationsColor]),
        )

        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(brush = brush)) { append(latest.title) }
                if (text.isNotEmpty()) {
                    append("  ")
                    withStyle(SpanStyle(brush = dimBrush)) { append(text) }
                }
            },
            modifier = Modifier
                .padding(start = QuartersMetrics.LatestIconGap)
                .basicMarquee(iterations = 1),
            style = TextStyle(fontSize = QuartersMetrics.LatestSize, fontFamily = QuartersThemeSpec.Font),
            maxLines = 1,
        )
    }
}

@Composable
private fun QuartersBattery(
    state: AodState,
    glyphHeight: Dp,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val alpha by animateFloatAsState(
        targetValue = if (battery?.isCharging == true) 1f else QuartersMetrics.IDLE_BATTERY_ALPHA,
        label = "QuartersBatteryAlpha",
    )

    Box(
        modifier = modifier.heightIn(min = glyphHeight),
        contentAlignment = Alignment.Center,
    ) {
        AodStatusLine(
            battery = battery,
            isFlashlightOn = state.isFlashlightOn,
            notifications = AodNotificationsUiModel(),
            color = config[QuartersThemeSpec.BatteryColor],
            glyphHeight = glyphHeight,
            fontFamily = QuartersThemeSpec.Font,
            modifier = Modifier.graphicsLayer { this.alpha = alpha },
            brush = config.gradientOf(QuartersThemeSpec.BatteryColor)?.toBrush(),
        )
    }
}

@Composable
private fun QuartersPlayer(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components,
        modifier = modifier,
    ) {
        AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodQuartersPortraitPreview() {
    AodQuartersPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.QUARTERS)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodQuartersLandscapePreview() {
    AodQuartersLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.QUARTERS)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
