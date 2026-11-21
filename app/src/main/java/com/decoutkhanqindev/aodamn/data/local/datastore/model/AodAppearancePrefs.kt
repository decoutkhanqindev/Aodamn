package com.decoutkhanqindev.aodamn.data.local.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class AodAppearancePrefs(
    val themeId: String = DEFAULT_THEME_ID,
    val themeConfigs: Map<String, Map<String, Int>> = emptyMap(),
    val componentStyles: Map<String, Int> = emptyMap(),
    val isLandscape: Boolean = false,
    val wallpaperCode: Int = 0,
) {
    companion object {
        const val DEFAULT_THEME_ID = "classic"
        val DEFAULT = AodAppearancePrefs()
    }
}
