package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.component.AodComponentValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet

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

private val AllComponents = AodComponentValue.entries.toImmutableSet()

// Đặc tả một chủ đề: thuộc tính sửa được (chia tab), các khối nó tự vẽ và các khối dùng chung nó có. Phần vẽ đồng hồ của chủ đề nằm ở
// components/aod/, chọn theo AodThemeValue.
@Immutable
interface AodThemeSpec : AodOptionSpec {
    // Khối chủ đề tự vẽ thay bản mặc định: ngày thì khối chung không vẽ lại, nhạc thì chủ đề có trình phát riêng (AodMediaControls), dòng
    // trạng thái thì pin và icon thông báo chung một dòng (AodThemeStatus).
    val ownBlocks: ImmutableSet<AodBlockValue>

    // Khối dùng chung chủ đề có (mặc định tất cả). Khối không có ở đây thì tab của nó ẩn ở Tùy chỉnh và AOD không vẽ dù đang bật; cài
    // đặt của khối vẫn giữ cho lúc dùng chủ đề khác.
    val components: ImmutableSet<AodComponentValue>
        get() = AllComponents
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
