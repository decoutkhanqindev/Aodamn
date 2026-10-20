package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.halfmoon

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansText
import com.decoutkhanqindev.aodamn.presentation.theme.Grey6E
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE0
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Red
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

// Chủ đề Bán nguyệt (Half Concentric của Muviz Edge, plans/muviz-edge-themes.md): nửa mặt số sát mép trái, vòng giây ngoài và vòng phút trong
// xoay theo giờ thật để giá trị hiện tại chỉ sang phải, giờ lớn và phút trong viên viền; cột ngày, thứ, dòng pin · thông báo trên trục bên phải,
// trình phát dưới mặt số. Ghi nhớ, sự kiện, nét vẽ không có chỗ đặt cân quanh nửa mặt số (mock đã duyệt).
// Đủ 6 màu của app mẫu: giờ, phút, viền viên phút (Ring Color), vạch + số hai vòng (Dash Color), ngày; màu điều khiển tách thành dòng pin và nhạc
// theo phần như các chủ đề khác (user duyệt).
object HalfMoonThemeSpec : AodThemeSpec {
    // App mẫu dùng Product Sans Regular (số, dòng pin, nghệ sĩ) / Bold (ngày, tên bài): Google Sans cùng thiết kế.
    val Font: FontFamily = FontFamilyGoogleSansText

    // Viền viên phút đỏ cho thẻ chủ đề bắt mắt (màu nhấn chưa chủ đề nào dùng); còn lại như app mẫu: số trắng, vạch xám #757575.
    val HourColor = AodColorOption(key = "hour_color", labelRes = R.string.theme_color_hour, default = GreyED)
    val MinuteColor = AodColorOption(key = "minute_color", labelRes = R.string.theme_color_minute, default = GreyED)
    val RingColor = AodColorOption(key = "ring_color", labelRes = R.string.theme_color_minute_ring, default = Red)
    val DialColor = AodColorOption(key = "dial_color", labelRes = R.string.theme_color_dial, default = Grey6E)

    // Thứ đủ màu, dòng ngày tối hơn 30% như app mẫu.
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyED)

    // Dòng pin · thông báo tối hơn như app mẫu (màu điều khiển pha 30% đen).
    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = Grey9A)

    // Tên bài, nghệ sĩ, các nút.
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyE0)

    // Mỗi khối chủ đề tự vẽ (ownBlocks) có tab ở đây: Tùy chỉnh › Ngày / Pin / Thông báo / Nhạc dẫn sang màn chủ đề.
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(HourColor, MinuteColor, RingColor, DialColor),
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
            options = persistentListOf(StatusColor),
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

    // App mẫu hiện thông báo mới (icon + chữ) ở dòng pin: chủ đề tự vẽ dòng thông báo mới nhất dưới dòng pin.
    override val hasNotificationContent = true

    // App mẫu xoay hai vòng theo giờ thật cả ở màn sửa / xem trước.
    override val isLiveInPreview = true
}
