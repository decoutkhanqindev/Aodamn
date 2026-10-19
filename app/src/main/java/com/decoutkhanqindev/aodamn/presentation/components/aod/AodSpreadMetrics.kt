package com.decoutkhanqindev.aodamn.presentation.components.aod

// Màn dọc giãn trên - dưới (Cổ điển, Sỏi, Viên nang, Thẻ; mock đã duyệt): cụm trên (đồng hồ, ngày, khối thêm) bắt đầu ở TOP_RATIO chiều cao,
// cụm đáy (thông báo mới nhất, nhạc, dòng trạng thái) thẳng đáy cách đáy BOTTOM_RATIO, giữa để trống. Cụm trên dài tới cụm đáy thì cụm đáy
// nằm ngay dưới, cách minGap.
object AodSpreadMetrics {
    const val TOP_RATIO = 0.12f
    const val BOTTOM_RATIO = 0.1f
    const val ROOM_RATIO = 1f - TOP_RATIO - BOTTOM_RATIO

    fun topY(height: Int): Int = (height * TOP_RATIO).toInt()

    fun bottomY(
        height: Int,
        topHeight: Int,
        bottomHeight: Int,
        minGap: Int,
    ): Int = maxOf((height * (1f - BOTTOM_RATIO)).toInt() - bottomHeight, topY(height) + topHeight + minGap)
}
