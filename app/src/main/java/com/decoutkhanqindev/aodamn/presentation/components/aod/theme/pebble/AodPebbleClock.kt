package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pebble

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.lerpRadii
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.radialShapePath
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodDateStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodShapeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTransitionValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.PebbleThemeSpec

@Composable
fun AodPebbleClock(
    nowMillis: Long,
    config: AodThemeConfig,
    isDateEnabled: Boolean,
    tileSize: Dp = 112.dp,
    isStackable: Boolean = true,
) {
    val timeDescription = rememberZonedDateTime(nowMillis).format(rememberTimeFormatter())
    val size = tileSize * (config[PebbleThemeSpec.Size] / 100f)
    val spacing = size / 8

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val tilesModifier = Modifier.clearAndSetSemantics { contentDescription = timeDescription }
        if (isStackable && config[PebbleThemeSpec.Stacked]) {
            Column(modifier = tilesModifier, verticalArrangement = Arrangement.spacedBy(spacing)) {
                PebbleTimeTile(nowMillis = nowMillis, config = config, isHour = true, size = size)
                PebbleTimeTile(nowMillis = nowMillis, config = config, isHour = false, size = size)
            }
        } else {
            Row(modifier = tilesModifier, horizontalArrangement = Arrangement.spacedBy(spacing)) {
                PebbleTimeTile(nowMillis = nowMillis, config = config, isHour = true, size = size)
                PebbleTimeTile(nowMillis = nowMillis, config = config, isHour = false, size = size)
            }
        }

        AnimatedVisibility(visible = isDateEnabled) {
            PebbleDate(nowMillis = nowMillis, config = config, modifier = Modifier.padding(top = size / 7))
        }
    }
}

@Composable
fun PebbleTimeTile(
    nowMillis: Long,
    config: AodThemeConfig,
    isHour: Boolean,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    val formatter = rememberFormatter(
        when {
            !isHour -> R.string.aod_minute_pattern
            is24HourFormat() -> R.string.aod_hour_pattern_24h
            else -> R.string.aod_hour_pattern_12h
        },
    )

    PebbleTile(
        text = rememberZonedDateTime(nowMillis).format(formatter),
        shape = config[if (isHour) PebbleThemeSpec.HourShape else PebbleThemeSpec.MinuteShape],
        isFilled = config[if (isHour) PebbleThemeSpec.HourStyle else PebbleThemeSpec.MinuteStyle] == AodTileStyleValue.FILLED,
        tileBrush = config.brushOf(if (isHour) PebbleThemeSpec.HourTileColor else PebbleThemeSpec.MinuteTileColor),
        digitBrush = config.brushOf(if (isHour) PebbleThemeSpec.HourDigitColor else PebbleThemeSpec.MinuteDigitColor),
        fontFamily = PebbleThemeSpec.Font,
        size = size,
        fontSize = with(LocalDensity.current) { (size * 50f / 112f).toSp() },
        transition = config[PebbleThemeSpec.Transition],
        modifier = modifier,
    )
}

