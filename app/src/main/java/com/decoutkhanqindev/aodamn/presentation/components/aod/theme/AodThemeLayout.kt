package com.decoutkhanqindev.aodamn.presentation.components.aod.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodBottomDetails
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPortraitSpread
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodTopDetails
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
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.classic.AodClassicClock
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.classic.AodClassicLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass.AodCompassLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass.AodCompassPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline.AodGradientLineLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline.AodGradientLinePortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.halfmoon.AodHalfMoonLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.halfmoon.AodHalfMoonPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.jersey.AodJerseyLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.jersey.AodJerseyPortrait
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pebble.AodPebbleClock
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pebble.AodPebbleLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple.AodRippleLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple.AodRipplePortrait
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
    val theme = state.appearance.theme

    when (theme.value) {
        AodThemeValue.CLASSIC -> ClockAndDetails(state = state, onIntent = onIntent) {
            AodClassicClock(nowMillis = state.nowMillis, config = theme.config)
        }

        AodThemeValue.PEBBLE -> ClockAndDetails(state = state, onIntent = onIntent) {
            AodPebbleClock(
                nowMillis = state.nowMillis,
                config = theme.config,
                isDateEnabled = state.extras.isDateEnabled,
            )
        }

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
    }
}

@Composable
private fun ClockAndDetails(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    clock: @Composable () -> Unit,
) {
    AodPortraitSpread(
        top = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                clock()

                AodTopDetails(state = state, onIntent = onIntent)
            }
        },
        bottom = { AodBottomDetails(state = state, onIntent = onIntent) },
    )
}
