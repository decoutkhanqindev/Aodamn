package com.decoutkhanqindev.custom_aod.presentation.components.aod

import android.os.SystemClock
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppSlider
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.AccentGradient
import com.decoutkhanqindev.custom_aod.presentation.theme.FontFamilyPlusJakartaSans
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey3A
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey5A
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey8A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import kotlinx.coroutines.delay

// Chủ đề có trình phát riêng thì rẽ nhánh ở đây (như AodClockFace), còn lại dùng trình phát chung.
@Composable
fun AodMediaControls(
    media: MediaUiModel?,
    theme: AodThemeUiModel,
    onIntent: (AodIntent) -> Unit,
) {
    AnimatedContent(
        targetState = media,
        contentKey = { it != null },
        label = "AodMediaControls",
    ) { target ->
        if (target != null) {
            when (theme.value) {
                AodThemeValue.CLASSIC -> AodClassicMedia(media = target, onIntent = onIntent)
                AodThemeValue.PEBBLE -> AodPebbleMedia(media = target, config = theme.config, onIntent = onIntent)
            }
        }
    }
}

// Phiên nhạc chỉ báo vị trí lúc phát / dừng / tua nên nội suy mỗi giây; vị trí chỉ đọc trong AppSlider (pha vẽ) nên AOD không
// recompose theo tiến trình. track vẽ thanh theo chủ đề.
@Composable
fun AodMediaProgress(
    progress: MediaProgressUiModel,
    track: @Composable (fraction: () -> Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val elapsedMillis = remember(progress) { mutableLongStateOf(SystemClock.elapsedRealtime()) }

    LaunchedEffect(progress) {
        while (progress.isAdvancing) {
            delay(1000)
            elapsedMillis.longValue = SystemClock.elapsedRealtime()
        }
    }

    AppSlider(
        progress = { progress.permilleAt(elapsedMillis.longValue) },
        valueRange = 0..MediaProgressUiModel.PERMILLE,
        step = 1,
        modifier = modifier.fillMaxWidth(),
        isThumbVisible = false,
        track = track,
    )
}

@Composable
fun AodMediaButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(iconRes),
        contentDescription = contentDescription,
        modifier = modifier
            .size(40.dp)
            .then(
                if (isEnabled) {
                    Modifier.onClick(shape = CircleShape, ripple = false, action = onClick)
                } else {
                    Modifier
                },
            )
            .padding(8.dp),
        tint = if (isEnabled) GreyB4 else Grey5A,
    )
}

// Trình phát chung: thẻ viền gradient của app, nút phát trong vòng gradient, thanh tiến trình gradient như thanh trượt của app.
@Composable
private fun AodClassicMedia(
    media: MediaUiModel,
    onIntent: (AodIntent) -> Unit,
) {
    val progress = media.progress

    AodCard(modifier = Modifier.padding(top = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            ) {
                Text(
                    text = media.title,
                    color = GreyB4,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamilyPlusJakartaSans,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                media.artist?.let { artist ->
                    Text(
                        text = artist,
                        color = Grey8A,
                        fontSize = 12.sp,
                        fontFamily = FontFamilyPlusJakartaSans,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            AodMediaButton(
                iconRes = R.drawable.ic_skip_back_light,
                contentDescription = stringResource(R.string.aod_media_previous),
                isEnabled = media.canSkipToPrevious,
                onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
            )

            ClassicPlayButton(
                isPlaying = media.isPlaying,
                onClick = { onIntent(AodIntent.PlayPauseMedia) },
                modifier = Modifier.padding(horizontal = 2.dp),
            )

            AodMediaButton(
                iconRes = R.drawable.ic_skip_forward_light,
                contentDescription = stringResource(R.string.aod_media_next),
                isEnabled = media.canSkipToNext,
                onClick = { onIntent(AodIntent.SkipToNextTrack) },
            )
        }

        if (progress != null) {
            AodMediaProgress(
                progress = progress,
                track = { fraction -> ClassicProgressTrack(fraction = fraction) },
                modifier = Modifier.padding(top = 10.dp),
            )
        }
    }
}

@Composable
private fun ClassicPlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .border(width = 1.5.dp, brush = AccentGradient, shape = CircleShape)
            .onClick(shape = CircleShape, ripple = false, action = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Crossfade(targetState = isPlaying, label = "ClassicPlayIcon") { playing ->
            Icon(
                painter = painterResource(if (playing) R.drawable.ic_pause_light else R.drawable.ic_play_light),
                contentDescription = stringResource(if (playing) R.string.aod_media_pause else R.string.aod_media_play),
                modifier = Modifier.size(20.dp),
                tint = GreyB4,
            )
        }
    }
}

@Composable
private fun ClassicProgressTrack(fraction: () -> Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .drawWithCache {
                val radius = CornerRadius(size.height / 2f)
                val isRtl = layoutDirection == LayoutDirection.Rtl

                onDrawBehind {
                    withTransform({ if (isRtl) scale(scaleX = -1f, scaleY = 1f, pivot = center) }) {
                        drawRoundRect(color = Grey3A, cornerRadius = radius)
                        drawRoundRect(
                            brush = AccentGradient,
                            size = Size(width = size.width * fraction(), height = size.height),
                            cornerRadius = radius,
                        )
                    }
                }
            },
    )
}

@Preview(widthDp = 360)
@Composable
private fun AodMediaControlsPreview() {
    AodMediaControls(
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
        theme = AodThemeUiModel(),
        onIntent = {},
    )
}
