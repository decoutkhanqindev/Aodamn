package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.moonphase

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodNotificationIcons
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatus
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.moonphase.MoonPhaseThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.Grey3A
import kotlin.math.abs

@Composable
fun AodMoonPhasePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val moonSize = minOf(maxWidth, maxHeight) * MoonPhaseMetrics.PORTRAIT_MOON

        MoonPhaseStarField(state = state, modifier = Modifier.fillMaxSize())
        MoonPhaseHeader(state = state, modifier = Modifier.align(Alignment.TopCenter).padding(top = MoonPhaseMetrics.HeaderTop))
        MoonPhaseMoon(state = state, modifier = Modifier.align(Alignment.Center).size(moonSize))
        MoonPhasePortraitDetails(state = state, onIntent = onIntent)
    }
}

@Composable
fun AodMoonPhaseLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val moonSize = minOf(maxWidth, maxHeight) * MoonPhaseMetrics.LANDSCAPE_MOON
        val detailsWidth = maxWidth * MoonPhaseMetrics.LandscapeDetailsWidth

        MoonPhaseStarField(state = state, modifier = Modifier.fillMaxSize())
        MoonPhaseMoon(
            state = state,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = maxWidth * 0.12f)
                .size(moonSize),
        )
        MoonPhaseLandscapeDetails(
            state = state,
            onIntent = onIntent,
            width = detailsWidth,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}

@Composable
private fun MoonPhasePortraitDetails(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MoonPhaseWeather(
            state = state,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = MoonPhaseMetrics.PortraitWeatherBottom),
        )
        MoonPhaseIcons(
            state = state,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = MoonPhaseMetrics.PortraitIconsBottom),
        )
        MoonPhaseLatest(
            state = state,
            onIntent = onIntent,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = MoonPhaseMetrics.PortraitLatestBottom),
        )
        MoonPhaseMedia(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = MoonPhaseMetrics.PortraitMediaBottom)
                .fillMaxWidth(),
        )
        MoonPhaseStatus(
            state = state,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = MoonPhaseMetrics.PortraitStatusBottom),
        )
    }
}

@Composable
private fun MoonPhaseLandscapeDetails(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    width: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(width),
    ) {
        MoonPhaseHeader(
            state = state,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = MoonPhaseMetrics.LandscapeHeaderTop),
        )
        MoonPhaseWeather(
            state = state,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = MoonPhaseMetrics.LandscapeWeatherTop),
        )
        MoonPhaseIcons(
            state = state,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = MoonPhaseMetrics.LandscapeIconsTop),
        )
        MoonPhaseLatest(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = MoonPhaseMetrics.LandscapeLatestTop)
                .widthIn(max = width - 24.dp),
        )
        MoonPhaseMedia(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 12.dp, end = 12.dp, bottom = MoonPhaseMetrics.LandscapeMediaBottom)
                .fillMaxWidth(),
        )
        MoonPhaseStatus(
            state = state,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = MoonPhaseMetrics.LandscapeStatusBottom),
        )
    }
}

@Composable
private fun MoonPhaseHeader(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val time = rememberZonedDateTime(state.nowMillis)
    val context = androidx.compose.ui.platform.LocalContext.current
    val timePattern = if (android.text.format.DateFormat.is24HourFormat(context)) {
        R.string.aod_moon_phase_time_24h
    } else {
        R.string.aod_moon_phase_time_12h
    }
    val shownTime = time.format(rememberFormatter(timePattern)).uppercase()
    val timeDescription = time.format(rememberTimeFormatter())
    val date = time.format(rememberFormatter(R.string.aod_moon_phase_date_pattern)).uppercase()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = shownTime,
            modifier = Modifier.clearAndSetSemantics { contentDescription = timeDescription },
            style = LocalTextStyle.current.merge(TextStyle(brush = config.brushOf(MoonPhaseThemeSpec.ClockColor))),
            fontFamily = MoonPhaseThemeSpec.Font,
            fontWeight = FontWeight.Medium,
            fontSize = MoonPhaseMetrics.HeaderTimeSize * (config[MoonPhaseThemeSpec.ClockSize] / 100f),
            letterSpacing = MoonPhaseMetrics.TimeLetterSpacing.em,
            maxLines = 1,
        )
        AnimatedVisibility(visible = state.extras.isDateEnabled) {
            Text(
                text = date,
                modifier = Modifier.padding(top = MoonPhaseMetrics.HeaderGap),
                style = LocalTextStyle.current.merge(TextStyle(brush = config.brushOf(MoonPhaseThemeSpec.DateColor))),
                fontFamily = MoonPhaseThemeSpec.Font,
                fontWeight = FontWeight.Medium,
                fontSize = MoonPhaseMetrics.HeaderDateSize,
                letterSpacing = MoonPhaseMetrics.DateLetterSpacing.em,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun MoonPhaseWeather(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(visible = state.weather != null) {
        Box(modifier = modifier) {
            AodWeather(
                weather = state.weather,
                style = state.appearance.components.styleOf(com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue.WEATHER),
                layout = state.appearance.components.weatherLayout,
            )
        }
    }
}

@Composable
private fun MoonPhaseIcons(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        AodNotificationIcons(
            notifications = state.notifications,
            style = state.appearance.components.styleOf(com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue.NOTIFICATIONS),
            layout = state.appearance.components.notificationsLayout,
        )
    }
}

@Composable
private fun MoonPhaseLatest(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(visible = state.notifications.latest != null, modifier = modifier) {
        AodPanelBlock(block = AodPanelBlockValue.NOTIFICATION, state = state, onIntent = onIntent)
    }
}

@Composable
private fun MoonPhaseMedia(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(visible = state.media != null, modifier = modifier) {
        AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
    }
}

@Composable
private fun MoonPhaseStatus(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        AodStatus(
            battery = state.battery,
            isBatteryEnabled = state.extras.isBatteryEnabled,
            isFlashlightOn = state.isFlashlightOn,
            style = state.appearance.components.styleOf(com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue.BATTERY),
            layout = state.appearance.components.batteryLayout,
        )
    }
}

