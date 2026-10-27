package com.decoutkhanqindev.aodamn.presentation.components.aod.theme

import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

@Composable
fun resolveTypeface(
    fontFamily: FontFamily,
    fontWeight: FontWeight,
    fallback: Typeface,
): Typeface = LocalFontFamilyResolver.current
    .resolve(fontFamily = fontFamily, fontWeight = fontWeight)
    .value as? Typeface ?: fallback
