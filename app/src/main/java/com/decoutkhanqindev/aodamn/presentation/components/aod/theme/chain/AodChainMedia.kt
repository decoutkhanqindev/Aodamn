package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.chain

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWavePlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.chain.ChainThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodChainMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodWavePlayer(
        media = media,
        style = ChainMetrics.WavePlayer,
        color = config[ChainThemeSpec.MediaColor],
        gradient = config.gradientOf(ChainThemeSpec.MediaColor)?.toBrush(),
        brush = config.brushOf(ChainThemeSpec.MediaColor),
        onIntent = onIntent,
    )
}

@Preview(widthDp = 393)
@Composable
private fun AodChainMediaPreview() {
    AodChainMedia(
        media = MediaUiModel(
            title = "That Cool Song",
            artist = "It's Artist",
            isPlaying = true,
            canSkipToPrevious = true,
            canSkipToNext = true,
            progress = MediaProgressUiModel(
                durationMillis = 180_000L,
                positionMillis = 75_000L,
                updatedAtMillis = 0L,
                speed = 0f,
            ),
        ),
        config = AodThemeConfig(),
        onIntent = {},
    )
}
