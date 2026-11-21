package com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodRulesPrefs
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

@Immutable
data class AodScheduleUiModel(
    val isEnabled: Boolean = AodRulesPrefs.DEFAULT.isScheduleEnabled,
    val startMinute: Int = AodRulesPrefs.DEFAULT.scheduleStartMinute,
    val endMinute: Int = AodRulesPrefs.DEFAULT.scheduleEndMinute,
) {
    fun isActiveAt(minuteOfDay: Int): Boolean = when {
        !isEnabled || startMinute == endMinute -> true
        startMinute < endMinute -> minuteOfDay in startMinute until endMinute
        else -> minuteOfDay !in endMinute..<startMinute
    }

    fun minuteOf(time: ScheduleTimeValue): Int = when (time) {
        ScheduleTimeValue.START -> startMinute
        ScheduleTimeValue.END -> endMinute
    }

    companion object {
        private const val SECONDS_PER_MINUTE = 60

        fun timeOf(minuteOfDay: Int): LocalTime =
            LocalTime.ofSecondOfDay(minuteOfDay.toLong() * SECONDS_PER_MINUTE)

        fun minuteOfDay(time: LocalTime): Int = time.toSecondOfDay() / SECONDS_PER_MINUTE

        fun minuteOfDay(epochMillis: Long): Int =
            minuteOfDay(
                Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).toLocalTime()
            )
    }
}
