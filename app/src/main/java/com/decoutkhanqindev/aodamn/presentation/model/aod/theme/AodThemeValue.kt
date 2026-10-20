package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bezel.BezelThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.capsule.CapsuleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.card.CardThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.chain.ChainThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.classic.ClassicThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.CompassThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gradientline.GradientLineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.halfmoon.HalfMoonThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.jersey.JerseyThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.PebbleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tower.TowerThemeSpec

// Các chủ đề AOD. id lưu trong DataStore nên không đổi; thêm chủ đề = một mục ở đây + đặc tả AodThemeSpec + phần vẽ ở
// components/aod/theme/ (AodPortrait / AodLandscape chọn theo mục này).
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
    GRADIENT_LINE(id = "gradient_line", nameRes = R.string.theme_gradient_line, spec = GradientLineThemeSpec);

    companion object {
        val DEFAULT = CLASSIC

        fun fromId(id: String?): AodThemeValue = entries.find { it.id == id } ?: DEFAULT
    }
}
