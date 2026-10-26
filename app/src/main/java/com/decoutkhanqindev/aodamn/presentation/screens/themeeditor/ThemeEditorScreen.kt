package com.decoutkhanqindev.aodamn.presentation.screens.themeeditor

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
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
import com.decoutkhanqindev.aodamn.ads.AdsManager
import com.decoutkhanqindev.aodamn.ads.composables.BannerAdView
import com.decoutkhanqindev.aodamn.data.device.permission.PermissionManager
import com.decoutkhanqindev.aodamn.presentation.components.AppToastHost
import com.decoutkhanqindev.aodamn.presentation.components.LocalTag
import com.decoutkhanqindev.aodamn.presentation.effects.LaunchedWithLifecycleEffect
import com.decoutkhanqindev.aodamn.presentation.model.ToastUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.isGranted
import com.decoutkhanqindev.aodamn.presentation.navigation.CustomizeDestination
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.state.ThemeEditorEffect
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.state.ThemeEditorIntent
import com.decoutkhanqindev.aodamn.utils.collectCatching
import com.decoutkhanqindev.aodamn.utils.navigateBack
import com.decoutkhanqindev.aodamn.utils.openMiuiPermissionSettings
import com.decoutkhanqindev.aodamn.utils.openNotificationSettings
import com.decoutkhanqindev.aodamn.utils.openOverlaySettings
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

@Composable
fun ThemeEditorScreen(
    backStack: NavBackStack<NavKey>,
    themeId: String,
    focusBlock: AodBlockValue?,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val tag = LocalTag.current
    val viewModel: ThemeEditorViewModel = koinViewModel { parametersOf(themeId, focusBlock) }
    val adsManager: AdsManager = koinInject()
    val permissionManager: PermissionManager = koinInject()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val toastState = remember { SnackbarHostState() }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        viewModel.onIntent(ThemeEditorIntent.Permission.NotificationPermissionResult(isGranted))
    }

    LaunchedWithLifecycleEffect {
        viewModel.effect.collectCatching(
            block = { effect ->
                when (effect) {
                    is ThemeEditorEffect.ShowMessage -> scope.launch {
                        toastState.currentSnackbarData?.dismiss()
                        toastState.showSnackbar(ToastUiModel(resources.getString(effect.messageRes), effect.isError))
                    }
                    is ThemeEditorEffect.NavigateToCustomize ->
                        if (backStack.any { key -> key is CustomizeDestination }) {
                            backStack.navigateBack()
                        } else {
                            backStack.add(CustomizeDestination(isFirstOpen = false))
                        }
                    is ThemeEditorEffect.NavigateBack -> backStack.navigateBack()
                    is ThemeEditorEffect.OpenOverlaySettings ->
                        context.openOverlaySettings { permissionManager.isGranted(PermissionValue.OVERLAY) }

                    is ThemeEditorEffect.OpenMiuiPermissionSettings ->
                        context.openMiuiPermissionSettings { permissionManager.isGranted(effect.permission) }

                    is ThemeEditorEffect.OpenNotificationSettings ->
                        context.openNotificationSettings { permissionManager.isGranted(PermissionValue.NOTIFICATIONS) }

                    is ThemeEditorEffect.RequestNotificationPermission ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                }
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LifecycleResumeEffect(Unit) {
        viewModel.onIntent(ThemeEditorIntent.Permission.RefreshPermissions)
        onPauseOrDispose { }
    }

    BackHandler {
        viewModel.onIntent(ThemeEditorIntent.NavigateBack)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ThemeEditorContent(
            state = state,
            onIntent = viewModel::onIntent,
            bannerAd = { BannerAdView(adUnit = { adsManager.bannerThemeEditor }) },
        )

        AppToastHost(
            hostState = toastState,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
