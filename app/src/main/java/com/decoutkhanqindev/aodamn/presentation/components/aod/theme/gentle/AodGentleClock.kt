package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gentle

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gentle.GentleThemeSpec

@Composable
fun rememberGentleEm(size: Int, maxHeight: Dp): Dp {
    val density = LocalDensity.current
    return remember(size, maxHeight, density) {
        GentleMetrics.fitEm(
            em = with(density) { (GentleMetrics.CLOCK_SIZE * size / 100f).sp.toDp() },
            maxHeight = maxHeight,
        )
    }
}

@Composable
fun AodGentleClock(
    nowMillis: Long,
    config: AodThemeConfig,
    em: Dp,
    modifier: Modifier = Modifier,
) {
    val time = rememberZonedDateTime(nowMillis)
    val hours = time.format(rememberFormatter(if (is24HourFormat()) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h))
    val minutes = time.format(rememberFormatter(R.string.aod_minute_pattern))
    val description = time.format(rememberTimeFormatter())

    Column(
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GentleNumber(text = hours, em = em, config = config, colorOption = GentleThemeSpec.HourColor)
        GentleNumber(
            text = minutes,
            em = em,
            config = config,
            colorOption = GentleThemeSpec.MinuteColor,
            modifier = Modifier.padding(top = GentleMetrics.numberGap(em)),
        )
    }
}

@Composable
private fun GentleNumber(
    text: String,
    em: Dp,
    config: AodThemeConfig,
    colorOption: AodColorOption,
    modifier: Modifier = Modifier,
) {
    val weight = config[GentleThemeSpec.Weight].fontWeight
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val width = em * GentleMetrics.DIGITS_WIDTH
    val height = em * GentleMetrics.CAP

    AnimatedContent(
        targetState = text to weight,
        modifier = modifier,
        transitionSpec = {
            fadeIn(tween(GentleMetrics.DIGIT_MILLIS)) togetherWith
                fadeOut(tween(GentleMetrics.DIGIT_MILLIS)) using SizeTransform(clip = false)
        },
        contentAlignment = Alignment.Center,
        label = "GentleNumber",
    ) { (digits, fontWeight) ->
        val style = TextStyle(
            fontFamily = GentleThemeSpec.Font,
            fontWeight = fontWeight,
            fontSize = with(density) { em.toSp() },
        )
        val layout = remember(digits, style, density) { measurer.measure(text = digits, style = style, density = density) }

        Spacer(
            modifier = Modifier
                .size(width, height)
                .drawWithCache {
                    val brush = config.gradientOf(colorOption)?.toBrush(Rect(Offset.Zero, size)) ?: SolidColor(config[colorOption])

                    onDrawBehind {
                        drawText(
                            textLayoutResult = layout,
                            brush = brush,
                            topLeft = Offset((size.width - layout.size.width) / 2f, size.height - layout.firstBaseline),
                        )
                    }
                },
        )
    }
}

@Preview
@Composable
private fun AodGentleClockPreview() {
    AodGentleClock(nowMillis = System.currentTimeMillis(), config = AodThemeConfig(), em = GentleMetrics.CLOCK_SIZE.dp)
}
