package com.decoutkhanqindev.aodamn.presentation.model.permission

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R

// Mọi quyền app dùng. isRequired: thiếu thì AOD mặc định không hiện được — màn quyền lúc mở app lần đầu và bottom sheet ở màn chính đòi đủ.
@Immutable
enum class PermissionValue(
    @param:StringRes val titleRes: Int,
    @param:StringRes val nameRes: Int,
    @param:StringRes val descriptionRes: Int,
    @param:DrawableRes val iconRes: Int,
    val isRequired: Boolean,
) {
    OVERLAY(
        titleRes = R.string.perm_overlay_title,
        nameRes = R.string.perm_overlay_name,
        descriptionRes = R.string.perm_overlay_desc,
        iconRes = R.drawable.ic_stack_duotone,
        isRequired = true,
    ),
    MIUI_LOCK_SCREEN(
        titleRes = R.string.perm_lock_title,
        nameRes = R.string.perm_lock_name,
        descriptionRes = R.string.perm_lock_desc,
        iconRes = R.drawable.ic_lock_simple_duotone,
        isRequired = true,
    ),

    // HyperOS chặn mở AOD từ nền khi thiếu quyền này, kể cả khi đã có "Hiển thị trên ứng dụng khác".
    MIUI_BACKGROUND_POPUP(
        titleRes = R.string.perm_popup_title,
        nameRes = R.string.perm_popup_name,
        descriptionRes = R.string.perm_popup_desc,
        iconRes = R.drawable.ic_picture_in_picture_duotone,
        isRequired = true,
    ),

    // Thông báo của foreground service giữ đồng hồ chạy nền; Android 13+ phải được cho phép mới hiện.
    NOTIFICATIONS(
        titleRes = R.string.perm_notif_title,
        nameRes = R.string.perm_notif_name,
        descriptionRes = R.string.perm_notif_desc,
        iconRes = R.drawable.ic_bell_duotone,
        isRequired = true,
    ),

    // Chỉ cho icon thông báo, viền sáng và điều khiển nhạc; đồng hồ vẫn chạy khi thiếu.
    NOTIFICATION_ACCESS(
        titleRes = R.string.perm_notif_access_title,
        nameRes = R.string.perm_notif_access_title,
        descriptionRes = R.string.perm_notif_access_desc,
        iconRes = R.drawable.ic_bell_ringing_duotone,
        isRequired = false,
    ),

    // Chỉ cho "Sự kiện hôm nay" và "Thời tiết": bật hai mục đó mới hỏi.
    CALENDAR(
        titleRes = R.string.perm_calendar_title,
        nameRes = R.string.perm_calendar_title,
        descriptionRes = R.string.perm_calendar_desc,
        iconRes = R.drawable.ic_calendar_duotone,
        isRequired = false,
    ),
    LOCATION(
        titleRes = R.string.perm_location_title,
        nameRes = R.string.perm_location_title,
        descriptionRes = R.string.perm_location_desc,
        iconRes = R.drawable.ic_map_pin_duotone,
        isRequired = false,
    ),
}
