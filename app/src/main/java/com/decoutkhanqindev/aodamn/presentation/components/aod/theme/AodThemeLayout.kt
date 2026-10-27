package com.decoutkhanqindev.aodamn.presentation.components.aod.theme

import androidx.compose.runtime.Composable
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bezel.AodBezelLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bezel.AodBezelPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bigoverlap.AodBigOverlapLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bigoverlap.AodBigOverlapPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.capsule.AodCapsuleLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.capsule.AodCapsulePortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.card.AodCardLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.card.AodCardPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.chain.AodChainLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.chain.AodChainPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.classic.AodClassicLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.classic.AodClassicPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass.AodCompassLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass.AodCompassPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline.AodGradientLineLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline.AodGradientLinePortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.halfmoon.AodHalfMoonLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.halfmoon.AodHalfMoonPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.jersey.AodJerseyLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.jersey.AodJerseyPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pebble.AodPebbleLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pebble.AodPebblePortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.quarters.AodQuartersLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.quarters.AodQuartersPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple.AodRippleLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple.AodRipplePortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.sector.AodSectorLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.sector.AodSectorPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tick.AodTickLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tick.AodTickPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tower.AodTowerLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tower.AodTowerPortrait
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    when (state.appearance.theme.value) {
        AodThemeValue.CLASSIC -> AodClassicPortrait(state = state, onIntent = onIntent)
        AodThemeValue.PEBBLE -> AodPebblePortrait(state = state, onIntent = onIntent)
        AodThemeValue.JERSEY -> AodJerseyPortrait(state = state, onIntent = onIntent)
        AodThemeValue.CAPSULE -> AodCapsulePortrait(state = state, onIntent = onIntent)
        AodThemeValue.CARD -> AodCardPortrait(state = state, onIntent = onIntent)
        AodThemeValue.TOWER -> AodTowerPortrait(state = state, onIntent = onIntent)
        AodThemeValue.CHAIN -> AodChainPortrait(state = state, onIntent = onIntent)
        AodThemeValue.BEZEL -> AodBezelPortrait(state = state, onIntent = onIntent)
        AodThemeValue.COMPASS -> AodCompassPortrait(state = state, onIntent = onIntent)
        AodThemeValue.HALF_MOON -> AodHalfMoonPortrait(state = state, onIntent = onIntent)
        AodThemeValue.GRADIENT_LINE -> AodGradientLinePortrait(state = state, onIntent = onIntent)
        AodThemeValue.RIPPLE -> AodRipplePortrait(state = state, onIntent = onIntent)
        AodThemeValue.TICK -> AodTickPortrait(state = state, onIntent = onIntent)
        AodThemeValue.BIG_OVERLAP -> AodBigOverlapPortrait(state = state, onIntent = onIntent)
        AodThemeValue.QUARTERS -> AodQuartersPortrait(state = state, onIntent = onIntent)
        AodThemeValue.SECTOR -> AodSectorPortrait(state = state, onIntent = onIntent)
    }
}

@Composable
fun AodLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    when (state.appearance.theme.value) {
        AodThemeValue.CLASSIC -> AodClassicLandscape(state = state, onIntent = onIntent)
        AodThemeValue.PEBBLE -> AodPebbleLandscape(state = state, onIntent = onIntent)
        AodThemeValue.JERSEY -> AodJerseyLandscape(state = state, onIntent = onIntent)
        AodThemeValue.CAPSULE -> AodCapsuleLandscape(state = state, onIntent = onIntent)
        AodThemeValue.CARD -> AodCardLandscape(state = state, onIntent = onIntent)
        AodThemeValue.TOWER -> AodTowerLandscape(state = state, onIntent = onIntent)
        AodThemeValue.CHAIN -> AodChainLandscape(state = state, onIntent = onIntent)
        AodThemeValue.BEZEL -> AodBezelLandscape(state = state, onIntent = onIntent)
        AodThemeValue.COMPASS -> AodCompassLandscape(state = state, onIntent = onIntent)
        AodThemeValue.HALF_MOON -> AodHalfMoonLandscape(state = state, onIntent = onIntent)
        AodThemeValue.GRADIENT_LINE -> AodGradientLineLandscape(state = state, onIntent = onIntent)
        AodThemeValue.RIPPLE -> AodRippleLandscape(state = state, onIntent = onIntent)
        AodThemeValue.TICK -> AodTickLandscape(state = state, onIntent = onIntent)
        AodThemeValue.BIG_OVERLAP -> AodBigOverlapLandscape(state = state, onIntent = onIntent)
        AodThemeValue.QUARTERS -> AodQuartersLandscape(state = state, onIntent = onIntent)
        AodThemeValue.SECTOR -> AodSectorLandscape(state = state, onIntent = onIntent)
    }
}
