package com.decoutkhanqindev.aodamn.presentation.model.customize

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.data.device.permission.PermissionManager
import com.decoutkhanqindev.aodamn.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.aodamn.data.local.image.AodImageManager
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.WallpaperValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.observeAodAppearance
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.AodExtrasUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.observeAodExtras
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.observeAodNotificationOptions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

@Immutable
data class CustomizeDraftUiModel(
    val appearance: CustomizeAppearanceUiModel = CustomizeAppearanceUiModel(),
    val decor: CustomizeDecorUiModel = CustomizeDecorUiModel(),
    val info: CustomizeInfoUiModel = CustomizeInfoUiModel(),
    val effects: CustomizeEffectsUiModel = CustomizeEffectsUiModel(),
    val isThemePreview: Boolean = false,
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
            isCalendarEnabled = AodBlockValue.EVENTS in ownBlocks,
            isMediaControlsEnabled = AodBlockValue.MEDIA in ownBlocks,
        ),
        isThemePreview = true,
    )
}

fun CustomizeDraftUiModel.withTheme(theme: AodThemeUiModel): CustomizeDraftUiModel =
    copy(appearance = appearance.copy(clock = appearance.clock.copy(theme = theme)))

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
