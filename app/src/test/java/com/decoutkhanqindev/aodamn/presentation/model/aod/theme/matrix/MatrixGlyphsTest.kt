package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MatrixGlyphsTest {
    @Test
    fun `time cells keep the expected dimensions and lit count`() {
        val cells = MatrixGlyphs.cells("08:30")
        val expectedLit = listOf('0', '8', ':', '3', '0').sumOf { character ->
            val rows = if (character == ':') MatrixGlyphs.Colon else MatrixGlyphs.Digits.getValue(character)
            rows.sumOf { row -> row.count { it == '#' } }
        }

        assertEquals(25, cells.columns)
        assertEquals(7, cells.rows)
        assertEquals(expectedLit, cells.lit.size)
    }

    @Test
    fun `colon does not create unlit cells`() {
        val cells = MatrixGlyphs.cells("08:30")

        assertFalse(cells.unlit.any { it.column == 12 })
    }

    @Test
    fun `every digit has a five by seven grid`() {
        ('0'..'9').forEach { digit ->
            val rows = MatrixGlyphs.Digits.getValue(digit)
            assertEquals(7, rows.size)
            assertTrue(rows.all { it.length == 5 })
        }
    }

    @Test
    fun `lit and unlit cells cover every digit cell`() {
        val cells = MatrixGlyphs.cells("88:88")
        val covered = (cells.lit + cells.unlit).toSet()
        val expected = buildSet {
            listOf(0, 6, 14, 20).forEach { start ->
                repeat(7) { row ->
                    repeat(5) { column -> add(MatrixCell(column = start + column, row = row)) }
                }
            }
        }

        assertTrue(covered.containsAll(expected))
        assertEquals(4 * 35, covered.count { it.column != 12 })
    }

    @Test
    fun `bolt grid keeps every lit cell`() {
        val expected = MatrixGlyphs.Bolt.sumOf { row -> row.count { it == '#' } }

        assertEquals(expected, MatrixGlyphs.gridCells(MatrixGlyphs.Bolt).size)
    }
}
