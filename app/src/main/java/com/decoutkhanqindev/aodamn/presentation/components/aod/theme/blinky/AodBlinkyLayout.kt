package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.blinky

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.min
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.lineText
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.blinky.BlinkyLineValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.blinky.BlinkyThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyNunito
import kotlinx.coroutines.delay

@Composable
fun AodBlinkyPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BlinkyFace(state = state, onIntent = onIntent)
}

@Composable
fun AodBlinkyLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BlinkyFace(state = state, onIntent = onIntent)
}

@Composable
private fun BlinkyFace(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val config = state.appearance.theme.config
    val isCharging = state.battery?.isCharging == true
    val latestStamp = state.notifications.latest?.let { latest -> listOf(latest.key, latest.title, latest.text) }
    val mediaStamp = state.media?.let { media -> listOf(media.title, media.artist) }
    val hasMedia = state.media != null
    var line by remember { mutableStateOf(BlinkyLineValue.STATUS) }
    var lineStamp by remember { mutableIntStateOf(0) }
    var wakeCount by remember { mutableIntStateOf(0) }
    var isAsleep by remember { mutableStateOf(false) }
    var seenCharging by remember { mutableStateOf(isCharging) }
    var seenLatest by remember { mutableStateOf(latestStamp) }
    var seenMedia by remember { mutableStateOf(if (state.isPreview) mediaStamp else null) }

    SideEffect(isCharging) {
        if (isCharging != seenCharging) {
            seenCharging = isCharging
            line = BlinkyLineValue.STATUS
            lineStamp++
            wakeCount++
        }
    }

    SideEffect(latestStamp) {
        if (latestStamp != seenLatest) {
            seenLatest = latestStamp
            if (latestStamp != null) {
                line = BlinkyLineValue.NOTIFICATION
                lineStamp++
                wakeCount++
            }
        }
    }

    SideEffect(mediaStamp) {
        if (mediaStamp != seenMedia) {
            seenMedia = mediaStamp
            if (mediaStamp != null) {
                line = BlinkyLineValue.MEDIA
                lineStamp++
                wakeCount++
            }
        }
    }

    LaunchedEffect(line, lineStamp, hasMedia) {
        val holdMillis = BlinkyMetrics.lineHoldMillis(line, hasMedia) ?: return@LaunchedEffect
        delay(holdMillis)
        line = BlinkyLineValue.STATUS
    }

    DisposableEffect(isAsleep) {
        if (isAsleep) onIntent(AodIntent.ToggleMotionWatch(isEnabled = true))
        onDispose {
            if (isAsleep) onIntent(AodIntent.ToggleMotionWatch(isEnabled = false))
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .blinkyTap(isEnabled = state.isClockLive) {
                line = line.next
                lineStamp++
                wakeCount++
            },
    ) {
        AodBlinkyEyes(
            eyeSize = BlinkyMetrics.eyeSize(min(maxWidth, maxHeight), config[BlinkyThemeSpec.CharacterSize]),
            color = config[BlinkyThemeSpec.CharacterColor],
            gradient = config.gradientOf(BlinkyThemeSpec.CharacterColor),
            isLive = state.isClockLive,
            isSleepAllowed = !state.isPreview,
            wakeKey = listOf(wakeCount, state.motionCount),
            onAsleepChange = { isAsleep = it },
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(BlinkyMetrics.CLUSTER_ZONE),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AodBlinkyTime(
                    nowMillis = state.nowMillis,
                    brush = config.brushOf(BlinkyThemeSpec.ClockColor),
                    sizeOption = config[BlinkyThemeSpec.ClockSize],
                    isHidden = config[BlinkyThemeSpec.ClockSize] <= BlinkyThemeSpec.ClockSize.range.first,
                )

                BlinkyLine(
                    state = state,
                    line = line,
                    onPlayPause = {
                        onIntent(AodIntent.PlayPauseMedia)
                        lineStamp++
                    },
                )

                BlinkyNotificationIcons(state = state)
            }
        }
    }
}

