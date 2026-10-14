package com.decoutkhanqindev.custom_aod.presentation.model

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Immutable

// Một toast của app (AppToastHost): hàng đợi và thời gian hiện dùng SnackbarHostState, phần vẽ là của app. isError: viền đỏ, icon cảnh báo.
@Immutable
data class ToastUiModel(
    override val message: String,
    val isError: Boolean = false,
) : SnackbarVisuals {
    override val actionLabel: String? = null
    override val withDismissAction: Boolean = false
    override val duration: SnackbarDuration = SnackbarDuration.Short
}
