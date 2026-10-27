package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.spine

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodCompactPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.spine.SpineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodSpineMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodCompactPlayer(
        media = media,
        config = config,
        colorOption = SpineThemeSpec.MediaColor,
        style = SpineMetrics.CompactPlayer,
        onIntent = onIntent,
    )
}

@Preview(widthDp = 303)
@Composable
private fun AodSpineMediaPreview() {
    AodSpineMedia(
        media = MediaUiModel(
            title = "That Cool Song",
            artist = "It's Artist",
            isPlaying = true,
            canSkipToPrevious = true,
            canSkipToNext = true,
        ),
        config = AodThemeConfig(),
        onIntent = {},
    )
}
