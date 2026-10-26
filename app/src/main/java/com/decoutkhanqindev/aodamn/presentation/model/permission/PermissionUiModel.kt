package com.decoutkhanqindev.aodamn.presentation.model.permission

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.device.permission.PermissionManager
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class PermissionUiModel(
    val permission: PermissionValue,
    val isGranted: Boolean?,
) {
    val status: PermissionStatusValue
        get() = when {
            isGranted == true -> PermissionStatusValue.GRANTED
            isGranted == null -> PermissionStatusValue.UNKNOWN
            permission.isRequired -> PermissionStatusValue.MISSING_REQUIRED
            else -> PermissionStatusValue.MISSING_OPTIONAL
        }
}

fun PermissionManager.aodPermissions(): ImmutableList<PermissionUiModel> = buildList {
    add(PermissionUiModel(PermissionValue.OVERLAY, canDrawOverlays()))
    if (isXiaomi) {
        add(PermissionUiModel(PermissionValue.MIUI_LOCK_SCREEN, isMiuiShowWhenLockedAllowed()))
        add(
            PermissionUiModel(
                PermissionValue.MIUI_BACKGROUND_POPUP,
                isMiuiBackgroundStartAllowed()
            )
        )
    }
    add(PermissionUiModel(PermissionValue.NOTIFICATIONS, areNotificationsEnabled()))
    add(PermissionUiModel(PermissionValue.NOTIFICATION_ACCESS, isNotificationListenerEnabled()))
    add(PermissionUiModel(PermissionValue.CALENDAR, hasCalendarPermission()))
    add(PermissionUiModel(PermissionValue.LOCATION, hasCoarseLocationPermission()))
}.toImmutableList()

fun PermissionManager.isGranted(permission: PermissionValue): Boolean = when (permission) {
    PermissionValue.OVERLAY -> canDrawOverlays()
    PermissionValue.MIUI_LOCK_SCREEN -> isMiuiShowWhenLockedAllowed() != false
    PermissionValue.MIUI_BACKGROUND_POPUP -> isMiuiBackgroundStartAllowed() != false
    PermissionValue.NOTIFICATIONS -> areNotificationsEnabled()
    PermissionValue.NOTIFICATION_ACCESS -> isNotificationListenerEnabled()
    PermissionValue.CALENDAR -> hasCalendarPermission()
    PermissionValue.LOCATION -> hasCoarseLocationPermission()
}

fun PermissionManager.requiredAodPermissions(): ImmutableList<PermissionUiModel> =
    aodPermissions().filter { it.permission.isRequired }.toImmutableList()

val List<PermissionUiModel>.hasRequiredPermissions: Boolean
    get() = none { it.permission.isRequired && it.isGranted == false }

fun List<PermissionUiModel>.nextPermission(): PermissionValue? =
    firstOrNull { permission -> permission.isGranted == false }?.permission
