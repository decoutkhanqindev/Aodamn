package com.decoutkhanqindev.custom_aod.presentation.screens.settings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppOptionIcon
import com.decoutkhanqindev.custom_aod.presentation.components.AppOptionRow
import com.decoutkhanqindev.custom_aod.presentation.components.AppPrimaryButton
import com.decoutkhanqindev.custom_aod.presentation.components.AppTextButton
import com.decoutkhanqindev.custom_aod.presentation.components.AppTimeWheel
import com.decoutkhanqindev.custom_aod.presentation.components.is24HourFormat
import com.decoutkhanqindev.custom_aod.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.sheet.AppSheet
import com.decoutkhanqindev.custom_aod.presentation.components.sheet.AppSheetToolHeader
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.rules.AodRulesUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.rules.AodScheduleUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.rules.ChargingRuleValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.rules.ScheduleTimeValue
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsRulesState

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
            iconRes = R.drawable.ic_plugs_connected_duotone,
            label = stringResource(R.string.opt_charging_rule),
            value = stringResource(settings.chargingRule.labelRes),
            onClick = { onIntent(SettingsIntent.Rules.ShowChargingRulePicker) },
        )

        SettingsDivider()

        SettingsSwitchItem(
            iconRes = R.drawable.ic_calendar_dots_duotone,
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
            progress = { settings.minBattery },
            valueRange = 0..AodRulesUiModel.BATTERY_MAX_PERCENT,
            step = AodRulesUiModel.BATTERY_STEP_PERCENT,
            onProgressChange = { onIntent(SettingsIntent.Rules.ChangeMinBattery(it)) },
            iconRes = R.drawable.ic_battery_warning_duotone,
        )
    }

    AppSheet(
        isVisible = rules.isChargingRulePickerVisible,
        onDismissRequest = { onIntent(SettingsIntent.Rules.DismissChargingRulePicker) },
    ) {
        AppSheetToolHeader(
            iconRes = R.drawable.ic_plugs_connected,
            title = stringResource(R.string.opt_charging_rule),
            description = stringResource(R.string.opt_charging_rule_desc),
        )

        ChargingRuleValue.entries.forEach { rule ->
            val isSelected = rule == settings.chargingRule
            AppOptionRow(
                label = stringResource(rule.labelRes),
                isSelected = isSelected,
                onClick = { onIntent(SettingsIntent.Rules.ChangeChargingRule(rule)) },
                modifier = Modifier.padding(top = 6.dp),
            ) {
                AppOptionIcon(iconRes = rule.iconRes, isSelected = isSelected)
            }
        }

        AppTextButton(
            text = stringResource(R.string.action_cancel),
            onClick = { onIntent(SettingsIntent.Rules.DismissChargingRulePicker) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
        )
    }

    // Bánh xe cuộn dọc nên sheet không kéo được (kéo bánh xe không kéo theo sheet).
    AppSheet(
        value = rules.editingScheduleTime,
        onDismissRequest = { onIntent(SettingsIntent.Rules.DismissScheduleTimePicker) },
        isDragEnabled = false,
    ) { time ->
        val initialTime = AodScheduleUiModel.timeOf(settings.schedule.minuteOf(time))
        var picked by remember(time) { mutableStateOf(initialTime) }

        AppSheetToolHeader(
            iconRes = R.drawable.ic_clock,
            title = stringResource(time.labelRes),
            description = stringResource(time.descriptionRes),
        )

        AppTimeWheel(
            initialTime = initialTime,
            is24Hour = is24HourFormat(),
            onTimeChange = { newTime -> picked = newTime },
        )

        AppPrimaryButton(
            text = stringResource(R.string.time_set, picked.format(rememberTimeFormatter())),
            onClick = { onIntent(SettingsIntent.Rules.ChangeScheduleTime(time, AodScheduleUiModel.minuteOfDay(picked))) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            iconRes = R.drawable.ic_check,
        )

        AppTextButton(
            text = stringResource(R.string.action_cancel),
            onClick = { onIntent(SettingsIntent.Rules.DismissScheduleTimePicker) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        )
    }
}
