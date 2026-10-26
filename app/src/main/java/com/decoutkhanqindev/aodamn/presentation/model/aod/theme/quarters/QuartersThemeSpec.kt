package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.quarters

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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.theme.Cream
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansFlex
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansFlexRounded
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansText
import com.decoutkhanqindev.aodamn.presentation.theme.GreyCE
import com.decoutkhanqindev.aodamn.presentation.theme.Orchid
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

object QuartersThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyGoogleSansText

    val DigitFont: FontFamily = FontFamilyGoogleSansFlex
    val RoundedDigitFont: FontFamily = FontFamilyGoogleSansFlexRounded
    val DigitWeight = FontWeight(1000)

    val DigitStyle = AodChoiceOption(
        key = "digit_style",
        labelRes = R.string.theme_opt_digit_style,
        entries = AodQuartersDigitStyleValue.entries.toImmutableList(),
        default = AodQuartersDigitStyleValue.FILLED_ROUND,
    )

    val HandStyle = AodChoiceOption(
        key = "hand_style",
        labelRes = R.string.theme_opt_hand_style,
        entries = AodTileStyleValue.entries.toImmutableList(),
        default = AodTileStyleValue.FILLED,
    )

    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 50..90,
        step = 5,
        default = 70,
    )

    val DigitColor = AodColorOption(key = "digit_color", labelRes = R.string.theme_color_digits, default = Orchid, isGradientAllowed = true)
    val HourColor = AodColorOption(key = "hour_color", labelRes = R.string.theme_color_hour_hand, default = GreyCE, isGradientAllowed = true)
    val MinuteColor = AodColorOption(
        key = "minute_color",
        labelRes = R.string.theme_color_minute_hand,
        default = Cream,
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
            options = persistentListOf(DigitStyle, HandStyle, Size, DigitColor, HourColor, MinuteColor),
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

    override val isLiveInPreview = true
}
