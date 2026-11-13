package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.meridian

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice

@Immutable
enum class AodMeridianStyleValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    OUTLINE(code = 0, labelRes = R.string.meridian_style_outline),
    FIRST(code = 1, labelRes = R.string.meridian_style_first),
    FILLED(code = 2, labelRes = R.string.meridian_style_filled),
    BOLD(code = 3, labelRes = R.string.meridian_style_bold),
    ;

    fun isFilled(index: Int): Boolean = when (this) {
        FILLED -> true
        FIRST -> index == 0
        else -> false
    }

    val strokeAlpha: Float
        get() = if (this == BOLD) 1f else .196f
}
