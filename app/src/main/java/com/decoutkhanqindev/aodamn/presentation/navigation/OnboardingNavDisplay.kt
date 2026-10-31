package com.decoutkhanqindev.aodamn.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.decoutkhanqindev.aodamn.presentation.screens.customize.CustomizeScreen
import com.decoutkhanqindev.aodamn.presentation.screens.language.LanguageScreen
import com.decoutkhanqindev.aodamn.presentation.screens.permission.PermissionScreen
import com.decoutkhanqindev.aodamn.utils.navigateBack

@Composable
fun OnboardingNavDisplay(backStack: NavBackStack<NavKey>) {
    val onboardingBackStack = rememberNavBackStack(LanguageDestination(isFirstOpen = true))

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
                    )
                }
                screen<PermissionDestination> {
                    PermissionScreen(backStack = onboardingBackStack, rootBackStack = backStack)
                }
            },
        ),
        modifier = Modifier.fillMaxSize(),
        onBack = { onboardingBackStack.navigateBack() },
    )
}
