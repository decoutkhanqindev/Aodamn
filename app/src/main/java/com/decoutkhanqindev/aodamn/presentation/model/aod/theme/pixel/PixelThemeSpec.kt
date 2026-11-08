package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pixel

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilySilkscreen
import com.decoutkhanqindev.aodamn.presentation.theme.GreyBD
import com.decoutkhanqindev.aodamn.presentation.theme.GreyCE
import com.decoutkhanqindev.aodamn.presentation.theme.Phosphor
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

object PixelThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilySilkscreen

    val Style = AodChoiceOption(
        key = "style",
        labelRes = R.string.theme_opt_clock_style,
        entries = AodPixelStyleValue.entries.toImmutableList(),
        default = AodPixelStyleValue.FILLED,
    )

    val ClockSize = AodRangeOption(
        key = "clock_size",
        labelRes = R.string.opt_size_value,
        range = 80..120,
        step = 5,
        default = 100,
    )

    val ClockColor = AodColorOption(
        key = "clock_color",
        labelRes = R.string.theme_color_clock,
        default = Phosphor,
        isGradientAllowed = true,
    )

    val DateColor = AodColorOption(
        key = "date_color",
        labelRes = R.string.theme_color_date,
        default = GreyCE,
        isGradientAllowed = true,
    )

    val StatusColor = AodColorOption(
        key = "status_color",
        labelRes = R.string.theme_color_status,
        default = GreyBD,
        isGradientAllowed = true,
    )

    val MediaColor = AodColorOption(
        key = "media_color",
        labelRes = R.string.theme_color_media,
        default = GreyBD,
        isGradientAllowed = true,
    )

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, ClockSize, ClockColor),
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
        AodThemeTab(
            iconRes = R.drawable.ic_music_notes,
            labelRes = R.string.theme_tab_media,
            options = persistentListOf(MediaColor),
            block = AodBlockValue.MEDIA,
        ),
    )

    override val ownBlocks = persistentSetOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA)

    override val components = persistentSetOf(
        AodComponentValue.DATE,
        AodComponentValue.BATTERY,
        AodComponentValue.WEATHER,
        AodComponentValue.NOTIFICATIONS,
        AodComponentValue.MEDIA,
    )

    override val hasNotificationContent = true
}
