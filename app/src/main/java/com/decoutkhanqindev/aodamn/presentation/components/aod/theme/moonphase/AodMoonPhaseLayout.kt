package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.moonphase

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.em
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodLatestLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.lineText
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.moonphase.MoonPhaseThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun AodMoonPhasePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    MoonPhaseFace(
        state = state,
        onIntent = onIntent,
        isLandscape = false,
    )
}

@Composable
fun AodMoonPhaseLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    MoonPhaseFace(
        state = state,
        onIntent = onIntent,
        isLandscape = true,
    )
}

@Composable
private fun MoonPhaseFace(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    isLandscape: Boolean,
) {
    val latest = state.notifications.latest
    val hasMedia = state.media != null
    val latestStamp = latest?.let { content ->
        listOf(content.key, content.title, content.text)
    }
    var seenLatest by remember { mutableStateOf(latestStamp) }

    SideEffect(latestStamp) {
        if (latestStamp != seenLatest) {
            seenLatest = latestStamp
            if (latestStamp != null) {
                if (!state.isPreview) onIntent(AodIntent.ToggleMediaControls(isShown = false))
            }
        }
    }

    val slidePx = with(LocalDensity.current) {
        MoonPhaseMetrics.SwapSlide.roundToPx()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AodMoonPhaseSky(
            nowMillis = state.nowMillis,
            isLive = state.isClockLive,
            isStill = state.isStill,
            isLandscape = isLandscape,
            config = state.appearance.theme.config,
        )

        MoonPhaseHeader(
            state = state,
            isLandscape = isLandscape,
        )

        AnimatedContent(
            targetState = hasMedia && ((!state.isPreview && state.isMediaShown) || state.isPreviewFocused(AodComponentValue.MEDIA)),
            modifier = Modifier.moonBand(isLandscape),
            transitionSpec = {
                if (targetState) {
                    slideInVertically(
                        animationSpec = tween(
                            durationMillis = MoonPhaseMetrics.SWAP_MILLIS,
                            easing = LinearOutSlowInEasing,
                        ),
                    ) { slidePx } togetherWith ExitTransition.None using SizeTransform(clip = false)
                } else {
                    fadeIn(
                        animationSpec = tween(MoonPhaseMetrics.FADE_MILLIS),
                    ) togetherWith (
                        slideOutVertically(
                            animationSpec = tween(MoonPhaseMetrics.SWAP_MILLIS),
                        ) { slidePx } + fadeOut(
                            animationSpec = tween(MoonPhaseMetrics.SWAP_MILLIS),
                        )
                    ) using SizeTransform(clip = false)
                }
            },
            contentAlignment = if (isLandscape) Alignment.Center else Alignment.TopCenter,
            label = "MoonPhaseBottom",
        ) { isMedia ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MoonPhaseMetrics.ScrimBrush)
                    .padding(vertical = MoonPhaseMetrics.BlockPadding),
                contentAlignment = Alignment.Center,
            ) {
                if (isMedia) {
                    AodPanelBlock(
                        block = AodPanelBlockValue.MEDIA,
                        state = state,
                        onIntent = onIntent,
                    )
                } else {
                    MoonPhaseDetails(
                        state = state,
                        isLatestShown = (!state.isPreview && state.isLatestShown) || state.isPreviewFocused(AodComponentValue.NOTIFICATIONS),
                        onTap = { if (latest != null || state.isLatestShown) onIntent(AodIntent.ToggleLatestNotification(isShown = !state.isLatestShown)) },
                    )
                }
            }
        }
    }
}

private fun Modifier.moonBand(isLandscape: Boolean): Modifier = layout { measurable, constraints ->
    val screenWidth = constraints.maxWidth
    val screenHeight = constraints.maxHeight
    val bandWidth = if (isLandscape) {
        minOf(
            screenWidth,
            screenHeight + MoonPhaseMetrics.LandscapeBandExtra.roundToPx(),
        )
    } else {
        screenWidth
    }
    val placeable = measurable.measure(
        constraints.copy(
            minWidth = bandWidth,
            maxWidth = bandWidth,
            minHeight = 0,
        ),
    )
    layout(screenWidth, screenHeight) {
        val x = if (isLandscape) {
            (screenWidth * MoonPhaseMetrics.LANDSCAPE_HEADER_X - placeable.width / 2f).roundToInt()
        } else {
            0
        }
        val y = if (isLandscape) {
            (screenHeight * MoonPhaseMetrics.LANDSCAPE_BAND_Y - placeable.height / 2f).roundToInt()
        } else {
            (screenHeight * MoonPhaseMetrics.PORTRAIT_BAND_TOP).roundToInt()
        }
        placeable.place(x, y)
    }
}

