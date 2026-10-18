package com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.state

import androidx.annotation.StringRes
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue

sealed interface ThemeEditorEffect {
    data class ShowMessage(
        @param:StringRes val messageRes: Int,
        val isError: Boolean = false,
    ) : ThemeEditorEffect
    data object NavigateToCustomize : ThemeEditorEffect
    data object NavigateBack : ThemeEditorEffect
    data object OpenOverlaySettings : ThemeEditorEffect
    data class OpenMiuiPermissionSettings(val permission: PermissionValue) : ThemeEditorEffect
    data object RequestNotificationPermission : ThemeEditorEffect
    data object OpenNotificationSettings : ThemeEditorEffect
}
