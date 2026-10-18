package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// Số đo của La bàn lấy từ AmbitioClock + layout may23_screen_layout của app mẫu (dịch ngược, plans/muviz-edge-themes.md). Mặt số vuông cạnh =
// cạnh ngắn × 0,75 × cỡ; u là nửa cạnh, mọi bán kính và cỡ chữ trên vòng theo u.
object CompassMetrics {
    const val DIAL_FRACTION = 0.75f

    // Vòng giờ: số 1–12 cỡ 0,165u ở 0,84u, xen "hh:30" cỡ 0,066u (0,165 / 2,5); lúc 0 giờ số 1 ở góc 120°.
    const val HOUR_RADIUS = 0.84f
    const val HOUR_SIZE = 0.165f
    const val HALF_HOUR_SIZE = 0.066f
    const val HOUR_START = 120f

    // Vòng phút: 00–55 cỡ 0,11u ở 0,55u, xen vạch từ 0,475u tới 0,625u (màu phút mờ 0,6, như màu 2 / màu 1 của app mẫu).
    const val MINUTE_RADIUS = 0.55f
    const val MINUTE_SIZE = 0.11f
    const val TICK_START = 0.475f
    const val TICK_END = 0.625f
    const val TICK_ALPHA = 0.6f

    // Vòng giây: 00–55 cỡ 0,09u ở 0,3u.
    const val SECOND_RADIUS = 0.3f
    const val SECOND_SIZE = 0.09f

    // Hai vòng tròn mảnh màu phút alpha 40 / 255 ôm vòng phút.
    const val CIRCLE_OUTER = 0.68f
    const val CIRCLE_INNER = 0.42f
    const val CIRCLE_ALPHA = 40f / 255f

    // Đường chỉ tới mép phải mặt số, đầu mũi tên dài 0,05u, xoè ±0,03u; kiểu có vòng giây bắt đầu từ tâm, kiểu không vòng giây lùi ra 0,28u.
    // Đổi kiểu: vòng giây mờ dần, đường chỉ lùi trong 300ms (ValueAnimator mặc định của app mẫu); kiểu không vòng giây sang phút mới thì vòng xoay
    // mượt 400ms (app mẫu nhảy).
    const val POINTER_HEAD = 0.05f
    const val POINTER_SPREAD = 0.03f
    const val POINTER_GAP = 0.28f
    const val STYLE_MILLIS = 300
    const val STEP_MILLIS = 400
    val LineWidth = 1.3.dp
    val CircleWidth = 1.dp

    // Khối dưới mặt số cách đáy 60dp: ngày in hoa Bold 14sp giãn 0,25em, cách 15dp dòng pin (icon pin 14dp, chữ 13sp), thông báo mới nhất (icon
    // 16dp, cách 10dp, chữ in hoa 12,5sp giãn 0,1em); thời tiết cách ngày 12dp; trình phát cách 24dp.
    val BottomMargin = 60.dp
    val DateSize = 14.sp
    val DateSpacing = 0.25.em
    val DateGap = 15.dp
    val WeatherGap = 12.dp
    val StatusGlyph = 14.dp
    val LatestGap = 12.dp
    val LatestIcon = 16.dp
    val LatestIconGap = 10.dp
    val LatestSize = 12.5.sp
    val LatestSpacing = 0.1.em
    val MediaGap = 24.dp
    val SideMargin = 30.dp

    // Trình phát như layout mẫu: nút lùi / tới 40dp (icon 12dp) cách mép 40dp ở màn dọc, tên in hoa Bold 13sp giãn 0,25em, cách 8dp nghệ sĩ
    // Regular 11,5sp mờ 0,7; không có nút phát, chạm tên để phát / dừng (dừng thì chữ mờ 0,6). Màn ngang rộng bằng vùng bên trừ 24dp.
    val PlayerMargin = 40.dp
    val PlayerInset = 24.dp
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
