package com.decoutkhanqindev.aodamn.presentation.components.aod

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.AodLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.AodPortrait
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.WallpaperValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.CalendarEventUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WeatherConditionValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WeatherUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.AodExtrasUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodActionValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodGestureValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AodContent(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
    isInteractive: Boolean = true,
    backdropRotationZ: Float? = null,
) {
    val currentOnIntent by rememberUpdatedState(onIntent)
    val focusRequester = remember { FocusRequester() }

    SideEffect(Unit) {
        if (isInteractive) focusRequester.requestFocus()
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .then(
                if (isInteractive) {
                    Modifier
                        .onPreviewKeyEvent { event ->
                            val gesture = when (event.key) {
                                Key.VolumeUp -> AodGestureValue.VOLUME_UP
                                Key.VolumeDown -> AodGestureValue.VOLUME_DOWN
                                else -> null
                            }
                            if (gesture == null || state.interaction.actionOf(gesture) == AodActionValue.NONE) {
                                false
                            } else {
                                if (event.type == KeyEventType.KeyDown && event.nativeKeyEvent.repeatCount == 0) {
                                    currentOnIntent(AodIntent.PerformGesture(gesture))
                                }
                                true
                            }
                        }
                        .focusRequester(focusRequester)
                        .focusable()
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = { currentOnIntent(AodIntent.PerformGesture(AodGestureValue.DOUBLE_TAP)) },
                            )
                        }
                        .pointerInput(Unit) {
                            var dragTotal = 0f
                            detectVerticalDragGestures(
                                onDragStart = { dragTotal = 0f },
                                onDragEnd = {
                                    val threshold = 80.dp.toPx()
                                    when {
                                        dragTotal <= -threshold ->
                                            currentOnIntent(AodIntent.PerformGesture(AodGestureValue.SWIPE_UP))

                                        dragTotal >= threshold ->
                                            currentOnIntent(AodIntent.PerformGesture(AodGestureValue.SWIPE_DOWN))
                                    }
                                },
                                onVerticalDrag = { change, dragAmount ->
                                    change.consume()
                                    dragTotal += dragAmount
                                },
                            )
                        }
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        val isLandscape = maxWidth > maxHeight

        AodBackdrop(rotationZ = backdropRotationZ) {
            AodWallpaper(wallpaper = state.wallpaper.takeIf { !state.isDark })

            AodBackground(background = state.background.takeIf { !state.isDark })
        }

        AodEdgeGlow(
            isGlowing = state.isGlowing,
            color = state.glowColorArgb?.let { Color(it) } ?: Mint,
        )

        AnimatedVisibility(
            visible = !state.isDark,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                if (isLandscape) {
                    AodLandscape(state = state, onIntent = currentOnIntent)
                } else {
                    AodPortrait(state = state, onIntent = currentOnIntent)
                }

                AodExitHint(
                    isVisible = state.isExitHintVisible,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 48.dp),
                )
            }
        }
    }
}

@Composable
private fun AodExitHint(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    val alpha = animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "AodExitHint",
    )

    Text(
        text = stringResource(R.string.aod_hint),
        modifier = modifier.alpha(alpha.value),
        color = Grey5A,
        fontSize = 12.sp,
    )
}

@Preview(widthDp = 360, heightDp = 720)
@Composable
private fun AodContentPreview() {
    AodContent(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            extras = AodExtrasUiModel(memo = "Buy milk"),
            wallpaper = WallpaperValue.AURORA,
            notifications = AodNotificationsUiModel(
                latest = NotificationContentUiModel(
                    key = "preview",
                    icon = createBitmap(48, 48),
                    title = "Alice",
                    text = "See you at 7 at the usual place?",
                    isHidden = false,
                ),
            ),
            weather = WeatherUiModel(
                temperature = 24,
                condition = WeatherConditionValue.CLOUDY,
                isDay = true,
                updatedAtMillis = System.currentTimeMillis(),
            ),
            events = persistentListOf(
                CalendarEventUiModel(
                    title = "Team meeting",
                    beginMillis = System.currentTimeMillis(),
                    endMillis = System.currentTimeMillis(),
                    isAllDay = false,
                ),
            ),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            media = MediaUiModel(
                title = "Song title",
                artist = "Artist",
                isPlaying = true,
                canSkipToPrevious = true,
                canSkipToNext = true,
            ),
            isGlowing = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 720, heightDp = 360)
@Composable
private fun AodContentLandscapePreview() {
    AodContent(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.PEBBLE)),
            battery = BatteryUiModel(percent = 72, isCharging = true),
        ),
        onIntent = {},
    )
}
