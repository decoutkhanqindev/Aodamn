package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab

import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.data.device.permission.PermissionManager
import com.decoutkhanqindev.custom_aod.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.custom_aod.data.local.image.AodImageManager
import com.decoutkhanqindev.custom_aod.presentation.base.BaseViewModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.model.customize.loadCustomizeDraft
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.requiredAodPermissions
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabEffect
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabPermissionState
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabState
import com.decoutkhanqindev.custom_aod.utils.collectCatching
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber

// isPermissionSkipped: user bấm Bỏ qua ở màn quyền onboarding thì sheet quyền bắt đầu ở trạng thái đã đóng, chỉ hiện lại khi bật AOD
// hay chạm dòng cảnh báo; Áp dụng ở Tùy chỉnh / màn chủ đề vẫn đòi quyền như thường.
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

    // Giao diện đổi ở màn Customize và màn chủ đề, quyền đổi ở app Cài đặt (mục cần quyền chỉ hiện khi có quyền), nên đọc lại mỗi lần
    // màn hình resume.
    private fun refreshDesign() {
        designJob?.cancel()
        designJob = viewModelScope.launch {
            val design = dataStoreManager.loadCustomizeDraft(aodImageManager, permissionManager)
            updateState { copy(design = design) }
        }
    }

    // Đặt lại về chủ đề mặc định: chỉ đổi chủ đề đang dùng, kiểu đã lưu của từng chủ đề giữ nguyên. Chờ ghi xong rồi đọc lại thẻ
    // "Giao diện của bạn" và chủ đề đang dùng.
    private fun resetTheme() {
        updateState { copy(isThemeResetConfirmVisible = false) }
        viewModelScope.launch {
            dataStoreManager.saveAodThemeId(AodThemeValue.DEFAULT.id).join()
            refreshDesign()
            sendEffect(AodTabEffect.ShowMessage(R.string.aod_tab_theme_reset_done))
        }
    }

    // Trạng thái quyền đổi ở app Cài đặt chứ không phải ở đây, nên đọc lại mỗi lần màn hình resume.
    private fun refreshPermissions() {
        updateState { copy(permission = permission.copy(requiredPermissions = permissionManager.requiredAodPermissions())) }
    }

    private fun showPermissionSheet() {
        updateState { copy(permission = permission.copy(isSheetDismissed = false)) }
    }

    // Bật AOD là lúc cần quyền: còn thiếu quyền bắt buộc thì sheet nhắc quyền hiện lại dù trước đó đã bị đóng. Áp dụng giao diện ở
    // Tùy chỉnh / màn chủ đề thì chính màn đó đòi đủ quyền bắt buộc trước khi ghi.
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

    // Đã cho phép thì start lại service để nó đăng lại thông báo; bị từ chối thì mở trang cài đặt thông báo (sau 2 lần từ chối hệ thống không hiện hộp thoại nữa).
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
