package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme

import androidx.compose.runtime.Composable
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.classic.AodClassicClock
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.jersey.AodJerseyClock
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.pebble.AodPebbleClock
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue

// Đồng hồ theo chủ đề đang dùng; thêm chủ đề = thêm một nhánh ở đây.
@Composable
fun AodClockFace(
    nowMillis: Long,
    theme: AodThemeUiModel,
    isDateEnabled: Boolean,
) {
    when (theme.value) {
        AodThemeValue.CLASSIC -> AodClassicClock(nowMillis = nowMillis, config = theme.config)
        AodThemeValue.PEBBLE -> AodPebbleClock(
            nowMillis = nowMillis,
            config = theme.config,
            isDateEnabled = isDateEnabled,
        )

        AodThemeValue.JERSEY -> AodJerseyClock(
            nowMillis = nowMillis,
            config = theme.config,
            isDateEnabled = isDateEnabled,
        )
    }
}
