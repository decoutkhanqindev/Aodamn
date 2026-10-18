package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.bezel

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Số đo của Vành lấy từ IosFillClock + layout aug24_screen_layout của app mẫu (dịch ngược, plans/muviz-edge-themes.md). Mặt số đo theo nửa cạnh
// ngắn (fMin) và nửa cạnh dài (fMax) của màn: lề ngang luôn theo fMin, lề dọc theo fMax như app mẫu. Kim, nét, chấm tâm app mẫu đặt bằng px trên máy
// 1220px (fMin 610px), ở đây đổi thành tỉ lệ fMin để to nhỏ theo màn.
object BezelMetrics {
    // Khung ngoài của dải vạch lùi vào 0,06 fMin / 0,03 fMax; vạch phút tới khung lùi 0,2 / 0,1; vạch giờ dài tới khung lùi 0,5 / 0,22.
    const val OUTER_X = 0.06f
    const val OUTER_Y = 0.03f
    const val MINUTE_X = 0.2f
    const val MINUTE_Y = 0.1f
    const val HOUR_X = 0.5f
    const val HOUR_Y = 0.22f

    // Vạch là tia từ tâm, bắt đầu từ 0,5 fMin rồi bị cắt trong dải khung; vạch giờ nét 0,018 fMin, vạch phút 0,012 fMin mờ 70 / 255.
    const val RAY_START = 0.5f
    const val MAJOR_STROKE = 0.018f
    const val MINOR_STROKE = 0.012f
    const val MINOR_ALPHA = 70f / 255f

    // Số 12 / 3 / 6 / 9: cỡ 0,14 cạnh ngắn; cặp số trên cạnh ngắn cách tâm 0,6 fMin, cặp trên cạnh dài 0,68 fMax. Chữ số Zalando Sans Expanded cao
    // 0,744 cỡ chữ (căn bố cục theo nét chữ như app mẫu).
    const val NUMBER_SIZE = 0.14f
    const val NUMBER_NEAR = 0.6f
    const val NUMBER_FAR = 0.68f
    const val NUMBER_CAP = 0.744f

    // Kim: đoạn mảnh từ tâm tới 0,2 fMin, đoạn dày từ 0,15 fMin tới 0,53 (giờ) / 0,83 (phút), có bóng mờ. Không có kim giây: AOD chỉ cập nhật
    // mỗi phút (app mẫu cũng ẩn kim giây ở chế độ tiết kiệm pin). Bóng: BlackAlpha30.
    const val HAND_THIN = 0.0148f
    const val HAND_THICK = 0.0328f
    const val HAND_NECK = 0.2f
    const val HAND_START = 0.15f
    const val HOUR_LENGTH = 0.53f
    const val MINUTE_LENGTH = 0.83f
    const val SHADOW_RADIUS = 0.0164f
    const val HAND_MILLIS = 400

    // Chấm tâm màu kim, lỗ đen giữa, vòng màu nhấn quanh lỗ.
    const val PIN = 0.0295f
    const val PIN_HOLE = 0.0131f
    const val PIN_RING = 0.0164f
    const val PIN_RING_STROKE = 0.0131f

    // Ngày (thứ + số, cách 7dp) Google Sans Bold 20sp như layout mẫu; thời tiết cách 12dp. Dòng pin cao 18dp như icon pin / thông báo của layout mẫu.
    val DateSize = 20.sp
    val DateGap = 7.dp
    val WeatherGap = 12.dp
    val StatusGlyph = 18.dp
    val GroupGap = 10.dp

    // Màn dọc: chữ nằm giữa số 12 (cách 16dp, dấu tiếng Việt của thứ cao hơn chữ hoa) và đầu kim phút (cách 6dp), giữa đầu kim và số 6; rộng tối đa
    // 184dp để không chạm đầu vạch giờ dài. Màn ngang: nhóm trái từ 0,25 bề ngang, nhóm phải thẳng phải ở 0,75 như layout mẫu.
    val NumberGap = 16.dp
    val ReachGap = 6.dp
    val ZoneWidth = 184.dp
    const val LANDSCAPE_START = 0.25f
    const val LANDSCAPE_END = 0.75f

    // Trình phát gọn: lùi · tên in hoa 14sp / nghệ sĩ 12sp mờ 0,7 (chạm để phát / dừng, đang dừng mờ 0,6) · tới; thanh tiến trình 1,5dp.
    val ButtonSize = 32.dp
    val ButtonIconSize = 13.dp
    val TitleSize = 14.sp
    val ArtistSize = 12.sp
    val ArtistGap = 2.dp
    val ProgressGap = 8.dp
    val TrackHeight = 1.5.dp
    const val ARTIST_ALPHA = 0.7f
    const val PAUSED_ALPHA = 0.6f
    const val TRACK_ALPHA = 0.3f
}
