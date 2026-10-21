package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.ripple

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.AodNotificationsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.ripple.RippleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Black

// Gợn sóng, màn dọc như layout mẫu: vùng thông báo trên cùng (hàng icon, thông báo mới nhất), mặt số giữa phần còn lại, thời tiết giữa đáy mặt số và
// vùng dưới, vùng dưới giữ chỗ cho trình phát (mặt số không nhảy khi nhạc bật / tắt) rồi dòng pin cách đáy 30dp.
@Composable
fun AodRipplePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(RippleMetrics.TopZone),
            contentAlignment = Alignment.Center,
        ) {
            RippleNotifications(state = state, maxWidth = maxWidth - RippleMetrics.PlayerMargin * 2)
        }

        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            val faceBottom = rippleFaceReach(state, minOf(maxWidth, maxHeight))

            RippleDial(state = state)

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height((maxHeight / 2 - faceBottom).coerceAtLeast(0.dp)),
                contentAlignment = Alignment.Center,
            ) {
                RippleWeather(state = state)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(RippleMetrics.PlayerTop + RippleMetrics.PlayerHeight + RippleMetrics.PlayerBottom)
                .padding(horizontal = RippleMetrics.PlayerMargin)
                .padding(top = RippleMetrics.PlayerTop),
        ) {
            RipplePlayer(state = state, onIntent = onIntent)
        }

        RippleBattery(state = state)

        Spacer(modifier = Modifier.height(RippleMetrics.BottomMargin))
    }
}

// Gợn sóng, màn ngang (mock đã duyệt; app mẫu dùng chung layout dọc nên mặt số chỉ còn rất nhỏ): mặt số giữa màn to như màn dọc, hai vùng bên chia
// thông tin như màn dọc chia trên / dưới. Không nhạc: thông báo trái, thời tiết + pin phải; có nhạc: thông báo, thời tiết, pin trái, trình phát
// phải; đổi bằng mờ chéo.
@Composable
fun AodRippleLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val reach = rippleFaceReach(state, minOf(maxWidth, maxHeight))
        val side = (maxWidth / 2 - reach - RippleMetrics.LandscapeGap * 2).coerceAtLeast(0.dp)
        val hasMedia = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components

        RippleDial(state = state)

        Crossfade(targetState = hasMedia, label = "RippleLandscape") { isMediaShown ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = RippleMetrics.LandscapeGap)
                        .width(side),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    RippleNotifications(state = state, maxWidth = side)

                    if (isMediaShown) {
                        RippleWeather(state = state, modifier = Modifier.padding(vertical = RippleMetrics.LandscapeItemGap / 2))
                        RippleBattery(state = state, modifier = Modifier.padding(vertical = RippleMetrics.LandscapeItemGap / 2))
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = RippleMetrics.LandscapeGap)
                        .width(side),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isMediaShown) {
                        RipplePlayer(state = state, onIntent = onIntent)
                    } else {
                        RippleWeather(state = state, modifier = Modifier.padding(vertical = RippleMetrics.LandscapeItemGap / 2))
                        RippleBattery(state = state, modifier = Modifier.padding(vertical = RippleMetrics.LandscapeItemGap / 2))
                    }
                }
            }
        }
    }
}

@Composable
private fun RippleDial(state: AodState) {
    AodRippleDial(
        nowMillis = state.nowMillis,
        isLive = state.isClockLive,
        isDateShown = state.extras.isDateEnabled,
        config = state.appearance.theme.config,
    )
}

// Mép ngoài đĩa (kể cả gợn) tính từ tâm vùng mặt số.
private fun rippleFaceReach(state: AodState, side: Dp): Dp =
    side / 2 * (state.appearance.theme.config[RippleThemeSpec.Size] / 100f) * RippleMetrics.FACE_RADIUS * (1f + RippleMetrics.WAVE_DEPTH)

