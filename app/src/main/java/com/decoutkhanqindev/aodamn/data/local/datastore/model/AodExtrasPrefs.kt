package com.decoutkhanqindev.aodamn.data.local.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class AodExtrasPrefs(
    val memo: String = "",
    val isCalendarEnabled: Boolean = false,
    val isWeatherEnabled: Boolean = false,
    val isWeatherFahrenheit: Boolean = false,
    val isDateEnabled: Boolean = true,
    val isBatteryEnabled: Boolean = true,
) {
    companion object {
        val DEFAULT = AodExtrasPrefs()
    }
}
