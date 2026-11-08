package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pixel

import org.junit.Assert.assertEquals
import org.junit.Test

class PixelMetricsTest {

    @Test
    fun `clock size follows portrait and landscape edges`() {
        assertEquals(194.4f, PixelMetrics.clockSizePx(1080f, 2400f, false, 100), 0.001f)
        assertEquals(172.8f, PixelMetrics.clockSizePx(2400f, 1080f, true, 100), 0.001f)
        assertEquals(233.28f, PixelMetrics.clockSizePx(1080f, 2400f, false, 120), 0.001f)
    }

    @Test
    fun `block top positions portrait and landscape clock blocks`() {
        assertEquals(628f, PixelMetrics.blockTop(false, 2400f, 20f), 0.001f)
        assertEquals(530f, PixelMetrics.blockTop(true, 1080f, 20f), 0.001f)
    }

    @Test
    fun `center x uses the clock column in landscape`() {
        assertEquals(540f, PixelMetrics.centerX(false, 1080f), 0.001f)
        assertEquals(540f, PixelMetrics.centerX(true, 2400f), 0.001f)
    }
}
