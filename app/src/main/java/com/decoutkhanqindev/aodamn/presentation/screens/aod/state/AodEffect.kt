package com.decoutkhanqindev.aodamn.presentation.screens.aod.state

sealed interface AodEffect {
    data object CloseAod : AodEffect
}
