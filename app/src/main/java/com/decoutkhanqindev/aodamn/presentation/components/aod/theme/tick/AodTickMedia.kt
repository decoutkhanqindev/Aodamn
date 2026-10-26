package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tick

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodTextPlayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tick.TickThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlinx.collections.immutable.toImmutableList

@Composable
fun AodTickMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    AodTextPlayer(
        media = media,
        style = TickMetrics.TextPlayer,
        color = config[TickThemeSpec.MediaColor],
        gradient = config.gradientOf(TickThemeSpec.MediaColor)?.toBrush(),
        titleBrush = config.brushOf(TickThemeSpec.MediaColor),
        artistBrush = config.darkerBrushOf(TickThemeSpec.MediaColor),
        onIntent = onIntent,
    )
}

private fun AodThemeConfig.darkerBrushOf(option: AodColorOption): Brush {
    val gradient = gradientOf(option) ?: return SolidColor(lerp(get(option), Black, TickMetrics.TEXT_DARKEN))
    return gradient.copy(colors = gradient.colors.map { color -> lerp(color, Black, TickMetrics.TEXT_DARKEN) }.toImmutableList()).toBrush()
}

@Preview(widthDp = 393)
@Composable
private fun AodTickMediaPreview() {
    AodTickMedia(
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
