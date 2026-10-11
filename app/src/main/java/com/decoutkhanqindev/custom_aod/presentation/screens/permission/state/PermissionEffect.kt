package com.decoutkhanqindev.custom_aod.presentation.screens.permission.state

import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue

sealed interface PermissionEffect {
    data object OpenOverlaySettings : PermissionEffect
    data class OpenMiuiPermissionSettings(val permission: PermissionValue) : PermissionEffect
    data object OpenNotificationSettings : PermissionEffect
    data object RequestNotificationPermission : PermissionEffect
    data object StartAodService : PermissionEffect
    data object NavigateToMain : PermissionEffect
    data object NavigateBack : PermissionEffect
}
