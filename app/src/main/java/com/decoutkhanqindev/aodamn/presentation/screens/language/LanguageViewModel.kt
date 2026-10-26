package com.decoutkhanqindev.aodamn.presentation.screens.language

import androidx.lifecycle.viewModelScope
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.data.local.locale.LanguageManager
import com.decoutkhanqindev.aodamn.presentation.base.BaseViewModel
import com.decoutkhanqindev.aodamn.presentation.model.language.LanguageValue
import com.decoutkhanqindev.aodamn.presentation.model.language.toUiModel
import com.decoutkhanqindev.aodamn.presentation.screens.language.state.LanguageEffect
import com.decoutkhanqindev.aodamn.presentation.screens.language.state.LanguageIntent
import com.decoutkhanqindev.aodamn.presentation.screens.language.state.LanguageState
import com.decoutkhanqindev.aodamn.utils.collectCatching
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber

class LanguageViewModel(
    private val isFirstOpen: Boolean,
    private val dataStoreManager: DataStoreManager,
    private val languageManager: LanguageManager,
) : BaseViewModel<LanguageState, LanguageIntent, LanguageEffect>(
    initialState = LanguageState(isFirstOpen = isFirstOpen),
) {

    init {
        loadLanguages()
        observeAppliedLanguage()
    }

    override fun onIntent(intent: LanguageIntent) {
        Timber.tag(tag).d("onIntent: $intent")
        when (intent) {
            is LanguageIntent.SelectLanguage -> updateState { copy(selectedLanguage = intent.language) }
            is LanguageIntent.ConfirmLanguage -> confirmLanguage()
            is LanguageIntent.NavigateBack -> viewModelScope.launch { sendEffect(LanguageEffect.NavigateBack) }
        }
    }

    private fun loadLanguages() {
        val deviceLanguage = LanguageValue.fromCode(languageManager.deviceLanguageCode())
            .takeIf { language -> language in LanguageValue.TRANSLATED }
        val languages = LanguageValue.TRANSLATED
            .sortedBy { language ->
                when (language) {
                    deviceLanguage -> 0
                    LanguageValue.DEFAULT -> 1
                    else -> 2
                }
            }
            .map { language -> language.toUiModel(languageManager) }
            .toImmutableList()
        updateState {
            copy(
                languages = languages,
                selectedLanguage = if (isFirstOpen) deviceLanguage
                    ?: LanguageValue.DEFAULT else selectedLanguage,
            )
        }
    }

    private fun observeAppliedLanguage() {
        viewModelScope.launch {
            dataStoreManager.selectedLangCode.filterNotNull().collectCatching(
                block = { code ->
                    val appliedLanguage = LanguageValue.fromCode(code)
                    updateState {
                        copy(
                            appliedLanguage = appliedLanguage,
                            selectedLanguage = selectedLanguage ?: appliedLanguage
                        )
                    }
                },
                catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
            )
        }
    }

    private fun confirmLanguage() {
        val language = state.value.selectedLanguage ?: return
        dataStoreManager.saveSelectedLangCode(language.code)
        if (isFirstOpen) dataStoreManager.saveIsFirstOpen(false)
        val effect =
            if (isFirstOpen) LanguageEffect.NavigateToCustomize else LanguageEffect.NavigateBack
        viewModelScope.launch { sendEffect(effect) }
    }
}
