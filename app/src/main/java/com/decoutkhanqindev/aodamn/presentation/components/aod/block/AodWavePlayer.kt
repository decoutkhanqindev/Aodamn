package com.decoutkhanqindev.aodamn.presentation.components.aod.block

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.AodWaveTrack
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodWavePlayerStyle
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A

@Composable
fun AodWavePlayer(
    media: MediaUiModel,
    style: AodWavePlayerStyle,
    color: Color,
    gradient: Brush?,
    brush: Brush,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress = media.progress
    val textAlpha = animateFloatAsState(
        targetValue = if (media.isPlaying) 1f else style.pausedAlpha,
        animationSpec = tween(durationMillis = 400),
        label = "AodWavePlayerAlpha",
    )
    val playLabel = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play)

    Column(modifier = modifier.width(style.width)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            WavePlayerButton(
                iconRes = R.drawable.ic_skip_back_fill,
                contentDescription = stringResource(R.string.aod_media_previous),
                isEnabled = media.canSkipToPrevious,
                style = style,
                color = color,
                gradient = gradient,
                onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
                modifier = Modifier.padding(start = style.buttonOuterGap, end = style.buttonInnerGap),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .alpha(textAlpha.value)
                    .onClick(ripple = false) { onIntent(AodIntent.PlayPauseMedia) }
                    .semantics {
                        role = Role.Button
                        onClick(label = playLabel) {
                            onIntent(AodIntent.PlayPauseMedia)
                            true
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                WavePlayerText(
                    text = if (style.isUppercase) media.title.uppercase() else media.title,
                    brush = brush,
                    fontSize = style.titleSize,
                    fontFamily = style.titleFont,
                    letterSpacing = style.titleSpacing,
                    style = style,
                )

                media.artist?.let { artist ->
                    WavePlayerText(
                        text = if (style.isUppercase) artist.uppercase() else artist,
                        brush = brush,
                        fontSize = style.artistSize,
                        fontFamily = style.artistFont,
                        letterSpacing = style.artistSpacing,
                        style = style,
                        modifier = Modifier
                            .padding(top = style.artistGap)
                            .alpha(style.artistAlpha),
                    )
                }
            }

            WavePlayerButton(
                iconRes = R.drawable.ic_skip_forward_fill,
                contentDescription = stringResource(R.string.aod_media_next),
                isEnabled = media.canSkipToNext,
                style = style,
                color = color,
                gradient = gradient,
                onClick = { onIntent(AodIntent.SkipToNextTrack) },
                modifier = Modifier.padding(start = style.buttonInnerGap, end = style.buttonOuterGap),
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
                modifier = Modifier.padding(start = style.progressInset, top = style.progressGap, end = style.progressInset),
            )
        }
    }
}

@Composable
private fun WavePlayerText(
    text: String,
    brush: Brush,
    fontSize: TextUnit,
    fontFamily: FontFamily,
    letterSpacing: TextUnit,
    style: AodWavePlayerStyle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        style = LocalTextStyle.current.merge(TextStyle(brush = brush)),
        fontSize = fontSize,
        fontWeight = style.textWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun WavePlayerButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    isEnabled: Boolean,
    style: AodWavePlayerStyle,
    color: Color,
    gradient: Brush?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(style.buttonSize)
            .then(if (isEnabled) Modifier.onClick(shape = CircleShape, ripple = false, action = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(style.buttonIconSize)
                .aodTint(gradient.takeIf { isEnabled }),
            tint = if (isEnabled) color else Grey5A,
        )
    }
}
