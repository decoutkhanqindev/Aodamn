package com.decoutkhanqindev.aodamn.presentation.model.aod.settings

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodExtrasPrefs
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue

@Immutable
data class AodExtrasUiModel(
    val memo: String = AodExtrasPrefs.DEFAULT.memo,
    val isCalendarEnabled: Boolean = AodExtrasPrefs.DEFAULT.isCalendarEnabled,
    val isWeatherEnabled: Boolean = AodExtrasPrefs.DEFAULT.isWeatherEnabled,
    val isWeatherFahrenheit: Boolean = AodExtrasPrefs.DEFAULT.isWeatherFahrenheit,
    val isDateEnabled: Boolean = AodExtrasPrefs.DEFAULT.isDateEnabled,
    val isBatteryEnabled: Boolean = AodExtrasPrefs.DEFAULT.isBatteryEnabled,
) {
    companion object {
        const val MEMO_MAX_LENGTH = 120
    }
}

fun AodExtrasPrefs.toUiModel(): AodExtrasUiModel = AodExtrasUiModel(
    memo = memo,
    isCalendarEnabled = isCalendarEnabled,
    isWeatherEnabled = isWeatherEnabled,
    isWeatherFahrenheit = isWeatherFahrenheit,
    isDateEnabled = isDateEnabled,
    isBatteryEnabled = isBatteryEnabled,
)

fun AodExtrasUiModel.shownIn(spec: AodThemeSpec): AodExtrasUiModel = copy(
    isCalendarEnabled = isCalendarEnabled && AodComponentValue.EVENTS in spec.components,
    isWeatherEnabled = isWeatherEnabled && AodComponentValue.WEATHER in spec.components,
)
