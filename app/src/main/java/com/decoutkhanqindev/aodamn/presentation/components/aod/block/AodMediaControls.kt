package com.decoutkhanqindev.aodamn.presentation.components.aod.block

import android.os.SystemClock
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppSlider
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bezel.AodBezelMedia
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bigoverlap.AodBigOverlapMedia
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.capsule.AodCapsuleMedia
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.card.AodCardMedia
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.chain.AodChainMedia
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass.AodCompassMedia
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline.AodGradientLineMedia
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.halfmoon.AodHalfMoonMedia
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pebble.AodPebbleMedia
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.quarters.AodQuartersMedia
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple.AodRippleMedia
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tick.AodTickMedia
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tower.AodTowerMedia
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaProgressUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodMediaLayoutValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.Grey3A
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyB4
import kotlinx.coroutines.delay

@Composable
fun AodMediaControls(
    media: MediaUiModel?,
    theme: AodThemeUiModel,
    style: AodComponentStyle,
    layout: AodMediaLayoutValue,
    onIntent: (AodIntent) -> Unit,
) {
    AnimatedContent(
        targetState = media,
        contentKey = { it != null },
        label = "AodMediaControls",
    ) { target ->
        if (target != null) {
            when (theme.value) {
                AodThemeValue.CLASSIC, AodThemeValue.JERSEY ->
                    AodClassicMedia(media = target, style = style, layout = layout, onIntent = onIntent)

                AodThemeValue.PEBBLE -> AodPebbleMedia(media = target, config = theme.config, onIntent = onIntent)
                AodThemeValue.CAPSULE -> AodCapsuleMedia(media = target, config = theme.config, onIntent = onIntent)
                AodThemeValue.CARD -> AodCardMedia(media = target, config = theme.config, onIntent = onIntent)
                AodThemeValue.TOWER -> AodTowerMedia(media = target, config = theme.config, onIntent = onIntent)
                AodThemeValue.CHAIN -> AodChainMedia(media = target, config = theme.config, onIntent = onIntent)
                AodThemeValue.BEZEL -> AodBezelMedia(media = target, config = theme.config, onIntent = onIntent)
                AodThemeValue.COMPASS -> AodCompassMedia(media = target, config = theme.config, onIntent = onIntent)
                AodThemeValue.HALF_MOON -> AodHalfMoonMedia(media = target, config = theme.config, onIntent = onIntent)
                AodThemeValue.GRADIENT_LINE -> AodGradientLineMedia(media = target, config = theme.config, onIntent = onIntent)
                AodThemeValue.RIPPLE -> AodRippleMedia(media = target, config = theme.config, onIntent = onIntent)
                AodThemeValue.TICK -> AodTickMedia(media = target, config = theme.config, onIntent = onIntent)
                AodThemeValue.BIG_OVERLAP -> AodBigOverlapMedia(media = target, config = theme.config, onIntent = onIntent)
                AodThemeValue.QUARTERS -> AodQuartersMedia(media = target, config = theme.config, onIntent = onIntent)
            }
        }
    }
}

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
    size: Dp = 40.dp,
    tint: Color = GreyB4,
) {
    Icon(
        painter = painterResource(iconRes),
        contentDescription = contentDescription,
        modifier = modifier
            .size(size)
            .then(
                if (isEnabled) {
                    Modifier.onClick(shape = CircleShape, ripple = false, action = onClick)
                } else {
                    Modifier
                },
            )
            .padding(8.dp),
        tint = if (isEnabled) tint else Grey5A,
    )
}

