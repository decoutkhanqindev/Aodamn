package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tick

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tick.TickThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A
import kotlinx.collections.immutable.toImmutableList

// Trình phát của Nhịp giây như media_widget_lt của app mẫu: lùi · tên bài in hoa / nghệ sĩ tối hơn 30% · tới, chữ giãn 0,25em, không có nút phát
// (chạm tên để phát / dừng; đang dừng thì chữ mờ đi, app mẫu không có). Đổi bài thì tên trượt lên từ dưới như app mẫu; tên dài thì chạy chữ.
// Rộng theo khung của bố cục (màn dọc: màn trừ 40dp mỗi bên; màn ngang: vùng bên).
@Composable
fun AodTickMedia(
    media: MediaUiModel,
    config: AodThemeConfig,
    onIntent: (AodIntent) -> Unit,
) {
    val color = config[TickThemeSpec.MediaColor]
    val gradientBrush = config.gradientOf(TickThemeSpec.MediaColor)?.toBrush()
    val titleBrush = config.brushOf(TickThemeSpec.MediaColor)
    val artistBrush = config.darkerBrushOf(TickThemeSpec.MediaColor)
    val textAlpha = animateFloatAsState(
        targetValue = if (media.isPlaying) 1f else TickMetrics.PAUSED_ALPHA,
        animationSpec = tween(durationMillis = 400),
        label = "TickTrackAlpha",
    )
    val playLabel = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TickMediaButton(
            iconRes = R.drawable.ic_skip_back_fill,
            contentDescription = stringResource(R.string.aod_media_previous),
            isEnabled = media.canSkipToPrevious,
            color = color,
            gradient = gradientBrush,
            onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
        )

        AnimatedContent(
            targetState = media.title to media.artist,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = TickMetrics.TextGap)
                .alpha(textAlpha.value)
                .onClick(ripple = false) { onIntent(AodIntent.PlayPauseMedia) }
                .semantics {
                    role = Role.Button
                    onClick(label = playLabel) {
                        onIntent(AodIntent.PlayPauseMedia)
                        true
                    }
                },
            transitionSpec = { (slideInVertically { height -> height } + fadeIn()) togetherWith fadeOut() },
            contentAlignment = Alignment.Center,
            label = "TickTrack",
        ) { (title, artist) ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TickMediaText(
                    text = title.uppercase(),
                    brush = titleBrush,
                    fontSize = TickMetrics.TitleSize,
                    fontWeight = FontWeight.Bold,
                    iterations = 2,
                )

                artist?.let {
                    TickMediaText(
                        text = artist.uppercase(),
                        brush = artistBrush,
                        fontSize = TickMetrics.ArtistSize,
                        fontWeight = FontWeight.Normal,
                        iterations = 1,
                        modifier = Modifier.padding(top = TickMetrics.ArtistGap),
                    )
                }
            }
        }

        TickMediaButton(
            iconRes = R.drawable.ic_skip_forward_fill,
            contentDescription = stringResource(R.string.aod_media_next),
            isEnabled = media.canSkipToNext,
            color = color,
            gradient = gradientBrush,
            onClick = { onIntent(AodIntent.SkipToNextTrack) },
        )
    }
}

// Tên bài / nghệ sĩ một dòng, dài thì chạy chữ (2 / 1 lần như marqueeRepeatLimit của app mẫu).
@Composable
private fun TickMediaText(
    text: String,
    brush: Brush,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    iterations: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.basicMarquee(iterations = iterations),
        style = LocalTextStyle.current.merge(TextStyle(brush = brush)),
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = TickThemeSpec.Font,
        letterSpacing = TickMetrics.TrackSpacing,
        maxLines = 1,
    )
}

// Nút lùi / tới: ô 40dp, icon đặc 12dp; không lùi / tới được thì xám.
@Composable
private fun TickMediaButton(
    @DrawableRes iconRes: Int,
    contentDescription: String,
    isEnabled: Boolean,
    color: Color,
    gradient: Brush?,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(TickMetrics.ButtonSize)
            .then(if (isEnabled) Modifier.onClick(shape = CircleShape, ripple = false, action = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(TickMetrics.ButtonIconSize)
                .aodTint(gradient.takeIf { isEnabled }),
            tint = if (isEnabled) color else Grey5A,
        )
    }
}

// Nghệ sĩ tối hơn 30% như app mẫu (màu điều khiển pha đen); gradient thì tối từng màu, giữ hướng.
private fun AodThemeConfig.darkerBrushOf(option: AodColorOption): Brush {
    val gradient = gradientOf(option) ?: return SolidColor(lerp(get(option), Black, TickMetrics.TEXT_DARKEN))
    return gradient.copy(colors = gradient.colors.map { color -> lerp(color, Black, TickMetrics.TEXT_DARKEN) }.toImmutableList()).toBrush()
}

@Preview(widthDp = 393)
@Composable
private fun AodTickMediaPreview() {
    AodTickMedia(
        media = MediaUiModel(
            title = "That Cool Song",
            artist = "It's Artist",
            isPlaying = true,
            canSkipToPrevious = true,
            canSkipToNext = true,
        ),
        config = AodThemeConfig(),
        onIntent = {},
    )
}
