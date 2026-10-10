package com.decoutkhanqindev.custom_aod.presentation.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.decoutkhanqindev.custom_aod.presentation.model.main.MainTabValue
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.AodTabScreen
import com.decoutkhanqindev.custom_aod.presentation.screens.edgelight.EdgeLightScreen
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.SettingsScreen
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.MintAlpha12
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.utils.navigateBack
import com.decoutkhanqindev.custom_aod.utils.navigateTo

@Composable
fun MainNavDisplay(
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
) {
    val tabBackStack = rememberNavBackStack(MainTabValue.AOD.destination)
    val selectedTab = MainTabValue.entries.find { tab -> tab.destination == tabBackStack.lastOrNull() }
        ?: MainTabValue.AOD

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MainNavigationBar(
                selectedTab = selectedTab,
                onSelectTab = { tab -> tabBackStack.selectTab(tab) },
            )
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
                    screen<AodTabDestination> { AodTabScreen(backStack) }
                    screen<EdgeLightDestination> { EdgeLightScreen(backStack) }
                    screen<SettingsDestination> { SettingsScreen(backStack) }
                },
            ),
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            transitionSpec = { tabTransition() },
            popTransitionSpec = { tabTransition() },
            predictivePopTransitionSpec = { _ -> tabTransition() },
            onBack = { tabBackStack.navigateBack() },
        )
    }
}

@Composable
private fun MainNavigationBar(
    selectedTab: MainTabValue,
    onSelectTab: (MainTabValue) -> Unit,
) {
    NavigationBar(containerColor = Neutral12) {
        MainTabValue.entries.forEach { tab ->
            val isSelected = tab == selectedTab

            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectTab(tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.icon,
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