private fun Modifier.blinkyTap(
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
private fun BlinkyLine(
    state: AodState,
    line: BlinkyLineValue,
    onPlayPause: () -> Unit,
) {
    AnimatedContent(
        targetState = line,
        transitionSpec = {
            fadeIn(tween(BlinkyMetrics.LINE_FADE_MILLIS)) togetherWith ExitTransition.None using SizeTransform(clip = false)
        },
        contentAlignment = Alignment.Center,
        label = "BlinkyLine",
    ) { target ->
        when (target) {
            BlinkyLineValue.STATUS -> BlinkyStatusLine(state = state)
            BlinkyLineValue.NOTIFICATION -> BlinkyNotificationLine(state = state)
            BlinkyLineValue.MEDIA -> BlinkyMediaLine(state = state, onPlayPause = onPlayPause)
        }
    }
}

@Composable
private fun BlinkyStatusLine(state: AodState) {
    val config = state.appearance.theme.config
    val components = state.appearance.components
    val statusOption = BlinkyThemeSpec.StatusColor
    val dateBrush = config.darkBrushOf(BlinkyThemeSpec.DateColor, BlinkyMetrics.LINE_DARKEN)
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val isDateShown = state.extras.isDateEnabled

    Row(
        modifier = Modifier
            .padding(horizontal = BlinkyMetrics.LineHorizontalPadding, vertical = BlinkyMetrics.LineVerticalPadding)
            .basicMarquee(iterations = 1),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (state.isFlashlightOn) {
            BlinkyIcon(
                iconRes = R.drawable.ic_flashlight_fill,
                option = statusOption,
                config = config,
                modifier = Modifier.padding(end = BlinkyMetrics.LinePowerGap),
            )
        }

        if (battery != null) {
            BlinkyIcon(
                iconRes = if (battery.isCharging) R.drawable.ic_battery_charging_fill else R.drawable.ic_battery_full_fill,
                option = statusOption,
                config = config,
            )
        }

        if (battery != null || state.isFlashlightOn) Spacer(modifier = Modifier.width(BlinkyMetrics.LineIconGap))

        if (battery != null) {
            BlinkyText(
                text = stringResource(R.string.aod_battery, battery.percent),
                brush = config.darkBrushOf(statusOption, BlinkyMetrics.LINE_DARKEN),
            )
        }

        if (isDateShown) {
            if (battery != null) BlinkyText(text = stringResource(R.string.aod_blinky_separator), brush = dateBrush)

            BlinkyText(
                text = rememberZonedDateTime(state.nowMillis).format(rememberFormatter(R.string.aod_blinky_date_pattern)),
                brush = dateBrush,
            )
        }

        if (battery == null && !isDateShown) BlinkyText(text = "", brush = dateBrush)

        AodWeather(
            weather = state.weather,
            style = components.styleOf(AodComponentValue.WEATHER),
            layout = components.weatherLayout,
        )
    }
}

@Composable
private fun BlinkyNotificationLine(state: AodState) {
    val config = state.appearance.theme.config
    val option = BlinkyThemeSpec.StatusColor
    val latest = state.notifications.latest

    Row(
        modifier = Modifier.padding(horizontal = BlinkyMetrics.LineHorizontalPadding, vertical = BlinkyMetrics.LineVerticalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (latest != null) {
            Image(
                bitmap = remember(latest.icon) { latest.icon.asImageBitmap() },
                contentDescription = null,
                modifier = Modifier
                    .size(BlinkyMetrics.LineIconSize)
                    .aodTint(config.gradientOf(option)?.toBrush()),
                colorFilter = ColorFilter.tint(config[option]),
            )
        } else {
            BlinkyIcon(iconRes = R.drawable.ic_bell_fill, option = option, config = config)
        }

        BlinkyText(
            text = latest?.lineText(R.string.aod_blinky_notification) ?: stringResource(R.string.aod_blinky_no_notification),
            brush = config.darkBrushOf(option, BlinkyMetrics.LINE_DARKEN),
            modifier = Modifier
                .padding(start = BlinkyMetrics.LineIconGap)
                .weight(1f, fill = false)
                .basicMarquee(iterations = 1),
        )
    }
}

@Composable
private fun BlinkyMediaLine(
    state: AodState,
    onPlayPause: () -> Unit,
) {
    val config = state.appearance.theme.config
    val option = BlinkyThemeSpec.MediaColor
    val media = state.media
    val alpha by animateFloatAsState(
        targetValue = if (media == null || media.isPlaying) 1f else BlinkyMetrics.PAUSED_ALPHA,
        label = "BlinkyMediaAlpha",
    )

    Row(
        modifier = Modifier
            .alpha(alpha)
            .then(if (media != null) Modifier.onClick(ripple = false, action = onPlayPause) else Modifier)
            .padding(horizontal = BlinkyMetrics.LineHorizontalPadding, vertical = BlinkyMetrics.LineVerticalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BlinkyIcon(iconRes = R.drawable.ic_music_note_fill, option = option, config = config)

        BlinkyText(
            text = when {
                media == null -> stringResource(R.string.aod_blinky_no_media)
                media.artist == null -> media.title
                else -> stringResource(R.string.aod_blinky_media, media.title, media.artist)
            },
            brush = config.darkBrushOf(option, BlinkyMetrics.LINE_DARKEN),
            modifier = Modifier
                .padding(start = BlinkyMetrics.LineIconGap)
                .weight(1f, fill = false)
                .basicMarquee(iterations = 1),
        )
    }
}

@Composable
private fun BlinkyNotificationIcons(state: AodState) {
    val config = state.appearance.theme.config
    val option = BlinkyThemeSpec.StatusColor
    val brush = if (config.gradientOf(option) != null) config.darkBrushOf(option, BlinkyMetrics.LINE_DARKEN) else null

    AodStatusLine(
        battery = null,
        isFlashlightOn = false,
        notifications = state.notifications.copy(latest = null),
        color = lerp(config[option], Black, BlinkyMetrics.LINE_DARKEN),
        glyphHeight = BlinkyMetrics.IconRowHeight,
        fontFamily = FontFamilyNunito,
        modifier = Modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            layout(placeable.width, 0) { placeable.place(0, -BlinkyMetrics.IconRowLift.roundToPx()) }
        },
        brush = brush,
    )
}

@Composable
private fun BlinkyIcon(
    @DrawableRes iconRes: Int,
    option: AodColorOption,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        modifier = modifier
            .size(BlinkyMetrics.LineIconSize)
            .aodTint(config.gradientOf(option)?.toBrush()),
        tint = config[option],
    )
}

@Composable
private fun BlinkyText(
    text: String,
    brush: Brush,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            brush = brush,
            fontSize = BlinkyMetrics.LineTextSize,
            fontFamily = FontFamilyNunito,
            fontWeight = FontWeight.Medium,
        ),
        maxLines = 1,
    )
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodBlinkyPortraitPreview() {
    AodBlinkyPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.BLINKY)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodBlinkyLandscapePreview() {
    AodBlinkyLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.BLINKY)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
