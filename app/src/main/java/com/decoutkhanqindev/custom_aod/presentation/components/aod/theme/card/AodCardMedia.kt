package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.card

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodMediaButton
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodMediaProgress
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.AodWaveTrack
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.card.CardThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.Black
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey5A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED

// Trình phát của Thẻ theo layout app mẫu: tên bài 17sp / nghệ sĩ 14sp (80%) bên trái, lùi · nút phát tròn đặc 48dp · tới bên phải (lùi / tới
// 80%), thanh tiến trình M3 gợn sóng (nét 6dp, biên độ 3dp, bước sóng 40dp, khe 5dp) lúc đang phát, dừng thì duỗi thẳng. Rộng 313dp như app
// mẫu (màn 393dp trừ hai lề 40dp), cột hẹp hơn (màn ngang) thì hẹp theo.
@Composable
fun AodCardMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val color = config[CardThemeSpec.MediaColor]
    val progress = media.progress
    val wave = animateFloatAsState(
        targetValue = if (media.isPlaying) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "CardProgressWave",
    )

    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .width(313.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.dp, end = 8.dp),
            ) {
                Text(
                    text = media.title,
                    color = color,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = CardThemeSpec.Font,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                media.artist?.let { artist ->
                    Text(
                        text = artist,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .alpha(0.8f),
                        color = color,
                        fontSize = 14.sp,
                        fontFamily = CardThemeSpec.Font,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            AodMediaButton(
                iconRes = R.drawable.ic_skip_back_fill,
                contentDescription = stringResource(R.string.aod_media_previous),
                isEnabled = media.canSkipToPrevious,
                onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
                modifier = Modifier.alpha(0.8f),
                size = 43.dp,
                tint = color,
            )

            CardPlayButton(
                isPlaying = media.isPlaying,
                color = color,
                onClick = { onIntent(AodIntent.PlayPauseMedia) },
                modifier = Modifier.padding(horizontal = 5.dp),
            )

            AodMediaButton(
                iconRes = R.drawable.ic_skip_forward_fill,
                contentDescription = stringResource(R.string.aod_media_next),
                isEnabled = media.canSkipToNext,
                onClick = { onIntent(AodIntent.SkipToNextTrack) },
                modifier = Modifier.alpha(0.8f),
                size = 43.dp,
                tint = color,
            )
        }

        if (progress != null) {
            AodMediaProgress(
                progress = progress,
                track = { fraction ->
                    AodWaveTrack(
                        fraction = fraction,
                        wave = { wave.value },
                        color = color,
                        trackColor = Grey5A,
                        height = 12.dp,
                        strokeWidth = 6.dp,
                        wavelength = 40.dp,
                        gap = 11.dp,
                    )
                },
                modifier = Modifier.padding(top = 9.dp),
            )
        }
    }
}

// Nút phát tròn đặc 48dp màu nhạc, icon 18dp (đen trên màu sáng, trắng trên màu tối).
@Composable
private fun CardPlayButton(
    isPlaying: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .background(color = color, shape = CircleShape)
            .onClick(shape = CircleShape, ripple = false, action = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Crossfade(targetState = isPlaying, label = "CardPlayIcon") { playing ->
            Icon(
                painter = painterResource(if (playing) R.drawable.ic_pause_fill else R.drawable.ic_play_fill),
                contentDescription = stringResource(if (playing) R.string.aod_media_pause else R.string.aod_media_play),
                modifier = Modifier.size(18.dp),
                tint = if (color.luminance() > 0.3f) Black else GreyED,
            )
        }
    }
}

@Preview(widthDp = 393)
@Composable
private fun AodCardMediaPreview() {
    AodCardMedia(
        media = MediaUiModel(
            title = "That Cool Song",
            artist = "It's Artist",
            isPlaying = true,
            canSkipToPrevious = true,
            canSkipToNext = true,
            progress = MediaProgressUiModel(
                durationMillis = 180_000L,
                positionMillis = 126_000L,
                updatedAtMillis = 0L,
                speed = 0f,
            ),
        ),
        config = AodThemeConfig(),
        onIntent = {},
    )
}
