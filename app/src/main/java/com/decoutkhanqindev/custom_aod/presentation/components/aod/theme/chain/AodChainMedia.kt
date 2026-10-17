package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.chain

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodMediaProgress
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.AodWaveTrack
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.chain.ChainThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey5A

// Trình phát của Chuỗi theo layout app mẫu: lùi 35dp · tên bài in hoa đậm / nghệ sĩ mờ ở giữa · tới 35dp, thanh tiến trình 1,5dp bên dưới. Không có
// nút phát: chạm tên bài để phát / dừng như app mẫu, đang dừng thì chữ mờ đi. Rộng 313dp (màn 393dp trừ đệm 40dp hai bên), cột hẹp hơn thì hẹp theo.
@Composable
fun AodChainMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val color = config[ChainThemeSpec.MediaColor]
    val progress = media.progress
    val textAlpha = animateFloatAsState(
        targetValue = if (media.isPlaying) 1f else ChainMetrics.PAUSED_ALPHA,
        animationSpec = tween(durationMillis = 400),
        label = "ChainTrackAlpha",
    )
    val playLabel = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play)

    Column(modifier = Modifier.width(ChainMetrics.PlayerWidth)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ChainMediaButton(
                iconRes = R.drawable.ic_skip_back_fill,
                contentDescription = stringResource(R.string.aod_media_previous),
                isEnabled = media.canSkipToPrevious,
                color = color,
                onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
            )

            Column(
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
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = media.title.uppercase(),
                    color = color,
                    fontSize = ChainMetrics.TitleSize,
                    fontFamily = ChainThemeSpec.TitleFont,
                    letterSpacing = ChainMetrics.MEDIA_TITLE_TRACKING.em,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                media.artist?.let { artist ->
                    Text(
                        text = artist.uppercase(),
                        modifier = Modifier
                            .padding(top = ChainMetrics.ArtistGap)
                            .alpha(ChainMetrics.SUBTITLE_ALPHA),
                        color = color,
                        fontSize = ChainMetrics.SubtitleSize,
                        fontFamily = ChainThemeSpec.Font,
                        letterSpacing = ChainMetrics.MEDIA_ARTIST_TRACKING.em,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            ChainMediaButton(
                iconRes = R.drawable.ic_skip_forward_fill,
                contentDescription = stringResource(R.string.aod_media_next),
                isEnabled = media.canSkipToNext,
                color = color,
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
                        trackColor = color.copy(alpha = color.alpha * ChainMetrics.TRACK_ALPHA),
                        height = ChainMetrics.TrackHeight,
                        strokeWidth = ChainMetrics.TrackHeight,
                        wavelength = 40.dp,
                        gap = 0.dp,
                    )
                },
                modifier = Modifier.padding(top = ChainMetrics.ProgressGap),
            )
        }
    }
}

// Nút lùi / tới: ô 35dp, icon 15dp như app mẫu (ô 35dp đệm 10dp); không lùi / tới được thì xám.
@Composable
private fun ChainMediaButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    isEnabled: Boolean,
    color: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(ChainMetrics.ButtonSize)
            .then(if (isEnabled) Modifier.onClick(shape = CircleShape, ripple = false, action = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(ChainMetrics.ButtonIconSize),
            tint = if (isEnabled) color else Grey5A,
        )
    }
}

@Preview(widthDp = 393)
@Composable
private fun AodChainMediaPreview() {
    AodChainMedia(
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
