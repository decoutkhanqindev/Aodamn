package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.card

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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.card.AodCardBackgroundValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.card.CardThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyBigShouldersDisplay
import kotlin.math.max

@Composable
fun AodCardClock(
    nowMillis: Long,
    config: AodThemeConfig,
    glyphs: CardGlyphs,
    unit: Dp,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val is24Hour = is24HourFormat()
    val hours = now.format(rememberFormatter(if (is24Hour) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h))
    val minutes = now.format(rememberFormatter(R.string.aod_minute_pattern))
    val tag = stringResource(
        when {
            is24Hour -> R.string.aod_card_24h
            now.hour < 12 -> R.string.aod_card_am
            else -> R.string.aod_card_pm
        },
    )
    val description = now.format(rememberTimeFormatter())
    val isOutline = config[CardThemeSpec.Style] == AodTileStyleValue.OUTLINE
    val background = config[CardThemeSpec.Background]
    val hourBrush = config.brushOf(CardThemeSpec.HourColor)
    val minuteBrush = config.brushOf(CardThemeSpec.MinuteColor)
    val cardBrush = config.brushOf(CardThemeSpec.CardColor)

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = modifier
                .size(width = unit * CardMetrics.CARD_WIDTH, height = unit * CardMetrics.CARD_HEIGHT)
                .clearAndSetSemantics { contentDescription = description }
                .drawBehind { drawCardBackground(background = background, brush = cardBrush, unit = unit.toPx()) },
        ) {
            CardDigitRow(
                text = hours,
                glyphs = glyphs,
                height = unit,
                brush = hourBrush,
                isOutline = isOutline,
                modifier = Modifier.offset(x = unit * CardMetrics.HOUR_LEFT, y = unit * CardMetrics.PADDING),
            )

            CardDigitRow(
                text = minutes,
                glyphs = glyphs,
                height = unit * CardMetrics.MINUTE,
                brush = minuteBrush,
                isOutline = isOutline,
                modifier = Modifier.offset(x = unit * CardMetrics.MINUTE_LEFT, y = unit * CardMetrics.PADDING),
            )

            CardTag(
                text = tag,
                glyphs = glyphs,
                brush = minuteBrush,
                isOutline = isOutline,
                unit = unit,
                modifier = Modifier
                    .offset(x = unit * CardMetrics.COLUMN_LEFT, y = unit * (CardMetrics.PADDING + CardMetrics.TAG_TOP))
                    .size(width = unit * CardMetrics.TAG_WIDTH, height = unit * (1f - CardMetrics.TAG_TOP)),
            )
        }
    }
}

private fun DrawScope.drawCardBackground(
    background: AodCardBackgroundValue,
    brush: Brush,
    unit: Float,
) {
    val corner = CornerRadius(unit * CardMetrics.CORNER)
    when (background) {
        AodCardBackgroundValue.TINTED -> drawRoundRect(brush = brush, alpha = CardMetrics.CARD_FILL_ALPHA, cornerRadius = corner)
        AodCardBackgroundValue.OUTLINE -> {
            val stroke = unit * CardMetrics.CARD_STROKE
            drawRoundRect(
                brush = brush,
                alpha = CardMetrics.CARD_STROKE_ALPHA,
                topLeft = Offset(stroke / 2f, stroke / 2f),
                size = Size(size.width - stroke, size.height - stroke),
                cornerRadius = corner,
                style = Stroke(width = stroke),
            )
        }

        AodCardBackgroundValue.NONE -> Unit
    }
}

@Composable
private fun CardDigitRow(
    text: String,
    glyphs: CardGlyphs,
    height: Dp,
    brush: Brush,
    isOutline: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        text.forEach { digit ->
            Crossfade(
                targetState = digit,
                modifier = Modifier.size(width = height * CardMetrics.PITCH, height = height),
                animationSpec = tween(durationMillis = CardMetrics.DIGIT_CHANGE_MILLIS),
                label = "CardDigit",
            ) { target ->
                CardDigit(digit = target, glyphs = glyphs, brush = brush, isOutline = isOutline)
            }
        }
    }
}

@Composable
private fun CardDigit(
    digit: Char,
    glyphs: CardGlyphs,
    brush: Brush,
    isOutline: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithCache {
                val path = glyphs.digit(digit = digit, size = size)
                val stroke = Stroke(
                    width = max(size.height * CardMetrics.OUTLINE, CardMetrics.MinStroke.toPx()),
                    join = StrokeJoin.Round,
                )

                onDrawBehind {
                    if (isOutline) drawPath(path = path, brush = brush, style = stroke) else drawPath(path = path, brush = brush)
                }
            },
    )
}

@Composable
private fun CardTag(
    text: String,
    glyphs: CardGlyphs,
    brush: Brush,
    isOutline: Boolean,
    unit: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.drawBehind {
            val r = unit.toPx()
            val corner = CornerRadius(r * CardMetrics.TAG_CORNER)
            if (isOutline) {
                val stroke = r * CardMetrics.TAG_STROKE
                drawRoundRect(
                    brush = brush,
                    alpha = CardMetrics.TAG_STROKE_ALPHA,
                    topLeft = Offset(stroke / 2f, stroke / 2f),
                    size = Size(size.width - stroke, size.height - stroke),
                    cornerRadius = corner,
                    style = Stroke(width = stroke),
                )
            } else {
                drawRoundRect(brush = brush, alpha = CardMetrics.TAG_FILL_ALPHA, cornerRadius = corner)
            }
        },
    ) {
        Crossfade(targetState = text, label = "CardTag") { target ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        val capHeight = unit.toPx() * CardMetrics.TAG_TEXT
                        val path = glyphs.word(text = target, capHeight = capHeight, width = size.width)
                        val top = (size.height - capHeight) / 2f

                        onDrawBehind {
                            translate(top = top) { drawPath(path = path, brush = brush) }
                        }
                    },
            )
        }
    }
}

@Composable
fun CardDate(
    nowMillis: Long,
    brush: Brush,
    unit: Dp,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val formatter = rememberFormatter(R.string.aod_theme_date_pattern)
    val fontSize = with(LocalDensity.current) { (unit * CardMetrics.DATE_FONT).toSp() }

    Text(
        text = now.format(formatter).uppercase(),
        modifier = modifier,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        fontFamily = CardThemeSpec.Font,
        maxLines = 1,
        softWrap = false,
        style = TextStyle(
            brush = brush,
            lineHeight = fontSize,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.None,
            ),
        ),
    )
}

@Composable
fun rememberCardGlyphs(): CardGlyphs {
    val typeface = resolveTypeface(fontFamily = FontFamilyBigShouldersDisplay, fontWeight = FontWeight.Black, fallback = Typeface.DEFAULT_BOLD)
    return remember(typeface) { CardGlyphs(typeface) }
}

@Preview(widthDp = 393, heightDp = 300)
@Composable
private fun AodCardClockPreview() {
    AodCardClock(
        nowMillis = System.currentTimeMillis(),
        config = AodThemeConfig(),
        glyphs = rememberCardGlyphs(),
        unit = 190.dp,
    )
}
