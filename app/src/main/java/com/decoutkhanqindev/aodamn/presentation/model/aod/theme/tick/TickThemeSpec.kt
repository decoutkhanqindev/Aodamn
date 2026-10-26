package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tick

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradientDirection
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.GradientSwatchValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansText
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE0
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

// Chủ đề Nhịp giây (Coum Face Clock của Muviz Edge, plans/muviz-edge-themes.md): vòng 60 vạch trôi theo chiều kim theo giây thật, chìm dần ở trên /
// dưới, chấm số giây ở mép phải, giờ + ngày giữa vòng; hàng icon + thông báo mới nhất ở trên, trình phát và dòng pin ở dưới (mock đã duyệt). Ghi
// nhớ, sự kiện, nét vẽ không có chỗ đặt cân quanh vòng ở cả dọc lẫn ngang. Màu đồng hồ / điều khiển của app mẫu tách theo phần (user duyệt); bỏ
// trạng thái tiết kiệm của app mẫu (vòng dừng, ẩn chấm giây) để vòng luôn chạy (user chọn).
object TickThemeSpec : AodThemeSpec {
    // App mẫu dùng Product Sans Regular (giờ, ngày, chấm giây, dòng phụ) / Bold (tên bài); Google Sans cùng thiết kế.
    val Font: FontFamily = FontFamilyGoogleSansText

    // Clock Size của app mẫu (55–90, mặc định 70): chỉ đổi cỡ giờ và ngày, vòng giữ nguyên.
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 55..90,
        step = 5,
        default = 70,
    )

    // Giờ, viền và số của chấm giây như màu đồng hồ của app mẫu (mặc định xám sáng, app mẫu tím nhạt).
    val ClockColor = AodColorOption(key = "clock_color", labelRes = R.string.theme_color_clock, default = GreyE0, isGradientAllowed = true)

    // Màu nhấn của chủ đề: Tropic trải dọc (cam vàng trên, xanh ngọc hai bên, cam đỏ dưới) thay Sunset của app mẫu vì Dải màu đã dùng.
    val RingColor = AodColorOption(
        key = "ring_color",
        labelRes = R.string.theme_color_ring,
        default = GradientSwatchValue.TROPIC.gradient.start,
        defaultGradient = GradientSwatchValue.TROPIC.gradient.copy(direction = AodGradientDirection.VERTICAL),
    )

    // App mẫu vẽ ngày bằng màu đồng hồ tối hơn 30%; mặc định đúng màu đó.
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = Grey9A, isGradientAllowed = true)
    val NotificationsColor = AodColorOption(
        key = "notifications_color",
        labelRes = R.string.theme_color_notifications,
        default = GreyE0,
        isGradientAllowed = true,
    )
    val BatteryColor = AodColorOption(key = "battery_color", labelRes = R.string.theme_color_battery, default = GreyE0, isGradientAllowed = true)

    // Tên bài, các nút; nghệ sĩ tối hơn 30% như app mẫu.
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyE0, isGradientAllowed = true)

    // Mỗi khối chủ đề tự vẽ (ownBlocks) có tab ở đây: Tùy chỉnh › Ngày / Pin / Thông báo / Nhạc dẫn sang màn chủ đề.
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Size, ClockColor, RingColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_calendar_blank,
            labelRes = R.string.theme_tab_date,
            options = persistentListOf(DateColor),
            block = AodBlockValue.DATE,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_battery_full,
            labelRes = R.string.theme_tab_status,
            options = persistentListOf(NotificationsColor, BatteryColor),
            block = AodBlockValue.STATUS,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_music_notes,
            labelRes = R.string.theme_tab_media,
            options = persistentListOf(MediaColor),
            block = AodBlockValue.MEDIA,
        ),
    )

    override val ownBlocks = persistentSetOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA)

    override val components = persistentSetOf(
        AodComponentValue.DATE,
        AodComponentValue.BATTERY,
        AodComponentValue.WEATHER,
        AodComponentValue.NOTIFICATIONS,
        AodComponentValue.MEDIA,
    )

    // App mẫu hiện thông báo mới (icon + chữ in hoa chạy chữ) thay hàng icon: chủ đề tự vẽ thông báo mới nhất dưới hàng icon.
    override val hasNotificationContent = true

    // Vòng trôi và số giây chạy theo giờ thật như app mẫu, cả ở màn sửa / xem trước.
    override val isLiveInPreview = true
}
