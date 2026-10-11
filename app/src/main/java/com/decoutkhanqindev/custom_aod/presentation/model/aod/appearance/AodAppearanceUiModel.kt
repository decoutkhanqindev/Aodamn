package com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.decoutkhanqindev.custom_aod.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.currentAodTheme
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.observeAodTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

// Diện mạo AOD: chủ đề đang dùng (đồng hồ và kiểu chữ) và xoay ngang (đồng hồ đêm, chọn ở tab Cài đặt).
@Immutable
data class AodAppearanceUiModel(
    val theme: AodThemeUiModel = AodThemeUiModel(),
    val isLandscape: Boolean = DataStoreManager.DEFAULT_IS_AOD_LANDSCAPE,
) {
    // Các khối dùng chung (ngày, thời tiết, ghi chú, nét vẽ) lấy font và màu theo chủ đề.
    val font: ClockFontValue
        get() = theme.font

    val color: Color
        get() = theme.accent
}

fun DataStoreManager.currentAodAppearance(): AodAppearanceUiModel = AodAppearanceUiModel(
    theme = currentAodTheme(),
    isLandscape = isAodLandscape.value ?: DataStoreManager.DEFAULT_IS_AOD_LANDSCAPE,
)

fun DataStoreManager.observeAodAppearance(): Flow<AodAppearanceUiModel> = combine(
    observeAodTheme(),
    isAodLandscape.filterNotNull(),
) { theme, isLandscape ->
    AodAppearanceUiModel(theme = theme, isLandscape = isLandscape)
}
