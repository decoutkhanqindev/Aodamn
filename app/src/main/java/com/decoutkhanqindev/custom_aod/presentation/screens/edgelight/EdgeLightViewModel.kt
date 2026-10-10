package com.decoutkhanqindev.custom_aod.presentation.screens.edgelight

import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.custom_aod.presentation.base.BaseViewModel
import com.decoutkhanqindev.custom_aod.presentation.screens.edgelight.state.EdgeLightEffect
import com.decoutkhanqindev.custom_aod.presentation.screens.edgelight.state.EdgeLightIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.edgelight.state.EdgeLightState
import kotlinx.coroutines.launch
import timber.log.Timber

class EdgeLightViewModel : BaseViewModel<EdgeLightState, EdgeLightIntent, EdgeLightEffect>(
    initialState = EdgeLightState,
) {

    override fun onIntent(intent: EdgeLightIntent) {
        Timber.tag(tag).d("onIntent: $intent")
        when (intent) {
            is EdgeLightIntent.NavigateToIap ->
                viewModelScope.launch { sendEffect(EdgeLightEffect.NavigateToIap) }

            is EdgeLightIntent.ShareApp -> viewModelScope.launch { sendEffect(EdgeLightEffect.ShareApp) }
        }
    }
}
