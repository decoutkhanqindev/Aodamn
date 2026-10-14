package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.capsule

import androidx.compose.ui.unit.Dp

// Tỉ lệ của Viên nang đo trên ảnh app mẫu (858 × 1908, reference/theme_3, xem README › Thêm chủ đề mới): mọi kích thước theo R = chiều cao chữ
// số. R là 5,45% cạnh dài và 12,12% cạnh ngắn màn hình (lấy số nhỏ hơn), nên màn ngang viên nang to như màn dọc.
object CapsuleMetrics {
    const val LONG_SIDE_RATIO = 0.0545f
    const val SHORT_SIDE_RATIO = 0.1212f

    // Viên nang và chữ số: số Plus Jakarta Sans ExtraBold cao 0,769 cỡ chữ; mỗi số căn giữa một ô rộng DIGIT_PITCH (giờ nào viên cũng như nhau),
    // hai chấm là hai chấm tròn cách tâm COLON_OFFSET. Kiểu đặc tô màu viên ở FILL_ALPHA (ảnh mẫu: viên #191919 với màu viên #D0D0D0).
    const val CAPSULE_WIDTH = 4.76f
    const val CAPSULE_HEIGHT = 1.81f
    const val OUTLINE = 0.043f
    const val FILL_ALPHA = 0.12f
    const val DIGIT_FONT = 1.3f
    const val DIGIT_PITCH = 0.856f
    const val COLON_WIDTH = 0.434f
    const val COLON_DOT = 0.26f
    const val COLON_OFFSET = 0.22f

    // Ngày in hoa đậm cỡ DATE_FONT, cách viên DATE_TOP (khoảng 0,41 R tới đỉnh chữ hoa như ảnh mẫu, đã trừ phần trống trên chữ của dòng).
    const val DATE_FONT = 0.323f
    const val DATE_TOP = 0.37f

    // Khối thêm cách ngày BLOCKS_TOP. Dòng trạng thái cao bằng icon pin STATUS_GLYPH, cách khối bên trên STATUS_TOP (màn ngang
    // LANDSCAPE_STATUS_TOP), khoảng cách trong dòng chặt hơn Số áo (STATUS_GAP_SCALE).
    const val BLOCKS_TOP = 0.4f
    const val STATUS_GLYPH = 0.327f
    const val STATUS_TOP = 0.84f
    const val LANDSCAPE_STATUS_TOP = 0.6f
    const val STATUS_GAP_SCALE = 0.77f

    fun unit(width: Dp, height: Dp, scale: Float): Dp =
        minOf(maxOf(width, height) * LONG_SIDE_RATIO, minOf(width, height) * SHORT_SIDE_RATIO) * scale
}
