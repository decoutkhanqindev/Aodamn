package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.poster

import org.junit.Assert.assertEquals
import org.junit.Test

class PosterMetricsTest {

    @Test
    fun `base size follows portrait and landscape edges`() {
        assertEquals(248.4f, PosterMetrics.baseSizePx(1080f, 2400f, false, 100), 0.001f)
        assertEquals(220.8f, PosterMetrics.baseSizePx(2400f, 1080f, true, 100), 0.001f)
        assertEquals(347.76f, PosterMetrics.baseSizePx(1080f, 2400f, false, 140), 0.001f)
    }

    @Test
    fun `fit scale shrinks only content that overflows`() {
        assertEquals(1f, PosterMetrics.fitScale(100f, 100f), 0.001f)
        assertEquals(.5f, PosterMetrics.fitScale(200f, 100f), 0.001f)
        assertEquals(1f, PosterMetrics.fitScale(0f, 100f), 0.001f)
    }

    @Test
    fun `centered top centers the block in its region`() {
        assertEquals(40f, PosterMetrics.centeredTop(100f, 20f), 0.001f)
    }
}
