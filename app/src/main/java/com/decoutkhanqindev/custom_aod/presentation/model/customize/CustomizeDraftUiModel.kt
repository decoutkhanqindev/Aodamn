package com.decoutkhanqindev.custom_aod.presentation.model.customize

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.data.device.permission.PermissionManager
import com.decoutkhanqindev.custom_aod.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.custom_aod.data.local.image.AodImageManager
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.WallpaperValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.observeAodAppearance
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.AodExtrasUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.observeAodExtras
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.observeAodNotificationOptions
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

// Chỉ ghi vào cài đặt thật khi bấm Áp dụng; mục cần quyền giữ giá trị có hiệu lực (đã bật và đã có quyền), mặc định = AOD trống.
@Immutable
data class CustomizeDraftUiModel(
    val appearance: CustomizeAppearanceUiModel = CustomizeAppearanceUiModel(),
    val decor: CustomizeDecorUiModel = CustomizeDecorUiModel(),
    val info: CustomizeInfoUiModel = CustomizeInfoUiModel(),
    val effects: CustomizeEffectsUiModel = CustomizeEffectsUiModel(),
) {
    val extras: AodExtrasUiModel
        get() = AodExtrasUiModel(
            memo = decor.memo,
            isCalendarEnabled = info.isCalendarEnabled,
            isWeatherEnabled = info.isWeatherEnabled,
            isWeatherFahrenheit = info.isWeatherFahrenheit,
            isDateEnabled = appearance.isDateEnabled,
            isBatteryEnabled = appearance.isBatteryEnabled,
        )
}

// Xem trước một chủ đề khác với phần chung (nền, thông báo...) của bản nháp.
fun CustomizeDraftUiModel.withTheme(theme: AodThemeUiModel): CustomizeDraftUiModel =
    copy(appearance = appearance.copy(clock = appearance.clock.copy(theme = theme)))

@Immutable
data class CustomizeAppearanceUiModel(
    val clock: AodAppearanceUiModel = AodAppearanceUiModel(),
    val isDateEnabled: Boolean = DataStoreManager.DEFAULT_IS_AOD_DATE_ENABLED,
    val isBatteryEnabled: Boolean = DataStoreManager.DEFAULT_IS_AOD_BATTERY_ENABLED,
)

// wallpaper và background loại trừ nhau: chọn cái này thì bỏ cái kia.
@Immutable
data class CustomizeDecorUiModel(
    val wallpaper: WallpaperValue? = null,
    val background: Bitmap? = null,
    val drawing: Bitmap? = null,
    val memo: String = DataStoreManager.DEFAULT_AOD_MEMO,
)

@Immutable
data class CustomizeInfoUiModel(
    val isNotificationIconsEnabled: Boolean = false,
    val isNotificationContentEnabled: Boolean = false,
    val isCalendarEnabled: Boolean = false,
    val isWeatherEnabled: Boolean = false,
    val isWeatherFahrenheit: Boolean = DataStoreManager.DEFAULT_IS_AOD_WEATHER_FAHRENHEIT,
    val isMediaControlsEnabled: Boolean = false,
)

@Immutable
data class CustomizeEffectsUiModel(
    val isEdgeGlowEnabled: Boolean = false,
)

// Đúng như AOD đang hiện: mục cần quyền chỉ tính là bật khi đã có quyền, vì AOD bỏ qua mục đó khi thiếu quyền.
suspend fun DataStoreManager.loadCustomizeDraft(
    aodImageManager: AodImageManager,
    permissionManager: PermissionManager,
): CustomizeDraftUiModel {
    val extras = observeAodExtras().first()
    val notificationOptions = observeAodNotificationOptions().first()
    val hasNotificationAccess = permissionManager.isNotificationListenerEnabled()

    return CustomizeDraftUiModel(
        appearance = CustomizeAppearanceUiModel(
            clock = observeAodAppearance().first(),
            isDateEnabled = extras.isDateEnabled,
            isBatteryEnabled = extras.isBatteryEnabled,
        ),
        decor = CustomizeDecorUiModel(
            wallpaper = WallpaperValue.fromCode(aodWallpaper.filterNotNull().first()),
            background = aodImageManager.loadBackground(),
            drawing = aodImageManager.loadDrawing(),
            memo = extras.memo,
        ),
        info = CustomizeInfoUiModel(
            isNotificationIconsEnabled = notificationOptions.isIconsEnabled && hasNotificationAccess,
            isNotificationContentEnabled = notificationOptions.isContentEnabled && hasNotificationAccess,
            isCalendarEnabled = extras.isCalendarEnabled && permissionManager.hasCalendarPermission(),
            isWeatherEnabled = extras.isWeatherEnabled && permissionManager.hasCoarseLocationPermission(),
            isWeatherFahrenheit = extras.isWeatherFahrenheit,
            isMediaControlsEnabled = notificationOptions.isMediaControlsEnabled && hasNotificationAccess,
        ),
        effects = CustomizeEffectsUiModel(
            isEdgeGlowEnabled = notificationOptions.isEdgeGlowEnabled && hasNotificationAccess,
        ),
    )
}
