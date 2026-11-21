package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyTiny5
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyTiny5Square

@Immutable
enum class AodMatrixDotValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
    val fontFamily: FontFamily,
    val cap: StrokeCap,
    val dotRatio: Float,
) : AodThemeChoice {
    ROUND(
        code = 0,
        labelRes = R.string.matrix_dot_round,
        fontFamily = FontFamilyTiny5,
        cap = StrokeCap.Round,
        dotRatio = .94f,
    ),
    SQUARE(
        code = 1,
        labelRes = R.string.matrix_dot_square,
        fontFamily = FontFamilyTiny5Square,
        cap = StrokeCap.Square,
        dotRatio = .9f,
    ),
}
