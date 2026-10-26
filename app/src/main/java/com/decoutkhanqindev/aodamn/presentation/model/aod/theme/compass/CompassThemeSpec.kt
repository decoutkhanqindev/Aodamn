package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.Amber
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansText
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyKronaOne
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE0
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

object CompassThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyGoogleSansText
    val NumberFont: FontFamily = FontFamilyKronaOne

    val Style = AodChoiceOption(
        key = "style",
        labelRes = R.string.theme_opt_clock_style,
        entries = AodCompassStyleValue.entries.toImmutableList(),
        default = AodCompassStyleValue.SECONDS,
    )

    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 70..120,
        step = 10,
        default = 100,
    )

    val HourColor = AodColorOption(key = "hour_color", labelRes = R.string.theme_color_hour, default = Amber, isGradientAllowed = true)
    val HalfHourColor = AodColorOption(key = "half_hour_color", labelRes = R.string.theme_color_half_hour, default = Grey9A, isGradientAllowed = true)
    val MinuteColor = AodColorOption(key = "minute_color", labelRes = R.string.theme_color_minute, default = GreyE0, isGradientAllowed = true)
    val SecondColor = AodColorOption(key = "second_color", labelRes = R.string.theme_color_second, default = Grey9A, isGradientAllowed = true)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyE0, isGradientAllowed = true)

    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = Grey9A, isGradientAllowed = true)

    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyE0, isGradientAllowed = true)

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, Size, HourColor, HalfHourColor, MinuteColor, SecondColor),
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

    override val isLiveInPreview = true
}
