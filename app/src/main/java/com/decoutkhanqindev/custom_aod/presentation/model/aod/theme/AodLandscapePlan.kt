package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.component.AodComponentValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

// Khối dùng chung chiếm chỗ riêng trên AOD (ngoài đồng hồ, ngày, pin, icon thông báo), theo thứ tự màn dọc; NOTIFICATION là thông báo mới
// nhất (icon thông báo đi cùng pin).
@Immutable
enum class AodPanelBlockValue(val component: AodComponentValue) {
    WEATHER(AodComponentValue.WEATHER),
    EVENTS(AodComponentValue.EVENTS),
    MEMO(AodComponentValue.MEMO),
    DRAWING(AodComponentValue.DRAWING),
    NOTIFICATION(AodComponentValue.NOTIFICATIONS),
    MEDIA(AodComponentValue.MEDIA),
}

// Bố cục màn ngang theo các khối đang hiện, dùng chung cho mọi chủ đề (README › Thêm chủ đề mới › Bố cục ngang): thời tiết đứng cạnh ngày
// trừ khi có sự kiện (lúc đó là đầu cột lịch trình); info là cột thông tin (lịch trình, thông báo mới nhất, ghi nhớ); nét vẽ và nhạc là
// khối lớn, mỗi khối một chỗ riêng.
@Immutable
data class AodLandscapePlan(
    val isWeatherBesideDate: Boolean = false,
    val info: ImmutableList<AodPanelBlockValue> = persistentListOf(),
    val hasDrawing: Boolean = false,
    val hasMedia: Boolean = false,
) {
    // Chỉ đồng hồ, ngày, pin, icon thông báo (và thời tiết cạnh ngày): cả cụm giữa màn hình.
    val isEmpty: Boolean
        get() = info.isEmpty() && !hasDrawing && !hasMedia

    val isMediaOnly: Boolean
        get() = hasMedia && info.isEmpty() && !hasDrawing

    // Nhạc cùng nhóm khác: đủ khối cho ba cột (thông tin hoặc nét vẽ | đồng hồ | nhạc).
    val hasMediaWithOthers: Boolean
        get() = hasMedia && (info.isNotEmpty() || hasDrawing)

    companion object {
        private val InfoOrder = listOf(
            AodPanelBlockValue.WEATHER,
            AodPanelBlockValue.EVENTS,
            AodPanelBlockValue.NOTIFICATION,
            AodPanelBlockValue.MEMO,
        )

        fun of(blocks: Collection<AodPanelBlockValue>): AodLandscapePlan {
            val isWeatherBesideDate = AodPanelBlockValue.WEATHER in blocks && AodPanelBlockValue.EVENTS !in blocks
            return AodLandscapePlan(
                isWeatherBesideDate = isWeatherBesideDate,
                info = InfoOrder
                    .filter { block -> block in blocks && !(block == AodPanelBlockValue.WEATHER && isWeatherBesideDate) }
                    .toImmutableList(),
                hasDrawing = AodPanelBlockValue.DRAWING in blocks,
                hasMedia = AodPanelBlockValue.MEDIA in blocks,
            )
        }
    }
}
