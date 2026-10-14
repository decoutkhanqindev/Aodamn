package com.decoutkhanqindev.custom_aod.presentation.screens.customize.state

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeGuideStepValue
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeTabValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CustomizeState(
    val isLoading: Boolean = true,
    val isFirstOpen: Boolean = false,
    val draft: CustomizeDraftUiModel = CustomizeDraftUiModel(),
    val selectedTab: CustomizeTabValue? = null,
    val history: CustomizeHistoryState = CustomizeHistoryState(),
    val decor: CustomizeDecorState = CustomizeDecorState(),
    val effects: CustomizeEffectsState = CustomizeEffectsState(),
    val guideStep: CustomizeGuideStepValue? = null,
    val permissionRequest: PermissionValue? = null,
    // Áp dụng khi còn thiếu quyền bắt buộc: sheet liệt kê các quyền đó, cấp đủ thì tự áp dụng, đóng sheet thì không áp dụng.
    val requiredPermissions: ImmutableList<PermissionUiModel> = persistentListOf(),
    val isApplyPermissionSheetVisible: Boolean = false,
    val isApplying: Boolean = false,
    val isLandscapePreview: Boolean = false,
)

@Immutable
data class CustomizeHistoryState(
    val hasChanges: Boolean = false,
    val isDefault: Boolean = true,
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val isResetConfirmVisible: Boolean = false,
)

@Immutable
data class CustomizeDecorState(
    val isLoadingBackground: Boolean = false,
    val isDrawingPadVisible: Boolean = false,
    val isMemoEditorVisible: Boolean = false,
)

@Immutable
data class CustomizeEffectsState(
    val isGlowPreviewing: Boolean = false,
)
