package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bigoverlap

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// Số đo của Chồng số lấy từ BigPixel2Clock + layout mar23_screen_layout dọc / ngang của app mẫu (dịch ngược, plans/muviz-edge-themes.md).
object BigOverlapMetrics {
    // Cỡ chữ số = 0,5 × cạnh ngắn vùng đồng hồ × cỡ; nét 0,035 × cạnh ngắn × cỡ = 0,07 cỡ chữ, kiểu viền mảnh hơn (chia 1 + 1,5 × độ viền).
    const val TEXT_SIZE = 0.5f
    const val STROKE = 0.07f
    const val OUTLINE_THINNING = 1.5f

    // Hai cột lấn qua tâm theo bề rộng số 0, giờ / phút lấn qua tâm theo chiều cao số 0; kiểu viền lấn nhiều hơn. Đổi kiểu chạy 500ms.
    const val OVERLAP_X_FILLED = 0.17f
    const val OVERLAP_X_OUTLINE = 0.2f
    const val OVERLAP_Y_FILLED = 0.04f
    const val OVERLAP_Y_OUTLINE = 0.06f
    const val STYLE_MILLIS = 500

    // Bề rộng nét số 0 / cỡ chữ (Google Sans Flex wght 1000 wdth 108): màn ngang tính chỗ hai bên khối số theo cỡ chữ.
    const val ZERO_WIDTH = 0.679f

    // Màn dọc như layout mẫu: vùng trên cao tối thiểu 130dp, khối thông tin dồn đáy, lề 40dp; đồng hồ giữa phần còn lại theo trọng số
    // 0,08 / 0,8 / 0,12; trình phát lề 40dp; dòng pin đệm 40dp quanh.
    val TopMinHeight = 130.dp
    val SideMargin = 40.dp
    const val TOP_SPACE_WEIGHT = 0.08f
    const val CLOCK_WEIGHT = 0.8f
    const val BOTTOM_SPACE_WEIGHT = 0.12f
    val BatteryPadding = 40.dp

    // Ngày 20sp (màn ngang 18sp); thời tiết cuối dòng ngày (màn ngang ngay sau ngày, cách 14dp). Hàng icon 18dp cách nhau 10dp, cách ngày 18dp;
    // thông báo mới nhất: icon 18dp, cách 7dp một dòng 16sp (tiêu đề, nội dung tối hơn 30%), cách hàng icon 10dp.
    val DateSize = 20.sp
    val LandscapeDateSize = 18.sp
    val WeatherGap = 14.dp
    val DetailsGap = 18.dp
    val IconRow = 18.dp
    const val ICON_GAP_SCALE = 0.492f
    val LatestIcon = 18.dp
    val LatestIconGap = 7.dp
    val LatestSize = 16.sp
    val LatestGap = 10.dp
    const val TEXT_DARKEN = 0.3f

    // Dòng pin chữ 14sp (màn ngang 13sp; icon pin cao bằng chữ / 0,915), mờ 0,8 khi không sạc.
    val BatteryGlyph = 15.3.dp
    val LandscapeBatteryGlyph = 14.2.dp
    const val IDLE_BATTERY_ALPHA = 0.8f

    // Màn ngang (mock đã duyệt): khối số giữa màn cỡ như màn dọc, hai bên cách khối 36dp, lề 40dp; trình phát cách dòng pin 24dp.
    val LandscapeGap = 36.dp
    val LandscapeItemGap = 24.dp

    // Trình phát như media_widget_lt: tên 16sp / nghệ sĩ 14sp (cách 4dp, tối hơn 30%) giãn 0,02em; nút phát 40dp (icon 20dp, cách 10dp hai bên),
    // tới 35dp (icon 15dp); thanh tiến trình 1,5dp cách 15dp, nền thanh cùng màu mờ 0,3.
    val TitleSize = 16.sp
    val ArtistSize = 14.sp
    val ArtistGap = 4.dp
    val TrackSpacing = 0.02.em
    val PlayButton = 40.dp
    val PlayIcon = 20.dp
    val PlayGap = 10.dp
    val NextButton = 35.dp
    val NextIcon = 15.dp
    val ProgressGap = 15.dp
    val TrackHeight = 1.5.dp
    const val TRACK_ALPHA = 0.3f
}
