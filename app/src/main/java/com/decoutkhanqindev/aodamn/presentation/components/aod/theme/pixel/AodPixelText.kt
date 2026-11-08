package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pixel

import android.graphics.Typeface
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pixel.PixelThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSans
import kotlinx.coroutines.delay

@Composable
fun AodPixelText(
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
                delay(PixelMetrics.MINUTE_MILLIS - current % PixelMetrics.MINUTE_MILLIS + PixelMetrics.TICK_SLACK_MILLIS)
            }
        }
    }

    val displayMillis = if (state.isClockLive && state.isPreview) previewMillis else state.nowMillis
    val time = rememberZonedDateTime(displayMillis)
    val locale = LocalConfiguration.current.locales[0]
    val clock = time.format(rememberFormatter(if (is24HourFormat()) R.string.aod_time_pattern_24h else R.string.aod_pixel_time_12h))
    val date = if (state.extras.isDateEnabled) {
        time.format(rememberFormatter(R.string.aod_pixel_date_pattern)).uppercase(locale)
    } else {
        null
    }
    val description = time.format(rememberTimeFormatter()) + if (date != null) " $date" else ""
    val typeface = resolveTypeface(PixelThemeSpec.Font, FontWeight.Normal, Typeface.DEFAULT)
    val block = remember(typeface) { PixelClockBlock(typeface) }
    val config = state.appearance.theme.config
    val clockColor = remember(config) { config[PixelThemeSpec.ClockColor].toArgb() }
    val clockGradient = config.gradientOf(PixelThemeSpec.ClockColor)
    val dateBrush = config.brushOf(PixelThemeSpec.DateColor)
    val components = state.appearance.components
    val hasWeather = state.weather != null

    Layout(
        content = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (date != null) {
                    Text(
                        text = date,
                        style = TextStyle(
                            brush = dateBrush,
                            fontSize = PixelMetrics.DateSize,
                            fontFamily = FontFamilyGoogleSans,
                            letterSpacing = PixelMetrics.DATE_SPACING.em,
                        ),
                        maxLines = 1,
                    )
                }
                if (date != null && hasWeather) {
                    Spacer(modifier = Modifier.width(PixelMetrics.DateWeatherGap))
                }
                AodWeather(
                    weather = state.weather,
                    style = components.styleOf(AodComponentValue.WEATHER),
                    layout = components.weatherLayout,
                )
            }
        },
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = description }
            .drawBehind { block.draw(drawContext.canvas.nativeCanvas, clock, clockColor) },
    ) { measurables, constraints ->
        val dateRow = measurables.single().measure(constraints.copy(minWidth = 0, minHeight = 0))
        block.measure(
            time = clock,
            widthPx = constraints.maxWidth.toFloat(),
            heightPx = constraints.maxHeight.toFloat(),
            isLandscape = isLandscape,
            sizePercent = config[PixelThemeSpec.ClockSize],
            isOutline = config[PixelThemeSpec.Style].isOutline,
            gradient = clockGradient,
        )
        val hasRow = dateRow.height > 0
        val dateGap = PixelMetrics.DateGap.toPx()
        val blockHeight = (if (hasRow) dateRow.height + dateGap else 0f) + block.height
        val top = PixelMetrics.blockTop(isLandscape, constraints.maxHeight.toFloat(), blockHeight)
        val centerX = PixelMetrics.centerX(isLandscape, constraints.maxWidth.toFloat())

        layout(constraints.maxWidth, constraints.maxHeight) {
            dateRow.place((centerX - dateRow.width / 2f).toInt(), top.toInt())
            block.place(
                left = centerX - block.width / 2f,
                top = top + if (hasRow) dateRow.height + dateGap else 0f,
            )
        }
    }
}
