package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.chain

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.Coral
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyAnton
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE0
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet

object ChainThemeSpec : AodThemeSpec {
    val TitleFont: FontFamily = FontFamilyAnton
    val Font: FontFamily = FontFamilyPlusJakartaSans

    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 70..130,
        step = 10,
        default = 100,
    )
    val ClockColor = AodColorOption(key = "clock_color", labelRes = R.string.theme_color_clock, default = Coral, isGradientAllowed = true)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyE0, isGradientAllowed = true)
    val BatteryColor = AodColorOption(key = "battery_color", labelRes = R.string.theme_color_battery, default = GreyE0, isGradientAllowed = true)
    val NotificationsColor = AodColorOption(key = "notifications_color", labelRes = R.string.theme_color_notifications, default = GreyE0, isGradientAllowed = true)
    val MemoColor = AodColorOption(key = "memo_color", labelRes = R.string.theme_color_memo, default = GreyE0, isGradientAllowed = true)
    val EventsColor = AodColorOption(key = "events_color", labelRes = R.string.theme_color_events, default = GreyE0, isGradientAllowed = true)

    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = Coral, isGradientAllowed = true)

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Size, ClockColor),
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
            options = persistentListOf(BatteryColor, NotificationsColor),
            block = AodBlockValue.STATUS,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_note_pencil,
            labelRes = R.string.customize_tab_memo,
            options = persistentListOf(MemoColor),
            block = AodBlockValue.MEMO,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_calendar_check,
            labelRes = R.string.customize_tab_events,
            options = persistentListOf(EventsColor),
            block = AodBlockValue.EVENTS,
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
        AodBlockValue.MEMO,
        AodBlockValue.EVENTS,
        AodBlockValue.MEDIA,
    )

    override val components = AodComponentValue.entries.filter { component -> component != AodComponentValue.DRAWING }.toImmutableSet()

    override val hasNotificationContent = true
}
