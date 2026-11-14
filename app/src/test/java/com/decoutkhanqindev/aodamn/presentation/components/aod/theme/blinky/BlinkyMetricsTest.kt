package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.blinky

import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.blinky.BlinkyLineValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BlinkyMetricsTest {

    @Test
    fun `blink openness follows the blink curve`() {
        assertEquals(0.7f, BlinkyMetrics.blinkOpenness(0f), 0.001f)
        assertEquals(0f, BlinkyMetrics.blinkOpenness(0.5f), 0.001f)
        assertEquals(0.7f, BlinkyMetrics.blinkOpenness(1f), 0.001f)
    }

    @Test
    fun `sleep openness follows the supplied frames`() {
        assertEquals(0.7f, BlinkyMetrics.sleepOpenness(0f), 0.001f)
        assertEquals(0f, BlinkyMetrics.sleepOpenness(1f), 0.001f)
        assertEquals(0.7f, BlinkyMetrics.sleepOpenness(13f / 80f), 0.001f)
        assertEquals(0f, BlinkyMetrics.sleepOpenness(14f / 80f), 0.001f)
        assertEquals(0.1f, BlinkyMetrics.sleepOpenness(36f / 80f), 0.001f)
    }

    @Test
    fun `sleep delay uses night and day ranges`() {
        listOf(22, 23, 0, 3, 6).forEach { hour ->
            repeat(1_000) { assertTrue(BlinkyMetrics.sleepDelaySeconds(hour) in 11..60) }
        }
        listOf(7, 12, 21).forEach { hour ->
            repeat(1_000) { assertTrue(BlinkyMetrics.sleepDelaySeconds(hour) in 60..359) }
        }
    }

    @Test
    fun `blink gap remains in its configured range`() {
        repeat(1_000) { assertTrue(BlinkyMetrics.blinkGapMillis() in 200L..5199L) }
    }

    @Test
    fun `eye and clock sizes scale from their options`() {
        assertEquals(36.39f, BlinkyMetrics.eyeSize(393.dp, 100).value, 0.01f)
        assertEquals(50.94f, BlinkyMetrics.eyeSize(393.dp, 140).value, 0.01f)
        assertEquals(28.6f, BlinkyMetrics.clockTextSize(22).value, 0.001f)
    }

    @Test
    fun `line holds match each content type`() {
        assertNull(BlinkyMetrics.lineHoldMillis(BlinkyLineValue.STATUS, hasMedia = false, holdSeconds = 30))
        assertEquals(30_000L, BlinkyMetrics.lineHoldMillis(BlinkyLineValue.NOTIFICATION, hasMedia = false, holdSeconds = 30))
        assertNull(BlinkyMetrics.lineHoldMillis(BlinkyLineValue.NOTIFICATION, hasMedia = false, holdSeconds = 0))
        assertEquals(30_000L, BlinkyMetrics.lineHoldMillis(BlinkyLineValue.MEDIA, hasMedia = true, holdSeconds = 30))
        assertEquals(1_000L, BlinkyMetrics.lineHoldMillis(BlinkyLineValue.MEDIA, hasMedia = false, holdSeconds = 30))
        assertNull(BlinkyMetrics.lineHoldMillis(BlinkyLineValue.MEDIA, hasMedia = true, holdSeconds = 0))
    }

    @Test
    fun `line values cycle in display order`() {
        assertEquals(BlinkyLineValue.NOTIFICATION, BlinkyLineValue.STATUS.next)
        assertEquals(BlinkyLineValue.MEDIA, BlinkyLineValue.NOTIFICATION.next)
        assertEquals(BlinkyLineValue.STATUS, BlinkyLineValue.MEDIA.next)
    }
}
