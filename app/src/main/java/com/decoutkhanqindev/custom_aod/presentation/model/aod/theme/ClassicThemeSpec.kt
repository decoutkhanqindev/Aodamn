package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import androidx.compose.ui.graphics.Color
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFaceValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

// Chủ đề Cổ điển: 4 mặt đồng hồ có từ trước khi có chủ đề (số, số xếp chồng, kim, kim tối giản). Mặc định xám dịu để ít sáng.
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
    val ClockColor = AodColorOption(key = "color", labelRes = R.string.opt_clock_color, default = GreyB4)
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_clock_size_value,
        range = 60..150,
        step = 10,
        default = 100,
    )

    // Onboarding chỉ có Cổ điển, một danh sách theo thứ tự cũ (mặt, font, màu, cỡ) thay vì chia tab như màn chủ đề.
    val OnboardingTab = AodThemeTab(
        iconRes = R.drawable.ic_clock,
        labelRes = R.string.theme_tab_clock,
        options = persistentListOf(Face, Font, ClockColor, Size),
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
            options = persistentListOf(ClockColor),
        ),
    )

    override val isDateBuiltIn = false

    override fun fontOf(config: AodThemeConfig): ClockFontValue = config[Font]

    override fun accentOf(config: AodThemeConfig): Color = config[ClockColor]
}
