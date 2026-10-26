package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bezel

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWavePlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bezel.BezelThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodBezelMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodWavePlayer(
        media = media,
        style = BezelMetrics.WavePlayer,
        color = config[BezelThemeSpec.MediaColor],
        gradient = config.gradientOf(BezelThemeSpec.MediaColor)?.toBrush(),
        brush = config.brushOf(BezelThemeSpec.MediaColor),
        onIntent = onIntent,
    )
}

@Preview(widthDp = 393)
@Composable
private fun AodBezelMediaPreview() {
    AodBezelMedia(
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
