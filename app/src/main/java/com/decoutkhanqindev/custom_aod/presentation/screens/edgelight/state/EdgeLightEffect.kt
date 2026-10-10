package com.decoutkhanqindev.custom_aod.presentation.screens.edgelight.state

sealed interface EdgeLightEffect {
    data object NavigateToIap : EdgeLightEffect
    data object ShareApp : EdgeLightEffect
}
