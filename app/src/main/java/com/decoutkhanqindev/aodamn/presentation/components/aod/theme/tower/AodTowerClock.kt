package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tower

import android.graphics.Typeface
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tower.AodTowerPaintValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tower.TowerThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyBigShouldersDisplay
import kotlin.math.max

@Composable
fun AodTowerClock(
    nowMillis: Long,
    config: AodThemeConfig,
    glyphs: TowerGlyphs,
    unit: Dp,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val hours = now.format(rememberFormatter(if (is24HourFormat()) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h))
    val minutes = now.format(rememberFormatter(R.string.aod_minute_pattern))
    val description = now.format(rememberTimeFormatter())
    val style = config[TowerThemeSpec.Style]

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = modifier
                .size(width = unit * TowerMetrics.CLOCK_WIDTH, height = unit)
                .clearAndSetSemantics { contentDescription = description },
        ) {
            TowerDigitRow(
                text = hours,
                glyphs = glyphs,
                height = unit,
                color = config[TowerThemeSpec.HourColor],
                gradient = config.gradientOf(TowerThemeSpec.HourColor)?.toBrush(),
                paint = style.hourPaint,
            )

            TowerDigitRow(
                text = minutes,
                glyphs = glyphs,
                height = unit,
                color = config[TowerThemeSpec.MinuteColor],
                gradient = config.gradientOf(TowerThemeSpec.MinuteColor)?.toBrush(),
                paint = style.minutePaint,
                modifier = Modifier.offset(x = unit * TowerMetrics.MINUTE_LEFT),
            )
        }
    }
}

@Composable
private fun TowerDigitRow(
    text: String,
    glyphs: TowerGlyphs,
    height: Dp,
    color: Color,
    gradient: Brush?,
    paint: AodTowerPaintValue,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        text.forEach { digit ->
            Crossfade(
                targetState = digit,
                modifier = Modifier.size(width = height * TowerMetrics.PITCH, height = height),
                animationSpec = tween(durationMillis = TowerMetrics.DIGIT_CHANGE_MILLIS),
                label = "TowerDigit",
            ) { target ->
                TowerDigit(digit = target, glyphs = glyphs, color = color, gradient = gradient, paint = paint)
            }
        }
    }
}

@Composable
private fun TowerDigit(
    digit: Char,
    glyphs: TowerGlyphs,
    color: Color,
    gradient: Brush?,
    paint: AodTowerPaintValue,
) {
    val isFade = paint == AodTowerPaintValue.FADE_DOWN || paint == AodTowerPaintValue.FADE_UP

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(if (gradient != null && isFade) Modifier.graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen) else Modifier)
            .drawWithCache {
                val path = glyphs.digit(digit = digit, size = size)
                val stroke = Stroke(
                    width = max(size.height * TowerMetrics.STROKE, TowerMetrics.MinStroke.toPx()),
                    join = StrokeJoin.Round,
                )
                val edge = color.copy(alpha = color.alpha * TowerMetrics.FADE_EDGE_ALPHA)
                val quarter = color.copy(alpha = color.alpha * TowerMetrics.FADE_QUARTER_ALPHA)
                val fade = when (paint) {
                    AodTowerPaintValue.FADE_DOWN -> Brush.verticalGradient(
                        0f to color,
                        0.5f to color,
                        0.75f to quarter,
                        1f to edge,
                        startY = 0f,
                        endY = size.height,
                    )

                    AodTowerPaintValue.FADE_UP -> Brush.verticalGradient(
                        0f to edge,
                        0.25f to quarter,
                        0.5f to color,
                        1f to color,
                        startY = 0f,
                        endY = size.height,
                    )

                    AodTowerPaintValue.FILL, AodTowerPaintValue.OUTLINE -> null
                }

                onDrawBehind {
                    when {
                        gradient != null && fade != null -> {
                            drawPath(path = path, brush = gradient, style = stroke)
                            drawRect(brush = fade, blendMode = BlendMode.DstIn)
                        }

                        gradient != null && paint == AodTowerPaintValue.FILL -> drawPath(path = path, brush = gradient)
                        gradient != null -> drawPath(path = path, brush = gradient, style = stroke)
                        fade != null -> drawPath(path = path, brush = fade, style = stroke)
                        paint == AodTowerPaintValue.FILL -> drawPath(path = path, color = color)
                        else -> drawPath(path = path, color = color, style = stroke)
                    }
                }
            },
    )
}

@Composable
fun TowerDate(
    nowMillis: Long,
    brush: Brush,
    fontSize: Dp,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val formatter = rememberFormatter(R.string.aod_tower_date_pattern)
    val size = with(LocalDensity.current) { fontSize.toSp() }

    Text(
        text = now.format(formatter).uppercase(),
        modifier = modifier,
        fontSize = size,
        fontFamily = TowerThemeSpec.Font,
        letterSpacing = TowerMetrics.DATE_TRACKING.em,
        maxLines = 1,
        softWrap = false,
        style = TextStyle(
            brush = brush,
            lineHeight = size,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
        ),
    )
}

@Composable
fun rememberTowerGlyphs(): TowerGlyphs {
    val typeface = resolveTypeface(fontFamily = FontFamilyBigShouldersDisplay, fontWeight = FontWeight.Black, fallback = Typeface.DEFAULT_BOLD)
    return remember(typeface) { TowerGlyphs(typeface) }
}

@Preview(widthDp = 393, heightDp = 400)
@Composable
private fun AodTowerClockPreview() {
    AodTowerClock(
        nowMillis = System.currentTimeMillis(),
        config = AodThemeConfig(),
        glyphs = rememberTowerGlyphs(),
        unit = 377.dp,
    )
}
