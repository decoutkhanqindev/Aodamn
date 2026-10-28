package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.fade

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.fade.AodFadeWeightValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.fade.FadeThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Black

@Composable
fun AodFadeClock(
    nowMillis: Long,
    config: AodThemeConfig,
    em: Dp,
    hourTop: Dp,
    isAppearing: Boolean,
    modifier: Modifier = Modifier,
) {
    val weight = config[FadeThemeSpec.Weight]
    val isFaded = config[FadeThemeSpec.Fade]
    val time = rememberZonedDateTime(nowMillis)
    val hours = time.format(rememberFormatter(if (is24HourFormat()) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h))
    val minutes = time.format(rememberFormatter(R.string.aod_minute_pattern))
    val description = time.format(rememberTimeFormatter())
    val appear = remember { Animatable(if (isAppearing) 0f else 1f) }
    val numberWidth = em * FadeMetrics.ADVANCE * 2
    val numberHeight = em * FadeMetrics.lineBox(weight)

    LaunchedEffect(Unit) {
        appear.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = FadeMetrics.APPEAR_MILLIS))
    }

    Box(
        modifier = modifier
            .graphicsLayer { alpha = appear.value }
            .clearAndSetSemantics { contentDescription = description },
        contentAlignment = Alignment.TopCenter,
    ) {
        FadeNumber(
            text = hours,
            em = em,
            weight = weight,
            config = config,
            colorOption = FadeThemeSpec.HourColor,
            isFaded = isFaded,
            modifier = Modifier
                .offset(y = hourTop)
                .size(width = numberWidth, height = numberHeight),
        )

        FadeNumber(
            text = minutes,
            em = em,
            weight = weight,
            config = config,
            colorOption = FadeThemeSpec.MinuteColor,
            isFaded = isFaded,
            modifier = Modifier
                .offset(y = hourTop + numberHeight - FadeMetrics.Overlap)
                .size(width = numberWidth, height = numberHeight),
        )
    }
}

@Composable
private fun FadeNumber(
    text: String,
    em: Dp,
    weight: AodFadeWeightValue,
    config: AodThemeConfig,
    colorOption: AodColorOption,
    isFaded: Boolean,
    modifier: Modifier = Modifier,
) {
    val measurer = rememberTextMeasurer()

    Crossfade(
        targetState = text,
        modifier = modifier,
        animationSpec = tween(durationMillis = FadeMetrics.DIGIT_MILLIS),
        label = "FadeNumber",
    ) { digits ->
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { compositingStrategy = if (isFaded) CompositingStrategy.Offscreen else CompositingStrategy.Auto }
                .drawWithCache {
                    val emPx = em.toPx()
                    val layout = measurer.measure(
                        text = digits,
                        style = TextStyle(fontFamily = FadeThemeSpec.Font, fontWeight = weight.fontWeight, fontSize = em.toSp()),
                    )
                    val origin = Offset(
                        x = (size.width - layout.size.width) / 2f,
                        y = FadeMetrics.lineTop(weight) * emPx - layout.firstBaseline,
                    )
                    val brush = config.gradientOf(colorOption)?.toBrush(Rect(-origin.x, -origin.y, size.width - origin.x, size.height - origin.y))
                        ?: SolidColor(config[colorOption])
                    val fade = Brush.linearGradient(
                        colorStops = Array(FadeMetrics.FADE_STEPS + 1) { step ->
                            val fraction = step / FadeMetrics.FADE_STEPS.toFloat()
                            fraction to lerp(Color.Transparent, Black, fraction * fraction)
                        },
                        start = Offset(origin.x, 0f),
                        end = Offset(origin.x + FadeMetrics.FADE_END_X * emPx, FadeMetrics.FADE_END_Y * emPx),
                    )

                    onDrawBehind {
                        drawText(textLayoutResult = layout, brush = brush, topLeft = origin)
                        if (isFaded) {
                            drawRect(brush = fade, blendMode = BlendMode.DstIn)
                        }
                    }
                },
        )
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodFadeClockPreview() {
    AodFadeClock(
        nowMillis = System.currentTimeMillis(),
        config = AodThemeConfig(),
        em = 139.5.dp,
        hourTop = 283.7.dp,
        isAppearing = false,
        modifier = Modifier.fillMaxSize(),
    )
}
