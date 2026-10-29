package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.frame

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodTextPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.frame.FrameThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodFrameMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodTextPlayer(
        media = media,
        style = FrameMetrics.TextPlayer,
        color = config[FrameThemeSpec.MediaColor],
        gradient = config.gradientOf(FrameThemeSpec.MediaColor)?.toBrush(),
        titleBrush = config.brushOf(FrameThemeSpec.MediaColor),
        artistBrush = config.brushOf(FrameThemeSpec.MediaColor),
        onIntent = onIntent,
    )
}

@Preview(widthDp = 333)
@Composable
private fun AodFrameMediaPreview() {
    AodFrameMedia(
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
