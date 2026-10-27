package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.sector

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.sector.SectorThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.BlackAlpha20
import com.decoutkhanqindev.aodamn.presentation.theme.BlackAlpha50
import com.decoutkhanqindev.aodamn.presentation.theme.Mint

@Composable
fun AodSectorPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val epochMillis = rememberSectorMillis(nowMillis = state.nowMillis, isLive = state.isClockLive)

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen },
        ) {
            AodSectorFace(epochMillis = epochMillis, config = state.appearance.theme.config, modifier = Modifier.fillMaxSize())

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .sectorFade(isVertical = true, isReversed = false)
                    .padding(vertical = SectorMetrics.BlockPadding, horizontal = SectorMetrics.SidePadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SectorRow(state = state, isWrapped = false, modifier = Modifier.padding(SectorMetrics.RowPadding))

                SectorLatest(state = state)
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .sectorFade(isVertical = true, isReversed = true),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SectorPlayer(state = state, onIntent = onIntent, modifier = Modifier.padding(SectorMetrics.PlayerMargin))

                SectorBattery(state = state, modifier = Modifier.padding(bottom = SectorMetrics.BatteryBottom))
            }
        }

        AodSectorHands(
            nowMillis = state.nowMillis,
            epochMillis = epochMillis,
            config = state.appearance.theme.config,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun AodSectorLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val epochMillis = rememberSectorMillis(nowMillis = state.nowMillis, isLive = state.isClockLive)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val reach = minOf(maxWidth, maxHeight) / 2 * (state.appearance.theme.config[SectorThemeSpec.Size] / 100f) * SectorMetrics.MINUTE_END
        val zone = (maxWidth / 2 - reach).coerceAtLeast(0.dp)
        val hasMedia = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen },
        ) {
            AodSectorFace(epochMillis = epochMillis, config = state.appearance.theme.config, modifier = Modifier.fillMaxSize())

            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(zone)
                    .fillMaxHeight()
                    .sectorFade(isVertical = false, isReversed = false)
                    .padding(start = SectorMetrics.LandscapeMargin, end = SectorMetrics.LandscapeGap),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SectorRow(state = state, isWrapped = true)

                    SectorLatest(state = state)
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(zone)
                    .fillMaxHeight()
                    .sectorFade(isVertical = false, isReversed = true)
                    .padding(start = SectorMetrics.LandscapeGap, end = SectorMetrics.LandscapeMargin),
                contentAlignment = Alignment.Center,
            ) {
                Crossfade(targetState = hasMedia, label = "SectorLandscape") { isMediaShown ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (isMediaShown) {
                            SectorPlayer(
                                state = state,
                                onIntent = onIntent,
                                modifier = Modifier.padding(bottom = SectorMetrics.LandscapePlayerGap),
                            )
                        }

                        SectorBattery(state = state)
                    }
                }
            }
        }

        AodSectorHands(
            nowMillis = state.nowMillis,
            epochMillis = epochMillis,
            config = state.appearance.theme.config,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun SectorRow(
    state: AodState,
    isWrapped: Boolean,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val isDateShown = state.extras.isDateEnabled
    val isWeatherShown = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components
    val hasIcons = state.notifications.icons.isNotEmpty()

    val icons = @Composable {
        AodStatusLine(
            battery = null,
            isFlashlightOn = false,
            notifications = state.notifications.copy(latest = null),
            color = config[SectorThemeSpec.NotificationsColor],
            glyphHeight = SectorMetrics.IconRow,
            fontFamily = SectorThemeSpec.Font,
            gapScale = SectorMetrics.ICON_GAP_SCALE,
            brush = config.gradientOf(SectorThemeSpec.NotificationsColor)?.toBrush(),
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedVisibility(visible = isDateShown) {
                SectorDate(state = state)
            }

            AnimatedVisibility(visible = isWeatherShown) {
                Box(modifier = Modifier.padding(start = if (isDateShown) SectorMetrics.WeatherGap else 0.dp)) {
                    AodWeather(
                        weather = state.weather,
                        style = state.appearance.components.styleOf(AodComponentValue.WEATHER),
                        layout = state.appearance.components.weatherLayout,
                    )
                }
            }

            if (!isWrapped) {
                AnimatedVisibility(visible = hasIcons && (isDateShown || isWeatherShown)) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = SectorMetrics.SeparatorGap)
                            .size(width = SectorMetrics.SeparatorWidth, height = SectorMetrics.SeparatorHeight)
                            .background(brush = config.brushOf(SectorThemeSpec.NotificationsColor)),
                    )
                }

                icons()
            }
        }

        if (isWrapped) {
            AnimatedVisibility(visible = hasIcons) {
                Box(modifier = Modifier.padding(top = if (isDateShown || isWeatherShown) SectorMetrics.LandscapeItemGap else 0.dp)) {
                    icons()
                }
            }
        }
    }
}