@Composable
private fun MoonPhaseMoon(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val lunarAge = remember(state.nowMillis) { moonAge(state.nowMillis) }
    val phase = config[MoonPhaseThemeSpec.Peek].takeIf { it > 0 }?.div(50f) ?: lunarAge
    val moonDescription = stringResource(R.string.theme_moon_phase)

    Box(
        modifier = modifier
            .clearAndSetSemantics { contentDescription = moonDescription }
            .drawWithCache {
                val radius = size.minDimension / 2f * (config[MoonPhaseThemeSpec.CharacterSize] / 100f)
                val moonCenter = Offset(size.width / 2f, size.height / 2f)
                val surface = config.brushOf(MoonPhaseThemeSpec.CharacterColor)
                val glow = config.brushOf(MoonPhaseThemeSpec.GlowColor)
                val shadowOffset = radius * (1f - phase * 2f)

                onDrawBehind {
                    drawCircle(brush = glow, radius = radius * 1.22f, center = moonCenter, alpha = MoonPhaseMetrics.GLOW_OUTER_ALPHA)
                    drawCircle(brush = glow, radius = radius * 1.13f, center = moonCenter, alpha = MoonPhaseMetrics.GLOW_MIDDLE_ALPHA)
                    drawCircle(brush = glow, radius = radius * 1.04f, center = moonCenter, alpha = MoonPhaseMetrics.GLOW_INNER_ALPHA)
                    rotate(degrees = config[MoonPhaseThemeSpec.Rotation].toFloat(), pivot = moonCenter) {
                        drawCircle(brush = surface, radius = radius, center = moonCenter)
                        drawCircle(color = Grey3A, radius = radius * 0.09f, center = Offset(moonCenter.x - radius * 0.4f, moonCenter.y - radius * 0.42f), alpha = MoonPhaseMetrics.CRATER_ALPHA)
                        drawCircle(color = Grey3A, radius = radius * 0.13f, center = Offset(moonCenter.x - radius * 0.68f, moonCenter.y - radius * 0.42f), alpha = MoonPhaseMetrics.CRATER_ALPHA)
                        drawCircle(color = Grey3A, radius = radius * 0.13f, center = Offset(moonCenter.x - radius * 0.16f, moonCenter.y - radius * 0.23f), alpha = MoonPhaseMetrics.CRATER_ALPHA)
                        drawCircle(color = Grey3A, radius = radius * 0.15f, center = Offset(moonCenter.x + radius * 0.30f, moonCenter.y + radius * 0.28f), alpha = MoonPhaseMetrics.CRATER_ALPHA)
                        drawCircle(color = Grey3A, radius = radius * 0.11f, center = Offset(moonCenter.x - radius * 0.44f, moonCenter.y + radius * 0.42f), alpha = MoonPhaseMetrics.CRATER_ALPHA)
                        drawCircle(color = Black, radius = radius, center = Offset(moonCenter.x + shadowOffset, moonCenter.y))
                    }
                }
            },
    )
}

@Composable
private fun MoonPhaseStarField(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val transition = rememberInfiniteTransition(label = "MoonPhaseField")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(MoonPhaseMetrics.STAR_LOOP_MILLIS)),
        label = "MoonPhaseFieldProgress",
    )

    Box(
        modifier = modifier.drawWithCache {
            val stars = FloatArray(96) { index -> ((index * 47) % 97) / 97f }
            val particle = config.brushOf(MoonPhaseThemeSpec.ParticleColor)

            onDrawBehind {
                repeat(24) { index ->
                    val x = size.width * stars[index * 2]
                    val y = size.height * stars[index * 2 + 1]
                    val alpha = 0.22f + abs(progress - stars[index * 2]) * 0.35f
                    drawCircle(brush = particle, radius = 1.2f + index % 3, center = Offset(x, y), alpha = alpha)
                }
                val startX = size.width * (1f - progress)
                val startY = size.height * (0.18f + progress * 0.22f)
                drawLine(brush = particle, start = Offset(startX, startY), end = Offset(startX - 42.dp.toPx(), startY + 24.dp.toPx()), strokeWidth = 3.dp.toPx(), alpha = 0.72f)
                drawCircle(brush = particle, radius = 3.dp.toPx(), center = Offset(startX, startY), alpha = 0.92f)
            }
        },
    )
}

private fun moonAge(nowMillis: Long): Float {
    val julianDate = nowMillis / 86_400_000.0 + MoonPhaseMetrics.JULIAN_UNIX_EPOCH
    val cycle = (julianDate - MoonPhaseMetrics.LUNAR_REFERENCE) / MoonPhaseMetrics.LUNAR_CYCLE_DAYS
    val age = cycle - kotlin.math.floor(cycle)
    return if (age > 0.5) (1.0 - age).toFloat() * 2f else age.toFloat() * 2f
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodMoonPhasePortraitPreview() {
    AodMoonPhasePortrait(
        state = AodState(
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.MOON_PHASE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
