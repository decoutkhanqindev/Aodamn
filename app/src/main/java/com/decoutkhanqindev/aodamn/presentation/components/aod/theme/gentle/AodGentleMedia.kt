package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gentle

import androidx.compose.runtime.Composable
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodCompactPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gentle.GentleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodGentleMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodCompactPlayer(
        media = media,
        config = config,
        colorOption = GentleThemeSpec.MediaColor,
        style = GentleMetrics.CompactPlayer,
        onIntent = onIntent,
    )
}
