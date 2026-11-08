package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.moonphase

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyExo2
import com.decoutkhanqindev.aodamn.presentation.theme.GreyBD
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE0
import com.decoutkhanqindev.aodamn.presentation.theme.MoonDust
import com.decoutkhanqindev.aodamn.presentation.theme.MoonGlow
import com.decoutkhanqindev.aodamn.presentation.theme.MoonGold
import com.decoutkhanqindev.aodamn.presentation.theme.White
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

object MoonPhaseThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyExo2

    val ClockColor = AodColorOption(
        key = "clock_color",
        labelRes = R.string.theme_color_clock,
        default = MoonGold,
        isGradientAllowed = true,
    )

    val ClockSize = AodRangeOption(
        key = "clock_size",
        labelRes = R.string.opt_size_value,
        range = 80..120,
        step = 5,
        default = 100,
    )

    val CharacterColor = AodColorOption(
        key = "character_color",
        labelRes = R.string.theme_color_moon_surface,
        default = White,
        isGradientAllowed = true,
    )

    val CharacterSize = AodRangeOption(
        key = "character_size",
        labelRes = R.string.theme_character_size_value,
        range = 60..140,
        step = 5,
        default = 100,
    )

    val Peek = AodRangeOption(
        key = "peek",
        labelRes = R.string.theme_opt_peek,
        range = 0..50,
        step = 1,
        default = 0,
    )

    val Rotation = AodRangeOption(
        key = "rotation",
        labelRes = R.string.theme_opt_rotation,
        range = 0..359,
        step = 1,
        default = 45,
    )

    val GlowColor = AodColorOption(
        key = "glow_color",
        labelRes = R.string.theme_color_shooting_star,
        default = MoonGlow,
        isGradientAllowed = true,
    )

    val ParticleColor = AodColorOption(
        key = "particle_color",
        labelRes = R.string.theme_color_particles,
        default = MoonDust,
        isGradientAllowed = true,
    )

    val DateColor = AodColorOption(
        key = "date_color",
        labelRes = R.string.theme_color_date,
        default = GreyE0,
        isGradientAllowed = true,
    )

    val StatusColor = AodColorOption(
        key = "status_color",
        labelRes = R.string.theme_color_status,
        default = GreyBD,
        isGradientAllowed = true,
    )

    val MediaColor = AodColorOption(
        key = "media_color",
        labelRes = R.string.theme_color_media,
        default = GreyBD,
        isGradientAllowed = true,
    )

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(ClockColor, ClockSize),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_moon,
            labelRes = R.string.theme_tab_moon,
            options = persistentListOf(CharacterColor, CharacterSize, Peek, Rotation),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_sparkle,
            labelRes = R.string.theme_tab_effects,
            options = persistentListOf(GlowColor, ParticleColor),
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

    override val ownBlocks = persistentSetOf(
        AodBlockValue.DATE,
        AodBlockValue.STATUS,
        AodBlockValue.MEDIA,
    )

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
