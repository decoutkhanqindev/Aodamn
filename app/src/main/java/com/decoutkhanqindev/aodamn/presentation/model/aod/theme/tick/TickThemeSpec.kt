package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tick

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradientDirection
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.GradientSwatchValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansText
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE0
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

object TickThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyGoogleSansText

    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 55..90,
        step = 5,
        default = 70,
    )

    val ClockColor = AodColorOption(key = "clock_color", labelRes = R.string.theme_color_clock, default = GreyE0, isGradientAllowed = true)

    val RingColor = AodColorOption(
        key = "ring_color",
        labelRes = R.string.theme_color_ring,
        default = GradientSwatchValue.TROPIC.gradient.start,
        defaultGradient = GradientSwatchValue.TROPIC.gradient.copy(direction = AodGradientDirection.VERTICAL),
    )

    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = Grey9A, isGradientAllowed = true)
    val NotificationsColor = AodColorOption(
        key = "notifications_color",
        labelRes = R.string.theme_color_notifications,
        default = GreyE0,
        isGradientAllowed = true,
    )
    val BatteryColor = AodColorOption(key = "battery_color", labelRes = R.string.theme_color_battery, default = GreyE0, isGradientAllowed = true)

    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyE0, isGradientAllowed = true)

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Size, ClockColor, RingColor),
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
