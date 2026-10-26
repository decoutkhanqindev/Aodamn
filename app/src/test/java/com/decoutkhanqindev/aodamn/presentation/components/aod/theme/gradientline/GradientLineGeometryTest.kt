package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline

import org.junit.Assert.assertEquals
import org.junit.Test

class GradientLineGeometryTest {
    private val th = 20f

    @Test
    fun `vertical follows the reference from top to bottom with a date row`() {
        val start = vertical(fraction = 0f, rowHeight = 0.6f * th)
        assertEquals(th, start.timeCenter.y, DELTA)
        assertEquals(150f, start.timeCenter.x, DELTA)
        assertEquals(th - 1.1f * th, start.cut.top, DELTA)
        assertEquals(th + 2f * th, start.cut.bottom, DELTA)
        assertEquals(th + 1.2f * th, start.rowCenter.y, DELTA)

        val end = vertical(fraction = 1f, rowHeight = 0.6f * th)
        assertEquals(HEIGHT - 2f * th, end.timeCenter.y, DELTA)
    }

    @Test
    fun `vertical without date only cuts around the time and keeps the reference travel`() {
        val shape = vertical(fraction = 0.5f, rowHeight = 0f)
        val center = th + (HEIGHT - 3f * th) * 0.5f
        assertEquals(center, shape.timeCenter.y, DELTA)
        assertEquals(center + 1.1f * th, shape.cut.bottom, DELTA)
    }

    @Test
    fun `vertical makes room for a tall date row`() {
        val shape = vertical(fraction = 1f, rowHeight = 2f * th)
        val rowOffset = 0.85f * th + th
        assertEquals(shape.timeCenter.y + rowOffset, shape.rowCenter.y, DELTA)
        assertEquals(HEIGHT - (rowOffset + th), shape.timeCenter.y, DELTA)
        assertEquals(shape.timeCenter.y + rowOffset + th + 0.45f * th, shape.cut.bottom, DELTA)
    }

    @Test
    fun `horizontal runs left to right inside the area and cuts around the time only`() {
        val start = horizontal(fraction = 0f)
        assertEquals(60f, start.timeCenter.x, DELTA)
        assertEquals(HEIGHT / 2f - 0.35f * th, start.timeCenter.y, DELTA)
        assertEquals(60f - 40f - 0.6f * th, start.cut.left, DELTA)
        assertEquals(60f + 40f + 0.6f * th, start.cut.right, DELTA)

        val end = horizontal(fraction = 1f)
        assertEquals(WIDTH - 60f, end.timeCenter.x, DELTA)
        assertEquals(end.timeCenter.y, end.lineStart.y, DELTA)
    }

    private fun vertical(fraction: Float, rowHeight: Float) = GradientLineGeometry.vertical(
        width = WIDTH,
        height = HEIGHT,
        lineWidth = 2f,
        textHeight = th,
        rowHeight = rowHeight,
        fraction = fraction,
    )

    private fun horizontal(fraction: Float) = GradientLineGeometry.horizontal(
        width = WIDTH,
        height = HEIGHT,
        lineWidth = 2f,
        textHeight = th,
        textWidth = 80f,
        rowWidth = 120f,
        rowHeight = 0.6f * th,
        fraction = fraction,
    )

    private companion object {
        const val WIDTH = 300f
        const val HEIGHT = 600f
        const val DELTA = 0.01f
    }
}
