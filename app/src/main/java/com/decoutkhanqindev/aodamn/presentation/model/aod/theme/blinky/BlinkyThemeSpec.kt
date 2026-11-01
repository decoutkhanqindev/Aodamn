package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.blinky

import androidx.compose.ui.graphics.Color
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.Orchid
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

object BlinkyThemeSpec : AodThemeSpec {
    val ThemeColor = AodColorOption(
        key = "theme_color",
        labelRes = R.string.theme_color_theme,
        default = Orchid,
        isGradientAllowed = true,
    )

    val CharacterSize = AodRangeOption(
        key = "character_size",
        labelRes = R.string.blinky_character_size_value,
        range = 70..140,
        step = 5,
        default = 100,
    )

    val CharacterColor = AodColorOption(
        key = "character_color",
        labelRes = R.string.theme_color_character,
        default = Orchid,
        isGradientAllowed = true,
    )

    val ClockSize = AodRangeOption(
        key = "clock_size",
        labelRes = R.string.blinky_clock_size_value,
        range = 17..30,
        step = 1,
        default = 22,
    )

    val ClockColor = AodColorOption(
        key = "clock_color",
        labelRes = R.string.theme_color_clock,
        default = Orchid,
        isGradientAllowed = true,
    )

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_paint_brush,
            labelRes = R.string.theme_tab_theme,
            options = persistentListOf(ThemeColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_eye,
            labelRes = R.string.theme_tab_character,
            options = persistentListOf(CharacterSize, CharacterColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(ClockSize, ClockColor),
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

    override val hasNotificationContent = true

    override val isLiveInPreview = true

    fun characterColor(config: AodThemeConfig): Color = colorOf(config, CharacterColor)

    fun characterGradient(config: AodThemeConfig): AodGradient? = gradientOf(config, CharacterColor)

    fun clockColor(config: AodThemeConfig): Color = colorOf(config, ClockColor)

    fun clockGradient(config: AodThemeConfig): AodGradient? = gradientOf(config, ClockColor)

    private fun colorOf(config: AodThemeConfig, option: AodColorOption): Color =
        if (option.key in config.values) config[option] else config[ThemeColor]

    private fun gradientOf(config: AodThemeConfig, option: AodColorOption): AodGradient? =
        if (option.key in config.values) config.gradientOf(option) else config.gradientOf(ThemeColor)
}
