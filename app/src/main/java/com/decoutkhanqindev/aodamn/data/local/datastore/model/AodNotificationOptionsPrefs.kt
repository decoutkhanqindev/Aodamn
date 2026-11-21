package com.decoutkhanqindev.aodamn.data.local.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class AodNotificationOptionsPrefs(
    val isIconsEnabled: Boolean = true,
    val isContentEnabled: Boolean = false,
    val isEdgeGlowEnabled: Boolean = true,
    val isMediaControlsEnabled: Boolean = true,
) {
    companion object {
        val DEFAULT = AodNotificationOptionsPrefs()
    }
}
