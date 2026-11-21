package com.decoutkhanqindev.aodamn.presentation.model.aod.appearance

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodAppearancePrefs
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.toComponentsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.toThemeUiModel

@Immutable
data class AodAppearanceUiModel(
    val theme: AodThemeUiModel = AodThemeUiModel(),
    val components: AodComponentsUiModel = AodComponentsUiModel(),
    val isLandscape: Boolean = AodAppearancePrefs.DEFAULT.isLandscape,
)

fun AodAppearancePrefs.toUiModel(): AodAppearanceUiModel = AodAppearanceUiModel(
    theme = toThemeUiModel(),
    components = toComponentsUiModel(),
    isLandscape = isLandscape,
)
