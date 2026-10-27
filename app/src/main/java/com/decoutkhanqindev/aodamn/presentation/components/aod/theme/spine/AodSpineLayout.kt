package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.spine

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodLatestLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.lineText
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.spine.SpineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.White

@Composable
fun AodSpinePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val hasMedia = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val unit = minOf(maxWidth, maxHeight) / SpineMetrics.BASE_WIDTH

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(SpineMetrics.TOP_WEIGHT))

            Column(
                modifier = Modifier
                    .weight(SpineMetrics.INFO_WEIGHT)
                    .fillMaxWidth()
                    .padding(start = SpineMetrics.InfoStart, end = SpineMetrics.InfoEnd),
            ) {
                SpineDetails(state = state)

                Spacer(modifier = Modifier.height(SpineMetrics.DetailsBottom))

                SpinePlayer(state = state, onIntent = onIntent)

                SpineExtras(
                    state = state,
                    onIntent = onIntent,
                    modifier = Modifier.padding(top = if (hasMedia) SpineMetrics.ExtrasGap else 0.dp),
                )
            }

            SpineClock(state = state, unit = unit, bottomGap = SpineMetrics.PORTRAIT_CLOCK_BOTTOM)
        }
    }
}

@Composable
fun AodSpineLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val config = state.appearance.theme.config
    val (clock, date) = rememberSpineTexts(state.nowMillis)
    val typeface = resolveTypeface(fontFamily = SpineThemeSpec.Font, fontWeight = FontWeight.Normal, fallback = Typeface.DEFAULT)
    val density = LocalDensity.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val unit = minOf(maxWidth, maxHeight) / SpineMetrics.BASE_WIDTH
        val clockSize = unit * (SpineMetrics.CLOCK_SIZE * config[SpineThemeSpec.Size] / 100f)
        val dateHeight = if (state.extras.isDateEnabled) {
            unit * SpineMetrics.DATE_GAP + spineLength(text = date, fontSize = clockSize * SpineMetrics.DATE_SCALE, typeface = typeface, density = density)
        } else {
            0.dp
        }
        val columnHeight = unit * SpineMetrics.LANDSCAPE_CLOCK_BOTTOM +
            spineLength(text = clock, fontSize = clockSize, typeface = typeface, density = density) + dateHeight
        val scale = ((maxHeight - SpineMetrics.LandscapeFitMargin) / columnHeight).coerceAtMost(1f)

        Row(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(SpineMetrics.LANDSCAPE_EDGE_WEIGHT))

            SpineClock(
                state = state,
                unit = unit * scale,
                bottomGap = SpineMetrics.LANDSCAPE_CLOCK_BOTTOM,
                modifier = Modifier.align(Alignment.Bottom),
            )

            Spacer(modifier = Modifier.weight(SpineMetrics.LANDSCAPE_GAP_WEIGHT))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(SpineMetrics.LandscapeInfoMargin),
                verticalArrangement = Arrangement.Bottom,
            ) {
                SpineExtras(state = state, onIntent = onIntent, modifier = Modifier.padding(bottom = SpineMetrics.ExtrasGap))

                SpineDetails(state = state, modifier = Modifier.widthIn(max = SpineMetrics.PlayerMaxWidth))

                Spacer(modifier = Modifier.height(SpineMetrics.DetailsBottom))

                SpinePlayer(state = state, onIntent = onIntent)
            }

            Spacer(modifier = Modifier.weight(SpineMetrics.LANDSCAPE_EDGE_WEIGHT))
        }
    }
}

@Composable
private fun SpineClock(
    state: AodState,
    unit: Dp,
    bottomGap: Float,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val (clock, date) = rememberSpineTexts(state.nowMillis)
    val clockSize = unit * (SpineMetrics.CLOCK_SIZE * config[SpineThemeSpec.Size] / 100f)

    Column(
        modifier = modifier.padding(start = unit * SpineMetrics.COLUMN_START),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(visible = state.extras.isDateEnabled) {
            SpineVerticalText(
                text = date,
                fontSize = clockSize * SpineMetrics.DATE_SCALE,
                option = SpineThemeSpec.DateColor,
                config = config,
                modifier = Modifier.padding(bottom = unit * SpineMetrics.DATE_GAP),
            )
        }

        SpineVerticalText(
            text = clock,
            fontSize = clockSize,
            option = SpineThemeSpec.ClockColor,
            config = config,
            modifier = Modifier.padding(bottom = unit * bottomGap),
        )
    }
}

