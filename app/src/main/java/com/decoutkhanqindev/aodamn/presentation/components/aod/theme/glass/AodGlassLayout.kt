package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.glass

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodLatestLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.lineText
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.glass.GlassThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyInter
import com.decoutkhanqindev.aodamn.presentation.theme.GlassCard
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape14dp
import kotlin.math.roundToInt

@Composable
fun AodGlassPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    GlassFace(state = state, onIntent = onIntent, isLandscape = false)
}

@Composable
fun AodGlassLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    GlassFace(state = state, onIntent = onIntent, isLandscape = true)
}

@Composable
private fun GlassFace(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    isLandscape: Boolean,
) {
    val latest = state.notifications.latest
    val hasMedia = state.media != null
    val showsMedia = hasMedia && ((!state.isPreview && state.isMediaShown) || state.isPreviewShown(AodComponentValue.MEDIA))
    val showsLatest = (!state.isPreview && state.isLatestShown) || state.isPreviewFocused(AodComponentValue.NOTIFICATIONS)
    val showsNotification = state.notifications.icons.isNotEmpty() || (showsLatest && latest != null)
    val showsBattery = state.battery != null && state.extras.isBatteryEnabled
    val hasDateRow = state.extras.isDateEnabled || state.weather != null
    val hasEvents = state.events.isNotEmpty()
    val toggleLatest = {
        if (latest != null || state.isLatestShown) {
            onIntent(AodIntent.ToggleLatestNotification(isShown = !state.isLatestShown))
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scale = GlassMetrics.scale(minOf(maxWidth, maxHeight).value)
        val maxCardWidth = maxWidth - GlassMetrics.CardMargin * 2
        val slidePx = with(LocalDensity.current) { (GlassMetrics.SwapSlide * scale).roundToPx() }

        if (isLandscape) {
            GlassLandscape(
                state = state,
                onIntent = onIntent,
                scale = scale,
                maxCardWidth = maxCardWidth,
                slidePx = slidePx,
                showsLatest = showsLatest,
                showsNotification = showsNotification,
                showsMedia = showsMedia,
                showsBattery = showsBattery,
                hasDateRow = hasDateRow,
                hasEvents = hasEvents,
                onToggleLatest = toggleLatest,
            )
        } else {
            GlassPortrait(
                state = state,
                onIntent = onIntent,
                scale = scale,
                maxCardWidth = maxCardWidth,
                slidePx = slidePx,
                showsLatest = showsLatest,
                showsNotification = showsNotification,
                showsMedia = showsMedia,
                showsBattery = showsBattery,
                hasDateRow = hasDateRow,
                hasEvents = hasEvents,
                onToggleLatest = toggleLatest,
            )
        }
    }
}

@Composable
private fun GlassPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    scale: Float,
    maxCardWidth: Dp,
    slidePx: Int,
    showsLatest: Boolean,
    showsNotification: Boolean,
    showsMedia: Boolean,
    showsBattery: Boolean,
    hasDateRow: Boolean,
    hasEvents: Boolean,
    onToggleLatest: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .fillMaxHeight(GlassMetrics.PORTRAIT_TOP),
            contentAlignment = Alignment.Center,
        ) {
            GlassClockCluster(
                state = state,
                onIntent = onIntent,
                scale = scale,
                hasDateRow = hasDateRow,
                hasEvents = hasEvents,
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(
                    start = GlassMetrics.CardMargin * scale,
                    end = GlassMetrics.CardMargin * scale,
                    bottom = GlassMetrics.BatteryBottom * scale,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(visible = showsNotification, enter = fadeIn(tween(GlassMetrics.FADE_MILLIS)), exit = fadeOut(tween(GlassMetrics.FADE_MILLIS))) {
                GlassNotificationCard(
                    state = state,
                    showsLatest = showsLatest,
                    scale = scale,
                    maxWidth = maxCardWidth,
                    onTap = onToggleLatest,
                )
            }

            AnimatedVisibility(
                visible = showsMedia,
                enter = slideInVertically(tween(GlassMetrics.SWAP_MILLIS)) { slidePx } + fadeIn(tween(GlassMetrics.FADE_MILLIS)),
                exit = slideOutVertically(tween(GlassMetrics.SWAP_MILLIS)) { slidePx } + fadeOut(tween(GlassMetrics.FADE_MILLIS)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = if (showsNotification) GlassMetrics.CardGap * scale else 0.dp),
                ) {
                    AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
                }
            }

            AnimatedVisibility(visible = showsBattery, enter = fadeIn(tween(GlassMetrics.FADE_MILLIS)), exit = fadeOut(tween(GlassMetrics.FADE_MILLIS))) {
                Box(modifier = Modifier.padding(top = if (showsMedia || showsNotification) GlassMetrics.MediaBatteryGap * scale else 0.dp)) {
                    GlassBattery(state = state, scale = scale)
                }
            }
        }
    }
}

