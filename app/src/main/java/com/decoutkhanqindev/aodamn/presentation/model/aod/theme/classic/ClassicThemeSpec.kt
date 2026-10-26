package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.classic

import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.ClockFaceValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

object ClassicThemeSpec : AodThemeSpec {
    val Face = AodChoiceOption(
        key = "face",
        labelRes = R.string.opt_clock_face,
        entries = ClockFaceValue.entries.toImmutableList(),
        default = ClockFaceValue.STACKED,
    )
    val Font = AodChoiceOption(
        key = "font",
        labelRes = R.string.opt_clock_font,
        entries = ClockFontValue.entries.toImmutableList(),
        default = ClockFontValue.DEFAULT,
    )

    val HourColor = AodColorOption(key = "color", labelRes = R.string.theme_color_hour, default = Mint, isGradientAllowed = true)
    val MinuteColor = AodColorOption(
        key = "minute_color",
        labelRes = R.string.theme_color_minute,
        default = GreyED,
        isGradientAllowed = true,
    )
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 60..150,
        step = 10,
        default = 100,
    )

    val ClockTab = AodThemeTab(
        iconRes = R.drawable.ic_clock,
        labelRes = R.string.theme_tab_clock,
        options = persistentListOf(Face, Font, Size, HourColor, MinuteColor),
    )

    override val tabs = persistentListOf(ClockTab)

    override val ownBlocks = persistentSetOf<AodBlockValue>()
}
