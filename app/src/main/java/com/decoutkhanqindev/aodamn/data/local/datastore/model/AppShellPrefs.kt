package com.decoutkhanqindev.aodamn.data.local.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class AppShellPrefs(
    val selectedLangCode: String = "en",
    val isFirstOpen: Boolean = true,
    val isCustomizeGuideShown: Boolean = false,
    val lastWakeCode: Int = 0,
) {
    companion object {
        val DEFAULT = AppShellPrefs()
    }
}