@Composable
private fun MoonPhaseHeader(
    state: AodState,
    isLandscape: Boolean,
) {
    var previewMillis by remember { mutableLongStateOf(state.nowMillis) }

    if (state.isClockLive && state.isPreview) {
        LaunchedEffect(Unit) {
            while (true) {
                val current = System.currentTimeMillis()
                previewMillis = current
                delay(
                    MoonPhaseMetrics.MINUTE_MILLIS -
                        current % MoonPhaseMetrics.MINUTE_MILLIS +
                        MoonPhaseMetrics.TICK_SLACK_MILLIS,
                )
            }
        }
    }

    val displayMillis = if (state.isClockLive && state.isPreview) {
        previewMillis
    } else {
        state.nowMillis
    }
    val config = state.appearance.theme.config
    val components = state.appearance.components
    val time = rememberZonedDateTime(displayMillis)
    val locale = LocalConfiguration.current.locales[0]
    val shownTime = time.format(
        rememberFormatter(
            if (is24HourFormat()) {
                R.string.aod_moon_phase_time_24h
            } else {
                R.string.aod_moon_phase_time_12h
            },
        ),
    ).uppercase(locale)
    val timeDescription = time.format(rememberTimeFormatter())
    val date = time.format(
        rememberFormatter(R.string.aod_moon_phase_date_pattern),
    ).uppercase(locale)
    val scale = config[MoonPhaseThemeSpec.ClockSize] / 100f
    val opening = remember(state.isStill) {
        Animatable(if (state.isStill) 1f else 0f)
    }

    if (!state.isStill) {
        LaunchedEffect(opening) {
            opening.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = MoonPhaseMetrics.OPEN_MILLIS,
                    easing = FastOutSlowInEasing,
                ),
            )
        }
    }

    val shift = with(LocalDensity.current) {
        MoonPhaseMetrics.OpenShift.toPx()
    }

    Layout(
        modifier = Modifier.fillMaxSize(),
        content = {
            Spacer(modifier = Modifier.background(MoonPhaseMetrics.ScrimBrush))
            Text(
                text = shownTime,
                modifier = Modifier
                    .clearAndSetSemantics { contentDescription = timeDescription }
                    .graphicsLayer {
                        translationY = (1f - opening.value) * shift
                    },
                style = TextStyle(
                    brush = config.brushOf(MoonPhaseThemeSpec.ClockColor),
                    fontSize = MoonPhaseMetrics.ClockEm * scale,
                    fontFamily = MoonPhaseThemeSpec.Font,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = (MoonPhaseMetrics.CLOCK_SPACING * scale).em,
                ),
                maxLines = 1,
            )
            Row(
                modifier = Modifier.graphicsLayer {
                    translationY = -(1f - opening.value) * shift
                },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AodWeather(
                    weather = state.weather,
                    style = components.styleOf(AodComponentValue.WEATHER),
                    layout = components.weatherLayout,
                )
                if (state.weather != null && state.extras.isDateEnabled) {
                    Spacer(modifier = Modifier.width(MoonPhaseMetrics.DateWeatherGap))
                }
                if (state.extras.isDateEnabled) {
                    Text(
                        text = date,
                        style = TextStyle(
                            brush = config.brushOf(MoonPhaseThemeSpec.DateColor),
                            fontSize = MoonPhaseMetrics.DateEm * scale,
                            fontFamily = MoonPhaseThemeSpec.Font,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = (MoonPhaseMetrics.DATE_SPACING * scale).em,
                        ),
                        maxLines = 1,
                    )
                }
            }
        },
    ) { measurables, constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val clock = measurables[1].measure(looseConstraints)
        val dateRow = measurables[2].measure(looseConstraints)
        val clockBox = (MoonPhaseMetrics.ClockEm * scale).toPx() * MoonPhaseMetrics.LINE_BOX
        val dateBox = (MoonPhaseMetrics.DateEm * scale).toPx() * MoonPhaseMetrics.LINE_BOX
        val padding = MoonPhaseMetrics.BlockPadding.toPx()
        val gap = (
            if (isLandscape) MoonPhaseMetrics.LandscapeGap else MoonPhaseMetrics.PortraitGap
        ).toPx() * scale
        val block = (padding * 2f + clockBox + gap + dateBox).roundToInt()
        val scrim = measurables[0].measure(
            Constraints.fixed(
                width = clock.width + (padding * 2f).roundToInt(),
                height = block,
            ),
        )
        val centerX = if (isLandscape) {
            constraints.maxWidth * MoonPhaseMetrics.LANDSCAPE_HEADER_X
        } else {
            constraints.maxWidth / 2f
        }
        val top = if (isLandscape) {
            constraints.maxHeight * MoonPhaseMetrics.LANDSCAPE_HEADER_Y - block / 2f
        } else {
            constraints.maxHeight * MoonPhaseMetrics.PORTRAIT_HEADER_BOTTOM - block
        }
        val clockX = centerX - clock.width / 2f
        val clockY = top + padding + (clockBox - clock.height) / 2f
        val dateX = if (isLandscape) {
            centerX - dateRow.width / 2f
        } else {
            clockX + clock.width - dateRow.width
        }
        val dateY = top + padding + clockBox + gap + (dateBox - dateRow.height) / 2f
        val scrimX = clockX - padding

        layout(constraints.maxWidth, constraints.maxHeight) {
            scrim.place(scrimX.roundToInt(), top.roundToInt())
            clock.place(clockX.roundToInt(), clockY.roundToInt())
            dateRow.place(dateX.roundToInt(), dateY.roundToInt())
        }
    }
}

