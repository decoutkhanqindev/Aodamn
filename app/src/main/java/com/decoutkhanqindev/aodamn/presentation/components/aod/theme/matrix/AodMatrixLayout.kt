package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.matrix

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.CalendarEventUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix.AodMatrixDotValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix.MatrixGlyphs
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix.MatrixThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import java.time.Instant
import java.time.ZoneId
import kotlin.math.roundToInt

@Composable
fun AodMatrixPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    MatrixFace(state = state, onIntent = onIntent, isLandscape = false)
}

@Composable
fun AodMatrixLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    MatrixFace(state = state, onIntent = onIntent, isLandscape = true)
}

@Composable
private fun MatrixFace(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    isLandscape: Boolean,
) {
    val latest = state.notifications.latest
    val showsMedia = state.media != null && ((!state.isPreview && state.isMediaShown) || state.isPreviewShown(AodComponentValue.MEDIA))
    val showsLatest = (!state.isPreview && state.isLatestShown) || state.isPreviewFocused(AodComponentValue.NOTIFICATIONS)
    val showsDetails = state.notifications.icons.isNotEmpty() || (showsLatest && latest != null)
    val showsBattery = state.battery != null && state.extras.isBatteryEnabled
    val showsDate = state.extras.isDateEnabled
    val showsWeather = state.weather != null
    val event = state.events.firstOrNull()
    val showsEvent = event != null
    val toggleLatest = {
        if (latest != null || state.isLatestShown) {
            onIntent(AodIntent.ToggleLatestNotification(isShown = !state.isLatestShown))
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scale = MatrixMetrics.scale(maxWidth.value, maxHeight.value)
        val clockSize = state.appearance.theme.config[MatrixThemeSpec.ClockSize].dp * scale
        val clockHeight = clockSize * MatrixMetrics.DIGIT_HEIGHT
        val slidePx = with(LocalDensity.current) { (MatrixMetrics.SwapSlide * scale).roundToPx() }

        if (isLandscape) {
            MatrixLandscape(
                state = state,
                onIntent = onIntent,
                scale = scale,
                clockSize = clockSize,
                slidePx = slidePx,
                showsLatest = showsLatest,
                showsDetails = showsDetails,
                showsMedia = showsMedia,
                showsBattery = showsBattery,
                showsDate = showsDate,
                showsWeather = showsWeather,
                event = event,
                showsEvent = showsEvent,
                onToggleLatest = toggleLatest,
            )
        } else {
            MatrixPortrait(
                state = state,
                onIntent = onIntent,
                scale = scale,
                clockSize = clockSize,
                clockHeight = clockHeight,
                top = maxHeight * MatrixMetrics.PORTRAIT_ANCHOR - clockHeight - MatrixMetrics.AnchorBelowClock * scale,
                slidePx = slidePx,
                showsLatest = showsLatest,
                showsDetails = showsDetails,
                showsMedia = showsMedia,
                showsBattery = showsBattery,
                showsDate = showsDate,
                showsWeather = showsWeather,
                event = event,
                showsEvent = showsEvent,
                onToggleLatest = toggleLatest,
            )
        }
    }
}

@Composable
private fun MatrixPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    scale: Float,
    clockSize: Dp,
    clockHeight: Dp,
    top: Dp,
    slidePx: Int,
    showsLatest: Boolean,
    showsDetails: Boolean,
    showsMedia: Boolean,
    showsBattery: Boolean,
    showsDate: Boolean,
    showsWeather: Boolean,
    event: CalendarEventUiModel?,
    showsEvent: Boolean,
    onToggleLatest: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MatrixMetrics.SidePadding * scale)
                .offset(y = top),
            horizontalAlignment = Alignment.Start,
        ) {
            AodMatrixClock(state = state, size = clockSize)
            MatrixInfo(
                state = state,
                scale = scale,
                showsLatest = showsLatest,
                showsDetails = showsDetails,
                showsDate = showsDate,
                showsWeather = showsWeather,
                event = event,
                showsEvent = showsEvent,
                horizontalAlignment = Alignment.Start,
                onToggleLatest = onToggleLatest,
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = MatrixMetrics.SidePadding * scale),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(
                visible = showsMedia,
                enter = slideInVertically(tween(MatrixMetrics.SWAP_MILLIS)) { slidePx } + fadeIn(tween(MatrixMetrics.FADE_MILLIS)),
                exit = slideOutVertically(tween(MatrixMetrics.SWAP_MILLIS)) { slidePx } + fadeOut(tween(MatrixMetrics.FADE_MILLIS)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MatrixMetrics.PlayerBottom * scale),
                ) {
                    AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
                }
            }

            AnimatedVisibility(
                visible = showsBattery,
                enter = fadeIn(tween(MatrixMetrics.FADE_MILLIS)),
                exit = fadeOut(tween(MatrixMetrics.FADE_MILLIS)),
            ) {
                Box(modifier = Modifier.padding(bottom = MatrixMetrics.BatteryBottom * scale)) {
                    MatrixBattery(state = state, scale = scale)
                }
            }
        }
    }
}

