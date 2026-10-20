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

// Dải màu, màn dọc như layout mẫu: lề 40dp hai bên, 60dp trên / dưới; trình phát trên cùng (cách vạch 30dp), vạch chiếm phần còn lại, khối dưới
// (thông báo mới nhất, dòng pin · thông báo) neo đáy. Trình phát hiện / ẩn thì vạch co giãn theo, giờ trượt theo.
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

// Dải màu, màn ngang (mock đã duyệt; app mẫu dùng chung layout dọc nên vạch ngắn, giờ nhỏ): vạch nằm ngang hết bề rộng trừ lề 40dp, giờ trượt từ
// trái sang phải; trình phát giữa phía trên, khối dưới giữa phía dưới, cùng trục giữa như màn dọc.
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

// Trình phát của chủ đề (AodGradientLineMedia qua AodMediaControls); không có nhạc thì không chiếm chỗ (cả khoảng cách tới vạch).
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

// Thông báo mới nhất (cách 10dp) trên dòng pin · thông báo; dòng nào không có thì không chiếm chỗ.
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

// Dòng pin · icon thông báo như dòng trạng thái của các chủ đề khác, nhưng hai màu như app mẫu: pin (chữ mờ 0,9 như battery_status) theo màu pin,
// chấm ngăn và icon theo màu thông báo. Khoảng cách, chấm theo tỉ lệ của AodStatusLine.
@Composable
private fun GradientLineStatus(state: AodState) {
    val config = state.appearance.theme.config
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val hasPower = battery != null || state.isFlashlightOn
    val hasIcons = state.notifications.icons.isNotEmpty()
    val notificationsColor = config[GradientLineThemeSpec.NotificationsColor]
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
        )

        AnimatedVisibility(visible = hasPower && hasIcons, enter = fadeIn(), exit = fadeOut()) {
            Box(
                modifier = Modifier
                    .padding(start = glyph * 1.13f, end = glyph * 1.065f)
                    .size(glyph * 0.228f)
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
        )
    }
}

// Thông báo mới nhất như notify_lt của app mẫu: icon 20dp, cách 10dp, chữ 16sp một dòng chạy chữ (một lần) khi dài.
@Composable
private fun GradientLineLatest(
    latest: NotificationContentUiModel,
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val color = state.appearance.theme.config[GradientLineThemeSpec.NotificationsColor]
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
            modifier = Modifier.size(GradientLineMetrics.LatestIcon),
            colorFilter = ColorFilter.tint(color),
        )

        Text(
            text = text,
            modifier = Modifier
                .padding(start = GradientLineMetrics.LatestIconGap)
                .basicMarquee(iterations = 1),
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
