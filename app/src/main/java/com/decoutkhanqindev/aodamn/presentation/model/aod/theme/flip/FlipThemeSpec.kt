package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.flip

import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Midnight
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

object FlipThemeSpec : AodThemeSpec {
    val Style = AodChoiceOption(
        key = "style",
        labelRes = R.string.theme_opt_clock_style,
        entries = AodFlipStyleValue.entries.toImmutableList(),
        default = AodFlipStyleValue.BEBAS_NEUE,
    )

    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 50..100,
        step = 5,
        default = 65,
    )

    val CharacterSize = AodRangeOption(
        key = "character_size",
        labelRes = R.string.flip_character_size_value,
        range = 35..75,
        step = 5,
        default = 55,
    )

    val CardColor = AodColorOption(
        key = "card_color",
        labelRes = R.string.theme_color_card,
        default = Midnight,
        isGradientAllowed = true,
    )

    val DigitColor = AodColorOption(
        key = "digit_color",
        labelRes = R.string.theme_color_digits,
        default = GreyED,
        isGradientAllowed = true,
    )

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, Size, CharacterSize, CardColor, DigitColor),
        ),
    )

    override val ownBlocks = persistentSetOf<AodBlockValue>()

    override val components = persistentSetOf(
        AodComponentValue.DATE,
        AodComponentValue.BATTERY,
        AodComponentValue.WEATHER,
        AodComponentValue.NOTIFICATIONS,
    )

    override val hasNotificationContent = false

    override val isLiveInPreview = true
}