package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.mono

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.GradientSwatchValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilySpaceMono
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE4
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

object MonoThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilySpaceMono

    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 60..100,
        step = 5,
        default = 80,
    )

    val HourColor = AodColorOption(
        key = "hour_color",
        labelRes = R.string.theme_color_hour,
        default = GradientSwatchValue.AUTUMN.gradient.start,
        defaultGradient = GradientSwatchValue.AUTUMN.gradient,
    )
    val MinuteColor = AodColorOption(key = "minute_color", labelRes = R.string.theme_color_minute, default = GreyED, isGradientAllowed = true)

    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyE4, isGradientAllowed = true)

    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = GreyE4, isGradientAllowed = true)

    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyE4, isGradientAllowed = true)

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
        AodComponentValue.EVENTS,
        AodComponentValue.MEMO,
        AodComponentValue.NOTIFICATIONS,
        AodComponentValue.MEDIA,
    )

    override val hasNotificationContent = true
}
