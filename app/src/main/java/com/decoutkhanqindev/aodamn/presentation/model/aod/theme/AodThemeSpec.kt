package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet

// Một tab của màn sửa chủ đề: các thuộc tính hiện cùng nhau. block: khối chủ đề tự vẽ (ownBlocks) mà tab này chỉnh; Tùy chỉnh › tab của khối đó
// mở thẳng màn chủ đề ở tab này. Mỗi khối trong ownBlocks có đúng một tab (AodThemeSpecTest).
@Immutable
data class AodThemeTab(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val labelRes: Int,
    val options: ImmutableList<AodThemeOption<*>>,
    val block: AodBlockValue? = null,
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
    // trạng thái thì pin và icon thông báo chung một dòng trong bố cục của chủ đề.
    val ownBlocks: ImmutableSet<AodBlockValue>

    // Khối dùng chung chủ đề có (mặc định tất cả). Khối không có ở đây thì tab của nó ẩn ở Tùy chỉnh và AOD không vẽ dù đang bật; cài
    // đặt của khối vẫn giữ cho lúc dùng chủ đề khác.
    val components: ImmutableSet<AodComponentValue>
        get() = AllComponents

    // Có hiện nội dung thông báo mới nhất không: chủ đề tự vẽ dòng trạng thái mặc định chỉ có icon (Số áo, Viên nang...), chủ đề tự vẽ cả thông
    // báo mới nhất (Chuỗi) thì bật lại.
    val hasNotificationContent: Boolean
        get() = AodBlockValue.STATUS !in ownBlocks

    // Chủ đề có chuyển động theo giờ thật như app mẫu (vòng giây của La bàn) thì mặt số chạy theo giờ thật cả ở khung xem trước; các chủ đề khác
    // xem trước ở giờ mẫu cố định (CustomizePreviewSamples, 08:30).
    val isLiveInPreview: Boolean
        get() = false
}

val AodOptionSpec.options: List<AodThemeOption<*>>
    get() = tabs.flatMap { tab -> tab.options }

fun AodOptionSpec.tabOf(block: AodBlockValue?): AodThemeTab? =
    block?.let { tabs.find { tab -> tab.block == block } }

fun AodOptionSpec.isDefault(config: AodThemeConfig): Boolean = options.all { option -> option.isDefault(config) }

fun AodThemeTab.isDefault(config: AodThemeConfig): Boolean = options.all { option -> option.isDefault(config) }

fun AodOptionSpec.isSame(first: AodThemeConfig, second: AodThemeConfig): Boolean =
    options.all { option -> option.isSame(first, second) }
