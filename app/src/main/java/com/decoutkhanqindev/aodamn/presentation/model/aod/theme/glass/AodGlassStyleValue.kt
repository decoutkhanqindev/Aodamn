package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.glass

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyAllertaStencil
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyFrauncesDisplay
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSansFlexRound
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyInterDisplay

@Immutable
enum class AodGlassStyleValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
    val fontFamily: FontFamily,
    val fontWeight: FontWeight,
    val isOutline: Boolean,
) : AodThemeChoice {
    REGULAR(
        code = 0,
        labelRes = R.string.glass_style_regular,
        fontFamily = FontFamilyInterDisplay,
        fontWeight = FontWeight.Medium,
        isOutline = false,
    ),
    ROUNDED(
        code = 1,
        labelRes = R.string.glass_style_rounded,
        fontFamily = FontFamilyGoogleSansFlexRound,
        fontWeight = FontWeight.Bold,
        isOutline = false,
    ),
    STENCIL(
        code = 2,
        labelRes = R.string.glass_style_stencil,
        fontFamily = FontFamilyAllertaStencil,
        fontWeight = FontWeight.Normal,
        isOutline = false,
    ),
    SERIF(
        code = 3,
        labelRes = R.string.glass_style_serif,
        fontFamily = FontFamilyFrauncesDisplay,
        fontWeight = FontWeight.Black,
        isOutline = false,
    ),
    RAILS(
        code = 4,
        labelRes = R.string.glass_style_rails,
        fontFamily = FontFamilyInterDisplay,
        fontWeight = FontWeight.Light,
        isOutline = true,
    ),
}
