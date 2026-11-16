package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.dot

import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.DotRed
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE4
import com.decoutkhanqindev.aodamn.presentation.theme.White
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

object DotThemeSpec : AodThemeSpec {
    val ClockSize = AodRangeOption(
        key = "clock_size",
        labelRes = R.string.opt_size_value,
        range = 70..130,
        step = 2,
        default = 100,
    )

    val HourColor = AodColorOption(
        key = "hour_color",
        labelRes = R.string.theme_color_hour,
        default = White,
        isGradientAllowed = true,
    )

    val MinuteColor = AodColorOption(
        key = "minute_color",
        labelRes = R.string.theme_color_minute,
        default = White,
        isGradientAllowed = true,
    )

    val DotsColor = AodColorOption(
        key = "dots_color",
        labelRes = R.string.theme_color_separator,
        default = DotRed,
        isGradientAllowed = true,
    )

    val DateColor = AodColorOption(
        key = "date_color",
        labelRes = R.string.theme_color_date,
        default = GreyE4,
        isGradientAllowed = true,
    )

    val NotificationColor = AodColorOption(
        key = "notification_color",
        labelRes = R.string.theme_color_notifications,
        default = GreyE4,
        isGradientAllowed = true,
    )

    val BatteryColor = AodColorOption(
        key = "battery_color",
        labelRes = R.string.theme_color_battery,
        default = GreyE4,
        isGradientAllowed = true,
    )

    val MediaColor = AodColorOption(
        key = "media_color",
        labelRes = R.string.theme_color_media,
        default = GreyE4,
        isGradientAllowed = true,
    )

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(ClockSize, HourColor, MinuteColor, DotsColor),
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
            options = persistentListOf(NotificationColor, BatteryColor),
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
