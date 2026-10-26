package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.card

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice

@Immutable
enum class AodCardBackgroundValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    TINTED(code = 0, labelRes = R.string.card_background_tinted),
    OUTLINE(code = 1, labelRes = R.string.card_background_outline),
    NONE(code = 2, labelRes = R.string.card_background_none),
}
