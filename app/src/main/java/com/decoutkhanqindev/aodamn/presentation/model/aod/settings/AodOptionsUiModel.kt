package com.decoutkhanqindev.aodamn.presentation.model.aod.settings

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodOptionsPrefs

@Immutable
data class AodOptionsUiModel(
    val isEnabled: Boolean = AodOptionsPrefs.DEFAULT.isEnabled,
    val isCustomBrightness: Boolean = AodOptionsPrefs.DEFAULT.isCustomBrightness,
    val brightnessPercent: Int = AodOptionsPrefs.DEFAULT.brightnessPercent,
    val isProximityEnabled: Boolean = AodOptionsPrefs.DEFAULT.isProximityEnabled,
    val timeoutMinutes: Int = AodOptionsPrefs.DEFAULT.timeoutMinutes,
    val autoHideSeconds: Int = AodOptionsPrefs.DEFAULT.autoHideSeconds,
) {
    companion object {
        const val BRIGHTNESS_MIN_PERCENT = 1
        const val BRIGHTNESS_MAX_PERCENT = 100
        const val TIMEOUT_STEP_MINUTES = 5
        const val TIMEOUT_MAX_MINUTES = 120
        const val AUTO_HIDE_STEP_SECONDS = 10
        const val AUTO_HIDE_MAX_SECONDS = 60
    }
}

fun AodOptionsPrefs.toUiModel(): AodOptionsUiModel = AodOptionsUiModel(
    isEnabled = isEnabled,
    isCustomBrightness = isCustomBrightness,
    brightnessPercent = brightnessPercent,
    isProximityEnabled = isProximityEnabled,
    timeoutMinutes = timeoutMinutes,
    autoHideSeconds = autoHideSeconds,
)
