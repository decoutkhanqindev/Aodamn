package com.decoutkhanqindev.aodamn.presentation.model.aod.appearance

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.theme.GradientAutumnEnd
import com.decoutkhanqindev.aodamn.presentation.theme.GradientAutumnMiddle
import com.decoutkhanqindev.aodamn.presentation.theme.GradientAutumnStart
import com.decoutkhanqindev.aodamn.presentation.theme.GradientGraphiteEnd
import com.decoutkhanqindev.aodamn.presentation.theme.GradientGraphiteMiddle
import com.decoutkhanqindev.aodamn.presentation.theme.GradientGraphiteStart
import com.decoutkhanqindev.aodamn.presentation.theme.GradientIceEmberEnd
import com.decoutkhanqindev.aodamn.presentation.theme.GradientIceEmberMiddle
import com.decoutkhanqindev.aodamn.presentation.theme.GradientIceEmberStart
import com.decoutkhanqindev.aodamn.presentation.theme.GradientLavaEnd
import com.decoutkhanqindev.aodamn.presentation.theme.GradientLavaMiddle
import com.decoutkhanqindev.aodamn.presentation.theme.GradientLavaStart
import com.decoutkhanqindev.aodamn.presentation.theme.GradientLilacSeaEnd
import com.decoutkhanqindev.aodamn.presentation.theme.GradientLilacSeaMiddle
import com.decoutkhanqindev.aodamn.presentation.theme.GradientLilacSeaStart
import com.decoutkhanqindev.aodamn.presentation.theme.GradientLimeRoseEnd
import com.decoutkhanqindev.aodamn.presentation.theme.GradientLimeRoseMiddle
import com.decoutkhanqindev.aodamn.presentation.theme.GradientLimeRoseStart
import com.decoutkhanqindev.aodamn.presentation.theme.GradientMintBerryEnd
import com.decoutkhanqindev.aodamn.presentation.theme.GradientMintBerryMiddle
import com.decoutkhanqindev.aodamn.presentation.theme.GradientMintBerryStart
import com.decoutkhanqindev.aodamn.presentation.theme.GradientOrchidGoldEnd
import com.decoutkhanqindev.aodamn.presentation.theme.GradientOrchidGoldMiddle
import com.decoutkhanqindev.aodamn.presentation.theme.GradientOrchidGoldStart
import com.decoutkhanqindev.aodamn.presentation.theme.GradientPastelSkyEnd
import com.decoutkhanqindev.aodamn.presentation.theme.GradientPastelSkyMiddle
import com.decoutkhanqindev.aodamn.presentation.theme.GradientPastelSkyStart
import com.decoutkhanqindev.aodamn.presentation.theme.GradientSilverEnd
import com.decoutkhanqindev.aodamn.presentation.theme.GradientSilverMiddle
import com.decoutkhanqindev.aodamn.presentation.theme.GradientSilverStart
import com.decoutkhanqindev.aodamn.presentation.theme.GradientSunsetEnd
import com.decoutkhanqindev.aodamn.presentation.theme.GradientSunsetMiddle
import com.decoutkhanqindev.aodamn.presentation.theme.GradientSunsetStart
import com.decoutkhanqindev.aodamn.presentation.theme.GradientTropicEnd
import com.decoutkhanqindev.aodamn.presentation.theme.GradientTropicMiddle
import com.decoutkhanqindev.aodamn.presentation.theme.GradientTropicStart
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

// Bốn hướng cộng nút Đảo chiều (đảo thứ tự màu) là đủ 8 hướng. start / end tính theo khung đang vẽ: Offset.Infinite là cạnh phải / dưới.
@Immutable
enum class AodGradientDirection(
    val code: Int,
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val labelRes: Int,
    val start: Offset,
    val end: Offset,
) {
    HORIZONTAL(
        code = 1,
        iconRes = R.drawable.ic_arrow_right,
        labelRes = R.string.gradient_direction_horizontal,
        start = Offset.Zero,
        end = Offset(Float.POSITIVE_INFINITY, 0f),
    ),
    VERTICAL(
        code = 2,
        iconRes = R.drawable.ic_arrow_down,
        labelRes = R.string.gradient_direction_vertical,
        start = Offset.Zero,
        end = Offset(0f, Float.POSITIVE_INFINITY),
    ),
    DIAGONAL_DOWN(
        code = 3,
        iconRes = R.drawable.ic_arrow_down_right,
        labelRes = R.string.gradient_direction_diagonal_down,
        start = Offset.Zero,
        end = Offset.Infinite,
    ),
    DIAGONAL_UP(
        code = 4,
        iconRes = R.drawable.ic_arrow_up_right,
        labelRes = R.string.gradient_direction_diagonal_up,
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f),
    ),
    ;

    companion object {
        val DEFAULT = HORIZONTAL

        fun fromCode(code: Int?): AodGradientDirection? = entries.find { direction -> direction.code == code }
    }
}

