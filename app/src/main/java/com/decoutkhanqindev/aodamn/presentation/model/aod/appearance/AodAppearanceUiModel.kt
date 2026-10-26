package com.decoutkhanqindev.aodamn.presentation.model.aod.appearance

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.currentAodComponents
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.observeAodComponents
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.currentAodTheme
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.observeAodTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

@Immutable
data class AodAppearanceUiModel(
    val theme: AodThemeUiModel = AodThemeUiModel(),
    val components: AodComponentsUiModel = AodComponentsUiModel(),
    val isLandscape: Boolean = DataStoreManager.DEFAULT_IS_AOD_LANDSCAPE,
)

fun DataStoreManager.currentAodAppearance(): AodAppearanceUiModel = AodAppearanceUiModel(
    theme = currentAodTheme(),
    components = currentAodComponents(),
    isLandscape = isAodLandscape.value ?: DataStoreManager.DEFAULT_IS_AOD_LANDSCAPE,
)

fun DataStoreManager.observeAodAppearance(): Flow<AodAppearanceUiModel> = combine(
    observeAodTheme(),
    observeAodComponents(),
    isAodLandscape.filterNotNull(),
) { theme, components, isLandscape ->
    AodAppearanceUiModel(theme = theme, components = components, isLandscape = isLandscape)
}