@Composable
private fun MoonPhaseDetails(
    state: AodState,
    isLatestShown: Boolean,
    onTap: () -> Unit,
) {
    val config = state.appearance.theme.config
    val latest = state.notifications.latest
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val showsLatest = isLatestShown && latest != null
    val statusColor = config[MoonPhaseThemeSpec.StatusColor]
    val statusBrush = if (config.gradientOf(MoonPhaseThemeSpec.StatusColor) != null) {
        config.brushOf(MoonPhaseThemeSpec.StatusColor)
    } else {
        null
    }
    val slide = with(LocalDensity.current) {
        MoonPhaseMetrics.SwapSlide.roundToPx()
    }

    AnimatedContent(
        targetState = showsLatest,
        modifier = Modifier.then(
            if (state.isClockLive) {
                Modifier.onClick(ripple = false, action = onTap)
            } else {
                Modifier
            },
        ),
        transitionSpec = {
            if (targetState) {
                slideInVertically(
                    animationSpec = tween(MoonPhaseMetrics.SWAP_MILLIS),
                ) { -slide } togetherWith ExitTransition.None using SizeTransform(clip = false)
            } else {
                fadeIn(
                    animationSpec = tween(MoonPhaseMetrics.FADE_MILLIS),
                ) togetherWith (
                    slideOutVertically(
                        animationSpec = tween(MoonPhaseMetrics.SWAP_MILLIS),
                    ) { -slide } + fadeOut(
                        animationSpec = tween(MoonPhaseMetrics.SWAP_MILLIS),
                    )
                ) using SizeTransform(clip = false)
            }
        },
        contentAlignment = Alignment.Center,
        label = "MoonPhaseStatus",
    ) { isLatest ->
        if (isLatest && latest != null) {
            AodLatestLine(
                text = latest.lineText(R.string.aod_moon_phase_notification),
                icon = latest.icon,
                color = statusColor,
                brush = statusBrush,
                style = MoonPhaseMetrics.LatestLine,
                modifier = Modifier.padding(horizontal = MoonPhaseMetrics.LatestMargin),
            )
        } else {
            AodStatusLine(
                battery = battery,
                isFlashlightOn = state.isFlashlightOn,
                notifications = state.notifications.copy(latest = null),
                color = statusColor,
                glyphHeight = MoonPhaseMetrics.StatusGlyph,
                fontFamily = MoonPhaseThemeSpec.Font,
                gapScale = MoonPhaseMetrics.GAP_SCALE,
                brush = statusBrush,
            )
        }
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodMoonPhasePortraitPreview() {
    AodMoonPhasePortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(
                theme = AodThemeUiModel(value = AodThemeValue.MOON_PHASE),
            ),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodMoonPhaseLandscapePreview() {
    AodMoonPhaseLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(
                theme = AodThemeUiModel(value = AodThemeValue.MOON_PHASE),
            ),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
