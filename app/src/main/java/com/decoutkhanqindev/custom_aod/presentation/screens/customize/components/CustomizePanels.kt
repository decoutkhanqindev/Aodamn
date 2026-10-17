package com.decoutkhanqindev.custom_aod.presentation.screens.customize.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.aod.editor.AodThemeOptionList
import com.decoutkhanqindev.custom_aod.presentation.components.aod.editor.WallpaperPicker
import com.decoutkhanqindev.custom_aod.presentation.components.settings.AppSwitchRow
import com.decoutkhanqindev.custom_aod.presentation.components.settings.AppValueRow
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.classic.ClassicThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.component.AodComponentSpec
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.component.AodComponentValue
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
    isFirstOpen: Boolean,
    draft: CustomizeDraftUiModel,
    decor: CustomizeDecorState,
    onIntent: (CustomizeIntent) -> Unit,
) {
    Column {
        when (tab.cluster) {
            CustomizeClusterValue.APPEARANCE -> AppearancePanel(
                tab = tab,
                isFirstOpen = isFirstOpen,
                appearance = draft.appearance,
                onIntent = onIntent
            )

            CustomizeClusterValue.DECOR -> DecorPanel(
                tab = tab,
                decor = draft.decor,
                decorState = decor,
                onIntent = onIntent
            )

            CustomizeClusterValue.INFO -> InfoPanel(
                tab = tab,
                info = draft.info,
                hasNotificationContent = draft.appearance.clock.theme.value.spec.hasNotificationContent,
                onIntent = onIntent,
            )

            CustomizeClusterValue.EFFECT -> EffectsPanel(effects = draft.effects, onIntent = onIntent)
        }

        tab.component?.let { component ->
            ComponentStyle(component = component, clock = draft.appearance.clock, onIntent = onIntent)
        }
    }
}

// Font, cỡ, màu riêng của khối (dưới phần bật / tắt và nội dung của tab). Khối mà chủ đề đang dùng tự vẽ (ngày, nhạc của Sỏi) thì kiểu do
// chủ đề quyết định: dẫn sang màn chủ đề thay vì hiện tùy chọn không có tác dụng.
@Composable
private fun ComponentStyle(
    component: AodComponentValue,
    clock: AodAppearanceUiModel,
    onIntent: (CustomizeIntent) -> Unit,
) {
    val theme = clock.theme.value
    val ownBlock = component.ownBlock

    if (ownBlock != null && ownBlock in theme.spec.ownBlocks) {
        AppValueRow(
            label = stringResource(R.string.customize_style_by_theme),
            value = stringResource(theme.nameRes),
            onClick = { onIntent(CustomizeIntent.Appearance.OpenThemeEditor(block = ownBlock)) },
        )
    } else {
        AodThemeOptionList(
            tab = AodComponentSpec.optionsOf(component).tab,
            config = clock.components.config,
            onOptionPreview = { key, raw -> onIntent(CustomizeIntent.Appearance.PreviewComponentOption(key, raw)) },
            onOptionChange = { key, raw -> onIntent(CustomizeIntent.Appearance.ChangeComponentOption(key, raw)) },
        )
    }
}

@Composable
private fun AppearancePanel(
    tab: CustomizeTabValue,
    isFirstOpen: Boolean,
    appearance: CustomizeAppearanceUiModel,
    onIntent: (CustomizeIntent) -> Unit,
) {
    when (tab) {
        CustomizeTabValue.CLOCK -> ClockPanel(isFirstOpen = isFirstOpen, theme = appearance.clock.theme, onIntent = onIntent)

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

// Onboarding: chỉ Cổ điển (chủ đề mặc định), mặt, font, màu, cỡ như bản trước khi có chủ đề, cùng control với màn chủ đề, để user mới thấy
// app chỉnh được gì. Sau đó: dòng dẫn sang màn chủ đề của chủ đề đang dùng (chọn chủ đề khác ở tab AOD).
@Composable
private fun ClockPanel(
    isFirstOpen: Boolean,
    theme: AodThemeUiModel,
    onIntent: (CustomizeIntent) -> Unit,
) {
    if (isFirstOpen) {
        AodThemeOptionList(
            tab = ClassicThemeSpec.ClockTab,
            config = theme.config,
            onOptionPreview = { key, raw -> onIntent(CustomizeIntent.Appearance.PreviewThemeOption(key, raw)) },
            onOptionChange = { key, raw -> onIntent(CustomizeIntent.Appearance.ChangeThemeOption(key, raw)) },
        )
    } else {
        AppValueRow(
            label = stringResource(R.string.customize_clock_theme),
            value = stringResource(theme.value.nameRes),
            onClick = { onIntent(CustomizeIntent.Appearance.OpenThemeEditor(block = null)) },
            description = stringResource(R.string.customize_clock_theme_desc),
        )
    }
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

// hasNotificationContent = false: chủ đề tự vẽ dòng trạng thái (Số áo) chỉ có icon thông báo, không có nội dung thông báo.
@Composable
private fun InfoPanel(
    tab: CustomizeTabValue,
    info: CustomizeInfoUiModel,
    hasNotificationContent: Boolean,
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

            if (hasNotificationContent) {
                AppSwitchRow(
                    label = stringResource(R.string.opt_notification_content),
                    isChecked = info.isNotificationContentEnabled,
                    onCheckedChange = { onIntent(CustomizeIntent.Info.ToggleNotificationContent(it)) },
                    description = stringResource(R.string.opt_notification_content_desc),
                )
            }
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

