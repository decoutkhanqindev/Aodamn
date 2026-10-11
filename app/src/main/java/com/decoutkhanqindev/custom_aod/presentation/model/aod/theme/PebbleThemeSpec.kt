package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.custom_aod.presentation.theme.Black
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import com.decoutkhanqindev.custom_aod.presentation.theme.Purple
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

// Chủ đề Sỏi: giờ và phút mỗi số một khối hình mềm (8 hình, đặc hoặc viền), dòng ngày ngay dưới, số đổi có animation; trình phát
// nhạc riêng (nút phát trong khối hình, thanh tiến trình gợn sóng). Khối, chữ số, ngày và nhạc mỗi phần một màu.
object PebbleThemeSpec : AodThemeSpec {
    val HourShape = shapeOption(key = "hour_shape", default = AodShapeValue.SQUIRCLE)
    val HourStyle = styleOption(key = "hour_style", default = AodTileStyleValue.FILLED)
    val MinuteShape = shapeOption(key = "minute_shape", default = AodShapeValue.CLOVER)
    val MinuteStyle = styleOption(key = "minute_style", default = AodTileStyleValue.OUTLINE)
    val Font = AodChoiceOption(
        key = "font",
        labelRes = R.string.opt_clock_font,
        entries = ClockFontValue.entries.toImmutableList(),
        default = ClockFontValue.DEFAULT,
    )
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_clock_size_value,
        range = 60..150,
        step = 10,
        default = 100,
    )
    val Stacked = AodToggleOption(key = "stacked", labelRes = R.string.theme_opt_stacked, default = false)

    // Màu từng phần: số trên khối đặc mặc định đen như khoét lỗ, trên khối viền cùng màu khối.
    val HourTileColor = AodColorOption(key = "hour_tile_color", labelRes = R.string.theme_color_hour_tile, default = Purple)
    val HourDigitColor = AodColorOption(key = "hour_digit_color", labelRes = R.string.theme_color_hour_digits, default = Black)
    val MinuteTileColor = AodColorOption(key = "minute_tile_color", labelRes = R.string.theme_color_minute_tile, default = Purple)
    val MinuteDigitColor = AodColorOption(key = "minute_digit_color", labelRes = R.string.theme_color_minute_digits, default = Purple)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = GreyB4)
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = Purple)
    val DateStyle = AodChoiceOption(
        key = "date_style",
        labelRes = R.string.theme_opt_date_style,
        entries = AodDateStyleValue.entries.toImmutableList(),
        default = AodDateStyleValue.BOLD,
    )
    val Transition = AodChoiceOption(
        key = "transition",
        labelRes = R.string.theme_opt_transition,
        entries = AodTransitionValue.entries.toImmutableList(),
        default = AodTransitionValue.SLIDE,
    )
    val PlayShape = shapeOption(key = "play_shape", labelRes = R.string.theme_opt_play_shape, default = AodShapeValue.PEBBLE)
    val ProgressStyle = AodChoiceOption(
        key = "progress_style",
        labelRes = R.string.theme_opt_progress_style,
        entries = AodProgressStyleValue.entries.toImmutableList(),
        default = AodProgressStyleValue.WAVE,
    )

    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_hour,
            options = persistentListOf(HourShape, HourStyle),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_timer,
            labelRes = R.string.theme_tab_minute,
            options = persistentListOf(MinuteShape, MinuteStyle),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_text_aa,
            labelRes = R.string.theme_tab_digits,
            options = persistentListOf(Font, Size, Stacked),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_palette,
            labelRes = R.string.theme_tab_color,
            options = persistentListOf(HourTileColor, HourDigitColor, MinuteTileColor, MinuteDigitColor, DateColor, MediaColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_calendar_blank,
            labelRes = R.string.theme_tab_date,
            options = persistentListOf(DateStyle),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_music_notes,
            labelRes = R.string.theme_tab_media,
            options = persistentListOf(PlayShape, ProgressStyle),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_magic_wand,
            labelRes = R.string.theme_tab_effects,
            options = persistentListOf(Transition),
        ),
    )

    override val isDateBuiltIn = true

    override fun fontOf(config: AodThemeConfig): ClockFontValue = config[Font]

    override fun accentOf(config: AodThemeConfig): Color = config[HourTileColor]

    private fun shapeOption(
        key: String,
        default: AodShapeValue,
        @StringRes labelRes: Int = R.string.theme_opt_shape,
    ) = AodChoiceOption(
        key = key,
        labelRes = labelRes,
        entries = AodShapeValue.entries.toImmutableList(),
        default = default,
    )

    private fun styleOption(key: String, default: AodTileStyleValue) = AodChoiceOption(
        key = key,
        labelRes = R.string.theme_opt_tile_style,
        entries = AodTileStyleValue.entries.toImmutableList(),
        default = default,
    )
}
