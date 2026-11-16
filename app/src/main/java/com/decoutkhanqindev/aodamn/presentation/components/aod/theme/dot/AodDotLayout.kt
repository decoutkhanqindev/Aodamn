package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.dot

import android.graphics.Bitmap
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
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.lineText
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.dot.DotThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyTiny5
import kotlin.math.roundToInt

@Composable
fun AodDotPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    DotFace(state = state, onIntent = onIntent, isLandscape = false)
}

@Composable
fun AodDotLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    DotFace(state = state, onIntent = onIntent, isLandscape = true)
}

@Composable
private fun DotFace(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    isLandscape: Boolean,
) {
    val latest = state.notifications.latest
    val showsMedia = state.media != null && ((!state.isPreview && state.isMediaShown) || state.isPreviewShown(AodComponentValue.MEDIA))
    val showsLatest = (!state.isPreview && state.isLatestShown) || state.isPreviewFocused(AodComponentValue.NOTIFICATIONS)
    val showsDetails = state.notifications.icons.isNotEmpty() || (showsLatest && latest != null)
    val showsBattery = state.battery != null && state.extras.isBatteryEnabled
    val hasDateRow = state.extras.isDateEnabled || state.weather != null
    val toggleLatest = {
        if (latest != null || state.isLatestShown) {
            onIntent(AodIntent.ToggleLatestNotification(isShown = !state.isLatestShown))
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scale = DotMetrics.scale(maxWidth.value, maxHeight.value)
        val slidePx = with(LocalDensity.current) { (DotMetrics.SwapSlide * scale).roundToPx() }

        if (isLandscape) {
            DotLandscape(
                state = state,
                onIntent = onIntent,
                scale = scale,
                slidePx = slidePx,
                showsLatest = showsLatest,
                showsDetails = showsDetails,
                showsMedia = showsMedia,
                showsBattery = showsBattery,
                hasDateRow = hasDateRow,
                onToggleLatest = toggleLatest,
            )
        } else {
            DotPortrait(
                state = state,
                onIntent = onIntent,
                scale = scale,
                slidePx = slidePx,
                showsLatest = showsLatest,
                showsDetails = showsDetails,
                showsMedia = showsMedia,
                showsBattery = showsBattery,
                hasDateRow = hasDateRow,
                onToggleLatest = toggleLatest,
            )
        }
    }
}

@Composable
private fun DotPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    scale: Float,
    slidePx: Int,
    showsLatest: Boolean,
    showsDetails: Boolean,
    showsMedia: Boolean,
    showsBattery: Boolean,
    hasDateRow: Boolean,
    onToggleLatest: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(DotMetrics.PORTRAIT_TOP)
                .fillMaxWidth()
                .padding(horizontal = DotMetrics.SidePadding * scale),
            contentAlignment = Alignment.BottomCenter,
        ) {
            DotDateAndDetails(
                state = state,
                scale = scale,
                showsLatest = showsLatest,
                showsDetails = showsDetails,
                hasDateRow = hasDateRow,
                horizontalAlignment = Alignment.CenterHorizontally,
                onToggleLatest = onToggleLatest,
            )
        }

        Box(
            modifier = Modifier
                .weight(DotMetrics.PORTRAIT_CLOCK)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            AodDotClock(
                state = state,
                size = state.appearance.theme.config[DotThemeSpec.ClockSize].dp * scale,
                scale = scale,
            )
        }

        Column(
            modifier = Modifier
                .weight(DotMetrics.PORTRAIT_BOTTOM)
                .fillMaxWidth()
                .padding(horizontal = DotMetrics.SidePadding * scale),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(
                visible = showsMedia,
                enter = slideInVertically(tween(DotMetrics.SWAP_MILLIS)) { slidePx } + fadeIn(tween(DotMetrics.FADE_MILLIS)),
                exit = slideOutVertically(tween(DotMetrics.SWAP_MILLIS)) { slidePx } + fadeOut(tween(DotMetrics.FADE_MILLIS)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (showsBattery) DotMetrics.PlayerBottom * scale else 0.dp),
                ) {
                    AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
                }
            }

            AnimatedVisibility(
                visible = showsBattery,
                enter = fadeIn(tween(DotMetrics.FADE_MILLIS)),
                exit = fadeOut(tween(DotMetrics.FADE_MILLIS)),
            ) {
                Box(modifier = Modifier.padding(bottom = DotMetrics.BatteryBottom * scale)) {
                    DotBattery(state = state, scale = scale)
                }
            }
        }
    }
}