@Composable
private fun GlassLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    scale: Float,
    maxCardWidth: Dp,
    slidePx: Int,
    showsLatest: Boolean,
    showsNotification: Boolean,
    showsMedia: Boolean,
    showsBattery: Boolean,
    hasDateRow: Boolean,
    hasEvents: Boolean,
    onToggleLatest: () -> Unit,
) {
    val hasRight = showsNotification || showsMedia
    val leftFraction by animateFloatAsState(
        targetValue = if (hasRight) GlassMetrics.LANDSCAPE_LEFT else 1f,
        animationSpec = tween(GlassMetrics.SWAP_MILLIS),
        label = "GlassLandscapeLeft",
    )

    Row(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .glassLandscapeLeft { leftFraction }
                .fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                GlassClockCluster(
                    state = state,
                    onIntent = onIntent,
                    scale = scale,
                    hasDateRow = hasDateRow,
                    hasEvents = hasEvents,
                )
                if (showsBattery) {
                    Spacer(modifier = Modifier.height(GlassMetrics.LandscapeBatteryGap * scale))
                    GlassBattery(state = state, scale = scale)
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = GlassMetrics.CardMargin * scale),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(visible = showsNotification, enter = fadeIn(tween(GlassMetrics.FADE_MILLIS)), exit = fadeOut(tween(GlassMetrics.FADE_MILLIS))) {
                GlassNotificationCard(
                    state = state,
                    showsLatest = showsLatest,
                    scale = scale,
                    maxWidth = maxCardWidth,
                    onTap = onToggleLatest,
                )
            }

            AnimatedVisibility(
                visible = showsMedia,
                enter = slideInVertically(tween(GlassMetrics.SWAP_MILLIS)) { slidePx } + fadeIn(tween(GlassMetrics.FADE_MILLIS)),
                exit = slideOutVertically(tween(GlassMetrics.SWAP_MILLIS)) { slidePx } + fadeOut(tween(GlassMetrics.FADE_MILLIS)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = if (showsNotification) GlassMetrics.CardGap * scale else 0.dp),
                ) {
                    AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
                }
            }
        }
    }
}

@Composable
private fun GlassClockCluster(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    scale: Float,
    hasDateRow: Boolean,
    hasEvents: Boolean,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (hasDateRow) {
            GlassDate(state = state, scale = scale)
            Spacer(modifier = Modifier.height(GlassMetrics.DateClockGap * scale))
        }

        AodGlassClock(
            state = state,
            size = state.appearance.theme.config[GlassThemeSpec.ClockSize].sp * scale,
        )

        if (hasEvents) {
            Spacer(modifier = Modifier.height(GlassMetrics.ClockEventsGap * scale))
            AodPanelBlock(
                block = AodPanelBlockValue.EVENTS,
                state = state,
                onIntent = onIntent,
            )
        }
    }
}

@Composable
private fun GlassDate(
    state: AodState,
    scale: Float,
) {
    val date = if (state.extras.isDateEnabled) {
        rememberZonedDateTime(state.nowMillis).format(rememberFormatter(R.string.aod_glass_date_pattern))
    } else {
        null
    }
    val weather = state.weather
    val config = state.appearance.theme.config
    val style = TextStyle(
        brush = config.brushOf(GlassThemeSpec.DateColor),
        alpha = GlassMetrics.DATE_ALPHA,
        fontSize = GlassMetrics.DateSize * scale,
        fontFamily = FontFamilyInter,
        fontWeight = FontWeight.Medium,
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (date != null) {
            Text(text = date, style = style, maxLines = 1)
        }
        if (date != null && weather != null) {
            Text(text = stringResource(R.string.aod_glass_separator), style = style, maxLines = 1)
        }
        AodWeather(
            weather = weather,
            style = state.appearance.components.styleOf(AodComponentValue.WEATHER),
            layout = state.appearance.components.weatherLayout,
        )
    }
}

