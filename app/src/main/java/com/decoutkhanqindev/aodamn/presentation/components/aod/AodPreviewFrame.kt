package com.decoutkhanqindev.aodamn.presentation.components.aod

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodPreviewFrame(
    state: AodState,
    isLandscape: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!isLandscape) {
        AodContent(state = state, onIntent = {}, modifier = modifier, isInteractive = false, backdropRotationZ = 0f)
        return
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .requiredSize(width = maxHeight, height = maxWidth)
                .graphicsLayer { rotationZ = 90f },
        ) {
            AodContent(
                state = state,
                onIntent = {},
                modifier = Modifier.fillMaxSize(),
                isInteractive = false,
                backdropRotationZ = -90f,
            )
        }
    }
}
