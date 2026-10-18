package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.capsule

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodMediaButton
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodMediaProgress
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.capsule.CapsuleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.Grey3A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyB4

// Trình phát của Viên nang như ảnh mẫu: tên bài (màu nhạc) / nghệ sĩ bên trái, lùi · nút phát tròn đặc màu nhạc (icon đen) · tới bên phải, thanh
// tiến trình dày tách đôi (phần đã nghe màu nhạc, một khe nhỏ, phần còn lại xám). Rộng 315dp như ảnh mẫu, cột hẹp hơn (màn ngang) thì hẹp theo.
@Composable
fun AodCapsuleMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val color = config[CapsuleThemeSpec.MediaColor]
    val progress = media.progress

    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .width(315.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            ) {
                Text(
                    text = media.title,
                    color = color,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = CapsuleThemeSpec.Font,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                media.artist?.let { artist ->
                    Text(
                        text = artist,
                        color = GreyB4,
                        fontSize = 13.5.sp,
                        fontFamily = CapsuleThemeSpec.Font,
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
                size = 34.dp,
            )

            CapsulePlayButton(
                isPlaying = media.isPlaying,
                color = color,
                onClick = { onIntent(AodIntent.PlayPauseMedia) },
                modifier = Modifier.padding(horizontal = 10.dp),
            )

            AodMediaButton(
                iconRes = R.drawable.ic_skip_forward_fill,
                contentDescription = stringResource(R.string.aod_media_next),
                isEnabled = media.canSkipToNext,
                onClick = { onIntent(AodIntent.SkipToNextTrack) },
                size = 34.dp,
            )
        }

        if (progress != null) {
            AodMediaProgress(
                progress = progress,
                track = { fraction -> CapsuleProgressTrack(fraction = fraction, color = color) },
                modifier = Modifier.padding(top = 18.dp),
            )
        }
    }
}

// Nút phát tròn đặc 35dp trong vùng chạm 44dp.
@Composable
private fun CapsulePlayButton(
    isPlaying: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .onClick(shape = CircleShape, ripple = false, action = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(35.dp)
                .background(color = color, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Crossfade(targetState = isPlaying, label = "CapsulePlayIcon") { playing ->
                Icon(
                    painter = painterResource(if (playing) R.drawable.ic_pause_fill else R.drawable.ic_play_fill),
                    contentDescription = stringResource(if (playing) R.string.aod_media_pause else R.string.aod_media_play),
                    modifier = Modifier.size(16.dp),
                    tint = Black,
                )
            }
        }
    }
}

// Thanh dày 6dp: phần đã nghe và phần còn lại là hai đoạn bo tròn cách nhau một khe 5dp.
@Composable
private fun CapsuleProgressTrack(
    fraction: () -> Float,
    color: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .drawWithCache {
                val strokeWidth = size.height
                val gap = 5.dp.toPx()
                val isRtl = layoutDirection == LayoutDirection.Rtl

                onDrawBehind {
                    val centerY = size.height / 2f
                    val start = strokeWidth / 2f
                    val end = size.width - strokeWidth / 2f
                    val playedEnd = start + (end - start) * fraction()
                    val restStart = if (fraction() > 0f) playedEnd + strokeWidth + gap else start

                    withTransform({ if (isRtl) scale(scaleX = -1f, scaleY = 1f, pivot = center) }) {
                        if (restStart < end) {
                            drawLine(
                                color = Grey3A,
                                start = Offset(restStart, centerY),
                                end = Offset(end, centerY),
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round,
                            )
                        }
                        if (fraction() > 0f) {
                            drawLine(
                                color = color,
                                start = Offset(start, centerY),
                                end = Offset(playedEnd, centerY),
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round,
                            )
                        }
                    }
                }
            },
    )
}

@Preview(widthDp = 393)
@Composable
private fun AodCapsuleMediaPreview() {
    AodCapsuleMedia(
        media = MediaUiModel(
            title = "That Cool Song",
            artist = "It's Artist",
            isPlaying = false,
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
