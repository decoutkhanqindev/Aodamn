package com.decoutkhanqindev.aodamn.domain.model

data class Weather(
    val temperatureCelsius: Double,
    val condition: WeatherCondition,
    val isDay: Boolean,
    val updatedAtMillis: Long,
)
