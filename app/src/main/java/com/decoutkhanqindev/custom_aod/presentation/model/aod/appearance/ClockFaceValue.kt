package com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeChoice

@Immutable
enum class ClockFaceValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    DIGITAL(code = 0, labelRes = R.string.clock_face_digital),
    STACKED(code = 1, labelRes = R.string.clock_face_stacked),
    ANALOG(code = 2, labelRes = R.string.clock_face_analog),
    ANALOG_MINIMAL(code = 3, labelRes = R.string.clock_face_analog_minimal);

    companion object {
        fun fromCode(code: Int?): ClockFaceValue = entries.find { it.code == code } ?: DIGITAL
    }
}
