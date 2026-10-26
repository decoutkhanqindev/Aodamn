package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tick

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

// Số đo của Nhịp giây lấy từ CoumTick + layout oct23_screen_layout của app mẫu (dịch ngược, plans/muviz-edge-themes.md). u là nửa cạnh ngắn của
// màn (app mẫu: nửa bề ngang màn dọc), big là nửa cạnh dài; vòng, chấm giây, giờ, ngày tính theo u.
object TickMetrics {
    // 60 vạch từ 0,8u tới 0,9u, nét 0,007u, trôi theo chiều kim 6°/giây; gradient trải từ tâm − 0,7u tới tâm + 0,7u, đứng yên theo màn.
    const val TICK_COUNT = 60
    const val DEGREES_PER_SECOND = 6f
    const val MINUTE_MILLIS = 60_000L
    const val TICK_INNER = 0.8f
    const val TICK_OUTER = 0.9f
    const val STROKE = 0.007f
    const val GRADIENT_REACH = 0.7f

    // Hai vùng tròn bán kính u ở tâm ± 0,7 big làm vòng chìm: mất hẳn trong 0,65u, rõ hẳn từ u.
    const val FADE_OFFSET = 0.7f
    const val FADE_SOLID = 0.65f

    // Chấm giây ở tâm + 0,84u bên phải, bán kính 0,12u, số giây 0,115u căn giữa theo nét chữ.
    const val SECOND_OFFSET = 0.84f
    const val SECOND_RADIUS = 0.12f
    const val SECOND_SIZE = 0.115f

    // Giờ 72sp × cỡ, ngày 23sp × cỡ trên màn 393dp (u = 196,5dp); cỡ là Size / 100.
    const val CLOCK_SIZE = 72f / 196.5f
    const val DATE_SIZE = 23f / 196.5f
    val ClockSpacing = 0.02.em
    val DateSpacing = 0.12.em

    // Thời tiết treo dưới ngày, không đẩy cụm giờ + ngày.
    val WeatherGap = 8.dp

    // Màn dọc như layout mẫu: khối dưới cách đáy 60dp + đệm 10dp (đệm ngang 30dp); hàng thông báo ở trên đối xứng. Trình phát cách dòng pin 16dp.
    val EdgeMargin = 70.dp
    val SideMargin = 30.dp
    val PlayerGap = 16.dp
    val PlayerMargin = 40.dp

    // Màn ngang (mock đã duyệt): vòng giữa màn cỡ như màn dọc; hai vùng bên đối xứng, mép trong cách chấm giây 15dp, mép ngoài cách màn 24dp.
    const val SECOND_REACH = SECOND_OFFSET + SECOND_RADIUS
    val LandscapeGap = 15.dp
    val LandscapeMargin = 24.dp

    // Hàng icon thông báo 17dp; thông báo mới nhất cách 12dp: icon 17dp, cách 10dp chữ in hoa 13sp giãn 0,1em chạy chữ một lần. Dòng pin: icon
    // 15dp, chữ 14sp.
    val IconRow = 17.dp
    val LatestGap = 12.dp
    val LatestIcon = 17.dp
    val LatestIconGap = 10.dp
    val LatestSize = 13.sp
    val LatestSpacing = 0.1.em
    val StatusGlyph = 15.dp

    // Trình phát như media_widget_lt: nút lùi / tới 40dp (icon 12dp), cách chữ 10dp; tên Bold 13sp, cách 8dp nghệ sĩ 11,5sp tối hơn 30%, in hoa giãn
    // 0,25em; chạm tên để phát / dừng, đang dừng thì chữ mờ 0,6.
    val ButtonSize = 40.dp
    val ButtonIconSize = 12.dp
    val TextGap = 10.dp
    val TitleSize = 13.sp
    val ArtistSize = 11.5.sp
    val TrackSpacing = 0.25.em
    val ArtistGap = 8.dp
    const val TEXT_DARKEN = 0.3f
    const val PAUSED_ALPHA = 0.6f
}
