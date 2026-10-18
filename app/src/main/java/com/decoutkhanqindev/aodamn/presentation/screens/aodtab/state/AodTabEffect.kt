package com.decoutkhanqindev.aodamn.presentation.screens.aodtab.state

import androidx.annotation.StringRes
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue

sealed interface AodTabEffect {
    data object StartAodService : AodTabEffect
    data object StopAodService : AodTabEffect
    data object NavigateToCustomize : AodTabEffect
    data class NavigateToThemeEditor(val themeId: String) : AodTabEffect
    data object OpenOverlaySettings : AodTabEffect
    data class OpenMiuiPermissionSettings(val permission: PermissionValue) : AodTabEffect
    data object OpenNotificationSettings : AodTabEffect
    data object OpenNotificationAccessSettings : AodTabEffect
    data class OpenAppSettings(val permission: PermissionValue) : AodTabEffect
    data object RequestNotificationPermission : AodTabEffect
    data class ShowMessage(
        @param:StringRes val messageRes: Int,
        val isError: Boolean = false,
    ) : AodTabEffect
}
