package com.decoutkhanqindev.aodamn.presentation.screens.aodtab

import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.data.device.permission.PermissionManager
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.data.local.image.AodImageManager
import com.decoutkhanqindev.aodamn.presentation.base.BaseViewModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.customize.loadCustomizeDraft
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.requiredAodPermissions
import com.decoutkhanqindev.aodamn.presentation.screens.aodtab.state.AodTabEffect
import com.decoutkhanqindev.aodamn.presentation.screens.aodtab.state.AodTabIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aodtab.state.AodTabPermissionState
import com.decoutkhanqindev.aodamn.presentation.screens.aodtab.state.AodTabState
import com.decoutkhanqindev.aodamn.utils.collectCatching
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber

class AodTabViewModel(
    isPermissionSkipped: Boolean,
    private val dataStoreManager: DataStoreManager,
    private val permissionManager: PermissionManager,
    private val aodImageManager: AodImageManager,
) : BaseViewModel<AodTabState, AodTabIntent, AodTabEffect>(
    initialState = AodTabState(permission = AodTabPermissionState(isSheetDismissed = isPermissionSkipped)),
) {

    private var designJob: Job? = null

    init {
        observeAodEnabled()
    }

    override fun onIntent(intent: AodTabIntent) {
        Timber.tag(tag).d("onIntent: $intent")
        when (intent) {
            is AodTabIntent.ToggleAod -> toggleAod(intent.isEnabled)
            is AodTabIntent.RefreshDesign -> refreshDesign()
            is AodTabIntent.NavigateToCustomize ->
                viewModelScope.launch { sendEffect(AodTabEffect.NavigateToCustomize) }

            is AodTabIntent.OpenTheme ->
                viewModelScope.launch { sendEffect(AodTabEffect.NavigateToThemeEditor(intent.theme.id)) }

            is AodTabIntent.ShowThemeResetConfirm -> updateState { copy(isThemeResetConfirmVisible = true) }
            is AodTabIntent.DismissThemeResetConfirm -> updateState { copy(isThemeResetConfirmVisible = false) }
            is AodTabIntent.ResetTheme -> resetTheme()

            is AodTabIntent.RefreshPermissions -> refreshPermissions()
            is AodTabIntent.ShowPermissionSheet -> showPermissionSheet()
            is AodTabIntent.DismissPermissionSheet ->
                updateState { copy(permission = permission.copy(isSheetDismissed = true)) }

            is AodTabIntent.OpenPermissionSettings -> openPermissionSettings(intent.permission)
            is AodTabIntent.NotificationPermissionResult -> onNotificationPermissionResult(intent.isGranted)
        }
    }

    private fun observeAodEnabled() {
        viewModelScope.launch {
            dataStoreManager.isAodEnabled.filterNotNull().collectCatching(
                block = { isEnabled -> updateState { copy(isLoading = false, isAodEnabled = isEnabled) } },
                catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
            )
        }
    }

    private fun refreshDesign() {
        designJob?.cancel()
        designJob = viewModelScope.launch {
            val design = dataStoreManager.loadCustomizeDraft(aodImageManager, permissionManager)
            updateState { copy(design = design) }
        }
    }

    private fun resetTheme() {
        updateState { copy(isThemeResetConfirmVisible = false) }
        viewModelScope.launch {
            dataStoreManager.saveAodThemeId(AodThemeValue.DEFAULT.id).join()
            refreshDesign()
            sendEffect(AodTabEffect.ShowMessage(R.string.aod_tab_theme_reset_done))
        }
    }

    private fun refreshPermissions() {
        updateState { copy(permission = permission.copy(requiredPermissions = permissionManager.requiredAodPermissions())) }
    }

    private fun showPermissionSheet() {
        updateState { copy(permission = permission.copy(isSheetDismissed = false)) }
    }

    private fun toggleAod(isEnabled: Boolean) {
        if (isEnabled) showPermissionSheet()
        dataStoreManager.saveIsAodEnabled(isEnabled)
        viewModelScope.launch {
            sendEffect(if (isEnabled) AodTabEffect.StartAodService else AodTabEffect.StopAodService)
        }
    }

    private fun openPermissionSettings(permission: PermissionValue) {
        val effect = when (permission) {
            PermissionValue.OVERLAY -> AodTabEffect.OpenOverlaySettings
            PermissionValue.MIUI_LOCK_SCREEN,
            PermissionValue.MIUI_BACKGROUND_POPUP -> AodTabEffect.OpenMiuiPermissionSettings(permission)

            PermissionValue.NOTIFICATIONS ->
                if (permissionManager.needsNotificationPermission()) {
                    AodTabEffect.RequestNotificationPermission
                } else {
                    AodTabEffect.OpenNotificationSettings
                }

            PermissionValue.NOTIFICATION_ACCESS -> AodTabEffect.OpenNotificationAccessSettings
            PermissionValue.CALENDAR,
            PermissionValue.LOCATION -> AodTabEffect.OpenAppSettings(permission)
        }
        viewModelScope.launch { sendEffect(effect) }
    }

    private fun onNotificationPermissionResult(isGranted: Boolean) {
        refreshPermissions()
        viewModelScope.launch {
            when {
                isGranted -> if (dataStoreManager.isAodEnabled.value == true) sendEffect(AodTabEffect.StartAodService)
                else -> sendEffect(AodTabEffect.OpenNotificationSettings)
            }
        }
    }
}
