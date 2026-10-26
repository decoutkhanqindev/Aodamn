package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
enum class AodPanelBlockValue(val component: AodComponentValue) {
    WEATHER(AodComponentValue.WEATHER),
    EVENTS(AodComponentValue.EVENTS),
    MEMO(AodComponentValue.MEMO),
    DRAWING(AodComponentValue.DRAWING),
    NOTIFICATION(AodComponentValue.NOTIFICATIONS),
    MEDIA(AodComponentValue.MEDIA),
}

@Immutable
data class AodLandscapePlan(
    val isWeatherBesideDate: Boolean = false,
    val info: ImmutableList<AodPanelBlockValue> = persistentListOf(),
    val hasDrawing: Boolean = false,
    val hasMedia: Boolean = false,
) {
    val isEmpty: Boolean
        get() = info.isEmpty() && !hasDrawing && !hasMedia

    val isMediaOnly: Boolean
        get() = hasMedia && info.isEmpty() && !hasDrawing

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
