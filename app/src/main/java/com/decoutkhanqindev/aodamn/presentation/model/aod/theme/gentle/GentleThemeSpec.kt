package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gentle

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyNunito
import com.decoutkhanqindev.aodamn.presentation.theme.GreyB4
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Mist
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

object GentleThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyNunito

    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 70..130,
        step = 5,
        default = 100,
    )

    val Weight = AodChoiceOption(
        key = "weight",
        labelRes = R.string.theme_opt_weight,
        entries = AodGentleWeightValue.entries.toImmutableList(),
        default = AodGentleWeightValue.REGULAR,
    )

    val HourColor = AodColorOption(key = "hour_color", labelRes = R.string.theme_color_hour, default = Mist, isGradientAllowed = true)
    val MinuteColor = AodColorOption(key = "minute_color", labelRes = R.string.theme_color_minute, default = GreyED, isGradientAllowed = true)
    val NotificationsColor = AodColorOption(
        key = "notifications_color",
        labelRes = R.string.theme_color_notifications,
        default = GreyB4,
        isGradientAllowed = true,
    )
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = Mist, isGradientAllowed = true)

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Size, Weight, HourColor, MinuteColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_battery_full,
            labelRes = R.string.theme_tab_status,
            options = persistentListOf(NotificationsColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_music_notes,
            labelRes = R.string.theme_tab_media,
            options = persistentListOf(MediaColor),
        ),
    )

    override val ownBlocks = persistentSetOf<AodBlockValue>()

    override val components = persistentSetOf(
        AodComponentValue.DATE,
        AodComponentValue.BATTERY,
        AodComponentValue.WEATHER,
        AodComponentValue.NOTIFICATIONS,
        AodComponentValue.MEDIA,
    )
}
