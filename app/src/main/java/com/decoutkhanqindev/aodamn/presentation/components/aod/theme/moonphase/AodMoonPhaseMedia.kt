package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.moonphase

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodTextPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.moonphase.MoonPhaseThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodMoonPhaseMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodTextPlayer(
        media = media,
        style = MoonPhaseMetrics.TextPlayer,
        color = config[MoonPhaseThemeSpec.MediaColor],
        gradient = config.gradientOf(MoonPhaseThemeSpec.MediaColor)?.toBrush(),
        titleBrush = config.brushOf(MoonPhaseThemeSpec.MediaColor),
        artistBrush = config.darkBrushOf(MoonPhaseThemeSpec.MediaColor, MoonPhaseMetrics.ARTIST_DARKEN),
        onIntent = onIntent,
        modifier = Modifier.padding(horizontal = MoonPhaseMetrics.PlayerMargin),
    )
}
