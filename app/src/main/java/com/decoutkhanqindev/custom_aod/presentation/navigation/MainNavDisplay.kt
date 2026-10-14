package com.decoutkhanqindev.custom_aod.presentation.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.ads.AdsManager
import com.decoutkhanqindev.custom_aod.ads.composables.BannerAdView
import com.decoutkhanqindev.custom_aod.presentation.components.AppBottomAdSlot
import com.decoutkhanqindev.custom_aod.presentation.model.main.MainTabValue
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.AodTabScreen
import com.decoutkhanqindev.custom_aod.presentation.screens.edgelight.EdgeLightScreen
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.SettingsScreen
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.HeadlineSmall
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.MintAlpha12
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.utils.navigateBack
import com.decoutkhanqindev.custom_aod.utils.navigateTo
import com.decoutkhanqindev.custom_aod.utils.playStoreUrl
import com.decoutkhanqindev.custom_aod.utils.shareText
import org.koin.compose.koinInject

@Composable
fun MainNavDisplay(
    backStack: NavBackStack<NavKey>,
    isPermissionSkipped: Boolean,
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    val tabBackStack = rememberNavBackStack(MainTabValue.AOD.destination)
    val adsManager: AdsManager = koinInject()
    val selectedTab =
        MainTabValue.entries.find { tab -> tab.destination == tabBackStack.lastOrNull() }
            ?: MainTabValue.AOD

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainTopBar(
                title = stringResource(selectedTab.titleRes),
                onIapClick = { backStack.add(IapDestination) },
                onShareClick = {
                    context.shareText(
                        text = resources.getString(R.string.share_app_text, context.playStoreUrl()),
                        chooserTitle = resources.getString(R.string.share_app),
                    )
                },
            )
        },
        containerColor = Color.Transparent,
        contentColor = GreyED,
        bottomBar = {
            Column {
                MainNavigationBar(
                    selectedTab = selectedTab,
                    onSelectTab = { tab -> tabBackStack.selectTab(tab) },
                )

                AppBottomAdSlot {
                    BannerAdView(adUnit = { adsManager.bannerMain })
                }
            }
        },
    ) { innerPadding ->
        NavDisplay(
            entries = rememberDecoratedNavEntries(
                backStack = tabBackStack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider {
                    screen<AodTabDestination> { AodTabScreen(backStack, isPermissionSkipped = isPermissionSkipped) }
                    screen<EdgeLightDestination> { EdgeLightScreen() }
                    screen<SettingsDestination> { SettingsScreen(backStack) }
                },
            ),
            modifier = Modifier.padding(innerPadding),
            transitionSpec = { tabTransition() },
            popTransitionSpec = { tabTransition() },
            predictivePopTransitionSpec = { _ -> tabTransition() },
            onBack = { tabBackStack.navigateBack() },
        )
    }
}

// Tiêu đề đổi theo tab (mờ dần như nội dung tab), IAP và Share giữ nguyên khi đổi tab. Tiêu đề lệch 4dp để thẳng mép 20dp với
// nội dung tab.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainTopBar(
    title: String,
    onIapClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    TopAppBar(
        title = {
            AnimatedContent(
                targetState = title,
                transitionSpec = { tabTransition() },
                label = "MainTopBarTitle",
            ) { target ->
                Text(
                    text = target,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .semantics { heading() },
                    fontWeight = FontWeight.SemiBold,
                    style = HeadlineSmall,
                )
            }
        },
        actions = {
            IconButton(onClick = onIapClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_crown_simple),
                    contentDescription = stringResource(R.string.iap_title),
                )
            }

            IconButton(onClick = onShareClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_share_network),
                    contentDescription = stringResource(R.string.share_app),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = GreyED,
            actionIconContentColor = GreyED,
        ),
    )
}

@Composable
private fun MainNavigationBar(
    selectedTab: MainTabValue,
    onSelectTab: (MainTabValue) -> Unit,
) {
    NavigationBar(containerColor = Neutral12, windowInsets = WindowInsets(0)) {
        MainTabValue.entries.forEach { tab ->
            val isSelected = tab == selectedTab

            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectTab(tab) },
                icon = {
                    Icon(
                        painter = painterResource(if (isSelected) tab.selectedIconRes else tab.iconRes),
                        contentDescription = null,
                    )
                },
                label = { Text(text = stringResource(tab.labelRes)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Mint,
                    selectedTextColor = GreyED,
                    indicatorColor = MintAlpha12,
                    unselectedIconColor = Grey9A,
                    unselectedTextColor = Grey9A,
                ),
            )
        }
    }
}

// Tab AOD luôn là gốc, tab khác chồng lên nó: Back ở tab khác về tab AOD rồi mới thoát app, và tab AOD giữ nguyên trạng thái.
private fun NavBackStack<NavKey>.selectTab(tab: MainTabValue) {
    if (lastOrNull() == tab.destination) return
    navigateTo(MainTabValue.AOD.destination)
    if (tab != MainTabValue.AOD) add(tab.destination)
}

private fun tabTransition(): ContentTransform =
    fadeIn(animationSpec = tween(durationMillis = 220)) togetherWith
            fadeOut(animationSpec = tween(durationMillis = 90))
