package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.gradientline

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gradientline.GradientLineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodGradientLinePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = GradientLineMetrics.SideMargin, vertical = GradientLineMetrics.VerticalMargin),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GradientLinePlayer(
            state = state,
            onIntent = onIntent,
            modifier = Modifier.fillMaxWidth(),
            bottomGap = GradientLineMetrics.PlayerGap,
        )

        AodGradientLineClock(
            state = state,
            isVertical = true,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = GradientLineMetrics.ClockGap),
        )

        GradientLineDetails(
            state = state,
            modifier = Modifier.padding(top = GradientLineMetrics.DetailsPadding),
        )
    }
}

@Composable
fun AodGradientLineLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AodGradientLineClock(
            state = state,
            isVertical = false,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = GradientLineMetrics.SideMargin),
        )

        GradientLinePlayer(
            state = state,
            onIntent = onIntent,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = GradientLineMetrics.LandscapeTopMargin)
                .width(GradientLineMetrics.LandscapePlayerWidth),
        )

        GradientLineDetails(
            state = state,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = GradientLineMetrics.LandscapeBottomMargin),
        )
    }
}

@Composable
private fun GradientLinePlayer(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
    bottomGap: Dp = 0.dp,
) {
    AnimatedVisibility(
        visible = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(bottom = bottomGap)) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }
    }
}

@Composable
private fun GradientLineDetails(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val latest = state.notifications.latest

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(visible = latest != null) {
            if (latest != null) {
                GradientLineLatest(
                    latest = latest,
                    state = state,
                    modifier = Modifier.padding(bottom = GradientLineMetrics.LatestGap),
                )
            }
        }

        GradientLineStatus(state = state)
    }
}

@Composable
private fun GradientLineStatus(state: AodState) {
    val config = state.appearance.theme.config
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val hasPower = battery != null || state.isFlashlightOn
    val hasIcons = state.notifications.icons.isNotEmpty()
    val notificationsColor = config[GradientLineThemeSpec.NotificationsColor]
    val notificationsBrush = config.gradientOf(GradientLineThemeSpec.NotificationsColor)?.toBrush()
    val glyph = GradientLineMetrics.StatusGlyph

    Row(verticalAlignment = Alignment.CenterVertically) {
        AodStatusLine(
            battery = battery,
            isFlashlightOn = state.isFlashlightOn,
            notifications = AodNotificationsUiModel(),
            color = config[GradientLineThemeSpec.BatteryColor],
            glyphHeight = glyph,
            fontFamily = GradientLineThemeSpec.Font,
            modifier = Modifier.alpha(GradientLineMetrics.STATUS_ALPHA),
            brush = config.gradientOf(GradientLineThemeSpec.BatteryColor)?.toBrush(),
        )

        AnimatedVisibility(visible = hasPower && hasIcons, enter = fadeIn(), exit = fadeOut()) {
            Box(
                modifier = Modifier
                    .padding(start = glyph * 1.13f, end = glyph * 1.065f)
                    .size(glyph * 0.228f)
                    .aodTint(notificationsBrush)
                    .background(color = notificationsColor, shape = CircleShape),
            )
        }

        AodStatusLine(
            battery = null,
            isFlashlightOn = false,
            notifications = state.notifications.copy(latest = null),
            color = notificationsColor,
            glyphHeight = glyph,
            fontFamily = GradientLineThemeSpec.Font,
            brush = notificationsBrush,
        )
    }
}

@Composable
private fun GradientLineLatest(
    latest: NotificationContentUiModel,
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val color = state.appearance.theme.config[GradientLineThemeSpec.NotificationsColor]
    val brush = state.appearance.theme.config.gradientOf(GradientLineThemeSpec.NotificationsColor)?.toBrush()
    val text = when {
        latest.isHidden -> stringResource(R.string.aod_notification_content_hidden)
        latest.text.isEmpty() -> latest.title
        else -> stringResource(R.string.aod_gradient_line_notification, latest.title, latest.text)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            bitmap = remember(latest.icon) { latest.icon.asImageBitmap() },
            contentDescription = null,
            modifier = Modifier
                .size(GradientLineMetrics.LatestIcon)
                .aodTint(brush),
            colorFilter = ColorFilter.tint(color),
        )

        Text(
            text = text,
            modifier = Modifier
                .padding(start = GradientLineMetrics.LatestIconGap)
                .basicMarquee(iterations = 1)
                .aodTint(brush),
            color = color,
            fontSize = GradientLineMetrics.LatestSize,
            fontFamily = GradientLineThemeSpec.Font,
            maxLines = 1,
        )
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodGradientLinePortraitPreview() {
    AodGradientLinePortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.GRADIENT_LINE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodGradientLineLandscapePreview() {
    AodGradientLineLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.GRADIENT_LINE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
