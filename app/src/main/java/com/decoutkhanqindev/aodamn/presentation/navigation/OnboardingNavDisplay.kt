package com.decoutkhanqindev.aodamn.presentation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.decoutkhanqindev.aodamn.ads.AdsManager
import com.decoutkhanqindev.aodamn.ads.composables.BannerAdView
import com.decoutkhanqindev.aodamn.presentation.components.AppBottomAdSlot
import com.decoutkhanqindev.aodamn.presentation.screens.customize.CustomizeScreen
import com.decoutkhanqindev.aodamn.presentation.screens.language.LanguageScreen
import com.decoutkhanqindev.aodamn.presentation.screens.permission.PermissionScreen
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.utils.navigateBack
import org.koin.compose.koinInject

// Onboarding lần đầu (Ngôn ngữ → Tùy chỉnh → Quyền) trong back stack riêng, banner chung ở đáy nằm ngoài NavDisplay nên chuyển màn
// không tải lại banner. Màn trong đây không tự chừa thanh điều hướng (đã tiêu thụ, chỗ banner chừa); rời onboarding (vào Main, màn chủ đề)
// đi bằng back stack gốc.
@Composable
fun OnboardingNavDisplay(backStack: NavBackStack<NavKey>) {
    val onboardingBackStack = rememberNavBackStack(LanguageDestination(isFirstOpen = true))
    val adsManager: AdsManager = koinInject()
    var isBannerHidden by remember { mutableStateOf(false) }
    // Tùy chỉnh có thanh tab nền Neutral12 sát banner và nền đen sau khung xem trước (lộ ra khi nhấn giữ xem AOD, banner trượt xuống).
    val isCustomize = onboardingBackStack.lastOrNull() is CustomizeDestination
    val slotColor by animateColorAsState(
        targetValue = if (isCustomize) Neutral12 else Color.Transparent,
        label = "OnboardingBannerSlot",
    )
    val underColor by animateColorAsState(
        targetValue = if (isCustomize) Black else Color.Transparent,
        label = "OnboardingBannerUnder",
    )

    Column(modifier = Modifier.fillMaxSize()) {
        NavDisplay(
            entries = rememberDecoratedNavEntries(
                backStack = onboardingBackStack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider {
                    screen<LanguageDestination> { dest ->
                        LanguageScreen(backStack = onboardingBackStack, isFirstOpen = dest.isFirstOpen)
                    }
                    screen<CustomizeDestination> { dest ->
                        CustomizeScreen(
                            backStack = onboardingBackStack,
                            isFirstOpen = dest.isFirstOpen,
                            rootBackStack = backStack,
                            onPeekingChange = { isPeeking -> isBannerHidden = isPeeking },
                        )
                    }
                    screen<PermissionDestination> { PermissionScreen(backStack = onboardingBackStack, rootBackStack = backStack) }
                },
            ),
            modifier = Modifier
                .weight(1f)
                .consumeWindowInsets(WindowInsets.navigationBars),
            onBack = { onboardingBackStack.navigateBack() },
        )

        AppBottomAdSlot(
            modifier = Modifier.background(underColor),
            isHidden = isBannerHidden && isCustomize,
            background = slotColor,
        ) {
            BannerAdView(adUnit = { adsManager.bannerOnboarding })
        }
    }
}
