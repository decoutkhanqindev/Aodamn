package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.fade

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodCompactPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.fade.FadeThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodFadeMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodCompactPlayer(
        media = media,
        config = config,
        colorOption = FadeThemeSpec.MediaColor,
        style = FadeMetrics.CompactPlayer,
        onIntent = onIntent,
    )
}

@Preview(widthDp = 313)
@Composable
private fun AodFadeMediaPreview() {
    AodFadeMedia(
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
