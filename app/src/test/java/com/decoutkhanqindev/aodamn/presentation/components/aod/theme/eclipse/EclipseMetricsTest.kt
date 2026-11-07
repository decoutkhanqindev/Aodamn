package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.eclipse

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.theme.White
import org.junit.Assert.assertEquals
import org.junit.Test

class EclipseMetricsTest {

    @Test
    fun `hour fraction wraps every twelve hours`() {
        assertEquals(0.6667f, EclipseMetrics.hourFraction(8, 0), 0.001f)
        assertEquals(0.1944f, EclipseMetrics.hourFraction(14, 20), 0.001f)
        assertEquals(0f, EclipseMetrics.hourFraction(0, 0), 0.001f)
        assertEquals(0f, EclipseMetrics.hourFraction(12, 0), 0.001f)
    }

    @Test
    fun `rotation uses the hour fraction or pinned degrees`() {
        assertEquals(330f, EclipseMetrics.rotationDegrees(EclipseMetrics.hourFraction(8, 0), AodRangeOption.AUTO), 0.001f)
        assertEquals(160f, EclipseMetrics.rotationDegrees(EclipseMetrics.hourFraction(14, 20), AodRangeOption.AUTO), 0.001f)
        assertEquals(180f, EclipseMetrics.rotationDegrees(0f, 90), 0.001f)
        assertEquals(90f, EclipseMetrics.rotationDegrees(0f, 0), 0.001f)
    }

    @Test
    fun `peek follows the day or pinned percent`() {
        assertEquals(0.6667f, EclipseMetrics.peek(EclipseMetrics.hourFraction(8, 0), 8, AodRangeOption.AUTO), 0.001f)
        assertEquals(0.8056f, EclipseMetrics.peek(EclipseMetrics.hourFraction(14, 20), 14, AodRangeOption.AUTO), 0.001f)
        assertEquals(1f, EclipseMetrics.peek(EclipseMetrics.hourFraction(12, 0), 12, AodRangeOption.AUTO), 0.001f)
        assertEquals(0f, EclipseMetrics.peek(EclipseMetrics.hourFraction(0, 0), 0, AodRangeOption.AUTO), 0.001f)
        assertEquals(0.4f, EclipseMetrics.peek(0f, 0, 40), 0.001f)
        assertEquals(0f, EclipseMetrics.peek(0f, 0, 0), 0.001f)
    }

    @Test
    fun `disc geometry follows peek`() {
        assertEquals(5f, EclipseMetrics.innerOffset(1000f, 1f), 0.001f)
        assertEquals(246.7f, EclipseMetrics.innerRadius(1000f, 1f), 0.001f)
        assertEquals(340f, EclipseMetrics.coronaX(500f, 1000f, 1f), 0.001f)
        assertEquals(0f, EclipseMetrics.innerOffset(1000f, 0f), 0.001f)
        assertEquals(249.7f, EclipseMetrics.innerRadius(1000f, 0f), 0.001f)
        assertEquals(400f, EclipseMetrics.coronaX(500f, 1000f, 0f), 0.001f)
    }

    @Test
    fun `letter spacing opens with the eclipse`() {
        assertEquals(1.8f, EclipseMetrics.letterSpacing(0f), 0.001f)
        assertEquals(2.1f, EclipseMetrics.letterSpacing(1f), 0.001f)
    }

    @Test
    fun `landscape center leaves room for the bottom band`() {
        assertEquals(158.5f, EclipseMetrics.landscapeCenterY(393f, 30f, 30f, 46f), 0.001f)
    }

    @Test
    fun `mix uses linear sRGB channels`() {
        assertEquals(0.9294f, EclipseMetrics.mix(Color(0xFFA75F82), White, 0.8f).red, 0.001f)
    }

    @Test
    fun `cap lightness preserves dark colors and caps bright colors`() {
        val capped = EclipseMetrics.capLightness(Color(0xFF1B1C38), 0.15f)
        assertEquals(0.15f, (maxOf(capped.red, capped.green, capped.blue) + minOf(capped.red, capped.green, capped.blue)) / 2f, 0.005f)
        assertEquals(Color(0xFF101010), EclipseMetrics.capLightness(Color(0xFF101010), 0.15f))
        assertEquals(Color(0.15f, 0.15f, 0.15f), EclipseMetrics.capLightness(White, 0.15f))
    }

    @Test
    fun `corona and rim colors share their outer highlight`() {
        val ring = listOf(Color(0xFFA75F82))
        val shadow = listOf(Color(0xFF1B1C38))
        val corona = EclipseMetrics.coronaColors(ring, shadow)
        val rim = EclipseMetrics.rimColors(ring)

        assertEquals(5, corona.size)
        assertEquals(rim.first(), corona.first())
        assertEquals(128, corona[3] ushr 24)
        assertEquals(Color.Transparent.toArgb(), corona[4])
        assertEquals(5, EclipseMetrics.CoronaStops.size)
    }
}
