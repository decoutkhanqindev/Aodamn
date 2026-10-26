package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.jersey

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Orange
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

object JerseyThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyPlusJakartaSans

    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 50..100,
        step = 10,
        default = 100,
    )
    val HourColor = AodColorOption(key = "hour_color", labelRes = R.string.theme_color_hour, default = Orange, isGradientAllowed = true)
    val MinuteColor = AodColorOption(key = "minute_color", labelRes = R.string.theme_color_minute, default = GreyED, isGradientAllowed = true)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = Orange, isGradientAllowed = true)
    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = GreyED, isGradientAllowed = true)

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Size, HourColor, MinuteColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_calendar_blank,
            labelRes = R.string.theme_tab_date,
            options = persistentListOf(DateColor),
            block = AodBlockValue.DATE,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_battery_full,
            labelRes = R.string.theme_tab_status,
            options = persistentListOf(StatusColor),
            block = AodBlockValue.STATUS,
        ),
    )

    override val ownBlocks = persistentSetOf(AodBlockValue.DATE, AodBlockValue.STATUS)
}
