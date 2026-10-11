package com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyBigShouldersDisplay
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyComfortaa
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyDancingScript
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyFraunces
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyJetBrainsMono
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyJosefinSans
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyPlusJakartaSans

// Font đóng gói sẵn trong app nên AOD không cần mạng lúc hiện. Mã 0–3 giữ từ bản dùng font hệ thống để máy cũ không mất lựa chọn.
@Immutable
enum class ClockFontValue(
    val code: Int,
    @param:StringRes val labelRes: Int,
    val fontFamily: FontFamily,
) {
    DEFAULT(code = 0, labelRes = R.string.clock_font_default, fontFamily = FontFamilyPlusJakartaSans),
    SERIF(code = 1, labelRes = R.string.clock_font_serif, fontFamily = FontFamilyFraunces),
    MONOSPACE(code = 2, labelRes = R.string.clock_font_monospace, fontFamily = FontFamilyJetBrainsMono),
    CURSIVE(code = 3, labelRes = R.string.clock_font_cursive, fontFamily = FontFamilyDancingScript),
    ROUNDED(code = 4, labelRes = R.string.clock_font_rounded, fontFamily = FontFamilyComfortaa),
    CONDENSED(code = 5, labelRes = R.string.clock_font_condensed, fontFamily = FontFamilyBigShouldersDisplay),
    GEOMETRIC(code = 6, labelRes = R.string.clock_font_geometric, fontFamily = FontFamilyJosefinSans);

    companion object {
        fun fromCode(code: Int?): ClockFontValue = entries.find { it.code == code } ?: DEFAULT
    }
}
