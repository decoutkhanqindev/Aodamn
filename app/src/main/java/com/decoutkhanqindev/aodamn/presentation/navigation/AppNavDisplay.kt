package com.decoutkhanqindev.aodamn.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.decoutkhanqindev.aodamn.data.network.connectivity.NetworkManager
import com.decoutkhanqindev.aodamn.presentation.components.AppTagProvider
import com.decoutkhanqindev.aodamn.presentation.components.dialog.AppNoInternetDialog
import com.decoutkhanqindev.aodamn.presentation.screens.customize.CustomizeScreen
import com.decoutkhanqindev.aodamn.presentation.screens.iap.IapScreen
import com.decoutkhanqindev.aodamn.presentation.screens.language.LanguageScreen
import com.decoutkhanqindev.aodamn.presentation.screens.splash.SplashScreen
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.ThemeEditorScreen
import com.decoutkhanqindev.aodamn.presentation.theme.BackgroundGradient
import com.decoutkhanqindev.aodamn.utils.navigateBack
import com.decoutkhanqindev.aodamn.utils.openWifiSettings
import org.koin.compose.koinInject

@Composable
fun AppNavDisplay() {
    val context = LocalContext.current
    val backStack = rememberNavBackStack(SplashDestination)
    val networkManager: NetworkManager = koinInject()
    val isNetworkAvailable by networkManager.isAvailable.collectAsStateWithLifecycle()

    NavDisplay(
        entries = rememberDecoratedNavEntries(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                screen<SplashDestination> { SplashScreen(backStack) }
                screen<OnboardingDestination> { OnboardingNavDisplay(backStack) }
                screen<LanguageDestination> { dest ->
                    LanguageScreen(
                        backStack = backStack,
                        isFirstOpen = dest.isFirstOpen
                    )
                }
                screen<CustomizeDestination> { dest ->
                    CustomizeScreen(
                        backStack = backStack,
                        isFirstOpen = dest.isFirstOpen,
                    )
                }
                screen<MainDestination> { dest -> MainNavDisplay(backStack, isPermissionSkipped = dest.isPermissionSkipped) }
                screen<IapDestination> { IapScreen(backStack) }
                screen<ThemeEditorDestination> { dest ->
                    ThemeEditorScreen(backStack, themeId = dest.themeId, focusBlock = dest.focusBlock)
                }
                // TODO: Đăng ký screen<XxxDestination> { dest -> XxxScreen(...) } cho màn mới
            },
        ),
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGradient),
        onBack = { backStack.navigateBack() },
    )

    if (!isNetworkAvailable) AppNoInternetDialog {
        context.openWifiSettings { networkManager.isAvailable.value }
    }
}

// Mỗi màn được cấp LocalTag = tên class của destination (vd MainDestination), cùng ý với interface Tag của lớp thường.
// AppNavDisplay, MainNavDisplay (các tab) và OnboardingNavDisplay dùng chung.
inline fun <reified K : NavKey> EntryProviderScope<NavKey>.screen(
    crossinline content: @Composable (K) -> Unit,
) {
    entry<K> { dest ->
        AppTagProvider(tag = K::class.java.simpleName) { content(dest) }
    }
}
