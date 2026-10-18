package com.decoutkhanqindev.aodamn.presentation.screens.language.state

import com.decoutkhanqindev.aodamn.presentation.model.language.LanguageValue

sealed interface LanguageIntent {
    data class SelectLanguage(val language: LanguageValue) : LanguageIntent
    data object ConfirmLanguage : LanguageIntent
    data object NavigateBack : LanguageIntent
}