@Composable
private fun SectorDate(state: AodState) {
    val time = rememberZonedDateTime(state.nowMillis)

    Text(
        text = time.format(rememberFormatter(R.string.aod_sector_date_pattern)),
        style = TextStyle(
            brush = state.appearance.theme.config.brushOf(SectorThemeSpec.DateColor),
            fontSize = SectorMetrics.DateSize,
            fontFamily = SectorThemeSpec.Font,
        ),
        maxLines = 1,
    )
}

@Composable
private fun SectorLatest(state: AodState) {
    val latest = state.notifications.latest
    val isWeatherShown = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components
    val hasTopGap = state.extras.isDateEnabled || isWeatherShown || state.notifications.icons.isNotEmpty()

    AnimatedVisibility(
        visible = latest != null,
        enter = slideInVertically { height -> -height } + fadeIn(),
        exit = fadeOut(),
    ) {
        if (latest != null) {
            SectorLatestCard(
                latest = latest,
                state = state,
                modifier = Modifier.padding(top = if (hasTopGap) SectorMetrics.LatestGap else 0.dp),
            )
        }
    }
}

@Composable
private fun SectorLatestCard(
    latest: NotificationContentUiModel,
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val brush = config.brushOf(SectorThemeSpec.NotificationsColor)
    val text = if (latest.isHidden) stringResource(R.string.aod_notification_content_hidden) else latest.text

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(SectorMetrics.LatestBadge)
                .background(brush = brush, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                bitmap = remember(latest.icon) { latest.icon.asImageBitmap() },
                contentDescription = null,
                modifier = Modifier.size(SectorMetrics.LatestIcon),
                colorFilter = ColorFilter.tint(Black),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(start = SectorMetrics.LatestTextGap),
        ) {
            Text(
                text = latest.title,
                modifier = Modifier.basicMarquee(iterations = 1),
                style = TextStyle(brush = brush, fontSize = SectorMetrics.TitleSize, fontFamily = SectorThemeSpec.Font),
                maxLines = 1,
            )

            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    modifier = Modifier.padding(top = SectorMetrics.TextGap),
                    style = TextStyle(
                        brush = config.darkBrushOf(SectorThemeSpec.NotificationsColor, SectorMetrics.TEXT_DARKEN),
                        fontSize = SectorMetrics.TextSize,
                        fontFamily = SectorThemeSpec.Font,
                    ),
                    maxLines = SectorMetrics.LATEST_MAX_LINES,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun SectorBattery(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val brush = config.brushOf(SectorThemeSpec.BatteryColor)
    val alpha by animateFloatAsState(
        targetValue = if (battery?.isCharging == true) 1f else SectorMetrics.IDLE_BATTERY_ALPHA,
        label = "SectorBatteryAlpha",
    )

    Row(
        modifier = modifier.graphicsLayer { this.alpha = alpha },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(visible = battery != null) {
            if (battery != null) {
                val textRes = when {
                    battery.isCharging && battery.percent >= 100 -> R.string.aod_sector_battery_full
                    battery.isCharging -> R.string.aod_sector_battery_charging
                    else -> R.string.aod_battery
                }

                Text(
                    text = stringResource(textRes, battery.percent),
                    style = TextStyle(brush = brush, fontSize = SectorMetrics.BatterySize, fontFamily = SectorThemeSpec.Font),
                    maxLines = 1,
                )
            }
        }

        AnimatedVisibility(visible = state.isFlashlightOn) {
            Icon(
                painter = painterResource(R.drawable.ic_flashlight_light),
                contentDescription = stringResource(R.string.aod_flashlight_on),
                modifier = Modifier
                    .padding(start = if (battery != null) SectorMetrics.FlashlightGap else 0.dp)
                    .size(SectorMetrics.FlashlightIcon),
                tint = Mint,
            )
        }
    }
}

@Composable
private fun SectorPlayer(
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

private fun Modifier.sectorFade(isVertical: Boolean, isReversed: Boolean): Modifier = drawWithCache {
    val stops = arrayOf(0f to BlackAlpha50, SectorMetrics.FADE_MIDDLE to BlackAlpha20, 1f to Color.Transparent)
    val length = if (isVertical) size.height else size.width
    val start = if (isReversed) length else 0f
    val end = if (isReversed) 0f else length
    val brush = if (isVertical) {
        Brush.verticalGradient(*stops, startY = start, endY = end)
    } else {
        Brush.horizontalGradient(*stops, startX = start, endX = end)
    }

    onDrawBehind { drawRect(brush = brush, blendMode = BlendMode.DstOut) }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodSectorPortraitPreview() {
    AodSectorPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.SECTOR)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodSectorLandscapePreview() {
    AodSectorLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.SECTOR)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
