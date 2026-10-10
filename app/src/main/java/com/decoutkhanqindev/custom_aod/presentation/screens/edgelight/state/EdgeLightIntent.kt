package com.decoutkhanqindev.custom_aod.presentation.screens.edgelight.state

sealed interface EdgeLightIntent {
    data object NavigateToIap : EdgeLightIntent
    data object ShareApp : EdgeLightIntent
}
