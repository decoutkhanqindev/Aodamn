package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bezel

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice

@Immutable
enum class AodBezelTicksValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    MINUTES(code = 0, labelRes = R.string.bezel_ticks_minutes),
    HOURS(code = 1, labelRes = R.string.bezel_ticks_hours),
}
