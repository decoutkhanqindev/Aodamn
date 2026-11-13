package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.meridian

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodZoneUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.meridian.AodMeridianStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.meridian.MeridianThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import kotlinx.coroutines.delay

@Composable
fun AodMeridianCards(
    state: AodState,
    displayMillis: Long,
    scale: Float,
    modifier: Modifier = Modifier,
) {
    val local = rememberZonedDateTime(displayMillis)
    val config = state.appearance.theme.config
    val zoneId = config[MeridianThemeSpec.Zone]
    val other = remember(local, zoneId) { local.withZoneSameInstant(MeridianMetrics.zoneIdOf(zoneId)) }
    val formatter = rememberFormatter(if (is24HourFormat()) R.string.aod_time_pattern_24h else R.string.aod_meridian_time_12h)
    val locale = LocalConfiguration.current.locales[0]
    val localTime = local.format(formatter)
    val otherTime = other.format(formatter)
    val localLabel = stringResource(R.string.aod_meridian_local).uppercase(locale)
    val otherLabel = remember(zoneId, locale) { AodZoneUiModel.cityOf(zoneId, locale).uppercase(locale) }

    Row(
        modifier = modifier.semantics { contentDescription = "$localTime $localLabel, $otherTime $otherLabel" },
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            MeridianCard(
                index = 0,
                hour = local.hour,
                minute = local.minute,
                time = localTime,
                label = localLabel,
                style = config[MeridianThemeSpec.Style],
                config = config,
                scale = scale,
            )
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            MeridianCard(
                index = 1,
                hour = other.hour,
                minute = other.minute,
                time = otherTime,
                label = otherLabel,
                style = config[MeridianThemeSpec.Style],
                config = config,
                scale = scale,
            )
        }
    }
}

@Composable
private fun MeridianCard(
    index: Int,
    hour: Int,
    minute: Int,
    time: String,
    label: String,
    style: AodMeridianStyleValue,
    config: AodThemeConfig,
    scale: Float,
) {
    val cardBrush = config.brushOf(MeridianThemeSpec.CardColor)

    Column(
        modifier = Modifier
            .width(MeridianMetrics.CardWidth * scale)
            .drawWithCache {
                val cornerPx = (MeridianMetrics.CardCorner * scale).toPx()
                val strokeWidth = (MeridianMetrics.CardStroke * scale).toPx()
                val outline = Stroke(strokeWidth)
                val isFilled = style.isFilled(index)

                onDrawBehind {
                    if (isFilled) {
                        drawRoundRect(
                            brush = cardBrush,
                            cornerRadius = CornerRadius(cornerPx),
                            alpha = MeridianMetrics.FILL_ALPHA,
                        )
                    }
                    drawRoundRect(
                        brush = cardBrush,
                        topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
                        size = Size(size.width - strokeWidth, size.height - strokeWidth),
                        cornerRadius = CornerRadius(cornerPx - strokeWidth / 2f),
                        style = outline,
                        alpha = style.strokeAlpha,
                    )
                }
            }
            .padding(MeridianMetrics.CardPadding * scale),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MeridianDial(
            hour = hour,
            minute = minute,
            color = config[MeridianThemeSpec.DialColor],
            gradient = config.gradientOf(MeridianThemeSpec.DialColor),
            modifier = Modifier
                .padding(MeridianMetrics.DialMargin * scale)
                .size(MeridianMetrics.DialSize * scale),
        )
        Text(
            text = time,
            style = TextStyle(
                brush = cardBrush,
                fontSize = MeridianMetrics.TimeSize * scale,
                fontFamily = MeridianThemeSpec.Font,
            ),
            maxLines = 1,
            softWrap = false,
        )
        Text(
            text = label,
            style = TextStyle(
                brush = cardBrush,
                alpha = MeridianMetrics.LABEL_ALPHA,
                fontSize = MeridianMetrics.LabelSize * scale,
                fontFamily = MeridianThemeSpec.Font,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun AodMeridianDate(
    state: AodState,
    displayMillis: Long,
    scale: Float,
    isInline: Boolean,
    modifier: Modifier = Modifier,
) {
    val time = rememberZonedDateTime(displayMillis)
    val locale = LocalConfiguration.current.locales[0]
    val date = if (state.extras.isDateEnabled) {
        time.format(rememberFormatter(R.string.aod_meridian_date_pattern)).uppercase(locale)
    } else {
        null
    }
    val config = state.appearance.theme.config
    val weather = state.weather
    val components = state.appearance.components

    if (isInline) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (date != null) MeridianDateText(date = date, config = config, scale = scale)
            if (date != null && weather != null) Spacer(modifier = Modifier.width(MeridianMetrics.InlineWeatherGap))
            AodWeather(
                weather = weather,
                style = components.styleOf(AodComponentValue.WEATHER),
                layout = components.weatherLayout,
            )
        }
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (date != null) MeridianDateText(date = date, config = config, scale = scale)
            if (date != null && weather != null) Spacer(modifier = Modifier.height(MeridianMetrics.DateWeatherGap))
            AodWeather(
                weather = weather,
                style = components.styleOf(AodComponentValue.WEATHER),
                layout = components.weatherLayout,
            )
        }
    }
}

@Composable
private fun MeridianDateText(
    date: String,
    config: AodThemeConfig,
    scale: Float,
) {
    Text(
        text = date,
        style = TextStyle(
            brush = config.brushOf(MeridianThemeSpec.DateColor),
            alpha = MeridianMetrics.DATE_ALPHA,
            fontSize = MeridianMetrics.DateSize * scale,
            fontFamily = MeridianThemeSpec.Font,
        ),
        maxLines = 1,
    )
}

@Composable
fun rememberMeridianMillis(state: AodState): Long {
    var previewMillis by remember { mutableLongStateOf(state.nowMillis) }

    if (state.isClockLive && state.isPreview) {
        LaunchedEffect(Unit) {
            while (true) {
                val current = System.currentTimeMillis()
                previewMillis = current
                delay(MeridianMetrics.MINUTE_MILLIS - current % MeridianMetrics.MINUTE_MILLIS + MeridianMetrics.TICK_SLACK_MILLIS)
            }
        }
    }

    return if (state.isClockLive && state.isPreview) previewMillis else state.nowMillis
}
