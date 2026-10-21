package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Số đo của Gợn sóng lấy từ Android12Clock2 / Android12ClockBg2 + layout aug21_screen_layout của app mẫu (dịch ngược,
// plans/muviz-edge-themes.md). half là nửa cạnh ngắn của vùng mặt số; cỡ (Size) nhân vào kim và đĩa, không nhân vào chữ ngày như app mẫu.
object RippleMetrics {
    // Đĩa bán kính 0,9 × cỡ × half, viền 12 gợn: 24 đoạn 15°, điểm điều khiển xen kẽ ngoài / trong 6%. Kiểu viền nét 2dp.
    const val FACE_RADIUS = 0.9f
    const val WAVE_STEP_DEGREES = 15
    const val WAVE_DEPTH = 0.06f
    val OutlineWidth = 2.dp

    // Kim tính trên cỡ × half: phút dài 0,5, giờ 0,3, dày 0,1, đầu tròn; chấm giây bán kính 0,05 chạy vòng ở 0,71.
    const val MINUTE_LENGTH = 0.5f
    const val HOUR_LENGTH = 0.3f
    const val HAND_WIDTH = 0.1f
    const val SECOND_ORBIT = 0.71f
    const val SECOND_RADIUS = 0.05f

    // Ngày "E  dd" Medium cỡ 0,085 half, căn giữa trên cung nửa trên bán kính 0,75 đĩa (chữ nằm ngoài cung).
    const val DATE_ARC = 0.75f
    const val DATE_SIZE = 0.085f

    // Màn dọc như layout mẫu: vùng thông báo trên cùng cao 200dp (đệm 70dp trên / dưới), vùng dưới: trình phát 65dp cách trên 10dp, dưới 30dp, lề
    // 40dp hai bên; dòng pin cách đáy 30dp. Vùng mặt số là phần còn lại (giữ chỗ cho trình phát nên mặt số không nhảy khi nhạc bật / tắt).
    val TopZone = 200.dp
    val PlayerTop = 10.dp
    val PlayerHeight = 65.dp
    val PlayerBottom = 30.dp
    val PlayerMargin = 40.dp
    val BottomMargin = 30.dp

    // Màn ngang (mock đã duyệt): mặt số giữa màn tính trên cạnh ngắn của màn; mỗi bên là vùng còn lại trừ 24dp, các khối cách nhau 22dp.
    val LandscapeGap = 24.dp
    val LandscapeItemGap = 22.dp

    // Hàng icon thông báo 18dp; thông báo mới nhất: icon 18dp trong vòng tròn 28dp tô màu thông báo, cách 12dp tiêu đề 16sp, nội dung 14sp (tối
    // hơn 40%, tối đa 2 dòng, cách 5dp); hàng icon và thông báo cách nhau 12dp.
    val IconRow = 18.dp
    val LatestBadge = 28.dp
    val LatestIcon = 18.dp
    val LatestGap = 12.dp
    val LatestTitleSize = 16.sp
    val LatestTextSize = 14.sp
    val LatestTextGap = 5.dp
    const val TEXT_DARKEN = 0.4f

    // Dòng pin chữ 14sp (icon pin 16dp), mờ 0,8 khi không sạc như bottom_tv.
    val StatusGlyph = 16.dp
    const val IDLE_BATTERY_ALPHA = 0.8f

    // Thẻ nhạc như media_details_lt: cao 65dp bo tròn hết, đệm 5dp, ảnh bìa tròn 55dp, cách 12dp tên 15sp Medium / nghệ sĩ 14sp tối hơn 40%
    // (cách 2dp); nút phát tròn 60dp cách thẻ 15dp, icon 16dp. Vuốt ngang thẻ quá 48dp để chuyển bài.
    val CardPadding = 5.dp
    val AlbumArt = 55.dp
    val TextGap = 12.dp
    val TitleSize = 15.sp
    val ArtistSize = 14.sp
    val ArtistGap = 2.dp
    val PlayButton = 60.dp
    val PlayButtonGap = 15.dp
    val PlayIcon = 16.dp
    val FallbackIcon = 24.dp
    val SwipeThreshold = 48.dp
}
