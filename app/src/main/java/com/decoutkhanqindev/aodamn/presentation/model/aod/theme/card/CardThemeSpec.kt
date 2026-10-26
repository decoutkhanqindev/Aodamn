package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.card

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.theme.Blue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE4
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

object CardThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyPlusJakartaSans

    val Style = AodChoiceOption(
        key = "digit_style",
        labelRes = R.string.theme_opt_digit_style,
        entries = AodTileStyleValue.entries.toImmutableList(),
        default = AodTileStyleValue.FILLED,
    )
    val Background = AodChoiceOption(
        key = "card_background",
        labelRes = R.string.theme_opt_card_background,
        entries = AodCardBackgroundValue.entries.toImmutableList(),
        default = AodCardBackgroundValue.TINTED,
    )

    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 80..120,
        step = 5,
        default = 100,
    )

    val HourColor = AodColorOption(key = "hour_color", labelRes = R.string.theme_color_hour, default = Blue, isGradientAllowed = true)
    val MinuteColor = AodColorOption(
        key = "minute_color",
        labelRes = R.string.theme_color_minute_tag,
        default = GreyE4,
        isGradientAllowed = true,
    )
    val CardColor = AodColorOption(key = "card_color", labelRes = R.string.theme_color_card, default = Blue, isGradientAllowed = true)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyE4, isGradientAllowed = true)

    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyE4, isGradientAllowed = true)
    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = GreyE4, isGradientAllowed = true)

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, Background, Size, HourColor, MinuteColor, CardColor),
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
