package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tick

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.aodTint
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tick.TickThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

// Nhịp giây, màn dọc (mock đã duyệt): vòng phủ màn như CoumTick của app mẫu, giờ + ngày (+ thời tiết) giữa vòng; hàng icon thông báo và thông báo
// mới nhất ở trên, trình phát và dòng pin ở dưới, cùng cách mép 70dp như khối dưới của layout mẫu (app mẫu dồn hết xuống dưới; chia hai phía cho cân
// quanh vòng, user duyệt).
@Composable
fun AodTickPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        TickDial(state = state)

        TickNotifications(
            state = state,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = TickMetrics.EdgeMargin)
                .padding(horizontal = TickMetrics.SideMargin),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = TickMetrics.EdgeMargin),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TickPlayer(
                state = state,
                onIntent = onIntent,
                modifier = Modifier.padding(horizontal = TickMetrics.PlayerMargin),
            )

            TickBattery(state = state)
        }
    }
}

// Nhịp giây, màn ngang (mock đã duyệt; app mẫu dùng chung layout dọc nên vòng tính theo bề ngang chỉ còn hai cung lớn sát mép): vòng giữa màn cỡ
// như màn dọc, hai vùng bên đối xứng: thông báo trái, dòng pin phải, có nhạc thì trình phát ngay trên dòng pin.
@Composable
fun AodTickLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val unit = min(maxWidth, maxHeight) / 2
        val side = (maxWidth / 2 - unit * TickMetrics.SECOND_REACH - TickMetrics.LandscapeGap - TickMetrics.LandscapeMargin)
            .coerceAtLeast(0.dp)

        TickDial(state = state)

        TickNotifications(
            state = state,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = TickMetrics.LandscapeMargin)
                .width(side),
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = TickMetrics.LandscapeMargin)
                .width(side),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TickPlayer(state = state, onIntent = onIntent)

            TickBattery(state = state)
        }
    }
}

@Composable
private fun TickDial(state: AodState) {
    AodTickDial(
        nowMillis = state.nowMillis,
        isLive = state.isClockLive,
        isDateShown = state.extras.isDateEnabled,
        config = state.appearance.theme.config,
        below = { TickWeather(state = state) },
    )
}

// Hàng icon thông báo rồi thông báo mới nhất cách 12dp; dòng nào không có thì không chiếm chỗ.
@Composable
private fun TickNotifications(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val color = config[TickThemeSpec.NotificationsColor]
    val brush = config.gradientOf(TickThemeSpec.NotificationsColor)?.toBrush()
    val latest = state.notifications.latest
    val hasIcons = state.notifications.icons.isNotEmpty()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AodStatusLine(
            battery = null,
            isFlashlightOn = false,
            notifications = state.notifications.copy(latest = null),
            color = color,
            glyphHeight = TickMetrics.IconRow,
            fontFamily = TickThemeSpec.Font,
            brush = brush,
        )

        AnimatedVisibility(
            visible = latest != null,
            enter = slideInVertically { height -> -height } + expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            if (latest != null) {
                TickLatest(
                    latest = latest,
                    color = color,
                    brush = brush,
                    modifier = Modifier.padding(top = if (hasIcons) TickMetrics.LatestGap else 0.dp),
                )
            }
        }
    }
}

// Thông báo mới nhất như notify_lt của app mẫu: icon 17dp, chữ in hoa giãn chữ một dòng, dài thì chạy chữ một lần; thông báo mới trượt xuống từ trên
// như move_in_from_top.
@Composable
private fun TickLatest(
    latest: NotificationContentUiModel,
    color: Color,
    brush: Brush?,
    modifier: Modifier = Modifier,
) {
    val text = when {
        latest.isHidden -> stringResource(R.string.aod_notification_content_hidden)
        latest.text.isEmpty() -> latest.title
        else -> stringResource(R.string.aod_tick_notification, latest.title, latest.text)
    }

    AnimatedContent(
        targetState = latest.icon to text,
        modifier = modifier,
        transitionSpec = { (slideInVertically { height -> -height } + fadeIn()) togetherWith fadeOut() },
        contentAlignment = Alignment.Center,
        label = "TickLatest",
    ) { (icon, content) ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                bitmap = remember(icon) { icon.asImageBitmap() },
                contentDescription = null,
                modifier = Modifier
                    .size(TickMetrics.LatestIcon)
                    .aodTint(brush),
                colorFilter = ColorFilter.tint(color),
            )

            Text(
                text = content.uppercase(),
                modifier = Modifier
                    .padding(start = TickMetrics.LatestIconGap)
                    .basicMarquee(iterations = 1)
                    .aodTint(brush),
                color = color,
                fontSize = TickMetrics.LatestSize,
                fontFamily = TickThemeSpec.Font,
                letterSpacing = TickMetrics.LatestSpacing,
                maxLines = 1,
            )
        }
    }
}

// Thời tiết (khối dùng chung) một dòng dưới ngày; tắt thì không chiếm chỗ.
@Composable
private fun TickWeather(state: AodState) {
    val components = state.appearance.components

    AnimatedVisibility(visible = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components) {
        AodWeather(
            weather = state.weather,
            style = components.styleOf(AodComponentValue.WEATHER),
            layout = components.weatherLayout,
        )
    }
}

// Dòng pin như battery_lt của app mẫu (icon 15dp, chữ 14sp); đèn pin đang bật thì icon cạnh pin.
@Composable
private fun TickBattery(state: AodState) {
    val config = state.appearance.theme.config

    AodStatusLine(
        battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
        isFlashlightOn = state.isFlashlightOn,
        notifications = AodNotificationsUiModel(),
        color = config[TickThemeSpec.BatteryColor],
        glyphHeight = TickMetrics.StatusGlyph,
        fontFamily = TickThemeSpec.Font,
        brush = config.gradientOf(TickThemeSpec.BatteryColor)?.toBrush(),
    )
}

// Trình phát của chủ đề (AodTickMedia qua AodMediaControls), cách dòng pin 16dp; không có nhạc thì không chiếm chỗ (cả khoảng cách).
@Composable
private fun TickPlayer(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(bottom = TickMetrics.PlayerGap)) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodTickPortraitPreview() {
    AodTickPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.TICK)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodTickLandscapePreview() {
    AodTickLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.TICK)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
