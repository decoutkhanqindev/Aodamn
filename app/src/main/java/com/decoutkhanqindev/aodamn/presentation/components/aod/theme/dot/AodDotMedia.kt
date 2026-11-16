package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.dot

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodMediaProgress
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.dot.DotThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.FontFamilyTiny5
import kotlin.math.floor

@Composable
fun AodDotMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val scale = DotMetrics.scale((maxWidth + DotMetrics.SidePadding * 2).value, DotMetrics.REFERENCE_SIDE)
        val brush = config.brushOf(DotThemeSpec.MediaColor)
        val artistBrush = config.darkBrushOf(DotThemeSpec.MediaColor, DotMetrics.ARTIST_DARKEN)
        val locale = LocalConfiguration.current.locales[0]

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedContent(
                    targetState = media.title to media.artist,
                    modifier = Modifier.weight(1f),
                    transitionSpec = {
                        (slideInVertically(tween(DotMetrics.SWAP_MILLIS)) { height -> height } + fadeIn(tween(DotMetrics.FADE_MILLIS))) togetherWith
                            fadeOut(tween(DotMetrics.FADE_MILLIS))
                    },
                    contentAlignment = Alignment.CenterStart,
                    label = "DotMediaTrack",
                ) { (title, artist) ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = title.uppercase(locale),
                            modifier = Modifier.basicMarquee(iterations = 2),
                            style = TextStyle(
                                brush = brush,
                                fontSize = DotMetrics.TitleSize * scale,
                                fontFamily = FontFamilyTiny5,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = DotMetrics.TitleSpacing,
                            ),
                            maxLines = 1,
                        )
                        artist?.let { name ->
                            Text(
                                text = name.uppercase(locale),
                                modifier = Modifier
                                    .padding(top = DotMetrics.ArtistTop * scale)
                                    .basicMarquee(iterations = 1),
                                style = TextStyle(
                                    brush = artistBrush,
                                    fontSize = DotMetrics.ArtistSize * scale,
                                    fontFamily = FontFamilyTiny5,
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = DotMetrics.ArtistSpacing,
                                ),
                                maxLines = 1,
                            )
                        }
                    }
                }

                DotMediaButton(
                    rows = if (media.isPlaying) DotGrids.Pause else DotGrids.Play,
                    brush = brush,
                    contentDescription = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play),
                    isEnabled = true,
                    isNext = false,
                    buttonSize = DotMetrics.PlayButton * scale,
                    iconSize = DotMetrics.PlayIcon * scale,
                    modifier = Modifier.padding(horizontal = DotMetrics.ButtonMargin * scale),
                    onClick = { onIntent(AodIntent.PlayPauseMedia) },
                )

                DotMediaButton(
                    rows = DotGrids.Play,
                    brush = brush,
                    contentDescription = stringResource(R.string.aod_media_next),
                    isEnabled = media.canSkipToNext,
                    isNext = true,
                    buttonSize = DotMetrics.NextButton * scale,
                    iconSize = DotMetrics.NextIcon * scale,
                    onClick = { onIntent(AodIntent.SkipToNextTrack) },
                )
            }

            media.progress?.let { progress ->
                AodMediaProgress(
                    progress = progress,
                    track = { fraction -> DotProgressTrack(fraction = fraction, brush = brush, scale = scale) },
                    modifier = Modifier.padding(top = DotMetrics.ProgressTop * scale),
                )
            }
        }
    }
}

@Composable
private fun DotMediaButton(
    rows: List<String>,
    brush: Brush,
    contentDescription: String,
    isEnabled: Boolean,
    isNext: Boolean,
    buttonSize: Dp,
    iconSize: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(buttonSize)
            .alpha(if (isEnabled) 1f else .4f)
            .semantics {
                this.contentDescription = contentDescription
                role = Role.Button
                if (!isEnabled) disabled()
            }
            .then(if (isEnabled) Modifier.onClick(ripple = false, action = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Spacer(
            modifier = Modifier
                .size(iconSize)
                .drawWithCache {
                    val pitch = size.height / DotMetrics.MEDIA_GRID_ROWS
                    val patternWidth = if (isNext) 6.3f * pitch else (rows.maxOfOrNull { it.length } ?: 1) * pitch - pitch
                    val left = (size.width - patternWidth) / 2f
                    val top = (size.height - (DotMetrics.MEDIA_GRID_ROWS - 1) * pitch) / 2f

                    onDrawBehind {
                        drawDotGrid(
                            rows = rows,
                            topLeft = Offset(left, top),
                            pitch = pitch,
                            brush = brush,
                            dotRatio = DotMetrics.MEDIA_DOT,
                        )
                        if (isNext) {
                            repeat(DotMetrics.MEDIA_GRID_ROWS) { row ->
                                drawCircle(
                                    brush = brush,
                                    radius = pitch * DotMetrics.MEDIA_DOT / 2f,
                                    center = Offset(left + 6.3f * pitch, top + row * pitch),
                                )
                            }
                        }
                    }
                },
        )
    }
}

@Composable
private fun DotProgressTrack(
    fraction: () -> Float,
    brush: Brush,
    scale: Float,
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(DotMetrics.ProgressDot * scale)
            .drawWithCache {
                val pitch = DotMetrics.ProgressDotPitch.toPx() * scale
                val radius = DotMetrics.ProgressDot.toPx() * scale / 2f
                val count = floor(size.width / pitch).toInt().coerceAtLeast(1)
                val left = (size.width - (count - 1) * pitch) / 2f
                val isRtl = layoutDirection == LayoutDirection.Rtl

                onDrawBehind {
                    withTransform({ if (isRtl) scale(scaleX = -1f, scaleY = 1f, pivot = center) }) {
                        val filled = fraction().coerceIn(0f, 1f)
                        repeat(count) { index ->
                            val dotFraction = if (count == 1) 0f else index.toFloat() / (count - 1)
                            drawCircle(
                                brush = brush,
                                radius = radius,
                                center = Offset(left + index * pitch, center.y),
                                alpha = if (dotFraction <= filled) 1f else DotMetrics.PROGRESS_REST_ALPHA,
                            )
                        }
                    }
                }
            },
    )
}
