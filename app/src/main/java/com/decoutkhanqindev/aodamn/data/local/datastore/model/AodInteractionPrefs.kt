package com.decoutkhanqindev.aodamn.data.local.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class AodInteractionPrefs(
    val actions: Map<String, String> = emptyMap(),
    val isAutoDimEnabled: Boolean = true,
    val isRaiseToWakeEnabled: Boolean = true,
) {
    companion object {
        val DEFAULT = AodInteractionPrefs()
    }
}
