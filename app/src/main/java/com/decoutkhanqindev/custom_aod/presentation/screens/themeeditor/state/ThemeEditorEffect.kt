package com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state

import androidx.annotation.StringRes

sealed interface ThemeEditorEffect {
    data class ShowMessage(@param:StringRes val messageRes: Int) : ThemeEditorEffect
    data object NavigateToCustomize : ThemeEditorEffect
    data object NavigateBack : ThemeEditorEffect
}
