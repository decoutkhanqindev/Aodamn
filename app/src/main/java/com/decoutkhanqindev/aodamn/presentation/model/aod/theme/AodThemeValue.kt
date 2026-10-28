package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bezel.BezelThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap.BigOverlapThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.capsule.CapsuleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.card.CardThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.chain.ChainThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.classic.ClassicThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.clip.ClipThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.CompassThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.fade.FadeThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gradientline.GradientLineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.halfmoon.HalfMoonThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.jersey.JerseyThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.PebbleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.quarters.QuartersThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.ripple.RippleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.sector.SectorThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.spine.SpineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tick.TickThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tower.TowerThemeSpec

@Immutable
enum class AodThemeValue(
    val id: String,
    @param:StringRes val nameRes: Int,
    val spec: AodThemeSpec,
) {
    CLASSIC(id = DataStoreManager.DEFAULT_AOD_THEME_ID, nameRes = R.string.theme_classic, spec = ClassicThemeSpec),
    PEBBLE(id = "pebble", nameRes = R.string.theme_pebble, spec = PebbleThemeSpec),
    JERSEY(id = "jersey", nameRes = R.string.theme_jersey, spec = JerseyThemeSpec),
    CAPSULE(id = "capsule", nameRes = R.string.theme_capsule, spec = CapsuleThemeSpec),
    CARD(id = "card", nameRes = R.string.theme_card, spec = CardThemeSpec),
    TOWER(id = "tower", nameRes = R.string.theme_tower, spec = TowerThemeSpec),
    CHAIN(id = "chain", nameRes = R.string.theme_chain, spec = ChainThemeSpec),
    BEZEL(id = "bezel", nameRes = R.string.theme_bezel, spec = BezelThemeSpec),
    COMPASS(id = "compass", nameRes = R.string.theme_compass, spec = CompassThemeSpec),
    HALF_MOON(id = "half_moon", nameRes = R.string.theme_half_moon, spec = HalfMoonThemeSpec),
    GRADIENT_LINE(id = "gradient_line", nameRes = R.string.theme_gradient_line, spec = GradientLineThemeSpec),
    RIPPLE(id = "ripple", nameRes = R.string.theme_ripple, spec = RippleThemeSpec),
    TICK(id = "tick", nameRes = R.string.theme_tick, spec = TickThemeSpec),
    BIG_OVERLAP(id = "big_overlap", nameRes = R.string.theme_big_overlap, spec = BigOverlapThemeSpec),
    QUARTERS(id = "quarters", nameRes = R.string.theme_quarters, spec = QuartersThemeSpec),
    SECTOR(id = "sector", nameRes = R.string.theme_sector, spec = SectorThemeSpec),
    SPINE(id = "spine", nameRes = R.string.theme_spine, spec = SpineThemeSpec),
    CLIP(id = "clip", nameRes = R.string.theme_clip, spec = ClipThemeSpec),
    FADE(id = "fade", nameRes = R.string.theme_fade, spec = FadeThemeSpec);

    companion object {
        val DEFAULT = CLASSIC

        fun fromId(id: String?): AodThemeValue = entries.find { it.id == id } ?: DEFAULT
    }
}