@Composable
private fun SpineVerticalText(
    text: String,
    fontSize: Dp,
    option: AodColorOption,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val typeface = resolveTypeface(fontFamily = SpineThemeSpec.Font, fontWeight = FontWeight.Normal, fallback = Typeface.DEFAULT)
    val density = LocalDensity.current
    val paint = remember(typeface, fontSize, density) { spinePaint(typeface = typeface, fontSize = fontSize, density = density) }
    val length = remember(paint, text) { paint.measureText(text) }
    val lineHeight = remember(paint) { paint.fontMetrics.bottom - paint.fontMetrics.top }
    val color = config[option]
    val gradient = config.gradientOf(option)

    Spacer(
        modifier = modifier
            .size(width = with(density) { lineHeight.toDp() }, height = with(density) { length.toDp() })
            .semantics { contentDescription = text }
            .drawWithCache {
                val bounds = Rect().also { rect -> paint.getTextBounds(text, 0, text.length, rect) }
                val baseline = -(bounds.top + bounds.bottom) / 2f
                val shader = gradient?.toShader(0f, baseline + bounds.top, length, baseline + bounds.bottom)

                onDrawBehind {
                    paint.shader = shader
                    paint.color = if (shader == null) color.toArgb() else White.toArgb()
                    drawIntoCanvas { canvas ->
                        val native = canvas.nativeCanvas
                        native.save()
                        native.translate(size.width / 2f, size.height)
                        native.rotate(-90f)
                        native.drawText(text, 0f, baseline, paint)
                        native.restore()
                    }
                }
            },
    )
}

@Composable
private fun SpineDetails(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SpineStatus(state = state)

        SpineLatest(state = state)
    }
}

@Composable
private fun SpineStatus(state: AodState) {
    val config = state.appearance.theme.config
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val isWeatherShown = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components
    val hasStatus = battery != null || state.isFlashlightOn || state.notifications.icons.isNotEmpty()
    val color = config[SpineThemeSpec.StatusColor]
    val brush = config.gradientOf(SpineThemeSpec.StatusColor)?.toBrush()

    Row(verticalAlignment = Alignment.CenterVertically) {
        AnimatedVisibility(visible = isWeatherShown) {
            AodWeather(
                weather = state.weather,
                style = state.appearance.components.styleOf(AodComponentValue.WEATHER),
                layout = state.appearance.components.weatherLayout,
            )
        }

        AnimatedVisibility(visible = isWeatherShown && hasStatus) {
            Box(
                modifier = Modifier
                    .padding(horizontal = SpineMetrics.DotGap)
                    .size(SpineMetrics.StatusGlyph * SpineMetrics.DOT_SCALE)
                    .aodTint(brush)
                    .background(color = color, shape = CircleShape),
            )
        }

        AodStatusLine(
            battery = battery,
            isFlashlightOn = state.isFlashlightOn,
            notifications = state.notifications.copy(latest = null),
            color = color,
            glyphHeight = SpineMetrics.StatusGlyph,
            fontFamily = SpineThemeSpec.Font,
            gapScale = SpineMetrics.STATUS_GAP_SCALE,
            brush = brush,
        )
    }
}

@Composable
private fun SpineLatest(state: AodState) {
    val config = state.appearance.theme.config
    val latest = state.notifications.latest

    AnimatedVisibility(
        visible = latest != null,
        enter = slideInVertically { height -> -height } + fadeIn(),
        exit = fadeOut(),
    ) {
        if (latest != null) {
            val text = latest.lineText(R.string.aod_spine_notification)

            AnimatedContent(
                targetState = latest.icon to text,
                modifier = Modifier.padding(top = SpineMetrics.LatestGap),
                transitionSpec = { (slideInVertically { height -> -height } + fadeIn()) togetherWith fadeOut() },
                contentAlignment = Alignment.CenterStart,
                label = "SpineLatest",
            ) { (icon, content) ->
                AodLatestLine(
                    text = content,
                    icon = icon,
                    color = config[SpineThemeSpec.StatusColor],
                    brush = config.gradientOf(SpineThemeSpec.StatusColor)?.toBrush(),
                    style = SpineMetrics.LatestLine,
                )
            }
        }
    }
}

@Composable
private fun SpinePlayer(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    AnimatedVisibility(visible = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components) {
        Box(
            modifier = Modifier
                .widthIn(max = SpineMetrics.PlayerMaxWidth)
                .padding(end = SpineMetrics.PlayerEnd),
        ) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }
    }
}

@Composable
private fun SpineExtras(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        AodPanelBlock(block = AodPanelBlockValue.EVENTS, state = state, onIntent = onIntent)

        AodPanelBlock(block = AodPanelBlockValue.MEMO, state = state, onIntent = onIntent)
    }
}

@Composable
private fun rememberSpineTexts(nowMillis: Long): Pair<String, String> {
    val time = rememberZonedDateTime(nowMillis)
    val timeFormatter = rememberTimeFormatter()
    val dateFormatter = rememberFormatter(R.string.aod_spine_date_pattern)
    val locale = LocalConfiguration.current.locales[0]

    return remember(time, timeFormatter, dateFormatter, locale) {
        time.format(timeFormatter) to time.format(dateFormatter).uppercase(locale)
    }
}

private fun spinePaint(typeface: Typeface, fontSize: Dp, density: Density): Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    this.typeface = typeface
    isSubpixelText = true
    textSize = with(density) { fontSize.toPx() }
}

private fun spineLength(text: String, fontSize: Dp, typeface: Typeface, density: Density): Dp =
    with(density) { spinePaint(typeface = typeface, fontSize = fontSize, density = density).measureText(text).toDp() }

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodSpinePortraitPreview() {
    AodSpinePortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.SPINE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodSpineLandscapePreview() {
    AodSpineLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.SPINE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
