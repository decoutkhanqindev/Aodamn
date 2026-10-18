package com.decoutkhanqindev.aodamn.presentation.screens.settings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.AodOptionsUiModel
import com.decoutkhanqindev.aodamn.presentation.screens.settings.state.SettingsIntent

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
            iconRes = R.drawable.ic_sun_duotone,
            label = stringResource(R.string.opt_custom_brightness),
            isChecked = options.isCustomBrightness,
            onCheckedChange = { onIntent(SettingsIntent.Options.ToggleCustomBrightness(it)) },
        )

        AnimatedVisibility(visible = options.isCustomBrightness) {
            SettingsSliderItem(
                label = stringResource(R.string.opt_brightness),
                valueText = stringResource(R.string.value_percent, options.brightnessPercent),
                progress = { options.brightnessPercent },
                valueRange = AodOptionsUiModel.BRIGHTNESS_MIN_PERCENT..AodOptionsUiModel.BRIGHTNESS_MAX_PERCENT,
                step = 1,
                onProgressChange = { onIntent(SettingsIntent.Options.ChangeBrightness(it)) },
            )
        }

        SettingsDivider()

        SettingsSwitchItem(
            iconRes = R.drawable.ic_eye_closed_duotone,
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
            progress = { options.timeoutMinutes },
            valueRange = 0..AodOptionsUiModel.TIMEOUT_MAX_MINUTES,
            step = AodOptionsUiModel.TIMEOUT_STEP_MINUTES,
            onProgressChange = { onIntent(SettingsIntent.Options.ChangeTimeout(it)) },
            iconRes = R.drawable.ic_timer_duotone,
        )

        SettingsDivider()

        SettingsSwitchItem(
            iconRes = R.drawable.ic_device_rotate_duotone,
            label = stringResource(R.string.opt_landscape),
            isChecked = isLandscape,
            onCheckedChange = { onIntent(SettingsIntent.Options.ToggleLandscape(it)) },
        )
    }
}
