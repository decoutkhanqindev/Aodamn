package com.decoutkhanqindev.custom_aod.presentation.screens.settings.components

import android.text.format.DateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BatteryAlert
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.settings.AppRadioRow
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.AodRulesUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.AodScheduleUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.ChargingRuleValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.ScheduleTimeValue
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsRulesState
import java.time.LocalTime

@Composable
fun SettingsRulesSection(
    rules: SettingsRulesState,
    onIntent: (SettingsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val timeFormatter = rememberTimeFormatter()
    val settings = rules.settings

    SettingsGroup(
        title = stringResource(R.string.section_rules),
        modifier = modifier,
    ) {
        SettingsValueItem(
            icon = Icons.Outlined.Power,
            label = stringResource(R.string.opt_charging_rule),
            value = stringResource(settings.chargingRule.labelRes),
            onClick = { onIntent(SettingsIntent.Rules.ShowChargingRulePicker) },
        )

        SettingsDivider()

        SettingsSwitchItem(
            icon = Icons.Outlined.DateRange,
            label = stringResource(R.string.opt_schedule),
            isChecked = settings.schedule.isEnabled,
            onCheckedChange = { onIntent(SettingsIntent.Rules.ToggleSchedule(it)) },
        )

        AnimatedVisibility(visible = settings.schedule.isEnabled) {
            Column {
                ScheduleTimeValue.entries.forEach { time ->
                    SettingsSubItem(
                        label = stringResource(time.labelRes),
                        value = timeFormatter.format(AodScheduleUiModel.timeOf(settings.schedule.minuteOf(time))),
                        onClick = { onIntent(SettingsIntent.Rules.ShowScheduleTimePicker(time)) },
                    )
                }
            }
        }

        SettingsDivider()

        SettingsSliderItem(
            label = stringResource(R.string.opt_min_battery),
            valueText = if (settings.minBattery == 0) {
                stringResource(R.string.value_off)
            } else {
                stringResource(R.string.value_below_percent, settings.minBattery)
            },
            value = settings.minBattery,
            valueRange = 0..AodRulesUiModel.BATTERY_MAX_PERCENT,
            step = AodRulesUiModel.BATTERY_STEP_PERCENT,
            onValueChange = { onIntent(SettingsIntent.Rules.ChangeMinBattery(it)) },
            icon = Icons.Outlined.BatteryAlert,
        )
    }

    if (rules.isChargingRulePickerVisible) {
        ChargingRuleDialog(
            selected = settings.chargingRule,
            onSelect = { rule -> onIntent(SettingsIntent.Rules.ChangeChargingRule(rule)) },
            onDismiss = { onIntent(SettingsIntent.Rules.DismissChargingRulePicker) },
        )
    }

    rules.editingScheduleTime?.let { time ->
        ScheduleTimeDialog(
            time = time,
            initialTime = AodScheduleUiModel.timeOf(settings.schedule.minuteOf(time)),
            onConfirm = { picked ->
                onIntent(
                    SettingsIntent.Rules.ChangeScheduleTime(
                        time,
                        AodScheduleUiModel.minuteOfDay(picked)
                    )
                )
            },
            onDismiss = { onIntent(SettingsIntent.Rules.DismissScheduleTimePicker) },
        )
    }
}

@Composable
private fun ChargingRuleDialog(
    selected: ChargingRuleValue,
    onSelect: (ChargingRuleValue) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_cancel))
            }
        },
        title = { Text(text = stringResource(R.string.opt_charging_rule)) },
        text = {
            Column {
                ChargingRuleValue.entries.forEach { rule ->
                    AppRadioRow(
                        label = stringResource(rule.labelRes),
                        isSelected = rule == selected,
                        onClick = { onSelect(rule) },
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleTimeDialog(
    time: ScheduleTimeValue,
    initialTime: LocalTime,
    onConfirm: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val pickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = DateFormat.is24HourFormat(context),
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(
                    LocalTime.of(
                        pickerState.hour,
                        pickerState.minute
                    )
                )
            }) {
                Text(text = stringResource(R.string.action_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_cancel))
            }
        },
        title = { Text(text = stringResource(time.labelRes)) },
        text = { TimePicker(state = pickerState) },
    )
}
