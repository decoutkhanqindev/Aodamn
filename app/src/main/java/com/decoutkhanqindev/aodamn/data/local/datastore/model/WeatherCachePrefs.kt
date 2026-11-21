package com.decoutkhanqindev.aodamn.data.local.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherCachePrefs(
    val temperatureCelsius: Double? = null,
    val weatherCode: Int? = null,
    val isDay: Boolean? = null,
    val updatedAtMillis: Long? = null,
)
