package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.jersey

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Orange
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

// Chủ đề Số áo: giờ và phút hai hàng chữ số rất cao chỉ có nét viền như số áo thể thao, ngày xếp dọc bên phải hàng giờ, pin và icon
// thông báo chung một dòng dưới đồng hồ. Ít tuỳ chọn: cỡ và màu từng phần (giờ, phút, ngày, dòng pin + thông báo). Các khối dùng chung khác (thời tiết, sự kiện, ghi nhớ, nét vẽ,
// nhạc) nằm dưới dòng trạng thái, kiểu / font / cỡ / màu chỉnh ở Tùy chỉnh như Cổ điển; thông báo mới nhất không có (dòng trạng thái chỉ có icon).
object JerseyThemeSpec : AodThemeSpec {
    // Font cố định của chủ đề cho ngày và dòng trạng thái; chữ số là Big Shoulders Display Black (JerseyGlyphs).
    val Font: FontFamily = FontFamilyPlusJakartaSans

    // 100% to như app mẫu (mỗi hàng số ~35% chiều cao màn hình), không to hơn để còn chỗ cho dòng trạng thái.
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 50..100,
        step = 10,
        default = 100,
    )
    // Mặc định giờ và ngày cam như áo đấu, phút trắng: thẻ chủ đề bắt mắt.
    val HourColor = AodColorOption(key = "hour_color", labelRes = R.string.theme_color_hour, default = Orange)
    val MinuteColor = AodColorOption(key = "minute_color", labelRes = R.string.theme_color_minute, default = GreyED)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = Orange)
    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = GreyED)

    // Mỗi khối chủ đề tự vẽ (ownBlocks) có tab ở đây: Tùy chỉnh › Ngày / Pin / Thông báo dẫn sang màn chủ đề.
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Size, HourColor, MinuteColor),
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
    )

    override val ownBlocks = persistentSetOf(AodBlockValue.DATE, AodBlockValue.STATUS)
}
