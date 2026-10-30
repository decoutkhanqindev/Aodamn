package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.orbit

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodTextPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.orbit.OrbitThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodOrbitMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodTextPlayer(
        media = media,
        style = OrbitMetrics.TextPlayer,
        color = config[OrbitThemeSpec.MediaColor],
        gradient = config.gradientOf(OrbitThemeSpec.MediaColor)?.toBrush(),
        titleBrush = config.brushOf(OrbitThemeSpec.MediaColor),
        artistBrush = config.darkBrushOf(OrbitThemeSpec.MediaColor, OrbitMetrics.TEXT_DARKEN),
        onIntent = onIntent,
    )
}

@Preview(widthDp = 313)
@Composable
private fun AodOrbitMediaPreview() {
    AodOrbitMedia(
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
