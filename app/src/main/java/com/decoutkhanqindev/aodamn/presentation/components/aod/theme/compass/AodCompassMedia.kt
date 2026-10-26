package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodTextPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.CompassThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodCompassMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val brush = config.brushOf(CompassThemeSpec.MediaColor)

    AodTextPlayer(
        media = media,
        style = CompassMetrics.TextPlayer,
        color = config[CompassThemeSpec.MediaColor],
        gradient = config.gradientOf(CompassThemeSpec.MediaColor)?.toBrush(),
        titleBrush = brush,
        artistBrush = brush,
        onIntent = onIntent,
    )
}

@Preview(widthDp = 393)
@Composable
private fun AodCompassMediaPreview() {
    AodCompassMedia(
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
