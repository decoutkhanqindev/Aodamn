package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.eclipse

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.EclipseHalo
import com.decoutkhanqindev.aodamn.presentation.theme.EclipseShadow
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyLato
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

object EclipseThemeSpec : AodThemeSpec {
    val Font: FontFamily = FontFamilyLato

    val RingColor = AodColorOption(
        key = "ring_color",
        labelRes = R.string.theme_color_ring,
        default = EclipseHalo,
        isGradientAllowed = true,
    )

    val ShadowColor = AodColorOption(
        key = "shadow_color",
        labelRes = R.string.theme_color_shadow,
        default = EclipseShadow,
        isGradientAllowed = true,
    )

    val Rotation = AodRangeOption(
        key = "rotation",
        labelRes = R.string.eclipse_rotation_value,
        range = 0..360,
        step = 5,
        default = AodRangeOption.AUTO,
        autoLabelRes = R.string.eclipse_rotation_live,
    )

    val Peek = AodRangeOption(
        key = "peek",
        labelRes = R.string.eclipse_peek_value,
        range = 0..100,
        step = 5,
        default = AodRangeOption.AUTO,
        autoLabelRes = R.string.eclipse_peek_live,
    )

    val CharacterColor = AodColorOption(
        key = "character_color",
        labelRes = R.string.theme_color_digits,
        default = GreyED,
        isGradientAllowed = true,
    )

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(RingColor, ShadowColor, Rotation, Peek, CharacterColor),
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

    override val isLiveInPreview = true
}
