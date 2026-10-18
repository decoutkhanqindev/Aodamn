package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.permission.hasRequiredPermissions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

@Immutable
data class AodTabState(
    val isLoading: Boolean = true,
    val isAodEnabled: Boolean = false,
    val design: CustomizeDraftUiModel = CustomizeDraftUiModel(),
    // Cấu hình đã lưu của từng chủ đề cho lưới chủ đề (chủ đề chưa chỉnh không có mục: lấy mặc định).
    val themeConfigs: ImmutableMap<AodThemeValue, AodThemeConfig> = persistentMapOf(),
    val isThemeResetConfirmVisible: Boolean = false,
    val permission: AodTabPermissionState = AodTabPermissionState(),
) {
    val activeTheme: AodThemeValue
        get() = design.appearance.clock.theme.value

    // Onboarding có màn quyền riêng nhưng user bỏ qua màn Customize thì vào thẳng Main; thiếu quyền bắt buộc thì nhắc bằng bottom sheet.
    private val isMissingRequiredPermissions: Boolean
        get() = !isLoading && permission.requiredPermissions.isNotEmpty() && !permission.requiredPermissions.hasRequiredPermissions

    // Đóng sheet (hay bỏ qua màn quyền) thì nó không tự hiện lại tới lần mở app sau (ViewModel tạo mới) hay lần bật AOD;
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
