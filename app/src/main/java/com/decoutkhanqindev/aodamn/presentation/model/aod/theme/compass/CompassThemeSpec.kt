package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.theme.Amber
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansText
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyKronaOne
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE0
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

// Chủ đề La bàn (Ambitio Face của Muviz Edge, plans/muviz-edge-themes.md): ba vòng số giờ (xen giờ rưỡi), phút, giây xoay theo giờ thật về một
// đường chỉ cố định bên phải tâm; ngày, dòng pin · thông báo (kèm thông báo mới nhất như app mẫu) và trình phát dưới mặt số. Mặt số tròn tối
// giản nên chỉ có ngày (+ thời tiết), pin · thông báo, nhạc: ghi nhớ, sự kiện, nét vẽ không có chỗ đặt cân quanh mặt số (mock đã duyệt).
// Đủ tuỳ chọn của app mẫu: kiểu (có / không vòng giây), cỡ, màu 1 (số giờ, phút), màu 2 (giờ rưỡi, vạch, giây), màu điều khiển; màu tách theo
// từng vòng (user duyệt) và theo phần như các chủ đề khác.
object CompassThemeSpec : AodThemeSpec {
    // Ngày, dòng pin, thông báo, trình phát: Google Sans (app mẫu dùng Product Sans Bold / Regular); số trên các vòng: Krona One như app mẫu.
    val Font: FontFamily = FontFamilyGoogleSansText
    val NumberFont: FontFamily = FontFamilyKronaOne

    // Mặc định có vòng giây như Style 1 của app mẫu: ba vòng cùng chuyển động trông nổi nhất trên thẻ chủ đề.
    val Style = AodChoiceOption(
        key = "style",
        labelRes = R.string.theme_opt_clock_style,
        entries = AodCompassStyleValue.entries.toImmutableList(),
        default = AodCompassStyleValue.SECONDS,
    )

    // 100% bằng cỡ 75 của app mẫu (mặt số = 75% cạnh ngắn; dải 55–90 → 70–120%).
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 70..120,
        step = 10,
        default = 100,
    )

    // Số giờ và đường chỉ hổ phách cho thẻ chủ đề bắt mắt; giờ rưỡi và giây xám như màu 2 của app mẫu, phút xám sáng như màu 1.
    val HourColor = AodColorOption(key = "hour_color", labelRes = R.string.theme_color_hour, default = Amber, isGradientAllowed = true)
    val HalfHourColor = AodColorOption(key = "half_hour_color", labelRes = R.string.theme_color_half_hour, default = Grey9A, isGradientAllowed = true)
    val MinuteColor = AodColorOption(key = "minute_color", labelRes = R.string.theme_color_minute, default = GreyE0, isGradientAllowed = true)
    val SecondColor = AodColorOption(key = "second_color", labelRes = R.string.theme_color_second, default = Grey9A, isGradientAllowed = true)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyE0, isGradientAllowed = true)

    // Dòng pin · thông báo tối hơn ngày như app mẫu (màu điều khiển pha 30% đen).
    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = Grey9A, isGradientAllowed = true)

    // Tên bài, nghệ sĩ, các nút.
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyE0, isGradientAllowed = true)

    // Mỗi khối chủ đề tự vẽ (ownBlocks) có tab ở đây: Tùy chỉnh › Ngày / Pin / Thông báo / Nhạc dẫn sang màn chủ đề.
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Style, Size, HourColor, HalfHourColor, MinuteColor, SecondColor),
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

    // App mẫu hiện thông báo mới nhất (icon + chữ) dưới dòng pin: chủ đề tự vẽ dòng này.
    override val hasNotificationContent = true

    // App mẫu xoay các vòng theo giờ thật cả ở màn sửa / xem trước.
    override val isLiveInPreview = true
}
