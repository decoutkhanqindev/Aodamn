package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeChoice

@Immutable
enum class AodTileStyleValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    FILLED(code = 0, labelRes = R.string.tile_style_filled),
    OUTLINE(code = 1, labelRes = R.string.tile_style_outline),
}

@Immutable
enum class AodDateStyleValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    BOLD(code = 0, labelRes = R.string.date_style_bold),
    PLAIN(code = 1, labelRes = R.string.date_style_plain),
}

@Immutable
enum class AodTransitionValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    NONE(code = 0, labelRes = R.string.transition_none),
    SLIDE(code = 1, labelRes = R.string.transition_slide),
    ZOOM(code = 2, labelRes = R.string.transition_zoom),
    PULSE(code = 3, labelRes = R.string.transition_pulse),
}

@Immutable
enum class AodProgressStyleValue(
    override val code: Int,
    @param:StringRes override val labelRes: Int,
) : AodThemeChoice {
    WAVE(code = 0, labelRes = R.string.progress_style_wave),
    LINE(code = 1, labelRes = R.string.progress_style_line),
    HIDDEN(code = 2, labelRes = R.string.progress_style_hidden),
}
