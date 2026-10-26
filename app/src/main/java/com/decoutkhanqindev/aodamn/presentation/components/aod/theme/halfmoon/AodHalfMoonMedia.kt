package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.halfmoon

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.halfmoon.HalfMoonThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A

@Composable
fun AodHalfMoonMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val color = config[HalfMoonThemeSpec.MediaColor]
    val brush = config.brushOf(HalfMoonThemeSpec.MediaColor)
    val gradientBrush = config.gradientOf(HalfMoonThemeSpec.MediaColor)?.toBrush()
    val textAlpha = animateFloatAsState(
        targetValue = if (media.isPlaying) 1f else HalfMoonMetrics.PAUSED_ALPHA,
        animationSpec = tween(durationMillis = 400),
        label = "HalfMoonTrackAlpha",
    )
    val playLabel = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HalfMoonMediaButton(
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
                .padding(horizontal = HalfMoonMetrics.TextGap)
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
            label = "HalfMoonTrack",
        ) { (title, artist) ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                HalfMoonMediaText(
                    text = title.uppercase(),
                    brush = brush,
                    fontSize = HalfMoonMetrics.TitleSize,
                    fontWeight = FontWeight.Bold,
                    iterations = 2,
                )

                artist?.let {
                    HalfMoonMediaText(
                        text = artist.uppercase(),
                        brush = brush,
                        fontSize = HalfMoonMetrics.ArtistSize,
                        fontWeight = FontWeight.Normal,
                        iterations = 1,
                        modifier = Modifier
                            .padding(top = HalfMoonMetrics.ArtistGap)
                            .alpha(HalfMoonMetrics.ARTIST_ALPHA),
                    )
                }
            }
        }

        HalfMoonMediaButton(
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
private fun HalfMoonMediaText(
    text: String,
    brush: Brush,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    iterations: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.basicMarquee(iterations = iterations),
        style = LocalTextStyle.current.merge(TextStyle(brush = brush)),
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = HalfMoonThemeSpec.Font,
        letterSpacing = HalfMoonMetrics.TrackSpacing,
        maxLines = 1,
    )
}

@Composable
private fun HalfMoonMediaButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    isEnabled: Boolean,
    color: Color,
    gradient: Brush?,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(HalfMoonMetrics.ButtonSize)
            .then(if (isEnabled) Modifier.onClick(shape = CircleShape, ripple = false, action = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(HalfMoonMetrics.ButtonIconSize)
                .aodTint(gradient.takeIf { isEnabled }),
            tint = if (isEnabled) color else Grey5A,
        )
    }
}

@Preview(widthDp = 393)
@Composable
private fun AodHalfMoonMediaPreview() {
    AodHalfMoonMedia(
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
