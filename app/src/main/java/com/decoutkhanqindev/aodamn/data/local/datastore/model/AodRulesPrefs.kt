package com.decoutkhanqindev.aodamn.data.local.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class AodRulesPrefs(
    val minBatteryPercent: Int = 0,
    val chargingRule: String = "always",
    val isScheduleEnabled: Boolean = false,
    val scheduleStartMinute: Int = 7 * 60,
    val scheduleEndMinute: Int = 23 * 60,
) {
    companion object {
        val DEFAULT = AodRulesPrefs()
    }
}
