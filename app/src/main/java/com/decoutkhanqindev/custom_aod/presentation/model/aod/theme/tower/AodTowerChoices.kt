package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.tower

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeChoice

// Cách tô một cặp chữ số của Tháp: đặc, nét viền, hay nét viền nhạt dần về chân / đỉnh số (app mẫu: 15 / 255 ở mép, 60 / 255 ở một phần tư,
// đậm hẳn từ giữa).
@Immutable
enum class AodTowerPaintValue {
    FILL,
    OUTLINE,
    FADE_DOWN,
    FADE_UP,
}

// Sáu kiểu số của Tháp theo thứ tự app mẫu (Long HyperOS, Clock Style 1–6); mỗi kiểu là cách tô cặp giờ và cặp phút.
@Immutable
enum class AodTowerStyleValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
    val hourPaint: AodTowerPaintValue,
    val minutePaint: AodTowerPaintValue,
) : AodThemeChoice {
    OUTLINE(
        code = 0,
        labelRes = R.string.tile_style_outline,
        hourPaint = AodTowerPaintValue.OUTLINE,
        minutePaint = AodTowerPaintValue.OUTLINE,
    ),
    FADE_DOWN(
        code = 1,
        labelRes = R.string.tower_style_fade_down,
        hourPaint = AodTowerPaintValue.FADE_DOWN,
        minutePaint = AodTowerPaintValue.FADE_DOWN,
    ),
    FADE_UP(
        code = 2,
        labelRes = R.string.tower_style_fade_up,
        hourPaint = AodTowerPaintValue.FADE_UP,
        minutePaint = AodTowerPaintValue.FADE_UP,
    ),
    FILLED(
        code = 3,
        labelRes = R.string.tile_style_filled,
        hourPaint = AodTowerPaintValue.FILL,
        minutePaint = AodTowerPaintValue.FILL,
    ),
    HOUR_FILLED(
        code = 4,
        labelRes = R.string.tower_style_hour_filled,
        hourPaint = AodTowerPaintValue.FILL,
        minutePaint = AodTowerPaintValue.OUTLINE,
    ),
    MINUTE_FILLED(
        code = 5,
        labelRes = R.string.tower_style_minute_filled,
        hourPaint = AodTowerPaintValue.OUTLINE,
        minutePaint = AodTowerPaintValue.FILL,
    ),
}
