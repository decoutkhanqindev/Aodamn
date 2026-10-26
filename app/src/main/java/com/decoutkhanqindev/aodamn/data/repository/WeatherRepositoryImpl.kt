package com.decoutkhanqindev.aodamn.data.repository

import com.decoutkhanqindev.aodamn.data.device.location.DeviceLocationManager
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.data.mapper.weatherConditionOf
import com.decoutkhanqindev.aodamn.data.network.api.OpenMeteoApiService
import com.decoutkhanqindev.aodamn.domain.model.Weather
import com.decoutkhanqindev.aodamn.domain.repository.WeatherRepository
import com.decoutkhanqindev.aodamn.utils.Tag
import com.decoutkhanqindev.aodamn.utils.withContextCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import timber.log.Timber
import kotlin.math.roundToInt

class WeatherRepositoryImpl(
    private val openMeteoApiService: OpenMeteoApiService,
    private val deviceLocationManager: DeviceLocationManager,
    private val dataStoreManager: DataStoreManager,
) : WeatherRepository, Tag {

    override fun observeWeather(): Flow<Weather?> = combine(
        dataStoreManager.weatherTemperatureCelsius.filterNotNull(),
        dataStoreManager.weatherCode.filterNotNull(),
        dataStoreManager.isWeatherDay.filterNotNull(),
        dataStoreManager.weatherUpdatedAtMillis.filterNotNull(),
    ) { temperatureCelsius, code, isDay, updatedAtMillis ->
        if (updatedAtMillis == DataStoreManager.DEFAULT_WEATHER_UPDATED_AT_MILLIS) {
            null
        } else {
            Weather(
                temperatureCelsius = temperatureCelsius,
                condition = weatherConditionOf(code),
                isDay = isDay,
                updatedAtMillis = updatedAtMillis,
            )
        }
    }

    override suspend fun refreshWeather(): Unit = withContextCatching(
        block = {
            val coordinates = checkNotNull(deviceLocationManager.currentCoarseLocation()) {
                "Location is unavailable (no permission, or location is off)"
            }
            val current = openMeteoApiService.getForecast(
                latitude = coordinates.latitude.roundedCoordinate(),
                longitude = coordinates.longitude.roundedCoordinate(),
                current = OpenMeteoApiService.CURRENT_WEATHER_FIELDS,
            ).current
            dataStoreManager.saveWeather(
                temperatureCelsius = current.temperatureCelsius,
                code = current.weatherCode,
                isDay = current.isDay == OPEN_METEO_DAY,
                updatedAtMillis = System.currentTimeMillis(),
            )
        },
        catch = { e -> Timber.tag(tag).w("Could not refresh the weather: ${e.message}") },
    )

    private fun Double.roundedCoordinate(): Double =
        (this * COORDINATE_SCALE).roundToInt() / COORDINATE_SCALE

    companion object {
        private const val COORDINATE_SCALE = 100.0
        private const val OPEN_METEO_DAY = 1
    }
}
