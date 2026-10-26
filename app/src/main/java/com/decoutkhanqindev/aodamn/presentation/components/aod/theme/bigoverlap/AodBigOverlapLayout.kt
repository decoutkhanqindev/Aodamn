package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bigoverlap

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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap.BigOverlapThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodBigOverlapPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = BigOverlapMetrics.TopMinHeight)
                .padding(horizontal = BigOverlapMetrics.SideMargin),
            contentAlignment = Alignment.BottomStart,
        ) {
            BigOverlapInfo(state = state, dateSize = BigOverlapMetrics.DateSize, isWeatherAtEnd = true)
        }

        Spacer(modifier = Modifier.weight(BigOverlapMetrics.TOP_SPACE_WEIGHT))

        BigOverlapClock(
            state = state,
            modifier = Modifier
                .weight(BigOverlapMetrics.CLOCK_WEIGHT)
                .fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(BigOverlapMetrics.BOTTOM_SPACE_WEIGHT))

        BigOverlapPlayer(
            state = state,
            onIntent = onIntent,
            modifier = Modifier.padding(horizontal = BigOverlapMetrics.SideMargin),
        )

        BigOverlapBattery(
            state = state,
            glyphHeight = BigOverlapMetrics.BatteryGlyph,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(BigOverlapMetrics.BatteryPadding),
        )
    }
}

@Composable
fun AodBigOverlapLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scale = state.appearance.theme.config[BigOverlapThemeSpec.Size] / 100f
        val textSize = minOf(maxWidth, maxHeight) * BigOverlapMetrics.TEXT_SIZE * scale
        val blockHalf = textSize * BigOverlapMetrics.ZERO_WIDTH * (1f - BigOverlapMetrics.OVERLAP_X_FILLED)
        val side = (maxWidth / 2 - blockHalf - BigOverlapMetrics.LandscapeGap - BigOverlapMetrics.SideMargin).coerceAtLeast(0.dp)
        val hasMedia = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components

        BigOverlapClock(state = state, modifier = Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = BigOverlapMetrics.SideMargin)
                .width(side),
        ) {
            BigOverlapInfo(state = state, dateSize = BigOverlapMetrics.LandscapeDateSize, isWeatherAtEnd = false)
        }

        Crossfade(
            targetState = hasMedia,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = BigOverlapMetrics.SideMargin)
                .width(side),
            label = "BigOverlapLandscape",
        ) { isMediaShown ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (isMediaShown) {
                    BigOverlapPlayer(
                        state = state,
                        onIntent = onIntent,
                        modifier = Modifier.padding(bottom = BigOverlapMetrics.LandscapeItemGap),
                    )
                }

                BigOverlapBattery(state = state, glyphHeight = BigOverlapMetrics.LandscapeBatteryGlyph)
            }
        }
    }
}

@Composable
private fun BigOverlapClock(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    AodBigOverlapClock(
        nowMillis = state.nowMillis,
        config = state.appearance.theme.config,
        modifier = modifier,
    )
}

@Composable
private fun BigOverlapInfo(
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
                    BigOverlapDate(state = state, fontSize = dateSize)
                }

                if (isDateShown && isWeatherShown) {
                    if (isWeatherAtEnd) {
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        Spacer(modifier = Modifier.width(BigOverlapMetrics.WeatherGap))
                    }
                }

                BigOverlapWeather(state = state)
            }
        }

        AnimatedVisibility(visible = hasIcons) {
            AodStatusLine(
                battery = null,
                isFlashlightOn = false,
                notifications = state.notifications.copy(latest = null),
                color = state.appearance.theme.config[BigOverlapThemeSpec.NotificationsColor],
                glyphHeight = BigOverlapMetrics.IconRow,
                fontFamily = BigOverlapThemeSpec.Font,
                modifier = Modifier.padding(top = if (isDateShown || isWeatherShown) BigOverlapMetrics.DetailsGap else 0.dp),
                gapScale = BigOverlapMetrics.ICON_GAP_SCALE,
                brush = state.appearance.theme.config.gradientOf(BigOverlapThemeSpec.NotificationsColor)?.toBrush(),
            )
        }

        AnimatedVisibility(
            visible = latest != null,
            enter = slideInVertically { height -> -height } + fadeIn(),
            exit = fadeOut(),
        ) {
            if (latest != null) {
                val gap = when {
                    hasIcons -> BigOverlapMetrics.LatestGap
                    isDateShown || isWeatherShown -> BigOverlapMetrics.DetailsGap
                    else -> 0.dp
                }

                BigOverlapLatest(latest = latest, state = state, modifier = Modifier.padding(top = gap))
            }
        }
    }
}

@Composable
private fun BigOverlapDate(
    state: AodState,
    fontSize: TextUnit,
) {
    val time = rememberZonedDateTime(state.nowMillis)

    Text(
        text = time.format(rememberFormatter(R.string.aod_date_pattern)),
        style = TextStyle(
            brush = state.appearance.theme.config.brushOf(BigOverlapThemeSpec.DateColor),
            fontSize = fontSize,
            fontFamily = BigOverlapThemeSpec.Font,
        ),
        maxLines = 1,
    )
}

@Composable
private fun BigOverlapWeather(state: AodState) {
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
private fun BigOverlapLatest(
    latest: NotificationContentUiModel,
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val brush = config.brushOf(BigOverlapThemeSpec.NotificationsColor)
    val dimBrush = config.dimBrushOf(BigOverlapThemeSpec.NotificationsColor)
    val text = if (latest.isHidden) stringResource(R.string.aod_notification_content_hidden) else latest.text

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            bitmap = remember(latest.icon) { latest.icon.asImageBitmap() },
            contentDescription = null,
            modifier = Modifier
                .size(BigOverlapMetrics.LatestIcon)
                .aodTint(config.gradientOf(BigOverlapThemeSpec.NotificationsColor)?.toBrush()),
            colorFilter = ColorFilter.tint(config[BigOverlapThemeSpec.NotificationsColor]),
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
                .padding(start = BigOverlapMetrics.LatestIconGap)
                .basicMarquee(iterations = 1),
            style = TextStyle(fontSize = BigOverlapMetrics.LatestSize, fontFamily = BigOverlapThemeSpec.Font),
            maxLines = 1,
        )
    }
}

@Composable
private fun BigOverlapBattery(
    state: AodState,
    glyphHeight: Dp,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val alpha by animateFloatAsState(
        targetValue = if (battery?.isCharging == true) 1f else BigOverlapMetrics.IDLE_BATTERY_ALPHA,
        label = "BigOverlapBatteryAlpha",
    )

    Box(
        modifier = modifier.heightIn(min = glyphHeight),
        contentAlignment = Alignment.Center,
    ) {
        AodStatusLine(
            battery = battery,
            isFlashlightOn = state.isFlashlightOn,
            notifications = AodNotificationsUiModel(),
            color = config[BigOverlapThemeSpec.BatteryColor],
            glyphHeight = glyphHeight,
            fontFamily = BigOverlapThemeSpec.Font,
            modifier = Modifier.graphicsLayer { this.alpha = alpha },
            brush = config.gradientOf(BigOverlapThemeSpec.BatteryColor)?.toBrush(),
        )
    }
}

@Composable
private fun BigOverlapPlayer(
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
private fun AodBigOverlapPortraitPreview() {
    AodBigOverlapPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.BIG_OVERLAP)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodBigOverlapLandscapePreview() {
    AodBigOverlapLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.BIG_OVERLAP)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
