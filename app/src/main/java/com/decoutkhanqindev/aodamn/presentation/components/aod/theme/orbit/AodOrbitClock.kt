package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.orbit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.rememberLiveClockMillis
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.orbit.OrbitThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import java.util.TimeZone
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun AodOrbitClock(
    nowMillis: Long,
    isLive: Boolean,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val clockMillis = rememberLiveClockMillis(isLive)
    val epochMillis = remember(nowMillis, isLive) { { if (isLive) clockMillis.longValue else nowMillis } }
    val minuteMillis by remember(nowMillis, isLive) {
        derivedStateOf { if (isLive) clockMillis.longValue / OrbitMetrics.MINUTE_MILLIS * OrbitMetrics.MINUTE_MILLIS else nowMillis }
    }
    val scale = config[OrbitThemeSpec.Size] / OrbitMetrics.SIZE_DIVISOR
    val time = rememberZonedDateTime(minuteMillis)
    val description = time.format(rememberTimeFormatter())

    Box(
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val center = size.center
                    val radius = min(size.width, size.height) / 2f * scale
                    val disc = Rect(center = center, radius = radius)
                    val orbit = config.gradientOf(OrbitThemeSpec.ClockColor)?.toBrush(disc) ?: SolidColor(config[OrbitThemeSpec.ClockColor])
                    val hourRadius = radius * OrbitMetrics.HOUR_PLANET
                    val minuteRadius = radius * OrbitMetrics.MINUTE_PLANET
                    val secondRadius = radius * OrbitMetrics.SECOND_PLANET
                    val hour = config.planetBrush(OrbitThemeSpec.HourColor, hourRadius)
                    val minute = config.planetBrush(OrbitThemeSpec.MinuteColor, minuteRadius)
                    val second = config.planetBrush(OrbitThemeSpec.SecondColor, secondRadius)
                    val stroke = Stroke(width = OrbitMetrics.OrbitLine.toPx() * scale)
                    val inner = radius * OrbitMetrics.INNER_ORBIT
                    val middle = radius * OrbitMetrics.MIDDLE_ORBIT

                    onDrawBehind {
                        val local = localMillisOf(epochMillis())
                        drawCircle(color = Black, radius = radius, center = center, alpha = OrbitMetrics.DISC_ALPHA)
                        drawCircle(brush = orbit, radius = inner, center = center, alpha = OrbitMetrics.INNER_ALPHA, style = stroke)
                        drawCircle(brush = orbit, radius = middle, center = center, alpha = OrbitMetrics.MIDDLE_ALPHA, style = stroke)
                        drawCircle(brush = orbit, radius = radius, center = center, alpha = OrbitMetrics.OUTER_ALPHA, style = stroke)
                        drawPlanet(brush = hour, radius = hourRadius, center = planetOf(center, inner, degreesOf(local, OrbitMetrics.HALF_DAY_MILLIS)))
                        drawPlanet(brush = minute, radius = minuteRadius, center = planetOf(center, middle, degreesOf(local, OrbitMetrics.HOUR_MILLIS)))
                        drawPlanet(brush = second, radius = secondRadius, center = planetOf(center, radius, degreesOf(local, OrbitMetrics.MINUTE_MILLIS)))
                    }
                },
        )

        AnimatedVisibility(visible = config[OrbitThemeSpec.Digital], enter = fadeIn(), exit = fadeOut()) {
            Text(
                text = time.format(rememberTimeFormatter()),
                style = TextStyle(
                    brush = config.brushOf(OrbitThemeSpec.ClockColor),
                    fontSize = (OrbitMetrics.DIGITAL_SIZE * scale).sp,
                    fontFamily = OrbitThemeSpec.DigitalFont,
                    letterSpacing = OrbitMetrics.DigitalSpacing,
                ),
                maxLines = 1,
            )
        }
    }
}

private fun AodThemeConfig.planetBrush(
    option: AodColorOption,
    radius: Float,
): Brush = gradientOf(option)?.toBrush(Rect(center = Offset.Zero, radius = radius)) ?: SolidColor(this[option])

private fun DrawScope.drawPlanet(
    brush: Brush,
    radius: Float,
    center: Offset,
) {
    translate(left = center.x, top = center.y) {
        drawCircle(brush = brush, radius = radius, center = Offset.Zero)
    }
}

private fun localMillisOf(epochMillis: Long): Long = epochMillis + TimeZone.getDefault().getOffset(epochMillis)

private fun degreesOf(localMillis: Long, periodMillis: Long): Float = Math.floorMod(localMillis, periodMillis) * 360f / periodMillis

private fun planetOf(center: Offset, orbit: Float, degrees: Float): Offset {
    val radians = Math.toRadians(degrees.toDouble())
    return Offset(x = center.x + orbit * sin(radians).toFloat(), y = center.y - orbit * cos(radians).toFloat())
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodOrbitClockPreview() {
    AodOrbitClock(
        nowMillis = System.currentTimeMillis(),
        isLive = false,
        config = AodThemeConfig(),
        modifier = Modifier.fillMaxSize(),
    )
}
