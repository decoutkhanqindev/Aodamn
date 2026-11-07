package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.eclipse

import android.graphics.BlendMode
import android.graphics.BlurMaskFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.eclipse.EclipseThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlinx.coroutines.delay

@Composable
fun AodEclipseClock(
    nowMillis: Long,
    isLive: Boolean,
    isPreview: Boolean,
    isStill: Boolean,
    isLandscape: Boolean,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    var previewMillis by remember { mutableLongStateOf(nowMillis) }

    if (isLive && isPreview) {
        LaunchedEffect(Unit) {
            while (true) {
                val current = System.currentTimeMillis()
                previewMillis = current
                delay(EclipseMetrics.MINUTE_MILLIS - current % EclipseMetrics.MINUTE_MILLIS + EclipseMetrics.TICK_SLACK_MILLIS)
            }
        }
    }

    val displayMillis = if (isLive && isPreview) previewMillis else nowMillis
    val time = rememberZonedDateTime(displayMillis)
    val text = time.format(rememberFormatter(if (is24HourFormat()) R.string.aod_time_pattern_24h else R.string.aod_eclipse_time_12h))
    val description = time.format(rememberTimeFormatter())
    val hourFraction = EclipseMetrics.hourFraction(time.hour, time.minute)
    val currentText by rememberUpdatedState(text)
    val currentRotation by rememberUpdatedState(EclipseMetrics.rotationDegrees(hourFraction, config[EclipseThemeSpec.Rotation]))
    val currentPeek by rememberUpdatedState(EclipseMetrics.peek(hourFraction, time.hour, config[EclipseThemeSpec.Peek]))
    val opening = remember(isStill) { Animatable(if (isStill) 1f else 0f) }

    if (!isStill) {
        LaunchedEffect(opening) {
            opening.animateTo(1f, tween(EclipseMetrics.OPEN_MILLIS, easing = FastOutSlowInEasing))
        }
    }

    val typeface = resolveTypeface(EclipseThemeSpec.Font, FontWeight.Normal, Typeface.DEFAULT)
    val coronaColors = remember(config) {
        EclipseMetrics.coronaColors(
            config.colorsOf(EclipseThemeSpec.RingColor),
            config.colorsOf(EclipseThemeSpec.ShadowColor),
        )
    }
    val rimColors = remember(config) { EclipseMetrics.rimColors(config.colorsOf(EclipseThemeSpec.RingColor)) }
    val digitColor = config[EclipseThemeSpec.CharacterColor].toArgb()
    val digitGradient = config.gradientOf(EclipseThemeSpec.CharacterColor)

    Spacer(
        modifier = modifier
            .fillMaxSize()
            .clearAndSetSemantics { contentDescription = description }
            .drawWithCache {
                val span = if (isLandscape) {
                    size.height - EclipseMetrics.LandscapeInset.toPx() * 2f
                } else {
                    minOf(size.width, size.height)
                }
                val centerX = size.width / 2f
                val centerY = if (isLandscape) {
                    EclipseMetrics.landscapeCenterY(
                        size.height,
                        EclipseMetrics.LandscapeInset.toPx(),
                        EclipseMetrics.LandscapeBandGap.toPx(),
                        EclipseMetrics.LandscapeBand.toPx(),
                    )
                } else {
                    size.height / 2f
                }
                val discRadius = span * EclipseMetrics.DISC_RADIUS
                val coronaRadius = span * EclipseMetrics.CORONA_RADIUS
                val coronaPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    blendMode = BlendMode.SCREEN
                    shader = RadialGradient(
                        0f,
                        0f,
                        coronaRadius,
                        coronaColors,
                        EclipseMetrics.CoronaStops,
                        Shader.TileMode.CLAMP,
                    )
                }
                val rimPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    blendMode = BlendMode.SCREEN
                    shader = LinearGradient(
                        centerX - discRadius,
                        0f,
                        centerX + discRadius,
                        0f,
                        rimColors[0],
                        rimColors[1],
                        Shader.TileMode.CLAMP,
                    )
                    maskFilter = BlurMaskFilter(span * EclipseMetrics.RIM_BLUR, BlurMaskFilter.Blur.SOLID)
                }
                val discPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Black.toArgb()
                    alpha = EclipseMetrics.DISC_ALPHA
                }
                val digitSize = (if (isLandscape) EclipseMetrics.LandscapeDigit else EclipseMetrics.PortraitDigit).toPx()
                val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    this.typeface = typeface
                    textAlign = Paint.Align.CENTER
                    textSize = digitSize
                    color = digitColor
                    shader = digitGradient?.toShader(
                        left = centerX - digitSize * EclipseMetrics.TEXT_HALF_WIDTH_EM,
                        top = centerY - digitSize,
                        right = centerX + digitSize * EclipseMetrics.TEXT_HALF_WIDTH_EM,
                        bottom = centerY + digitSize,
                    )
                }
                val baseline = centerY - (textPaint.ascent() + textPaint.descent()) / 2f
                val innerPath = Path()

                onDrawBehind {
                    val reveal = opening.value
                    val peek = currentPeek * reveal
                    val innerX = centerX + EclipseMetrics.innerOffset(span, peek)
                    val innerRadius = EclipseMetrics.innerRadius(span, peek)
                    val canvas = drawContext.canvas.nativeCanvas
                    val checkpoint = canvas.save()
                    canvas.rotate(currentRotation, centerX, centerY)
                    canvas.save()
                    innerPath.rewind()
                    innerPath.addCircle(innerX, centerY, innerRadius, Path.Direction.CW)
                    canvas.clipOutPath(innerPath)
                    canvas.save()
                    canvas.translate(EclipseMetrics.coronaX(centerX, span, peek), centerY)
                    canvas.drawCircle(0f, 0f, coronaRadius, coronaPaint)
                    canvas.restore()
                    canvas.drawCircle(centerX, centerY, discRadius, rimPaint)
                    canvas.restore()
                    canvas.drawCircle(innerX, centerY, innerRadius, discPaint)
                    canvas.restoreToCount(checkpoint)
                    textPaint.letterSpacing = EclipseMetrics.letterSpacing(reveal)
                    canvas.drawText(currentText, centerX, baseline, textPaint)
                }
            },
    )
}
