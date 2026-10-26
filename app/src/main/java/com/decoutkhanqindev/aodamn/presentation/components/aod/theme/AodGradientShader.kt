package com.decoutkhanqindev.aodamn.presentation.components.aod.theme

import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient

fun AodGradient.toShader(left: Float, top: Float, right: Float, bottom: Float): Shader = LinearGradient(
    if (direction.start.x.isInfinite()) right else left + direction.start.x,
    if (direction.start.y.isInfinite()) bottom else top + direction.start.y,
    if (direction.end.x.isInfinite()) right else left + direction.end.x,
    if (direction.end.y.isInfinite()) bottom else top + direction.end.y,
    colors.toArgbArray(),
    null,
    Shader.TileMode.CLAMP,
)

fun List<Color>.toLineShader(from: Offset, to: Offset): Shader? =
    if (size > 1) LinearGradient(from.x, from.y, to.x, to.y, toArgbArray(), null, Shader.TileMode.CLAMP) else null

private fun List<Color>.toArgbArray(): IntArray = IntArray(size) { index -> this[index].toArgb() }
