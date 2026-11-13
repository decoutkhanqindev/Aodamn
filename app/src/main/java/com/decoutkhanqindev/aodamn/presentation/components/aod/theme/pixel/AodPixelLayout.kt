package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pixel

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodLatestLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.lineText
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pixel.PixelThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSans
import kotlinx.coroutines.delay

@Composable
fun AodPixelPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    PixelFace(state = state, onIntent = onIntent, isLandscape = false)
}

@Composable
fun AodPixelLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    PixelFace(state = state, onIntent = onIntent, isLandscape = true)
}

@Composable
private fun PixelFace(
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
            delay(PixelMetrics.MEDIA_HOLD_MILLIS)
            isMediaShown = false
        }
    }

    if (isLatestShown) {
        LaunchedEffect(latestHoldStamp) {
            delay(PixelMetrics.LATEST_HOLD_MILLIS)
            isLatestShown = false
        }
    }

    val slidePx = with(LocalDensity.current) { PixelMetrics.SwapSlide.roundToPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pixelTap(isEnabled = state.isClockLive) {
                if (isMediaShown) {
                    isMediaShown = false
                } else if (currentHasMedia) {
                    isMediaShown = true
                    mediaStamp++
                }
            },
    ) {
        AodPixelText(state = state, isLandscape = isLandscape)

        Column(
            modifier = Modifier.pixelCluster(isLandscape),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(
                visible = hasMedia && (isMediaShown || state.isPreviewShown(AodComponentValue.MEDIA)),
                enter = slideInVertically(tween(PixelMetrics.SWAP_MILLIS)) { slidePx } + fadeIn(tween(PixelMetrics.SWAP_MILLIS)),
                exit = fadeOut(tween(PixelMetrics.SWAP_MILLIS)),
            ) {
                Box(
                    modifier = Modifier.padding(
                        bottom = if (isLandscape) PixelMetrics.LandscapeDetailsTop else PixelMetrics.PortraitDetailsTop,
                    ),
                ) {
                    AodPanelBlock(
                        block = AodPanelBlockValue.MEDIA,
                        state = state,
                        onIntent = { intent ->
                            onIntent(intent)
                            mediaStamp++
                        },
                    )
                }
            }

            PixelDetails(
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

private fun Modifier.pixelTap(
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

private fun Modifier.pixelCluster(isLandscape: Boolean): Modifier = layout { measurable, constraints ->
    val padding = PixelMetrics.LandscapePadding.roundToPx()
    val width = if (isLandscape) {
        (constraints.maxWidth * (1f - PixelMetrics.LANDSCAPE_COLUMN)).toInt() - 2 * padding
    } else {
        constraints.maxWidth
    }
    val placeable = measurable.measure(constraints.copy(minWidth = width, maxWidth = width))
    layout(constraints.maxWidth, constraints.maxHeight) {
        val x = if (isLandscape) (constraints.maxWidth * PixelMetrics.LANDSCAPE_COLUMN).toInt() + padding else 0
        val bottom = if (isLandscape) constraints.maxHeight - PixelMetrics.LandscapeBottom.roundToPx() else (constraints.maxHeight * PixelMetrics.PORTRAIT_BOTTOM).toInt()
        placeable.place(x, bottom - placeable.height)
    }
}

@Composable
private fun PixelDetails(
    state: AodState,
    isLandscape: Boolean,
    isLatestShown: Boolean,
    onTap: () -> Unit,
) {
    val config = state.appearance.theme.config
    val latest = state.notifications.latest
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val showsLatest = isLatestShown && latest != null
    val statusBrush = config.gradientOf(PixelThemeSpec.StatusColor)?.toBrush()
    val slide = with(LocalDensity.current) { PixelMetrics.SwapSlide.roundToPx() }

    Column(
        modifier = Modifier
            .then(if (state.isClockLive) Modifier.onClick(ripple = false, action = onTap) else Modifier)
            .padding(bottom = if (isLandscape) 0.dp else PixelMetrics.PortraitDetailsBottom),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedContent(
            targetState = showsLatest,
            transitionSpec = {
                if (targetState) {
                    slideInVertically(tween(PixelMetrics.SWAP_MILLIS)) { -slide } togetherWith
                        ExitTransition.None using SizeTransform(clip = false)
                } else {
                    fadeIn(tween(PixelMetrics.FADE_MILLIS)) togetherWith (
                        slideOutVertically(tween(PixelMetrics.SWAP_MILLIS)) { -slide } + fadeOut(tween(PixelMetrics.SWAP_MILLIS))
                    ) using SizeTransform(clip = false)
                }
            },
            contentAlignment = Alignment.Center,
            label = "PixelStatus",
        ) { isLatest ->
            if (isLatest && latest != null) {
                AodLatestLine(
                    text = latest.lineText(R.string.aod_pixel_notification),
                    icon = latest.icon,
                    color = config[PixelThemeSpec.StatusColor],
                    brush = statusBrush,
                    style = PixelMetrics.LatestLine,
                    modifier = Modifier.padding(horizontal = PixelMetrics.LatestMargin),
                )
            } else {
                AodStatusLine(
                    battery = battery,
                    isFlashlightOn = state.isFlashlightOn,
                    notifications = state.notifications.copy(latest = null),
                    color = config[PixelThemeSpec.StatusColor],
                    glyphHeight = PixelMetrics.StatusGlyph,
                    fontFamily = FontFamilyGoogleSans,
                    gapScale = PixelMetrics.STATUS_GAP_SCALE,
                    brush = statusBrush,
                )
            }
        }
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodPixelPortraitPreview() {
    AodPixelPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.PIXEL)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodPixelLandscapePreview() {
    AodPixelLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.PIXEL)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
