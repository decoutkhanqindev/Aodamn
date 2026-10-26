package com.decoutkhanqindev.aodamn.presentation.model

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Immutable

@Immutable
data class ToastUiModel(
    override val message: String,
    val isError: Boolean = false,
) : SnackbarVisuals {
    override val actionLabel: String? = null
    override val withDismissAction: Boolean = false
    override val duration: SnackbarDuration = SnackbarDuration.Short
}
