package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gradientline

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.GradientSwatchValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansText
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE0
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

object GradientLineThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyGoogleSansText

    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 80..150,
        step = 10,
        default = 110,
    )

    val Position = AodRangeOption(
        key = "position",
        labelRes = R.string.opt_position_value,
        range = 0..100,
        step = 5,
        default = AodRangeOption.AUTO,
        autoLabelRes = R.string.opt_position_auto,
    )

    val ClockColor = AodColorOption(
        key = "clock_color",
        labelRes = R.string.theme_color_clock,
        default = GreyE0,
        isGradientAllowed = true,
    )

    val LineColor = AodColorOption(
        key = "line_color",
        labelRes = R.string.theme_color_line,
        default = GradientSwatchValue.SUNSET.gradient.start,
        defaultGradient = GradientSwatchValue.SUNSET.gradient,
    )

    val DateColor = AodColorOption(
        key = "date_color",
        labelRes = R.string.theme_color_date,
        default = GreyE0,
        isGradientAllowed = true,
    )
    val BatteryColor = AodColorOption(key = "battery_color", labelRes = R.string.theme_color_battery, default = GreyE0, isGradientAllowed = true)
    val NotificationsColor = AodColorOption(
        key = "notifications_color",
        labelRes = R.string.theme_color_notifications,
        default = GreyE0,
        isGradientAllowed = true,
    )

    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyE0, isGradientAllowed = true)

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Size, Position, ClockColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_line_vertical,
            labelRes = R.string.theme_tab_line,
            options = persistentListOf(LineColor),
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
