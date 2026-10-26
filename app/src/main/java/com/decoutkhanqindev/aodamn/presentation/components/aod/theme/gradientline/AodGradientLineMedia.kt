package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gradientline.GradientLineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A

@Composable
fun AodGradientLineMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val color = config[GradientLineThemeSpec.MediaColor]
    val brush = config.brushOf(GradientLineThemeSpec.MediaColor)
    val gradientBrush = config.gradientOf(GradientLineThemeSpec.MediaColor)?.toBrush()
    val textAlpha = animateFloatAsState(
        targetValue = if (media.isPlaying) 1f else GradientLineMetrics.PAUSED_ALPHA,
        animationSpec = tween(durationMillis = 400),
        label = "GradientLineTrackAlpha",
    )
    val playLabel = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GradientLineMediaButton(
            iconRes = R.drawable.ic_skip_back_fill,
            contentDescription = stringResource(R.string.aod_media_previous),
            isEnabled = media.canSkipToPrevious,
            color = color,
            gradient = gradientBrush,
            onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
        )

        AnimatedContent(
            targetState = media.title to media.artist,
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
            transitionSpec = { (slideInVertically { height -> height } + fadeIn()) togetherWith fadeOut() },
            contentAlignment = Alignment.Center,
            label = "GradientLineTrack",
        ) { (title, artist) ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GradientLineMediaText(text = title, brush = brush, fontSize = GradientLineMetrics.TitleSize, iterations = 2)

                artist?.let {
                    GradientLineMediaText(
                        text = artist,
                        brush = brush,
                        fontSize = GradientLineMetrics.ArtistSize,
                        iterations = 1,
                        modifier = Modifier
                            .padding(top = GradientLineMetrics.ArtistGap)
                            .alpha(GradientLineMetrics.ARTIST_ALPHA),
                    )
                }
            }
        }

        GradientLineMediaButton(
            iconRes = R.drawable.ic_skip_forward_fill,
            contentDescription = stringResource(R.string.aod_media_next),
            isEnabled = media.canSkipToNext,
            color = color,
            gradient = gradientBrush,
            onClick = { onIntent(AodIntent.SkipToNextTrack) },
        )
    }
}

@Composable
private fun GradientLineMediaText(
    text: String,
    brush: Brush,
    fontSize: TextUnit,
    iterations: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.basicMarquee(iterations = iterations),
        style = LocalTextStyle.current.merge(TextStyle(brush = brush)),
        fontSize = fontSize,
        fontFamily = GradientLineThemeSpec.Font,
        maxLines = 1,
    )
}

@Composable
private fun GradientLineMediaButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    isEnabled: Boolean,
    color: Color,
    gradient: Brush?,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(GradientLineMetrics.ButtonSize)
            .alpha(GradientLineMetrics.BUTTON_ALPHA)
            .then(if (isEnabled) Modifier.onClick(shape = CircleShape, ripple = false, action = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(GradientLineMetrics.ButtonIconSize)
                .aodTint(gradient.takeIf { isEnabled }),
            tint = if (isEnabled) color else Grey5A,
        )
    }
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
