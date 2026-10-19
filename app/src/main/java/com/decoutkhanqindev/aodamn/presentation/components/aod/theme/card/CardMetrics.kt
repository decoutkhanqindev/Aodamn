package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.card

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Tỉ lệ của Thẻ lấy từ layout + code dịch ngược của app mẫu (Muviz Edge, M3 Expressive; README › Thêm chủ đề mới bước 2), không từ ảnh chụp:
// mọi kích thước theo R = chiều cao số giờ. App mẫu đặt số giờ 190sp với font có chữ số cao đúng 1 em, nên R = 190dp trên màn 393dp (48,35%
// cạnh ngắn); màn ngang thu còn 0,9 như app mẫu.
object CardMetrics {
    const val SHORT_SIDE_RATIO = 0.4835f
    const val LANDSCAPE_SCALE = 0.9f

    // Chữ số: Big Shoulders Display Black nén ngang CONDENSE cho số 0 rộng 0,281 R, nét 0,095 R (font mẫu 0,286 / 0,105). Mỗi số căn giữa một
    // ô rộng PITCH (bước chữ số của font mẫu), nét chữ rộng INK. Kiểu viền vẽ nét OUTLINE × chiều cao số (1% cỡ chữ như app mẫu).
    const val CONDENSE = 0.55f
    const val PITCH = 0.358f
    const val INK = 0.284f
    const val OUTLINE = 0.01f
    val MinStroke = 0.6.dp
    const val DIGIT_CHANGE_MILLIS = 400

    // Thẻ đệm PADDING quanh nét chữ số, bo CORNER; nền mờ là màu giờ ở CARD_FILL_ALPHA, viền là nét CARD_STROKE ở CARD_STROKE_ALPHA.
    const val PADDING = 0.15f
    const val CORNER = 0.21f
    const val CARD_FILL_ALPHA = 0.118f
    const val CARD_STROKE = 0.013f
    const val CARD_STROKE_ALPHA = 0.235f

    // Cột phải cách số giờ COLUMN_GAP, rộng TAG_WIDTH: phút cao MINUTE (ngang đỉnh số giờ), ô AM/PM từ TAG_TOP tới chân số giờ, bo TAG_CORNER,
    // chữ cao TAG_TEXT; ô tô màu phút ở TAG_FILL_ALPHA, kiểu viền là nét TAG_STROKE ở TAG_STROKE_ALPHA.
    const val COLUMN_GAP = 0.073f
    const val TAG_WIDTH = 0.33f
    const val MINUTE = 0.5f
    const val TAG_TOP = 0.58f
    const val TAG_CORNER = 0.079f
    const val TAG_TEXT = 0.207f
    const val TAG_FILL_ALPHA = 0.196f
    const val TAG_STROKE = 0.0105f
    const val TAG_STROKE_ALPHA = 0.31f

    const val HOUR_LEFT = PADDING - (PITCH - INK) / 2f
    const val COLUMN_LEFT = PADDING + PITCH + INK + COLUMN_GAP
    const val MINUTE_LEFT = COLUMN_LEFT + (TAG_WIDTH - (PITCH + INK) * MINUTE) / 2f - (PITCH - INK) / 2f * MINUTE
    const val CARD_WIDTH = COLUMN_LEFT + TAG_WIDTH + PADDING
    const val CARD_HEIGHT = 1f + 2f * PADDING

    // Ngày in hoa đậm chữ hoa cao 0,059 R (cỡ DATE_FONT với Plus Jakarta Sans cao chữ hoa 0,745 em), đỉnh chữ hoa cách thẻ 0,118 R (DATE_TOP đã
    // trừ phần trống trên chữ của dòng).
    const val DATE_FONT = 0.0792f
    const val DATE_TOP = 0.108f

    // Khối thêm cách ngày BLOCKS_TOP; dòng trạng thái cao bằng icon pin STATUS_GLYPH (16dp ở R = 190), cách khối bên trên STATUS_TOP (40dp như
    // app mẫu; màn ngang LANDSCAPE_STATUS_TOP). Màn dọc nhiều khối thì thẻ nhỏ lại (tới MIN_SCALE) cho hai cụm vừa AodSpreadMetrics.ROOM_RATIO
    // chiều cao; màn ngang cả cụm không cao quá HEIGHT_FILL.
    const val BLOCKS_TOP = 0.08f
    const val STATUS_GLYPH = 0.082f
    const val STATUS_TOP = 0.21f
    const val LANDSCAPE_STATUS_TOP = 0.15f
    const val STATUS_GAP_SCALE = 0.77f
    const val HEIGHT_FILL = 0.92f
    const val MIN_SCALE = 0.55f

    // Chiều cao cụm thẻ + ngày + dòng trạng thái theo R (không kể khối thêm).
    const val COLUMN_HEIGHT = CARD_HEIGHT + DATE_TOP + DATE_FONT + BLOCKS_TOP + STATUS_TOP + STATUS_GLYPH
    const val LANDSCAPE_COLUMN_HEIGHT = CARD_HEIGHT + DATE_TOP + DATE_FONT + LANDSCAPE_STATUS_TOP + STATUS_GLYPH

    fun unit(width: Dp, height: Dp, scale: Float): Dp = minOf(width, height) * SHORT_SIDE_RATIO * scale
}
