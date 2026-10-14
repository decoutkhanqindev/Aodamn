package com.decoutkhanqindev.custom_aod.presentation.screens.settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppOptionIcon
import com.decoutkhanqindev.custom_aod.presentation.components.AppOptionRow
import com.decoutkhanqindev.custom_aod.presentation.components.AppTextButton
import com.decoutkhanqindev.custom_aod.presentation.components.sheet.AppSheet
import com.decoutkhanqindev.custom_aod.presentation.components.sheet.AppSheetToolHeader
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.interaction.AodActionValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.interaction.AodGestureValue
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsInteractionState

@Composable
fun SettingsInteractionSection(
    interaction: SettingsInteractionState,
    onIntent: (SettingsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsGroup(
        title = stringResource(R.string.section_interaction),
        modifier = modifier,
    ) {
        AodGestureValue.entries.forEach { gesture ->
            SettingsValueItem(
                iconRes = gesture.iconRes,
                label = stringResource(gesture.labelRes),
                value = stringResource(interaction.settings.actionOf(gesture).labelRes),
                onClick = { onIntent(SettingsIntent.Interaction.ShowGestureActionPicker(gesture)) },
            )

            SettingsDivider()
        }

        SettingsSwitchItem(
            iconRes = R.drawable.ic_moon_duotone,
            label = stringResource(R.string.opt_auto_dim),
            isChecked = interaction.settings.isAutoDimEnabled && interaction.isLightSensorAvailable,
            onCheckedChange = { onIntent(SettingsIntent.Interaction.ToggleAutoDim(it)) },
            description = stringResource(
                if (interaction.isLightSensorAvailable) R.string.opt_auto_dim_desc else R.string.sensor_unsupported_light,
            ),
            isEnabled = interaction.isLightSensorAvailable,
        )

        SettingsDivider()

        SettingsSwitchItem(
            iconRes = R.drawable.ic_hand_waving_duotone,
            label = stringResource(R.string.opt_raise_to_wake),
            isChecked = interaction.settings.isRaiseToWakeEnabled && interaction.isPickupSensorAvailable,
            onCheckedChange = { onIntent(SettingsIntent.Interaction.ToggleRaiseToWake(it)) },
            description = stringResource(
                if (interaction.isPickupSensorAvailable) R.string.opt_raise_to_wake_desc else R.string.sensor_unsupported_pickup,
            ),
            isEnabled = interaction.isPickupSensorAvailable,
        )
    }

    AppSheet(
        value = interaction.editingGesture,
        onDismissRequest = { onIntent(SettingsIntent.Interaction.DismissGestureActionPicker) },
    ) { gesture ->
        val selected = interaction.settings.actionOf(gesture)

        AppSheetToolHeader(
            iconRes = gesture.iconRes,
            title = stringResource(gesture.labelRes),
            description = stringResource(R.string.option_tap_to_save),
        )

        AodActionValue.entries
            .filter { action -> action != AodActionValue.FLASHLIGHT || interaction.isFlashlightAvailable }
            .forEach { action ->
                AppOptionRow(
                    label = stringResource(action.labelRes),
                    isSelected = action == selected,
                    onClick = { onIntent(SettingsIntent.Interaction.ChangeGestureAction(gesture, action)) },
                    modifier = Modifier.padding(top = 6.dp),
                ) {
                    AppOptionIcon(iconRes = action.iconRes, isSelected = action == selected)
                }
            }

        AppTextButton(
            text = stringResource(R.string.action_cancel),
            onClick = { onIntent(SettingsIntent.Interaction.DismissGestureActionPicker) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
        )
    }
}
