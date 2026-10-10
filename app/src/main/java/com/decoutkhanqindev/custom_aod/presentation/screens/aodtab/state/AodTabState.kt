package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.permission.hasRequiredPermissions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class AodTabState(
    val isLoading: Boolean = true,
    val isAodEnabled: Boolean = false,
    val nowMillis: Long = 0L,
    val design: CustomizeDraftUiModel = CustomizeDraftUiModel(),
    val permission: AodTabPermissionState = AodTabPermissionState(),
) {
    // Onboarding có màn quyền riêng nhưng user bỏ qua màn Customize thì vào thẳng Main; thiếu quyền bắt buộc thì nhắc bằng bottom sheet.
    private val isMissingRequiredPermissions: Boolean
        get() = !isLoading && permission.requiredPermissions.isNotEmpty() && !permission.requiredPermissions.hasRequiredPermissions

    // Đóng sheet thì nó không tự hiện lại tới lần mở app sau (ViewModel tạo mới) hay lần bật AOD;
    // trong lúc đó dòng cảnh báo dưới công tắc mở lại được sheet.
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
