package com.decoutkhanqindev.aodamn.presentation.screens.settings

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
import com.decoutkhanqindev.aodamn.data.device.permission.PermissionManager
import com.decoutkhanqindev.aodamn.presentation.aod.AodNotificationListener
import com.decoutkhanqindev.aodamn.presentation.aod.AodService
import com.decoutkhanqindev.aodamn.presentation.components.LocalTag
import com.decoutkhanqindev.aodamn.presentation.effects.LaunchedWithLifecycleEffect
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.isGranted
import com.decoutkhanqindev.aodamn.presentation.navigation.LanguageDestination
import com.decoutkhanqindev.aodamn.presentation.screens.settings.state.SettingsEffect
import com.decoutkhanqindev.aodamn.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.aodamn.utils.collectCatching
import com.decoutkhanqindev.aodamn.utils.openAppSettings
import com.decoutkhanqindev.aodamn.utils.openMiuiPermissionSettings
import com.decoutkhanqindev.aodamn.utils.openNotificationSettings
import com.decoutkhanqindev.aodamn.utils.openOverlaySettings
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

@Composable
fun SettingsScreen(backStack: NavBackStack<NavKey>) {
    val context = LocalContext.current
    val tag = LocalTag.current
    val viewModel: SettingsViewModel = koinViewModel()
    val permissionManager: PermissionManager = koinInject()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        viewModel.onIntent(SettingsIntent.Permission.NotificationPermissionResult(isGranted))
    }
    val calendarPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        viewModel.onIntent(SettingsIntent.Permission.CalendarPermissionResult(isGranted))
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        viewModel.onIntent(SettingsIntent.Permission.LocationPermissionResult(isGranted))
    }

    LaunchedWithLifecycleEffect {
        viewModel.effect.filterIsInstance<SettingsEffect.Permission>().collectCatching(
            block = { effect ->
                when (effect) {
                    is SettingsEffect.Permission.OpenOverlaySettings ->
                        context.openOverlaySettings { permissionManager.isGranted(PermissionValue.OVERLAY) }

                    is SettingsEffect.Permission.OpenMiuiPermissionSettings ->
                        context.openMiuiPermissionSettings { permissionManager.isGranted(effect.permission) }

                    is SettingsEffect.Permission.OpenNotificationSettings ->
                        context.openNotificationSettings {
                            permissionManager.isGranted(PermissionValue.NOTIFICATIONS)
                        }

                    is SettingsEffect.Permission.OpenNotificationAccessSettings ->
                        AodNotificationListener.openAccessSettings(context) {
                            permissionManager.isGranted(PermissionValue.NOTIFICATION_ACCESS)
                        }

                    is SettingsEffect.Permission.OpenAppSettings ->
                        context.openAppSettings { permissionManager.isGranted(effect.permission) }

                    is SettingsEffect.Permission.RequestNotificationPermission ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }

                    is SettingsEffect.Permission.RequestCalendarPermission ->
                        calendarPermissionLauncher.launch(Manifest.permission.READ_CALENDAR)

                    is SettingsEffect.Permission.RequestLocationPermission ->
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LaunchedWithLifecycleEffect {
        viewModel.effect.filterIsInstance<SettingsEffect.StartAodService>().collectCatching(
            block = { AodService.start(context) },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LaunchedWithLifecycleEffect {
        viewModel.effect.filterIsInstance<SettingsEffect.NavigateToLanguage>().collectCatching(
            block = { backStack.add(LanguageDestination(isFirstOpen = false)) },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LifecycleResumeEffect(Unit) {
        viewModel.onIntent(SettingsIntent.Permission.RefreshPermissions)
        onPauseOrDispose { }
    }

    SettingsContent(state = state, onIntent = viewModel::onIntent)
}
