package com.decoutkhanqindev.aodamn.presentation.model.aod.appearance

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Color.toStoredClockColor(): Int = toArgb() or (0xFF shl 24)

fun Color.toHexCode(): String = (toArgb() and 0xFFFFFF).toString(16).padStart(6, '0').uppercase()

fun String.toHexColorOrNull(): Color? =
    takeIf { it.length == 6 }?.toIntOrNull(16)?.let { rgb -> Color(rgb or (0xFF shl 24)) }
