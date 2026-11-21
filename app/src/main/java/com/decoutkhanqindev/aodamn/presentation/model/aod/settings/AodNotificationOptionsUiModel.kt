package com.decoutkhanqindev.aodamn.presentation.model.aod.settings

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodNotificationOptionsPrefs
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue

@Immutable
data class AodNotificationOptionsUiModel(
    val isIconsEnabled: Boolean = AodNotificationOptionsPrefs.DEFAULT.isIconsEnabled,
    val isContentEnabled: Boolean = AodNotificationOptionsPrefs.DEFAULT.isContentEnabled,
    val isEdgeGlowEnabled: Boolean = AodNotificationOptionsPrefs.DEFAULT.isEdgeGlowEnabled,
    val isMediaControlsEnabled: Boolean = AodNotificationOptionsPrefs.DEFAULT.isMediaControlsEnabled,
)

fun AodNotificationOptionsPrefs.toUiModel(): AodNotificationOptionsUiModel = AodNotificationOptionsUiModel(
    isIconsEnabled = isIconsEnabled,
    isContentEnabled = isContentEnabled,
    isEdgeGlowEnabled = isEdgeGlowEnabled,
    isMediaControlsEnabled = isMediaControlsEnabled,
)

fun AodNotificationOptionsUiModel.shownIn(spec: AodThemeSpec): AodNotificationOptionsUiModel = copy(
    isContentEnabled = isContentEnabled && spec.hasNotificationContent,
    isMediaControlsEnabled = isMediaControlsEnabled && AodComponentValue.MEDIA in spec.components,
)
