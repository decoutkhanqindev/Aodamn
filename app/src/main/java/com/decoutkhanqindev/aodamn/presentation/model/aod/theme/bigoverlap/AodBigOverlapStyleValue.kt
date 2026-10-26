package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice

@Immutable
enum class AodBigOverlapStyleValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
    val isOutline: Boolean,
    val isRounded: Boolean,
) : AodThemeChoice {
    FILLED(code = 0, labelRes = R.string.tile_style_filled, isOutline = false, isRounded = false),
    OUTLINE(code = 1, labelRes = R.string.tile_style_outline, isOutline = true, isRounded = false),
    FILLED_ROUND(code = 2, labelRes = R.string.big_overlap_style_filled_round, isOutline = false, isRounded = true),
    OUTLINE_ROUND(code = 3, labelRes = R.string.big_overlap_style_outline_round, isOutline = true, isRounded = true),
}
