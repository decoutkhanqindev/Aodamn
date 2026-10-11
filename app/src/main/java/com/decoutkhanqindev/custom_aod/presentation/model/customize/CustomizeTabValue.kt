package com.decoutkhanqindev.custom_aod.presentation.model.customize

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue

// Mỗi tab chỉnh một phần trên AOD; permission = quyền phải có thì phần đó mới hiện được trên AOD thật. isOnboardingOnly: chỉ hiện
// lúc mở app lần đầu để user mới thấy hết app làm được gì (chọn chủ đề đồng hồ); sau đó chỉnh ở tab AOD › Chủ đề.
@Immutable
enum class CustomizeTabValue(
    val cluster: CustomizeClusterValue,
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val labelRes: Int,
    val permission: PermissionValue? = null,
    val isOnboardingOnly: Boolean = false,
) {
    CLOCK(
        CustomizeClusterValue.APPEARANCE,
        R.drawable.ic_clock,
        R.string.customize_tab_clock,
        isOnboardingOnly = true,
    ),
    DATE(
        CustomizeClusterValue.APPEARANCE,
        R.drawable.ic_calendar_blank,
        R.string.customize_tab_date
    ),
    BATTERY(
        CustomizeClusterValue.APPEARANCE,
        R.drawable.ic_battery_full,
        R.string.customize_tab_battery
    ),
    BACKGROUND(
        CustomizeClusterValue.DECOR,
        R.drawable.ic_image,
        R.string.customize_tab_background
    ),
    DRAWING(CustomizeClusterValue.DECOR, R.drawable.ic_paint_brush, R.string.customize_tab_drawing),
    MEMO(CustomizeClusterValue.DECOR, R.drawable.ic_note_pencil, R.string.customize_tab_memo),
    NOTIFICATIONS(
        CustomizeClusterValue.INFO,
        R.drawable.ic_bell,
        R.string.customize_tab_notifications,
        PermissionValue.NOTIFICATION_ACCESS,
    ),
    EVENTS(
        CustomizeClusterValue.INFO,
        R.drawable.ic_calendar_check,
        R.string.customize_tab_events,
        PermissionValue.CALENDAR
    ),
    WEATHER(
        CustomizeClusterValue.INFO,
        R.drawable.ic_cloud_sun,
        R.string.customize_tab_weather,
        PermissionValue.LOCATION
    ),
    MEDIA(
        CustomizeClusterValue.INFO,
        R.drawable.ic_music_notes,
        R.string.customize_tab_media,
        PermissionValue.NOTIFICATION_ACCESS,
    ),
    EFFECTS(
        CustomizeClusterValue.EFFECT,
        R.drawable.ic_sparkle,
        R.string.customize_tab_effects,
        PermissionValue.NOTIFICATION_ACCESS,
    ),
}

fun CustomizeTabValue.isShown(isFirstOpen: Boolean): Boolean = isFirstOpen || !isOnboardingOnly
