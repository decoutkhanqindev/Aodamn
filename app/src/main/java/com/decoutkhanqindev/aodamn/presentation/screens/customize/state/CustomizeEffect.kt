package com.decoutkhanqindev.aodamn.presentation.screens.customize.state

import androidx.annotation.StringRes
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue

sealed interface CustomizeEffect {
    sealed interface Decor : CustomizeEffect {
        data object OpenBackgroundPicker : Decor
    }

    sealed interface Permission : CustomizeEffect {
        data object RequestCalendarPermission : Permission
        data object RequestLocationPermission : Permission
        data object OpenNotificationAccessSettings : Permission
        data class OpenAppSettings(val permission: PermissionValue) : Permission
        data object OpenOverlaySettings : Permission
        data class OpenMiuiPermissionSettings(val permission: PermissionValue) : Permission
        data object RequestNotificationPermission : Permission
        data object OpenNotificationSettings : Permission
    }

    sealed interface Navigation : CustomizeEffect {
        data object NavigateToPermission : Navigation
        data object NavigateToMain : Navigation
        data class NavigateToThemeEditor(val themeId: String, val focusBlock: AodBlockValue?) : Navigation
        data object NavigateBack : Navigation
    }

    data class ShowMessage(
        @param:StringRes val messageRes: Int,
        val isError: Boolean = false,
    ) : CustomizeEffect
}
