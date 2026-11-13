package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.meridian

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.meridian.MeridianThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSans
import kotlinx.coroutines.delay

@Composable
fun AodMeridianPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    MeridianFace(state = state, onIntent = onIntent, isLandscape = false)
}

@Composable
fun AodMeridianLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    MeridianFace(state = state, onIntent = onIntent, isLandscape = true)
}

@Composable
private fun MeridianFace(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    isLandscape: Boolean,
) {
    val latest = state.notifications.latest
    val hasMedia = state.media != null
    val currentHasMedia by rememberUpdatedState(hasMedia)
    val slidePx = with(LocalDensity.current) { MeridianMetrics.SwapSlide.roundToPx() }
    val displayMillis = rememberMeridianMillis(state = state)
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
            delay(MeridianMetrics.MEDIA_HOLD_MILLIS)
            isMediaShown = false
        }
    }
    if (isLatestShown) {
        LaunchedEffect(latestHoldStamp) {
            delay(MeridianMetrics.LATEST_HOLD_MILLIS)
            isLatestShown = false
        }
    }

    val showsMedia = hasMedia && (isMediaShown || state.isPreviewShown(AodComponentValue.MEDIA))
    val showsLatest = isLatestShown || state.isPreviewFocused(AodComponentValue.NOTIFICATIONS)
    val hasStatus = (state.battery != null && state.extras.isBatteryEnabled) ||
        state.isFlashlightOn ||
        state.notifications.icons.isNotEmpty() ||
        (showsLatest && latest != null)
    val toggleLatest = {
        if (isLatestShown) {
            isLatestShown = false
        } else if (latest != null) {
            isLatestShown = true
            latestHoldStamp++
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .meridianTap(isEnabled = state.isClockLive) {
                if (isMediaShown) {
                    isMediaShown = false
                } else if (currentHasMedia) {
                    isMediaShown = true
                    mediaStamp++
                }
            },
    ) {
        val scale = MeridianMetrics.scale(minOf(maxWidth, maxHeight).value)
        if (!isLandscape) {
            Box(modifier = Modifier.fillMaxSize()) {
                AodMeridianDate(
                    state = state,
                    displayMillis = displayMillis,
                    scale = scale,
                    isInline = false,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = MeridianMetrics.PortraitDateTop),
                )
                AodMeridianCards(
                    state = state,
                    displayMillis = displayMillis,
                    scale = scale,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(
                            start = MeridianMetrics.PortraitSide,
                            end = MeridianMetrics.PortraitSide,
                            bottom = MeridianMetrics.PortraitBottom,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AnimatedVisibility(
                        visible = showsMedia,
                        enter = slideInVertically(tween(MeridianMetrics.SWAP_MILLIS)) { slidePx } + fadeIn(tween(MeridianMetrics.SWAP_MILLIS)),
                        exit = fadeOut(tween(MeridianMetrics.SWAP_MILLIS)),
                    ) {
                        Box(modifier = if (hasStatus) Modifier.padding(bottom = MeridianMetrics.PortraitMediaGap) else Modifier) {
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
                    MeridianDetails(
                        state = state,
                        isLatestShown = showsLatest,
                        onTap = toggleLatest,
                    )
                }
            }
        } else {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(MeridianMetrics.LANDSCAPE_LEFT)
                        .fillMaxHeight()
                        .padding(horizontal = MeridianMetrics.LandscapePairPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    AodMeridianCards(state = state, displayMillis = displayMillis, scale = scale, modifier = Modifier.fillMaxWidth())
                }
                Column(
                    modifier = Modifier
                        .weight(1f - MeridianMetrics.LANDSCAPE_LEFT)
                        .fillMaxHeight()
                        .padding(end = MeridianMetrics.LandscapeEnd),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val hasDateRow = state.extras.isDateEnabled || state.weather != null
                    if (hasDateRow) {
                        AodMeridianDate(state = state, displayMillis = displayMillis, scale = scale, isInline = true)
                        if (hasStatus) {
                            Spacer(modifier = Modifier.height(MeridianMetrics.LandscapeLineGap))
                        }
                    }
                    MeridianDetails(
                        state = state,
                        isLatestShown = showsLatest,
                        onTap = toggleLatest,
                    )
                    AnimatedVisibility(
                        visible = showsMedia,
                        enter = fadeIn(tween(MeridianMetrics.SWAP_MILLIS)) + expandVertically(tween(MeridianMetrics.SWAP_MILLIS)),
                        exit = fadeOut(tween(MeridianMetrics.SWAP_MILLIS)) + shrinkVertically(tween(MeridianMetrics.SWAP_MILLIS)),
                    ) {
                        Box(modifier = if (hasDateRow || hasStatus) Modifier.padding(top = MeridianMetrics.LandscapeMediaGap) else Modifier) {
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
                }
            }
        }
    }
}

private fun Modifier.meridianTap(
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

@Composable
private fun MeridianDetails(
    state: AodState,
    isLatestShown: Boolean,
    onTap: () -> Unit,
) {
    val config = state.appearance.theme.config
    val latest = state.notifications.latest
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val showsLatest = isLatestShown && latest != null
    val statusBrush = config.gradientOf(MeridianThemeSpec.StatusColor)?.toBrush()
    val slide = with(LocalDensity.current) { MeridianMetrics.SwapSlide.roundToPx() }

    Column(
        modifier = Modifier.then(if (state.isClockLive) Modifier.onClick(ripple = false, action = onTap) else Modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedContent(
            targetState = showsLatest,
            transitionSpec = {
                if (targetState) {
                    slideInVertically(tween(MeridianMetrics.SWAP_MILLIS)) { -slide } togetherWith
                        ExitTransition.None using SizeTransform(clip = false)
                } else {
                    fadeIn(tween(MeridianMetrics.FADE_MILLIS)) togetherWith (
                        slideOutVertically(tween(MeridianMetrics.SWAP_MILLIS)) { -slide } + fadeOut(tween(MeridianMetrics.SWAP_MILLIS))
                    ) using SizeTransform(clip = false)
                }
            },
            contentAlignment = Alignment.Center,
            label = "MeridianStatus",
        ) { isLatest ->
            if (isLatest && latest != null) {
                AodLatestLine(
                    text = latest.lineText(R.string.aod_meridian_notification),
                    icon = latest.icon,
                    color = config[MeridianThemeSpec.StatusColor],
                    brush = statusBrush,
                    style = MeridianMetrics.LatestLine,
                    modifier = Modifier.padding(horizontal = MeridianMetrics.LatestMargin),
                )
            } else {
                AodStatusLine(
                    battery = battery,
                    isFlashlightOn = state.isFlashlightOn,
                    notifications = state.notifications.copy(latest = null),
                    color = config[MeridianThemeSpec.StatusColor],
                    glyphHeight = MeridianMetrics.StatusGlyph,
                    fontFamily = FontFamilyGoogleSans,
                    brush = statusBrush,
                )
            }
        }
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodMeridianPortraitPreview() {
    AodMeridianPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.MERIDIAN)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodMeridianLandscapePreview() {
    AodMeridianLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.MERIDIAN)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
