package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state

import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue

sealed interface AodTabIntent {
    data class ToggleAod(val isEnabled: Boolean) : AodTabIntent
    data object RefreshDesign : AodTabIntent
    data object NavigateToCustomize : AodTabIntent
    data class OpenTheme(val theme: AodThemeValue) : AodTabIntent
    data object RefreshPermissions : AodTabIntent
    data object ShowPermissionSheet : AodTabIntent
    data object DismissPermissionSheet : AodTabIntent
    data class OpenPermissionSettings(val permission: PermissionValue) : AodTabIntent
    data class NotificationPermissionResult(val isGranted: Boolean) : AodTabIntent
}
