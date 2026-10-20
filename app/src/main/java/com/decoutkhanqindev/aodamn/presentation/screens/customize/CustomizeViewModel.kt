package com.decoutkhanqindev.aodamn.presentation.screens.customize

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.data.device.permission.PermissionManager
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.data.local.image.AodImageManager
import com.decoutkhanqindev.aodamn.domain.usecase.RefreshWeatherUseCase
import com.decoutkhanqindev.aodamn.presentation.base.BaseViewModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.WallpaperValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.AodExtrasUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.saveAodComponents
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.isSame
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.observeAodTheme
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.saveAodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeDecorUiModel
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeGuideStepValue
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeInfoUiModel
import com.decoutkhanqindev.aodamn.presentation.model.customize.loadCustomizeDraft
import com.decoutkhanqindev.aodamn.presentation.model.customize.withTheme
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.hasRequiredPermissions
import com.decoutkhanqindev.aodamn.presentation.model.permission.requiredAodPermissions
import com.decoutkhanqindev.aodamn.presentation.screens.customize.state.CustomizeEffect
import com.decoutkhanqindev.aodamn.presentation.screens.customize.state.CustomizeIntent
import com.decoutkhanqindev.aodamn.presentation.screens.customize.state.CustomizeState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class CustomizeViewModel(
    private val isFirstOpen: Boolean,
    private val dataStoreManager: DataStoreManager,
    private val permissionManager: PermissionManager,
    private val aodImageManager: AodImageManager,
    private val refreshWeatherUseCase: RefreshWeatherUseCase,
) : BaseViewModel<CustomizeState, CustomizeIntent, CustomizeEffect>(
    initialState = CustomizeState(isFirstOpen = isFirstOpen),
) {

    private var initialDraft = CustomizeDraftUiModel()
    private val undoStack = ArrayDeque<CustomizeDraftUiModel>()
    private val redoStack = ArrayDeque<CustomizeDraftUiModel>()

    // Mục cần quyền mà user vừa bật: chỉ ghi vào bản nháp khi quyền đã được cấp.
    private var pendingChange: (CustomizeDraftUiModel.() -> CustomizeDraftUiModel)? = null
    private var glowPreviewJob: Job? = null

    // Kéo color picker (đồng hồ, kiểu khối): xem trước trực tiếp không ghi lịch sử, thả tay mới ghi một bước hoàn tác cho cả lần kéo.
    private var dragBase: CustomizeDraftUiModel? = null

    init {
        loadDraft()
        showGuideOnce()
    }

    override fun onIntent(intent: CustomizeIntent) {
        Timber.tag(tag).d("onIntent: $intent")
        when (intent) {
            is CustomizeIntent.Panel -> onPanelIntent(intent)
            is CustomizeIntent.Appearance -> onAppearanceIntent(intent)
            is CustomizeIntent.Decor -> onDecorIntent(intent)
            is CustomizeIntent.Info -> onInfoIntent(intent)
            is CustomizeIntent.Effects -> onEffectsIntent(intent)
            is CustomizeIntent.History -> onHistoryIntent(intent)
            is CustomizeIntent.Permission -> onPermissionIntent(intent)
            is CustomizeIntent.Guide -> onGuideIntent(intent)
            is CustomizeIntent.ConfirmCustomization -> confirmCustomization()
            is CustomizeIntent.SkipCustomization ->
                viewModelScope.launch { sendEffect(CustomizeEffect.Navigation.NavigateToMain) }

            is CustomizeIntent.ToggleLandscapePreview ->
                updateState { copy(isLandscapePreview = !isLandscapePreview) }

            is CustomizeIntent.NavigateBack ->
                viewModelScope.launch { sendEffect(CustomizeEffect.Navigation.NavigateBack) }
        }
    }

    // Bấm lại tab đang mở thì thu panel lại để nhìn trọn AOD.
    private fun onPanelIntent(intent: CustomizeIntent.Panel) {
        when (intent) {
            is CustomizeIntent.Panel.SelectTab ->
                updateState { copy(selectedTab = if (selectedTab == intent.tab) null else intent.tab) }

            is CustomizeIntent.Panel.ClosePanel -> updateState { copy(selectedTab = null) }
        }
    }

    private fun onAppearanceIntent(intent: CustomizeIntent.Appearance) {
        when (intent) {
            is CustomizeIntent.Appearance.ToggleDate -> editAppearance { copy(isDateEnabled = intent.isEnabled) }
            is CustomizeIntent.Appearance.ToggleBattery -> editAppearance { copy(isBatteryEnabled = intent.isEnabled) }
            is CustomizeIntent.Appearance.PreviewThemeOption -> previewOption { withThemeOption(intent.raws) }
            is CustomizeIntent.Appearance.ChangeThemeOption -> changeOption { withThemeOption(intent.raws) }
            is CustomizeIntent.Appearance.PreviewComponentOption ->
                previewOption { withComponentOption(intent.raws) }

            is CustomizeIntent.Appearance.ChangeComponentOption ->
                changeOption { withComponentOption(intent.raws) }

            is CustomizeIntent.Appearance.ResetComponentStyle -> edit {
                val clock = appearance.clock
                val options = AodComponentSpec.optionsOf(intent.component).tab.options
                copy(
                    appearance = appearance.copy(
                        clock = clock.copy(components = clock.components.copy(config = clock.components.config.without(options))),
                    ),
                )
            }

            is CustomizeIntent.Appearance.ResetClockStyle -> edit {
                withTheme(appearance.clock.theme.copy(config = AodThemeConfig()))
            }

            is CustomizeIntent.Appearance.OpenThemeEditor -> viewModelScope.launch {
                val themeId = state.value.draft.appearance.clock.theme.value.id
                sendEffect(CustomizeEffect.Navigation.NavigateToThemeEditor(themeId, focusBlock = intent.block))
            }

            is CustomizeIntent.Appearance.RefreshTheme -> refreshTheme()
        }
    }

    private fun onDecorIntent(intent: CustomizeIntent.Decor) {
        when (intent) {
            is CustomizeIntent.Decor.SelectWallpaper -> editDecor {
                copy(
                    wallpaper = intent.wallpaper,
                    background = null
                )
            }

            is CustomizeIntent.Decor.OpenBackgroundPicker ->
                viewModelScope.launch { sendEffect(CustomizeEffect.Decor.OpenBackgroundPicker) }

            is CustomizeIntent.Decor.BackgroundPickerResult -> onBackgroundPickerResult(intent.uri)
            is CustomizeIntent.Decor.RemoveBackground -> editDecor {
                copy(
                    wallpaper = null,
                    background = null
                )
            }

            is CustomizeIntent.Decor.ShowDrawingPad -> updateState {
                copy(
                    decor = decor.copy(
                        isDrawingPadVisible = true
                    )
                )
            }

            is CustomizeIntent.Decor.DismissDrawingPad ->
                updateState { copy(decor = decor.copy(isDrawingPadVisible = false)) }

            is CustomizeIntent.Decor.ChangeDrawing -> {
                updateState { copy(decor = decor.copy(isDrawingPadVisible = false)) }
                editDecor { copy(drawing = intent.drawing) }
            }

            is CustomizeIntent.Decor.RemoveDrawing -> editDecor { copy(drawing = null) }
            is CustomizeIntent.Decor.ShowMemoEditor -> updateState {
                copy(
                    decor = decor.copy(
                        isMemoEditorVisible = true
                    )
                )
            }

            is CustomizeIntent.Decor.DismissMemoEditor ->
                updateState { copy(decor = decor.copy(isMemoEditorVisible = false)) }

            is CustomizeIntent.Decor.ChangeMemo -> {
                updateState { copy(decor = decor.copy(isMemoEditorVisible = false)) }
                editDecor { copy(memo = intent.memo.trim().take(AodExtrasUiModel.MEMO_MAX_LENGTH)) }
            }
        }
    }

    private fun onInfoIntent(intent: CustomizeIntent.Info) {
        when (intent) {
            is CustomizeIntent.Info.ToggleNotificationIcons ->
                toggleInfo(PermissionValue.NOTIFICATION_ACCESS, intent.isEnabled) {
                    copy(
                        isNotificationIconsEnabled = it
                    )
                }

            is CustomizeIntent.Info.ToggleNotificationContent ->
                toggleInfo(PermissionValue.NOTIFICATION_ACCESS, intent.isEnabled) {
                    copy(isNotificationContentEnabled = it)
                }

            is CustomizeIntent.Info.ToggleCalendar ->
                toggleInfo(
                    PermissionValue.CALENDAR,
                    intent.isEnabled
                ) { copy(isCalendarEnabled = it) }

            is CustomizeIntent.Info.ToggleWeather ->
                toggleInfo(
                    PermissionValue.LOCATION,
                    intent.isEnabled
                ) { copy(isWeatherEnabled = it) }

            is CustomizeIntent.Info.ToggleWeatherFahrenheit ->
                edit { copy(info = info.copy(isWeatherFahrenheit = intent.isEnabled)) }

            is CustomizeIntent.Info.ToggleMediaControls ->
                toggleInfo(PermissionValue.NOTIFICATION_ACCESS, intent.isEnabled) {
                    copy(
                        isMediaControlsEnabled = it
                    )
                }
        }
    }

    private fun onEffectsIntent(intent: CustomizeIntent.Effects) {
        when (intent) {
            is CustomizeIntent.Effects.ToggleEdgeGlow ->
                toggleWithPermission(
                    PermissionValue.NOTIFICATION_ACCESS,
                    intent.isEnabled
                ) { isEnabled ->
                    copy(effects = effects.copy(isEdgeGlowEnabled = isEnabled))
                }

            is CustomizeIntent.Effects.PreviewEdgeGlow -> previewEdgeGlow()
        }
    }

    private fun onHistoryIntent(intent: CustomizeIntent.History) {
        when (intent) {
            is CustomizeIntent.History.UndoChange -> undo()
            is CustomizeIntent.History.RedoChange -> redo()
            is CustomizeIntent.History.ShowResetConfirm ->
                updateState { copy(history = history.copy(isResetConfirmVisible = true)) }

            is CustomizeIntent.History.DismissResetConfirm ->
                updateState { copy(history = history.copy(isResetConfirmVisible = false)) }

            is CustomizeIntent.History.ResetCustomization -> {
                updateState { copy(history = history.copy(isResetConfirmVisible = false)) }
                edit { defaultDraft() }
            }
        }
    }

    private fun onPermissionIntent(intent: CustomizeIntent.Permission) {
        when (intent) {
            is CustomizeIntent.Permission.OpenPermissionSettings -> openPermissionSettings(intent.permission)
            is CustomizeIntent.Permission.DismissPermissionSheet -> dismissPermissionSheet()
            is CustomizeIntent.Permission.DismissApplyPermissionSheet ->
                updateState { copy(isApplyPermissionSheetVisible = false) }

            is CustomizeIntent.Permission.NotificationPermissionResult -> onNotificationPermissionResult(intent.isGranted)
            is CustomizeIntent.Permission.CalendarPermissionResult ->
                onPermissionResult(PermissionValue.CALENDAR, intent.isGranted)

            is CustomizeIntent.Permission.LocationPermissionResult ->
                onPermissionResult(PermissionValue.LOCATION, intent.isGranted)

            is CustomizeIntent.Permission.RefreshPermissions -> refreshPermissions()
        }
    }

    private fun onGuideIntent(intent: CustomizeIntent.Guide) {
        when (intent) {
            is CustomizeIntent.Guide.ShowGuide ->
                updateState {
                    copy(
                        guideStep = CustomizeGuideStepValue.entries.first(),
                        selectedTab = null
                    )
                }

            is CustomizeIntent.Guide.ShowNextGuideStep -> {
                val next = state.value.guideStep?.next
                if (next == null) dismissGuide() else updateState { copy(guideStep = next) }
            }

            is CustomizeIntent.Guide.DismissGuide -> dismissGuide()
        }
    }

    // Bản nháp bắt đầu từ cài đặt đang có (lần đầu mở app là mặc định).
    private fun loadDraft() {
        viewModelScope.launch {
            initialDraft = dataStoreManager.loadCustomizeDraft(aodImageManager, permissionManager)
            undoStack.clear()
            redoStack.clear()
            publish(initialDraft)
            updateState { copy(isLoading = false) }
        }
    }

    private fun showGuideOnce() {
        viewModelScope.launch {
            if (dataStoreManager.isCustomizeGuideShown.filterNotNull().first()) return@launch
            updateState { copy(guideStep = CustomizeGuideStepValue.entries.first()) }
        }
    }

    private fun editAppearance(transform: CustomizeAppearanceUiModel.() -> CustomizeAppearanceUiModel) =
        edit { copy(appearance = appearance.transform()) }

    private fun editDecor(transform: CustomizeDecorUiModel.() -> CustomizeDecorUiModel) =
        edit { copy(decor = decor.transform()) }

    private fun edit(transform: CustomizeDraftUiModel.() -> CustomizeDraftUiModel) {
        val current = state.value.draft
        commit(from = current, next = current.transform())
    }

    // Mỗi thay đổi đẩy bản trước vào lịch sử hoàn tác; thay đổi mới thì bỏ lịch sử làm lại. from khác bản đang hiện khi vừa kéo xem
    // trước: kéo về đúng chỗ cũ thì chỉ trả bản nháp về from.
    private fun commit(from: CustomizeDraftUiModel, next: CustomizeDraftUiModel) {
        if (next == from) {
            if (state.value.draft != from) publish(from)
            return
        }
        undoStack.addLast(from)
        if (undoStack.size > MAX_HISTORY) undoStack.removeFirst()
        redoStack.clear()
        publish(next)
    }

    // Ngoài onboarding không có tab Đồng hồ: đặt lại giữ chủ đề đang dùng, nếu không Áp dụng sẽ âm thầm đổi chủ đề.
    private fun defaultDraft(): CustomizeDraftUiModel =
        if (isFirstOpen) CustomizeDraftUiModel() else CustomizeDraftUiModel().withTheme(initialDraft.appearance.clock.theme)

    private fun previewOption(transform: CustomizeDraftUiModel.() -> CustomizeDraftUiModel) {
        val current = state.value.draft
        if (dragBase == null) dragBase = current
        publish(current.transform())
    }

    private fun changeOption(transform: CustomizeDraftUiModel.() -> CustomizeDraftUiModel) {
        val base = dragBase ?: state.value.draft
        dragBase = null
        commit(from = base, next = base.transform())
    }

    // Chủ đề đổi ở màn chủ đề thì bản nháp, lịch sử hoàn tác và bản gốc đều lấy chủ đề mới, để Áp dụng không ghi đè lại chủ đề cũ.
    // Ngoài onboarding Tùy chỉnh không tự đổi chủ đề nên bản nháp và bản gốc luôn cùng chủ đề.
    private fun refreshTheme() {
        if (isFirstOpen || state.value.isLoading) return
        viewModelScope.launch {
            val theme = dataStoreManager.observeAodTheme().first()
            if (theme == initialDraft.appearance.clock.theme) return@launch
            initialDraft = initialDraft.withTheme(theme)
            val undone = undoStack.map { draft -> draft.withTheme(theme) }
            val redone = redoStack.map { draft -> draft.withTheme(theme) }
            undoStack.clear()
            undoStack.addAll(undone)
            redoStack.clear()
            redoStack.addAll(redone)
            publish(state.value.draft.withTheme(theme))
            if (state.value.selectedTab?.isShownIn(theme.value.spec) == false) updateState { copy(selectedTab = null) }
        }
    }

    private fun CustomizeDraftUiModel.withThemeOption(raws: Map<String, Int?>): CustomizeDraftUiModel {
        val theme = appearance.clock.theme
        return withTheme(theme.copy(config = theme.config.withRaws(raws)))
    }

    private fun CustomizeDraftUiModel.withComponentOption(raws: Map<String, Int?>): CustomizeDraftUiModel {
        val clock = appearance.clock
        val components = clock.components
        return copy(
            appearance = appearance.copy(
                clock = clock.copy(components = components.copy(config = components.config.withRaws(raws))),
            ),
        )
    }

    private fun undo() {
        val previous = undoStack.removeLastOrNull() ?: return
        redoStack.addLast(state.value.draft)
        publish(previous)
    }

    private fun redo() {
        val next = redoStack.removeLastOrNull() ?: return
        undoStack.addLast(state.value.draft)
        publish(next)
    }

    private fun publish(draft: CustomizeDraftUiModel) {
        updateState {
            copy(
                draft = draft,
                history = history.copy(
                    hasChanges = draft != initialDraft,
                    isDefault = draft == defaultDraft(),
                    canUndo = undoStack.isNotEmpty(),
                    canRedo = redoStack.isNotEmpty(),
                ),
            )
        }
    }

    // Ảnh giữ trong bản nháp (chưa ghi file); giải mã ở IO vì ảnh gốc có thể rất lớn.
    private fun onBackgroundPickerResult(uri: String?) {
        if (uri == null) return
        viewModelScope.launch {
            updateState { copy(decor = decor.copy(isLoadingBackground = true)) }
            val background = aodImageManager.decodeBackground(uri)
            updateState { copy(decor = decor.copy(isLoadingBackground = false)) }
            if (background == null) {
                sendEffect(CustomizeEffect.ShowMessage(R.string.background_save_failed, isError = true))
            } else {
                editDecor { copy(background = background, wallpaper = null) }
            }
        }
    }

    private fun toggleInfo(
        permission: PermissionValue,
        isEnabled: Boolean,
        change: CustomizeInfoUiModel.(Boolean) -> CustomizeInfoUiModel,
    ) = toggleWithPermission(permission, isEnabled) { enabled -> copy(info = info.change(enabled)) }

    // Tắt thì ghi ngay; bật khi chưa có quyền thì hiện sheet quyền, cấp xong mới bật.
    private fun toggleWithPermission(
        permission: PermissionValue,
        isEnabled: Boolean,
        change: CustomizeDraftUiModel.(Boolean) -> CustomizeDraftUiModel,
    ) {
        if (!isEnabled || isGranted(permission)) {
            edit { change(isEnabled) }
            return
        }
        pendingChange = { change(true) }
        updateState { copy(permissionRequest = permission) }
    }

    private fun openPermissionSettings(permission: PermissionValue) {
        val effect = when (permission) {
            PermissionValue.OVERLAY -> CustomizeEffect.Permission.OpenOverlaySettings
            PermissionValue.MIUI_LOCK_SCREEN,
            PermissionValue.MIUI_BACKGROUND_POPUP -> CustomizeEffect.Permission.OpenMiuiPermissionSettings(permission)

            PermissionValue.NOTIFICATIONS ->
                if (permissionManager.needsNotificationPermission()) {
                    CustomizeEffect.Permission.RequestNotificationPermission
                } else {
                    CustomizeEffect.Permission.OpenNotificationSettings
                }

            PermissionValue.CALENDAR -> CustomizeEffect.Permission.RequestCalendarPermission
            PermissionValue.LOCATION -> CustomizeEffect.Permission.RequestLocationPermission
            PermissionValue.NOTIFICATION_ACCESS -> CustomizeEffect.Permission.OpenNotificationAccessSettings
        }
        viewModelScope.launch { sendEffect(effect) }
    }

    // Bị từ chối thì mở Thông tin ứng dụng (từ chối hai lần thì hệ thống không hỏi nữa), cấp xong app tự quay về và refreshPermissions
    // bật mục đang chờ; sheet quyền giữ nguyên trong lúc đó.
    private fun onPermissionResult(permission: PermissionValue, isGranted: Boolean) {
        if (isGranted) {
            applyPendingChange()
        } else {
            viewModelScope.launch { sendEffect(CustomizeEffect.Permission.OpenAppSettings(permission)) }
        }
    }

    // Quyền cấp ở app Cài đặt (truy cập thông báo, lịch / vị trí đã bị từ chối, quyền bắt buộc lúc áp dụng): quay lại màn này thì kiểm lại.
    private fun refreshPermissions() {
        state.value.permissionRequest?.let { permission -> if (isGranted(permission)) applyPendingChange() }
        if (state.value.isApplyPermissionSheetVisible) refreshApplyPermissions()
    }

    // Đủ quyền bắt buộc thì đóng sheet và áp dụng luôn, như user vừa bấm lại Áp dụng.
    private fun refreshApplyPermissions() {
        val required = permissionManager.requiredAodPermissions()
        if (required.hasRequiredPermissions) {
            updateState { copy(requiredPermissions = required, isApplyPermissionSheetVisible = false) }
            confirmCustomization()
        } else {
            updateState { copy(requiredPermissions = required) }
        }
    }

    // Bị từ chối thì mở trang cài đặt thông báo (sau 2 lần từ chối hệ thống không hiện hộp thoại nữa).
    private fun onNotificationPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            refreshPermissions()
        } else {
            viewModelScope.launch { sendEffect(CustomizeEffect.Permission.OpenNotificationSettings) }
        }
    }

    private fun applyPendingChange() {
        pendingChange?.let { change -> edit(change) }
        dismissPermissionSheet()
    }

    private fun dismissPermissionSheet() {
        pendingChange = null
        updateState { copy(permissionRequest = null) }
    }

    private fun isGranted(permission: PermissionValue): Boolean = when (permission) {
        PermissionValue.NOTIFICATION_ACCESS -> permissionManager.isNotificationListenerEnabled()
        PermissionValue.CALENDAR -> permissionManager.hasCalendarPermission()
        PermissionValue.LOCATION -> permissionManager.hasCoarseLocationPermission()
        else -> true
    }

    private fun previewEdgeGlow() {
        glowPreviewJob?.cancel()
        glowPreviewJob = viewModelScope.launch {
            updateState { copy(effects = effects.copy(isGlowPreviewing = true)) }
            delay(GLOW_PREVIEW_MILLIS)
            updateState { copy(effects = effects.copy(isGlowPreviewing = false)) }
        }
    }

    private fun dismissGuide() {
        dataStoreManager.saveIsCustomizeGuideShown(true)
        updateState { copy(guideStep = null) }
    }

    // Chỉ bấm được khi bản nháp đã khác lúc vào màn; ghi phần đã đổi. Mở từ màn chính thì ở lại màn và báo toast, bản vừa ghi thành bản
    // gốc mới (ảnh ghi lỗi thì giữ bản gốc cũ để Áp dụng lại được); còn thiếu quyền bắt buộc thì chưa ghi gì, hiện sheet quyền, cấp đủ
    // thì tự áp dụng (refreshApplyPermissions). Onboarding thì sang bước sau (navigateNext).
    private fun confirmCustomization() {
        if (state.value.isApplying || !state.value.history.hasChanges) return
        if (!isFirstOpen) {
            val required = permissionManager.requiredAodPermissions()
            if (!required.hasRequiredPermissions) {
                updateState { copy(requiredPermissions = required, isApplyPermissionSheetVisible = true) }
                return
            }
        }
        viewModelScope.launch {
            updateState { copy(isApplying = true, selectedTab = null) }
            val draft = state.value.draft
            val isSaved = save(draft, initialDraft)
            if (isFirstOpen) {
                navigateNext()
            } else if (isSaved) {
                initialDraft = draft
                undoStack.clear()
                redoStack.clear()
                publish(draft)
                sendEffect(CustomizeEffect.ShowMessage(R.string.customize_applied))
            }
            updateState { copy(isApplying = false) }
        }
    }

    // Onboarding: đủ quyền bắt buộc thì vào thẳng Main, không thì sang màn quyền. Bỏ qua không đi qua đây: không ghi gì và vào thẳng Main
    // (thiếu quyền thì Main hiện bottom sheet nhắc), vì user đã muốn bỏ qua mà còn bắt cấp quyền thì dễ bỏ app.
    private suspend fun navigateNext() {
        val isReady = permissionManager.requiredAodPermissions().hasRequiredPermissions
        sendEffect(if (isReady) CustomizeEffect.Navigation.NavigateToMain else CustomizeEffect.Navigation.NavigateToPermission)
    }

    // Chỉ ghi phần đã đổi: mục cần quyền user không đụng tới giữ cờ đã lưu (vd icon thông báo mặc định bật nhưng chưa có quyền).
    // false khi ảnh nền hay nét vẽ ghi lỗi (đã báo toast lỗi).
    private suspend fun save(draft: CustomizeDraftUiModel, initial: CustomizeDraftUiModel): Boolean {
        saveAppearance(draft.appearance, initial.appearance)
        saveInfo(draft.info, initial.info)
        if (draft.effects.isEdgeGlowEnabled != initial.effects.isEdgeGlowEnabled) {
            dataStoreManager.saveIsAodEdgeGlowEnabled(draft.effects.isEdgeGlowEnabled)
        }
        if (draft.decor.memo != initial.decor.memo) dataStoreManager.saveAodMemo(draft.decor.memo)
        val isBackgroundSaved = saveBackground(draft.decor, initial.decor)
        val isDrawingSaved = saveDrawing(draft.decor.drawing, initial.decor.drawing)
        return isBackgroundSaved && isDrawingSaved
    }

    private fun saveAppearance(
        draft: CustomizeAppearanceUiModel,
        initial: CustomizeAppearanceUiModel
    ) {
        val chosen = draft.clock.theme
        val previous = initial.clock.theme
        with(dataStoreManager) {
            if (chosen.value != previous.value || !chosen.value.spec.isSame(chosen.config, previous.config)) {
                saveAodThemeConfig(chosen.value, chosen.config)
            }
            if (chosen.value != previous.value) saveAodThemeId(chosen.value.id)
            if (!AodComponentSpec.isSame(draft.clock.components.config, initial.clock.components.config)) {
                saveAodComponents(draft.clock.components)
            }
            if (draft.isDateEnabled != initial.isDateEnabled) saveIsAodDateEnabled(draft.isDateEnabled)
            if (draft.isBatteryEnabled != initial.isBatteryEnabled) saveIsAodBatteryEnabled(draft.isBatteryEnabled)
        }
    }

    private suspend fun saveInfo(draft: CustomizeInfoUiModel, initial: CustomizeInfoUiModel) {
        with(dataStoreManager) {
            if (draft.isNotificationIconsEnabled != initial.isNotificationIconsEnabled) {
                saveIsAodNotificationIconsEnabled(draft.isNotificationIconsEnabled)
            }
            if (draft.isNotificationContentEnabled != initial.isNotificationContentEnabled) {
                saveIsAodNotificationContentEnabled(draft.isNotificationContentEnabled)
            }
            if (draft.isMediaControlsEnabled != initial.isMediaControlsEnabled) {
                saveIsAodMediaControlsEnabled(draft.isMediaControlsEnabled)
            }
            if (draft.isCalendarEnabled != initial.isCalendarEnabled) saveIsAodCalendarEnabled(draft.isCalendarEnabled)
            if (draft.isWeatherFahrenheit != initial.isWeatherFahrenheit) {
                saveIsAodWeatherFahrenheit(draft.isWeatherFahrenheit)
            }
            if (draft.isWeatherEnabled != initial.isWeatherEnabled) saveIsAodWeatherEnabled(draft.isWeatherEnabled)
        }
        if (draft.isWeatherEnabled && !initial.isWeatherEnabled) {
            refreshWeatherUseCase(nowMillis = System.currentTimeMillis(), isForced = true)
                .onFailure { e -> Timber.tag(tag).w("Could not refresh the weather: ${e.message}") }
        }
    }

    private suspend fun saveBackground(
        draft: CustomizeDecorUiModel,
        initial: CustomizeDecorUiModel
    ): Boolean {
        if (draft.wallpaper == initial.wallpaper && draft.background === initial.background) return true
        val background = draft.background
        val wallpaper = draft.wallpaper
        when {
            background != null -> {
                if (!aodImageManager.saveBackground(background)) {
                    sendEffect(CustomizeEffect.ShowMessage(R.string.background_save_failed, isError = true))
                    return false
                }
                dataStoreManager.saveAodWallpaper(WallpaperValue.NONE_CODE)
            }

            wallpaper != null -> {
                dataStoreManager.saveAodWallpaper(wallpaper.code)
                aodImageManager.removeBackground()
            }

            else -> {
                dataStoreManager.saveAodWallpaper(WallpaperValue.NONE_CODE)
                aodImageManager.removeBackground()
            }
        }
        return true
    }

    private suspend fun saveDrawing(drawing: Bitmap?, initialDrawing: Bitmap?): Boolean {
        if (drawing === initialDrawing) return true
        if (drawing == null) {
            aodImageManager.removeDrawing()
        } else if (!aodImageManager.saveDrawing(drawing)) {
            sendEffect(CustomizeEffect.ShowMessage(R.string.drawing_save_failed, isError = true))
            return false
        }
        return true
    }

    private companion object {
        const val GLOW_PREVIEW_MILLIS = 3_000L
        const val MAX_HISTORY = 50
    }
}
