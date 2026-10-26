package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodTextPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gradientline.GradientLineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodGradientLineMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val brush = config.brushOf(GradientLineThemeSpec.MediaColor)

    AodTextPlayer(
        media = media,
        style = GradientLineMetrics.TextPlayer,
        color = config[GradientLineThemeSpec.MediaColor],
        gradient = config.gradientOf(GradientLineThemeSpec.MediaColor)?.toBrush(),
        titleBrush = brush,
        artistBrush = brush,
        onIntent = onIntent,
    )
}

@Preview(widthDp = 313)
@Composable
private fun AodGradientLineMediaPreview() {
    AodGradientLineMedia(
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
