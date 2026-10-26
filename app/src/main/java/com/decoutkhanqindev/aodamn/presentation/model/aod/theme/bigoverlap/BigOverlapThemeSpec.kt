package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansFlex
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansFlexRounded
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansText
import com.decoutkhanqindev.aodamn.presentation.theme.GreyCE
import com.decoutkhanqindev.aodamn.presentation.theme.Peach
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

// Chủ đề Chồng số (Big Overlap Clock của Muviz Edge, plans/muviz-edge-themes.md): bốn chữ số lớn xếp lưới 2 × 2 chồng mép lên nhau giữa màn, số
// sau che số trước; ngày + icon thông báo + thông báo mới nhất căn trái ở trên, trình phát và dòng pin ở dưới. Ghi nhớ, sự kiện, nét vẽ không có chỗ
// đặt cân quanh khối số (mock đã duyệt). App mẫu có màu chủ đạo cho ngày / thông báo / nhạc / pin; bên mình mỗi phần một màu (user duyệt). Bỏ trạng
// thái tiết kiệm của app mẫu (tự sang kiểu viền) vì AOD của mình không có trạng thái đó (user duyệt).
object BigOverlapThemeSpec : AodThemeSpec {
    // Chữ ngày, thông báo, pin, nhạc: Product Sans Regular của app mẫu = Google Sans Regular.
    val Font: FontFamily = FontFamilyGoogleSansText

    // Chữ số Google Sans Flex như app mẫu: góc vuông / bo tròn là hai họ (hai file tĩnh ghim trục ROND), độ đậm 1000 như file.
    val DigitFont: FontFamily = FontFamilyGoogleSansFlex
    val RoundedDigitFont: FontFamily = FontFamilyGoogleSansFlexRounded
    val DigitWeight = FontWeight(1000)

    // Mặc định đặc bo tròn (app mẫu đặc góc vuông) như thẻ chủ đề trong lưới của app mẫu, bắt mắt nhất.
    val Style = AodChoiceOption(
        key = "style",
        labelRes = R.string.theme_opt_clock_style,
        entries = AodBigOverlapStyleValue.entries.toImmutableList(),
        default = AodBigOverlapStyleValue.FILLED_ROUND,
    )

    // Cỡ như app mẫu (50–100, mặc định 85), tính trên cạnh ngắn của vùng đồng hồ.
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 50..100,
        step = 5,
        default = 85,
    )

    // Color 1–4 của app mẫu (giờ chục, giờ đơn vị, phút chục, phút đơn vị), mặc định chéo như app mẫu: Color 3 theo Color 2, Color 4 theo Color 1.
    // Màu nhấn đào (màu gốc của BigPixel2Clock) thay xám xanh #CFD8DC, màu kia GreyCE gần #D7CCC8 / #CFD8DC của app mẫu.
    val HourFirstColor = AodColorOption(
        key = "hour_first_color",
        labelRes = R.string.theme_color_hour_digit_1,
        default = Peach,
        isGradientAllowed = true,
    )
    val HourSecondColor = AodColorOption(
        key = "hour_second_color",
        labelRes = R.string.theme_color_hour_digit_2,
        default = GreyCE,
        isGradientAllowed = true,
    )
    val MinuteFirstColor = AodColorOption(
        key = "minute_first_color",
        labelRes = R.string.theme_color_minute_digit_1,
        default = GreyCE,
        isGradientAllowed = true,
    )
    val MinuteSecondColor = AodColorOption(
        key = "minute_second_color",
        labelRes = R.string.theme_color_minute_digit_2,
        default = Peach,
        isGradientAllowed = true,
    )

    // Các phần còn lại mặc định GreyCE, gần màu chủ đạo #CFD8DC của app mẫu.
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyCE, isGradientAllowed = true)
    val NotificationsColor = AodColorOption(
        key = "notifications_color",
        labelRes = R.string.theme_color_notifications,
        default = GreyCE,
        isGradientAllowed = true,
    )
    val BatteryColor = AodColorOption(key = "battery_color", labelRes = R.string.theme_color_battery, default = GreyCE, isGradientAllowed = true)

    // Tên bài, nghệ sĩ (tối hơn 30% như app mẫu), nút phát / tới, thanh tiến trình.
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyCE, isGradientAllowed = true)

    // Mỗi khối chủ đề tự vẽ (ownBlocks) có tab ở đây: Tùy chỉnh › Ngày / Pin / Thông báo / Nhạc dẫn sang màn chủ đề.
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, Size, HourFirstColor, HourSecondColor, MinuteFirstColor, MinuteSecondColor),
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

    // App mẫu hiện thông báo mới (icon + một dòng chữ) ngay dưới ngày: chủ đề tự vẽ thông báo mới nhất dưới hàng icon.
    override val hasNotificationContent = true
}
