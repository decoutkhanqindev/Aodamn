package com.decoutkhanqindev.custom_aod.presentation.screens.customize.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.aod.AodThemeOptionList
import com.decoutkhanqindev.custom_aod.presentation.components.aod.WallpaperPicker
import com.decoutkhanqindev.custom_aod.presentation.components.settings.AppSwitchRow
import com.decoutkhanqindev.custom_aod.presentation.components.settings.AppValueRow
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.ClassicThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeAppearanceUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeClusterValue
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDecorUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeEffectsUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeInfoUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeTabValue
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeDecorState
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeIntent

@Composable
fun CustomizePanel(
    tab: CustomizeTabValue,
    draft: CustomizeDraftUiModel,
    decor: CustomizeDecorState,
    onIntent: (CustomizeIntent) -> Unit,
) {
    when (tab.cluster) {
        CustomizeClusterValue.APPEARANCE -> AppearancePanel(
            tab = tab,
            appearance = draft.appearance,
            onIntent = onIntent
        )

        CustomizeClusterValue.DECOR -> DecorPanel(
            tab = tab,
            decor = draft.decor,
            decorState = decor,
            onIntent = onIntent
        )

        CustomizeClusterValue.INFO -> InfoPanel(tab = tab, info = draft.info, onIntent = onIntent)
        CustomizeClusterValue.EFFECT -> EffectsPanel(effects = draft.effects, onIntent = onIntent)
    }
}

@Composable
private fun AppearancePanel(
    tab: CustomizeTabValue,
    appearance: CustomizeAppearanceUiModel,
    onIntent: (CustomizeIntent) -> Unit,
) {
    when (tab) {
        CustomizeTabValue.CLOCK -> ClockPanel(theme = appearance.clock.theme, onIntent = onIntent)

        CustomizeTabValue.DATE -> AppSwitchRow(
            label = stringResource(R.string.opt_date),
            isChecked = appearance.isDateEnabled,
            onCheckedChange = { onIntent(CustomizeIntent.Appearance.ToggleDate(it)) },
        )

        else -> AppSwitchRow(
            label = stringResource(R.string.opt_battery),
            isChecked = appearance.isBatteryEnabled,
            onCheckedChange = { onIntent(CustomizeIntent.Appearance.ToggleBattery(it)) },
        )
    }
}

// Chỉ có lúc onboarding và chỉ Cổ điển (chủ đề mặc định): mặt, font, màu, cỡ như bản trước khi có chủ đề, cùng control với màn chủ đề.
// Các chủ đề khác chọn và chỉnh ở tab AOD › Chủ đề.
@Composable
private fun ClockPanel(
    theme: AodThemeUiModel,
    onIntent: (CustomizeIntent) -> Unit,
) {
    AodThemeOptionList(
        tab = ClassicThemeSpec.OnboardingTab,
        config = theme.config,
        onOptionPreview = { key, raw -> onIntent(CustomizeIntent.Appearance.PreviewThemeOption(key, raw)) },
        onOptionChange = { key, raw -> onIntent(CustomizeIntent.Appearance.ChangeThemeOption(key, raw)) },
    )
}

@Composable
private fun DecorPanel(
    tab: CustomizeTabValue,
    decor: CustomizeDecorUiModel,
    decorState: CustomizeDecorState,
    onIntent: (CustomizeIntent) -> Unit,
) {
    when (tab) {
        CustomizeTabValue.DRAWING -> DrawingPanel(
            hasDrawing = decor.drawing != null,
            onIntent = onIntent
        )

        CustomizeTabValue.MEMO -> MemoPanel(memo = decor.memo, onIntent = onIntent)
        else -> BackgroundPanel(
            decor = decor,
            isLoadingBackground = decorState.isLoadingBackground,
            onIntent = onIntent
        )
    }
}

