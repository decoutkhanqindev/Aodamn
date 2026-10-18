package com.decoutkhanqindev.aodamn.presentation.model.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.navigation.AodTabDestination
import com.decoutkhanqindev.aodamn.presentation.navigation.EdgeLightDestination
import com.decoutkhanqindev.aodamn.presentation.navigation.SettingsDestination

// Các tab của Main, đúng thứ tự trên thanh điều hướng dưới; tab đầu là gốc của back stack các tab.
@Immutable
enum class MainTabValue(
    val destination: NavKey,
    @param:DrawableRes val iconRes: Int,
    @param:DrawableRes val selectedIconRes: Int,
    @param:StringRes val labelRes: Int,
    @param:StringRes val titleRes: Int,
) {
    AOD(
        destination = AodTabDestination,
        iconRes = R.drawable.ic_clock,
        selectedIconRes = R.drawable.ic_clock_fill,
        labelRes = R.string.main_tab_aod,
        titleRes = R.string.aod_tab_title,
    ),
    EDGE_LIGHT(
        destination = EdgeLightDestination,
        iconRes = R.drawable.ic_device_mobile,
        selectedIconRes = R.drawable.ic_device_mobile_fill,
        labelRes = R.string.edge_light,
        titleRes = R.string.edge_light,
    ),
    SETTINGS(
        destination = SettingsDestination,
        iconRes = R.drawable.ic_gear_six,
        selectedIconRes = R.drawable.ic_gear_six_fill,
        labelRes = R.string.settings,
        titleRes = R.string.settings,
    ),
}
