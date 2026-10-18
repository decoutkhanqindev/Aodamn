package com.decoutkhanqindev.aodamn.presentation.screens.iap

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.aodamn.utils.navigateBack

// TODO: Thêm IapViewModel + state/ và Play Billing khi làm Premium.
@Composable
fun IapScreen(backStack: NavBackStack<NavKey>) {
    IapContent(onNavigateBack = { backStack.navigateBack() })
}
