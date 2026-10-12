package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

// Một tab của màn sửa chủ đề: các thuộc tính hiện cùng nhau.
@Immutable
data class AodThemeTab(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val labelRes: Int,
    val options: ImmutableList<AodThemeOption<*>>,
)

// Danh sách thuộc tính sửa được chia theo tab, dùng cho chủ đề (AodThemeSpec) và kiểu từng khối dùng chung (AodComponentSpec).
@Immutable
interface AodOptionSpec {
    val tabs: ImmutableList<AodThemeTab>
}

// Đặc tả một chủ đề: thuộc tính sửa được (chia tab) và các khối nó tự vẽ. Phần vẽ đồng hồ của chủ đề nằm ở components/aod/, chọn theo
// AodThemeValue.
@Immutable
interface AodThemeSpec : AodOptionSpec {
    // Khối chủ đề tự vẽ thay bản mặc định: ngày thì khối chung không vẽ lại, nhạc thì chủ đề có trình phát riêng (AodMediaControls).
    val ownBlocks: ImmutableSet<AodBlockValue>
}

val AodOptionSpec.options: List<AodThemeOption<*>>
    get() = tabs.flatMap { tab -> tab.options }

// So theo giá trị đọc ra chứ không theo map đã lưu: khoá thiếu và khoá ghi giá trị mặc định là như nhau.
fun AodOptionSpec.isDefault(config: AodThemeConfig): Boolean =
    options.all { option -> option.decode(config.values[option.key]) == option.default }

fun AodThemeTab.isDefault(config: AodThemeConfig): Boolean =
    options.all { option -> option.decode(config.values[option.key]) == option.default }

fun AodOptionSpec.isSame(first: AodThemeConfig, second: AodThemeConfig): Boolean =
    options.all { option -> option.decode(first.values[option.key]) == option.decode(second.values[option.key]) }
