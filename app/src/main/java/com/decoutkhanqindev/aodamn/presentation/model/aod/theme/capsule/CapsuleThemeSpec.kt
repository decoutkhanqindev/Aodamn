package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.capsule

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Lime
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

object CapsuleThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyPlusJakartaSans

    val Style = AodChoiceOption(
        key = "capsule_style",
        labelRes = R.string.theme_opt_tile_style,
        entries = AodTileStyleValue.entries.toImmutableList(),
        default = AodTileStyleValue.OUTLINE,
    )
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 60..150,
        step = 10,
        default = 100,
    )
    val DigitColor = AodColorOption(key = "digit_color", labelRes = R.string.theme_color_digits, default = Lime, isGradientAllowed = true)
    val CapsuleColor = AodColorOption(key = "capsule_color", labelRes = R.string.theme_color_capsule, default = Lime, isGradientAllowed = true)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyED, isGradientAllowed = true)

    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = Lime, isGradientAllowed = true)
    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = GreyED, isGradientAllowed = true)

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, Size, DigitColor, CapsuleColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_calendar_blank,
            labelRes = R.string.theme_tab_date,
            options = persistentListOf(DateColor),
            block = AodBlockValue.DATE,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_music_notes,
            labelRes = R.string.theme_tab_media,
            options = persistentListOf(MediaColor),
            block = AodBlockValue.MEDIA,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_battery_full,
            labelRes = R.string.theme_tab_status,
            options = persistentListOf(StatusColor),
            block = AodBlockValue.STATUS,
        ),
    )

    override val ownBlocks = persistentSetOf(AodBlockValue.DATE, AodBlockValue.MEDIA, AodBlockValue.STATUS)
}
