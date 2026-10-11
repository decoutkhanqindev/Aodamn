package com.decoutkhanqindev.custom_aod.presentation.screens.settings.state

import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue

sealed interface SettingsEffect {
    sealed interface Permission : SettingsEffect {
        data object OpenOverlaySettings : Permission
        data class OpenMiuiPermissionSettings(val permission: PermissionValue) : Permission
        data object OpenNotificationSettings : Permission
        data object OpenNotificationAccessSettings : Permission
        data class OpenAppSettings(val permission: PermissionValue) : Permission
        data object RequestNotificationPermission : Permission
        data object RequestCalendarPermission : Permission
        data object RequestLocationPermission : Permission
    }

    data object StartAodService : SettingsEffect
    data object NavigateToLanguage : SettingsEffect
}
