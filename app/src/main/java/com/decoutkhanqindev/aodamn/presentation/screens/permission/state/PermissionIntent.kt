package com.decoutkhanqindev.aodamn.presentation.screens.permission.state

import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue

sealed interface PermissionIntent {
    data object RefreshPermissions : PermissionIntent
    data class OpenPermissionSettings(val permission: PermissionValue) : PermissionIntent
    data class NotificationPermissionResult(val isGranted: Boolean) : PermissionIntent
    data object ConfirmPermissions : PermissionIntent
    data object SkipPermissions : PermissionIntent
    data object NavigateBack : PermissionIntent
}
