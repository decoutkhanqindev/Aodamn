package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.fade

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice

@Immutable
enum class AodFadeWeightValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
    val fontWeight: FontWeight,
) : AodThemeChoice {
    THIN(code = 0, labelRes = R.string.font_weight_thin, fontWeight = FontWeight.Thin),
    LIGHT(code = 1, labelRes = R.string.font_weight_light, fontWeight = FontWeight.Light),
    REGULAR(code = 2, labelRes = R.string.font_weight_regular, fontWeight = FontWeight.Normal),
    BOLD(code = 3, labelRes = R.string.font_weight_bold, fontWeight = FontWeight.Bold),
}