// Ba màu Đầu → Giữa → Cuối. Màu Đầu cũng là màu một màu của thuộc tính (AodColorOption) nên code chỉ đọc Color vẫn đúng.
@Immutable
data class AodGradient(
    val colors: ImmutableList<Color>,
    val direction: AodGradientDirection = AodGradientDirection.DEFAULT,
) {
    val start: Color
        get() = colors.first()

    fun withColor(index: Int, color: Color): AodGradient =
        copy(colors = colors.mapIndexed { i, old -> if (i == index) color else old }.toImmutableList())

    fun reversed(): AodGradient = copy(colors = colors.reversed().toImmutableList())

    // Trải theo khung của chính phần được tô (mỗi chữ, mỗi icon), không theo cả màn.
    fun toBrush(): Brush = Brush.linearGradient(colors = colors, start = direction.start, end = direction.end)

    companion object {
        const val STOP_COUNT = 3

        fun of(start: Color, middle: Color, end: Color): AodGradient = AodGradient(persistentListOf(start, middle, end))
    }
}

// Bộ gradient có sẵn lấy từ bảng màu của app mẫu (Muviz Edge), hướng đã quy về thứ tự Đầu → Cuối; bộ trùng đã bỏ.
// Lưới 2 hàng × 6 theo đúng thứ tự khai báo.
@Immutable
enum class GradientSwatchValue(
    val gradient: AodGradient,
    @param:StringRes val labelRes: Int,
) {
    SUNSET(AodGradient.of(GradientSunsetStart, GradientSunsetMiddle, GradientSunsetEnd), R.string.gradient_swatch_sunset),
    ICE_EMBER(AodGradient.of(GradientIceEmberStart, GradientIceEmberMiddle, GradientIceEmberEnd), R.string.gradient_swatch_ice_ember),
    LIME_ROSE(AodGradient.of(GradientLimeRoseStart, GradientLimeRoseMiddle, GradientLimeRoseEnd), R.string.gradient_swatch_lime_rose),
    TROPIC(AodGradient.of(GradientTropicStart, GradientTropicMiddle, GradientTropicEnd), R.string.gradient_swatch_tropic),
    ORCHID_GOLD(AodGradient.of(GradientOrchidGoldStart, GradientOrchidGoldMiddle, GradientOrchidGoldEnd), R.string.gradient_swatch_orchid_gold),
    PASTEL_SKY(AodGradient.of(GradientPastelSkyStart, GradientPastelSkyMiddle, GradientPastelSkyEnd), R.string.gradient_swatch_pastel_sky),
    LAVA(AodGradient.of(GradientLavaStart, GradientLavaMiddle, GradientLavaEnd), R.string.gradient_swatch_lava),
    MINT_BERRY(AodGradient.of(GradientMintBerryStart, GradientMintBerryMiddle, GradientMintBerryEnd), R.string.gradient_swatch_mint_berry),
    LILAC_SEA(AodGradient.of(GradientLilacSeaStart, GradientLilacSeaMiddle, GradientLilacSeaEnd), R.string.gradient_swatch_lilac_sea),
    AUTUMN(AodGradient.of(GradientAutumnStart, GradientAutumnMiddle, GradientAutumnEnd), R.string.gradient_swatch_autumn),
    GRAPHITE(AodGradient.of(GradientGraphiteStart, GradientGraphiteMiddle, GradientGraphiteEnd), R.string.gradient_swatch_graphite),
    SILVER(AodGradient.of(GradientSilverStart, GradientSilverMiddle, GradientSilverEnd), R.string.gradient_swatch_silver),
    ;

    companion object {
        val DEFAULT = SUNSET
        const val COLUMN_COUNT = 6
    }
}