@Composable
private fun PebbleTile(
    text: String,
    shape: AodShapeValue,
    isFilled: Boolean,
    tileBrush: Brush,
    digitBrush: Brush,
    fontFamily: FontFamily,
    size: Dp,
    fontSize: TextUnit,
    transition: AodTransitionValue,
    modifier: Modifier = Modifier,
) {
    val currentTransition by rememberUpdatedState(transition)
    val pulse = remember { Animatable(0f) }
    val shownText = remember { arrayOf(text) }

    LaunchedEffect(text) {
        val isChanged = shownText[0] != text
        shownText[0] = text
        if (isChanged && currentTransition == AodTransitionValue.PULSE) {
            pulse.animateTo(targetValue = 1f, animationSpec = tween(durationMillis = 220))
            pulse.animateTo(targetValue = 0f, animationSpec = spring(dampingRatio = 0.45f, stiffness = 300f))
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                val scale = 1f - 0.08f * pulse.value
                scaleX = scale
                scaleY = scale
            }
            .drawWithCache {
                val strokeWidth = this.size.minDimension * 0.035f
                val inset = if (isFilled) 0f else strokeWidth / 2f
                val basePath = radialShapePath(radii = shape.radii, size = this.size, inset = inset)
                val morphRadii = FloatArray(AodShapeValue.POINTS)
                val morphPath = Path()
                val stroke = Stroke(width = strokeWidth)

                onDrawBehind {
                    val fraction = pulse.value
                    val path = if (fraction == 0f) {
                        basePath
                    } else {
                        radialShapePath(
                            radii = lerpRadii(shape.radii, AodShapeValue.CIRCLE.radii, fraction * 0.7f, morphRadii),
                            size = this.size,
                            inset = inset,
                            path = morphPath,
                        )
                    }
                    if (isFilled) {
                        drawPath(path = path, brush = tileBrush)
                    } else {
                        drawPath(path = path, brush = tileBrush, style = stroke)
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        PebbleDigits(
            text = text,
            brush = digitBrush,
            fontFamily = fontFamily,
            fontSize = fontSize,
            transition = transition,
        )
    }
}

@Composable
private fun PebbleDigits(
    text: String,
    brush: Brush,
    fontFamily: FontFamily,
    fontSize: TextUnit,
    transition: AodTransitionValue,
) {
    val digits = @Composable { value: String ->
        Text(
            text = value,
            style = LocalTextStyle.current.merge(TextStyle(brush = brush)),
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily,
            textAlign = TextAlign.Center,
        )
    }

    when (transition) {
        AodTransitionValue.SLIDE, AodTransitionValue.ZOOM -> AnimatedContent(
            targetState = text,
            modifier = Modifier.clipToBounds(),
            transitionSpec = { digitTransition(transition) },
            label = "PebbleDigits",
        ) { target -> digits(target) }

        AodTransitionValue.NONE, AodTransitionValue.PULSE -> digits(text)
    }
}

private fun digitTransition(transition: AodTransitionValue): ContentTransform = if (transition == AodTransitionValue.SLIDE) {
    (slideInVertically(animationSpec = tween(durationMillis = 420)) { height -> height } +
            fadeIn(animationSpec = tween(durationMillis = 420))) togetherWith
            (slideOutVertically(animationSpec = tween(durationMillis = 420)) { height -> -height } +
                    fadeOut(animationSpec = tween(durationMillis = 300)))
} else {
    (scaleIn(animationSpec = tween(durationMillis = 380), initialScale = 0.6f) +
            fadeIn(animationSpec = tween(durationMillis = 380))) togetherWith
            (scaleOut(animationSpec = tween(durationMillis = 300), targetScale = 1.3f) +
                    fadeOut(animationSpec = tween(durationMillis = 300)))
}

@Composable
fun PebbleDate(
    nowMillis: Long,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val isBold = config[PebbleThemeSpec.DateStyle] == AodDateStyleValue.BOLD
    val scale = config[PebbleThemeSpec.Size] / 100f
    val formatter = rememberFormatter(if (isBold) R.string.aod_theme_date_pattern else R.string.aod_date_pattern)
    val text = now.format(formatter)

    Text(
        text = if (isBold) text.uppercase() else text,
        modifier = modifier,
        style = LocalTextStyle.current.merge(TextStyle(brush = config.brushOf(PebbleThemeSpec.DateColor))),
        fontSize = (if (isBold) 15.sp else 16.sp) * scale,
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        fontFamily = PebbleThemeSpec.Font,
        letterSpacing = if (isBold) 0.6.sp else 0.sp,
    )
}

@Preview(widthDp = 360, heightDp = 360)
@Composable
private fun AodPebbleClockPreview() {
    AodPebbleClock(
        nowMillis = System.currentTimeMillis(),
        config = AodThemeConfig(),
        isDateEnabled = true,
    )
}
