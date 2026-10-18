package com.decoutkhanqindev.aodamn.presentation.model.aod.appearance

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.decoutkhanqindev.aodamn.presentation.theme.Blue
import com.decoutkhanqindev.aodamn.presentation.theme.GreyB4
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.Orange
import com.decoutkhanqindev.aodamn.presentation.theme.Pink
import com.decoutkhanqindev.aodamn.presentation.theme.Purple
import com.decoutkhanqindev.aodamn.presentation.theme.Red

// Màu đồng hồ lưu dạng ARGB đục. Bản trước color picker lưu mã 0–7 của bảng màu bên dưới; màu thật alpha 0xFF nên là số âm, không trùng
// mã cũ, nhờ vậy máy đã cài bản cũ vẫn giữ màu đang chọn. Mặc định xám dịu để ít sáng, ít burn-in.
private val LegacyClockColors = listOf(GreyB4, GreyED, Mint, Blue, Purple, Orange, Pink, Red)

fun clockColorOf(stored: Int?): Color = when (stored) {
    null -> GreyB4
    in LegacyClockColors.indices -> LegacyClockColors[stored]
    else -> Color(stored)
}

fun Color.toStoredClockColor(): Int = toArgb() or (0xFF shl 24)
