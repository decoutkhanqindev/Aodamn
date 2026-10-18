package com.decoutkhanqindev.aodamn.presentation.screens.aodtab

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.aodamn.data.device.permission.PermissionManager
import com.decoutkhanqindev.aodamn.presentation.aod.AodNotificationListener
import com.decoutkhanqindev.aodamn.presentation.aod.AodService
import com.decoutkhanqindev.aodamn.presentation.components.AppToastHost
import com.decoutkhanqindev.aodamn.presentation.components.LocalTag
import com.decoutkhanqindev.aodamn.presentation.effects.LaunchedWithLifecycleEffect
import com.decoutkhanqindev.aodamn.presentation.model.ToastUiModel
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.isGranted
import com.decoutkhanqindev.aodamn.presentation.navigation.CustomizeDestination
import com.decoutkhanqindev.aodamn.presentation.navigation.ThemeEditorDestination
import com.decoutkhanqindev.aodamn.presentation.screens.aodtab.state.AodTabEffect
import com.decoutkhanqindev.aodamn.presentation.screens.aodtab.state.AodTabIntent
import com.decoutkhanqindev.aodamn.utils.collectCatching
import com.decoutkhanqindev.aodamn.utils.openAppSettings
import com.decoutkhanqindev.aodamn.utils.openMiuiPermissionSettings
import com.decoutkhanqindev.aodamn.utils.openNotificationSettings
import com.decoutkhanqindev.aodamn.utils.openOverlaySettings
import kotlinx.coroutines.launch
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
    val resources = LocalResources.current
    val tag = LocalTag.current
    val viewModel: AodTabViewModel = koinViewModel { parametersOf(isPermissionSkipped) }
    val permissionManager: PermissionManager = koinInject()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val toastState = remember { SnackbarHostState() }
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

                    is AodTabEffect.ShowMessage -> scope.launch {
                        toastState.currentSnackbarData?.dismiss()
                        toastState.showSnackbar(ToastUiModel(resources.getString(effect.messageRes), effect.isError))
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

    Box(modifier = Modifier.fillMaxSize()) {
        AodTabContent(state = state, onIntent = viewModel::onIntent)

        AppToastHost(
            hostState = toastState,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
