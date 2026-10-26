package com.decoutkhanqindev.aodamn.domain.usecase

import com.decoutkhanqindev.aodamn.domain.repository.WeatherRepository
import com.decoutkhanqindev.aodamn.utils.suspendRunCatching
import kotlinx.coroutines.flow.first

class RefreshWeatherUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(nowMillis: Long, isForced: Boolean = false): Result<Unit> =
        suspendRunCatching {
            val updatedAtMillis = repository.observeWeather().first()?.updatedAtMillis ?: 0L
            if (isForced || nowMillis - updatedAtMillis >= MIN_REFRESH_INTERVAL_MILLIS) repository.refreshWeather()
        }

    companion object {
        private const val MIN_REFRESH_INTERVAL_MILLIS = 30 * 60_000L
    }
}
