package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble

import androidx.annotation.StringRes
import androidx.compose.ui.text.font.FontFamily
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodToggleOption
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.aodamn.presentation.theme.Lavender
import com.decoutkhanqindev.aodamn.presentation.theme.Pink
import com.decoutkhanqindev.aodamn.presentation.theme.Purple
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

// Chủ đề Sỏi: giờ và phút mỗi số một khối hình mềm (8 hình, đặc hoặc viền), dòng ngày ngay dưới, số đổi có animation; trình phát
// nhạc riêng (nút phát trong khối hình, thanh tiến trình gợn sóng). Khối, chữ số, ngày và nhạc mỗi phần một màu.
object PebbleThemeSpec : AodThemeSpec {
    val HourShape = shapeOption(key = "hour_shape", default = AodShapeValue.COOKIE)
    val HourStyle = styleOption(key = "hour_style", default = AodTileStyleValue.FILLED)
    val MinuteShape = shapeOption(key = "minute_shape", default = AodShapeValue.FLOWER)
    val MinuteStyle = styleOption(key = "minute_style", default = AodTileStyleValue.OUTLINE)

    // Font cố định của chủ đề (không cho chọn): chữ số, ngày và trình phát. Khoá "font" đã lưu ở bản cũ bị bỏ qua.
    val Font: FontFamily = FontFamilyPlusJakartaSans
    val Size = AodRangeOption(
        key = "size",
        labelRes = R.string.opt_size_value,
        range = 60..150,
        step = 10,
        default = 100,
    )
    val Stacked = AodToggleOption(key = "stacked", labelRes = R.string.theme_opt_stacked, default = false)

    // Màu từng phần: số trên khối đặc mặc định đen như khoét lỗ, trên khối viền cùng màu khối. Mặc định hai tông tím + hồng (giờ tím, phút
    // và nhạc hồng, ngày lavender) cho thẻ chủ đề bắt mắt.
    val HourTileColor = AodColorOption(key = "hour_tile_color", labelRes = R.string.theme_color_hour_tile, default = Purple)
    val HourDigitColor = AodColorOption(key = "hour_digit_color", labelRes = R.string.theme_color_hour_digits, default = Black)
    val MinuteTileColor = AodColorOption(key = "minute_tile_color", labelRes = R.string.theme_color_minute_tile, default = Pink)
    val MinuteDigitColor = AodColorOption(key = "minute_digit_color", labelRes = R.string.theme_color_minute_digits, default = Pink)
    val DateColor = AodColorOption(key = "date_color", labelRes = R.string.theme_color_date, default = Lavender)
    val MediaColor = AodColorOption(key = "media_color", labelRes = R.string.theme_color_media, default = Pink)
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
    val PlayShape = shapeOption(key = "play_shape", labelRes = R.string.theme_opt_play_shape, default = AodShapeValue.COOKIE)
    val ProgressStyle = AodChoiceOption(
        key = "progress_style",
        labelRes = R.string.theme_opt_progress_style,
        entries = AodProgressStyleValue.entries.toImmutableList(),
        default = AodProgressStyleValue.WAVE,
    )

    // Mỗi tab một phần của chủ đề như khối ở Tùy chỉnh: kiểu rồi màu của chính phần đó (không gom màu vào một tab riêng).
    override val tabs = persistentListOf(
        AodThemeTab(
            iconRes = R.drawable.ic_clock,
            labelRes = R.string.theme_tab_hour,
            options = persistentListOf(HourShape, HourStyle, HourTileColor, HourDigitColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_timer,
            labelRes = R.string.theme_tab_minute,
            options = persistentListOf(MinuteShape, MinuteStyle, MinuteTileColor, MinuteDigitColor),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_text_aa,
            labelRes = R.string.theme_tab_digits,
            options = persistentListOf(Size, Stacked),
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_calendar_blank,
            labelRes = R.string.theme_tab_date,
            options = persistentListOf(DateStyle, DateColor),
            block = AodBlockValue.DATE,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_music_notes,
            labelRes = R.string.theme_tab_media,
            options = persistentListOf(PlayShape, ProgressStyle, MediaColor),
            block = AodBlockValue.MEDIA,
        ),
        AodThemeTab(
            iconRes = R.drawable.ic_magic_wand,
            labelRes = R.string.theme_tab_effects,
            options = persistentListOf(Transition),
        ),
    )

    override val ownBlocks = persistentSetOf(AodBlockValue.DATE, AodBlockValue.MEDIA)

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
