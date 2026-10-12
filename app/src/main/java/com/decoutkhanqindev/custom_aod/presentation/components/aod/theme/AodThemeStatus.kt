package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme

import androidx.compose.runtime.Composable
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.jersey.AodJerseyStatus
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue

// Dòng trạng thái do chủ đề tự vẽ (AodBlockValue.STATUS) thay cho icon thông báo, nội dung thông báo và dòng pin dùng chung.
@Composable
fun AodThemeStatus(
    theme: AodThemeUiModel,
    battery: BatteryUiModel?,
    isBatteryEnabled: Boolean,
    isFlashlightOn: Boolean,
    notifications: AodNotificationsUiModel,
) {
    when (theme.value) {
        AodThemeValue.JERSEY -> AodJerseyStatus(
            battery = battery?.takeIf { isBatteryEnabled },
            isFlashlightOn = isFlashlightOn,
            notifications = notifications,
            config = theme.config,
        )

        AodThemeValue.CLASSIC, AodThemeValue.PEBBLE -> Unit
    }
}
