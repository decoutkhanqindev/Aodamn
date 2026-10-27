package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tick

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodTextPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tick.TickThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodTickMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodTextPlayer(
        media = media,
        style = TickMetrics.TextPlayer,
        color = config[TickThemeSpec.MediaColor],
        gradient = config.gradientOf(TickThemeSpec.MediaColor)?.toBrush(),
        titleBrush = config.brushOf(TickThemeSpec.MediaColor),
        artistBrush = config.darkBrushOf(TickThemeSpec.MediaColor, TickMetrics.TEXT_DARKEN),
        onIntent = onIntent,
    )
}

@Preview(widthDp = 393)
@Composable
private fun AodTickMediaPreview() {
    AodTickMedia(
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
