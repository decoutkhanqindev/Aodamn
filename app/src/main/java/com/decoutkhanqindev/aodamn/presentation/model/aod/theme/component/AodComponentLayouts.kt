package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice

// Kiểu hiển thị của từng khối dùng chung (như mặt đồng hồ): mỗi kiểu một bố cục, đều không viền, không nền; mã lưu không đổi.

@Immutable
enum class AodDateLayoutValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    SPACED(code = 0, labelRes = R.string.date_layout_spaced),
    SENTENCE(code = 1, labelRes = R.string.date_layout_sentence),
    CALENDAR(code = 2, labelRes = R.string.date_layout_calendar),
    SPLIT(code = 3, labelRes = R.string.date_layout_split),
    NUMERIC(code = 4, labelRes = R.string.date_layout_numeric),
}

@Immutable
enum class AodBatteryLayoutValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    ICON(code = 0, labelRes = R.string.battery_layout_icon),
    RING(code = 1, labelRes = R.string.battery_layout_ring),
    BAR(code = 2, labelRes = R.string.battery_layout_bar),
    DOTS(code = 3, labelRes = R.string.battery_layout_dots),
    TEXT(code = 4, labelRes = R.string.battery_layout_text),
}

@Immutable
enum class AodWeatherLayoutValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    INLINE(code = 0, labelRes = R.string.weather_layout_inline),
    TEMPERATURE(code = 1, labelRes = R.string.weather_layout_temperature),
    STACKED(code = 2, labelRes = R.string.weather_layout_stacked),
    DETAIL(code = 3, labelRes = R.string.weather_layout_detail),
}

@Immutable
enum class AodEventsLayoutValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    LIST(code = 0, labelRes = R.string.events_layout_list),
    TIMELINE(code = 1, labelRes = R.string.events_layout_timeline),
    NEXT(code = 2, labelRes = R.string.events_layout_next),
    INLINE(code = 3, labelRes = R.string.events_layout_inline),
}

@Immutable
enum class AodMemoLayoutValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    ICON(code = 0, labelRes = R.string.memo_layout_icon),
    QUOTE(code = 1, labelRes = R.string.memo_layout_quote),
    PLAIN(code = 2, labelRes = R.string.memo_layout_plain),
    TAG(code = 3, labelRes = R.string.memo_layout_tag),
}

@Immutable
enum class AodNotificationsLayoutValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    ICONS(code = 0, labelRes = R.string.notifications_layout_icons),
    STACK(code = 1, labelRes = R.string.notifications_layout_stack),
    BADGE(code = 2, labelRes = R.string.notifications_layout_badge),
    DOTS(code = 3, labelRes = R.string.notifications_layout_dots),
}

@Immutable
enum class AodMediaLayoutValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    ROW(code = 0, labelRes = R.string.media_layout_row),
    CENTER(code = 1, labelRes = R.string.media_layout_center),
    COMPACT(code = 2, labelRes = R.string.media_layout_compact),
}
