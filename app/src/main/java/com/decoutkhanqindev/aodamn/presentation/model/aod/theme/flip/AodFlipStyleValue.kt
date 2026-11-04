package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.flip

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyAnton
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyBebasNeue
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyBigShouldersDisplay
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyBodoniModa
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyRajdhani
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyRubik
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyTomorrow

@Immutable
enum class AodFlipStyleValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
    val fontFamily: FontFamily,
    val fontWeight: FontWeight,
    val textScale: Float,
    val isOutlined: Boolean = false,
) : AodThemeChoice {
    BEBAS_NEUE(
        code = 0,
        labelRes = R.string.flip_style_bebas_neue,
        fontFamily = FontFamilyBebasNeue,
        fontWeight = FontWeight.Normal,
        textScale = 1.05f,
    ),
    BIG_SHOULDERS_BOLD(
        code = 1,
        labelRes = R.string.flip_style_big_shoulders_bold,
        fontFamily = FontFamilyBigShouldersDisplay,
        fontWeight = FontWeight.Bold,
        textScale = 0.94f,
    ),
    BIG_SHOULDERS_LIGHT(
        code = 2,
        labelRes = R.string.flip_style_big_shoulders_light,
        fontFamily = FontFamilyBigShouldersDisplay,
        fontWeight = FontWeight.Light,
        textScale = 0.92f,
    ),
    DOUBLE_OUTLINE(
        code = 3,
        labelRes = R.string.flip_style_double_outline,
        fontFamily = FontFamilyBigShouldersDisplay,
        fontWeight = FontWeight.Bold,
        textScale = 0.9f,
        isOutlined = true,
    ),
    TOMORROW(
        code = 4,
        labelRes = R.string.flip_style_tomorrow,
        fontFamily = FontFamilyTomorrow,
        fontWeight = FontWeight.Medium,
        textScale = 0.64f,
    ),
    RUBIK(
        code = 5,
        labelRes = R.string.flip_style_rubik,
        fontFamily = FontFamilyRubik,
        fontWeight = FontWeight.Light,
        textScale = 0.72f,
    ),
    BODONI_MODA(
        code = 6,
        labelRes = R.string.flip_style_bodoni_moda,
        fontFamily = FontFamilyBodoniModa,
        fontWeight = FontWeight.Normal,
        textScale = 0.71f,
    ),
    ANTON(
        code = 7,
        labelRes = R.string.flip_style_anton,
        fontFamily = FontFamilyAnton,
        fontWeight = FontWeight.Normal,
        textScale = 0.8f,
    ),
    RAJDHANI(
        code = 8,
        labelRes = R.string.flip_style_rajdhani,
        fontFamily = FontFamilyRajdhani,
        fontWeight = FontWeight.Bold,
        textScale = 0.84f,
    ),
}
