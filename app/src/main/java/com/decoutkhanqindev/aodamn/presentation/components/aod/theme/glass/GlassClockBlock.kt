package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.glass

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.glass.GlassThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import kotlin.math.roundToInt

class GlassClockBlock {
    private val bounds = Rect()

    var width = 0
        private set
    var height = 0
        private set
    var left = 0
        private set
    var top = 0
        private set

    fun measure(
        time: String,
        textSizePx: Float,
        typeface: Typeface,
        isOutline: Boolean,
    ) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.textSize = textSizePx
            this.typeface = typeface
            style = if (isOutline) Paint.Style.STROKE else Paint.Style.FILL
            strokeWidth = if (isOutline) textSizePx * GlassMetrics.RAIL_FRACTION else 0f
        }
        paint.getTextBounds(time, 0, time.length, bounds)
        width = bounds.width()
        height = bounds.height()
        left = bounds.left
        top = bounds.top
    }
}

@Composable
fun AodGlassClock(
    state: AodState,
    size: TextUnit,
    modifier: Modifier = Modifier,
) {
    val time = rememberZonedDateTime(state.nowMillis).format(
        rememberFormatter(if (is24HourFormat()) R.string.aod_glass_time_24h else R.string.aod_glass_time_12h),
    )
    val config = state.appearance.theme.config
    val clockStyle = config[GlassThemeSpec.Style]
    val typeface = resolveTypeface(clockStyle.fontFamily, clockStyle.fontWeight, Typeface.DEFAULT)
    val density = LocalDensity.current
    val textSizePx = with(density) { size.toPx() }
    val block = remember(time, textSizePx, typeface, clockStyle) {
        GlassClockBlock().apply {
            measure(time = time, textSizePx = textSizePx, typeface = typeface, isOutline = clockStyle.isOutline)
        }
    }

    val currentTime = rememberUpdatedState(time)
    val currentColor = rememberUpdatedState(config[GlassThemeSpec.ClockColor].toArgb())
    val gradient = config.gradientOf(GlassThemeSpec.ClockColor)

    Box(
        modifier = modifier
            .layout { measurable, constraints ->
                val width = block.width.coerceIn(constraints.minWidth, constraints.maxWidth)
                val height = block.height.coerceIn(constraints.minHeight, constraints.maxHeight)
                val placeable = measurable.measure(constraints.copy(minWidth = width, maxWidth = width, minHeight = height, maxHeight = height))
                layout(width, height) { placeable.place(0, 0) }
            }
            .drawWithCache {
                val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    this.textSize = textSizePx
                    this.typeface = typeface
                    style = if (clockStyle.isOutline) Paint.Style.STROKE else Paint.Style.FILL
                    strokeWidth = if (clockStyle.isOutline) textSizePx * GlassMetrics.RAIL_FRACTION else 0f
                    shader = gradient?.toShader(0f, 0f, this@drawWithCache.size.width, this@drawWithCache.size.height)
                }

                onDrawBehind {
                    paint.color = currentColor.value
                    paint.alpha = (GlassMetrics.CLOCK_ALPHA * 255).roundToInt()
                    drawContext.canvas.nativeCanvas.drawText(
                        currentTime.value,
                        -block.left.toFloat(),
                        -block.top.toFloat(),
                        paint,
                    )
                }
            },
    )
}
