package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansFlex
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansFlexRounded
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansText
import com.decoutkhanqindev.aodamn.presentation.theme.GreyCE
import com.decoutkhanqindev.aodamn.presentation.theme.Peach
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

object BigOverlapThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyGoogleSansText

    val DigitFont: FontFamily = FontFamilyGoogleSansFlex
    val RoundedDigitFont: FontFamily = FontFamilyGoogleSansFlexRounded
    val DigitWeight = FontWeight(1000)

    val Style = AodChoiceOption(
        key = "style",
        labelRes = R.string.theme_opt_clock_style,
        entries = AodBigOverlapStyleValue.entries.toImmutableList(),
        default = AodBigOverlapStyleValue.FILLED_ROUND,
    )

    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 50..100,
        step = 5,
        default = 85,
    )

    val HourFirstColor = AodColorOption(
        key = "hour_first_color",
        labelRes = R.string.theme_color_hour_digit_1,
        default = Peach,
        isGradientAllowed = true,
    )
    val HourSecondColor = AodColorOption(
        key = "hour_second_color",
        labelRes = R.string.theme_color_hour_digit_2,
        default = GreyCE,
        isGradientAllowed = true,
    )
    val MinuteFirstColor = AodColorOption(
        key = "minute_first_color",
        labelRes = R.string.theme_color_minute_digit_1,
        default = GreyCE,
        isGradientAllowed = true,
    )
    val MinuteSecondColor = AodColorOption(
        key = "minute_second_color",
        labelRes = R.string.theme_color_minute_digit_2,
        default = Peach,
        isGradientAllowed = true,
    )

    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyCE, isGradientAllowed = true)
    val NotificationsColor = AodColorOption(
        key = "notifications_color",
        labelRes = R.string.theme_color_notifications,
        default = GreyCE,
        isGradientAllowed = true,
    )
    val BatteryColor = AodColorOption(key = "battery_color", labelRes = R.string.theme_color_battery, default = GreyCE, isGradientAllowed = true)

    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyCE, isGradientAllowed = true)

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, Size, HourFirstColor, HourSecondColor, MinuteFirstColor, MinuteSecondColor),
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
            options = persistentListOf(NotificationsColor, BatteryColor),
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
