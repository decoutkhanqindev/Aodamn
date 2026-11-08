package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.poster

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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.poster.PosterThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodPosterMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AodTextPlayer(
            media = media,
            style = PosterMetrics.TextPlayer,
            color = config[PosterThemeSpec.MediaColor],
            gradient = config.gradientOf(PosterThemeSpec.MediaColor)?.toBrush(),
            titleBrush = config.brushOf(PosterThemeSpec.MediaColor),
            artistBrush = config.darkBrushOf(PosterThemeSpec.MediaColor, PosterMetrics.ARTIST_DARKEN),
            onIntent = onIntent,
            modifier = Modifier.padding(horizontal = PosterMetrics.PlayerMargin),
        )

        media.progress?.let { progress ->
            AodFlatProgress(
                progress = progress,
                color = config[PosterThemeSpec.MediaColor],
                brush = config.brushOf(PosterThemeSpec.MediaColor),
                trackHeight = PosterMetrics.TrackHeight,
                trackAlpha = PosterMetrics.TRACK_ALPHA,
                modifier = Modifier.padding(
                    start = PosterMetrics.ProgressInset,
                    top = PosterMetrics.ProgressGap,
                    end = PosterMetrics.ProgressInset,
                ),
            )
        }
    }
}

@Preview(widthDp = 393)
@Composable
private fun AodPosterMediaPreview() {
    AodPosterMedia(
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
