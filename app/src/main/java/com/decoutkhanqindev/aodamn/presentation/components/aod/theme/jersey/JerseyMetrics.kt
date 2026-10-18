package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.jersey

import androidx.compose.ui.unit.dp

// Tỉ lệ của Số áo đo trên ảnh app mẫu (858 × 1908, xem README › Thêm chủ đề mới): mọi kích thước theo R = chiều cao một hàng chữ số tính cả
// nét viền; ở màn dọc R là 35,06% chiều cao màn hình. Cỡ chữ ngày và dòng trạng thái cũng theo R (không dùng sp / dp cố định) nên đổi cỡ
// màn hình hay tuỳ chọn Cỡ thì mọi thứ co giãn cùng nhau đúng như ảnh mẫu.
object JerseyMetrics {
    // Màn dọc
    const val ROW_HEIGHT_RATIO = 0.3506f
    const val MAX_WIDTH_RATIO = 0.85f

    // Chữ số: mỗi số một ô rộng cố định (sát nhau, số 1 hẹp nằm giữa ô nên hàng "11" thoáng hơn hàng "22" như ảnh mẫu); hai hàng cách nhau ROW_GAP.
    const val PITCH = 0.4006f
    const val ROW_GAP = 0.062f
    const val CONDENSE = 0.685f
    const val STROKE = 0.0082f

    // Số 1 của ảnh mẫu có cờ vuông (font Big Shoulders có cờ vát nên tự vẽ): hình chữ nhật trừ góc dưới trái.
    const val ONE_WIDTH = 0.2078f
    const val ONE_FLAG_WIDTH = 0.0359f
    const val ONE_FLAG_HEIGHT = 0.172f

    // Ngày (màn dọc) treo bên phải khối số, không ảnh hưởng vị trí số: cột rộng bằng viên thuốc, ba dòng "tháng / ngày", dưới là viên thuốc chứa thứ xoay dọc. Cỡ chữ là cỡ font (cao số = 0,745 cỡ chữ).
    const val DATE_GAP = 0.0665f
    const val DATE_WIDTH = 0.0837f
    const val DATE_FONT = 0.0481f
    const val DATE_LINE = 0.0613f
    const val PILL_GAP = 0.0628f
    const val PILL_BORDER = 0.006f
    const val PILL_PAD_ALONG = 0.033f
    const val WEEKDAY_TRACKING = 0.056f

    // Dòng trạng thái (AodStatusLine): cách hàng phút STATUS_TOP, cao bằng icon pin, cỡ theo R màn dọc; phần còn lại của dòng theo chiều cao icon pin.
    const val STATUS_TOP = 0.161f
    const val STATUS_GLYPH_HEIGHT = 0.046f

    // Có khối thêm (thời tiết, sự kiện, nhạc…) dưới dòng trạng thái: hàng số nhỏ lại cho cả cụm vừa PORTRAIT_HEIGHT_FILL chiều cao màn hình,
    // không nhỏ hơn MIN_ROW_RATIO cỡ gốc (ngày treo bên phải vẫn vừa); ngày và dòng trạng thái giữ cỡ gốc.
    const val PORTRAIT_HEIGHT_FILL = 0.9f
    const val MIN_ROW_RATIO = 0.5f

    // Màn ngang: một cột giữa màn hình, ngày nằm ngang (như màn dọc nhưng số đứng thẳng) → bốn chữ số một hàng, giờ và phút cách nhau
    // LANDSCAPE_PAIR_GAP → dòng trạng thái → khối thêm. Ngày và dòng trạng thái cỡ như màn dọc (R theo cạnh dài).
    const val LANDSCAPE_ROW_HEIGHT_RATIO = 0.62f
    const val LANDSCAPE_MAX_WIDTH_RATIO = 0.45f
    const val LANDSCAPE_HEIGHT_FILL = 0.9f
    const val LANDSCAPE_MIN_ROW_RATIO = 0.3f
    const val LANDSCAPE_PAIR_GAP = 0.3f
    const val LANDSCAPE_STATUS_TOP = 0.072f

    const val DIGIT_CHANGE_MILLIS = 500

    val MinStroke = 1.5.dp
}
