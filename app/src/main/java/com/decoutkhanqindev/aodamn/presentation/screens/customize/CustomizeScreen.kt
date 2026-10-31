package com.decoutkhanqindev.aodamn.presentation.screens.customize

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import com.decoutkhanqindev.aodamn.presentation.aod.AodNotificationListener
import com.decoutkhanqindev.aodamn.presentation.components.AppToastHost
import com.decoutkhanqindev.aodamn.presentation.components.LocalTag
import com.decoutkhanqindev.aodamn.presentation.effects.LaunchedWithLifecycleEffect
import com.decoutkhanqindev.aodamn.presentation.model.ToastUiModel
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.isGranted
import com.decoutkhanqindev.aodamn.presentation.navigation.MainDestination
import com.decoutkhanqindev.aodamn.presentation.navigation.PermissionDestination
import com.decoutkhanqindev.aodamn.presentation.navigation.ThemeEditorDestination
import com.decoutkhanqindev.aodamn.presentation.screens.customize.state.CustomizeEffect
import com.decoutkhanqindev.aodamn.presentation.screens.customize.state.CustomizeIntent
import com.decoutkhanqindev.aodamn.utils.collectCatching
import com.decoutkhanqindev.aodamn.utils.navigateBack
import com.decoutkhanqindev.aodamn.utils.navigateTo
import com.decoutkhanqindev.aodamn.utils.openAppSettings
import com.decoutkhanqindev.aodamn.utils.openMiuiPermissionSettings
import com.decoutkhanqindev.aodamn.utils.openNotificationSettings
import com.decoutkhanqindev.aodamn.utils.openOverlaySettings
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

@Composable
fun CustomizeScreen(
    backStack: NavBackStack<NavKey>,
    isFirstOpen: Boolean,
    rootBackStack: NavBackStack<NavKey> = backStack,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val tag = LocalTag.current
    val viewModel: CustomizeViewModel = koinViewModel { parametersOf(isFirstOpen) }
    val adsManager: AdsManager = koinInject()
    val permissionManager: PermissionManager = koinInject()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val toastState = remember { SnackbarHostState() }
    val backgroundPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        viewModel.onIntent(CustomizeIntent.Decor.BackgroundPickerResult(uri?.toString()))
    }
    val calendarPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        viewModel.onIntent(CustomizeIntent.Permission.CalendarPermissionResult(isGranted))
    }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        viewModel.onIntent(CustomizeIntent.Permission.LocationPermissionResult(isGranted))
    }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        viewModel.onIntent(CustomizeIntent.Permission.NotificationPermissionResult(isGranted))
    }

    LaunchedWithLifecycleEffect {
        viewModel.effect.filterIsInstance<CustomizeEffect.Decor.OpenBackgroundPicker>()
            .collectCatching(
                block = {
                    backgroundPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
            )
    }

    LaunchedWithLifecycleEffect {
        viewModel.effect.filterIsInstance<CustomizeEffect.Permission>().collectCatching(
            block = { effect ->
                when (effect) {
                    is CustomizeEffect.Permission.RequestCalendarPermission ->
                        calendarPermissionLauncher.launch(Manifest.permission.READ_CALENDAR)

                    is CustomizeEffect.Permission.RequestLocationPermission ->
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

                    is CustomizeEffect.Permission.OpenNotificationAccessSettings ->
                        AodNotificationListener.openAccessSettings(context) {
                            permissionManager.isGranted(PermissionValue.NOTIFICATION_ACCESS)
                        }

                    is CustomizeEffect.Permission.OpenAppSettings ->
                        context.openAppSettings { permissionManager.isGranted(effect.permission) }

                    is CustomizeEffect.Permission.OpenOverlaySettings ->
                        context.openOverlaySettings { permissionManager.isGranted(PermissionValue.OVERLAY) }

                    is CustomizeEffect.Permission.OpenMiuiPermissionSettings ->
                        context.openMiuiPermissionSettings { permissionManager.isGranted(effect.permission) }

                    is CustomizeEffect.Permission.OpenNotificationSettings ->
                        context.openNotificationSettings { permissionManager.isGranted(PermissionValue.NOTIFICATIONS) }

                    is CustomizeEffect.Permission.RequestNotificationPermission ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                }
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LaunchedWithLifecycleEffect {
        viewModel.effect.filterIsInstance<CustomizeEffect.Navigation>().collectCatching(
            block = { effect ->
                when (effect) {
                    is CustomizeEffect.Navigation.NavigateToPermission -> backStack.add(
                        PermissionDestination
                    )

                    is CustomizeEffect.Navigation.NavigateToMain ->
                        rootBackStack.navigateTo(MainDestination(isPermissionSkipped = false), preserveState = false)

                    is CustomizeEffect.Navigation.NavigateBack -> backStack.navigateBack()

                    is CustomizeEffect.Navigation.NavigateToThemeEditor -> {
                        val destination = ThemeEditorDestination(effect.themeId, focusBlock = effect.focusBlock)
                        if (rootBackStack.any { key -> key is ThemeEditorDestination }) {
                            rootBackStack.navigateBack()
                            if (effect.focusBlock != null && rootBackStack.last() is ThemeEditorDestination) {
                                rootBackStack[rootBackStack.lastIndex] = destination
                            }
                        } else {
                            rootBackStack.add(destination)
                        }
                    }
                }
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LaunchedWithLifecycleEffect {
        viewModel.effect.filterIsInstance<CustomizeEffect.ShowMessage>().collectCatching(
            block = { effect ->
                scope.launch {
                    toastState.currentSnackbarData?.dismiss()
                    toastState.showSnackbar(ToastUiModel(resources.getString(effect.messageRes), effect.isError))
                }
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LifecycleResumeEffect(Unit) {
        viewModel.onIntent(CustomizeIntent.Permission.RefreshPermissions)
        viewModel.onIntent(CustomizeIntent.Appearance.RefreshTheme)
        onPauseOrDispose { }
    }

    BackHandler(enabled = state.isApplying || state.guideStep != null || state.selectedTab != null) {
        when {
            state.isApplying -> Unit
            state.guideStep != null -> viewModel.onIntent(CustomizeIntent.Guide.DismissGuide)
            else -> viewModel.onIntent(CustomizeIntent.Panel.ClosePanel)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CustomizeContent(
            state = state,
            onIntent = viewModel::onIntent,
            bannerAd = {
                BannerAdView(
                    adUnit = {
                        if (isFirstOpen) {
                            adsManager.bannerOnboardingCustomize
                        } else {
                            adsManager.bannerCustomize
                        }
                    }
                )
            },
        )

        AppToastHost(
            hostState = toastState,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
