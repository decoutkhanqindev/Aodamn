package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gradientline

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.GradientSwatchValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansText
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE0
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

// Chủ đề Dải màu (Gradient Line Clock của Muviz Edge, plans/muviz-edge-themes.md): vạch gradient mảnh chạy dọc cạnh dài của màn, giờ và ngày
// nằm trên vạch (vạch khoét quanh), trượt từ đầu vạch (00:00) tới cuối vạch (23:59) theo phút trong ngày; trình phát ở đầu màn, dòng pin ·
// thông báo neo đáy. Ghi nhớ, sự kiện, nét vẽ không có chỗ: khối giờ quét hết chiều dài vạch, hai bên vạch hẹp (mock đã duyệt).
// App mẫu có màu chủ đạo cho mọi phần chưa chỉnh riêng; bên mình mỗi phần một màu, mặc định #E0E0E0 như màu chủ đạo của app mẫu (user duyệt).
object GradientLineThemeSpec : AodThemeSpec {
    // App mẫu dùng Product Sans Regular cho giờ, ngày, trình phát: Google Sans cùng thiết kế.
    val Font: FontFamily = FontFamilyGoogleSansText

    // Cỡ giờ = 0,11 cạnh ngắn của vùng vạch × cỡ, dải và mặc định như app mẫu.
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 80..150,
        step = 10,
        default = 110,
    )

    // Mặc định theo giờ như app mẫu (Position −1); tắt thì ghim khối giờ ở 0–100% chiều dài vạch, 20 nấc như thanh trượt của app mẫu.
    val Position = AodRangeOption(
        key = "position",
        labelRes = R.string.opt_position_value,
        range = 0..100,
        step = 5,
        default = AodRangeOption.AUTO,
        autoLabelRes = R.string.opt_position_auto,
    )

    val ClockColor = AodColorOption(
        key = "clock_color",
        labelRes = R.string.theme_color_clock,
        default = GreyE0,
        isGradientAllowed = true,
    )

    // Gradient của app mẫu (vàng → hồng tím → xanh từ đầu tới cuối vạch) là màu nhấn của chủ đề; vạch luôn tô dọc theo vạch, bỏ qua hướng.
    val LineColor = AodColorOption(
        key = "line_color",
        labelRes = R.string.theme_color_line,
        default = GradientSwatchValue.SUNSET.gradient.start,
        defaultGradient = GradientSwatchValue.SUNSET.gradient,
    )

    // App mẫu vẽ ngày bằng màu giờ mờ 78%; tách màu riêng cho tab Ngày như các chủ đề khác (user duyệt), vẫn mờ 78%.
    val DateColor = AodColorOption(
        key = "date_color",
        labelRes = R.string.theme_color_date,
        default = GreyE0,
        isGradientAllowed = true,
    )
    val BatteryColor = AodColorOption(key = "battery_color", labelRes = R.string.theme_color_battery, default = GreyE0, isGradientAllowed = true)
    val NotificationsColor = AodColorOption(
        key = "notifications_color",
        labelRes = R.string.theme_color_notifications,
        default = GreyE0,
        isGradientAllowed = true,
    )

    // Tên bài, nghệ sĩ, các nút.
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyE0, isGradientAllowed = true)

    // Mỗi khối chủ đề tự vẽ (ownBlocks) có tab ở đây: Tùy chỉnh › Ngày / Pin / Thông báo / Nhạc dẫn sang màn chủ đề.
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Size, Position, ClockColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_line_vertical,
            labelRes = R.string.theme_tab_line,
            options = persistentListOf(LineColor),
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
            options = persistentListOf(BatteryColor, NotificationsColor),
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

    // App mẫu hiện thông báo mới (icon + chữ) ở đáy: chủ đề tự vẽ dòng thông báo mới nhất trên dòng pin.
    override val hasNotificationContent = true
}
