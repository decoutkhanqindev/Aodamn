package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Số đo của Dải màu lấy từ Sep23Clock + layout sep23_screen_layout của app mẫu (dịch ngược, plans/muviz-edge-themes.md). th là chiều cao nét chữ
// của giờ (getTextBounds); mọi khoảng trên vạch theo th.
object GradientLineMetrics {
    // Giờ cỡ 0,11 cạnh ngắn của vùng vạch × cỡ (Size, %), ngày cỡ 0,043, mờ 200 / 255; vạch dày 0,008 cạnh ngắn (2,5dp ở màn dọc), đầu tròn.
    const val TIME_SIZE = 0.11f
    const val DATE_SIZE = 0.043f
    const val DATE_ALPHA = 200f / 255f
    const val LINE_WIDTH = 0.008f

    // Tâm giờ chạy từ th tới hết vạch trừ 2th (chừa ngày); vạch khoét từ 1,1th trước tâm giờ tới 2th sau (không có ngày: 1,1th); ngày cách tâm giờ
    // 1,2th. Dòng ngày + thời tiết cao hơn ngày của app mẫu thì lùi xuống, cách nét giờ ít nhất 0,35th, chỗ khoét / chừa dài theo, hết dòng
    // thêm 0,45th (dòng chỉ có ngày thì đúng 2th như app mẫu).
    const val TRAVEL_START = 1f
    const val TRAVEL_END = 2f
    const val CUT_BEFORE = 1.1f
    const val CUT_AFTER = 1.1f
    const val CUT_AFTER_DATE = 2f
    const val DATE_OFFSET = 1.2f
    const val ROW_GAP = 0.35f
    const val DATE_MARGIN = 0.45f
    const val MINUTES_PER_DAY = 24f * 60f

    // Màn ngang (mock đã duyệt): vạch nằm ngang hết bề rộng, cao hơn giữa màn 0,35th để giờ + ngày cân giữa; vạch khoét quanh giờ, cách nét
    // chữ 0,6th mỗi bên; ngày dưới vạch nên không khoét.
    const val LANDSCAPE_LINE_SHIFT = 0.35f
    const val LANDSCAPE_CUT = 0.6f

    // Layout mẫu: lề 40dp hai bên, 60dp trên / dưới; trình phát cách vạch 30dp; vạch cách khối dưới 15dp, khối dưới đệm 15dp. Màn ngang: trình
    // phát cách đỉnh 30dp, rộng 360dp; khối dưới cách đáy 32dp.
    val SideMargin = 40.dp
    val VerticalMargin = 60.dp
    val PlayerGap = 30.dp
    val ClockGap = 15.dp
    val DetailsPadding = 15.dp
    val LandscapeTopMargin = 30.dp
    val LandscapeBottomMargin = 32.dp
    val LandscapePlayerWidth = 360.dp

    // Dòng pin chữ 15sp mờ 0,9 như battery_status (icon pin cao 16dp); thông báo mới nhất: icon 20dp, cách 10dp, chữ 16sp, cách dòng pin 10dp.
    val StatusGlyph = 16.dp
    const val STATUS_ALPHA = 0.9f
    val LatestIcon = 20.dp
    val LatestIconGap = 10.dp
    val LatestSize = 16.sp
    val LatestGap = 10.dp

    // Trình phát như media_widget_lt: nút lùi / tới 35dp (icon 9dp) mờ 0,7, tên 16sp, cách 4dp nghệ sĩ 13sp mờ 0,7; chạm tên để phát / dừng,
    // đang dừng thì chữ mờ 0,6.
    val ButtonSize = 35.dp
    val ButtonIconSize = 9.dp
    const val BUTTON_ALPHA = 0.7f
    val TitleSize = 16.sp
    val ArtistSize = 13.sp
    val ArtistGap = 4.dp
    const val ARTIST_ALPHA = 0.7f
    const val PAUSED_ALPHA = 0.6f
}
