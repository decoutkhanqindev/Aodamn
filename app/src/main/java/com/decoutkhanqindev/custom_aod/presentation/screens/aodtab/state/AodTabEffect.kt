package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state

import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue

sealed interface AodTabEffect {
    data object StartAodService : AodTabEffect
    data object StopAodService : AodTabEffect
    data object NavigateToCustomize : AodTabEffect
    data object OpenOverlaySettings : AodTabEffect
    data class OpenMiuiPermissionSettings(val permission: PermissionValue) : AodTabEffect
    data object OpenNotificationSettings : AodTabEffect
    data object OpenNotificationAccessSettings : AodTabEffect
    data class OpenAppSettings(val permission: PermissionValue) : AodTabEffect
    data object RequestNotificationPermission : AodTabEffect
}
