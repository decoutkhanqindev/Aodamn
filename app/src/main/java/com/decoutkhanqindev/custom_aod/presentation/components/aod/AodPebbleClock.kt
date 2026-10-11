package com.decoutkhanqindev.custom_aod.presentation.components.aod

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.is24HourFormat
import com.decoutkhanqindev.custom_aod.presentation.components.rememberFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodDateStyleValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodShapeValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodTileStyleValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodTransitionValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.PebbleThemeSpec

// Chủ đề Sỏi: giờ và phút mỗi số một khối hình mềm, dòng ngày ngay dưới. Animation chỉ chạy lúc số đổi; giá trị animation đọc ở
// pha vẽ (graphicsLayer, drawWithCache) nên không recompose từng khung hình.
@Composable
fun AodPebbleClock(
    nowMillis: Long,
    config: AodThemeConfig,
    isDateEnabled: Boolean,
) {
    val now = rememberZonedDateTime(nowMillis)
    val hourFormatter = rememberFormatter(
        if (is24HourFormat()) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h,
    )
    val minuteFormatter = rememberFormatter(R.string.aod_minute_pattern)
    val timeDescription = now.format(rememberTimeFormatter())
    val scale = config[PebbleThemeSpec.Size] / 100f
    val spacing = 14.dp * scale
    val fontFamily = config[PebbleThemeSpec.Font].fontFamily
    val transition = config[PebbleThemeSpec.Transition]

    val hourTile = @Composable {
        PebbleTile(
            text = now.format(hourFormatter),
            shape = config[PebbleThemeSpec.HourShape],
            isFilled = config[PebbleThemeSpec.HourStyle] == AodTileStyleValue.FILLED,
            tileColor = config[PebbleThemeSpec.HourTileColor],
            digitColor = config[PebbleThemeSpec.HourDigitColor],
            fontFamily = fontFamily,
            size = 112.dp * scale,
            fontSize = 50.sp * scale,
            transition = transition,
        )
    }
    val minuteTile = @Composable {
        PebbleTile(
            text = now.format(minuteFormatter),
            shape = config[PebbleThemeSpec.MinuteShape],
            isFilled = config[PebbleThemeSpec.MinuteStyle] == AodTileStyleValue.FILLED,
            tileColor = config[PebbleThemeSpec.MinuteTileColor],
            digitColor = config[PebbleThemeSpec.MinuteDigitColor],
            fontFamily = fontFamily,
            size = 112.dp * scale,
            fontSize = 50.sp * scale,
            transition = transition,
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val tilesModifier = Modifier.clearAndSetSemantics { contentDescription = timeDescription }
        if (config[PebbleThemeSpec.Stacked]) {
            Column(modifier = tilesModifier, verticalArrangement = Arrangement.spacedBy(spacing)) {
                hourTile()
                minuteTile()
            }
        } else {
            Row(modifier = tilesModifier, horizontalArrangement = Arrangement.spacedBy(spacing)) {
                hourTile()
                minuteTile()
            }
        }

        AnimatedVisibility(visible = isDateEnabled) {
            PebbleDate(
                nowMillis = nowMillis,
                style = config[PebbleThemeSpec.DateStyle],
                color = config[PebbleThemeSpec.DateColor],
                fontFamily = fontFamily,
                scale = scale,
                modifier = Modifier.padding(top = 16.dp * scale),
            )
        }
    }
}

@Composable
private fun PebbleTile(
    text: String,
    shape: AodShapeValue,
    isFilled: Boolean,
    tileColor: Color,
    digitColor: Color,
    fontFamily: FontFamily,
    size: Dp,
    fontSize: TextUnit,
    transition: AodTransitionValue,
) {
    val currentTransition by rememberUpdatedState(transition)
    val pulse = remember { Animatable(0f) }
    // Số đang hiện, giữ ngoài snapshot: lần chạy đầu của effect (mở AOD) không phải lúc số đổi nên không nảy.
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
        modifier = Modifier
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
                        drawPath(path = path, color = tileColor)
                    } else {
                        drawPath(path = path, color = tileColor, style = stroke)
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        PebbleDigits(
            text = text,
            color = digitColor,
            fontFamily = fontFamily,
            fontSize = fontSize,
            transition = transition,
        )
    }
}

@Composable
private fun PebbleDigits(
    text: String,
    color: Color,
    fontFamily: FontFamily,
    fontSize: TextUnit,
    transition: AodTransitionValue,
) {
    val digits = @Composable { value: String ->
        Text(
            text = value,
            color = color,
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
private fun PebbleDate(
    nowMillis: Long,
    style: AodDateStyleValue,
    color: Color,
    fontFamily: FontFamily,
    scale: Float,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val isBold = style == AodDateStyleValue.BOLD
    val formatter = rememberFormatter(if (isBold) R.string.aod_theme_date_pattern else R.string.aod_date_pattern)
    val text = now.format(formatter)

    Text(
        text = if (isBold) text.uppercase() else text,
        modifier = modifier,
        color = color,
        fontSize = (if (isBold) 15.sp else 16.sp) * scale,
        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
        fontFamily = fontFamily,
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
