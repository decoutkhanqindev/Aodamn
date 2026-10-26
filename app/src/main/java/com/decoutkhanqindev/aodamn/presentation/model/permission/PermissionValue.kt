package com.decoutkhanqindev.aodamn.presentation.model.permission

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R

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

    MIUI_BACKGROUND_POPUP(
        titleRes = R.string.perm_popup_title,
        nameRes = R.string.perm_popup_name,
        descriptionRes = R.string.perm_popup_desc,
        iconRes = R.drawable.ic_picture_in_picture_duotone,
        isRequired = true,
    ),

    NOTIFICATIONS(
        titleRes = R.string.perm_notif_title,
        nameRes = R.string.perm_notif_name,
        descriptionRes = R.string.perm_notif_desc,
        iconRes = R.drawable.ic_bell_duotone,
        isRequired = true,
    ),

    NOTIFICATION_ACCESS(
        titleRes = R.string.perm_notif_access_title,
        nameRes = R.string.perm_notif_access_title,
        descriptionRes = R.string.perm_notif_access_desc,
        iconRes = R.drawable.ic_bell_ringing_duotone,
        isRequired = false,
    ),

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
