package com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R

@Immutable
enum class AodGestureValue(
    val id: String,
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
    val defaultAction: AodActionValue,
) {
    DOUBLE_TAP(
        id = "double_tap",
        labelRes = R.string.aod_gesture_double_tap,
        iconRes = R.drawable.ic_hand_tap_duotone,
        defaultAction = AodActionValue.CLOSE,
    ),
    SWIPE_UP(
        id = "swipe_up",
        labelRes = R.string.aod_gesture_swipe_up,
        iconRes = R.drawable.ic_arrow_fat_lines_up_duotone,
        defaultAction = AodActionValue.NONE,
    ),
    SWIPE_DOWN(
        id = "swipe_down",
        labelRes = R.string.aod_gesture_swipe_down,
        iconRes = R.drawable.ic_arrow_fat_lines_down_duotone,
        defaultAction = AodActionValue.NONE,
    ),
    VOLUME_UP(
        id = "volume_up",
        labelRes = R.string.aod_gesture_volume_up,
        iconRes = R.drawable.ic_speaker_high_duotone,
        defaultAction = AodActionValue.NONE,
    ),
    VOLUME_DOWN(
        id = "volume_down",
        labelRes = R.string.aod_gesture_volume_down,
        iconRes = R.drawable.ic_speaker_low_duotone,
        defaultAction = AodActionValue.NONE,
    ),
    BACK(
        id = "back",
        labelRes = R.string.aod_gesture_back,
        iconRes = R.drawable.ic_arrow_u_up_left_duotone,
        defaultAction = AodActionValue.CLOSE,
    ),
}
