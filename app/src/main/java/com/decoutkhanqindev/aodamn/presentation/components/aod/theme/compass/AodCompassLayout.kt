package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.compass

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.NotificationContentUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.CompassThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

// La bàn, màn dọc (mock đã duyệt): mặt số giữa màn như layout mẫu; ngày (+ thời tiết), dòng pin · thông báo, thông báo mới nhất và trình phát
// xếp dọc giữa màn, thẳng đáy cách đáy 60dp (khối lớn lên phía trên khi có thêm dòng). Trình phát rộng bằng màn trừ 40dp mỗi bên như layout mẫu.
@Composable
fun AodCompassPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val paints = rememberCompassPaints()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val infoWidth = maxWidth - CompassMetrics.SideMargin * 2
        val playerWidth = maxWidth - CompassMetrics.PlayerMargin * 2

        AodCompassDial(
            nowMillis = state.nowMillis,
            isLive = state.isClockLive,
            config = state.appearance.theme.config,
            paints = paints,
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = CompassMetrics.BottomMargin),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CompassInfo(state = state, maxWidth = infoWidth)

            CompassPlayer(
                state = state,
                width = playerWidth,
                onIntent = onIntent,
                modifier = Modifier.padding(top = CompassMetrics.MediaGap),
            )
        }
    }
}

