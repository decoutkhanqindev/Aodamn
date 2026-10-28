package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.clip

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodLatestLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.lineText
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.clip.ClipThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodClipPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val infoWidth = maxWidth - ClipMetrics.SideMargin * 2
        val playerWidth = maxWidth - ClipMetrics.PlayerMargin * 2

        AodClipHands(
            nowMillis = state.nowMillis,
            config = state.appearance.theme.config,
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = ClipMetrics.BottomMargin),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ClipInfo(
                state = state,
                dateSize = ClipMetrics.DateSize,
                maxWidth = infoWidth,
            )

            ClipPlayer(
                state = state,
                width = playerWidth,
                onIntent = onIntent,
                modifier = Modifier.padding(top = ClipMetrics.MediaGap),
            )
        }
    }
}

@Composable
fun AodClipLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val config = state.appearance.theme.config
    val hasMedia = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val radius = min(maxWidth, maxHeight) / 2 * (config[ClipThemeSpec.Size] / ClipMetrics.SIZE_DIVISOR)
        val zone = maxWidth / 2 - radius * ClipMetrics.MINUTE_END
        val inner = zone - ClipMetrics.ZoneInset * 2

        AodClipHands(
            nowMillis = state.nowMillis,
            config = config,
            modifier = Modifier.fillMaxSize(),
        )

        Crossfade(targetState = hasMedia, label = "AodClipLandscape") { isMediaShown ->
            Row(modifier = Modifier.fillMaxSize()) {
                ClipZone(width = zone) {
                    if (isMediaShown) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ClipInfo(state = state, dateSize = ClipMetrics.LandscapeDateSize, maxWidth = inner)
                        }
                    } else {
                        ClipDateRow(state = state, dateSize = ClipMetrics.LandscapeDateSize)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                ClipZone(width = zone) {
                    if (isMediaShown) {
                        ClipPlayer(state = state, width = inner, onIntent = onIntent)
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ClipStatus(state = state, maxWidth = inner)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClipZone(
    width: Dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(width)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
private fun ClipInfo(
    state: AodState,
    dateSize: TextUnit,
    maxWidth: Dp,
) {
    ClipDateRow(state = state, dateSize = dateSize)

    ClipStatus(
        state = state,
        maxWidth = maxWidth,
        modifier = Modifier.padding(top = if (state.isDateRowShown()) ClipMetrics.DateGap else 0.dp),
    )
}

@Composable
private fun ClipStatus(
    state: AodState,
    maxWidth: Dp,
    modifier: Modifier = Modifier,
) {
    val color = state.appearance.theme.config[ClipThemeSpec.StatusColor]
    val brush = state.appearance.theme.config.gradientOf(ClipThemeSpec.StatusColor)?.toBrush()
    val latest = state.notifications.latest

    AodStatusLine(
        battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
        isFlashlightOn = state.isFlashlightOn,
        notifications = state.notifications,
        color = color,
        glyphHeight = ClipMetrics.StatusGlyph,
        fontFamily = ClipThemeSpec.Font,
        modifier = modifier,
        gapScale = ClipMetrics.STATUS_GAP_SCALE,
        brush = brush,
    )

    AnimatedVisibility(visible = latest != null) {
        if (latest != null) {
            AodLatestLine(
                text = latest.lineText(R.string.aod_clip_notification),
                icon = latest.icon,
                color = color,
                brush = brush,
                style = ClipMetrics.LatestLine,
                modifier = Modifier
                    .padding(top = ClipMetrics.LatestGap)
                    .widthIn(max = maxWidth),
            )
        }
    }
}

@Composable
private fun ClipDateRow(
    state: AodState,
    dateSize: TextUnit,
) {
    val isWeatherShown = AodComponentValue.WEATHER in state.appearance.theme.value.spec.components
    val components = state.appearance.components

    Row(verticalAlignment = Alignment.CenterVertically) {
        AnimatedVisibility(visible = state.extras.isDateEnabled) {
            ClipDate(
                nowMillis = state.nowMillis,
                brush = state.appearance.theme.config.brushOf(ClipThemeSpec.DateColor),
                size = dateSize,
                modifier = Modifier.padding(
                    end = if (isWeatherShown && state.weather != null) ClipMetrics.WeatherGap else 0.dp,
                ),
            )
        }

        if (isWeatherShown) {
            AodWeather(
                weather = state.weather,
                style = components.styleOf(AodComponentValue.WEATHER),
                layout = components.weatherLayout,
            )
        }
    }
}

@Composable
private fun ClipDate(
    nowMillis: Long,
    brush: Brush,
    size: TextUnit,
    modifier: Modifier = Modifier,
) {
    val date = rememberZonedDateTime(nowMillis).format(rememberFormatter(R.string.aod_clip_date_pattern)).uppercase()

    Text(
        text = date,
        modifier = modifier,
        style = LocalTextStyle.current.merge(TextStyle(brush = brush)),
        fontSize = size,
        fontWeight = FontWeight.SemiBold,
        fontFamily = ClipThemeSpec.Font,
        letterSpacing = ClipMetrics.DateSpacing,
        maxLines = 1,
        softWrap = false,
    )
}

@Composable
private fun ClipPlayer(
    state: AodState,
    width: Dp,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.width(width)) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }
    }
}

private fun AodState.isDateRowShown(): Boolean =
    extras.isDateEnabled || (weather != null && AodComponentValue.WEATHER in appearance.theme.value.spec.components)

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodClipPortraitPreview() {
    AodClipPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.CLIP)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            media = MediaUiModel(
                title = "That Cool Song",
                artist = "It's Artist",
                isPlaying = true,
                canSkipToPrevious = true,
                canSkipToNext = true,
            ),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodClipLandscapePreview() {
    AodClipLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.CLIP)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
