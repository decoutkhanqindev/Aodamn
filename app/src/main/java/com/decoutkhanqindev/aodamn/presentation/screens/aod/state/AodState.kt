package com.decoutkhanqindev.aodamn.presentation.screens.aod.state

import android.graphics.Bitmap
import androidx.annotation.ColorInt
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.WallpaperValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.CalendarEventUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WeatherUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.AodExtrasUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodActionValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodGestureValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodInteractionUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class AodState(
    val nowMillis: Long = 0L,
    val appearance: AodAppearanceUiModel = AodAppearanceUiModel(),
    val interaction: AodInteractionUiModel = AodInteractionUiModel(),
    val extras: AodExtrasUiModel = AodExtrasUiModel(),
    val wallpaper: WallpaperValue? = null,
    val background: Bitmap? = null,
    val drawing: Bitmap? = null,
    val weather: WeatherUiModel? = null,
    val events: ImmutableList<CalendarEventUiModel> = persistentListOf(),
    val battery: BatteryUiModel? = null,
    val notifications: AodNotificationsUiModel = AodNotificationsUiModel(),
    val media: MediaUiModel? = null,
    val isFlashlightOn: Boolean = false,
    val isHintVisible: Boolean = true,
    val isGlowing: Boolean = false,
    @param:ColorInt val glowColorArgb: Int? = null,
    val isDimmed: Boolean = false,
    val isDark: Boolean = false,
    val isPreview: Boolean = false,
    val isStill: Boolean = false,
) {
    val isClockLive: Boolean
        get() = !isPreview || (appearance.theme.value.spec.isLiveInPreview && !isStill)

    val isExitHintVisible: Boolean
        get() = isHintVisible && interaction.actionOf(AodGestureValue.DOUBLE_TAP) == AodActionValue.CLOSE
}
