package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tower

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Lavender
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet

// Chủ đề Tháp (Long HyperOS của Muviz Edge, plans/muviz-edge-themes.md): bốn chữ số rất cao và hẹp một hàng sát đỉnh màn hình (mặc định chỉ nét
// viền), ngày in hoa giãn chữ dưới số; trình phát riêng (lùi · tên bài giãn chữ · tới, chạm tên bài để phát / dừng) và pin + icon thông báo
// chung một dòng neo dưới. Thời tiết, sự kiện, ghi nhớ nằm trên trình phát, kiểu chỉnh ở Tùy chỉnh như Cổ điển; không có nét vẽ (khối lớn
// tranh chỗ với hàng số cao) và thông báo mới nhất (dòng trạng thái chỉ có icon). App mẫu có kiểu số, cỡ, màu giờ, phút, ngày, điều khiển;
// màu điều khiển tách thành màu nhạc và màu dòng pin như các chủ đề khác.
object TowerThemeSpec : AodThemeSpec {
    // Font cố định của ngày, trình phát và dòng trạng thái; chữ số là Big Shoulders Display Black nén ngang (TowerGlyphs).
    val Font: FontFamily = FontFamilyPlusJakartaSans

    val Style = AodChoiceOption(
        key = "digit_style",
        labelRes = R.string.theme_opt_digit_style,
        entries = AodTowerStyleValue.entries.toImmutableList(),
        // Mặc định nhạt dần xuống: hàng số cao trông nổi bật nhất trên thẻ chủ đề.
        default = AodTowerStyleValue.FADE_DOWN,
    )

    // Dải cỡ như app mẫu (80–110%).
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 80..110,
        step = 5,
        default = 100,
    )
    val HourColor = AodColorOption(key = "hour_color", labelRes = R.string.theme_color_hour, default = GreyED)
    val MinuteColor = AodColorOption(key = "minute_color", labelRes = R.string.theme_color_minute, default = Lavender)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyED)

    // Tên bài, nghệ sĩ, các nút và thanh tiến trình.
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyED)
    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = GreyED)

    // Mỗi khối chủ đề tự vẽ (ownBlocks) có tab ở đây: Tùy chỉnh › Ngày / Pin / Thông báo / Nhạc dẫn sang màn chủ đề.
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, Size, HourColor, MinuteColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_calendar_blank,
            labelRes = R.string.theme_tab_date,
            options = persistentListOf(DateColor),
            block = AodBlockValue.DATE,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_music_notes,
            labelRes = R.string.theme_tab_media,
            options = persistentListOf(MediaColor),
            block = AodBlockValue.MEDIA,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_battery_full,
            labelRes = R.string.theme_tab_status,
            options = persistentListOf(StatusColor),
            block = AodBlockValue.STATUS,
        ),
    )

    override val ownBlocks = persistentSetOf(AodBlockValue.DATE, AodBlockValue.MEDIA, AodBlockValue.STATUS)

    override val components = AodComponentValue.entries.filter { component -> component != AodComponentValue.DRAWING }.toImmutableSet()
}