@Composable
private fun DotLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    scale: Float,
    slidePx: Int,
    showsLatest: Boolean,
    showsDetails: Boolean,
    showsMedia: Boolean,
    showsBattery: Boolean,
    hasDateRow: Boolean,
    onToggleLatest: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = DotMetrics.SidePadding * scale),
            contentAlignment = Alignment.CenterEnd,
        ) {
            DotDateAndDetails(
                state = state,
                scale = scale,
                showsLatest = showsLatest,
                showsDetails = showsDetails,
                hasDateRow = hasDateRow,
                horizontalAlignment = Alignment.End,
                onToggleLatest = onToggleLatest,
            )
        }

        Spacer(modifier = Modifier.width(DotMetrics.LandscapeGap * scale))

        AodDotClock(
            state = state,
            size = state.appearance.theme.config[DotThemeSpec.ClockSize].dp * scale * DotMetrics.LANDSCAPE_CLOCK,
            scale = scale,
        )

        Spacer(modifier = Modifier.width(DotMetrics.LandscapeGap * scale))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = DotMetrics.SidePadding * scale),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            AnimatedVisibility(
                visible = showsMedia,
                enter = slideInVertically(tween(DotMetrics.SWAP_MILLIS)) { slidePx } + fadeIn(tween(DotMetrics.FADE_MILLIS)),
                exit = slideOutVertically(tween(DotMetrics.SWAP_MILLIS)) { slidePx } + fadeOut(tween(DotMetrics.FADE_MILLIS)),
            ) {
                AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
            }

            AnimatedVisibility(
                visible = showsBattery,
                enter = fadeIn(tween(DotMetrics.FADE_MILLIS)),
                exit = fadeOut(tween(DotMetrics.FADE_MILLIS)),
            ) {
                Column {
                    if (showsMedia) Spacer(modifier = Modifier.height(DotMetrics.LandscapeLineGap * scale))
                    DotBattery(state = state, scale = scale)
                }
            }
        }
    }
}

@Composable
private fun DotDateAndDetails(
    state: AodState,
    scale: Float,
    showsLatest: Boolean,
    showsDetails: Boolean,
    hasDateRow: Boolean,
    horizontalAlignment: Alignment.Horizontal,
    onToggleLatest: () -> Unit,
) {
    Column(
        modifier = Modifier.then(
            if (state.isClockLive) Modifier.onClick(ripple = false, action = onToggleLatest) else Modifier,
        ),
        horizontalAlignment = horizontalAlignment,
    ) {
        if (hasDateRow) {
            DotDateRow(state = state, scale = scale, horizontalAlignment = horizontalAlignment)
        }

        AnimatedVisibility(
            visible = showsDetails,
            enter = fadeIn(tween(DotMetrics.FADE_MILLIS)),
            exit = fadeOut(tween(DotMetrics.FADE_MILLIS)),
        ) {
            Box(modifier = Modifier.padding(top = if (hasDateRow) DotMetrics.DateDetailsGap * scale else 0.dp)) {
                DotDetails(state = state, scale = scale, showsLatest = showsLatest, horizontalAlignment = horizontalAlignment)
            }
        }
    }
}

