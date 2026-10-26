package com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager

@Immutable
enum class AodGestureValue(
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
    val defaultActionCode: Int,
) {
    DOUBLE_TAP(
        labelRes = R.string.aod_gesture_double_tap,
        iconRes = R.drawable.ic_hand_tap_duotone,
        defaultActionCode = DataStoreManager.DEFAULT_AOD_DOUBLE_TAP_ACTION
    ),
    SWIPE_UP(
        labelRes = R.string.aod_gesture_swipe_up,
        iconRes = R.drawable.ic_arrow_fat_lines_up_duotone,
        defaultActionCode = DataStoreManager.DEFAULT_AOD_SWIPE_UP_ACTION
    ),
    SWIPE_DOWN(
        labelRes = R.string.aod_gesture_swipe_down,
        iconRes = R.drawable.ic_arrow_fat_lines_down_duotone,
        defaultActionCode = DataStoreManager.DEFAULT_AOD_SWIPE_DOWN_ACTION
    ),
    VOLUME_UP(
        labelRes = R.string.aod_gesture_volume_up,
        iconRes = R.drawable.ic_speaker_high_duotone,
        defaultActionCode = DataStoreManager.DEFAULT_AOD_VOLUME_UP_ACTION
    ),
    VOLUME_DOWN(
        labelRes = R.string.aod_gesture_volume_down,
        iconRes = R.drawable.ic_speaker_low_duotone,
        defaultActionCode = DataStoreManager.DEFAULT_AOD_VOLUME_DOWN_ACTION
    ),
    BACK(
        labelRes = R.string.aod_gesture_back,
        iconRes = R.drawable.ic_arrow_u_up_left_duotone,
        defaultActionCode = DataStoreManager.DEFAULT_AOD_BACK_ACTION
    ),
}
