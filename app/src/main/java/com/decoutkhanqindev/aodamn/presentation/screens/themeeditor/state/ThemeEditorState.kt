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
    val isLandscapePreview: Boolean = false,
    // Áp dụng khi còn thiếu quyền bắt buộc: sheet liệt kê các quyền đó, cấp đủ thì tự áp dụng, đóng sheet thì không áp dụng.
    val requiredPermissions: ImmutableList<PermissionUiModel> = persistentListOf(),
    val isPermissionSheetVisible: Boolean = false,
) {
    val preview: CustomizeDraftUiModel
        get() = AodThemeUiModel(value = theme, config = config).toThemePreviewDraft()

    val hasChanges: Boolean
        get() = !theme.spec.isSame(config, savedConfig)

    // Chủ đề không có tab cho khối đó (vd Cổ điển không tự vẽ khối nào) thì giữ chế độ xem.
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
