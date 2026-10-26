package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice

@Immutable
enum class AodCompassStyleValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    SECONDS(code = 0, labelRes = R.string.compass_style_seconds),
    MINUTES(code = 1, labelRes = R.string.compass_style_minutes),
}
