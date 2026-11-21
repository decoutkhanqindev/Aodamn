package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodAppearancePrefs
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toPersistentMap

@Immutable
data class AodThemeUiModel(
    val value: AodThemeValue = AodThemeValue.DEFAULT,
    val config: AodThemeConfig = AodThemeConfig(),
)

fun AodAppearancePrefs.themeConfigOf(theme: AodThemeValue): AodThemeConfig =
    AodThemeConfig(themeConfigs[theme.id].orEmpty().toPersistentMap())

fun AodAppearancePrefs.themeConfigs(): ImmutableMap<AodThemeValue, AodThemeConfig> =
    AodThemeValue.entries.associateWith { theme -> themeConfigOf(theme) }.toImmutableMap()

fun AodAppearancePrefs.toThemeUiModel(): AodThemeUiModel {
    val theme = AodThemeValue.fromId(themeId)
    return AodThemeUiModel(value = theme, config = themeConfigOf(theme))
}

fun AodAppearancePrefs.withThemeConfig(theme: AodThemeValue, config: AodThemeConfig): AodAppearancePrefs =
    copy(themeConfigs = themeConfigs + (theme.id to config.values))
