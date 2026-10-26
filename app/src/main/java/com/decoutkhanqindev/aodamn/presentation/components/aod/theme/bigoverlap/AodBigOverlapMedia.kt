package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bigoverlap

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodMediaProgress
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.AodWaveTrack
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap.BigOverlapThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A

// Trình phát của Chồng số như media_widget_lt của app mẫu: tên bài / nghệ sĩ tối hơn bên trái, nút phát / dừng rồi nút tới bên phải (không có nút
// lùi), thanh tiến trình mảnh bên dưới. Đổi bài thì tên trượt lên từ dưới như app mẫu; tên dài thì chạy chữ. Rộng theo khung của bố cục.
@Composable
fun AodBigOverlapMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val color = config[BigOverlapThemeSpec.MediaColor]
    val brush = config.brushOf(BigOverlapThemeSpec.MediaColor)
    val gradientBrush = config.gradientOf(BigOverlapThemeSpec.MediaColor)?.toBrush()
    val progress = media.progress

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedContent(
                targetState = media.title to media.artist,
                modifier = Modifier.weight(1f),
                transitionSpec = { (slideInVertically { height -> height } + fadeIn()) togetherWith fadeOut() },
                contentAlignment = Alignment.CenterStart,
                label = "BigOverlapTrack",
            ) { (title, artist) ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    BigOverlapMediaText(text = title, brush = brush, fontSize = BigOverlapMetrics.TitleSize, iterations = 2)

                    artist?.let {
                        BigOverlapMediaText(
                            text = artist,
                            brush = config.dimBrushOf(BigOverlapThemeSpec.MediaColor),
                            fontSize = BigOverlapMetrics.ArtistSize,
                            iterations = 1,
                            modifier = Modifier.padding(top = BigOverlapMetrics.ArtistGap),
                        )
                    }
                }
            }

            BigOverlapMediaButton(
                iconRes = if (media.isPlaying) R.drawable.ic_pause_fill else R.drawable.ic_play_fill,
                contentDescription = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play),
                isEnabled = true,
                color = color,
                gradient = gradientBrush,
                size = BigOverlapMetrics.PlayButton,
                iconSize = BigOverlapMetrics.PlayIcon,
                onClick = { onIntent(AodIntent.PlayPauseMedia) },
                modifier = Modifier.padding(horizontal = BigOverlapMetrics.PlayGap),
            )

            BigOverlapMediaButton(
                iconRes = R.drawable.ic_skip_forward_fill,
                contentDescription = stringResource(R.string.aod_media_next),
                isEnabled = media.canSkipToNext,
                color = color,
                gradient = gradientBrush,
                size = BigOverlapMetrics.NextButton,
                iconSize = BigOverlapMetrics.NextIcon,
                onClick = { onIntent(AodIntent.SkipToNextTrack) },
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
                        trackColor = color.copy(alpha = color.alpha * BigOverlapMetrics.TRACK_ALPHA),
                        brush = brush,
                        height = BigOverlapMetrics.TrackHeight,
                        strokeWidth = BigOverlapMetrics.TrackHeight,
                        wavelength = 40.dp,
                        gap = 0.dp,
                    )
                },
                modifier = Modifier.padding(top = BigOverlapMetrics.ProgressGap),
            )
        }
    }
}

// Tên bài / nghệ sĩ một dòng giãn 0,02em, dài thì chạy chữ (2 / 1 lần như marqueeRepeatLimit của app mẫu).
@Composable
private fun BigOverlapMediaText(
    text: String,
    brush: Brush,
    fontSize: TextUnit,
    iterations: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.basicMarquee(iterations = iterations),
        style = TextStyle(
            brush = brush,
            fontSize = fontSize,
            fontFamily = BigOverlapThemeSpec.Font,
            letterSpacing = BigOverlapMetrics.TrackSpacing,
        ),
        maxLines = 1,
    )
}

// Nút phát / tới: icon đặc giữa ô bấm; không tới được thì xám.
@Composable
private fun BigOverlapMediaButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    isEnabled: Boolean,
    color: Color,
    gradient: Brush?,
    size: Dp,
    iconSize: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .then(if (isEnabled) Modifier.onClick(shape = CircleShape, ripple = false, action = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(iconSize)
                .aodTint(gradient.takeIf { isEnabled }),
            tint = if (isEnabled) color else Grey5A,
        )
    }
}

@Preview(widthDp = 313)
@Composable
private fun AodBigOverlapMediaPreview() {
    AodBigOverlapMedia(
        media = MediaUiModel(
            title = "That Cool Song",
            artist = "It's Artist",
            isPlaying = true,
            canSkipToPrevious = true,
            canSkipToNext = true,
            progress = MediaProgressUiModel(
                durationMillis = 180_000L,
                positionMillis = 36_000L,
                updatedAtMillis = 0L,
                speed = 0f,
            ),
        ),
        config = AodThemeConfig(),
        onIntent = {},
    )
}