@Composable
private fun MatrixLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    scale: Float,
    clockSize: Dp,
    slidePx: Int,
    showsLatest: Boolean,
    showsDetails: Boolean,
    showsMedia: Boolean,
    showsBattery: Boolean,
    showsDate: Boolean,
    showsWeather: Boolean,
    event: CalendarEventUiModel?,
    showsEvent: Boolean,
    onToggleLatest: () -> Unit,
) {
    val hasRight = showsDetails || showsMedia
    val leftFraction by animateFloatAsState(
        targetValue = if (hasRight) MatrixMetrics.LANDSCAPE_LEFT else 1f,
        animationSpec = tween(MatrixMetrics.SWAP_MILLIS),
        label = "MatrixLandscapeLeft",
    )

    Row(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .matrixLandscapeLeft { leftFraction }
                .fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AodMatrixClock(state = state, size = clockSize)
                MatrixLandscapeInfo(
                    state = state,
                    scale = scale,
                    showsDate = showsDate,
                    showsWeather = showsWeather,
                    event = event,
                    showsEvent = showsEvent,
                    showsBattery = showsBattery,
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = MatrixMetrics.SidePadding * scale),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            AnimatedVisibility(
                visible = showsDetails,
                enter = fadeIn(tween(MatrixMetrics.FADE_MILLIS)),
                exit = fadeOut(tween(MatrixMetrics.FADE_MILLIS)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(if (state.isClockLive) Modifier.onClick(ripple = false, action = onToggleLatest) else Modifier),
                ) {
                    MatrixDetails(state = state, scale = scale, showsLatest = showsLatest)
                    Spacer(
                        modifier = Modifier
                            .padding(top = MatrixMetrics.SeparatorTop * scale)
                            .fillMaxWidth()
                            .height(MatrixMetrics.Separator)
                            .background(
                                state.appearance.theme.config.darkBrushOf(
                                    MatrixThemeSpec.NotificationColor,
                                    MatrixMetrics.SEPARATOR_DARKEN,
                                ),
                            ),
                    )
                }
            }

            AnimatedVisibility(
                visible = showsMedia,
                enter = slideInVertically(tween(MatrixMetrics.SWAP_MILLIS)) { slidePx } + fadeIn(tween(MatrixMetrics.FADE_MILLIS)),
                exit = slideOutVertically(tween(MatrixMetrics.SWAP_MILLIS)) { slidePx } + fadeOut(tween(MatrixMetrics.FADE_MILLIS)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = if (showsDetails) MatrixMetrics.DetailsMediaGap * scale else 0.dp),
                ) {
                    AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
                }
            }
        }
    }
}

