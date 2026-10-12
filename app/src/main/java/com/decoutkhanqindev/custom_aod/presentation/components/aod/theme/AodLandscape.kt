package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme

import androidx.compose.runtime.Composable
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.classic.AodClassicLandscape
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.jersey.AodJerseyLandscape
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.pebble.AodPebbleLandscape
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState

// Màn ngang theo chủ đề: mỗi chủ đề một bố cục riêng (màn dọc dùng chung: đồng hồ trên, các khối dưới). Thêm chủ đề = thêm một nhánh ở đây.
@Composable
fun AodLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    when (state.appearance.theme.value) {
        AodThemeValue.CLASSIC -> AodClassicLandscape(state = state, onIntent = onIntent)
        AodThemeValue.PEBBLE -> AodPebbleLandscape(state = state, onIntent = onIntent)
        AodThemeValue.JERSEY -> AodJerseyLandscape(state = state)
    }
}
