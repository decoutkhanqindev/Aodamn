package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.bezel

import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.custom_aod.presentation.theme.Cyan
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyGoogleSans
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyZalandoSansExpanded
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

// Chủ đề Vành (iOS Fill Clock của Muviz Edge, plans/muviz-edge-themes.md): mặt số kim phủ cả màn, vạch là tia từ tâm cắt trong dải khung chữ nhật
// sát mép, bốn số 12 / 3 / 6 / 9 lớn màu nhấn; thứ + số ngày phía trên kim, pin · thông báo và trình phát gọn phía dưới. Mặt đồng hồ tối giản nên
// chỉ có ngày (+ thời tiết), pin · thông báo, nhạc: ghi nhớ, sự kiện, nét vẽ, nội dung thông báo không vừa vùng trống quanh kim (mock đã duyệt).
// App mẫu có kiểu vạch, màu 1 (số, số ngày) và màu 2 (kim, vạch, thứ, dòng pin); màu tách theo phần như các chủ đề khác, ngày (thứ + số ngày)
// một màu ở tab Ngày.
object BezelThemeSpec : AodThemeSpec {
    // Ngày, dòng pin, trình phát: Google Sans Bold; bốn số: Zalando Sans Expanded SemiBold như app mẫu.
    val Font: FontFamily = FontFamilyGoogleSans
    val NumberFont: FontFamily = FontFamilyZalandoSansExpanded

    val Ticks = AodChoiceOption(
        key = "ticks",
        labelRes = R.string.theme_opt_ticks,
        entries = AodBezelTicksValue.entries.toImmutableList(),
        // Mặc định đủ vạch phút: mặt số dày vạch trông nổi nhất trên thẻ chủ đề.
        default = AodBezelTicksValue.MINUTES,
    )

    // Số 12 / 3 / 6 / 9 (màu 1 của app mẫu); vòng ở tâm kim cùng màu.
    val NumberColor = AodColorOption(key = "number_color", labelRes = R.string.theme_color_digits, default = Cyan)
    val HandColor = AodColorOption(key = "hand_color", labelRes = R.string.theme_color_hands, default = GreyED)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyED)
    val StatusColor = AodColorOption(key = "status_color", labelRes = R.string.theme_color_status, default = GreyED)

    // Tên bài, nghệ sĩ, các nút và thanh tiến trình.
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = GreyED)

    // Mỗi khối chủ đề tự vẽ (ownBlocks) có tab ở đây: Tùy chỉnh › Ngày / Pin / Thông báo / Nhạc dẫn sang màn chủ đề.
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Ticks, NumberColor, HandColor),
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
}
