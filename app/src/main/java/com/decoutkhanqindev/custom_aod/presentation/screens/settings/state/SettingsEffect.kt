package com.decoutkhanqindev.custom_aod.presentation.screens.settings.state

import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue

sealed interface SettingsEffect {
    sealed interface Permission : SettingsEffect {
        data object OpenOverlaySettings : Permission
        data object OpenMiuiPermissionSettings : Permission
        data object OpenNotificationSettings : Permission
        data object OpenNotificationAccessSettings : Permission
        data class OpenAppSettings(val permission: PermissionValue) : Permission
        data object RequestNotificationPermission : Permission
        data object RequestCalendarPermission : Permission
        data object RequestLocationPermission : Permission
    }

    sealed interface Navigation : SettingsEffect {
        data object NavigateToLanguage : Navigation
        data object NavigateToIap : Navigation
    }

    data object StartAodService : SettingsEffect
    data object ShareApp : SettingsEffect
}
