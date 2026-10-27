package com.decoutkhanqindev.aodamn.presentation.components.aod.block

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.AodWaveTrack
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodCompactPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A

@Composable
fun AodCompactPlayer(
    media: MediaUiModel,
    config: AodThemeConfig,
    colorOption: AodColorOption,
    style: AodCompactPlayerStyle,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = config[colorOption]
    val brush = config.brushOf(colorOption)
    val gradientBrush = config.gradientOf(colorOption)?.toBrush()
    val progress = media.progress

    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedContent(
                targetState = media.title to media.artist,
                modifier = Modifier.weight(1f),
                transitionSpec = { (slideInVertically { height -> height } + fadeIn()) togetherWith fadeOut() },
                contentAlignment = Alignment.CenterStart,
                label = "AodCompactPlayerTrack",
            ) { (title, artist) ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    CompactPlayerText(text = title, brush = brush, fontSize = style.titleSize, style = style, iterations = 2)

                    artist?.let {
                        CompactPlayerText(
                            text = artist,
                            brush = config.darkBrushOf(colorOption, style.artistDarken),
                            fontSize = style.artistSize,
                            style = style,
                            iterations = 1,
                            modifier = Modifier.padding(top = style.artistGap),
                        )
                    }
                }
            }

            CompactPlayerButton(
                iconRes = if (media.isPlaying) R.drawable.ic_pause_fill else R.drawable.ic_play_fill,
                contentDescription = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play),
                isEnabled = true,
                color = color,
                gradient = gradientBrush,
                size = style.playButton,
                iconSize = style.playIcon,
                onClick = { onIntent(AodIntent.PlayPauseMedia) },
                modifier = Modifier.padding(start = style.playStartGap, end = style.playEndGap),
            )

            CompactPlayerButton(
                iconRes = R.drawable.ic_skip_forward_fill,
                contentDescription = stringResource(R.string.aod_media_next),
                isEnabled = media.canSkipToNext,
                color = color,
                gradient = gradientBrush,
                size = style.nextButton,
                iconSize = style.nextIcon,
                onClick = { onIntent(AodIntent.SkipToNextTrack) },
                modifier = Modifier.padding(start = style.nextStartGap),
            )
        }

        if (progress != null) {
            AodMediaProgress(
                progress = progress,
                track = { fraction ->
                    AodWaveTrack(
                        fraction = fraction,
                        wave = { 0f },
                        color = color,
                        trackColor = color.copy(alpha = color.alpha * style.trackAlpha),
                        brush = brush,
                        height = style.trackHeight,
                        strokeWidth = style.trackHeight,
                        wavelength = 40.dp,
                        gap = 0.dp,
                    )
                },
                modifier = Modifier.padding(top = style.progressGap),
            )
        }
    }
}

@Composable
private fun CompactPlayerText(
    text: String,
    brush: Brush,
    fontSize: TextUnit,
    style: AodCompactPlayerStyle,
    iterations: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.basicMarquee(iterations = iterations),
        style = TextStyle(
            brush = brush,
            fontSize = fontSize,
            fontFamily = style.font,
            letterSpacing = style.letterSpacing,
        ),
        maxLines = 1,
    )
}

@Composable
private fun CompactPlayerButton(
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
