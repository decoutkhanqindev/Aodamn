package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.poster

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyLeagueGothic
import com.decoutkhanqindev.aodamn.presentation.theme.GreyBD
import com.decoutkhanqindev.aodamn.presentation.theme.GreyCE
import com.decoutkhanqindev.aodamn.presentation.theme.Vermilion
import com.decoutkhanqindev.aodamn.presentation.theme.VermilionDeep
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

object PosterThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyLeagueGothic

    val ClockSize = AodRangeOption(
        key = "clock_size",
        labelRes = R.string.opt_size_value,
        range = 80..140,
        step = 5,
        default = 100,
    )

    val HourColor = AodColorOption(
        key = "hour_color",
        labelRes = R.string.theme_color_hour,
        default = Vermilion,
        isGradientAllowed = true,
    )

    val MinuteColor = AodColorOption(
        key = "minute_color",
        labelRes = R.string.theme_color_minute,
        default = VermilionDeep,
        isGradientAllowed = true,
    )

    val DayColor = AodColorOption(
        key = "day_color",
        labelRes = R.string.theme_color_day,
        default = GreyCE,
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
            options = persistentListOf(ClockSize, HourColor, MinuteColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_text_aa,
            labelRes = R.string.theme_tab_day,
            options = persistentListOf(DayColor),
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
