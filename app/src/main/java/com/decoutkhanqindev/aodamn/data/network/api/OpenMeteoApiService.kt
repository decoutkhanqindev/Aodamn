package com.decoutkhanqindev.aodamn.data.network.api

import com.decoutkhanqindev.aodamn.data.network.api.response.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApiService {

    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String,
    ): ForecastResponse

    companion object {
        const val BASE_URL = "https://api.open-meteo.com/"
        const val CURRENT_WEATHER_FIELDS = "temperature_2m,weather_code,is_day"
    }
}
