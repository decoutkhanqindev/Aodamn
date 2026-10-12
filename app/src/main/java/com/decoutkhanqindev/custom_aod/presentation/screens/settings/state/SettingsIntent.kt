package com.decoutkhanqindev.custom_aod.presentation.screens.settings.state

import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.interaction.AodActionValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.interaction.AodGestureValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.rules.ChargingRuleValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.rules.ScheduleTimeValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue

sealed interface SettingsIntent {
    sealed interface Options : SettingsIntent {
        data class ToggleCustomBrightness(val isEnabled: Boolean) : Options
        data class ChangeBrightness(val percent: Int) : Options
        data class ToggleProximity(val isEnabled: Boolean) : Options
        data class ChangeTimeout(val minutes: Int) : Options
        data class ToggleLandscape(val isEnabled: Boolean) : Options
    }

    sealed interface Interaction : SettingsIntent {
        data class ShowGestureActionPicker(val gesture: AodGestureValue) : Interaction
        data object DismissGestureActionPicker : Interaction
        data class ChangeGestureAction(
            val gesture: AodGestureValue,
            val action: AodActionValue
        ) : Interaction

        data class ToggleAutoDim(val isEnabled: Boolean) : Interaction
        data class ToggleRaiseToWake(val isEnabled: Boolean) : Interaction
    }

    sealed interface Rules : SettingsIntent {
        data object ShowChargingRulePicker : Rules
        data object DismissChargingRulePicker : Rules
        data class ChangeChargingRule(val rule: ChargingRuleValue) : Rules
        data class ToggleSchedule(val isEnabled: Boolean) : Rules
        data class ShowScheduleTimePicker(val time: ScheduleTimeValue) : Rules
        data object DismissScheduleTimePicker : Rules
        data class ChangeScheduleTime(val time: ScheduleTimeValue, val minuteOfDay: Int) : Rules
        data class ChangeMinBattery(val percent: Int) : Rules
    }

    sealed interface Permission : SettingsIntent {
        data class OpenPermissionSettings(val permission: PermissionValue) : Permission
        data object RefreshPermissions : Permission
        data class NotificationPermissionResult(val isGranted: Boolean) : Permission
        data class CalendarPermissionResult(val isGranted: Boolean) : Permission
        data class LocationPermissionResult(val isGranted: Boolean) : Permission
    }

    data object NavigateToLanguage : SettingsIntent
}
