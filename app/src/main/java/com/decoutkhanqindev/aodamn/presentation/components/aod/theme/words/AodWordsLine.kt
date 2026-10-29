package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.words

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.words.AodWordsWeightValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.words.WordsThemeSpec

@Composable
fun AodWordsLine(
    text: String,
    slot: Dp,
    weight: AodWordsWeightValue,
    config: AodThemeConfig,
    colorOption: AodColorOption,
    modifier: Modifier = Modifier,
) {
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier.semantics { contentDescription = text }) {
        val maxWidth = constraints.maxWidth

        Crossfade(
            targetState = text,
            animationSpec = tween(durationMillis = WordsMetrics.WORD_MILLIS),
            label = "AodWordsLine",
        ) { words ->
            val line = remember(words, slot, weight, maxWidth, density) {
                measurer.fitLine(text = words, slot = slot, weight = weight, maxWidth = maxWidth, density = density)
            }

            Spacer(
                modifier = Modifier
                    .size(width = with(density) { line.size.width.toDp() }, height = slot)
                    .drawWithCache {
                        val origin = Offset(x = 0f, y = WordsMetrics.lineTop(weight) * line.layoutInput.style.fontSize.toPx() - line.firstBaseline)
                        val bounds = Rect(0f, 0f, line.size.width.toFloat(), line.size.height.toFloat())
                        val brush = config.gradientOf(colorOption)?.toBrush(bounds) ?: SolidColor(config[colorOption])

                        onDrawBehind {
                            drawText(textLayoutResult = line, brush = brush, topLeft = origin)
                        }
                    },
            )
        }
    }
}

private fun TextMeasurer.fitLine(
    text: String,
    slot: Dp,
    weight: AodWordsWeightValue,
    maxWidth: Int,
    density: Density,
): TextLayoutResult {
    val em = slot / WordsMetrics.lineBox(weight)
    val full = measureLine(text = text, em = em, weight = weight, density = density)

    return if (full.size.width <= maxWidth) full else measureLine(text = text, em = em * (maxWidth.toFloat() / full.size.width), weight = weight, density = density)
}

private fun TextMeasurer.measureLine(
    text: String,
    em: Dp,
    weight: AodWordsWeightValue,
    density: Density,
): TextLayoutResult = measure(
    text = text,
    style = TextStyle(fontFamily = WordsThemeSpec.Font, fontWeight = weight.fontWeight, fontSize = with(density) { em.toSp() }),
    softWrap = false,
    maxLines = 1,
    density = density,
)

@Preview(widthDp = 313)
@Composable
private fun AodWordsLinePreview() {
    AodWordsLine(
        text = "Hai mươi bảy",
        slot = 40.dp,
        weight = AodWordsWeightValue.BOLD,
        config = AodThemeConfig(),
        colorOption = WordsThemeSpec.MinuteColor,
    )
}