@Composable
private fun AodClassicMedia(
    media: MediaUiModel,
    style: AodComponentStyle,
    layout: AodMediaLayoutValue,
    onIntent: (AodIntent) -> Unit,
) {
    val progress = media.progress

    when (layout) {
        AodMediaLayoutValue.ROW -> AodBlock(modifier = Modifier.padding(top = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TrackInfo(
                    media = media,
                    style = style,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                )

                MediaButtons(media = media, style = style, onIntent = onIntent)
            }

            if (progress != null) MediaProgress(progress = progress, style = style)
        }

        AodMediaLayoutValue.CENTER -> AodBlock(modifier = Modifier.padding(top = 20.dp)) {
            TrackInfo(
                media = media,
                style = style,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                MediaButtons(media = media, style = style, onIntent = onIntent)
            }

            if (progress != null) MediaProgress(progress = progress, style = style)
        }

        AodMediaLayoutValue.COMPACT -> Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .widthIn(max = 296.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PlayPauseButton(media = media, style = style, onIntent = onIntent)

            Text(
                text = listOfNotNull(media.title, media.artist).joinToString(separator = "  ·  "),
                modifier = Modifier.weight(1f, fill = false).aodTint(style),
                color = style.color,
                fontSize = 13.sp * style.scale,
                fontFamily = style.fontFamily,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            AodMediaButton(
                iconRes = R.drawable.ic_skip_forward_light,
                contentDescription = stringResource(R.string.aod_media_next),
                isEnabled = media.canSkipToNext,
                onClick = { onIntent(AodIntent.SkipToNextTrack) },
                size = 36.dp * style.scale,
                modifier = if (media.canSkipToNext) Modifier.aodTint(style) else Modifier,
                tint = style.color,
            )
        }
    }
}

@Composable
private fun TrackInfo(
    media: MediaUiModel,
    style: AodComponentStyle,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
) {
    Column(modifier = modifier) {
        Text(
            text = media.title,
            modifier = Modifier.fillMaxWidth().aodTint(style),
            color = style.color,
            fontSize = 14.sp * style.scale,
            fontWeight = FontWeight.SemiBold,
            fontFamily = style.fontFamily,
            textAlign = textAlign,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        media.artist?.let { artist ->
            Text(
                text = artist,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.7f)
                    .aodTint(style),
                color = style.color,
                fontSize = 12.sp * style.scale,
                fontFamily = style.fontFamily,
                textAlign = textAlign,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun MediaButtons(
    media: MediaUiModel,
    style: AodComponentStyle,
    onIntent: (AodIntent) -> Unit,
) {
    AodMediaButton(
        iconRes = R.drawable.ic_skip_back_light,
        contentDescription = stringResource(R.string.aod_media_previous),
        isEnabled = media.canSkipToPrevious,
        onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
        size = 40.dp * style.scale,
        modifier = if (media.canSkipToPrevious) Modifier.aodTint(style) else Modifier,
        tint = style.color,
    )

    PlayPauseButton(media = media, style = style, onIntent = onIntent)

    AodMediaButton(
        iconRes = R.drawable.ic_skip_forward_light,
        contentDescription = stringResource(R.string.aod_media_next),
        isEnabled = media.canSkipToNext,
        onClick = { onIntent(AodIntent.SkipToNextTrack) },
        size = 40.dp * style.scale,
        modifier = if (media.canSkipToNext) Modifier.aodTint(style) else Modifier,
        tint = style.color,
    )
}

@Composable
private fun PlayPauseButton(
    media: MediaUiModel,
    style: AodComponentStyle,
    onIntent: (AodIntent) -> Unit,
) {
    AodMediaButton(
        iconRes = if (media.isPlaying) R.drawable.ic_pause_light else R.drawable.ic_play_light,
        contentDescription = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play),
        isEnabled = true,
        onClick = { onIntent(AodIntent.PlayPauseMedia) },
        size = 40.dp * style.scale,
        modifier = Modifier.aodTint(style),
        tint = style.color,
    )
}

@Composable
private fun MediaProgress(
    progress: MediaProgressUiModel,
    style: AodComponentStyle,
) {
    AodMediaProgress(
        progress = progress,
        track = { fraction -> ClassicProgressTrack(fraction = fraction, color = style.color, brush = style.gradient?.toBrush()) },
        modifier = Modifier.padding(top = 10.dp),
    )
}

@Composable
private fun ClassicProgressTrack(
    fraction: () -> Float,
    color: Color,
    brush: Brush?,
) {
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
                        val filled = Size(width = size.width * fraction(), height = size.height)
                        if (brush != null) {
                            drawRoundRect(brush = brush, size = filled, cornerRadius = radius)
                        } else {
                            drawRoundRect(color = color, size = filled, cornerRadius = radius)
                        }
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
        style = AodComponentStyle(font = ClockFontValue.DEFAULT, scale = 1f, color = GreyB4),
        layout = AodMediaLayoutValue.ROW,
        onIntent = {},
    )
}
