package com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor

import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.data.device.permission.PermissionManager
import com.decoutkhanqindev.custom_aod.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.custom_aod.data.local.image.AodImageManager
import com.decoutkhanqindev.custom_aod.presentation.base.BaseViewModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.isDefault
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.observeAodThemeConfigs
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.saveAodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.customize.loadCustomizeDraft
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorEffect
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorHistoryState
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber

class ThemeEditorViewModel(
    themeId: String,
    private val dataStoreManager: DataStoreManager,
    private val aodImageManager: AodImageManager,
    private val permissionManager: PermissionManager,
) : BaseViewModel<ThemeEditorState, ThemeEditorIntent, ThemeEditorEffect>(
    initialState = ThemeEditorState(theme = AodThemeValue.fromId(themeId)),
) {

    private val undoStack = ArrayDeque<AodThemeConfig>()
    private val redoStack = ArrayDeque<AodThemeConfig>()

    // Kéo color picker hay slider: xem trước trực tiếp không ghi lịch sử, thả tay mới ghi một bước hoàn tác cho cả lần kéo.
    private var dragBase: AodThemeConfig? = null
    private var baseJob: Job? = null

    init {
        load()
        tickClock()
    }

    override fun onIntent(intent: ThemeEditorIntent) {
        Timber.tag(tag).d("onIntent: $intent")
        when (intent) {
            is ThemeEditorIntent.Edit -> onEditIntent(intent)
            is ThemeEditorIntent.Option -> onOptionIntent(intent)
            is ThemeEditorIntent.History -> onHistoryIntent(intent)
            is ThemeEditorIntent.SaveTheme -> saveTheme()
            is ThemeEditorIntent.ApplyTheme -> applyTheme()
            is ThemeEditorIntent.RefreshBase -> refreshBase()
            is ThemeEditorIntent.NavigateToCustomize ->
                viewModelScope.launch { sendEffect(ThemeEditorEffect.NavigateToCustomize) }

            is ThemeEditorIntent.NavigateBack ->
                if (state.value.isEditing) stopEditing() else viewModelScope.launch { sendEffect(ThemeEditorEffect.NavigateBack) }
        }
    }

    private fun onEditIntent(intent: ThemeEditorIntent.Edit) {
        when (intent) {
            is ThemeEditorIntent.Edit.StartEditing ->
                updateState { copy(isEditing = true, selectedTab = theme.spec.tabs.first()) }

            is ThemeEditorIntent.Edit.StopEditing -> stopEditing()
            is ThemeEditorIntent.Edit.ConfirmDiscard -> discardChanges()
            is ThemeEditorIntent.Edit.DismissDiscard ->
                updateState { copy(history = history.copy(isDiscardConfirmVisible = false)) }

            is ThemeEditorIntent.Edit.SelectTab ->
                updateState { copy(selectedTab = if (selectedTab == intent.tab) null else intent.tab) }
        }
    }

    private fun onOptionIntent(intent: ThemeEditorIntent.Option) {
        when (intent) {
            is ThemeEditorIntent.Option.PreviewOption -> {
                val current = state.value.config
                if (dragBase == null) dragBase = current
                publish(current.withRaw(intent.key, intent.raw))
            }

            is ThemeEditorIntent.Option.ChangeOption -> {
                val base = dragBase ?: state.value.config
                dragBase = null
                commit(from = base, next = base.withRaw(intent.key, intent.raw))
            }
        }
    }

    private fun onHistoryIntent(intent: ThemeEditorIntent.History) {
        when (intent) {
            is ThemeEditorIntent.History.UndoChange -> {
                val previous = undoStack.removeLastOrNull() ?: return
                redoStack.addLast(state.value.config)
                publish(previous)
            }

            is ThemeEditorIntent.History.RedoChange -> {
                val next = redoStack.removeLastOrNull() ?: return
                undoStack.addLast(state.value.config)
                publish(next)
            }

            is ThemeEditorIntent.History.ShowResetConfirm ->
                updateState { copy(history = history.copy(isResetConfirmVisible = true)) }

            is ThemeEditorIntent.History.DismissResetConfirm ->
                updateState { copy(history = history.copy(isResetConfirmVisible = false)) }

            is ThemeEditorIntent.History.ResetTheme -> {
                updateState { copy(history = history.copy(isResetConfirmVisible = false)) }
                commit(from = state.value.config, next = AodThemeConfig())
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            val base = dataStoreManager.loadCustomizeDraft(aodImageManager, permissionManager)
            val config = dataStoreManager.observeAodThemeConfigs().first().getValue(state.value.theme)
            val activeId = dataStoreManager.aodThemeId.filterNotNull().first()
            updateState {
                copy(
                    isLoading = false,
                    base = base,
                    savedConfig = config,
                    isActive = activeId == theme.id,
                )
            }
            publish(config)
        }
    }

    // Đồng hồ trên khung xem trước nhảy đúng đầu mỗi phút như AOD thật, để thấy animation đổi số.
    private fun tickClock() {
        viewModelScope.launch {
            while (isActive) {
                val nowMillis = System.currentTimeMillis()
                updateState { copy(nowMillis = nowMillis) }
                delay(MINUTE_MILLIS - nowMillis % MINUTE_MILLIS)
            }
        }
    }

    private fun stopEditing() {
        if (state.value.hasChanges) {
            updateState { copy(history = history.copy(isDiscardConfirmVisible = true)) }
        } else {
            discardChanges()
        }
    }

    private fun discardChanges() {
        clearHistory()
        updateState { copy(isEditing = false, selectedTab = null, history = history.copy(isDiscardConfirmVisible = false)) }
        publish(state.value.savedConfig)
    }

    private fun saveTheme() {
        val config = state.value.config
        dataStoreManager.saveAodThemeConfig(state.value.theme, config)
        clearHistory()
        updateState { copy(savedConfig = config, isEditing = false, selectedTab = null) }
        publish(config)
        viewModelScope.launch { sendEffect(ThemeEditorEffect.ShowMessage(R.string.theme_saved)) }
    }

    private fun applyTheme() {
        val current = state.value
        if (current.hasChanges) dataStoreManager.saveAodThemeConfig(current.theme, current.config)
        dataStoreManager.saveAodThemeId(current.theme.id)
        updateState { copy(isActive = true, savedConfig = config) }
        viewModelScope.launch { sendEffect(ThemeEditorEffect.ShowMessage(R.string.theme_applied)) }
    }

    // Phần chung đọc lại mỗi lần màn resume: quay về từ Tùy chỉnh thì khung xem trước theo phần chung mới. Lần resume đầu
    // load() đang đọc nên bỏ qua.
    private fun refreshBase() {
        if (state.value.isLoading) return
        baseJob?.cancel()
        baseJob = viewModelScope.launch {
            val base = dataStoreManager.loadCustomizeDraft(aodImageManager, permissionManager)
            updateState { copy(base = base) }
        }
    }

    // Mỗi thay đổi đẩy bản trước vào lịch sử hoàn tác; thay đổi mới thì bỏ lịch sử làm lại.
    private fun commit(from: AodThemeConfig, next: AodThemeConfig) {
        if (next == from) {
            if (state.value.config != from) publish(from)
            return
        }
        undoStack.addLast(from)
        if (undoStack.size > MAX_HISTORY) undoStack.removeFirst()
        redoStack.clear()
        publish(next)
    }

    private fun clearHistory() {
        undoStack.clear()
        redoStack.clear()
        dragBase = null
    }

    private fun publish(config: AodThemeConfig) {
        updateState {
            copy(
                config = config,
                history = ThemeEditorHistoryState(
                    canUndo = undoStack.isNotEmpty(),
                    canRedo = redoStack.isNotEmpty(),
                    isDefault = theme.spec.isDefault(config),
                    isResetConfirmVisible = history.isResetConfirmVisible,
                    isDiscardConfirmVisible = history.isDiscardConfirmVisible,
                ),
            )
        }
    }

    private companion object {
        const val MINUTE_MILLIS = 60_000L
        const val MAX_HISTORY = 50
    }
}
