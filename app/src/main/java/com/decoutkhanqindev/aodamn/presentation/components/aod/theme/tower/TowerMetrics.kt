package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tower

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Tỉ lệ của Tháp lấy từ layout + code dịch ngược của app mẫu (Muviz Edge, Long HyperOS; README › Thêm chủ đề mới bước 2), không từ ảnh chụp:
// mọi kích thước theo R = chiều cao chữ số. App mẫu đặt cỡ chữ 1,25 × cạnh ngắn × Cỡ với font có chữ số cao 0,768 em, nên R = 0,96 cạnh ngắn
// (377dp trên màn 393dp); màn ngang lấy min(0,3 cạnh dài, cạnh ngắn) thay cạnh ngắn (251dp trên 873 × 393dp).
object TowerMetrics {
    const val SHORT_SIDE_RATIO = 0.96f
    const val LANDSCAPE_LONG_SIDE_RATIO = 0.3f

    // Chữ số: Big Shoulders Display Black nén ngang CONDENSE cho số 0 rộng INK = 0,193 R như font mẫu (nét 0,056 R, font mẫu 0,083 R). Mỗi số
    // căn giữa một ô rộng PITCH (bước chữ số của font mẫu + giãn chữ 0,02 em); nét chữ của giờ và phút cách nhau PAIR_GAP (10dp ở R = 377).
    // Kiểu viền vẽ nét STROKE × R (0,5% cỡ chữ như app mẫu).
    const val CONDENSE = 0.312f
    const val PITCH = 0.215f
    const val INK = 0.193f
    const val PAIR_GAP = 0.0265f
    const val MINUTE_LEFT = 2f * PITCH + PAIR_GAP - (PITCH - INK)
    const val CLOCK_WIDTH = MINUTE_LEFT + 2f * PITCH
    const val STROKE = 0.0065f
    val MinStroke = 0.6.dp
    const val DIGIT_CHANGE_MILLIS = 400

    // Kiểu nhạt dần: màu nét ở FADE_EDGE_ALPHA tại mép nhạt, FADE_QUARTER_ALPHA ở một phần tư, đậm hẳn từ giữa số.
    const val FADE_EDGE_ALPHA = 15f / 255f
    const val FADE_QUARTER_ALPHA = 60f / 255f

    // Màn dọc: hàng số cách đỉnh màn hình TOP (thanh trạng thái + 20dp: 52 / 873), ngày cách chân số DATE_GAP (30dp), chữ ngày DATE_FONT (13sp),
    // giãn DATE_TRACKING em. Khoảng dưới ngày chia BOTTOM_WEIGHT / phần còn lại như layout mẫu: khối thêm + nhạc + dòng pin neo đáy phần
    // BOTTOM_WEIGHT, cách ngày ít nhất BLOCKS_TOP; dòng pin cao bằng icon pin STATUS_GLYPH (15dp), cách khối bên trên STATUS_TOP. Nhiều khối
    // không vừa thì hàng số nhỏ lại (tới MIN_SCALE); hàng số không rộng quá MAX_WIDTH_RATIO bề ngang màn hình.
    const val TOP_RATIO = 0.06f
    const val DATE_GAP = 0.0796f
    const val DATE_FONT = 0.0345f
    const val DATE_TRACKING = 0.48f
    const val BOTTOM_WEIGHT = 0.8f
    const val BLOCKS_TOP = 0.064f
    const val STATUS_GLYPH = 0.0398f
    const val STATUS_TOP = 0.074f
    const val MIN_SCALE = 0.6f
    const val MAX_WIDTH_RATIO = 0.92f

    // Màn ngang (theo R của màn ngang): ngày cách số 20dp, chữ 12sp, dòng pin 15dp cách ngày 26dp; cả cụm không cao quá LANDSCAPE_HEIGHT_FILL.
    const val LANDSCAPE_DATE_GAP = 0.0797f
    const val LANDSCAPE_DATE_FONT = 0.0478f
    const val LANDSCAPE_STATUS_TOP = 0.1036f
    const val LANDSCAPE_STATUS_GLYPH = 0.0598f
    const val LANDSCAPE_HEIGHT_FILL = 0.9f
    const val LANDSCAPE_COLUMN_HEIGHT = 1f + LANDSCAPE_DATE_GAP + LANDSCAPE_DATE_FONT + LANDSCAPE_STATUS_TOP + LANDSCAPE_STATUS_GLYPH

    // Phần cụm số + ngày (màn dọc) theo R, và phần cố định theo R của nhóm dưới (khoảng cách tới ngày, tới dòng pin, dòng pin).
    const val HEAD_HEIGHT = 1f + DATE_GAP + DATE_FONT
    const val BOTTOM_FIXED = BLOCKS_TOP + STATUS_TOP + STATUS_GLYPH

    fun unit(width: Dp, height: Dp, scale: Float): Dp = minOf(width, height) * SHORT_SIDE_RATIO * scale

    fun landscapeUnit(width: Dp, height: Dp, scale: Float): Dp =
        minOf(maxOf(width, height) * LANDSCAPE_LONG_SIDE_RATIO, minOf(width, height)) * SHORT_SIDE_RATIO * scale
}
