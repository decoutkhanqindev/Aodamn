package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.card

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.custom_aod.presentation.theme.Blue
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyE4
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

// Chủ đề Thẻ (M3 Expressive của Muviz Edge, plans/muviz-edge-themes.md): số giờ rất cao và hẹp, phút nhỏ bằng nửa đặt trên ô AM/PM, cả cụm trong
// một thẻ bo góc; ngày in hoa đậm dưới thẻ, trình phát riêng (nút phát tròn đặc, thanh gợn sóng), pin + icon thông báo chung một dòng dưới
// cùng. Các khối dùng chung khác (thời tiết, sự kiện, ghi nhớ, nét vẽ) nằm giữa ngày và nhạc, kiểu chỉnh ở Tùy chỉnh như Cổ điển; thông báo
// mới nhất không có (dòng trạng thái chỉ có icon). App mẫu có kiểu số, nền thẻ, hai màu, cỡ; thêm màu thẻ riêng (app mẫu lấy màu giờ), màu ngày,
// màu nhạc, màu dòng pin như Viên nang.
object CardThemeSpec : AodThemeSpec {
    // Font cố định của ngày, trình phát và dòng trạng thái; chữ số và AM/PM là Big Shoulders Display Black nén ngang (CardGlyphs).
    val Font: FontFamily = FontFamilyPlusJakartaSans

    val Style = AodChoiceOption(
        key = "digit_style",
        labelRes = R.string.theme_opt_digit_style,
        entries = AodTileStyleValue.entries.toImmutableList(),
        default = AodTileStyleValue.FILLED,
    )
    val Background = AodChoiceOption(
        key = "card_background",
        labelRes = R.string.theme_opt_card_background,
        entries = AodCardBackgroundValue.entries.toImmutableList(),
        default = AodCardBackgroundValue.TINTED,
    )

    // Dải cỡ như app mẫu (80–120%).
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 80..120,
        step = 5,
        default = 100,
    )

    // Màu phút còn tô ô AM/PM như app mẫu. Màu thẻ là nền mờ hay nét viền tuỳ kiểu nền, mặc định trùng màu giờ như app mẫu; giờ và thẻ mặc
    // định xanh dương cho thẻ chủ đề bắt mắt.
    val HourColor = AodColorOption(key = "hour_color", labelRes = R.string.theme_color_hour, default = Blue)
    val MinuteColor = AodColorOption(key = "minute_color", labelRes = R.string.theme_color_minute_tag, default = GreyE4)
    val CardColor = AodColorOption(key = "card_color", labelRes = R.string.theme_color_card, default = Blue)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyE4)

    // Tên bài, nghệ sĩ, các nút và phần đã nghe của thanh tiến trình.
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyE4)
    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = GreyE4)

    // Mỗi khối chủ đề tự vẽ (ownBlocks) có tab ở đây: Tùy chỉnh › Ngày / Pin / Thông báo / Nhạc dẫn sang màn chủ đề.
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, Background, Size, HourColor, MinuteColor, CardColor),
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
