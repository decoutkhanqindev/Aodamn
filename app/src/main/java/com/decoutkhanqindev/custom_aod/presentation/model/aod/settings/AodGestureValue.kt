package com.decoutkhanqindev.custom_aod.presentation.model.aod.settings

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.automirrored.outlined.VolumeDown
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.SwipeDown
import androidx.compose.material.icons.outlined.SwipeUp
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.data.local.datastore.DataStoreManager

// Mặc định giữ cách chạy trước đây: chạm 2 lần và phím back về màn hình khoá; phím âm lượng không gán thì vẫn chỉnh âm lượng.
@Immutable
enum class AodGestureValue(
    @param:StringRes val labelRes: Int,
    val icon: ImageVector,
    val defaultActionCode: Int,
) {
    DOUBLE_TAP(
        labelRes = R.string.aod_gesture_double_tap,
        icon = Icons.Outlined.TouchApp,
        defaultActionCode = DataStoreManager.DEFAULT_AOD_DOUBLE_TAP_ACTION
    ),
    SWIPE_UP(
        labelRes = R.string.aod_gesture_swipe_up,
        icon = Icons.Outlined.SwipeUp,
        defaultActionCode = DataStoreManager.DEFAULT_AOD_SWIPE_UP_ACTION
    ),
    SWIPE_DOWN(
        labelRes = R.string.aod_gesture_swipe_down,
        icon = Icons.Outlined.SwipeDown,
        defaultActionCode = DataStoreManager.DEFAULT_AOD_SWIPE_DOWN_ACTION
    ),
    VOLUME_UP(
        labelRes = R.string.aod_gesture_volume_up,
        icon = Icons.AutoMirrored.Outlined.VolumeUp,
        defaultActionCode = DataStoreManager.DEFAULT_AOD_VOLUME_UP_ACTION
    ),
    VOLUME_DOWN(
        labelRes = R.string.aod_gesture_volume_down,
        icon = Icons.AutoMirrored.Outlined.VolumeDown,
        defaultActionCode = DataStoreManager.DEFAULT_AOD_VOLUME_DOWN_ACTION
    ),
    BACK(
        labelRes = R.string.aod_gesture_back,
        icon = Icons.AutoMirrored.Outlined.Undo,
        defaultActionCode = DataStoreManager.DEFAULT_AOD_BACK_ACTION
    ),
}
