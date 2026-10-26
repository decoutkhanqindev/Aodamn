package com.decoutkhanqindev.aodamn.presentation.screens.permission.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.aodamn.presentation.model.permission.hasRequiredPermissions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class PermissionState(
    val permissions: ImmutableList<PermissionUiModel> = persistentListOf(),
) {
    val isConfirmEnabled: Boolean
        get() = permissions.isNotEmpty() && permissions.hasRequiredPermissions
}
