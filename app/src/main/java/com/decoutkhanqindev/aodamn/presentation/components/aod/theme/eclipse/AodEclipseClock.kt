package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.eclipse

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import kotlinx.coroutines.delay
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.eclipse.EclipseThemeSpec
import java.util.TimeZone
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AodEclipseClock(
    modifier: Modifier = Modifier,
    nowMillis: Long,
    isLive: Boolean,
    isPreview: Boolean,
    isStill: Boolean,
    isLandscape: Boolean,
    config: AodThemeConfig,
) {
    var previewMillis by remember { mutableLongStateOf(nowMillis) }

    SideEffect(nowMillis, isLive, isPreview) {
        if (!isLive || !isPreview) previewMillis = nowMillis
    }

    LaunchedEffect(isLive, isPreview) {
        while (isLive && isPreview) {
            val current = System.currentTimeMillis()
            previewMillis = current
            delay(EclipseMetrics.MINUTE_MILLIS - current % EclipseMetrics.MINUTE_MILLIS + EclipseMetrics.TICK_SLACK_MILLIS)
        }
    }

    val displayMillis = if (isLive && isPreview) previewMillis else nowMillis
    val time = rememberZonedDateTime(displayMillis)
    val text = time.format(rememberFormatter(if (is24HourFormat()) R.string.aod_time_pattern_24h else R.string.aod_time_pattern_12h))
    val description = time.format(rememberTimeFormatter())
    val typeface = resolveTypeface(EclipseThemeSpec.Font, androidx.compose.ui.text.font.FontWeight.Normal, Typeface.DEFAULT)
    val textPaint = remember(typeface) {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.typeface = typeface
            textAlign = Paint.Align.CENTER
        }
    }
    val opening = remember { Animatable(0f) }
    val ringColor = config[EclipseThemeSpec.RingColor]
    val ringGradient = config.gradientOf(EclipseThemeSpec.RingColor)
    val shadowColor = config[EclipseThemeSpec.ShadowColor]
    val shadowGradient = config.gradientOf(EclipseThemeSpec.ShadowColor)
    val characterColor = config[EclipseThemeSpec.CharacterColor]
    val characterGradient = config.gradientOf(EclipseThemeSpec.CharacterColor)
    val rotation = config[EclipseThemeSpec.Rotation]
    val peek = config[EclipseThemeSpec.Peek]

    LaunchedEffect(text, isStill) {
        opening.snapTo(if (isStill) 1f else 0f)
        if (!isStill) opening.animateTo(1f, tween(durationMillis = EclipseMetrics.OPEN_MILLIS))
    }

    Spacer(
        modifier = modifier
            .fillMaxSize()
            .clearAndSetSemantics { contentDescription = description }
            .drawWithCache {
                val radius = minOf(size.width, size.height) * if (isLandscape) EclipseMetrics.LANDSCAPE_RADIUS else EclipseMetrics.PORTRAIT_RADIUS
                val center = size.center
                val ringBounds = Rect(center = center, radius = radius)
                val ringBrush = ringGradient?.toBrush(ringBounds) ?: SolidColor(ringColor)
                val characterBounds = Rect(center = center, radius = radius * EclipseMetrics.CHARACTER_SIZE)

                onDrawBehind {
                    val currentMillis = displayMillis
                    val angle = rotation.takeUnless { it == AodRangeOption.AUTO }?.toFloat() ?: autoRotation(currentMillis)
                    val reveal = opening.value
                    val exposure = (peek.takeUnless { it == AodRangeOption.AUTO }?.let { pinnedPeek(it) } ?: autoPeek(currentMillis)) * reveal
                    val radians = (angle - 90f) * PI.toFloat() / 180f
                    val shadowCenter = Offset(
                        x = center.x + cos(radians) * radius * exposure,
                        y = center.y + sin(radians) * radius * exposure,
                    )
                    val shadowBounds = Rect(center = shadowCenter, radius = radius * EclipseMetrics.SHADOW_RADIUS)
                    val shadowBrush = shadowGradient?.toBrush(shadowBounds) ?: SolidColor(shadowColor)

                    drawCircle(brush = ringBrush, radius = radius * EclipseMetrics.HALO_RADIUS, center = center, alpha = 0.025f + reveal * 0.025f)
                    drawCircle(brush = ringBrush, radius = radius * EclipseMetrics.HALO_MIDDLE_RADIUS, center = center, alpha = 0.06f + reveal * 0.05f)
                    drawCircle(brush = ringBrush, radius = radius * EclipseMetrics.HALO_INNER_RADIUS, center = center, alpha = 0.14f + reveal * 0.08f)
                    drawCircle(brush = ringBrush, radius = radius, center = center, alpha = 0.42f)
                    drawCircle(brush = shadowBrush, radius = radius * EclipseMetrics.SHADOW_RADIUS, center = shadowCenter, alpha = 0.8f)
                    drawCircle(
                        brush = ringBrush,
                        radius = radius,
                        center = center,
                        style = Stroke(width = radius * EclipseMetrics.RING_STROKE),
                        alpha = 0.26f,
                    )

                    textPaint.textSize = radius * EclipseMetrics.CHARACTER_SIZE * (0.76f + reveal * 0.24f)
                    textPaint.letterSpacing = EclipseMetrics.CHARACTER_SPACING
                    textPaint.color = characterColor.toArgb()
                    textPaint.shader = characterGradient?.toShader(
                        left = characterBounds.left,
                        top = characterBounds.top,
                        right = characterBounds.right,
                        bottom = characterBounds.bottom,
                    )
                    val baseline = center.y - (textPaint.ascent() + textPaint.descent()) / 2f
                    drawContext.canvas.nativeCanvas.drawText(text, center.x, baseline, textPaint)
                    textPaint.shader = null
                }
            },
    )
}

private fun autoRotation(epochMillis: Long): Float {
    val localMillis = epochMillis + TimeZone.getDefault().getOffset(epochMillis)
    return Math.floorMod(localMillis, EclipseMetrics.HALF_DAY_MILLIS) * 360f / EclipseMetrics.HALF_DAY_MILLIS
}

private fun autoPeek(epochMillis: Long): Float {
    val localMillis = epochMillis + TimeZone.getDefault().getOffset(epochMillis)
    val dayFraction = Math.floorMod(localMillis, EclipseMetrics.DAY_MILLIS).toFloat() / EclipseMetrics.DAY_MILLIS
    return EclipseMetrics.MIN_AUTO_PEEK + (EclipseMetrics.MAX_AUTO_PEEK - EclipseMetrics.MIN_AUTO_PEEK) * abs(sin(dayFraction * PI.toFloat()))
}

private fun pinnedPeek(value: Int): Float = EclipseMetrics.MIN_PINNED_PEEK +
    (EclipseMetrics.MAX_PINNED_PEEK - EclipseMetrics.MIN_PINNED_PEEK) * value / 100f
