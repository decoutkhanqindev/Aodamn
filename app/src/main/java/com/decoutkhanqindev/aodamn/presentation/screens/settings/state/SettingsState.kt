package com.decoutkhanqindev.aodamn.presentation.screens.settings.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WakeResultValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.AodOptionsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodGestureValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodInteractionUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules.AodRulesUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules.ScheduleTimeValue
import com.decoutkhanqindev.aodamn.presentation.model.language.LanguageUiModel
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class SettingsState(
    val isLoading: Boolean = true,
    val options: AodOptionsUiModel = AodOptionsUiModel(),
    val appearance: AodAppearanceUiModel = AodAppearanceUiModel(),
    val interaction: SettingsInteractionState = SettingsInteractionState(),
    val rules: SettingsRulesState = SettingsRulesState(),
    val permissions: ImmutableList<PermissionUiModel> = persistentListOf(),
    val language: LanguageUiModel? = null,
    val lastWake: WakeResultValue = WakeResultValue.UNKNOWN,
)

@Immutable
data class SettingsInteractionState(
    val settings: AodInteractionUiModel = AodInteractionUiModel(),
    val editingGesture: AodGestureValue? = null,
    val isFlashlightAvailable: Boolean = false,
    val isLightSensorAvailable: Boolean = false,
    val isPickupSensorAvailable: Boolean = false,
)

@Immutable
data class SettingsRulesState(
    val settings: AodRulesUiModel = AodRulesUiModel(),
    val isChargingRulePickerVisible: Boolean = false,
    val editingScheduleTime: ScheduleTimeValue? = null,
)
