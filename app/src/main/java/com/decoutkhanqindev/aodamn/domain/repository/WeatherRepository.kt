package com.decoutkhanqindev.aodamn.domain.repository

import com.decoutkhanqindev.aodamn.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun observeWeather(): Flow<Weather?>

    suspend fun refreshWeather()
}
