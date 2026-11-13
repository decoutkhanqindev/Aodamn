package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.meridian

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodMediaProgress
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodPlayerButton
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.meridian.MeridianThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyGoogleSans

@Composable
fun AodMeridianMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedContent(
                targetState = media.title to media.artist,
                modifier = Modifier.weight(1f),
                transitionSpec = { (slideInVertically { height -> height } + fadeIn()) togetherWith fadeOut() },
                contentAlignment = Alignment.CenterStart,
                label = "AodMeridianMediaTrack",
            ) { (title, artist) ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = title,
                        modifier = Modifier.basicMarquee(iterations = 2),
                        style = TextStyle(
                            brush = config.brushOf(MeridianThemeSpec.MediaColor),
                            fontSize = MeridianMetrics.TitleSize,
                            fontFamily = FontFamilyGoogleSans,
                            letterSpacing = MeridianMetrics.TitleSpacing,
                        ),
                        maxLines = 1,
                    )
                    artist?.let {
                        Text(
                            text = artist,
                            modifier = Modifier
                                .padding(top = MeridianMetrics.ArtistGap)
                                .basicMarquee(iterations = 1),
                            style = TextStyle(
                                brush = config.darkBrushOf(MeridianThemeSpec.MediaColor, MeridianMetrics.ARTIST_DARKEN),
                                alpha = MeridianMetrics.ARTIST_ALPHA,
                                fontSize = MeridianMetrics.ArtistSize,
                                fontFamily = FontFamilyGoogleSans,
                            ),
                            maxLines = 1,
                        )
                    }
                }
            }
            AodPlayerButton(
                iconRes = R.drawable.ic_skip_back_fill,
                contentDescription = stringResource(R.string.aod_media_previous),
                isEnabled = media.canSkipToPrevious,
                color = lerp(config[MeridianThemeSpec.MediaColor], Black, MeridianMetrics.ARTIST_DARKEN),
                gradient = config.gradientOf(MeridianThemeSpec.MediaColor)?.let {
                    config.darkBrushOf(MeridianThemeSpec.MediaColor, MeridianMetrics.ARTIST_DARKEN)
                },
                size = MeridianMetrics.SkipButton,
                iconSize = MeridianMetrics.SkipIcon,
                onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
                modifier = Modifier.alpha(MeridianMetrics.SKIP_ALPHA),
            )
            MeridianPlayButton(
                isPlaying = media.isPlaying,
                brush = config.brushOf(MeridianThemeSpec.MediaColor),
                onClick = { onIntent(AodIntent.PlayPauseMedia) },
                modifier = Modifier.padding(horizontal = MeridianMetrics.PlayGap),
            )
            AodPlayerButton(
                iconRes = R.drawable.ic_skip_forward_fill,
                contentDescription = stringResource(R.string.aod_media_next),
                isEnabled = media.canSkipToNext,
                color = lerp(config[MeridianThemeSpec.MediaColor], Black, MeridianMetrics.ARTIST_DARKEN),
                gradient = config.gradientOf(MeridianThemeSpec.MediaColor)?.let {
                    config.darkBrushOf(MeridianThemeSpec.MediaColor, MeridianMetrics.ARTIST_DARKEN)
                },
                size = MeridianMetrics.SkipButton,
                iconSize = MeridianMetrics.SkipIcon,
                onClick = { onIntent(AodIntent.SkipToNextTrack) },
                modifier = Modifier.alpha(MeridianMetrics.SKIP_ALPHA),
            )
        }
        media.progress?.let { progress ->
            AodMediaProgress(
                progress = progress,
                track = { fraction ->
                    MeridianTrack(
                        fraction = fraction,
                        brush = config.brushOf(MeridianThemeSpec.MediaColor),
                        trackBrush = config.darkBrushOf(MeridianThemeSpec.MediaColor, MeridianMetrics.TRACK_DARKEN),
                    )
                },
                modifier = Modifier.padding(top = MeridianMetrics.ProgressTop),
            )
        }
    }
}

@Composable
private fun MeridianPlayButton(
    isPlaying: Boolean,
    brush: Brush,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(MeridianMetrics.PlayButton)
            .onClick(shape = CircleShape, ripple = false, action = onClick)
            .drawBehind { drawCircle(brush = brush, radius = (MeridianMetrics.PlayDisc / 2).toPx()) },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(if (isPlaying) R.drawable.ic_pause_fill else R.drawable.ic_play_fill),
            contentDescription = stringResource(if (isPlaying) R.string.aod_media_pause else R.string.aod_media_play),
            modifier = Modifier.size(MeridianMetrics.PlayIcon),
            tint = Black,
        )
    }
}

@Composable
private fun MeridianTrack(
    fraction: () -> Float,
    brush: Brush,
    trackBrush: Brush,
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(MeridianMetrics.TrackHeight)
            .drawBehind {
                val value = fraction().coerceIn(0f, 1f)
                val radius = size.height / 2f
                val gap = MeridianMetrics.TrackGap.toPx()
                val end = size.width * value
                if (end > 0f) {
                    drawRoundRect(
                        brush = brush,
                        size = Size(end, size.height),
                        cornerRadius = CornerRadius(radius),
                    )
                }
                val start = if (end > 0f) end + gap else 0f
                if (start < size.width) {
                    drawRoundRect(
                        brush = trackBrush,
                        topLeft = Offset(start, 0f),
                        size = Size(size.width - start, size.height),
                        cornerRadius = CornerRadius(radius),
                    )
                }
            },
    )
}

@Preview(widthDp = 393)
@Composable
private fun AodMeridianMediaPreview() {
    AodMeridianMedia(
        media = MediaUiModel(
            title = "That Cool Song",
            artist = "It's Artist",
            isPlaying = true,
            canSkipToPrevious = true,
            canSkipToNext = true,
            progress = MediaProgressUiModel(
                durationMillis = 180_000L,
                positionMillis = 75_000L,
                updatedAtMillis = 0L,
                speed = 0f,
            ),
        ),
        config = AodThemeConfig(),
        onIntent = {},
    )
}
