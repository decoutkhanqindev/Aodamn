package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFontValue
import kotlinx.collections.immutable.ImmutableList

// Một tab của màn sửa chủ đề: các thuộc tính hiện cùng nhau.
@Immutable
data class AodThemeTab(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val labelRes: Int,
    val options: ImmutableList<AodThemeOption<*>>,
)

// Đặc tả một chủ đề: thuộc tính sửa được (chia tab) và kiểu mà các khối dùng chung (ngày, thời tiết, ghi chú, nét vẽ) lấy theo.
// Phần vẽ đồng hồ của chủ đề nằm ở components/aod/theme/, chọn theo AodThemeValue.
@Immutable
interface AodThemeSpec {
    val tabs: ImmutableList<AodThemeTab>

    // Chủ đề tự vẽ dòng ngày cạnh đồng hồ thì khối chung không vẽ lại.
    val isDateBuiltIn: Boolean

    fun fontOf(config: AodThemeConfig): ClockFontValue

    fun accentOf(config: AodThemeConfig): Color
}

val AodThemeSpec.options: List<AodThemeOption<*>>
    get() = tabs.flatMap { tab -> tab.options }

// So theo giá trị đọc ra chứ không theo map đã lưu: khoá thiếu và khoá ghi giá trị mặc định là như nhau.
fun AodThemeSpec.isDefault(config: AodThemeConfig): Boolean =
    options.all { option -> option.decode(config.values[option.key]) == option.default }

fun AodThemeSpec.isSame(first: AodThemeConfig, second: AodThemeConfig): Boolean =
    options.all { option -> option.decode(first.values[option.key]) == option.decode(second.values[option.key]) }