// La bàn, màn ngang (mock đã duyệt): mặt số giữa màn (cạnh = cạnh ngắn × cỡ như app mẫu), hai nhóm giữa hai vùng bên, giữa chiều cao. Không nhạc:
// ngày (+ thời tiết) trái, pin · thông báo phải; có nhạc: ngày, pin · thông báo xếp dọc bên trái, trình phát bên phải. Nhạc phát / dừng thì đổi
// bằng mờ chéo.
@Composable
fun AodCompassLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val paints = rememberCompassPaints()
    val hasMedia = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val dial = maxHeight * (CompassMetrics.DIAL_FRACTION * state.appearance.theme.config[CompassThemeSpec.Size] / 100f)
        val zone = (maxWidth - dial) / 2

        AodCompassDial(
            nowMillis = state.nowMillis,
            isLive = state.isClockLive,
            config = state.appearance.theme.config,
            paints = paints,
        )

        Crossfade(targetState = hasMedia, label = "AodCompassLandscape") { isMediaShown ->
            Row(modifier = Modifier.fillMaxSize()) {
                CompassZone(width = zone) {
                    if (isMediaShown) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CompassInfo(state = state, maxWidth = zone - CompassMetrics.PlayerInset)
                        }
                    } else {
                        CompassDateRow(state = state)
                    }
                }

                Spacer(modifier = Modifier.width(dial))

                CompassZone(width = zone) {
                    if (isMediaShown) {
                        CompassPlayer(state = state, width = zone - CompassMetrics.PlayerInset, onIntent = onIntent)
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CompassStatus(state = state, maxWidth = zone - CompassMetrics.PlayerInset)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompassZone(
    width: Dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(width)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

// Ngày, cách 15dp dòng pin · thông báo, cách 12dp thông báo mới nhất; dòng nào không có thì không chiếm chỗ (cả khoảng cách).
@Composable
private fun CompassInfo(
    state: AodState,
    maxWidth: Dp,
) {
    CompassDateRow(state = state)

    CompassStatus(
        state = state,
        maxWidth = maxWidth,
        modifier = Modifier.padding(top = if (state.isDateRowShown()) CompassMetrics.DateGap else 0.dp),
    )
}

// Thứ tự trong cột: dòng pin rồi thông báo mới nhất (Column của nơi gọi xếp dọc).
@Composable
private fun CompassStatus(
    state: AodState,
    maxWidth: Dp,
    modifier: Modifier = Modifier,
) {
    val color = state.appearance.theme.config[CompassThemeSpec.StatusColor]
    val latest = state.notifications.latest

    AodStatusLine(
        battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
        isFlashlightOn = state.isFlashlightOn,
        notifications = state.notifications,
        color = color,
        glyphHeight = CompassMetrics.StatusGlyph,
        fontFamily = CompassThemeSpec.Font,
        modifier = modifier,
    )

    AnimatedVisibility(visible = latest != null) {
        if (latest != null) {
            CompassLatest(
                latest = latest,
                color = color,
                modifier = Modifier
                    .padding(top = CompassMetrics.LatestGap)
                    .widthIn(max = maxWidth),
            )
        }
    }
}

// Thông báo mới nhất như notify_lt của app mẫu: icon 16dp, chữ in hoa một dòng chạy chữ (marquee một lần) khi dài.
@Composable
private fun CompassLatest(
    latest: NotificationContentUiModel,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val text = when {
        latest.isHidden -> stringResource(R.string.aod_notification_content_hidden)
        latest.text.isEmpty() -> latest.title
        else -> stringResource(R.string.aod_compass_notification, latest.title, latest.text)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            bitmap = remember(latest.icon) { latest.icon.asImageBitmap() },
            contentDescription = null,
            modifier = Modifier.size(CompassMetrics.LatestIcon),
            colorFilter = ColorFilter.tint(color),
        )

        Text(
            text = text.uppercase(),
            modifier = Modifier
                .padding(start = CompassMetrics.LatestIconGap)
                .basicMarquee(iterations = 1),
            color = color,
            fontSize = CompassMetrics.LatestSize,
            fontFamily = CompassThemeSpec.Font,
            letterSpacing = CompassMetrics.LatestSpacing,
            maxLines = 1,
        )
    }
}

// Ngày in hoa giãn chữ như layout mẫu ("dd MMMM yyyy"); thời tiết cạnh ngày, kiểu chỉnh ở Tùy chỉnh như các chủ đề khác.
@Composable
private fun CompassDateRow(state: AodState) {
    val isWeatherShown = AodComponentValue.WEATHER in state.appearance.theme.value.spec.components
    val components = state.appearance.components

    Row(verticalAlignment = Alignment.CenterVertically) {
        AnimatedVisibility(visible = state.extras.isDateEnabled) {
            CompassDate(
                nowMillis = state.nowMillis,
                color = state.appearance.theme.config[CompassThemeSpec.DateColor],
                modifier = Modifier.padding(
                    end = if (isWeatherShown && state.weather != null) CompassMetrics.WeatherGap else 0.dp,
                ),
            )
        }

        if (isWeatherShown) {
            AodWeather(
                weather = state.weather,
                style = components.styleOf(AodComponentValue.WEATHER),
                layout = components.weatherLayout,
            )
        }
    }
}

@Composable
private fun CompassDate(
    nowMillis: Long,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val date = rememberZonedDateTime(nowMillis).format(rememberFormatter(R.string.aod_compass_date_pattern)).uppercase()

    Text(
        text = date,
        modifier = modifier,
        color = color,
        fontSize = CompassMetrics.DateSize,
        fontWeight = FontWeight.Bold,
        fontFamily = CompassThemeSpec.Font,
        letterSpacing = CompassMetrics.DateSpacing,
        maxLines = 1,
        softWrap = false,
    )
}

// Trình phát của chủ đề (AodCompassMedia qua AodMediaControls) trong khung rộng cố định; không có nhạc thì không chiếm chỗ (cả khoảng cách).
@Composable
private fun CompassPlayer(
    state: AodState,
    width: Dp,
    onIntent: (AodIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components,
        modifier = modifier,
    ) {
        Box(modifier = Modifier.width(width)) {
            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }
    }
}

private fun AodState.isDateRowShown(): Boolean =
    extras.isDateEnabled || (weather != null && AodComponentValue.WEATHER in appearance.theme.value.spec.components)

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodCompassPortraitPreview() {
    AodCompassPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.COMPASS)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodCompassLandscapePreview() {
    AodCompassLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.COMPASS)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
