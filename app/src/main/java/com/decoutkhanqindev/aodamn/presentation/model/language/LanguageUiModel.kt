package com.decoutkhanqindev.aodamn.presentation.model.language

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.local.locale.LanguageManager

@Immutable
data class LanguageUiModel(
    val language: LanguageValue,
    val displayName: String,
) {
    val label: String get() = "${language.flag}  $displayName"
}

fun LanguageValue.toUiModel(languageManager: LanguageManager): LanguageUiModel =
    LanguageUiModel(
        language = this,
        displayName = languageManager.displayNameOf(code = code, displayIn = code)
    )
