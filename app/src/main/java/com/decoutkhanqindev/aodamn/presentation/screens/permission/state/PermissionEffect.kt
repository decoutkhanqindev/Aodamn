package com.decoutkhanqindev.aodamn.presentation.screens.permission.state

import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue

sealed interface PermissionEffect {
    data object OpenOverlaySettings : PermissionEffect
    data class OpenMiuiPermissionSettings(val permission: PermissionValue) : PermissionEffect
    data object OpenNotificationSettings : PermissionEffect
    data object RequestNotificationPermission : PermissionEffect
    data object StartAodService : PermissionEffect
    data class NavigateToMain(val isPermissionSkipped: Boolean) : PermissionEffect
    data object NavigateBack : PermissionEffect
}
