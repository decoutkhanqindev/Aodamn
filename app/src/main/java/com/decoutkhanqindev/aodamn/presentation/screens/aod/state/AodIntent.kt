package com.decoutkhanqindev.aodamn.presentation.screens.aod.state

import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodGestureValue

sealed interface AodIntent {
    data class PerformGesture(val gesture: AodGestureValue) : AodIntent
    data class ToggleMotionWatch(val isEnabled: Boolean) : AodIntent
    data class ToggleLatestNotification(val isShown: Boolean) : AodIntent
    data class ToggleAutoHideBlocks(val isShown: Boolean) : AodIntent
    data class ToggleMediaControls(val isShown: Boolean) : AodIntent
    data object PlayPauseMedia : AodIntent
    data object SkipToPreviousTrack : AodIntent
    data object SkipToNextTrack : AodIntent
}
