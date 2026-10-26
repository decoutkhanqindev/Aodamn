package com.decoutkhanqindev.aodamn.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Mint,
    onPrimary = Black,
    background = Black,
    onBackground = GreyED,
    surface = Black,
    onSurface = GreyED,
    surfaceVariant = NeutralVariant30,
    onSurfaceVariant = Grey9A,
    surfaceContainer = Neutral12,
    outline = NeutralVariant60,
    outlineVariant = NeutralVariant30,
    error = Red,
)

@Composable
fun Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = MaterialTypography,
        shapes = MaterialShapes,
        content = content,
    )
}
