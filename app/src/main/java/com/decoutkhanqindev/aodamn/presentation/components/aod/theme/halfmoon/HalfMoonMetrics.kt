package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.halfmoon

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// Số đo của Bán nguyệt lấy từ HalfConcentricClock + layout dec22_screen_layout dọc / ngang của app mẫu (dịch ngược, plans/muviz-edge-themes.md).
// r là bán kính mặt số; mọi bán kính, cỡ chữ trên mặt số theo r.
object HalfMoonMetrics {
    // Màn dọc: mặt số vuông cạnh 0,9 cạnh ngắn như app mẫu. Màn ngang: r = 0,65 cạnh ngắn (mock đã duyệt; app mẫu 0,45 chỉ chiếm 1/5 bề ngang),
    // đỉnh / đáy hai vòng ra ngoài màn, chìm trong phủ tối.
    const val PORTRAIT_RADIUS = 0.45f
    const val LANDSCAPE_RADIUS = 0.65f

    // Tâm lùi ra ngoài mép trái 0,95r: chỉ thấy nửa phải, tới 1,05r.
    const val CENTER_SHIFT = 0.95f

    // Vòng giây (ngoài): 60 vạch tới r, vạch mỗi 5 giây từ 0,93r, còn lại từ 0,96r; số 00–55 cỡ 0,08r ở 0,86r.
    const val SECOND_TICK_MAJOR = 0.93f
    const val SECOND_TICK_MINOR = 0.96f
    const val SECOND_TICK_END = 1f
    const val SECOND_LABEL_RADIUS = 0.86f
    const val SECOND_LABEL_SIZE = 0.08f

    // Vòng phút (trong): vạch từ 0,68r / 0,72r tới 0,76r; số cỡ 0,09r ở 0,58r.
    const val MINUTE_TICK_MAJOR = 0.68f
    const val MINUTE_TICK_MINOR = 0.72f
    const val MINUTE_TICK_END = 0.76f
    const val MINUTE_LABEL_RADIUS = 0.58f
    const val MINUTE_LABEL_SIZE = 0.09f

    // Giờ cỡ 0,36r ở tâm + 0,18r; phút cỡ 0,155r trong viên ở tâm + 0,6r. Vạch / số hai vòng khoét quanh viên (vuông 2,4 × cao chữ số phút,
    // bo 0,6 cao chữ); viền viên cao 2,4 × cao chữ, từ số phút (lùi 1,3 × cao chữ) kéo tới mép mặt số như lúc thức của app mẫu, bo tròn hết.
    const val HOUR_SIZE = 0.36f
    const val HOUR_OFFSET = 0.18f
    const val MINUTE_SIZE = 0.155f
    const val PILL_OFFSET = 0.6f
    const val PILL_HALF_HEIGHT = 1.2f
    const val PILL_CLIP_CORNER = 0.5f
    const val PILL_HALF_WIDTH = 1.3f

    // Nét theo px như app mẫu (vạch 2 px, viền viên 3 px).
    const val TICK_WIDTH_PX = 2f
    const val RING_WIDTH_PX = 3f

    // Đĩa đen 50% dưới mặt số (thấy khi có ảnh nền); phủ tối tròn tâm cách tâm mặt số 1,75r về bên phải, bán kính 2r, tối dần từ 0,85.
    const val DISC_ALPHA = 0.5f
    const val VIGNETTE_SHIFT = 1.75f
    const val VIGNETTE_RADIUS = 2f
    const val VIGNETTE_START = 0.85f

    // Cột bên phải: ngày "dd MMM yyyy" (tối hơn 30%) rồi thứ "EEEE", in hoa Bold giãn 0,25em, 12 / 13sp màn dọc, 14 / 15sp màn ngang, cách
    // 8dp; thời tiết một dòng riêng cách 8dp; dòng pin (icon pin 15dp, chữ 14sp) cách 12dp; thông báo mới nhất (icon 17dp, cách 10dp, chữ in hoa
    // 13sp giãn 0,1em) cách 12dp.
    const val DATE_DARKEN = 0.3f
    val PortraitDateSize = 12.sp
    val PortraitDaySize = 13.sp
    val LandscapeDateSize = 14.sp
    val LandscapeDaySize = 15.sp
    val DateSpacing = 0.25.em
    val DayGap = 8.dp
    val WeatherGap = 8.dp
    val StatusGap = 12.dp
    val StatusGlyph = 15.dp
    val LatestGap = 12.dp
    val LatestIcon = 17.dp
    val LatestIconGap = 10.dp
    val LatestSize = 13.sp
    val LatestSpacing = 0.1.em
    val ColumnInset = 16.dp
    val LandscapeColumnWidth = 420.dp

    // Trình phát như layout mẫu: nút lùi / tới 40dp (icon 12dp) cách mép 40dp ở màn dọc, tên in hoa Bold 13sp giãn 0,25em, cách 8dp nghệ sĩ
    // Regular 11,5sp mờ 0,7; chạm tên để phát / dừng (dừng thì chữ mờ 0,6). Màn ngang rộng 360dp, cách cột 28dp.
    val PlayerMargin = 40.dp
    val LandscapePlayerWidth = 360.dp
    val LandscapePlayerGap = 28.dp
    val ButtonSize = 40.dp
    val ButtonIconSize = 12.dp
    val TextGap = 10.dp
    val TitleSize = 13.sp
    val ArtistSize = 11.5.sp
    val TrackSpacing = 0.25.em
    val ArtistGap = 8.dp
    const val ARTIST_ALPHA = 0.7f
    const val PAUSED_ALPHA = 0.6f
}
