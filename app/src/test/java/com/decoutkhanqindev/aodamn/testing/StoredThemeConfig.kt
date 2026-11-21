package com.decoutkhanqindev.aodamn.testing

import com.decoutkhanqindev.aodamn.data.local.datastore.PrefsJson
import com.decoutkhanqindev.aodamn.data.local.datastore.decodeOrNull
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodAppearancePrefs
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.themeConfigOf
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.withThemeConfig

fun AodThemeConfig.storedAndRestored(theme: AodThemeValue): AodThemeConfig {
    val raw = PrefsJson.encodeToString(AodAppearancePrefs.serializer(), AodAppearancePrefs.DEFAULT.withThemeConfig(theme, this))
    val restored = requireNotNull(PrefsJson.decodeOrNull(AodAppearancePrefs.serializer(), raw)) { raw }
    return restored.themeConfigOf(theme)
}
