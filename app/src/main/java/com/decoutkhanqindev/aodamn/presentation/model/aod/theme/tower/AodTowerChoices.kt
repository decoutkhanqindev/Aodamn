package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tower

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice

@Immutable
enum class AodTowerPaintValue {
    FILL,
    OUTLINE,
    FADE_DOWN,
    FADE_UP,
}

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
