package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gentle

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice

@Immutable
enum class AodGentleWeightValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
    val fontWeight: FontWeight,
) : AodThemeChoice {
    EXTRA_LIGHT(code = 0, labelRes = R.string.font_weight_extra_light, fontWeight = FontWeight.ExtraLight),
    REGULAR(code = 1, labelRes = R.string.font_weight_regular, fontWeight = FontWeight.Normal),
    BOLD(code = 2, labelRes = R.string.font_weight_bold, fontWeight = FontWeight.Bold),
}