@Composable
private fun InfoPanel(
    tab: CustomizeTabValue,
    info: CustomizeInfoUiModel,
    onIntent: (CustomizeIntent) -> Unit,
) {
    when (tab) {
        CustomizeTabValue.EVENTS -> AppSwitchRow(
            label = stringResource(R.string.opt_calendar),
            isChecked = info.isCalendarEnabled,
            onCheckedChange = { onIntent(CustomizeIntent.Info.ToggleCalendar(it)) },
            description = stringResource(R.string.opt_calendar_desc),
        )

        CustomizeTabValue.WEATHER -> Column {
            AppSwitchRow(
                label = stringResource(R.string.opt_weather),
                isChecked = info.isWeatherEnabled,
                onCheckedChange = { onIntent(CustomizeIntent.Info.ToggleWeather(it)) },
                description = stringResource(R.string.opt_weather_desc),
            )

            AnimatedVisibility(visible = info.isWeatherEnabled) {
                AppSwitchRow(
                    label = stringResource(R.string.opt_weather_fahrenheit),
                    isChecked = info.isWeatherFahrenheit,
                    onCheckedChange = { onIntent(CustomizeIntent.Info.ToggleWeatherFahrenheit(it)) },
                )
            }
        }

        CustomizeTabValue.MEDIA -> AppSwitchRow(
            label = stringResource(R.string.opt_media_controls),
            isChecked = info.isMediaControlsEnabled,
            onCheckedChange = { onIntent(CustomizeIntent.Info.ToggleMediaControls(it)) },
        )

        else -> Column {
            AppSwitchRow(
                label = stringResource(R.string.opt_notification_icons),
                isChecked = info.isNotificationIconsEnabled,
                onCheckedChange = { onIntent(CustomizeIntent.Info.ToggleNotificationIcons(it)) },
            )

            AppSwitchRow(
                label = stringResource(R.string.opt_notification_content),
                isChecked = info.isNotificationContentEnabled,
                onCheckedChange = { onIntent(CustomizeIntent.Info.ToggleNotificationContent(it)) },
                description = stringResource(R.string.opt_notification_content_desc),
            )
        }
    }
}

@Composable
private fun EffectsPanel(
    effects: CustomizeEffectsUiModel,
    onIntent: (CustomizeIntent) -> Unit,
) {
    Column {
        AppSwitchRow(
            label = stringResource(R.string.opt_edge_glow),
            isChecked = effects.isEdgeGlowEnabled,
            onCheckedChange = { onIntent(CustomizeIntent.Effects.ToggleEdgeGlow(it)) },
        )

        AppValueRow(
            label = stringResource(R.string.customize_glow_preview),
            value = stringResource(R.string.action_preview),
            onClick = { onIntent(CustomizeIntent.Effects.PreviewEdgeGlow) },
        )
    }
}

@Composable
private fun BackgroundPanel(
    decor: CustomizeDecorUiModel,
    isLoadingBackground: Boolean,
    onIntent: (CustomizeIntent) -> Unit,
) {
    Column {
        WallpaperPicker(
            selected = decor.wallpaper,
            onSelect = { wallpaper -> onIntent(CustomizeIntent.Decor.SelectWallpaper(wallpaper)) },
        )

        AppValueRow(
            label = stringResource(R.string.background_from_device),
            value = stringResource(
                when {
                    isLoadingBackground -> R.string.customize_background_loading
                    decor.background != null -> R.string.background_change
                    else -> R.string.background_choose
                },
            ),
            onClick = { onIntent(CustomizeIntent.Decor.OpenBackgroundPicker) },
        )

        AnimatedVisibility(visible = decor.background != null || decor.wallpaper != null) {
            AppValueRow(
                label = stringResource(R.string.background_remove),
                value = "",
                onClick = { onIntent(CustomizeIntent.Decor.RemoveBackground) },
            )
        }
    }
}

@Composable
private fun DrawingPanel(
    hasDrawing: Boolean,
    onIntent: (CustomizeIntent) -> Unit,
) {
    Column {
        AppValueRow(
            label = stringResource(R.string.opt_drawing),
            value = stringResource(if (hasDrawing) R.string.drawing_redraw else R.string.drawing_draw),
            onClick = { onIntent(CustomizeIntent.Decor.ShowDrawingPad) },
        )

        AnimatedVisibility(visible = hasDrawing) {
            AppValueRow(
                label = stringResource(R.string.drawing_remove),
                value = "",
                onClick = { onIntent(CustomizeIntent.Decor.RemoveDrawing) },
            )
        }
    }
}

@Composable
private fun MemoPanel(
    memo: String,
    onIntent: (CustomizeIntent) -> Unit,
) {
    Column {
        AppValueRow(
            label = stringResource(R.string.opt_memo),
            value = stringResource(if (memo.isBlank()) R.string.memo_add else R.string.memo_edit),
            onClick = { onIntent(CustomizeIntent.Decor.ShowMemoEditor) },
            description = memo.ifBlank { null },
        )

        AnimatedVisibility(visible = memo.isNotBlank()) {
            AppValueRow(
                label = stringResource(R.string.customize_memo_remove),
                value = "",
                onClick = { onIntent(CustomizeIntent.Decor.ChangeMemo("")) },
            )
        }
    }
}