@Composable
private fun GlassNotificationCard(
    state: AodState,
    showsLatest: Boolean,
    scale: Float,
    maxWidth: Dp,
    onTap: () -> Unit,
) {
    val config = state.appearance.theme.config
    val latest = state.notifications.latest
    val gradient = config.gradientOf(GlassThemeSpec.NotificationColor)?.toBrush()
    val slide = with(LocalDensity.current) { (GlassMetrics.SwapSlide * scale).roundToPx() }

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .widthIn(max = maxWidth)
            .background(color = GlassCard, shape = RoundedCornerShape14dp)
            .then(
                if (state.isClockLive) {
                    Modifier.onClick(shape = RoundedCornerShape14dp, ripple = false, action = onTap)
                } else {
                    Modifier
                },
            )
            .padding(GlassMetrics.CardPadding * scale),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = showsLatest && latest != null,
            transitionSpec = {
                if (targetState) {
                    slideInVertically(tween(GlassMetrics.SWAP_MILLIS)) { -slide } togetherWith
                        ExitTransition.None using SizeTransform(clip = false)
                } else {
                    fadeIn(tween(GlassMetrics.FADE_MILLIS)) togetherWith
                        (slideOutVertically(tween(GlassMetrics.SWAP_MILLIS)) { -slide } + fadeOut(tween(GlassMetrics.FADE_MILLIS))) using
                        SizeTransform(clip = false)
                }
            },
            contentAlignment = Alignment.Center,
            label = "GlassNotification",
        ) { isLatest ->
            if (isLatest && latest != null) {
                AodLatestLine(
                    text = latest.lineText(R.string.aod_glass_notification),
                    icon = latest.icon,
                    color = config[GlassThemeSpec.NotificationColor],
                    brush = gradient,
                    style = GlassMetrics.LatestLine.copy(
                        iconSize = GlassMetrics.LatestLine.iconSize * scale,
                        iconGap = GlassMetrics.LatestLine.iconGap * scale,
                        textSize = GlassMetrics.LatestLine.textSize * scale,
                    ),
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    state.notifications.icons.forEachIndexed { index, icon ->
                        key(icon.packageName) {
                            Image(
                                bitmap = remember(icon.icon) { icon.icon.asImageBitmap() },
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = if (index > 0) GlassMetrics.NotificationIconGap * scale else 0.dp)
                                    .size(GlassMetrics.NotificationIcon * scale)
                                    .aodTint(gradient),
                                colorFilter = ColorFilter.tint(config[GlassThemeSpec.NotificationColor]),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GlassBattery(
    state: AodState,
    scale: Float,
) {
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled } ?: return
    val textRes = when {
        battery.isCharging && battery.percent >= 100 -> R.string.aod_glass_battery_full
        battery.isCharging -> R.string.aod_glass_battery_charging
        else -> R.string.aod_battery
    }

    Text(
        text = stringResource(textRes, battery.percent),
        modifier = Modifier.alpha(GlassMetrics.BATTERY_ALPHA),
        style = TextStyle(
            brush = state.appearance.theme.config.brushOf(GlassThemeSpec.BatteryColor),
            fontSize = GlassMetrics.BatterySize * scale,
            fontFamily = FontFamilyInter,
            fontWeight = FontWeight.Medium,
        ),
        maxLines = 1,
    )
}

private fun Modifier.glassLandscapeLeft(fraction: () -> Float): Modifier = layout { measurable, constraints ->
    val width = (constraints.maxWidth * fraction()).roundToInt()
    val placeable = measurable.measure(constraints.copy(minWidth = width, maxWidth = width))
    layout(width, constraints.maxHeight) { placeable.place(0, 0) }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodGlassPortraitPreview() {
    AodGlassPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.GLASS)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodGlassLandscapePreview() {
    AodGlassLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.GLASS)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
