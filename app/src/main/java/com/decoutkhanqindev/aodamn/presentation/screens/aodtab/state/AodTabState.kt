package com.decoutkhanqindev.aodamn.presentation.screens.aodtab.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.aodamn.presentation.model.permission.hasRequiredPermissions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class AodTabState(
    val isLoading: Boolean = true,
    val isAodEnabled: Boolean = false,
    val design: CustomizeDraftUiModel = CustomizeDraftUiModel(),
    val isThemeResetConfirmVisible: Boolean = false,
    val permission: AodTabPermissionState = AodTabPermissionState(),
) {
    val activeTheme: AodThemeValue
        get() = design.appearance.clock.theme.value

    private val isMissingRequiredPermissions: Boolean
        get() = !isLoading && permission.requiredPermissions.isNotEmpty() && !permission.requiredPermissions.hasRequiredPermissions

    val isPermissionSheetVisible: Boolean
        get() = isMissingRequiredPermissions && !permission.isSheetDismissed

    val isPermissionWarningVisible: Boolean
        get() = isMissingRequiredPermissions && permission.isSheetDismissed
}

@Immutable
data class AodTabPermissionState(
    val requiredPermissions: ImmutableList<PermissionUiModel> = persistentListOf(),
    val isSheetDismissed: Boolean = false,
)
