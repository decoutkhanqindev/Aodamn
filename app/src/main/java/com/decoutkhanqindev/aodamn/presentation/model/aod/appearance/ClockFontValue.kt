package com.decoutkhanqindev.aodamn.presentation.model.aod.appearance

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyBigShouldersDisplay
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyComfortaa
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyDancingScript
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyFraunces
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyJetBrainsMono
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyJosefinSans
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyPlusJakartaSans

// Font đóng gói sẵn trong app nên AOD không cần mạng lúc hiện. Mã 0–3 giữ từ bản dùng font hệ thống để máy cũ không mất lựa chọn.
@Immutable
enum class ClockFontValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
    val fontFamily: FontFamily,
) : AodThemeChoice {
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
