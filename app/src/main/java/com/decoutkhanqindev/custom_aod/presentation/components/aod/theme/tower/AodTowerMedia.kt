package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.tower

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
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodMediaProgress
import com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.AodWaveTrack
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.tower.TowerThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey5A

// Trình phát của Tháp theo layout app mẫu: lùi · tên bài 13sp / nghệ sĩ 11,5sp (80%) giãn chữ 0,38 em ở giữa · tới, mỗi nút ô 40dp cách mép
// 20dp; thanh tiến trình mảnh cách 30dp, lùi vào 30dp hai bên. Không có nút phát: chạm tên bài để phát / dừng như app mẫu, đang dừng thì tên bài
// mờ đi. Rộng bằng màn 393dp, cột hẹp hơn (màn ngang) thì hẹp theo.
@Composable
fun AodTowerMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val color = config[TowerThemeSpec.MediaColor]
    val progress = media.progress
    val textAlpha = animateFloatAsState(
        targetValue = if (media.isPlaying) 1f else 0.6f,
        animationSpec = tween(durationMillis = 400),
        label = "TowerTrackAlpha",
    )
    val playLabel = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play)

    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .width(393.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TowerMediaButton(
                iconRes = R.drawable.ic_skip_back_fill,
                contentDescription = stringResource(R.string.aod_media_previous),
                isEnabled = media.canSkipToPrevious,
                color = color,
                onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
                modifier = Modifier.padding(start = 20.dp, end = 10.dp),
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
                    text = media.title,
                    color = color,
                    fontSize = 13.sp,
                    fontFamily = TowerThemeSpec.Font,
                    letterSpacing = 0.38.em,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                media.artist?.let { artist ->
                    Text(
                        text = artist,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .alpha(0.8f),
                        color = color,
                        fontSize = 11.5.sp,
                        fontFamily = TowerThemeSpec.Font,
                        letterSpacing = 0.38.em,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            TowerMediaButton(
                iconRes = R.drawable.ic_skip_forward_fill,
                contentDescription = stringResource(R.string.aod_media_next),
                isEnabled = media.canSkipToNext,
                color = color,
                onClick = { onIntent(AodIntent.SkipToNextTrack) },
                modifier = Modifier.padding(start = 10.dp, end = 20.dp),
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
                        trackColor = color.copy(alpha = color.alpha * 0.3f),
                        height = 3.dp,
                        strokeWidth = 3.dp,
                        wavelength = 40.dp,
                        gap = 0.dp,
                    )
                },
                modifier = Modifier.padding(start = 30.dp, top = 28.dp, end = 30.dp),
            )
        }
    }
}

// Nút lùi / tới: ô chạm 40dp, icon 14dp như app mẫu (ô 40dp đệm 14dp); không lùi / tới được thì xám.
@Composable
private fun TowerMediaButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    isEnabled: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .then(if (isEnabled) Modifier.onClick(shape = CircleShape, ripple = false, action = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(14.dp),
            tint = if (isEnabled) color else Grey5A,
        )
    }
}

@Preview(widthDp = 393)
@Composable
private fun AodTowerMediaPreview() {
    AodTowerMedia(
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
