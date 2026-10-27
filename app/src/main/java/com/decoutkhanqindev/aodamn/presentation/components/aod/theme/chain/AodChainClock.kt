package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.chain

import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.chain.ChainThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyAnton
import kotlin.math.roundToInt

@Composable
fun AodChainClock(
    nowMillis: Long,
    config: AodThemeConfig,
    glyphs: ChainGlyphs,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val hours = now.format(rememberFormatter(if (is24HourFormat()) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h))
    val text = hours + ":" + now.format(rememberFormatter(R.string.aod_minute_pattern))
    val description = now.format(rememberTimeFormatter())
    val color = config[ChainThemeSpec.ClockColor]
    val brush = config.brushOf(ChainThemeSpec.ClockColor)
    val viewWidth = ChainMetrics.ClockWidth * (config[ChainThemeSpec.Size] / 100f)
    val viewHeight = viewWidth * ChainMetrics.CLOCK_ASPECT
    val density = LocalDensity.current
    val digitHeight = with(density) { (viewHeight * ChainMetrics.DIGIT).toPx() }
    val tracking = with(density) { (viewHeight * ChainMetrics.TRACKING).toPx() }
    val padX = with(density) { (viewWidth * ChainMetrics.PAD_X).toPx() }
    val padY = with(density) { (viewWidth * ChainMetrics.PAD).toPx() }

    var change by remember { mutableStateOf(text to text) }
    val progress = remember { Animatable(1f) }
    val from = remember(glyphs, change.first, digitHeight, tracking) { glyphs.layout(change.first, digitHeight, tracking) }
    val to = remember(glyphs, change.second, digitHeight, tracking) { glyphs.layout(change.second, digitHeight, tracking) }

    LaunchedEffect(text) {
        if (text != change.second) {
            change = change.second to text
            progress.snapTo(0f)
            progress.animateTo(1f, tween(durationMillis = ChainMetrics.DIGIT_CHANGE_MILLIS))
        }
    }

    Box(
        modifier = modifier
            .layout { measurable, _ ->
                val fromWidth = from.inkRight() + 2f * padX
                val toWidth = to.inkRight() + 2f * padX
                val width = (fromWidth + (toWidth - fromWidth) * progress.value).roundToInt()
                val height = viewHeight.roundToPx()
                val placeable = measurable.measure(Constraints.fixed(width, height))
                layout(width, height) { placeable.place(0, 0) }
            }
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithCache {
                val pillHeight = digitHeight + 2f * padY
                val pillTop = (size.height - pillHeight) / 2f
                val textTop = (size.height - digitHeight) / 2f
                val hole = color.copy(alpha = 1f)

                onDrawBehind {
                    val t = progress.value
                    drawRoundRect(
                        brush = brush,
                        topLeft = Offset(0f, pillTop),
                        size = Size(size.width, pillHeight),
                        cornerRadius = CornerRadius(pillHeight / 2f),
                    )
                    translate(left = padX, top = textTop) {
                        to.forEachIndexed { index, glyph ->
                            val isKept = from.getOrNull(index)?.let { old -> old.char == glyph.char && old.left == glyph.left } == true
                            drawPath(path = glyph.path, color = hole, alpha = if (isKept) 1f else t, blendMode = BlendMode.DstOut)
                        }
                        from.forEachIndexed { index, glyph ->
                            val isKept = to.getOrNull(index)?.let { new -> new.char == glyph.char && new.left == glyph.left } == true
                            if (!isKept && t < 1f) {
                                drawPath(path = glyph.path, color = hole, alpha = 1f - t, blendMode = BlendMode.DstOut)
                            }
                        }
                    }
                }
            }
            .clearAndSetSemantics { contentDescription = description },
    )
}

@Composable
fun rememberChainGlyphs(): ChainGlyphs {
    val typeface = resolveTypeface(fontFamily = FontFamilyAnton, fontWeight = FontWeight.Normal, fallback = Typeface.DEFAULT_BOLD)
    return remember(typeface) { ChainGlyphs(typeface) }
}

private fun List<ChainGlyph>.inkRight(): Float = maxOfOrNull { glyph -> glyph.path.getBounds().right } ?: 0f

@Preview(widthDp = 200, heightDp = 100)
@Composable
private fun AodChainClockPreview() {
    AodChainClock(
        nowMillis = System.currentTimeMillis(),
        config = AodThemeConfig(),
        glyphs = rememberChainGlyphs(),
    )
}