@Composable
private fun DotDateRow(
    state: AodState,
    scale: Float,
    horizontalAlignment: Alignment.Horizontal,
) {
    val date = if (state.extras.isDateEnabled) {
        rememberZonedDateTime(state.nowMillis).format(rememberFormatter(R.string.aod_dot_date_pattern)).uppercase(LocalConfiguration.current.locales[0])
    } else {
        null
    }
    val weather = state.weather
    val brush = state.appearance.theme.config.brushOf(DotThemeSpec.DateColor)
    val style = TextStyle(
        brush = brush,
        fontSize = DotMetrics.DateSize * scale,
        fontFamily = FontFamilyTiny5,
        fontWeight = FontWeight.Normal,
    )
    val arrangement = if (horizontalAlignment == Alignment.End) Arrangement.End else Arrangement.Center

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (date != null) Text(text = date, style = style, maxLines = 1)
        if (date != null && weather != null) {
            Text(text = stringResource(R.string.aod_dot_separator), style = style, maxLines = 1)
        }
        if (weather != null) {
            val context = LocalContext.current
            val bitmap = remember(weather.iconRes) {
                ContextCompat.getDrawable(context, weather.iconRes)?.toBitmap(
                    width = DotMetrics.ICON_GRID * DotMetrics.ICON_SAMPLE_CELL,
                    height = DotMetrics.ICON_GRID * DotMetrics.ICON_SAMPLE_CELL,
                )
            }
            if (bitmap != null) {
                DotSampledIcon(bitmap = bitmap, brush = brush, size = DotMetrics.NotificationIcon * scale)
            }
            Text(
                text = stringResource(R.string.aod_weather_temperature, weather.temperature),
                style = style,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun DotDetails(
    state: AodState,
    scale: Float,
    showsLatest: Boolean,
    horizontalAlignment: Alignment.Horizontal,
) {
    val latest = state.notifications.latest
    val slide = with(LocalDensity.current) { (DotMetrics.SwapSlide * scale).roundToPx() }

    AnimatedContent(
        targetState = showsLatest && latest != null,
        modifier = Modifier.fillMaxWidth(),
        transitionSpec = {
            if (targetState) {
                slideInVertically(tween(DotMetrics.SWAP_MILLIS)) { -slide } togetherWith
                    ExitTransition.None using SizeTransform(clip = false)
            } else {
                fadeIn(tween(DotMetrics.FADE_MILLIS)) togetherWith
                    (slideOutVertically(tween(DotMetrics.SWAP_MILLIS)) { -slide } + fadeOut(tween(DotMetrics.FADE_MILLIS))) using
                    SizeTransform(clip = false)
            }
        },
        contentAlignment = if (horizontalAlignment == Alignment.End) Alignment.CenterEnd else Alignment.Center,
        label = "DotNotification",
    ) { isLatest ->
        if (isLatest && latest != null) {
            DotLatestLine(latest = latest, state = state, scale = scale)
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (horizontalAlignment == Alignment.End) Arrangement.End else Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                state.notifications.icons.forEachIndexed { index, icon ->
                    key(icon.packageName) {
                        DotSampledIcon(
                            bitmap = icon.icon,
                            brush = state.appearance.theme.config.brushOf(DotThemeSpec.NotificationColor),
                            size = DotMetrics.NotificationIcon * scale,
                            modifier = Modifier.padding(start = if (index > 0) DotMetrics.NotificationIconGap * scale else 0.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DotLatestLine(
    latest: NotificationContentUiModel,
    state: AodState,
    scale: Float,
) {
    val brush = state.appearance.theme.config.brushOf(DotThemeSpec.NotificationColor)
    val locale = LocalConfiguration.current.locales[0]

    Row(verticalAlignment = Alignment.CenterVertically) {
        DotSampledIcon(
            bitmap = latest.icon,
            brush = brush,
            size = DotMetrics.NotificationIcon * scale,
        )
        Text(
            text = latest.lineText(R.string.aod_dot_notification).uppercase(locale),
            modifier = Modifier
                .padding(start = DotMetrics.LatestIconGap * scale)
                .basicMarquee(iterations = 1),
            style = TextStyle(
                brush = brush,
                fontSize = DotMetrics.LatestSize * scale,
                fontFamily = FontFamilyTiny5,
                fontWeight = FontWeight.Normal,
            ),
            maxLines = 1,
        )
    }
}

@Composable
private fun DotSampledIcon(
    bitmap: Bitmap,
    brush: Brush,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    val dots = remember(bitmap) { sampleDots(bitmap, DotMetrics.ICON_GRID, DotMetrics.ICON_ALPHA_THRESHOLD) }

    Spacer(
        modifier = modifier
            .size(size)
            .drawWithCache {
                val pitch = this.size.minDimension / DotMetrics.ICON_GRID
                val radius = pitch * DotMetrics.ICON_DOT / 2f
                val start = radius

                onDrawBehind {
                    dots.forEachIndexed { index, isVisible ->
                        if (isVisible) {
                            drawCircle(
                                brush = brush,
                                radius = radius,
                                center = Offset(start + index % DotMetrics.ICON_GRID * pitch, start + index / DotMetrics.ICON_GRID * pitch),
                            )
                        }
                    }
                }
            },
    )
}

@Composable
private fun DotBattery(
    state: AodState,
    scale: Float,
) {
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled } ?: return
    val brush = state.appearance.theme.config.brushOf(DotThemeSpec.BatteryColor)

    Row(
        modifier = Modifier.alpha(if (battery.isCharging) 1f else DotMetrics.BATTERY_IDLE_ALPHA),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (battery.isCharging) {
            DotBolt(brush = brush, scale = scale)
            Spacer(modifier = Modifier.width(DotMetrics.BoltHeight * scale * .4f))
        }
        Text(
            text = stringResource(R.string.value_percent, battery.percent),
            style = TextStyle(
                brush = brush,
                fontSize = DotMetrics.BatterySize * scale,
                fontFamily = FontFamilyTiny5,
                fontWeight = FontWeight.Normal,
                letterSpacing = DotMetrics.BatterySpacing,
            ),
            maxLines = 1,
        )
    }
}

@Composable
private fun DotBolt(
    brush: Brush,
    scale: Float,
) {
    Spacer(
        modifier = Modifier
            .width(DotMetrics.BoltHeight * scale * 5f / 7f)
            .height(DotMetrics.BoltHeight * scale)
            .drawWithCache {
                val pitch = size.height / 7f
                val radius = pitch * DotMetrics.ICON_DOT / 2f

                onDrawBehind {
                    drawDotGrid(
                        rows = DotGrids.Bolt,
                        topLeft = Offset(radius, radius),
                        pitch = pitch,
                        brush = brush,
                        dotRatio = DotMetrics.ICON_DOT,
                    )
                }
            },
    )
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodDotPortraitPreview() {
    AodDotPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.DOT)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodDotLandscapePreview() {
    AodDotLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.DOT)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
