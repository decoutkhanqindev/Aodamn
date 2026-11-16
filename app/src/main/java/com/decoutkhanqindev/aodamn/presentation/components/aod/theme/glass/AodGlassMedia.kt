package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.glass

import android.os.SystemClock
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodMediaProgress
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodPlayerButton
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.glass.GlassThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyInter
import com.decoutkhanqindev.aodamn.presentation.theme.GlassCard
import com.decoutkhanqindev.aodamn.presentation.theme.GlassTrack
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape7dp
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape20dp
import kotlinx.coroutines.delay

@Composable
fun AodGlassMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val scale = GlassMetrics.scale((maxWidth + GlassMetrics.CardMargin * 2).value)
        val color = config[GlassThemeSpec.MediaColor]
        val gradient = config.gradientOf(GlassThemeSpec.MediaColor)?.toBrush()
        val brush = config.brushOf(GlassThemeSpec.MediaColor)
        val progress = media.progress

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = GlassCard, shape = RoundedCornerShape20dp)
                .padding(GlassMetrics.CardPadding * scale),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Crossfade(
                    targetState = media.albumArt,
                    animationSpec = tween(GlassMetrics.ART_FADE_MILLIS),
                    label = "GlassAlbumArt",
                ) { albumArt ->
                    if (albumArt != null) {
                        Image(
                            bitmap = remember(albumArt) { albumArt.asImageBitmap() },
                            contentDescription = null,
                            modifier = Modifier
                                .size(GlassMetrics.AlbumArt * scale)
                                .clip(RoundedCornerShape7dp),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(GlassMetrics.AlbumArt * scale)
                                .background(color = GlassCard, shape = RoundedCornerShape7dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_music_notes_fill),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(GlassMetrics.ButtonIcon * scale)
                                    .alpha(GlassMetrics.ARTIST_ALPHA)
                                    .aodTint(gradient),
                                tint = color,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(GlassMetrics.AlbumGap * scale))

                AnimatedContent(
                    targetState = media.title to media.artist,
                    modifier = Modifier.weight(1f),
                    transitionSpec = {
                        (slideInVertically(tween(GlassMetrics.SWAP_MILLIS)) { height -> height } + fadeIn(tween(GlassMetrics.FADE_MILLIS))) togetherWith
                            fadeOut(tween(GlassMetrics.FADE_MILLIS))
                    },
                    contentAlignment = Alignment.CenterStart,
                    label = "GlassMediaTrack",
                ) { (title, artist) ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = title,
                            modifier = Modifier.basicMarquee(iterations = 2),
                            style = TextStyle(
                                brush = brush,
                                fontSize = GlassMetrics.TitleSize * scale,
                                lineHeight = GlassMetrics.TrackInfoLineHeight * scale,
                                fontFamily = FontFamilyInter,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = GlassMetrics.TitleSpacing,
                            ),
                            maxLines = 1,
                        )
                        artist?.let { name ->
                            Text(
                                text = name,
                                modifier = Modifier
                                    .alpha(GlassMetrics.ARTIST_ALPHA)
                                    .basicMarquee(iterations = 1),
                                style = TextStyle(
                                    brush = brush,
                                    fontSize = GlassMetrics.ArtistSize * scale,
                                    lineHeight = GlassMetrics.TrackInfoLineHeight * scale,
                                    fontFamily = FontFamilyInter,
                                    fontWeight = FontWeight.Normal,
                                ),
                                maxLines = 1,
                            )
                        }
                    }
                }
            }

            if (progress != null) {
                GlassProgress(
                    progress = progress,
                    color = color,
                    gradient = gradient,
                    brush = brush,
                    scale = scale,
                    modifier = Modifier.padding(top = GlassMetrics.ProgressTop * scale),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = GlassMetrics.ButtonsTop * scale),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(1f))
                AodPlayerButton(
                    iconRes = R.drawable.ic_rewind_fill,
                    contentDescription = stringResource(R.string.aod_media_previous),
                    isEnabled = media.canSkipToPrevious,
                    color = color,
                    gradient = gradient,
                    size = GlassMetrics.Button * scale,
                    iconSize = GlassMetrics.ButtonIcon * scale,
                    onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
                )
                Spacer(modifier = Modifier.width(GlassMetrics.ButtonGap * scale))
                AodPlayerButton(
                    iconRes = if (media.isPlaying) R.drawable.ic_pause_fill else R.drawable.ic_play_fill,
                    contentDescription = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play),
                    isEnabled = true,
                    color = color,
                    gradient = gradient,
                    size = GlassMetrics.Button * scale,
                    iconSize = GlassMetrics.ButtonIcon * scale,
                    onClick = { onIntent(AodIntent.PlayPauseMedia) },
                )
                Spacer(modifier = Modifier.width(GlassMetrics.ButtonGap * scale))
                AodPlayerButton(
                    iconRes = R.drawable.ic_fast_forward_fill,
                    contentDescription = stringResource(R.string.aod_media_next),
                    isEnabled = media.canSkipToNext,
                    color = color,
                    gradient = gradient,
                    size = GlassMetrics.Button * scale,
                    iconSize = GlassMetrics.ButtonIcon * scale,
                    onClick = { onIntent(AodIntent.SkipToNextTrack) },
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun GlassProgress(
    progress: MediaProgressUiModel,
    color: Color,
    gradient: Brush?,
    brush: Brush,
    scale: Float,
    modifier: Modifier = Modifier,
) {
    var elapsedRealtime by remember(progress) { mutableLongStateOf(SystemClock.elapsedRealtime()) }

    LaunchedEffect(progress) {
        while (progress.isAdvancing) {
            delay(GlassMetrics.POSITION_TICK_MILLIS)
            elapsedRealtime = SystemClock.elapsedRealtime()
        }
    }

    val positionMillis = progress.positionAt(elapsedRealtime)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GlassTimeLabel(
            displayMillis = positionMillis,
            isRemaining = false,
            brush = brush,
            scale = scale,
            modifier = Modifier.weight(GlassMetrics.ELAPSED_WEIGHT),
        )
        AodMediaProgress(
            progress = progress,
            track = { fraction -> GlassProgressTrack(fraction = fraction, color = color, brush = gradient, scale = scale) },
            modifier = Modifier.weight(GlassMetrics.BAR_WEIGHT),
        )
        GlassTimeLabel(
            displayMillis = (progress.durationMillis - positionMillis).coerceAtLeast(0L),
            isRemaining = true,
            brush = brush,
            scale = scale,
            modifier = Modifier.weight(GlassMetrics.REMAINING_WEIGHT),
        )
    }
}

