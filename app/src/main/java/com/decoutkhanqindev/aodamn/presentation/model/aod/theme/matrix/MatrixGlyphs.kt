package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix

import androidx.compose.runtime.Immutable

@Immutable
data class MatrixCell(val column: Int, val row: Int)

@Immutable
data class MatrixCells(
    val lit: List<MatrixCell>,
    val unlit: List<MatrixCell>,
    val columns: Int,
    val rows: Int,
)

object MatrixGlyphs {
    const val ROWS = 7

    val Digits: Map<Char, List<String>> = mapOf(
        '0' to listOf(" ### ", "#   #", "#   #", "#   #", "#   #", "#   #", " ### "),
        '1' to listOf("  #  ", " ##  ", "  #  ", "  #  ", "  #  ", "  #  ", " ### "),
        '2' to listOf(" ### ", "#   #", "    #", "   # ", "  #  ", " #   ", "#####"),
        '3' to listOf(" ### ", "#   #", "    #", "  ## ", "    #", "#   #", " ### "),
        '4' to listOf("   # ", "  ## ", " # # ", "#  # ", "#####", "   # ", "   # "),
        '5' to listOf("#####", "#    ", "#    ", "#### ", "    #", "#   #", " ### "),
        '6' to listOf("  ## ", " #   ", "#    ", "#### ", "#   #", "#   #", " ### "),
        '7' to listOf("#####", "    #", "   # ", "  #  ", " #   ", " #   ", " #   "),
        '8' to listOf(" ### ", "#   #", "#   #", " ### ", "#   #", "#   #", " ### "),
        '9' to listOf(" ### ", "#   #", "#   #", " ####", "    #", "   # ", " ##  "),
    )

    val Colon: List<String> = listOf(" ", " ", "#", " ", "#", " ", " ")
    val Bolt: List<String> = listOf("   ##", "  ## ", " ##  ", "#####", "  ## ", " ##  ", "##   ")
    val Calendar: List<String> = listOf("#######", "# # # #", "#######", "#     #", "#  ## #", "#  ## #", "#######")
    val Play: List<String> = List(11) { row ->
        buildString {
            repeat(6) { column -> append(if (row in column..(10 - column)) '#' else ' ') }
        }
    }
    val Pause: List<String> = List(11) { "##  ##" }

    fun cells(text: String): MatrixCells {
        val glyphs = text.mapNotNull { character ->
            when {
                character in Digits -> Digits.getValue(character) to true
                character == ':' -> Colon to false
                else -> null
            }
        }
        val lit = mutableListOf<MatrixCell>()
        val unlit = mutableListOf<MatrixCell>()
        var columnOffset = 0

        glyphs.forEachIndexed { index, (rows, hasUnlit) ->
            rows.forEachIndexed { row, cells ->
                cells.forEachIndexed { column, cell ->
                    val matrixCell = MatrixCell(column = columnOffset + column, row = row)
                    if (cell == '#') {
                        lit += matrixCell
                    } else if (hasUnlit) {
                        unlit += matrixCell
                    }
                }
            }
            columnOffset += rows.maxOfOrNull { it.length } ?: 0
            if (index < glyphs.lastIndex) columnOffset++
        }

        return MatrixCells(lit = lit, unlit = unlit, columns = columnOffset, rows = ROWS)
    }

    fun gridCells(rows: List<String>): List<MatrixCell> = buildList {
        rows.forEachIndexed { row, cells ->
            cells.forEachIndexed { column, cell ->
                if (cell == '#') add(MatrixCell(column = column, row = row))
            }
        }
    }
}
