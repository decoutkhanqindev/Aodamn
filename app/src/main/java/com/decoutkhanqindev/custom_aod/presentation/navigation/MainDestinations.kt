package com.decoutkhanqindev.custom_aod.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object AodTabDestination : NavKey

@Serializable
data object EdgeLightDestination : NavKey

@Serializable
data object SettingsDestination : NavKey
