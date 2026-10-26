package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tower

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWavePlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tower.TowerThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodTowerMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodWavePlayer(
        media = media,
        style = TowerMetrics.WavePlayer,
        color = config[TowerThemeSpec.MediaColor],
        gradient = config.gradientOf(TowerThemeSpec.MediaColor)?.toBrush(),
        brush = config.brushOf(TowerThemeSpec.MediaColor),
        onIntent = onIntent,
        modifier = Modifier.padding(top = 20.dp),
    )
}

@Preview(widthDp = 393)
@Composable
private fun AodTowerMediaPreview() {
    AodTowerMedia(
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
