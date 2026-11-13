package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import android.icu.text.TimeZoneNames
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.text.Normalizer
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs
import android.icu.util.TimeZone as IcuTimeZone

@Immutable
data class AodZoneUiModel(
    val id: String,
    val city: String,
    val offsetMinutes: Int,
    val region: AodZoneRegionValue,
    val searchKey: String,
) {
    val offsetText: String
        get() = offsetTextOf(offsetMinutes)

    companion object {
        private val COMBINING_MARKS = Regex("\\p{Mn}+")

        fun offsetTextOf(offsetMinutes: Int): String {
            if (offsetMinutes == 0) return "GMT"
            val absoluteMinutes = abs(offsetMinutes)
            val hours = absoluteMinutes / 60
            val minutes = absoluteMinutes % 60
            val sign = if (offsetMinutes > 0) "+" else "−"
            return buildString {
                append("GMT")
                append(sign)
                append(hours)
                if (minutes != 0) {
                    append(':')
                    append(minutes.toString().padStart(2, '0'))
                }
            }
        }

        fun searchKeyOf(text: String): String = Normalizer
            .normalize(text.lowercase(Locale.ROOT), Normalizer.Form.NFD)
            .replace(COMBINING_MARKS, "")
            .replace('đ', 'd')
            .replace('−', '-')
            .trim()

        fun cityOf(id: String, locale: Locale): String =
            TimeZoneNames.getInstance(locale).getExemplarLocationName(id)
                ?: id.substringAfterLast('/').replace('_', ' ')

        fun catalog(locale: Locale, nowMillis: Long): ImmutableList<AodZoneUiModel> {
            val timeZoneNames = TimeZoneNames.getInstance(locale)
            return IcuTimeZone.getAvailableIDs(
                IcuTimeZone.SystemTimeZoneType.CANONICAL_LOCATION,
                null,
                null,
            ).mapNotNull { id ->
                AodZoneRegionValue.of(id)?.let { region ->
                    val city = timeZoneNames.getExemplarLocationName(id) ?: id.substringAfterLast('/').replace('_', ' ')
                    val offsetMinutes = TimeZone.getTimeZone(id).getOffset(nowMillis) / 60_000
                    AodZoneUiModel(
                        id = id,
                        city = city,
                        offsetMinutes = offsetMinutes,
                        region = region,
                        searchKey = searchKeyOf("$city ${id.replace('/', ' ').replace('_', ' ')} ${offsetTextOf(offsetMinutes)}"),
                    )
                }
            }.sortedWith(
                compareBy<AodZoneUiModel> { it.region.ordinal }
                    .thenBy { it.offsetMinutes }
                    .thenBy { it.city },
            ).toImmutableList()
        }
    }
}
