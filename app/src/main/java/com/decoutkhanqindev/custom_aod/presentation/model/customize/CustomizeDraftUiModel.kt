package com.decoutkhanqindev.custom_aod.presentation.model.customize

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.data.device.permission.PermissionManager
import com.decoutkhanqindev.custom_aod.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.custom_aod.data.local.image.AodImageManager
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.WallpaperValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.observeAodAppearance
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.AodExtrasUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.observeAodExtras
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.observeAodNotificationOptions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
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

// Xem trước riêng chủ đề (lưới chủ đề ở tab AOD, màn chủ đề): đồng hồ, ngày và các khối chủ đề tự vẽ (vd trình phát nhạc của Sỏi, dòng
// pin + icon thông báo của Số áo, bằng dữ liệu mẫu), không có nền, thông báo, pin... của user; AOD đầy đủ chỉ ở Tùy chỉnh.
fun AodThemeUiModel.toThemePreviewDraft(): CustomizeDraftUiModel {
    val ownBlocks = value.spec.ownBlocks
    return CustomizeDraftUiModel(
        appearance = CustomizeAppearanceUiModel(
            clock = AodAppearanceUiModel(theme = this),
            isDateEnabled = true,
            isBatteryEnabled = AodBlockValue.STATUS in ownBlocks,
        ),
        info = CustomizeInfoUiModel(
            isNotificationIconsEnabled = AodBlockValue.STATUS in ownBlocks,
            isMediaControlsEnabled = AodBlockValue.MEDIA in ownBlocks,
        ),
    )
}

// Xem trước một chủ đề khác với phần chung (nền, thông báo...) của bản nháp.
fun CustomizeDraftUiModel.withTheme(theme: AodThemeUiModel): CustomizeDraftUiModel =
    copy(appearance = appearance.copy(clock = appearance.clock.copy(theme = theme)))

// Tab của các khối đang bật (thẻ Giao diện của bạn ở tab AOD), theo thứ tự ở Tùy chỉnh; khối chủ đề không có thì không tính. Tính theo
// cài đặt chứ không theo dữ liệu lúc chạy (thời tiết, sự kiện có thể chưa có) vì thẻ tóm tắt giao diện user đã chọn.
fun CustomizeDraftUiModel.enabledBlockTabs(): ImmutableList<CustomizeTabValue> {
    val spec = appearance.clock.theme.value.spec
    return CustomizeTabValue.entries
        .filter { tab ->
            tab.isShownIn(spec) && when (tab) {
                CustomizeTabValue.DATE -> appearance.isDateEnabled
                CustomizeTabValue.BATTERY -> appearance.isBatteryEnabled
                CustomizeTabValue.DRAWING -> decor.drawing != null
                CustomizeTabValue.MEMO -> decor.memo.isNotBlank()
                CustomizeTabValue.NOTIFICATIONS -> info.isNotificationIconsEnabled || info.isNotificationContentEnabled
                CustomizeTabValue.EVENTS -> info.isCalendarEnabled
                CustomizeTabValue.WEATHER -> info.isWeatherEnabled
                CustomizeTabValue.MEDIA -> info.isMediaControlsEnabled
                CustomizeTabValue.CLOCK, CustomizeTabValue.BACKGROUND, CustomizeTabValue.EFFECTS -> false
            }
        }
        .toImmutableList()
}

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
