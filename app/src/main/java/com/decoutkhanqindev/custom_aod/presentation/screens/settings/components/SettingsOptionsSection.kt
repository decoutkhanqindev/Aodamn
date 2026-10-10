package com.decoutkhanqindev.custom_aod.presentation.screens.settings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.ScreenRotation
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.AodOptionsUiModel
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsIntent

@Composable
fun SettingsOptionsSection(
    options: AodOptionsUiModel,
    isLandscape: Boolean,
    onIntent: (SettingsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsGroup(
        title = stringResource(R.string.section_display),
        modifier = modifier,
    ) {
        SettingsSwitchItem(
            icon = Icons.Outlined.LightMode,
            label = stringResource(R.string.opt_custom_brightness),
            isChecked = options.isCustomBrightness,
            onCheckedChange = { onIntent(SettingsIntent.Options.ToggleCustomBrightness(it)) },
        )

        AnimatedVisibility(visible = options.isCustomBrightness) {
            SettingsSliderItem(
                label = stringResource(R.string.opt_brightness),
                valueText = stringResource(R.string.value_percent, options.brightnessPercent),
                value = options.brightnessPercent,
                valueRange = AodOptionsUiModel.BRIGHTNESS_MIN_PERCENT..AodOptionsUiModel.BRIGHTNESS_MAX_PERCENT,
                step = 1,
                onValueChange = { onIntent(SettingsIntent.Options.ChangeBrightness(it)) },
            )
        }

        SettingsDivider()

        SettingsSwitchItem(
            icon = Icons.Outlined.Sensors,
            label = stringResource(R.string.opt_proximity),
            isChecked = options.isProximityEnabled,
            onCheckedChange = { onIntent(SettingsIntent.Options.ToggleProximity(it)) },
        )

        SettingsDivider()

        SettingsSliderItem(
            label = stringResource(R.string.opt_timeout),
            valueText = if (options.timeoutMinutes == 0) {
                stringResource(R.string.value_never)
            } else {
                stringResource(R.string.value_minutes, options.timeoutMinutes)
            },
            value = options.timeoutMinutes,
            valueRange = 0..AodOptionsUiModel.TIMEOUT_MAX_MINUTES,
            step = AodOptionsUiModel.TIMEOUT_STEP_MINUTES,
            onValueChange = { onIntent(SettingsIntent.Options.ChangeTimeout(it)) },
            icon = Icons.Outlined.Timer,
        )

        SettingsDivider()

        SettingsSwitchItem(
            icon = Icons.Outlined.ScreenRotation,
            label = stringResource(R.string.opt_landscape),
            isChecked = isLandscape,
            onCheckedChange = { onIntent(SettingsIntent.Options.ToggleLandscape(it)) },
        )
    }
}
