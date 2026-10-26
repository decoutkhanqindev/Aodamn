package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.halfmoon

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodTextPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.halfmoon.HalfMoonThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodHalfMoonMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val brush = config.brushOf(HalfMoonThemeSpec.MediaColor)

    AodTextPlayer(
        media = media,
        style = HalfMoonMetrics.TextPlayer,
        color = config[HalfMoonThemeSpec.MediaColor],
        gradient = config.gradientOf(HalfMoonThemeSpec.MediaColor)?.toBrush(),
        titleBrush = brush,
        artistBrush = brush,
        onIntent = onIntent,
    )
}

@Preview(widthDp = 393)
@Composable
private fun AodHalfMoonMediaPreview() {
    AodHalfMoonMedia(
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
