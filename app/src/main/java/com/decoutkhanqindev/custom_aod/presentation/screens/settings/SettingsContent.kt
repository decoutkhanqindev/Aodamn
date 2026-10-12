package com.decoutkhanqindev.custom_aod.presentation.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.WakeResultValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.AodOptionsUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.rules.AodRulesUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.settings.rules.ChargingRuleValue
import com.decoutkhanqindev.custom_aod.presentation.model.language.LanguageUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.language.LanguageValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.components.SettingsAppSection
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.components.SettingsInteractionSection
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.components.SettingsOptionsSection
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.components.SettingsPermissionsSection
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.components.SettingsRulesSection
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsRulesState
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsState
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun SettingsContent(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        AnimatedVisibility(
            visible = !state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = 20.dp,
                        vertical = 12.dp,
                    ),
            ) {
                SettingsOptionsSection(
                    options = state.options,
                    isLandscape = state.appearance.isLandscape,
                    onIntent = onIntent,
                )

                SettingsRulesSection(
                    rules = state.rules,
                    onIntent = onIntent,
                )

                SettingsInteractionSection(
                    interaction = state.interaction,
                    onIntent = onIntent,
                )

                SettingsPermissionsSection(
                    permissions = state.permissions,
                    lastWake = state.lastWake,
                    onIntent = onIntent,
                )

                SettingsAppSection(
                    language = state.language,
                    onIntent = onIntent,
                )
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 2400)
@Composable
private fun SettingsContentPreview() {
    Theme {
        SettingsContent(
            state = SettingsState(
                isLoading = false,
                options = AodOptionsUiModel(
                    isCustomBrightness = true,
                    brightnessPercent = 20,
                    timeoutMinutes = 30,
                ),
                rules = SettingsRulesState(settings = AodRulesUiModel(chargingRule = ChargingRuleValue.PLUGGED)),
                permissions = persistentListOf(
                    PermissionUiModel(permission = PermissionValue.OVERLAY, isGranted = true),
                    PermissionUiModel(permission = PermissionValue.MIUI_LOCK_SCREEN, isGranted = false),
                    PermissionUiModel(permission = PermissionValue.MIUI_BACKGROUND_POPUP, isGranted = null),
                    PermissionUiModel(permission = PermissionValue.NOTIFICATIONS, isGranted = true),
                    PermissionUiModel(permission = PermissionValue.NOTIFICATION_ACCESS, isGranted = false),
                ),
                language = LanguageUiModel(language = LanguageValue.ENGLISH, displayName = "English"),
                lastWake = WakeResultValue.OK,
            ),
            onIntent = {},
        )
    }
}
