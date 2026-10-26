package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.halfmoon

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansText
import com.decoutkhanqindev.aodamn.presentation.theme.Grey6E
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE0
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Red
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

object HalfMoonThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyGoogleSansText

    val HourColor = AodColorOption(key = "hour_color", labelRes = R.string.theme_color_hour, default = GreyED, isGradientAllowed = true)
    val MinuteColor = AodColorOption(key = "minute_color", labelRes = R.string.theme_color_minute, default = GreyED, isGradientAllowed = true)
    val RingColor = AodColorOption(key = "ring_color", labelRes = R.string.theme_color_minute_ring, default = Red, isGradientAllowed = true)
    val DialColor = AodColorOption(key = "dial_color", labelRes = R.string.theme_color_dial, default = Grey6E, isGradientAllowed = true)

    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyED, isGradientAllowed = true)

    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = Grey9A, isGradientAllowed = true)

    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyE0, isGradientAllowed = true)

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(HourColor, MinuteColor, RingColor, DialColor),
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
