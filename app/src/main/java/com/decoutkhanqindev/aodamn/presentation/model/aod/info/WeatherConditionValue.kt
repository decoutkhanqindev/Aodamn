package com.decoutkhanqindev.aodamn.presentation.model.aod.info

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.domain.model.WeatherCondition

@Immutable
enum class WeatherConditionValue(
    @param:DrawableRes val dayIconRes: Int,
    @param:DrawableRes val nightIconRes: Int,
    @param:StringRes val labelRes: Int,
) {
    CLEAR(
        dayIconRes = R.drawable.ic_sun_light,
        nightIconRes = R.drawable.ic_moon_stars_light,
        labelRes = R.string.weather_clear
    ),
    PARTLY_CLOUDY(
        dayIconRes = R.drawable.ic_cloud_sun_light,
        nightIconRes = R.drawable.ic_cloud_moon_light,
        labelRes = R.string.weather_partly_cloudy,
    ),
    CLOUDY(
        dayIconRes = R.drawable.ic_cloud_light,
        nightIconRes = R.drawable.ic_cloud_light,
        labelRes = R.string.weather_cloudy
    ),
    FOG(
        dayIconRes = R.drawable.ic_cloud_fog_light,
        nightIconRes = R.drawable.ic_cloud_fog_light,
        labelRes = R.string.weather_fog
    ),
    DRIZZLE(
        dayIconRes = R.drawable.ic_drop_light,
        nightIconRes = R.drawable.ic_drop_light,
        labelRes = R.string.weather_drizzle
    ),
    RAIN(
        dayIconRes = R.drawable.ic_cloud_rain_light,
        nightIconRes = R.drawable.ic_cloud_rain_light,
        labelRes = R.string.weather_rain
    ),
    SNOW(
        dayIconRes = R.drawable.ic_cloud_snow_light,
        nightIconRes = R.drawable.ic_cloud_snow_light,
        labelRes = R.string.weather_snow
    ),
    THUNDERSTORM(
        dayIconRes = R.drawable.ic_cloud_lightning_light,
        nightIconRes = R.drawable.ic_cloud_lightning_light,
        labelRes = R.string.weather_thunderstorm,
    ),
}

fun WeatherCondition.toValue(): WeatherConditionValue = when (this) {
    WeatherCondition.CLEAR -> WeatherConditionValue.CLEAR
    WeatherCondition.PARTLY_CLOUDY -> WeatherConditionValue.PARTLY_CLOUDY
    WeatherCondition.CLOUDY -> WeatherConditionValue.CLOUDY
    WeatherCondition.FOG -> WeatherConditionValue.FOG
    WeatherCondition.DRIZZLE -> WeatherConditionValue.DRIZZLE
    WeatherCondition.RAIN -> WeatherConditionValue.RAIN
    WeatherCondition.SNOW -> WeatherConditionValue.SNOW
    WeatherCondition.THUNDERSTORM -> WeatherConditionValue.THUNDERSTORM
}
