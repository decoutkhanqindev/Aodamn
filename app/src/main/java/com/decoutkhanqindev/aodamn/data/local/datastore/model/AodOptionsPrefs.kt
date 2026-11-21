package com.decoutkhanqindev.aodamn.data.local.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class AodOptionsPrefs(
    val isEnabled: Boolean = true,
    val isCustomBrightness: Boolean = true,
    val brightnessPercent: Int = 1,
    val isProximityEnabled: Boolean = true,
    val timeoutMinutes: Int = 0,
    val autoHideSeconds: Int = 30,
) {
    companion object {
        val DEFAULT = AodOptionsPrefs()
    }
}
