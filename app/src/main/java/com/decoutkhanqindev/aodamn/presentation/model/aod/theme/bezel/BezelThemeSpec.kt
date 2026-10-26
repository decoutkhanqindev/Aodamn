package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bezel

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.Cyan
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSans
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyZalandoSansExpanded
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

object BezelThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyGoogleSans
    val NumberFont: FontFamily = FontFamilyZalandoSansExpanded

    val Ticks = AodChoiceOption(
        key = "ticks",
        labelRes = R.string.theme_opt_ticks,
        entries = AodBezelTicksValue.entries.toImmutableList(),
        default = AodBezelTicksValue.MINUTES,
    )

    val NumberColor = AodColorOption(key = "number_color", labelRes = R.string.theme_color_digits, default = Cyan, isGradientAllowed = true)
    val HandColor = AodColorOption(key = "hand_color", labelRes = R.string.theme_color_hands, default = GreyED, isGradientAllowed = true)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyED, isGradientAllowed = true)
    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = GreyED, isGradientAllowed = true)

    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyED, isGradientAllowed = true)

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Ticks, NumberColor, HandColor),
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
}
