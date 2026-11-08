package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pixel

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice

@Immutable
enum class AodPixelStyleValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
    val isOutline: Boolean,
) : AodThemeChoice {
    FILLED(code = 0, labelRes = R.string.tile_style_filled, isOutline = false),
    OUTLINE(code = 1, labelRes = R.string.tile_style_outline, isOutline = true),
}
