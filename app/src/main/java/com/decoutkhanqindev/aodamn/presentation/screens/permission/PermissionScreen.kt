package com.decoutkhanqindev.aodamn.presentation.screens.permission

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
import com.decoutkhanqindev.aodamn.presentation.aod.AodService
import com.decoutkhanqindev.aodamn.presentation.components.LocalTag
import com.decoutkhanqindev.aodamn.presentation.effects.LaunchedWithLifecycleEffect
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.isGranted
import com.decoutkhanqindev.aodamn.presentation.navigation.MainDestination
import com.decoutkhanqindev.aodamn.presentation.screens.permission.state.PermissionEffect
import com.decoutkhanqindev.aodamn.presentation.screens.permission.state.PermissionIntent
import com.decoutkhanqindev.aodamn.utils.collectCatching
import com.decoutkhanqindev.aodamn.utils.navigateBack
import com.decoutkhanqindev.aodamn.utils.navigateTo
import com.decoutkhanqindev.aodamn.utils.openMiuiPermissionSettings
import com.decoutkhanqindev.aodamn.utils.openNotificationSettings
import com.decoutkhanqindev.aodamn.utils.openOverlaySettings
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

@Composable
fun PermissionScreen(
    backStack: NavBackStack<NavKey>,
    rootBackStack: NavBackStack<NavKey> = backStack,
) {
    val context = LocalContext.current
    val tag = LocalTag.current
    val viewModel: PermissionViewModel = koinViewModel()
    val permissionManager: PermissionManager = koinInject()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        viewModel.onIntent(PermissionIntent.NotificationPermissionResult(isGranted))
    }

    LaunchedWithLifecycleEffect {
        viewModel.effect.collectCatching(
            block = { effect ->
                when (effect) {
                    is PermissionEffect.OpenOverlaySettings ->
                        context.openOverlaySettings { permissionManager.isGranted(PermissionValue.OVERLAY) }

                    is PermissionEffect.OpenMiuiPermissionSettings ->
                        context.openMiuiPermissionSettings { permissionManager.isGranted(effect.permission) }

                    is PermissionEffect.OpenNotificationSettings ->
                        context.openNotificationSettings {
                            permissionManager.isGranted(PermissionValue.NOTIFICATIONS)
                        }

                    is PermissionEffect.RequestNotificationPermission ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }

                    is PermissionEffect.StartAodService -> AodService.start(context)
                    is PermissionEffect.NavigateToMain -> rootBackStack.navigateTo(
                        MainDestination(isPermissionSkipped = effect.isPermissionSkipped),
                        preserveState = false,
                    )

                    is PermissionEffect.NavigateBack -> backStack.navigateBack()
                }
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LifecycleResumeEffect(Unit) {
        viewModel.onIntent(PermissionIntent.RefreshPermissions)
        onPauseOrDispose { }
    }

    PermissionContent(
        state = state,
        onIntent = viewModel::onIntent,
    )
}
