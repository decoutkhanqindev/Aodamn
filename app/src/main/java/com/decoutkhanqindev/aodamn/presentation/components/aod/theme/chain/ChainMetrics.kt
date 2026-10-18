package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.chain

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Số đo của Chuỗi lấy từ layout aug23_screen_layout + Aug23Clock dịch ngược của app mẫu (Muviz Edge, Bullet List; README › Thêm chủ đề mới bước 2),
// không từ ảnh chụp.
object ChainMetrics {
    // Đồng hồ: view rộng CLOCK_WIDTH × Cỡ, cao một nửa. Chữ Anton cỡ 0,58 chiều cao view, chữ số cao 0,875 em → chữ số cao DIGIT × chiều cao view,
    // giãn chữ TRACKING × chiều cao view (0,04 em).
    // Viên thuốc đặt sát trái view, giữa theo chiều cao: rộng = chữ + 2 × PAD_X, cao = chữ số + 2 × PAD_Y (PAD = 0,11 bề ngang view), bo tròn hết.
    val ClockWidth = 100.dp
    const val CLOCK_ASPECT = 0.5f
    const val DIGIT = 0.58f * 0.875f
    const val TRACKING = 0.04f * 0.58f
    const val PAD = 0.11f
    const val PAD_X = 1.4f * PAD
    const val DIGIT_CHANGE_MILLIS = 400

    // Màn dọc: khoảng trống TOP_RATIO chiều cao màn hình phía trên đồng hồ, lề trái / phải SideMargin, danh sách cách đồng hồ ClockGap. Trình
    // phát neo đáy: lề hai bên PlayerMargin, cách đáy PlayerBottom (đệm 40dp + 25dp của layout mẫu).
    const val TOP_RATIO = 0.13f
    val SideMargin = 30.dp
    val ClockGap = 30.dp
    val PlayerMargin = 40.dp
    val PlayerBottom = 65.dp

    // Màn ngang: cột ghi nhớ / sự kiện không rộng quá InfoMaxWidth; vạch ngăn cột cao DividerHeight, cách hai bên DividerGap.
    val InfoMaxWidth = 260.dp
    val DividerHeight = 160.dp
    val DividerGap = 32.dp
    val LandscapeMargin = 24.dp

    // Chuỗi: chấm Dot, vạch Line ở giữa chấm, chữ lùi RailWidth (chấm 6dp + 10dp). Vạch bắt đầu dưới chấm LineTop, chạy tới chân dòng. Dòng cao
    // = nội dung + RowGap (19dp + 2dp giữa hai dòng, dòng tiêu đề + dòng phụ ra đúng 60dp như layout mẫu), ít nhất RowMinHeight; dòng cuối
    // không có vạch, ít nhất LastRowMinHeight.
    val Dot = 6.dp
    val Line = 1.dp
    val RailWidth = 16.dp
    val LineTop = 5.dp
    val RowGap = 21.dp
    val RowMinHeight = 43.dp
    val LastRowMinHeight = 15.dp

    // Tiêu đề in hoa Anton TitleSize, giãn TITLE_TRACKING em; chiều cao dòng bằng cỡ chữ (chữ hoa nằm gọn trong dòng như NoPadTextView của app
    // mẫu). Chấm nằm giữa chữ hoa: TITLE_DOT_CENTER em từ đỉnh dòng (Anton ascent 1,176, descent 0,329, chữ hoa 0,859 em). Dòng phụ SubtitleSize mờ
    // SUBTITLE_ALPHA, cách tiêu đề SubtitleGap. Icon thông báo IconSize cách nhau IconGap; icon ghi nhớ / sự kiện RowIconSize cách tiêu đề
    // RowIconGap.
    val TitleSize = 18.sp
    const val TITLE_TRACKING = 0.03f
    const val TITLE_DOT_CENTER = 0.494f
    val SubtitleSize = 13.sp
    const val SUBTITLE_ALPHA = 0.7f
    val SubtitleGap = 8.dp
    val IconSize = 18.dp
    val IconGap = 10.dp
    val RowIconSize = 20.dp
    val RowIconGap = 6.dp

    // Trình phát: nút ButtonSize (icon ButtonIconSize), tên bài giãn MEDIA_TITLE_TRACKING em, nghệ sĩ MEDIA_ARTIST_TRACKING em cách ArtistGap,
    // thanh tiến trình TrackHeight cách ProgressGap, nền thanh TRACK_ALPHA; đang dừng thì chữ mờ PAUSED_ALPHA.
    val PlayerWidth = 313.dp
    val ButtonSize = 35.dp
    val ButtonIconSize = 15.dp
    const val MEDIA_TITLE_TRACKING = 0.05f
    const val MEDIA_ARTIST_TRACKING = 0.02f
    val ArtistGap = 4.dp
    val TrackHeight = 1.5.dp
    val ProgressGap = 15.dp
    const val TRACK_ALPHA = 0.3f
    const val PAUSED_ALPHA = 0.6f
}
