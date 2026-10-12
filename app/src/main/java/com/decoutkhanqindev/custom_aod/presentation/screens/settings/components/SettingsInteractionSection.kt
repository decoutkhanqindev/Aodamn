package com.decoutkhanqindev.custom_aod.presentation.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.settings.AppRadioRow
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

    interaction.editingGesture?.let { gesture ->
        GestureActionDialog(
            gesture = gesture,
            selected = interaction.settings.actionOf(gesture),
            isFlashlightAvailable = interaction.isFlashlightAvailable,
            onSelect = { action ->
                onIntent(
                    SettingsIntent.Interaction.ChangeGestureAction(
                        gesture,
                        action
                    )
                )
            },
            onDismiss = { onIntent(SettingsIntent.Interaction.DismissGestureActionPicker) },
        )
    }
}

@Composable
private fun GestureActionDialog(
    gesture: AodGestureValue,
    selected: AodActionValue,
    isFlashlightAvailable: Boolean,
    onSelect: (AodActionValue) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_cancel))
            }
        },
        title = { Text(text = stringResource(gesture.labelRes)) },
        text = {
            Column {
                AodActionValue.entries
                    .filter { action -> action != AodActionValue.FLASHLIGHT || isFlashlightAvailable }
                    .forEach { action ->
                        AppRadioRow(
                            label = stringResource(action.labelRes),
                            isSelected = action == selected,
                            onClick = { onSelect(action) },
                        )
                    }
            }
        },
    )
}
