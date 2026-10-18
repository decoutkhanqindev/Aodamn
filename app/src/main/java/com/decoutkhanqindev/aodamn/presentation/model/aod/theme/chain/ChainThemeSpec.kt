package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.chain

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.Coral
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyAnton
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE0
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet

// Chủ đề Chuỗi (Bullet List của Muviz Edge, plans/muviz-edge-themes.md): giờ khoét rỗng trong viên thuốc tô màu, dưới là một chuỗi dòng có
// chấm nối nhau bằng vạch mảnh: ngày (thời tiết cạnh ngày), pin, thông báo (icon hoặc thông báo mới nhất), ghi nhớ, sự kiện; trình phát riêng neo
// đáy. Ghi nhớ và sự kiện do chủ đề tự vẽ (icon đặc + tiêu đề in hoa như các dòng khác) nên kiểu ở màn chủ đề; không có nét vẽ (hình lớn không
// hợp dạng dòng). App mẫu có cỡ đồng hồ, màu chủ đạo và màu từng phần (đồng hồ, ngày, pin, thông báo, điều khiển).
object ChainThemeSpec : AodThemeSpec {
    // Chữ số và tiêu đề in hoa: Anton như app mẫu. Dòng phụ: app mẫu dùng Rubik nhưng Rubik không có tiếng Việt (thiếu Ạ, Ứ...), nên dùng Plus
    // Jakarta Sans.
    val TitleFont: FontFamily = FontFamilyAnton
    val Font: FontFamily = FontFamilyPlusJakartaSans

    // 100% bằng cỡ 50 của app mẫu (dải 35–65 → 70–130%).
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 70..130,
        step = 10,
        default = 100,
    )
    // Viên thuốc và trình phát mặc định san hô cho thẻ chủ đề bắt mắt, các dòng trong chuỗi giữ xám sáng.
    val ClockColor = AodColorOption(key = "clock_color", labelRes = R.string.theme_color_clock, default = Coral)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyE0)
    val BatteryColor = AodColorOption(key = "battery_color", labelRes = R.string.theme_color_battery, default = GreyE0)
    val NotificationsColor = AodColorOption(key = "notifications_color", labelRes = R.string.theme_color_notifications, default = GreyE0)
    val MemoColor = AodColorOption(key = "memo_color", labelRes = R.string.theme_color_memo, default = GreyE0)
    val EventsColor = AodColorOption(key = "events_color", labelRes = R.string.theme_color_events, default = GreyE0)

    // Tên bài, nghệ sĩ, các nút và thanh tiến trình.
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = Coral)

    // Mỗi khối chủ đề tự vẽ (ownBlocks) có tab ở đây, theo thứ tự trong chuỗi: Tùy chỉnh › Ngày / Pin / Thông báo / Ghi nhớ / Sự kiện / Nhạc dẫn
    // sang màn chủ đề.
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Size, ClockColor),
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
            iconRes = R.drawable.ic_note_pencil,
            labelRes = R.string.customize_tab_memo,
            options = persistentListOf(MemoColor),
            block = AodBlockValue.MEMO,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_calendar_check,
            labelRes = R.string.customize_tab_events,
            options = persistentListOf(EventsColor),
            block = AodBlockValue.EVENTS,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_music_notes,
            labelRes = R.string.theme_tab_media,
            options = persistentListOf(MediaColor),
            block = AodBlockValue.MEDIA,
        ),
    )

    override val ownBlocks = persistentSetOf(
        AodBlockValue.DATE,
        AodBlockValue.STATUS,
        AodBlockValue.MEMO,
        AodBlockValue.EVENTS,
        AodBlockValue.MEDIA,
    )

    override val components = AodComponentValue.entries.filter { component -> component != AodComponentValue.DRAWING }.toImmutableSet()

    // Dòng thông báo của chuỗi hiện thông báo mới nhất (icon + tiêu đề + nội dung) như app mẫu khi bật nội dung thông báo.
    override val hasNotificationContent = true
}
