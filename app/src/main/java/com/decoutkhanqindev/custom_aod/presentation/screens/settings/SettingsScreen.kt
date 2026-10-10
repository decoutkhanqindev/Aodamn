package com.decoutkhanqindev.custom_aod.presentation.screens.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.data.device.permission.PermissionManager
import com.decoutkhanqindev.custom_aod.presentation.aod.AodNotificationListener
import com.decoutkhanqindev.custom_aod.presentation.aod.AodService
import com.decoutkhanqindev.custom_aod.presentation.components.LocalTag
import com.decoutkhanqindev.custom_aod.presentation.effects.LaunchedWithLifecycleEffect
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.areMiuiPermissionsGranted
import com.decoutkhanqindev.custom_aod.presentation.model.permission.isGranted
import com.decoutkhanqindev.custom_aod.presentation.navigation.IapDestination
import com.decoutkhanqindev.custom_aod.presentation.navigation.LanguageDestination
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsEffect
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.custom_aod.utils.collectCatching
import com.decoutkhanqindev.custom_aod.utils.openAppSettings
import com.decoutkhanqindev.custom_aod.utils.openMiuiPermissionSettings
import com.decoutkhanqindev.custom_aod.utils.openNotificationSettings
import com.decoutkhanqindev.custom_aod.utils.openOverlaySettings
import com.decoutkhanqindev.custom_aod.utils.playStoreUrl
import com.decoutkhanqindev.custom_aod.utils.shareText
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

@Composable
fun SettingsScreen(backStack: NavBackStack<NavKey>) {
    val context = LocalContext.current
    val resources = LocalResources.current
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
                        context.openMiuiPermissionSettings { permissionManager.areMiuiPermissionsGranted() }

                    is SettingsEffect.Permission.OpenNotificationSettings ->
                        context.openNotificationSettings {
                            permissionManager.isGranted(
                                PermissionValue.NOTIFICATIONS
                            )
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
        viewModel.effect.filterIsInstance<SettingsEffect.Navigation>().collectCatching(
            block = { effect ->
                when (effect) {
                    is SettingsEffect.Navigation.NavigateToLanguage -> backStack.add(
                        LanguageDestination(isFirstOpen = false)
                    )

                    is SettingsEffect.Navigation.NavigateToIap -> backStack.add(IapDestination)
                }
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LaunchedWithLifecycleEffect {
        viewModel.effect.filterIsInstance<SettingsEffect.ShareApp>().collectCatching(
            block = {
                context.shareText(
                    text = resources.getString(R.string.share_app_text, context.playStoreUrl()),
                    chooserTitle = resources.getString(R.string.share_app),
                )
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LifecycleResumeEffect(Unit) {
        viewModel.onIntent(SettingsIntent.Permission.RefreshPermissions)
        onPauseOrDispose { }
    }

    SettingsContent(state = state, onIntent = viewModel::onIntent)
}
