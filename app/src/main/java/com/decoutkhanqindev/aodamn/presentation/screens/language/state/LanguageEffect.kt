package com.decoutkhanqindev.aodamn.presentation.screens.language.state

sealed interface LanguageEffect {
    data object NavigateToCustomize : LanguageEffect
    data object NavigateBack : LanguageEffect
}
