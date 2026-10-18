package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.bezel

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeChoice

// Hai kiểu vạch của Vành theo thứ tự app mẫu (iOS Fill Clock, Clock Style 1–2): đủ 60 vạch phút (vạch giờ đậm, vạch phút mờ) hoặc chỉ 12 vạch giờ.
@Immutable
enum class AodBezelTicksValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    MINUTES(code = 0, labelRes = R.string.bezel_ticks_minutes),
    HOURS(code = 1, labelRes = R.string.bezel_ticks_hours),
}
