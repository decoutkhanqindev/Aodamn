package com.decoutkhanqindev.custom_aod.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object SplashDestination : NavKey

// isPermissionSkipped: vào từ nút Bỏ qua ở màn quyền onboarding, tab AOD không tự hiện sheet quyền trong phiên này.
@Serializable
data class MainDestination(val isPermissionSkipped: Boolean) : NavKey

@Serializable
data class LanguageDestination(val isFirstOpen: Boolean) : NavKey

@Serializable
data class CustomizeDestination(val isFirstOpen: Boolean) : NavKey

@Serializable
data object PermissionDestination : NavKey

@Serializable
data object IapDestination : NavKey

@Serializable
data class ThemeEditorDestination(val themeId: String) : NavKey

// TODO: Thêm destination của project — data object khi không có args, data class (field primitive/@Serializable) khi có args
