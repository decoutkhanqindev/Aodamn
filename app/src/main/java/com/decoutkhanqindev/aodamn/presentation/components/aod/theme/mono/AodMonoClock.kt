package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.mono

import android.graphics.Paint
import android.graphics.Typeface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.mono.MonoThemeSpec

@Composable
fun AodMonoClock(
    nowMillis: Long,
    config: AodThemeConfig,
    scale: Float,
    modifier: Modifier = Modifier,
) {
    val time = rememberZonedDateTime(nowMillis)
    val hours = time.format(rememberFormatter(if (is24HourFormat()) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h))
    val minutes = time.format(rememberFormatter(R.string.aod_minute_pattern))
    val description = time.format(rememberTimeFormatter())
    val style = TextStyle(fontFamily = MonoThemeSpec.Font, fontSize = (MonoMetrics.CLOCK_SIZE * scale * config[MonoThemeSpec.Size] / 100f).sp)

    Column(
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MonoNumber(text = hours, style = style, config = config, colorOption = MonoThemeSpec.HourColor)

        MonoNumber(
            text = minutes,
            style = style,
            config = config,
            colorOption = MonoThemeSpec.MinuteColor,
            modifier = Modifier.padding(top = MonoMetrics.NumberGap),
        )
    }
}

@Composable
private fun MonoNumber(
    text: String,
    style: TextStyle,
    config: AodThemeConfig,
    colorOption: AodColorOption,
    modifier: Modifier = Modifier,
) {
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val typeface = resolveTypeface(fontFamily = MonoThemeSpec.Font, fontWeight = FontWeight.Normal, fallback = Typeface.MONOSPACE)
    val paint = remember(typeface) { Paint(Paint.ANTI_ALIAS_FLAG).apply { this.typeface = typeface } }

    AnimatedContent(
        targetState = text,
        modifier = modifier,
        transitionSpec = {
            fadeIn(tween(durationMillis = MonoMetrics.DIGIT_MILLIS)) togetherWith
                fadeOut(tween(durationMillis = MonoMetrics.DIGIT_MILLIS)) using SizeTransform(clip = false)
        },
        contentAlignment = Alignment.TopCenter,
        label = "MonoNumber",
    ) { digits ->
        val layout = remember(digits, style, density) { measurer.measure(text = digits, style = style, density = density) }
        val ink = remember(layout, paint) {
            val bounds = android.graphics.Rect()
            paint.textSize = with(density) { style.fontSize.toPx() }
            paint.getTextBounds(digits, 0, digits.length, bounds)
            Rect(
                left = bounds.left.toFloat(),
                top = layout.firstBaseline + bounds.top,
                right = bounds.right.toFloat(),
                bottom = layout.firstBaseline + bounds.bottom,
            )
        }

        Spacer(
            modifier = Modifier
                .size(width = with(density) { ink.right.toDp() }, height = with(density) { ink.height.toDp() })
                .drawWithCache {
                    val brush = config.gradientOf(colorOption)?.toBrush(ink) ?: SolidColor(config[colorOption])

                    onDrawBehind {
                        drawText(textLayoutResult = layout, brush = brush, topLeft = Offset(0f, -ink.top))
                    }
                },
        )
    }
}

@Preview
@Composable
private fun AodMonoClockPreview() {
    AodMonoClock(nowMillis = System.currentTimeMillis(), config = AodThemeConfig(), scale = 1f)
}
