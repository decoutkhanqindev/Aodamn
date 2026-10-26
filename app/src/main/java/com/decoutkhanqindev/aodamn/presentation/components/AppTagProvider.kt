package com.decoutkhanqindev.aodamn.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalTag = staticCompositionLocalOf { "App" }

@Composable
fun AppTagProvider(tag: String, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalTag provides tag, content = content)
}