@Composable
private fun MatrixInfo(
    state: AodState,
    scale: Float,
    showsLatest: Boolean,
    showsDetails: Boolean,
    showsDate: Boolean,
    showsWeather: Boolean,
    event: CalendarEventUiModel?,
    showsEvent: Boolean,
    horizontalAlignment: Alignment.Horizontal,
    onToggleLatest: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (state.isClockLive) Modifier.onClick(ripple = false, action = onToggleLatest) else Modifier),
        horizontalAlignment = horizontalAlignment,
    ) {
        AnimatedVisibility(
            visible = showsDate,
            enter = fadeIn(tween(MatrixMetrics.FADE_MILLIS)) + expandVertically(tween(MatrixMetrics.FADE_MILLIS)),
            exit = fadeOut(tween(MatrixMetrics.FADE_MILLIS)) + shrinkVertically(tween(MatrixMetrics.FADE_MILLIS)),
        ) {
            Box(modifier = Modifier.padding(top = MatrixMetrics.ClockInfoGap * scale)) {
                MatrixDate(state = state, scale = scale)
            }
        }

        AnimatedVisibility(
            visible = showsWeather,
            enter = fadeIn(tween(MatrixMetrics.FADE_MILLIS)) + expandVertically(tween(MatrixMetrics.FADE_MILLIS)),
            exit = fadeOut(tween(MatrixMetrics.FADE_MILLIS)) + shrinkVertically(tween(MatrixMetrics.FADE_MILLIS)),
        ) {
            Box(modifier = Modifier.padding(top = (if (showsDate) MatrixMetrics.LineGap else MatrixMetrics.ClockInfoGap) * scale)) {
                MatrixWeather(state = state, scale = scale)
            }
        }

        AnimatedVisibility(
            visible = showsEvent,
            enter = fadeIn(tween(MatrixMetrics.FADE_MILLIS)) + expandVertically(tween(MatrixMetrics.FADE_MILLIS)),
            exit = fadeOut(tween(MatrixMetrics.FADE_MILLIS)) + shrinkVertically(tween(MatrixMetrics.FADE_MILLIS)),
        ) {
            Box(modifier = Modifier.padding(top = (if (showsDate || showsWeather) MatrixMetrics.LineGap else MatrixMetrics.ClockInfoGap) * scale)) {
                if (event != null) MatrixEvent(event = event, state = state, scale = scale)
            }
        }

        AnimatedVisibility(
            visible = showsDetails,
            enter = fadeIn(tween(MatrixMetrics.FADE_MILLIS)) + expandVertically(tween(MatrixMetrics.FADE_MILLIS)),
            exit = fadeOut(tween(MatrixMetrics.FADE_MILLIS)) + shrinkVertically(tween(MatrixMetrics.FADE_MILLIS)),
        ) {
            Box(
                modifier = Modifier.padding(
                    top = (if (showsDate || showsWeather || showsEvent) MatrixMetrics.DetailsGap else MatrixMetrics.ClockInfoGap) * scale,
                ),
            ) {
                MatrixDetails(state = state, scale = scale, showsLatest = showsLatest)
            }
        }
    }
}

@Composable
private fun MatrixLandscapeInfo(
    state: AodState,
    scale: Float,
    showsDate: Boolean,
    showsWeather: Boolean,
    event: CalendarEventUiModel?,
    showsEvent: Boolean,
    showsBattery: Boolean,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedVisibility(
            visible = showsDate,
            enter = fadeIn(tween(MatrixMetrics.FADE_MILLIS)) + expandVertically(tween(MatrixMetrics.FADE_MILLIS)),
            exit = fadeOut(tween(MatrixMetrics.FADE_MILLIS)) + shrinkVertically(tween(MatrixMetrics.FADE_MILLIS)),
        ) {
            Box(modifier = Modifier.padding(top = MatrixMetrics.ClockInfoGap * scale)) {
                MatrixDate(state = state, scale = scale)
            }
        }

        AnimatedVisibility(
            visible = showsWeather,
            enter = fadeIn(tween(MatrixMetrics.FADE_MILLIS)) + expandVertically(tween(MatrixMetrics.FADE_MILLIS)),
            exit = fadeOut(tween(MatrixMetrics.FADE_MILLIS)) + shrinkVertically(tween(MatrixMetrics.FADE_MILLIS)),
        ) {
            Box(modifier = Modifier.padding(top = (if (showsDate) MatrixMetrics.LineGap else MatrixMetrics.ClockInfoGap) * scale)) {
                MatrixWeather(state = state, scale = scale)
            }
        }

        AnimatedVisibility(
            visible = showsEvent,
            enter = fadeIn(tween(MatrixMetrics.FADE_MILLIS)) + expandVertically(tween(MatrixMetrics.FADE_MILLIS)),
            exit = fadeOut(tween(MatrixMetrics.FADE_MILLIS)) + shrinkVertically(tween(MatrixMetrics.FADE_MILLIS)),
        ) {
            Box(modifier = Modifier.padding(top = (if (showsDate || showsWeather) MatrixMetrics.LineGap else MatrixMetrics.ClockInfoGap) * scale)) {
                if (event != null) MatrixEvent(event = event, state = state, scale = scale)
            }
        }

        AnimatedVisibility(
            visible = showsBattery,
            enter = fadeIn(tween(MatrixMetrics.FADE_MILLIS)) + expandVertically(tween(MatrixMetrics.FADE_MILLIS)),
            exit = fadeOut(tween(MatrixMetrics.FADE_MILLIS)) + shrinkVertically(tween(MatrixMetrics.FADE_MILLIS)),
        ) {
            Box(modifier = Modifier.padding(top = (if (showsDate || showsWeather || showsEvent) MatrixMetrics.LineGap else MatrixMetrics.ClockInfoGap) * scale)) {
                MatrixBattery(state = state, scale = scale)
            }
        }
    }
}

