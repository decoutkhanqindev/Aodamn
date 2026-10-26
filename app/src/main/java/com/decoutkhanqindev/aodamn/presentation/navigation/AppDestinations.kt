package com.decoutkhanqindev.aodamn.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import kotlinx.serialization.Serializable

@Serializable
data object SplashDestination : NavKey

@Serializable
data class MainDestination(val isPermissionSkipped: Boolean) : NavKey

@Serializable
data object OnboardingDestination : NavKey

@Serializable
data class LanguageDestination(val isFirstOpen: Boolean) : NavKey

@Serializable
data class CustomizeDestination(val isFirstOpen: Boolean) : NavKey

@Serializable
data object PermissionDestination : NavKey

@Serializable
data object IapDestination : NavKey

@Serializable
data class ThemeEditorDestination(val themeId: String, val focusBlock: AodBlockValue?) : NavKey

// TODO: Thêm destination của project — data object khi không có args, data class (field primitive/@Serializable) khi có args
