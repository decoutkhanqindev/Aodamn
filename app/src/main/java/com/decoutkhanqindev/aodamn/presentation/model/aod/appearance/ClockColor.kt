package com.decoutkhanqindev.aodamn.presentation.model.aod.appearance

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.decoutkhanqindev.aodamn.presentation.theme.Blue
import com.decoutkhanqindev.aodamn.presentation.theme.GreyB4
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.Orange
import com.decoutkhanqindev.aodamn.presentation.theme.Pink
import com.decoutkhanqindev.aodamn.presentation.theme.Purple
import com.decoutkhanqindev.aodamn.presentation.theme.Red

private val LegacyClockColors = listOf(GreyB4, GreyED, Mint, Blue, Purple, Orange, Pink, Red)

fun clockColorOf(stored: Int?): Color = when (stored) {
    null -> GreyB4
    in LegacyClockColors.indices -> LegacyClockColors[stored]
    else -> Color(stored)
}

fun Color.toStoredClockColor(): Int = toArgb() or (0xFF shl 24)

fun Color.toHexCode(): String = (toArgb() and 0xFFFFFF).toString(16).padStart(6, '0').uppercase()

fun String.toHexColorOrNull(): Color? =
    takeIf { it.length == 6 }?.toIntOrNull(16)?.let { rgb -> Color(rgb or (0xFF shl 24)) }
