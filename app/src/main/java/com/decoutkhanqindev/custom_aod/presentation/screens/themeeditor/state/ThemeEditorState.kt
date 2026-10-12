package com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.isSame
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.toThemePreviewDraft

// Một màn cho mọi chủ đề: xem trước (Sửa / Áp dụng, đang dùng thì Tùy chỉnh) rồi sửa (tab theo AodThemeSpec.tabs, Lưu). Khung xem
// trước chỉ riêng chủ đề (đồng hồ, ngày, khối chủ đề tự vẽ), không có nền, thông báo, pin... của user: AOD đầy đủ chỉ ở Tùy chỉnh.
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
) {
    val preview: CustomizeDraftUiModel
        get() = AodThemeUiModel(value = theme, config = config).toThemePreviewDraft()

    val hasChanges: Boolean
        get() = !theme.spec.isSame(config, savedConfig)
}

@Immutable
data class ThemeEditorHistoryState(
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val isDefault: Boolean = true,
    val isResetConfirmVisible: Boolean = false,
    val isDiscardConfirmVisible: Boolean = false,
)
