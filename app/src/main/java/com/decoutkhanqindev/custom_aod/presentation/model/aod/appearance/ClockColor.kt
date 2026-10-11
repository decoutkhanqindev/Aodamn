package com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.decoutkhanqindev.custom_aod.presentation.theme.Blue
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.Orange
import com.decoutkhanqindev.custom_aod.presentation.theme.Pink
import com.decoutkhanqindev.custom_aod.presentation.theme.Purple
import com.decoutkhanqindev.custom_aod.presentation.theme.Red

// Màu đồng hồ lưu dạng ARGB đục. Bản trước color picker lưu mã 0–7 của bảng màu bên dưới; màu thật alpha 0xFF nên là số âm, không trùng
// mã cũ, nhờ vậy máy đã cài bản cũ vẫn giữ màu đang chọn. Mặc định xám dịu để ít sáng, ít burn-in.
private val LegacyClockColors = listOf(GreyB4, GreyED, Mint, Blue, Purple, Orange, Pink, Red)

fun clockColorOf(stored: Int?): Color = when (stored) {
    null -> GreyB4
    in LegacyClockColors.indices -> LegacyClockColors[stored]
    else -> Color(stored)
}

fun Color.toStoredClockColor(): Int = toArgb() or (0xFF shl 24)
