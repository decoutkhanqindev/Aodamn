package com.decoutkhanqindev.aodamn.data.mapper

import com.decoutkhanqindev.aodamn.data.local.datastore.PrefsJson
import com.decoutkhanqindev.aodamn.data.local.datastore.decodeOrNull
import com.decoutkhanqindev.aodamn.data.local.datastore.model.WeatherCachePrefs
import com.decoutkhanqindev.aodamn.domain.model.Weather
import com.decoutkhanqindev.aodamn.domain.model.WeatherCondition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WeatherCacheMapperTest {

    @Test
    fun `full cache maps to weather with the wmo condition`() {
        val cache = WeatherCachePrefs(temperatureCelsius = 21.5, weatherCode = 61, isDay = false, updatedAtMillis = 1_000L)

        val expected = Weather(
            temperatureCelsius = 21.5,
            condition = WeatherCondition.RAIN,
            isDay = false,
            updatedAtMillis = 1_000L,
        )
        assertEquals(expected, cache.toDomain())
    }

    @Test
    fun `never refreshed cache has no weather`() {
        assertNull(WeatherCachePrefs().toDomain())
        assertNull(WeatherCachePrefs(temperatureCelsius = 20.0, weatherCode = 0, isDay = true).toDomain())
    }

    @Test
    fun `cache without temperature or code has no weather`() {
        assertNull(WeatherCachePrefs(weatherCode = 0, updatedAtMillis = 1L).toDomain())
        assertNull(WeatherCachePrefs(temperatureCelsius = 20.0, updatedAtMillis = 1L).toDomain())
    }

    @Test
    fun `missing day flag counts as day`() {
        val weather = WeatherCachePrefs(temperatureCelsius = 3.0, weatherCode = 71, updatedAtMillis = 5L).toDomain()

        assertEquals(true, weather?.isDay)
        assertEquals(WeatherCondition.SNOW, weather?.condition)
    }

    @Test
    fun `cache round trips through json`() {
        val cache = WeatherCachePrefs(temperatureCelsius = -4.25, weatherCode = 95, isDay = true, updatedAtMillis = 1_700_000_000_000L)
        val raw = PrefsJson.encodeToString(WeatherCachePrefs.serializer(), cache)

        assertEquals(cache, PrefsJson.decodeOrNull(WeatherCachePrefs.serializer(), raw))
    }
}
