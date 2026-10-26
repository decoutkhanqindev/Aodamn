package com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.isSame
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tabOf
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.aodamn.presentation.model.customize.toThemePreviewDraft
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ThemeEditorState(
    val theme: AodThemeValue = AodThemeValue.DEFAULT,
    val isLoading: Boolean = true,
    val config: AodThemeConfig = AodThemeConfig(),
    val savedConfig: AodThemeConfig = AodThemeConfig(),
    val isActive: Boolean = false,
    val isEditing: Boolean = false,
    val selectedTab: AodThemeTab? = null,
    val history: ThemeEditorHistoryState = ThemeEditorHistoryState(),
    val isApplying: Boolean = false,
    val isLandscapePreview: Boolean = false,
    val requiredPermissions: ImmutableList<PermissionUiModel> = persistentListOf(),
    val isPermissionSheetVisible: Boolean = false,
) {
    val preview: CustomizeDraftUiModel
        get() = AodThemeUiModel(value = theme, config = config).toThemePreviewDraft()

    val hasChanges: Boolean
        get() = !theme.spec.isSame(config, savedConfig)

    fun focusedOn(block: AodBlockValue?): ThemeEditorState {
        val tab = theme.spec.tabOf(block) ?: return this
        return copy(isEditing = true, selectedTab = tab)
    }
}

@Immutable
data class ThemeEditorHistoryState(
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val isDefault: Boolean = true,
    val isResetConfirmVisible: Boolean = false,
    val isDiscardConfirmVisible: Boolean = false,
)
