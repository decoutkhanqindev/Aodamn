package com.decoutkhanqindev.aodamn.presentation.screens.aod.state

import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodGestureValue

sealed interface AodIntent {
    data class PerformGesture(val gesture: AodGestureValue) : AodIntent
    data object PlayPauseMedia : AodIntent
    data object SkipToPreviousTrack : AodIntent
    data object SkipToNextTrack : AodIntent
}
