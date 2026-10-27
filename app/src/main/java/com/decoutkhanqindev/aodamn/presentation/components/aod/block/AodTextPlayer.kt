package com.decoutkhanqindev.aodamn.presentation.components.aod.block

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.MediaUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodTextPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent

@Composable
fun AodTextPlayer(
    media: MediaUiModel,
    style: AodTextPlayerStyle,
    color: Color,
    gradient: Brush?,
    titleBrush: Brush,
    artistBrush: Brush,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textAlpha = animateFloatAsState(
        targetValue = if (media.isPlaying) 1f else style.pausedAlpha,
        animationSpec = tween(durationMillis = 400),
        label = "AodTextPlayerAlpha",
    )
    val playLabel = stringResource(if (media.isPlaying) R.string.aod_media_pause else R.string.aod_media_play)
    val buttonModifier = if (style.buttonAlpha < 1f) Modifier.alpha(style.buttonAlpha) else Modifier

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AodPlayerButton(
            iconRes = R.drawable.ic_skip_back_fill,
            contentDescription = stringResource(R.string.aod_media_previous),
            isEnabled = media.canSkipToPrevious,
            color = color,
            gradient = gradient,
            size = style.buttonSize,
            iconSize = style.buttonIconSize,
            onClick = { onIntent(AodIntent.SkipToPreviousTrack) },
            modifier = buttonModifier,
        )

        AnimatedContent(
            targetState = media.title to media.artist,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = style.textGap)
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
            label = "AodTextPlayerTrack",
        ) { (title, artist) ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextPlayerText(
                    text = if (style.isUppercase) title.uppercase() else title,
                    brush = titleBrush,
                    fontSize = style.titleSize,
                    fontWeight = style.titleWeight,
                    style = style,
                    iterations = 2,
                )

                artist?.let {
                    TextPlayerText(
                        text = if (style.isUppercase) artist.uppercase() else artist,
                        brush = artistBrush,
                        fontSize = style.artistSize,
                        fontWeight = style.artistWeight,
                        style = style,
                        iterations = 1,
                        modifier = Modifier
                            .padding(top = style.artistGap)
                            .then(if (style.artistAlpha < 1f) Modifier.alpha(style.artistAlpha) else Modifier),
                    )
                }
            }
        }

        AodPlayerButton(
            iconRes = R.drawable.ic_skip_forward_fill,
            contentDescription = stringResource(R.string.aod_media_next),
            isEnabled = media.canSkipToNext,
            color = color,
            gradient = gradient,
            size = style.buttonSize,
            iconSize = style.buttonIconSize,
            onClick = { onIntent(AodIntent.SkipToNextTrack) },
            modifier = buttonModifier,
        )
    }
}

@Composable
private fun TextPlayerText(
    text: String,
    brush: Brush,
    fontSize: TextUnit,
    fontWeight: FontWeight?,
    style: AodTextPlayerStyle,
    iterations: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.basicMarquee(iterations = iterations),
        style = LocalTextStyle.current.merge(TextStyle(brush = brush)),
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = style.fontFamily,
        letterSpacing = style.letterSpacing,
        maxLines = 1,
    )
}
