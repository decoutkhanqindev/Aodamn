package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.compass

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeChoice

// Hai kiểu của La bàn theo thứ tự app mẫu (Ambitio Face, Clock Style 1–2): có vòng giây chạy theo giờ thật (đường chỉ từ tâm) hoặc không vòng giây
// (đường chỉ lùi ra 0,28u, mặt số đổi theo từng phút).
@Immutable
enum class AodCompassStyleValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    SECONDS(code = 0, labelRes = R.string.compass_style_seconds),
    MINUTES(code = 1, labelRes = R.string.compass_style_minutes),
}
