package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.meridian

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodZoneOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansFlexRound
import com.decoutkhanqindev.aodamn.presentation.theme.GreyCE
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE4
import com.decoutkhanqindev.aodamn.presentation.theme.Periwinkle
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

object MeridianThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyGoogleSansFlexRound

    val Style = AodChoiceOption(
        key = "style",
        labelRes = R.string.theme_opt_card_style,
        entries = AodMeridianStyleValue.entries.toImmutableList(),
        default = AodMeridianStyleValue.FILLED,
    )

    val Zone = AodZoneOption(
        key = "zone",
        labelRes = R.string.theme_opt_zone,
        default = "America/New_York",
    )

    val CardColor = AodColorOption(
        key = "card_color",
        labelRes = R.string.theme_color_card,
        default = Periwinkle,
        isGradientAllowed = true,
    )

    val DialColor = AodColorOption(
        key = "dial_color",
        labelRes = R.string.theme_color_face,
        default = GreyE4,
        isGradientAllowed = true,
    )

    val DateColor = AodColorOption(
        key = "date_color",
        labelRes = R.string.theme_color_date,
        default = GreyE4,
        isGradientAllowed = true,
    )

    val StatusColor = AodColorOption(
        key = "status_color",
        labelRes = R.string.theme_color_status,
        default = GreyE4,
        isGradientAllowed = true,
    )

    val MediaColor = AodColorOption(
        key = "media_color",
        labelRes = R.string.theme_color_media,
        default = GreyCE,
        isGradientAllowed = true,
    )

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, CardColor, DialColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_globe_hemisphere_west,
            labelRes = R.string.theme_tab_zone,
            options = persistentListOf(Zone),
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
}