@Composable
private fun MatrixDate(
    state: AodState,
    scale: Float,
) {
    val config = state.appearance.theme.config
    val dot = config[MatrixThemeSpec.DotStyle]
    val locale = LocalConfiguration.current.locales[0]
    val date = rememberZonedDateTime(state.nowMillis)
        .format(rememberFormatter(R.string.aod_matrix_date_pattern))
        .uppercase(locale)

    Text(
        text = date,
        style = TextStyle(
            brush = config.brushOf(MatrixThemeSpec.DateColor),
            fontSize = MatrixMetrics.DateSize * scale,
            fontFamily = dot.fontFamily,
            fontWeight = FontWeight.Normal,
        ),
        maxLines = 1,
    )
}

@Composable
private fun MatrixWeather(
    state: AodState,
    scale: Float,
) {
    val weather = state.weather ?: return
    val config = state.appearance.theme.config
    val dot = config[MatrixThemeSpec.DotStyle]
    val brush = config.brushOf(MatrixThemeSpec.DateColor)
    val context = LocalContext.current
    val bitmap = remember(weather.iconRes) {
        ContextCompat.getDrawable(context, weather.iconRes)?.toBitmap(
            width = MatrixMetrics.ICON_GRID * MatrixMetrics.ICON_SAMPLE_CELL,
            height = MatrixMetrics.ICON_GRID * MatrixMetrics.ICON_SAMPLE_CELL,
        )
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (bitmap != null) {
            MatrixSampledIcon(bitmap = bitmap, brush = brush, size = MatrixMetrics.LineIcon * scale, dot = dot)
            Spacer(modifier = Modifier.width(MatrixMetrics.LineIconGap * scale))
        }
        Text(
            text = stringResource(R.string.aod_weather_temperature, weather.temperature),
            style = TextStyle(
                brush = brush,
                fontSize = MatrixMetrics.LineSize * scale,
                fontFamily = dot.fontFamily,
                fontWeight = FontWeight.Normal,
            ),
            maxLines = 1,
        )
    }
}

