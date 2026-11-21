package com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodRulesPrefs

@Immutable
data class AodRulesUiModel(
    val minBattery: Int = AodRulesPrefs.DEFAULT.minBatteryPercent,
    val chargingRule: ChargingRuleValue = ChargingRuleValue.ALWAYS,
    val schedule: AodScheduleUiModel = AodScheduleUiModel(),
) {
    fun allows(
        batteryPercent: Int?,
        isCharging: Boolean,
        isPlugged: Boolean?,
        minuteOfDay: Int
    ): Boolean =
        chargingRule.allows(isPlugged) &&
                schedule.isActiveAt(minuteOfDay) &&
                !isBatteryLow(batteryPercent, isCharging)

    private fun isBatteryLow(batteryPercent: Int?, isCharging: Boolean): Boolean =
        !isCharging && batteryPercent != null && batteryPercent in 0 until minBattery

    companion object {
        const val BATTERY_STEP_PERCENT = 5
        const val BATTERY_MAX_PERCENT = 50
    }
}

fun AodRulesPrefs.toUiModel(): AodRulesUiModel = AodRulesUiModel(
    minBattery = minBatteryPercent,
    chargingRule = ChargingRuleValue.fromId(chargingRule),
    schedule = AodScheduleUiModel(
        isEnabled = isScheduleEnabled,
        startMinute = scheduleStartMinute,
        endMinute = scheduleEndMinute,
    ),
)
