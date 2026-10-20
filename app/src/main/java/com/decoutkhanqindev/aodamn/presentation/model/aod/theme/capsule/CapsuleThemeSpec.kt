package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.capsule

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Lime
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

// Chủ đề Viên nang: giờ:phút chữ rất đậm nằm trong một viên nang (đặc hoặc viền), ngày in hoa đậm ngay dưới, trình phát nhạc riêng (nút phát
// tròn đặc, thanh tiến trình dày tách đôi), pin + icon thông báo chung một dòng dưới cùng. Các khối dùng chung khác (thời tiết, sự kiện, ghi
// nhớ, nét vẽ) nằm giữa ngày và nhạc, kiểu / font / cỡ / màu chỉnh ở Tùy chỉnh như Cổ điển; thông báo mới nhất không có (dòng trạng thái chỉ có
// icon). App mẫu có kiểu viên, hai màu, cỡ; thêm màu ngày, màu nhạc như Sỏi và màu dòng pin + thông báo.
object CapsuleThemeSpec : AodThemeSpec {
    // Font cố định của chủ đề: chữ số ExtraBold, ngày Bold, trình phát và dòng trạng thái.
    val Font: FontFamily = FontFamilyPlusJakartaSans

    // Đặc: viên tô màu viên pha nhạt (xám tối như ảnh mẫu); viền: nét màu viên. Mặc định viền xanh chanh như thẻ chủ đề trong lưới của app mẫu.
    val Style = AodChoiceOption(
        key = "capsule_style",
        labelRes = R.string.theme_opt_tile_style,
        entries = AodTileStyleValue.entries.toImmutableList(),
        default = AodTileStyleValue.OUTLINE,
    )
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 60..150,
        step = 10,
        default = 100,
    )
    val DigitColor = AodColorOption(key = "digit_color", labelRes = R.string.theme_color_digits, default = Lime, isGradientAllowed = true)
    val CapsuleColor = AodColorOption(key = "capsule_color", labelRes = R.string.theme_color_capsule, default = Lime, isGradientAllowed = true)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyED, isGradientAllowed = true)

    // Tên bài, nút phát và phần đã nghe của thanh tiến trình; nghệ sĩ, nút lùi / tới giữ xám.
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = Lime, isGradientAllowed = true)
    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = GreyED, isGradientAllowed = true)

    // Mỗi khối chủ đề tự vẽ (ownBlocks) có tab ở đây: Tùy chỉnh › Ngày / Pin / Thông báo / Nhạc dẫn sang màn chủ đề.
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, Size, DigitColor, CapsuleColor),
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
}
