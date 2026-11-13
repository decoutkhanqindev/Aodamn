package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.eclipse

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodLatestLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.eclipse.EclipseThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlinx.coroutines.delay

@Composable
fun AodEclipsePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    EclipseFace(state = state, onIntent = onIntent, isLandscape = false)
}

@Composable
fun AodEclipseLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    EclipseFace(state = state, onIntent = onIntent, isLandscape = true)
}

@Composable
private fun EclipseFace(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    isLandscape: Boolean,
) {
    val latest = state.notifications.latest
    val hasMedia = state.media != null
    val currentHasMedia by rememberUpdatedState(hasMedia)
    val latestStamp = latest?.let { content -> listOf(content.key, content.title, content.text) }
    val trackStamp = state.media?.let { media -> listOf(media.title, media.artist) }
    var isMediaShown by remember { mutableStateOf(false) }
    var isLatestShown by remember { mutableStateOf(false) }
    var mediaStamp by remember { mutableIntStateOf(0) }
    var latestHoldStamp by remember { mutableIntStateOf(0) }
    var seenLatest by remember { mutableStateOf(latestStamp) }
    var seenTrack by remember { mutableStateOf(if (state.isPreview) trackStamp else null) }

    SideEffect(latestStamp) {
        if (latestStamp != seenLatest) {
            seenLatest = latestStamp
            if (latestStamp != null) {
                isMediaShown = false
                isLatestShown = true
                latestHoldStamp++
            }
        }
    }

    SideEffect(trackStamp) {
        if (trackStamp != seenTrack) {
            seenTrack = trackStamp
            if (trackStamp != null) {
                isMediaShown = true
                mediaStamp++
            }
        }
    }

    if (isMediaShown) {
        LaunchedEffect(mediaStamp) {
            delay(EclipseMetrics.MEDIA_HOLD_MILLIS)
            isMediaShown = false
        }
    }

    if (isLatestShown) {
        LaunchedEffect(latestHoldStamp) {
            delay(EclipseMetrics.LATEST_HOLD_MILLIS)
            isLatestShown = false
        }
    }

    val slidePx = with(LocalDensity.current) { EclipseMetrics.SwapSlide.roundToPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .eclipseTap(isEnabled = state.isClockLive) {
                if (isMediaShown) {
                    isMediaShown = false
                } else if (currentHasMedia) {
                    isMediaShown = true
                    mediaStamp++
                }
            },
    ) {
        AodEclipseClock(
            nowMillis = state.nowMillis,
            isLive = state.isClockLive,
            isPreview = state.isPreview,
            isStill = state.isStill,
            isLandscape = isLandscape,
            config = state.appearance.theme.config,
        )

        AnimatedContent(
            targetState = hasMedia && (isMediaShown || state.isPreviewFocused(AodComponentValue.MEDIA)),
            modifier = if (isLandscape) {
                Modifier
                    .align(Alignment.BottomCenter)
                    .eclipseBandWidth()
                    .padding(bottom = EclipseMetrics.LandscapeInset)
                    .height(EclipseMetrics.LandscapeBand)
            } else {
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = EclipseMetrics.PortraitBottomMargin)
            },
            transitionSpec = {
                if (targetState) {
                    slideInVertically(tween(EclipseMetrics.SWAP_MILLIS, easing = LinearOutSlowInEasing)) { slidePx } togetherWith
                        ExitTransition.None using SizeTransform(clip = false)
                } else {
                    fadeIn(tween(EclipseMetrics.FADE_MILLIS)) togetherWith (
                        slideOutVertically(tween(EclipseMetrics.SWAP_MILLIS)) { slidePx } + fadeOut(tween(EclipseMetrics.SWAP_MILLIS))
                    ) using SizeTransform(clip = false)
                }
            },
            contentAlignment = if (isLandscape) Alignment.Center else Alignment.BottomCenter,
            label = "EclipseBottom",
        ) { isMedia ->
            if (isMedia) {
                AodPanelBlock(
                    block = AodPanelBlockValue.MEDIA,
                    state = state,
                    onIntent = { intent ->
                        onIntent(intent)
                        mediaStamp++
                    },
                )
            } else {
                EclipseDetails(
                    state = state,
                    isLandscape = isLandscape,
                    isLatestShown = isLatestShown || state.isPreviewFocused(AodComponentValue.NOTIFICATIONS),
                    onTap = {
                        if (isLatestShown) {
                            isLatestShown = false
                        } else if (latest != null) {
                            isLatestShown = true
                            latestHoldStamp++
                        }
                    },
                )
            }
        }
    }
}

