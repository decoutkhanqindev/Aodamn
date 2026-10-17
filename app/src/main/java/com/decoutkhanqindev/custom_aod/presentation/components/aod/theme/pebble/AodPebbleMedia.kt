package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.pebble

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodMediaButton
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodMediaProgress
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.AodShapeOutline
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.AodWaveTrack
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.radialShapePath
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.pebble.AodProgressStyleValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.pebble.AodShapeValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.pebble.PebbleThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.Black
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey3A
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey8A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED

// Trình phát của chủ đề Sỏi: nút phát nằm trong khối hình màu nhạc (xoay khi phát / dừng; icon đen trên màu sáng, trắng trên màu tối),
// thanh tiến trình gợn sóng lúc đang phát.
// Vị trí bài nội suy mỗi giây và chỉ đọc trong AppSlider (pha vẽ) nên AOD không recompose theo tiến trình; sóng đứng yên, chỉ biên độ
// đổi lúc phát / dừng để AOD không vẽ lại liên tục.
@Composable
fun AodPebbleMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val mediaColor = config[PebbleThemeSpec.MediaColor]
    val fontFamily = PebbleThemeSpec.Font
    val progressStyle = config[PebbleThemeSpec.ProgressStyle]
    val progress = media.progress

    Column(
        modifier = Modifier
            .padding(top = 24.dp)
            .width(296.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PebbleTrackInfo(
                title = media.title,
                artist = media.artist,
                fontFamily = fontFamily,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            )

            AodMediaButton(
                iconRes = R.drawable.ic_skip_back_fill,
                contentDescription = stringResource(R.string.aod_media_previous),
                isEnabled = media.canSkipToPrevious,
                onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
            )

            PebblePlayButton(
                isPlaying = media.isPlaying,
                shape = config[PebbleThemeSpec.PlayShape],
                color = mediaColor,
                iconColor = if (mediaColor.luminance() > 0.3f) Black else GreyED,
                onClick = { onIntent(AodIntent.PlayPauseMedia) },
                modifier = Modifier.padding(horizontal = 4.dp),
            )

            AodMediaButton(
                iconRes = R.drawable.ic_skip_forward_fill,
                contentDescription = stringResource(R.string.aod_media_next),
                isEnabled = media.canSkipToNext,
                onClick = { onIntent(AodIntent.SkipToNextTrack) },
            )
        }

        AnimatedVisibility(visible = progress != null && progressStyle != AodProgressStyleValue.HIDDEN) {
            if (progress != null) {
                PebbleProgress(
                    progress = progress,
                    isWavy = media.isPlaying && progressStyle == AodProgressStyleValue.WAVE,
                    color = mediaColor,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }
    }
}

@Composable
private fun PebbleTrackInfo(
    title: String,
    artist: String?,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = GreyB4,
            fontSize = 16.sp,
            fontFamily = fontFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        artist?.let {
            Text(
                text = artist,
                color = Grey8A,
                fontSize = 13.sp,
                fontFamily = fontFamily,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun PebblePlayButton(
    isPlaying: Boolean,
    shape: AodShapeValue,
    color: Color,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotation = animateFloatAsState(
        targetValue = if (isPlaying) 90f else 0f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 220f),
        label = "PebblePlayRotation",
    )
    val outline = remember(shape) { AodShapeOutline(shape) }

    Box(
        modifier = modifier
            .size(52.dp)
            .onClick(shape = outline, ripple = false, action = onClick)
            .drawWithCache {
                val path = radialShapePath(radii = shape.radii, size = size)

                onDrawBehind {
                    rotate(degrees = rotation.value) {
                        drawPath(path = path, color = color)
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Crossfade(targetState = isPlaying, label = "PebblePlayIcon") { playing ->
            Icon(
                painter = painterResource(if (playing) R.drawable.ic_pause_fill else R.drawable.ic_play_fill),
                contentDescription = stringResource(if (playing) R.string.aod_media_pause else R.string.aod_media_play),
                modifier = Modifier.size(24.dp),
                tint = iconColor,
            )
        }
    }
}

@Composable
private fun PebbleProgress(
    progress: MediaProgressUiModel,
    isWavy: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val wave = animateFloatAsState(
        targetValue = if (isWavy) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "PebbleProgressWave",
    )

    AodMediaProgress(
        progress = progress,
        track = { fraction ->
            AodWaveTrack(
                fraction = fraction,
                wave = { wave.value },
                color = color,
                trackColor = Grey3A,
                height = 12.dp,
                strokeWidth = 4.dp,
                wavelength = 26.dp,
                gap = 6.dp,
            )
        },
        modifier = modifier,
    )
}

@Preview(widthDp = 360)
@Composable
private fun AodPebbleMediaPreview() {
    AodPebbleMedia(
        media = MediaUiModel(
            title = "Night drive",
            artist = "Lo-fi Radio",
            isPlaying = true,
            canSkipToPrevious = true,
            canSkipToNext = true,
            progress = MediaProgressUiModel(
                durationMillis = 210_000L,
                positionMillis = 130_000L,
                updatedAtMillis = 0L,
                speed = 0f,
            ),
        ),
        config = AodThemeConfig(),
        onIntent = {},
    )
}
