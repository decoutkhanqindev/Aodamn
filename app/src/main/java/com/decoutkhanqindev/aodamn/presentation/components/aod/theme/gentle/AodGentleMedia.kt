package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gentle

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodFlatProgress
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodPlayerButton
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gentle.GentleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodGentleMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val color = config[GentleThemeSpec.MediaColor]
    val brush = config.brushOf(GentleThemeSpec.MediaColor)
    val gradient = config.gradientOf(GentleThemeSpec.MediaColor)?.toBrush()
    val textAlpha by animateFloatAsState(if (media.isPlaying) 1f else GentleMetrics.PAUSED_ALPHA, label = "GentleMediaAlpha")

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedContent(
            targetState = media.title,
            modifier = Modifier.alpha(textAlpha),
            transitionSpec = {
                (slideInVertically(tween(GentleMetrics.TRACK_MILLIS)) { it } + fadeIn(tween(GentleMetrics.TRACK_MILLIS))) togetherWith
                    fadeOut(tween(GentleMetrics.TRACK_MILLIS)) using SizeTransform(clip = false)
            },
            label = "GentleMediaTitle",
        ) { title ->
            GentleTrackText(text = title, brush = brush, fontSize = GentleMetrics.TitleSize, fontWeight = FontWeight.Bold, iterations = 2)
        }

        media.artist?.let { artist ->
            AnimatedContent(
                targetState = artist,
                modifier = Modifier.padding(top = GentleMetrics.ArtistGap).alpha(textAlpha),
                transitionSpec = {
                    (slideInVertically(tween(GentleMetrics.TRACK_MILLIS, delayMillis = GentleMetrics.ARTIST_DELAY_MILLIS)) { it } +
                        fadeIn(tween(GentleMetrics.TRACK_MILLIS, delayMillis = GentleMetrics.ARTIST_DELAY_MILLIS))) togetherWith
                        fadeOut(tween(GentleMetrics.TRACK_MILLIS)) using SizeTransform(clip = false)
                },
                label = "GentleMediaArtist",
            ) { currentArtist ->
                GentleTrackText(
                    text = currentArtist,
                    brush = config.darkBrushOf(GentleThemeSpec.MediaColor, GentleMetrics.TEXT_DARKEN),
                    fontSize = GentleMetrics.ArtistSize,
                    fontWeight = FontWeight.Normal,
                    iterations = 1,
                )
            }
        }

        media.progress?.let { progress ->
            AodFlatProgress(
                progress = progress,
                color = color,
                brush = brush,
                trackHeight = GentleMetrics.TrackHeight,
                trackAlpha = GentleMetrics.TRACK_ALPHA,
                modifier = Modifier.padding(top = GentleMetrics.ProgressGap),
            )
        }

        Row(modifier = Modifier.padding(top = GentleMetrics.ControlsGap), verticalAlignment = Alignment.CenterVertically) {
            AodPlayerButton(
                iconRes = R.drawable.ic_skip_back_fill,
                contentDescription = stringResource(R.string.aod_media_previous),
                isEnabled = media.canSkipToPrevious,
                color = color,
                gradient = gradient,
                size = GentleMetrics.SkipButton,
                iconSize = GentleMetrics.SkipIcon,
                onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
            )
            AodPlayerButton(
                iconRes = if (media.isPlaying) R.drawable.ic_pause_fill else R.drawable.ic_play_fill,
                contentDescription = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play),
                isEnabled = true,
                color = color,
                gradient = gradient,
                size = GentleMetrics.PlayButton,
                iconSize = GentleMetrics.PlayIcon,
                onClick = { onIntent(AodIntent.PlayPauseMedia) },
                modifier = Modifier.padding(horizontal = GentleMetrics.PlaySideGap),
            )
            AodPlayerButton(
                iconRes = R.drawable.ic_skip_forward_fill,
                contentDescription = stringResource(R.string.aod_media_next),
                isEnabled = media.canSkipToNext,
                color = color,
                gradient = gradient,
                size = GentleMetrics.SkipButton,
                iconSize = GentleMetrics.SkipIcon,
                onClick = { onIntent(AodIntent.SkipToNextTrack) },
            )
        }
    }
}

@Composable
private fun GentleTrackText(
    text: String,
    brush: Brush,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    iterations: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(LocalConfiguration.current.locales[0]),
        modifier = modifier.basicMarquee(iterations = iterations),
        style = TextStyle(
            brush = brush,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontFamily = GentleThemeSpec.Font,
            letterSpacing = GentleMetrics.TrackSpacing,
        ),
        maxLines = 1,
    )
}
