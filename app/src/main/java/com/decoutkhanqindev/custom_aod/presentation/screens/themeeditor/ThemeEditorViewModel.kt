package com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor

import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.custom_aod.presentation.base.BaseViewModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.isDefault
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.observeAodThemeConfigs
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.saveAodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorEffect
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorHistoryState
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorState
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class ThemeEditorViewModel(
    themeId: String,
    private val dataStoreManager: DataStoreManager,
) : BaseViewModel<ThemeEditorState, ThemeEditorIntent, ThemeEditorEffect>(
    initialState = ThemeEditorState(theme = AodThemeValue.fromId(themeId)),
) {

    private val undoStack = ArrayDeque<AodThemeConfig>()
    private val redoStack = ArrayDeque<AodThemeConfig>()

    // Kéo color picker hay slider: xem trước trực tiếp không ghi lịch sử, thả tay mới ghi một bước hoàn tác cho cả lần kéo.
    private var dragBase: AodThemeConfig? = null

    init {
        load()
    }

    override fun onIntent(intent: ThemeEditorIntent) {
        Timber.tag(tag).d("onIntent: $intent")
        when (intent) {
            is ThemeEditorIntent.Edit -> onEditIntent(intent)
            is ThemeEditorIntent.Option -> onOptionIntent(intent)
            is ThemeEditorIntent.History -> onHistoryIntent(intent)
            is ThemeEditorIntent.SaveTheme -> saveTheme()
            is ThemeEditorIntent.ApplyTheme -> applyTheme()
            is ThemeEditorIntent.NavigateToCustomize ->
                viewModelScope.launch { sendEffect(ThemeEditorEffect.NavigateToCustomize) }

            is ThemeEditorIntent.NavigateBack -> when {
                state.value.isApplying -> Unit
                state.value.isEditing -> stopEditing()
                else -> viewModelScope.launch { sendEffect(ThemeEditorEffect.NavigateBack) }
            }
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

            is ThemeEditorIntent.Edit.ClosePanel -> updateState { copy(selectedTab = null) }
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

            is ThemeEditorIntent.Option.ResetTab -> {
                dragBase = null
                val current = state.value.config
                commit(from = current, next = current.without(intent.tab.options))
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
            val config = dataStoreManager.observeAodThemeConfigs().first().getValue(state.value.theme)
            val activeId = dataStoreManager.aodThemeId.filterNotNull().first()
            updateState {
                copy(
                    isLoading = false,
                    savedConfig = config,
                    isActive = activeId == theme.id,
                )
            }
            publish(config)
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

    // Chờ ghi xong dưới lớp phủ đang áp dụng (chặn chạm và Back) rồi mới thoát chế độ sửa.
    private fun saveTheme() {
        if (state.value.isApplying) return
        val config = state.value.config
        viewModelScope.launch {
            updateState { copy(isApplying = true, selectedTab = null) }
            dataStoreManager.saveAodThemeConfig(state.value.theme, config).join()
            clearHistory()
            updateState { copy(isApplying = false, savedConfig = config, isEditing = false) }
            publish(config)
            sendEffect(ThemeEditorEffect.ShowMessage(R.string.theme_saved))
        }
    }

    private fun applyTheme() {
        val current = state.value
        if (current.isApplying) return
        viewModelScope.launch {
            updateState { copy(isApplying = true) }
            listOfNotNull(
                dataStoreManager.saveAodThemeConfig(current.theme, current.config).takeIf { current.hasChanges },
                dataStoreManager.saveAodThemeId(current.theme.id),
            ).joinAll()
            updateState { copy(isApplying = false, isActive = true, savedConfig = current.config) }
            sendEffect(ThemeEditorEffect.ShowMessage(R.string.theme_applied))
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
        const val MAX_HISTORY = 50
    }
}