@Composable
private fun GlassTimeLabel(
    displayMillis: Long,
    isRemaining: Boolean,
    brush: Brush,
    scale: Float,
    modifier: Modifier = Modifier,
) {
    val totalSeconds = displayMillis / GlassMetrics.POSITION_TICK_MILLIS
    val text = stringResource(
        if (isRemaining) R.string.aod_glass_remaining else R.string.aod_glass_elapsed,
        totalSeconds / 60,
        totalSeconds % 60,
    )

    Text(
        text = text,
        modifier = modifier.alpha(GlassMetrics.TIME_ALPHA),
        style = TextStyle(
            brush = brush,
            fontSize = GlassMetrics.TimeSize * scale,
            fontFamily = FontFamilyInter,
            fontWeight = FontWeight.Normal,
            letterSpacing = GlassMetrics.TimeSpacing,
            textAlign = TextAlign.Center,
        ),
        maxLines = 1,
    )
}

private fun MediaProgressUiModel.positionAt(elapsedRealtime: Long): Long =
    (positionMillis + ((elapsedRealtime - updatedAtMillis) * speed).toLong()).coerceIn(0L, durationMillis)

@Composable
private fun GlassProgressTrack(
    fraction: () -> Float,
    color: Color,
    brush: Brush?,
    scale: Float,
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(GlassMetrics.TrackHeight * scale)
            .drawWithCache {
                val radius = CornerRadius(size.height / 2f)
                val isRtl = layoutDirection == LayoutDirection.Rtl

                onDrawBehind {
                    withTransform({ if (isRtl) scale(scaleX = -1f, scaleY = 1f, pivot = center) }) {
                        drawRoundRect(color = GlassTrack, cornerRadius = radius)
                        val filled = Size(width = size.width * fraction().coerceIn(0f, 1f), height = size.height)
                        if (brush != null) {
                            drawRoundRect(brush = brush, size = filled, cornerRadius = radius)
                        } else {
                            drawRoundRect(color = color, size = filled, cornerRadius = radius)
                        }
                    }
                }
            },
    )
}

@Preview(widthDp = 393)
@Composable
private fun AodGlassMediaPreview() {
    Box(modifier = Modifier.padding(horizontal = GlassMetrics.CardMargin)) {
        AodGlassMedia(
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
}
