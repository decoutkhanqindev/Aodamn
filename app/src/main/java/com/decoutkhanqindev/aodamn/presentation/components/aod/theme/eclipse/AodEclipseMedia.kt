package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.eclipse

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodTextPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.eclipse.EclipseThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodEclipseMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodTextPlayer(
        media = media,
        style = EclipseMetrics.TextPlayer,
        color = config[EclipseThemeSpec.MediaColor],
        gradient = config.gradientOf(EclipseThemeSpec.MediaColor)?.toBrush(),
        titleBrush = config.brushOf(EclipseThemeSpec.MediaColor),
        artistBrush = config.darkBrushOf(EclipseThemeSpec.MediaColor, EclipseMetrics.TEXT_DARKEN),
        onIntent = onIntent,
        modifier = Modifier.padding(horizontal = EclipseMetrics.PlayerMargin),
    )
}

@Preview(widthDp = 393)
@Composable
private fun AodEclipseMediaPreview() {
    AodEclipseMedia(
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
