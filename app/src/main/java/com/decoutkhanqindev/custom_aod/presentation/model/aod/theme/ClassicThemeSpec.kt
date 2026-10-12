package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFaceValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFontValue
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

    // Onboarding chỉ có Cổ điển, một danh sách (mặt rồi font, cỡ, màu như các khối khác của Tùy chỉnh) thay vì chia tab như màn chủ đề.
    val OnboardingTab = AodThemeTab(
        iconRes = R.drawable.ic_clock,
        labelRes = R.string.theme_tab_clock,
        options = persistentListOf(Face, Font, Size, HourColor, MinuteColor),
    )

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_clock,
            options = persistentListOf(Face, Size),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_text_aa,
            labelRes = R.string.theme_tab_font,
            options = persistentListOf(Font),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_palette,
            labelRes = R.string.theme_tab_color,
            options = persistentListOf(HourColor, MinuteColor),
        ),
    )

    override val ownBlocks = persistentSetOf<AodBlockValue>()
}