@Composable
private fun MatrixEvent(
    event: CalendarEventUiModel,
    state: AodState,
    scale: Float,
) {
    val config = state.appearance.theme.config
    val dot = config[MatrixThemeSpec.DotStyle]
    val brush = config.brushOf(MatrixThemeSpec.EventsColor)
    val locale = LocalConfiguration.current.locales[0]
    val formatter = rememberTimeFormatter()
    val allDay = stringResource(R.string.aod_event_all_day)
    val time = remember(event, formatter, allDay) {
        if (event.isAllDay) {
            allDay
        } else {
            Instant.ofEpochMilli(event.beginMillis).atZone(ZoneId.systemDefault()).format(formatter)
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        MatrixGridIcon(
            rows = MatrixGlyphs.Calendar,
            brush = brush,
            size = MatrixMetrics.LineIcon * scale,
            dot = dot,
        )
        Spacer(modifier = Modifier.width(MatrixMetrics.LineIconGap * scale))
        Text(
            text = stringResource(R.string.aod_matrix_event, time, event.title).uppercase(locale),
            modifier = Modifier
                .weight(1f, fill = false)
                .basicMarquee(iterations = 1),
            style = TextStyle(
                brush = brush,
                fontSize = MatrixMetrics.LineSize * scale,
                fontFamily = dot.fontFamily,
                fontWeight = FontWeight.Normal,
            ),
            maxLines = 1,
        )
    }
}

@Composable
private fun MatrixDetails(
    state: AodState,
    scale: Float,
    showsLatest: Boolean,
) {
    val latest = state.notifications.latest
    val slide = with(LocalDensity.current) { (MatrixMetrics.SwapSlide * scale).roundToPx() }
    val config = state.appearance.theme.config
    val iconBrush = config.brushOf(MatrixThemeSpec.NotificationColor)
    val dot = config[MatrixThemeSpec.DotStyle]

    AnimatedContent(
        targetState = showsLatest && latest != null,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = MatrixMetrics.IconRowHeight * scale),
        transitionSpec = {
            if (targetState) {
                slideInVertically(tween(MatrixMetrics.SWAP_MILLIS)) { -slide } togetherWith
                    ExitTransition.None using SizeTransform(clip = false)
            } else {
                fadeIn(tween(MatrixMetrics.FADE_MILLIS)) togetherWith
                    (slideOutVertically(tween(MatrixMetrics.SWAP_MILLIS)) { -slide } + fadeOut(tween(MatrixMetrics.FADE_MILLIS))) using
                    SizeTransform(clip = false)
            }
        },
        contentAlignment = Alignment.CenterStart,
        label = "MatrixNotification",
    ) { isLatest ->
        if (isLatest && latest != null) {
            MatrixLatestLine(latest = latest, state = state, scale = scale)
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                state.notifications.icons.forEachIndexed { index, icon ->
                    key(icon.packageName) {
                        MatrixSampledIcon(
                            bitmap = icon.icon,
                            brush = iconBrush,
                            size = MatrixMetrics.NotificationIcon * scale,
                            dot = dot,
                            modifier = Modifier.padding(start = if (index > 0) MatrixMetrics.NotificationIconGap * scale else 0.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MatrixLatestLine(
    latest: NotificationContentUiModel,
    state: AodState,
    scale: Float,
) {
    val config = state.appearance.theme.config
    val dot = config[MatrixThemeSpec.DotStyle]
    val brush = config.brushOf(MatrixThemeSpec.NotificationColor)
    val locale = LocalConfiguration.current.locales[0]

    Row(verticalAlignment = Alignment.CenterVertically) {
        MatrixSampledIcon(
            bitmap = latest.icon,
            brush = brush,
            size = MatrixMetrics.NotificationIcon * scale,
            dot = dot,
        )
        Text(
            text = latest.lineText(R.string.aod_matrix_notification).uppercase(locale),
            modifier = Modifier
                .padding(start = MatrixMetrics.LatestIconGap * scale)
                .basicMarquee(iterations = 1),
            style = TextStyle(
                brush = brush,
                fontSize = MatrixMetrics.LineSize * scale,
                fontFamily = dot.fontFamily,
                fontWeight = FontWeight.Normal,
            ),
            maxLines = 1,
        )
    }
}

@Composable
private fun MatrixSampledIcon(
    bitmap: Bitmap,
    brush: Brush,
    size: Dp,
    dot: AodMatrixDotValue,
    modifier: Modifier = Modifier,
) {
    val cells = remember(bitmap) { sampleMatrixDots(bitmap, MatrixMetrics.ICON_GRID) }

    Spacer(
        modifier = modifier
            .size(size)
            .drawWithCache {
                val pitch = this.size.minDimension / MatrixMetrics.ICON_GRID
                val points = cells.toPoints(Offset.Zero, pitch)

                onDrawBehind {
                    drawMatrixPoints(points = points, pitch = pitch, dot = dot, brush = brush)
                }
            },
    )
}

@Composable
private fun MatrixGridIcon(
    rows: List<String>,
    brush: Brush,
    size: Dp,
    dot: AodMatrixDotValue,
) {
    val cells = remember(rows) { MatrixGlyphs.gridCells(rows) }
    val columnCount = rows.maxOf { it.length }

    Spacer(
        modifier = Modifier
            .size(size)
            .drawWithCache {
                val pitch = minOf(this.size.width / columnCount, this.size.height / rows.size)
                val origin = Offset(
                    x = (this.size.width - columnCount * pitch) / 2f,
                    y = (this.size.height - rows.size * pitch) / 2f,
                )
                val points = cells.toPoints(origin, pitch)

                onDrawBehind {
                    drawMatrixPoints(points = points, pitch = pitch, dot = dot, brush = brush)
                }
            },
    )
}

@Composable
private fun MatrixBattery(
    state: AodState,
    scale: Float,
) {
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled } ?: return
    val config = state.appearance.theme.config
    val dot = config[MatrixThemeSpec.DotStyle]
    val brush = config.brushOf(MatrixThemeSpec.BatteryColor)

    Row(
        modifier = Modifier.alpha(if (battery.isCharging) 1f else MatrixMetrics.BATTERY_IDLE_ALPHA),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (battery.isCharging) {
            MatrixGridIcon(
                rows = MatrixGlyphs.Bolt,
                brush = brush,
                size = MatrixMetrics.BoltHeight * scale,
                dot = dot,
            )
            Spacer(modifier = Modifier.width(MatrixMetrics.BoltHeight * scale * .4f))
        }
        Text(
            text = stringResource(R.string.value_percent, battery.percent),
            style = TextStyle(
                brush = brush,
                fontSize = MatrixMetrics.BatterySize * scale,
                fontFamily = dot.fontFamily,
                fontWeight = FontWeight.Normal,
                letterSpacing = MatrixMetrics.BatterySpacing,
            ),
            maxLines = 1,
        )
    }
}

private fun Modifier.matrixLandscapeLeft(fraction: () -> Float): Modifier = layout { measurable, constraints ->
    val width = (constraints.maxWidth * fraction()).roundToInt()
    val placeable = measurable.measure(constraints.copy(minWidth = width, maxWidth = width))
    layout(width, constraints.maxHeight) { placeable.place(0, 0) }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodMatrixPortraitPreview() {
    AodMatrixPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.MATRIX)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodMatrixLandscapePreview() {
    AodMatrixLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.MATRIX)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
