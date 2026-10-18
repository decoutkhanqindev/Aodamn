package com.decoutkhanqindev.aodamn.domain.usecase

import com.decoutkhanqindev.aodamn.domain.model.Weather
import com.decoutkhanqindev.aodamn.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class ObserveWeatherUseCase(private val repository: WeatherRepository) {
    operator fun invoke(): Flow<Weather?> = repository.observeWeather()
}
