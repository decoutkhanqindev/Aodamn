package com.decoutkhanqindev.custom_aod.presentation.screens.settings

import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.custom_aod.data.device.flashlight.FlashlightManager
import com.decoutkhanqindev.custom_aod.data.device.light.AmbientLightManager
import com.decoutkhanqindev.custom_aod.data.device.permission.PermissionManager
import com.decoutkhanqindev.custom_aod.data.device.pickup.PickupGestureManager
import com.decoutkhanqindev.custom_aod.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.custom_aod.data.local.locale.LanguageManager
import com.decoutkhanqindev.custom_aod.presentation.base.BaseViewModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.observeAodAppearance
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.WakeResultValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.AodActionValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.AodGestureValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.AodInteractionUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.AodOptionsUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.AodRulesUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.ChargingRuleValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.ScheduleTimeValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.observeAodInteraction
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.observeAodOptions
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.observeAodRules
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.saveGestureAction
import com.decoutkhanqindev.custom_aod.presentation.model.language.LanguageValue
import com.decoutkhanqindev.custom_aod.presentation.model.language.toUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.aodPermissions
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsEffect
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsInteractionState
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsState
import com.decoutkhanqindev.custom_aod.utils.collectCatching
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsViewModel(
    private val dataStoreManager: DataStoreManager,
    private val permissionManager: PermissionManager,
    private val languageManager: LanguageManager,
    flashlightManager: FlashlightManager,
    ambientLightManager: AmbientLightManager,
    pickupGestureManager: PickupGestureManager,
) : BaseViewModel<SettingsState, SettingsIntent, SettingsEffect>(
    initialState = SettingsState(
        interaction = SettingsInteractionState(
            isFlashlightAvailable = flashlightManager.isAvailable,
            isLightSensorAvailable = ambientLightManager.isAvailable,
            isPickupSensorAvailable = pickupGestureManager.isSupported,
        ),
    ),
) {

    init {
        observeSettings()
        observeLanguage()
        observeLastWake()
    }

    override fun onIntent(intent: SettingsIntent) {
        Timber.tag(tag).d("onIntent: $intent")
        when (intent) {
            is SettingsIntent.Options -> onOptionsIntent(intent)
            is SettingsIntent.Interaction -> onInteractionIntent(intent)
            is SettingsIntent.Rules -> onRulesIntent(intent)
            is SettingsIntent.Permission -> onPermissionIntent(intent)
            is SettingsIntent.NavigateToLanguage ->
                viewModelScope.launch { sendEffect(SettingsEffect.NavigateToLanguage) }
        }
    }

    private fun onOptionsIntent(intent: SettingsIntent.Options) {
        when (intent) {
            is SettingsIntent.Options.ToggleCustomBrightness -> dataStoreManager.saveIsAodCustomBrightness(
                intent.isEnabled
            )

            is SettingsIntent.Options.ChangeBrightness -> dataStoreManager.saveAodBrightnessPercent(
                intent.percent
            )

            is SettingsIntent.Options.ToggleProximity -> dataStoreManager.saveIsAodProximityEnabled(
                intent.isEnabled
            )

            is SettingsIntent.Options.ChangeTimeout -> dataStoreManager.saveAodTimeoutMinutes(intent.minutes)
            is SettingsIntent.Options.ToggleLandscape -> dataStoreManager.saveIsAodLandscape(intent.isEnabled)
        }
    }

    private fun onInteractionIntent(intent: SettingsIntent.Interaction) {
        when (intent) {
            is SettingsIntent.Interaction.ShowGestureActionPicker ->
                updateState { copy(interaction = interaction.copy(editingGesture = intent.gesture)) }

            is SettingsIntent.Interaction.DismissGestureActionPicker ->
                updateState { copy(interaction = interaction.copy(editingGesture = null)) }

            is SettingsIntent.Interaction.ChangeGestureAction -> changeGestureAction(
                intent.gesture,
                intent.action
            )

            is SettingsIntent.Interaction.ToggleAutoDim -> dataStoreManager.saveIsAodAutoDimEnabled(
                intent.isEnabled
            )

            is SettingsIntent.Interaction.ToggleRaiseToWake -> dataStoreManager.saveIsAodRaiseToWakeEnabled(
                intent.isEnabled
            )
        }
    }

    private fun onRulesIntent(intent: SettingsIntent.Rules) {
        when (intent) {
            is SettingsIntent.Rules.ShowChargingRulePicker ->
                updateState { copy(rules = rules.copy(isChargingRulePickerVisible = true)) }

            is SettingsIntent.Rules.DismissChargingRulePicker ->
                updateState { copy(rules = rules.copy(isChargingRulePickerVisible = false)) }

            is SettingsIntent.Rules.ChangeChargingRule -> changeChargingRule(intent.rule)
            is SettingsIntent.Rules.ToggleSchedule -> dataStoreManager.saveIsAodScheduleEnabled(intent.isEnabled)
            is SettingsIntent.Rules.ShowScheduleTimePicker ->
                updateState { copy(rules = rules.copy(editingScheduleTime = intent.time)) }

            is SettingsIntent.Rules.DismissScheduleTimePicker ->
                updateState { copy(rules = rules.copy(editingScheduleTime = null)) }

            is SettingsIntent.Rules.ChangeScheduleTime -> changeScheduleTime(
                intent.time,
                intent.minuteOfDay
            )

            is SettingsIntent.Rules.ChangeMinBattery -> dataStoreManager.saveAodMinBattery(intent.percent)
        }
    }

    private fun onPermissionIntent(intent: SettingsIntent.Permission) {
        when (intent) {
            is SettingsIntent.Permission.OpenPermissionSettings -> openPermissionSettings(intent.permission)
            is SettingsIntent.Permission.RefreshPermissions -> refreshPermissions()
            is SettingsIntent.Permission.NotificationPermissionResult -> onNotificationPermissionResult(
                intent.isGranted
            )

            is SettingsIntent.Permission.CalendarPermissionResult ->
                onRuntimePermissionResult(PermissionValue.CALENDAR, intent.isGranted)

            is SettingsIntent.Permission.LocationPermissionResult ->
                onRuntimePermissionResult(PermissionValue.LOCATION, intent.isGranted)
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            combine(
                dataStoreManager.observeAodOptions(),
                dataStoreManager.observeAodAppearance(),
                dataStoreManager.observeAodInteraction(),
                dataStoreManager.observeAodRules(),
            ) { options, appearance, interaction, rules ->
                Settings(
                    options = options,
                    appearance = appearance,
                    interaction = interaction,
                    rules = rules,
                )
            }.collectCatching(
                block = { settings ->
                    updateState {
                        copy(
                            isLoading = false,
                            options = settings.options,
                            appearance = settings.appearance,
                            interaction = interaction.copy(settings = settings.interaction),
                            rules = rules.copy(settings = settings.rules),
                        )
                    }
                },
                catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
            )
        }
    }

    private fun observeLanguage() {
        viewModelScope.launch {
            dataStoreManager.selectedLangCode.filterNotNull().collectCatching(
                block = { code ->
                    updateState {
                        copy(
                            language = LanguageValue.fromCode(code).toUiModel(languageManager)
                        )
                    }
                },
                catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
            )
        }
    }

    private fun observeLastWake() {
        viewModelScope.launch {
            dataStoreManager.aodLastWake.filterNotNull().collectCatching(
                block = { code -> updateState { copy(lastWake = WakeResultValue.fromCode(code)) } },
                catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
            )
        }
    }

    // Trạng thái quyền đổi ở app Cài đặt chứ không phải ở đây, nên đọc lại mỗi lần màn hình resume.
    private fun refreshPermissions() {
        updateState { copy(permissions = permissionManager.aodPermissions()) }
    }

    private fun changeGestureAction(gesture: AodGestureValue, action: AodActionValue) {
        dataStoreManager.saveGestureAction(gesture, action)
        updateState { copy(interaction = interaction.copy(editingGesture = null)) }
    }

    private fun changeChargingRule(rule: ChargingRuleValue) {
        dataStoreManager.saveAodChargingRule(rule.code)
        updateState { copy(rules = rules.copy(isChargingRulePickerVisible = false)) }
    }

    private fun changeScheduleTime(time: ScheduleTimeValue, minuteOfDay: Int) {
        when (time) {
            ScheduleTimeValue.START -> dataStoreManager.saveAodScheduleStartMinute(minuteOfDay)
            ScheduleTimeValue.END -> dataStoreManager.saveAodScheduleEndMinute(minuteOfDay)
        }
        updateState { copy(rules = rules.copy(editingScheduleTime = null)) }
    }

    private fun openPermissionSettings(permission: PermissionValue) {
        val effect = when (permission) {
            PermissionValue.OVERLAY -> SettingsEffect.Permission.OpenOverlaySettings
            PermissionValue.MIUI_LOCK_SCREEN,
            PermissionValue.MIUI_BACKGROUND_POPUP -> SettingsEffect.Permission.OpenMiuiPermissionSettings(permission)

            PermissionValue.NOTIFICATIONS ->
                if (permissionManager.needsNotificationPermission()) {
                    SettingsEffect.Permission.RequestNotificationPermission
                } else {
                    SettingsEffect.Permission.OpenNotificationSettings
                }

            PermissionValue.NOTIFICATION_ACCESS -> SettingsEffect.Permission.OpenNotificationAccessSettings
            PermissionValue.CALENDAR ->
                if (permissionManager.hasCalendarPermission()) {
                    SettingsEffect.Permission.OpenAppSettings(PermissionValue.CALENDAR)
                } else {
                    SettingsEffect.Permission.RequestCalendarPermission
                }

            PermissionValue.LOCATION ->
                if (permissionManager.hasCoarseLocationPermission()) {
                    SettingsEffect.Permission.OpenAppSettings(PermissionValue.LOCATION)
                } else {
                    SettingsEffect.Permission.RequestLocationPermission
                }
        }
        viewModelScope.launch { sendEffect(effect) }
    }

    // Đã cho phép thì start lại service để nó đăng lại thông báo; bị từ chối thì mở trang cài đặt thông báo (sau 2 lần từ chối hệ thống không hiện hộp thoại nữa).
    private fun onNotificationPermissionResult(isGranted: Boolean) {
        refreshPermissions()
        viewModelScope.launch {
            when {
                isGranted -> if (dataStoreManager.isAodEnabled.value == true) sendEffect(SettingsEffect.StartAodService)
                else -> sendEffect(SettingsEffect.Permission.OpenNotificationSettings)
            }
        }
    }

    // Lịch / vị trí hỏi từ danh sách quyền chỉ để cấp, bật "Sự kiện hôm nay" / "Thời tiết" ở màn Customize. Bị từ chối thì mở
    // Thông tin ứng dụng, vì từ chối hai lần thì hệ thống không hỏi nữa.
    private fun onRuntimePermissionResult(permission: PermissionValue, isGranted: Boolean) {
        refreshPermissions()
        if (!isGranted) viewModelScope.launch { sendEffect(SettingsEffect.Permission.OpenAppSettings(permission)) }
    }

    // Gom mọi nhóm cài đặt vào một lần cập nhật: màn hình chỉ hết loading khi tất cả đã đọc xong.
    private data class Settings(
        val options: AodOptionsUiModel,
        val appearance: AodAppearanceUiModel,
        val interaction: AodInteractionUiModel,
        val rules: AodRulesUiModel,
    )
}
