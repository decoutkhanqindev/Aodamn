package com.decoutkhanqindev.aodamn.presentation.model.aod.info

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.domain.model.Weather
import kotlin.math.roundToInt

@Immutable
data class WeatherUiModel(
    val temperature: Int,
    val condition: WeatherConditionValue,
    val isDay: Boolean,
    val updatedAtMillis: Long,
) {
    @get:DrawableRes
    val iconRes: Int
        get() = if (isDay) condition.dayIconRes else condition.nightIconRes

    fun isFreshAt(nowMillis: Long): Boolean = nowMillis - updatedAtMillis < MAX_AGE_MILLIS

    companion object {
        private const val MAX_AGE_MILLIS = 3 * 60 * 60_000L
    }
}

fun Weather.toUiModel(isFahrenheit: Boolean): WeatherUiModel = WeatherUiModel(
    temperature = (if (isFahrenheit) temperatureCelsius * 9 / 5 + 32 else temperatureCelsius).roundToInt(),
    condition = condition.toValue(),
    isDay = isDay,
    updatedAtMillis = updatedAtMillis,
)
