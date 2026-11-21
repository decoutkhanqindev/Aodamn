package com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R

@Immutable
enum class AodActionValue(
    val id: String,
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
) {
    NONE(id = "none", labelRes = R.string.aod_action_none, iconRes = R.drawable.ic_prohibit),
    CLOSE(id = "close", labelRes = R.string.aod_action_close, iconRes = R.drawable.ic_lock_key),
    GO_DARK(id = "go_dark", labelRes = R.string.aod_action_go_dark, iconRes = R.drawable.ic_moon),
    FLASHLIGHT(id = "flashlight", labelRes = R.string.aod_action_flashlight, iconRes = R.drawable.ic_flashlight),
    PLAY_PAUSE(id = "play_pause", labelRes = R.string.aod_action_play_pause, iconRes = R.drawable.ic_play_pause),
    PREVIOUS_TRACK(id = "previous_track", labelRes = R.string.aod_action_previous_track, iconRes = R.drawable.ic_skip_back),
    NEXT_TRACK(id = "next_track", labelRes = R.string.aod_action_next_track, iconRes = R.drawable.ic_skip_forward);

    companion object {
        fun fromIdOrNull(id: String?): AodActionValue? = entries.find { it.id == id }
    }
}
