package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix

import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodToggleOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE4
import com.decoutkhanqindev.aodamn.presentation.theme.MatrixAmber
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

object MatrixThemeSpec : AodThemeSpec {
    val ClockSize = AodRangeOption(
        key = "clock_size",
        labelRes = R.string.matrix_clock_size_value,
        range = 30..50,
        step = 1,
        default = 40,
    )

    val DotStyle = AodChoiceOption(
        key = "dot_style",
        labelRes = R.string.theme_opt_dot_style,
        entries = AodMatrixDotValue.entries.toImmutableList(),
        default = AodMatrixDotValue.ROUND,
    )

    val UnlitDots = AodToggleOption(
        key = "unlit_dots",
        labelRes = R.string.theme_opt_unlit_dots,
        default = true,
    )

    val ClockColor = AodColorOption(
        key = "clock_color",
        labelRes = R.string.theme_color_clock,
        default = MatrixAmber,
        isGradientAllowed = true,
    )

    val DateColor = AodColorOption(
        key = "date_color",
        labelRes = R.string.theme_color_date,
        default = GreyE4,
        isGradientAllowed = true,
    )

    val EventsColor = AodColorOption(
        key = "events_color",
        labelRes = R.string.theme_color_events,
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
            options = persistentListOf(ClockSize, DotStyle, UnlitDots, ClockColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_calendar_blank,
            labelRes = R.string.theme_tab_date,
            options = persistentListOf(DateColor),
            block = AodBlockValue.DATE,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_calendar_check,
            labelRes = R.string.customize_tab_events,
            options = persistentListOf(EventsColor),
            block = AodBlockValue.EVENTS,
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

    override val ownBlocks = persistentSetOf(
        AodBlockValue.DATE,
        AodBlockValue.STATUS,
        AodBlockValue.EVENTS,
        AodBlockValue.MEDIA,
    )

    override val components = persistentSetOf(
        AodComponentValue.DATE,
        AodComponentValue.BATTERY,
        AodComponentValue.WEATHER,
        AodComponentValue.EVENTS,
        AodComponentValue.NOTIFICATIONS,
        AodComponentValue.MEDIA,
    )

    override val hasNotificationContent = true
}
