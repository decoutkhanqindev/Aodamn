package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.custom_aod.data.device.permission.PermissionManager
import com.decoutkhanqindev.custom_aod.presentation.aod.AodNotificationListener
import com.decoutkhanqindev.custom_aod.presentation.aod.AodService
import com.decoutkhanqindev.custom_aod.presentation.components.LocalTag
import com.decoutkhanqindev.custom_aod.presentation.effects.LaunchedWithLifecycleEffect
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.isGranted
import com.decoutkhanqindev.custom_aod.presentation.navigation.CustomizeDestination
import com.decoutkhanqindev.custom_aod.presentation.navigation.ThemeEditorDestination
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabEffect
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabIntent
import com.decoutkhanqindev.custom_aod.utils.collectCatching
import com.decoutkhanqindev.custom_aod.utils.openAppSettings
import com.decoutkhanqindev.custom_aod.utils.openMiuiPermissionSettings
import com.decoutkhanqindev.custom_aod.utils.openNotificationSettings
import com.decoutkhanqindev.custom_aod.utils.openOverlaySettings
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

@Composable
fun AodTabScreen(
    backStack: NavBackStack<NavKey>,
    isPermissionSkipped: Boolean,
) {
    val context = LocalContext.current
    val tag = LocalTag.current
    val viewModel: AodTabViewModel = koinViewModel { parametersOf(isPermissionSkipped) }
    val permissionManager: PermissionManager = koinInject()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        viewModel.onIntent(AodTabIntent.NotificationPermissionResult(isGranted))
    }

    LaunchedWithLifecycleEffect {
        viewModel.effect.collectCatching(
            block = { effect ->
                when (effect) {
                    is AodTabEffect.StartAodService -> AodService.start(context)
                    is AodTabEffect.StopAodService -> AodService.stop(context)
                    is AodTabEffect.NavigateToCustomize -> backStack.add(
                        CustomizeDestination(isFirstOpen = false)
                    )

                    is AodTabEffect.NavigateToThemeEditor -> backStack.add(ThemeEditorDestination(effect.themeId, focusBlock = null))

                    is AodTabEffect.OpenOverlaySettings ->
                        context.openOverlaySettings { permissionManager.isGranted(PermissionValue.OVERLAY) }

                    is AodTabEffect.OpenMiuiPermissionSettings ->
                        context.openMiuiPermissionSettings { permissionManager.isGranted(effect.permission) }

                    is AodTabEffect.OpenNotificationSettings ->
                        context.openNotificationSettings {
                            permissionManager.isGranted(
                                PermissionValue.NOTIFICATIONS
                            )
                        }

                    is AodTabEffect.OpenNotificationAccessSettings ->
                        AodNotificationListener.openAccessSettings(context) {
                            permissionManager.isGranted(PermissionValue.NOTIFICATION_ACCESS)
                        }

                    is AodTabEffect.OpenAppSettings ->
                        context.openAppSettings { permissionManager.isGranted(effect.permission) }

                    is AodTabEffect.RequestNotificationPermission ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                }
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LifecycleResumeEffect(Unit) {
        viewModel.onIntent(AodTabIntent.RefreshPermissions)
        viewModel.onIntent(AodTabIntent.RefreshDesign)
        onPauseOrDispose { }
    }

    AodTabContent(state = state, onIntent = viewModel::onIntent)
}
