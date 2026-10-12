package com.decoutkhanqindev.custom_aod.presentation.model.customize

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodComponentValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue

// Mỗi tab chỉnh một phần trên AOD; permission = quyền phải có thì phần đó mới hiện được trên AOD thật. component: khối dùng chung mà tab
// cho chỉnh font, cỡ, màu riêng (ngoài việc bật / tắt và nội dung của khối); tab Đồng hồ (CLOCK) lúc onboarding chỉnh Cổ điển, sau đó dẫn
// sang màn chủ đề.
@Immutable
enum class CustomizeTabValue(
    val cluster: CustomizeClusterValue,
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val labelRes: Int,
    val permission: PermissionValue? = null,
    val component: AodComponentValue? = null,
) {
    CLOCK(CustomizeClusterValue.APPEARANCE, R.drawable.ic_clock, R.string.customize_tab_clock),
    DATE(
        CustomizeClusterValue.APPEARANCE,
        R.drawable.ic_calendar_blank,
        R.string.customize_tab_date,
        component = AodComponentValue.DATE,
    ),
    BATTERY(
        CustomizeClusterValue.APPEARANCE,
        R.drawable.ic_battery_full,
        R.string.customize_tab_battery,
        component = AodComponentValue.BATTERY,
    ),
    BACKGROUND(
        CustomizeClusterValue.DECOR,
        R.drawable.ic_image,
        R.string.customize_tab_background,
    ),
    DRAWING(
        CustomizeClusterValue.DECOR,
        R.drawable.ic_paint_brush,
        R.string.customize_tab_drawing,
        component = AodComponentValue.DRAWING,
    ),
    MEMO(
        CustomizeClusterValue.DECOR,
        R.drawable.ic_note_pencil,
        R.string.customize_tab_memo,
        component = AodComponentValue.MEMO,
    ),
    NOTIFICATIONS(
        CustomizeClusterValue.INFO,
        R.drawable.ic_bell,
        R.string.customize_tab_notifications,
        PermissionValue.NOTIFICATION_ACCESS,
        AodComponentValue.NOTIFICATIONS,
    ),
    EVENTS(
        CustomizeClusterValue.INFO,
        R.drawable.ic_calendar_check,
        R.string.customize_tab_events,
        PermissionValue.CALENDAR,
        AodComponentValue.EVENTS,
    ),
    WEATHER(
        CustomizeClusterValue.INFO,
        R.drawable.ic_cloud_sun,
        R.string.customize_tab_weather,
        PermissionValue.LOCATION,
        AodComponentValue.WEATHER,
    ),
    MEDIA(
        CustomizeClusterValue.INFO,
        R.drawable.ic_music_notes,
        R.string.customize_tab_media,
        PermissionValue.NOTIFICATION_ACCESS,
        AodComponentValue.MEDIA,
    ),
    EFFECTS(
        CustomizeClusterValue.EFFECT,
        R.drawable.ic_sparkle,
        R.string.customize_tab_effects,
        PermissionValue.NOTIFICATION_ACCESS,
    ),
}
