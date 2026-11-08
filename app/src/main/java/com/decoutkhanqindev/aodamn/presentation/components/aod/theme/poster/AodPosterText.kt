package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.poster

import android.graphics.Typeface
import android.text.format.DateFormat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.poster.PosterThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import kotlinx.coroutines.delay

@Composable
fun AodPosterText(
    state: AodState,
    isLandscape: Boolean,
    modifier: Modifier = Modifier,
) {
    var previewMillis by remember { mutableLongStateOf(state.nowMillis) }

    if (state.isClockLive && state.isPreview) {
        LaunchedEffect(Unit) {
            while (true) {
                val current = System.currentTimeMillis()
                previewMillis = current
                delay(PosterMetrics.MINUTE_MILLIS - current % PosterMetrics.MINUTE_MILLIS + PosterMetrics.TICK_SLACK_MILLIS)
            }
        }
    }

    val displayMillis = if (state.isClockLive && state.isPreview) previewMillis else state.nowMillis
    val time = rememberZonedDateTime(displayMillis)
    val locale = LocalConfiguration.current.locales[0]
    val hour = time.format(rememberFormatter(if (is24HourFormat()) R.string.aod_poster_hour_24h else R.string.aod_poster_hour_12h))
    val minute = time.format(rememberFormatter(R.string.aod_poster_minute))
    val day = time.format(rememberFormatter(R.string.aod_poster_day)).uppercase(locale)
    val isDayFirst = remember(locale) {
        val pattern = DateFormat.getBestDateTimePattern(locale, "dM")
        pattern.indexOf('d') < pattern.indexOf('M')
    }
    val date = if (state.extras.isDateEnabled) {
        time.format(rememberFormatter(PosterMetrics.datePattern(isDayFirst))).uppercase(locale)
    } else {
        null
    }
    val description = time.format(rememberTimeFormatter()) + " " + day + if (date != null) " " + date else ""
    val typeface = resolveTypeface(PosterThemeSpec.Font, FontWeight.Normal, Typeface.DEFAULT)
    val block = remember(typeface) { PosterTextBlock(typeface) }
    val config = state.appearance.theme.config
    val colors = remember(config) {
        PosterColors(
            hour = config[PosterThemeSpec.HourColor].toArgb(),
            minute = config[PosterThemeSpec.MinuteColor].toArgb(),
            day = config[PosterThemeSpec.DayColor].toArgb(),
            date = config[PosterThemeSpec.DateColor].toArgb(),
            hourGradient = config.gradientOf(PosterThemeSpec.HourColor),
            minuteGradient = config.gradientOf(PosterThemeSpec.MinuteColor),
            dayGradient = config.gradientOf(PosterThemeSpec.DayColor),
            dateGradient = config.gradientOf(PosterThemeSpec.DateColor),
        )
    }
    val density = LocalDensity.current.density
    val components = state.appearance.components

    Layout(
        content = {
            AodWeather(
                weather = state.weather,
                style = components.styleOf(AodComponentValue.WEATHER),
                layout = components.weatherLayout,
            )
        },
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = description }
            .drawBehind { block.draw(drawContext.canvas.nativeCanvas, hour, minute, day, date, colors) },
    ) { measurables, constraints ->
        val weather = measurables.single().measure(constraints.copy(minWidth = 0, minHeight = 0))
        val lineGap = PosterMetrics.LineGap.value * density
        block.measure(
            hour = hour,
            minute = minute,
            day = day,
            date = date,
            widthPx = constraints.maxWidth,
            heightPx = constraints.maxHeight,
            isLandscape = isLandscape,
            sizePercent = config[PosterThemeSpec.ClockSize],
            density = density,
            extraHeight = if (date == null && weather.height > 0) lineGap + weather.height else 0f,
            colors = colors,
        )
        val weatherX = if (date != null) {
            (block.dateRight + PosterMetrics.WeatherGap.value * density).toInt()
        } else {
            block.padding.toInt()
        }
        val weatherY = if (date != null) {
            (block.dateCenterY - weather.height / 2f).toInt()
        } else {
            (block.dayBottom + lineGap).toInt()
        }
        layout(constraints.maxWidth, constraints.maxHeight) {
            weather.place(weatherX, weatherY)
        }
    }
}