private fun Modifier.eclipseTap(
    isEnabled: Boolean,
    onTap: () -> Unit,
): Modifier {
    if (!isEnabled) return this
    return pointerInput(Unit) {
        awaitEachGesture {
            awaitFirstDown(requireUnconsumed = false)
            if (waitForUpOrCancellation() != null) onTap()
        }
    }
}

private fun Modifier.eclipseBandWidth(): Modifier = layout { measurable, constraints ->
    val width = minOf(constraints.maxWidth, constraints.maxHeight)
    val placeable = measurable.measure(constraints.copy(minWidth = width, maxWidth = width))
    layout(placeable.width, placeable.height) { placeable.place(0, 0) }
}

@Composable
private fun EclipseDetails(
    state: AodState,
    isLandscape: Boolean,
    isLatestShown: Boolean,
    onTap: () -> Unit,
) {
    val config = state.appearance.theme.config
    val components = state.appearance.components
    val latest = state.notifications.latest
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val isDateShown = state.extras.isDateEnabled
    val hasDateRow = isDateShown || state.weather != null
    val hasStatus = battery != null || state.isFlashlightOn || state.notifications.icons.isNotEmpty()
    val showsLatest = isLatestShown && latest != null
    val statusColor = lerp(config[EclipseThemeSpec.StatusColor], Black, EclipseMetrics.TEXT_DARKEN)
    val statusBrush = if (config.gradientOf(EclipseThemeSpec.StatusColor) != null) {
        config.darkBrushOf(EclipseThemeSpec.StatusColor, EclipseMetrics.TEXT_DARKEN)
    } else {
        null
    }
    val slide = with(LocalDensity.current) { EclipseMetrics.SwapSlide.roundToPx() }

    Column(
        modifier = Modifier
            .then(if (state.isClockLive) Modifier.onClick(ripple = false, action = onTap) else Modifier)
            .padding(horizontal = EclipseMetrics.DetailsPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (hasDateRow) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isDateShown) {
                    Text(
                        text = rememberZonedDateTime(state.nowMillis)
                            .format(rememberFormatter(R.string.aod_eclipse_date_pattern))
                            .uppercase(LocalConfiguration.current.locales[0]),
                        style = TextStyle(
                            brush = config.brushOf(EclipseThemeSpec.DateColor),
                            fontSize = if (isLandscape) EclipseMetrics.LandscapeDate else EclipseMetrics.PortraitDate,
                            fontFamily = EclipseThemeSpec.Font,
                            letterSpacing = EclipseMetrics.DateSpacing,
                        ),
                        maxLines = 1,
                    )
                }

                AodWeather(
                    weather = state.weather,
                    style = components.styleOf(AodComponentValue.WEATHER),
                    layout = components.weatherLayout,
                )
            }
        }

        AnimatedContent(
            targetState = showsLatest,
            modifier = Modifier.padding(
                top = if (hasDateRow && (hasStatus || showsLatest)) {
                    if (isLandscape) EclipseMetrics.LandscapeDateGap else EclipseMetrics.PortraitDateGap
                } else {
                    0.dp
                },
            ),
            transitionSpec = {
                if (targetState) {
                    slideInVertically(tween(EclipseMetrics.SWAP_MILLIS)) { -slide } togetherWith
                        ExitTransition.None using SizeTransform(clip = false)
                } else {
                    fadeIn(tween(EclipseMetrics.FADE_MILLIS)) togetherWith (
                        slideOutVertically(tween(EclipseMetrics.SWAP_MILLIS)) { -slide } + fadeOut(tween(EclipseMetrics.SWAP_MILLIS))
                    ) using SizeTransform(clip = false)
                }
            },
            contentAlignment = Alignment.Center,
            label = "EclipseStatus",
        ) { isLatest ->
            if (isLatest && latest != null) {
                AodLatestLine(
                    text = latest.lineText(R.string.aod_eclipse_notification),
                    icon = latest.icon,
                    color = statusColor,
                    brush = statusBrush,
                    style = EclipseMetrics.LatestLine,
                )
            } else {
                AodStatusLine(
                    battery = battery,
                    isFlashlightOn = state.isFlashlightOn,
                    notifications = state.notifications.copy(latest = null),
                    color = statusColor,
                    glyphHeight = EclipseMetrics.StatusGlyph,
                    fontFamily = EclipseThemeSpec.Font,
                    gapScale = EclipseMetrics.GAP_SCALE,
                    brush = statusBrush,
                )
            }
        }
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodEclipsePortraitPreview() {
    AodEclipsePortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.ECLIPSE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodEclipseLandscapePreview() {
    AodEclipseLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.ECLIPSE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
