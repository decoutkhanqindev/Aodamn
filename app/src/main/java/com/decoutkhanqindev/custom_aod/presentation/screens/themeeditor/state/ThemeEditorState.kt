package com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeTab
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.isSame
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.withTheme

// Một màn cho mọi chủ đề: xem trước (Sửa / Áp dụng, đang dùng thì Tùy chỉnh thêm) rồi sửa (tab theo AodThemeSpec.tabs, Lưu). base là
// phần chung (nền, thông báo, thời tiết...) đang dùng để khung xem trước giống AOD thật.
@Immutable
data class ThemeEditorState(
    val theme: AodThemeValue = AodThemeValue.DEFAULT,
    val isLoading: Boolean = true,
    val nowMillis: Long = 0L,
    val base: CustomizeDraftUiModel = CustomizeDraftUiModel(),
    val config: AodThemeConfig = AodThemeConfig(),
    val savedConfig: AodThemeConfig = AodThemeConfig(),
    val isActive: Boolean = false,
    val isEditing: Boolean = false,
    val selectedTab: AodThemeTab? = null,
    val history: ThemeEditorHistoryState = ThemeEditorHistoryState(),
) {
    // Đang sửa thì luôn hiện dòng ngày và trình phát mẫu dù user đã tắt ở Tùy chỉnh, để tab Ngày / Nhạc thấy được thay đổi.
    val preview: CustomizeDraftUiModel
        get() {
            val draft = base.withTheme(AodThemeUiModel(value = theme, config = config))
            return if (isEditing) {
                draft.copy(
                    appearance = draft.appearance.copy(isDateEnabled = true),
                    info = draft.info.copy(isMediaControlsEnabled = true),
                )
            } else {
                draft
            }
        }

    val hasChanges: Boolean
        get() = !theme.spec.isSame(config, savedConfig)
}

@Immutable
data class ThemeEditorHistoryState(
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val isDefault: Boolean = true,
    val isResetConfirmVisible: Boolean = false,
    val isDiscardConfirmVisible: Boolean = false,
)
