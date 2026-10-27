package com.decoutkhanqindev.aodamn.presentation.components.aod.block

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.AodWaveTrack
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A

@Composable
fun AodPlayerButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    isEnabled: Boolean,
    color: Color,
    gradient: Brush?,
    size: Dp,
    iconSize: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .then(if (isEnabled) Modifier.onClick(shape = CircleShape, ripple = false, action = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(iconSize)
                .aodTint(gradient.takeIf { isEnabled }),
            tint = if (isEnabled) color else Grey5A,
        )
    }
}

@Composable
fun AodFlatProgress(
    progress: MediaProgressUiModel,
    color: Color,
    brush: Brush,
    trackHeight: Dp,
    trackAlpha: Float,
    modifier: Modifier = Modifier,
) {
    AodMediaProgress(
        progress = progress,
        track = { fraction ->
            AodWaveTrack(
                fraction = fraction,
                wave = { 0f },
                color = color,
                trackColor = color.copy(alpha = color.alpha * trackAlpha),
                brush = brush,
                height = trackHeight,
                strokeWidth = trackHeight,
                wavelength = 40.dp,
                gap = 0.dp,
            )
        },
        modifier = modifier,
    )
}
