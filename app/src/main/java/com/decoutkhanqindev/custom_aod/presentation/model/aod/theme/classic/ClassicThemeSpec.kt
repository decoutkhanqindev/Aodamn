package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.classic

import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFaceValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

// Chủ đề Cổ điển: 4 mặt đồng hồ có từ trước khi có chủ đề (số, số xếp chồng, kim, kim tối giản), giờ và phút có màu riêng. Mặc định xám dịu
// để ít sáng.
object ClassicThemeSpec : AodThemeSpec {
    val Face = AodChoiceOption(
        key = "face",
        labelRes = R.string.opt_clock_face,
        entries = ClockFaceValue.entries.toImmutableList(),
        default = ClockFaceValue.DIGITAL,
    )
    val Font = AodChoiceOption(
        key = "font",
        labelRes = R.string.opt_clock_font,
        entries = ClockFontValue.entries.toImmutableList(),
        default = ClockFontValue.DEFAULT,
    )

    // Giờ giữ khoá "color" của bản chỉ có một màu đồng hồ nên cấu hình đã lưu không mất; phút có màu riêng.
    val HourColor = AodColorOption(key = "color", labelRes = R.string.theme_color_hour, default = GreyB4)
    val MinuteColor = AodColorOption(key = "minute_color", labelRes = R.string.theme_color_minute, default = GreyB4)
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 60..150,
        step = 10,
        default = 100,
    )

    // Một tab như khối ở Tùy chỉnh (mặt rồi font, cỡ, màu giờ / phút), dùng ở màn chủ đề và tab Đồng hồ lúc onboarding.
    val ClockTab = AodThemeTab(
        iconRes = R.drawable.ic_clock,
        labelRes = R.string.theme_tab_clock,
        options = persistentListOf(Face, Font, Size, HourColor, MinuteColor),
    )

    override val tabs = persistentListOf(ClockTab)

    override val ownBlocks = persistentSetOf<AodBlockValue>()
}
