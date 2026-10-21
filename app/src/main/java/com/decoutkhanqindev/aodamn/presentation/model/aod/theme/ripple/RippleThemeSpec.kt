package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.ripple

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansMedium
import com.decoutkhanqindev.aodamn.presentation.theme.Green
import com.decoutkhanqindev.aodamn.presentation.theme.Grey7C
import com.decoutkhanqindev.aodamn.presentation.theme.GreyAD
import com.decoutkhanqindev.aodamn.presentation.theme.GreyB9
import com.decoutkhanqindev.aodamn.presentation.theme.GreyCE
import com.decoutkhanqindev.aodamn.presentation.theme.Ink
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

// Chủ đề Gợn sóng (Android 12 của Muviz Edge, plans/muviz-edge-themes.md): đĩa 12 gợn giữa màn, ngày uốn theo nửa trên đĩa, kim giờ / phút dày
// đầu tròn trôi liên tục, chấm giây chạy vòng theo giờ thật; hàng icon + thông báo mới nhất ở trên, thời tiết dưới đĩa, thẻ nhạc có ảnh bìa và dòng
// pin ở dưới. Ghi nhớ, sự kiện, nét vẽ không có chỗ đặt cân quanh đĩa (mock đã duyệt). App mẫu có màu chủ đạo cho thông báo / nhạc / pin; bên mình
// mỗi phần một màu, mặc định đúng như app mẫu tính ra (user duyệt). Bỏ trạng thái tiết kiệm của app mẫu (đĩa viền, ẩn chấm giây) để chấm giây luôn
// chạy (user chọn).
object RippleThemeSpec : AodThemeSpec {
    // App mẫu dùng Rubik (Medium: ngày, tên bài; Regular: còn lại); Rubik thiếu tiếng Việt nên dùng Google Sans cùng hai độ đậm.
    val Font: FontFamily = FontFamilyGoogleSansMedium

    // Clock Style 1 / 2 của app mẫu: đĩa đặc / viền, thẻ nhạc và nút phát đổi theo.
    val Style = AodChoiceOption(
        key = "style",
        labelRes = R.string.theme_opt_clock_style,
        entries = AodTileStyleValue.entries.toImmutableList(),
        default = AodTileStyleValue.FILLED,
    )

    // Cỡ như app mẫu (60–110, mặc định 80): kim theo cỡ, đĩa 0,9 cỡ, tính trên nửa cạnh ngắn của vùng mặt số.
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 60..110,
        step = 5,
        default = 80,
    )

    // Kim giờ xanh lá cho thẻ chủ đề bắt mắt (app mẫu trắng); còn lại như app mẫu.
    val HourColor = AodColorOption(key = "hour_color", labelRes = R.string.theme_color_hour, default = Green, isGradientAllowed = true)
    val MinuteColor = AodColorOption(key = "minute_color", labelRes = R.string.theme_color_minute, default = GreyCE, isGradientAllowed = true)
    val SecondColor = AodColorOption(key = "second_color", labelRes = R.string.theme_color_second, default = GreyAD, isGradientAllowed = true)
    val FaceColor = AodColorOption(key = "face_color", labelRes = R.string.theme_color_face, default = Ink, isGradientAllowed = true)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyCE, isGradientAllowed = true)
    val NotificationsColor = AodColorOption(
        key = "notifications_color",
        labelRes = R.string.theme_color_notifications,
        default = GreyB9,
        isGradientAllowed = true,
    )
    val BatteryColor = AodColorOption(key = "battery_color", labelRes = R.string.theme_color_battery, default = Grey7C, isGradientAllowed = true)

    // Tên bài, nghệ sĩ (tối hơn 40% như app mẫu), nút phát; nền thẻ nhạc và nút phát.
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyB9, isGradientAllowed = true)
    val MediaBackgroundColor = AodColorOption(
        key = "media_background_color",
        labelRes = R.string.theme_color_media_background,
        default = Ink,
        isGradientAllowed = true,
    )

    // Mỗi khối chủ đề tự vẽ (ownBlocks) có tab ở đây: Tùy chỉnh › Ngày / Pin / Thông báo / Nhạc dẫn sang màn chủ đề.
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, Size, HourColor, MinuteColor, SecondColor, FaceColor),
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
            options = persistentListOf(MediaColor, MediaBackgroundColor),
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

    // App mẫu hiện thông báo mới (icon trong vòng tròn màu + tiêu đề + nội dung) ở trên: chủ đề tự vẽ thông báo mới nhất dưới hàng icon.
    override val hasNotificationContent = true

    // Chấm giây chạy vòng theo giờ thật như app mẫu, cả ở màn sửa / xem trước.
    override val isLiveInPreview = true
}
