package com.decoutkhanqindev.custom_aod.presentation.screens.aod.state

import android.graphics.Bitmap
import androidx.annotation.ColorInt
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.WallpaperValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.CalendarEventUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.WeatherUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.AodExtrasUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.interaction.AodActionValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.interaction.AodGestureValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.interaction.AodInteractionUiModel
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
    // Khung xem trước (Tùy chỉnh, màn chủ đề, thẻ chủ đề) dùng giờ mẫu; AOD thật thì mặt số chạy theo giờ thật được (vòng giây của La bàn).
    val isPreview: Boolean = false,
    // Thẻ thu nhỏ ở tab AOD (thẻ thiết kế, lưới chủ đề) luôn đứng yên ở giờ mẫu, kể cả chủ đề bật isLiveInPreview: nhiều thẻ cùng chạy mỗi
    // khung hình trên màn danh sách là tốn pin vô ích.
    val isStill: Boolean = false,
) {
    // Mặt số chạy theo giờ thật: AOD thật, hoặc chủ đề bật isLiveInPreview thì cả khung xem trước (màn chủ đề, Tùy chỉnh), trừ thẻ ở tab AOD.
    val isClockLive: Boolean
        get() = !isPreview || (appearance.theme.value.spec.isLiveInPreview && !isStill)

    // Dòng "Chạm 2 lần để thoát" chỉ đúng khi chạm 2 lần vẫn là về màn hình khoá.
    val isExitHintVisible: Boolean
        get() = isHintVisible && interaction.actionOf(AodGestureValue.DOUBLE_TAP) == AodActionValue.CLOSE
}
