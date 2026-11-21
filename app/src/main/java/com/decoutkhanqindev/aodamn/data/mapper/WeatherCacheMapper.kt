package com.decoutkhanqindev.aodamn.data.mapper

import com.decoutkhanqindev.aodamn.data.local.datastore.model.WeatherCachePrefs
import com.decoutkhanqindev.aodamn.domain.model.Weather

fun WeatherCachePrefs.toDomain(): Weather? {
    val updatedAtMillis = updatedAtMillis ?: return null
    return Weather(
        temperatureCelsius = temperatureCelsius ?: return null,
        condition = weatherConditionOf(weatherCode ?: return null),
        isDay = isDay ?: true,
        updatedAtMillis = updatedAtMillis,
    )
}
