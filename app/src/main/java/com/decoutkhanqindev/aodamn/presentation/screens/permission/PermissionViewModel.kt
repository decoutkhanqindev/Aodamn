package com.decoutkhanqindev.aodamn.presentation.screens.permission

import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.aodamn.data.device.permission.PermissionManager
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.presentation.base.BaseViewModel
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.hasRequiredPermissions
import com.decoutkhanqindev.aodamn.presentation.model.permission.requiredAodPermissions
import com.decoutkhanqindev.aodamn.presentation.screens.permission.state.PermissionEffect
import com.decoutkhanqindev.aodamn.presentation.screens.permission.state.PermissionIntent
import com.decoutkhanqindev.aodamn.presentation.screens.permission.state.PermissionState
import kotlinx.coroutines.launch
import timber.log.Timber

class PermissionViewModel(
    private val dataStoreManager: DataStoreManager,
    private val permissionManager: PermissionManager,
) : BaseViewModel<PermissionState, PermissionIntent, PermissionEffect>(
    initialState = PermissionState(permissions = permissionManager.requiredAodPermissions()),
) {

    override fun onIntent(intent: PermissionIntent) {
        Timber.tag(tag).d("onIntent: $intent")
        when (intent) {
            is PermissionIntent.RefreshPermissions -> refreshPermissions()
            is PermissionIntent.OpenPermissionSettings -> openPermissionSettings(intent.permission)
            is PermissionIntent.NotificationPermissionResult -> onNotificationPermissionResult(
                intent.isGranted
            )

            is PermissionIntent.ConfirmPermissions -> confirmPermissions()
            is PermissionIntent.SkipPermissions ->
                viewModelScope.launch { sendEffect(PermissionEffect.NavigateToMain(isPermissionSkipped = true)) }

            is PermissionIntent.NavigateBack -> viewModelScope.launch { sendEffect(PermissionEffect.NavigateBack) }
        }
    }

    private fun refreshPermissions() {
        updateState { copy(permissions = permissionManager.requiredAodPermissions()) }
    }

    private fun openPermissionSettings(permission: PermissionValue) {
        val effect = when (permission) {
            PermissionValue.OVERLAY -> PermissionEffect.OpenOverlaySettings
            PermissionValue.MIUI_LOCK_SCREEN,
            PermissionValue.MIUI_BACKGROUND_POPUP -> PermissionEffect.OpenMiuiPermissionSettings(permission)

            PermissionValue.NOTIFICATIONS ->
                if (permissionManager.needsNotificationPermission()) {
                    PermissionEffect.RequestNotificationPermission
                } else {
                    PermissionEffect.OpenNotificationSettings
                }

            else -> return
        }
        viewModelScope.launch { sendEffect(effect) }
    }

    private fun onNotificationPermissionResult(isGranted: Boolean) {
        refreshPermissions()
        viewModelScope.launch {
            when {
                isGranted -> if (dataStoreManager.isAodEnabled.value == true) sendEffect(
                    PermissionEffect.StartAodService
                )

                else -> sendEffect(PermissionEffect.OpenNotificationSettings)
            }
        }
    }

    private fun confirmPermissions() {
        val permissions = permissionManager.requiredAodPermissions()
        updateState { copy(permissions = permissions) }
        if (!permissions.hasRequiredPermissions) return
        viewModelScope.launch { sendEffect(PermissionEffect.NavigateToMain(isPermissionSkipped = false)) }
    }
}
