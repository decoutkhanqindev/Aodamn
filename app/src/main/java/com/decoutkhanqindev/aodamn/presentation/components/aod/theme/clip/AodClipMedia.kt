package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.clip

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodTextPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.clip.ClipThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodClipMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodTextPlayer(
        media = media,
        style = ClipMetrics.TextPlayer,
        color = config[ClipThemeSpec.MediaColor],
        gradient = config.gradientOf(ClipThemeSpec.MediaColor)?.toBrush(),
        titleBrush = config.brushOf(ClipThemeSpec.MediaColor),
        artistBrush = config.darkBrushOf(ClipThemeSpec.MediaColor, ClipMetrics.TEXT_DARKEN),
        onIntent = onIntent,
    )
}

@Preview(widthDp = 393)
@Composable
private fun AodClipMediaPreview() {
    AodClipMedia(
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
