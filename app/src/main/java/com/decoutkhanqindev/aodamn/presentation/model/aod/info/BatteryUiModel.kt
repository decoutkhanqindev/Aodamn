package com.decoutkhanqindev.aodamn.presentation.model.aod.info

import androidx.compose.runtime.Immutable

@Immutable
data class BatteryUiModel(
    val percent: Int,
    val isCharging: Boolean,
) {
    val isLow: Boolean
        get() = !isCharging && percent <= LOW_PERCENT

    companion object {
        private const val LOW_PERCENT = 15
    }
}
