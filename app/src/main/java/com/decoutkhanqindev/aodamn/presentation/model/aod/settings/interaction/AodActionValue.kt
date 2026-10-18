package com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R

@Immutable
enum class AodActionValue(
    val code: Int,
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
) {
    NONE(code = 0, labelRes = R.string.aod_action_none, iconRes = R.drawable.ic_prohibit),
    CLOSE(code = 1, labelRes = R.string.aod_action_close, iconRes = R.drawable.ic_lock_key),
    GO_DARK(code = 2, labelRes = R.string.aod_action_go_dark, iconRes = R.drawable.ic_moon),
    FLASHLIGHT(code = 3, labelRes = R.string.aod_action_flashlight, iconRes = R.drawable.ic_flashlight),
    PLAY_PAUSE(code = 4, labelRes = R.string.aod_action_play_pause, iconRes = R.drawable.ic_play_pause),
    PREVIOUS_TRACK(code = 5, labelRes = R.string.aod_action_previous_track, iconRes = R.drawable.ic_skip_back),
    NEXT_TRACK(code = 6, labelRes = R.string.aod_action_next_track, iconRes = R.drawable.ic_skip_forward);

    companion object {
        fun fromCode(code: Int?): AodActionValue = entries.find { it.code == code } ?: NONE
    }
}
