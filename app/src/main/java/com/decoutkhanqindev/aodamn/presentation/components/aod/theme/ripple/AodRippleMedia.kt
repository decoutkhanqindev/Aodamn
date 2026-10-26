package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.ripple.RippleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.Grey3A
import kotlin.math.abs

@Composable
fun AodRippleMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val isOutline = config[RippleThemeSpec.Style] == AodTileStyleValue.OUTLINE
    val background = config.brushOf(RippleThemeSpec.MediaBackgroundColor)
    val textBrush = config.brushOf(RippleThemeSpec.MediaColor)
    val artistBrush = config.darkerBrushOf(RippleThemeSpec.MediaColor)
    val swipeThreshold = with(LocalDensity.current) { RippleMetrics.SwipeThreshold.toPx() }
    val currentMedia by rememberUpdatedState(media)
    var isPreviousSwipe by remember { mutableStateOf(false) }
    val previousLabel = stringResource(R.string.aod_media_previous)
    val nextLabel = stringResource(R.string.aod_media_next)
    val skipToPrevious = {
        if (currentMedia.canSkipToPrevious) {
            isPreviousSwipe = true
            onIntent(AodIntent.SkipToPreviousTrack)
        }
    }
    val skipToNext = {
        if (currentMedia.canSkipToNext) {
            isPreviousSwipe = false
            onIntent(AodIntent.SkipToNextTrack)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(RippleMetrics.PlayerHeight)
                .rippleSurface(background = background, isOutline = isOutline)
                .padding(RippleMetrics.CardPadding)
                .pointerInput(swipeThreshold) {
                    var drag = 0f
                    detectHorizontalDragGestures(
                        onDragStart = { drag = 0f },
                        onDragEnd = {
                            when {
                                drag > swipeThreshold -> skipToPrevious()
                                drag < -swipeThreshold -> skipToNext()
                            }
                        },
                        onHorizontalDrag = { change, amount ->
                            if (abs(amount) > 0f) change.consume()
                            drag += amount
                        },
                    )
                }
                .semantics {
                    customActions = listOf(
                        CustomAccessibilityAction(label = previousLabel) {
                            skipToPrevious()
                            true
                        },
                        CustomAccessibilityAction(label = nextLabel) {
                            skipToNext()
                            true
                        },
                    )
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RippleAlbumArt(media = media, iconColor = config[RippleThemeSpec.MediaColor])

            AnimatedContent(
                targetState = media.title to media.artist,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = RippleMetrics.TextGap),
                transitionSpec = {
                    val direction = if (isPreviousSwipe) -1 else 1
                    (slideInHorizontally { width -> direction * width } + fadeIn()) togetherWith fadeOut()
                },
                contentAlignment = Alignment.Center,
                label = "RippleTrack",
            ) { (title, artist) ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    RippleMediaText(text = title, brush = textBrush, fontSize = RippleMetrics.TitleSize, fontWeight = FontWeight.Medium)

                    artist?.let {
                        RippleMediaText(
                            text = artist,
                            brush = artistBrush,
                            fontSize = RippleMetrics.ArtistSize,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(top = RippleMetrics.ArtistGap),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(RippleMetrics.PlayButtonGap))

        Box(
            modifier = Modifier
                .size(RippleMetrics.PlayButton)
                .rippleSurface(background = background, isOutline = isOutline)
                .onClick(shape = CircleShape, ripple = false) { onIntent(AodIntent.PlayPauseMedia) },
            contentAlignment = Alignment.Center,
        ) {
            Crossfade(targetState = media.isPlaying, label = "RipplePlayPause") { isPlaying ->
                Icon(
                    painter = painterResource(if (isPlaying) R.drawable.ic_pause_fill else R.drawable.ic_play_fill),
                    contentDescription = stringResource(if (isPlaying) R.string.aod_media_pause else R.string.aod_media_play),
                    modifier = Modifier
                        .size(RippleMetrics.PlayIcon)
                        .aodTint(textBrush.takeIf { it !is SolidColor }),
                    tint = config[RippleThemeSpec.MediaColor],
                )
            }
        }
    }
}

@Composable
private fun RippleAlbumArt(
    media: MediaUiModel,
    iconColor: Color,
) {
    val art = media.albumArt

    Crossfade(targetState = art, label = "RippleAlbumArt") { bitmap ->
        if (bitmap != null) {
            Image(
                bitmap = remember(bitmap) { bitmap.asImageBitmap() },
                contentDescription = null,
                modifier = Modifier
                    .size(RippleMetrics.AlbumArt)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(
                modifier = Modifier
                    .size(RippleMetrics.AlbumArt)
                    .background(color = Grey3A, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_music_notes_light),
                    contentDescription = null,
                    modifier = Modifier.size(RippleMetrics.FallbackIcon),
                    tint = iconColor,
                )
            }
        }
    }
}

@Composable
private fun RippleMediaText(
    text: String,
    brush: Brush,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.basicMarquee(iterations = 1),
        style = TextStyle(brush = brush, fontSize = fontSize, fontWeight = fontWeight, fontFamily = RippleThemeSpec.Font),
        maxLines = 1,
    )
}

private fun Modifier.rippleSurface(background: Brush, isOutline: Boolean): Modifier =
    if (isOutline) border(width = RippleMetrics.OutlineWidth, brush = background, shape = CircleShape) else background(brush = background, shape = CircleShape)

@Preview(widthDp = 313)
@Composable
private fun AodRippleMediaPreview() {
    AodRippleMedia(
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
