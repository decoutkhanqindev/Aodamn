package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.mono

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodCompactPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.mono.MonoThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodMonoMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodCompactPlayer(
        media = media,
        config = config,
        colorOption = MonoThemeSpec.MediaColor,
        style = MonoMetrics.CompactPlayer,
        onIntent = onIntent,
    )
}

@Preview(widthDp = 326)
@Composable
private fun AodMonoMediaPreview() {
    AodMonoMedia(
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
