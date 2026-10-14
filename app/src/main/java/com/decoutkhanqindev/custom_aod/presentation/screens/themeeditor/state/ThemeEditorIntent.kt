package com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state

import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue

sealed interface ThemeEditorIntent {
    sealed interface Edit : ThemeEditorIntent {
        data object StartEditing : Edit
        data object StopEditing : Edit
        data object ConfirmDiscard : Edit
        data object DismissDiscard : Edit
        data class SelectTab(val tab: AodThemeTab) : Edit
        data object ClosePanel : Edit
    }

    // Giá trị đã mã hoá theo AodThemeOption.encode; Preview là lúc đang kéo (không ghi lịch sử hoàn tác). ResetTab: đặt lại riêng các
    // thuộc tính của tab đó (một bước hoàn tác).
    sealed interface Option : ThemeEditorIntent {
        data class PreviewOption(val key: String, val raw: Int) : Option
        data class ChangeOption(val key: String, val raw: Int) : Option
        data class ResetTab(val tab: AodThemeTab) : Option
    }

    sealed interface History : ThemeEditorIntent {
        data object UndoChange : History
        data object RedoChange : History
        data object ShowResetConfirm : History
        data object DismissResetConfirm : History
        data object ResetTheme : History
    }

    sealed interface Permission : ThemeEditorIntent {
        data class OpenPermissionSettings(val permission: PermissionValue) : Permission
        data object DismissPermissionSheet : Permission
        data class NotificationPermissionResult(val isGranted: Boolean) : Permission
        data object RefreshPermissions : Permission
    }

    data object ToggleLandscapePreview : ThemeEditorIntent
    data object SaveTheme : ThemeEditorIntent
    data object ApplyTheme : ThemeEditorIntent
    data object NavigateToCustomize : ThemeEditorIntent
    data object NavigateBack : ThemeEditorIntent
}
