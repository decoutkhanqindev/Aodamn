package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.poster

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.poster.PosterThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSans

@Composable
fun AodPosterPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    PosterFace(state = state, onIntent = onIntent, isLandscape = false)
}

@Composable
fun AodPosterLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    PosterFace(state = state, onIntent = onIntent, isLandscape = true)
}

@Composable
private fun PosterFace(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    isLandscape: Boolean,
) {
    val latest = state.notifications.latest
    val hasMedia = state.media != null
    val latestStamp = latest?.let { content -> listOf(content.key, content.title, content.text) }
    var seenLatest by remember { mutableStateOf(latestStamp) }

    SideEffect(latestStamp) {
        if (latestStamp != seenLatest) {
            seenLatest = latestStamp
            if (latestStamp != null) {
                if (!state.isPreview) onIntent(AodIntent.ToggleMediaControls(isShown = false))
            }
        }
    }

    val slidePx = with(LocalDensity.current) { PosterMetrics.SwapSlide.roundToPx() }

    Box(modifier = Modifier.fillMaxSize()) {
        AodPosterText(state = state, isLandscape = isLandscape)

        Column(
            modifier = Modifier.posterCluster(isLandscape),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(
                visible = hasMedia && ((!state.isPreview && state.isMediaShown) || state.isPreviewShown(AodComponentValue.MEDIA)),
                enter = slideInVertically(tween(PosterMetrics.SWAP_MILLIS)) { slidePx } + fadeIn(tween(PosterMetrics.SWAP_MILLIS)),
                exit = fadeOut(tween(PosterMetrics.SWAP_MILLIS)),
            ) {
                Box(
                    modifier = Modifier.padding(
                        bottom = if (isLandscape) PosterMetrics.LandscapeDetailsTop else PosterMetrics.PortraitDetailsTop,
                    ),
                ) {
                    AodPanelBlock(
                        block = AodPanelBlockValue.MEDIA,
                        state = state,
                        onIntent = onIntent,
                    )
                }
            }

            PosterDetails(
                state = state,
                isLandscape = isLandscape,
                isLatestShown = (!state.isPreview && state.isLatestShown) || state.isPreviewFocused(AodComponentValue.NOTIFICATIONS),
                onTap = { if (latest != null || state.isLatestShown) onIntent(AodIntent.ToggleLatestNotification(isShown = !state.isLatestShown)) },
            )
        }
    }
}

private fun Modifier.posterCluster(isLandscape: Boolean): Modifier = layout { measurable, constraints ->
    val padding = PosterMetrics.LandscapePadding.roundToPx()
    val width = if (isLandscape) {
        (constraints.maxWidth * (1f - PosterMetrics.LANDSCAPE_COLUMN)).toInt() - 2 * padding
    } else {
        constraints.maxWidth
    }
    val placeable = measurable.measure(constraints.copy(minWidth = width, maxWidth = width))
    layout(constraints.maxWidth, constraints.maxHeight) {
        val x = if (isLandscape) (constraints.maxWidth * PosterMetrics.LANDSCAPE_COLUMN).toInt() + padding else 0
        val bottom = if (isLandscape) constraints.maxHeight - PosterMetrics.LandscapeBottom.roundToPx() else (constraints.maxHeight * PosterMetrics.PORTRAIT_BOTTOM).toInt()
        placeable.place(x, bottom - placeable.height)
    }
}

@Composable
private fun PosterDetails(
    state: AodState,
    isLandscape: Boolean,
    isLatestShown: Boolean,
    onTap: () -> Unit,
) {
    val config = state.appearance.theme.config
    val latest = state.notifications.latest
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val showsLatest = isLatestShown && latest != null
    val statusBrush = config.gradientOf(PosterThemeSpec.StatusColor)?.toBrush()
    val slide = with(LocalDensity.current) { PosterMetrics.SwapSlide.roundToPx() }

    Column(
        modifier = Modifier
            .then(if (state.isClockLive) Modifier.onClick(ripple = false, action = onTap) else Modifier)
            .padding(bottom = if (isLandscape) 0.dp else PosterMetrics.PortraitDetailsBottom),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedContent(
            targetState = showsLatest,
            transitionSpec = {
                if (targetState) {
                    slideInVertically(tween(PosterMetrics.SWAP_MILLIS)) { -slide } togetherWith
                        ExitTransition.None using SizeTransform(clip = false)
                } else {
                    fadeIn(tween(PosterMetrics.FADE_MILLIS)) togetherWith (
                        slideOutVertically(tween(PosterMetrics.SWAP_MILLIS)) { -slide } + fadeOut(tween(PosterMetrics.SWAP_MILLIS))
                    ) using SizeTransform(clip = false)
                }
            },
            contentAlignment = Alignment.Center,
            label = "PosterStatus",
        ) { isLatest ->
            if (isLatest && latest != null) {
                AodLatestLine(
                    text = latest.lineText(R.string.aod_poster_notification),
                    icon = latest.icon,
                    color = config[PosterThemeSpec.StatusColor],
                    brush = statusBrush,
                    style = PosterMetrics.LatestLine,
                    modifier = Modifier.padding(horizontal = PosterMetrics.LatestMargin),
                )
            } else {
                AodStatusLine(
                    battery = battery,
                    isFlashlightOn = state.isFlashlightOn,
                    notifications = state.notifications.copy(latest = null),
                    color = config[PosterThemeSpec.StatusColor],
                    glyphHeight = PosterMetrics.StatusGlyph,
                    fontFamily = FontFamilyGoogleSans,
                    gapScale = PosterMetrics.STATUS_GAP_SCALE,
                    brush = statusBrush,
                )
            }
        }
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodPosterPortraitPreview() {
    AodPosterPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.POSTER)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodPosterLandscapePreview() {
    AodPosterLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.POSTER)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
