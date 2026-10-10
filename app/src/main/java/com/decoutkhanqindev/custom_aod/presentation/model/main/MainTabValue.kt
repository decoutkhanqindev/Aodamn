package com.decoutkhanqindev.custom_aod.presentation.model.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.EdgesensorHigh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.EdgesensorHigh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.navigation.AodTabDestination
import com.decoutkhanqindev.custom_aod.presentation.navigation.EdgeLightDestination
import com.decoutkhanqindev.custom_aod.presentation.navigation.SettingsDestination

// Các tab của Main, đúng thứ tự trên thanh điều hướng dưới; tab đầu là gốc của back stack các tab.
@Immutable
enum class MainTabValue(
    val destination: NavKey,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    @param:StringRes val labelRes: Int,
) {
    AOD(
        destination = AodTabDestination,
        icon = Icons.Outlined.AccessTime,
        selectedIcon = Icons.Filled.AccessTimeFilled,
        labelRes = R.string.main_tab_aod,
    ),
    EDGE_LIGHT(
        destination = EdgeLightDestination,
        icon = Icons.Outlined.EdgesensorHigh,
        selectedIcon = Icons.Filled.EdgesensorHigh,
        labelRes = R.string.edge_light,
    ),
    SETTINGS(
        destination = SettingsDestination,
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings,
        labelRes = R.string.settings,
    ),
}