// Hàng icon thông báo, cách 12dp thông báo mới nhất; dòng nào không có thì không chiếm chỗ.
@Composable
private fun RippleNotifications(
    state: AodState,
    maxWidth: Dp,
) {
    val config = state.appearance.theme.config
    val latest = state.notifications.latest
    val hasIcons = state.notifications.icons.isNotEmpty()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AodStatusLine(
            battery = null,
            isFlashlightOn = false,
            notifications = state.notifications.copy(latest = null),
            color = config[RippleThemeSpec.NotificationsColor],
            glyphHeight = RippleMetrics.IconRow,
            fontFamily = RippleThemeSpec.Font,
            brush = config.gradientOf(RippleThemeSpec.NotificationsColor)?.toBrush(),
        )

        AnimatedVisibility(visible = latest != null) {
            if (latest != null) {
                RippleLatest(
                    latest = latest,
                    state = state,
                    modifier = Modifier
                        .padding(top = if (hasIcons) RippleMetrics.LatestGap else 0.dp)
                        .widthIn(max = maxWidth),
                )
            }
        }
    }
}

// Thông báo mới nhất như notification_lt của app mẫu: icon đen trong vòng tròn tô màu thông báo, tiêu đề 16sp, nội dung 14sp tối hơn (tối đa 2
// dòng).
@Composable
private fun RippleLatest(
    latest: NotificationContentUiModel,
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val brush = config.brushOf(RippleThemeSpec.NotificationsColor)
    val text = if (latest.isHidden) stringResource(R.string.aod_notification_content_hidden) else latest.text

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(RippleMetrics.LatestBadge)
                .background(brush = brush, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                bitmap = remember(latest.icon) { latest.icon.asImageBitmap() },
                contentDescription = null,
                modifier = Modifier.size(RippleMetrics.LatestIcon),
                colorFilter = ColorFilter.tint(Black),
            )
        }

        Column(modifier = Modifier.padding(start = RippleMetrics.LatestGap)) {
            Text(
                text = latest.title,
                style = TextStyle(brush = brush, fontSize = RippleMetrics.LatestTitleSize, fontFamily = RippleThemeSpec.Font),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    modifier = Modifier.padding(top = RippleMetrics.LatestTextGap),
                    style = TextStyle(
                        brush = config.darkerBrushOf(RippleThemeSpec.NotificationsColor),
                        fontSize = RippleMetrics.LatestTextSize,
                        fontWeight = FontWeight.Normal,
                        fontFamily = RippleThemeSpec.Font,
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// Thời tiết (khối dùng chung) một dòng; tắt thì không chiếm chỗ.
@Composable
private fun RippleWeather(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val components = state.appearance.components

    AnimatedVisibility(
        visible = state.weather != null && AodComponentValue.WEATHER in state.appearance.theme.value.spec.components,
        modifier = modifier,
    ) {
        AodWeather(
            weather = state.weather,
            style = components.styleOf(AodComponentValue.WEATHER),
            layout = components.weatherLayout,
        )
    }
}

// Dòng pin như bottom_tv của app mẫu: mờ 0,8 khi không sạc, rõ hẳn khi đang sạc; đèn pin đang bật thì icon cạnh pin.
@Composable
private fun RippleBattery(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val battery = state.battery?.takeIf { state.extras.isBatteryEnabled }
    val alpha by animateFloatAsState(
        targetValue = if (battery?.isCharging == true) 1f else RippleMetrics.IDLE_BATTERY_ALPHA,
        label = "RippleBatteryAlpha",
    )

    AodStatusLine(
        battery = battery,
        isFlashlightOn = state.isFlashlightOn,
        notifications = AodNotificationsUiModel(),
        color = config[RippleThemeSpec.BatteryColor],
        glyphHeight = RippleMetrics.StatusGlyph,
        fontFamily = RippleThemeSpec.Font,
        modifier = modifier.graphicsLayer { this.alpha = alpha },
        brush = config.gradientOf(RippleThemeSpec.BatteryColor)?.toBrush(),
    )
}

// Trình phát của chủ đề (AodRippleMedia qua AodMediaControls); không có nhạc thì trống, chỗ vẫn giữ ở màn dọc.
@Composable
private fun RipplePlayer(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    AnimatedVisibility(visible = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components) {
        AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodRipplePortraitPreview() {
    AodRipplePortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.RIPPLE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodRippleLandscapePreview() {
    AodRippleLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.RIPPLE)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
