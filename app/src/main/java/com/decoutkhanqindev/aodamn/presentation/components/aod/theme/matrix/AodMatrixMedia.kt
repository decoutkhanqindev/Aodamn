package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.matrix

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
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.clipRect
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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix.AodMatrixDotValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix.MatrixGlyphs
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.matrix.MatrixThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import kotlin.math.floor

@Composable
fun AodMatrixMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val scale = MatrixMetrics.scale((maxWidth + MatrixMetrics.SidePadding * 2).value, MatrixMetrics.REFERENCE_SIDE)
        val dot = config[MatrixThemeSpec.DotStyle]
        val brush = config.brushOf(MatrixThemeSpec.MediaColor)
        val artistBrush = config.darkBrushOf(MatrixThemeSpec.MediaColor, MatrixMetrics.ARTIST_DARKEN)
        val locale = LocalConfiguration.current.locales[0]

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AnimatedContent(
                    targetState = media.title to media.artist,
                    modifier = Modifier.weight(1f),
                    transitionSpec = {
                        (slideInVertically(tween(MatrixMetrics.SWAP_MILLIS)) { height -> height } + fadeIn(tween(MatrixMetrics.FADE_MILLIS))) togetherWith
                            fadeOut(tween(MatrixMetrics.FADE_MILLIS))
                    },
                    contentAlignment = Alignment.CenterStart,
                    label = "MatrixMediaTrack",
                ) { (title, artist) ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = title.uppercase(locale),
                            modifier = Modifier.basicMarquee(iterations = 2),
                            style = TextStyle(
                                brush = brush,
                                fontSize = MatrixMetrics.TitleSize * scale,
                                fontFamily = dot.fontFamily,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = MatrixMetrics.TitleSpacing,
                            ),
                            maxLines = 1,
                        )
                        artist?.let { name ->
                            Text(
                                text = name.uppercase(locale),
                                modifier = Modifier
                                    .padding(top = MatrixMetrics.ArtistTop * scale)
                                    .basicMarquee(iterations = 1),
                                style = TextStyle(
                                    brush = artistBrush,
                                    fontSize = MatrixMetrics.ArtistSize * scale,
                                    fontFamily = dot.fontFamily,
                                    fontWeight = FontWeight.Normal,
                                    letterSpacing = MatrixMetrics.ArtistSpacing,
                                ),
                                maxLines = 1,
                            )
                        }
                    }
                }

                MatrixMediaButton(
                    rows = if (media.isPlaying) MatrixGlyphs.Pause else MatrixGlyphs.Play,
                    brush = brush,
                    dot = dot,
                    contentDescription = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play),
                    isEnabled = true,
                    isNext = false,
                    buttonSize = MatrixMetrics.PlayButton * scale,
                    iconSize = MatrixMetrics.PlayIcon * scale,
                    modifier = Modifier.padding(horizontal = MatrixMetrics.ButtonMargin * scale),
                    onClick = { onIntent(AodIntent.PlayPauseMedia) },
                )

                MatrixMediaButton(
                    rows = MatrixGlyphs.Play,
                    brush = brush,
                    dot = dot,
                    contentDescription = stringResource(R.string.aod_media_next),
                    isEnabled = media.canSkipToNext,
                    isNext = true,
                    buttonSize = MatrixMetrics.NextButton * scale,
                    iconSize = MatrixMetrics.NextIcon * scale,
                    onClick = { onIntent(AodIntent.SkipToNextTrack) },
                )
            }

            media.progress?.let { progress ->
                AodMediaProgress(
                    progress = progress,
                    track = { fraction -> MatrixProgressTrack(fraction = fraction, brush = brush, dot = dot, scale = scale) },
                    modifier = Modifier.padding(top = MatrixMetrics.ProgressTop * scale),
                )
            }
        }
    }
}

@Composable
private fun MatrixMediaButton(
    rows: List<String>,
    brush: Brush,
    dot: AodMatrixDotValue,
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
                    val pitch = size.height / MatrixMetrics.MEDIA_GRID_ROWS
                    val widthInCells = if (isNext) MatrixMetrics.NEXT_BAR_COLUMN + 1f else rows.maxOfOrNull { it.length }?.toFloat() ?: 1f
                    val origin = Offset((size.width - widthInCells * pitch) / 2f, 0f)
                    val points = MatrixGlyphs.gridCells(rows).toPoints(origin, pitch).let { playPoints ->
                        if (isNext) {
                            playPoints + List(MatrixMetrics.MEDIA_GRID_ROWS) { row ->
                                Offset(
                                    x = origin.x + (MatrixMetrics.NEXT_BAR_COLUMN + .5f) * pitch,
                                    y = origin.y + (row + .5f) * pitch,
                                )
                            }
                        } else {
                            playPoints
                        }
                    }

                    onDrawBehind {
                        drawMatrixPoints(points = points, pitch = pitch, dot = dot, brush = brush)
                    }
                },
        )
    }
}

@Composable
private fun MatrixProgressTrack(
    fraction: () -> Float,
    brush: Brush,
    dot: AodMatrixDotValue,
    scale: Float,
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(MatrixMetrics.ProgressDot * scale)
            .drawWithCache {
                val pitch = MatrixMetrics.ProgressDotPitch.toPx() * scale
                val strokeWidth = MatrixMetrics.ProgressDot.toPx() * scale
                val count = floor(size.width / pitch).toInt().coerceAtLeast(1)
                val left = (size.width - (count - 1) * pitch) / 2f
                val points = List(count) { index -> Offset(left + index * pitch, size.height / 2f) }
                val isRtl = layoutDirection == LayoutDirection.Rtl

                onDrawBehind {
                    withTransform({ if (isRtl) scale(scaleX = -1f, scaleY = 1f, pivot = center) }) {
                        drawPoints(
                            points = points,
                            pointMode = PointMode.Points,
                            brush = brush,
                            strokeWidth = strokeWidth,
                            cap = dot.cap,
                            alpha = MatrixMetrics.PROGRESS_REST_ALPHA,
                        )
                        val filled = fraction().coerceIn(0f, 1f)
                        val right = left + filled * (count - 1) * pitch + strokeWidth / 2f
                        clipRect(right = right) {
                            drawPoints(
                                points = points,
                                pointMode = PointMode.Points,
                                brush = brush,
                                strokeWidth = strokeWidth,
                                cap = dot.cap,
                            )
                        }
                    }
                }
            },
    )
}
