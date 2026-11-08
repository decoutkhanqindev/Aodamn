package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pixel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodFlatProgress
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodTextPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pixel.PixelThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodPixelMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AodTextPlayer(
            media = media,
            style = PixelMetrics.TextPlayer,
            color = config[PixelThemeSpec.MediaColor],
            gradient = config.gradientOf(PixelThemeSpec.MediaColor)?.toBrush(),
            titleBrush = config.brushOf(PixelThemeSpec.MediaColor),
            artistBrush = config.darkBrushOf(PixelThemeSpec.MediaColor, PixelMetrics.ARTIST_DARKEN),
            onIntent = onIntent,
            modifier = Modifier.padding(horizontal = PixelMetrics.PlayerMargin),
        )

        media.progress?.let { progress ->
            AodFlatProgress(
                progress = progress,
                color = config[PixelThemeSpec.MediaColor],
                brush = config.brushOf(PixelThemeSpec.MediaColor),
                trackHeight = PixelMetrics.TrackHeight,
                trackAlpha = PixelMetrics.TRACK_ALPHA,
                modifier = Modifier.padding(
                    start = PixelMetrics.ProgressInset,
                    top = PixelMetrics.ProgressGap,
                    end = PixelMetrics.ProgressInset,
                ),
            )
        }
    }
}

@Preview(widthDp = 393)
@Composable
private fun AodPixelMediaPreview() {
    AodPixelMedia(
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
