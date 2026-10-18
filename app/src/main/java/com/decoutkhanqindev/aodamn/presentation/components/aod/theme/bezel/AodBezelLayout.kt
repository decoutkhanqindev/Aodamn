package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bezel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
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
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bezel.BezelThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import kotlin.math.roundToInt

// Vành, màn dọc (mock đã duyệt): mặt số phủ màn; thứ + ngày (+ thời tiết) giữa vùng từ số 12 tới đầu kim phút, dòng pin · thông báo và trình phát
// gọn giữa vùng từ đầu kim tới số 6, nên kim không bao giờ đè lên chữ. Nhạc phát / dừng thì nhóm dưới cao thấp có animation, nhóm tự căn giữa lại.
@Composable
fun AodBezelPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val paints = rememberBezelPaints()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val half = maxHeight / 2
        val unit = minOf(maxWidth, maxHeight) / 2
        val number = minOf(maxWidth, maxHeight) * (BezelMetrics.NUMBER_SIZE * BezelMetrics.NUMBER_CAP / 2f)
        val numberOffset = maxOf(maxWidth, maxHeight) / 2 * BezelMetrics.NUMBER_FAR
        val reach = unit * BezelMetrics.MINUTE_LENGTH
        val topCenter = ((half - numberOffset + number + BezelMetrics.NumberGap) + (half - reach - BezelMetrics.ReachGap)) / 2
        val bottomCenter = ((half + reach + BezelMetrics.ReachGap) + (half + numberOffset - number - BezelMetrics.ReachGap)) / 2
        val centerX = maxWidth / 2

        AodBezelDial(nowMillis = state.nowMillis, config = state.appearance.theme.config, paints = paints)

        BezelDateRow(state = state, modifier = Modifier.anchorAt(x = centerX, centerY = topCenter, anchor = 0.5f))

        Column(
            modifier = Modifier.anchorAt(x = centerX, centerY = bottomCenter, anchor = 0.5f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BezelStatus(state = state)

            AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
        }
    }
}

// Vành, màn ngang (mock đã duyệt): mặt số luôn phủ màn như app mẫu, hai nhóm giữa chiều cao, nhóm trái từ 0,25 bề ngang, nhóm phải thẳng phải ở 0,75.
// Không nhạc: pin · thông báo trái, ngày phải như app mẫu; có nhạc: ngày + pin xếp dọc bên trái, trình phát bên phải. Nhạc phát / dừng thì đổi
// bằng mờ chéo.
@Composable
fun AodBezelLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val paints = rememberBezelPaints()
    val hasMedia = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val start = maxWidth * BezelMetrics.LANDSCAPE_START
        val end = maxWidth * BezelMetrics.LANDSCAPE_END
        val centerY = maxHeight / 2

        AodBezelDial(nowMillis = state.nowMillis, config = state.appearance.theme.config, paints = paints)

        Crossfade(targetState = hasMedia, label = "AodBezelLandscape") { isMediaShown ->
            Box(modifier = Modifier.fillMaxSize()) {
                if (isMediaShown) {
                    Column(modifier = Modifier.anchorAt(x = start, centerY = centerY, anchor = 0f)) {
                        BezelDateRow(state = state)

                        BezelStatus(state = state, modifier = Modifier.padding(top = BezelMetrics.GroupGap))
                    }

                    Box(modifier = Modifier.anchorAt(x = end, centerY = centerY, anchor = 1f)) {
                        AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
                    }
                } else {
                    BezelStatus(state = state, modifier = Modifier.anchorAt(x = start, centerY = centerY, anchor = 0f))

                    BezelDateRow(state = state, modifier = Modifier.anchorAt(x = end, centerY = centerY, anchor = 1f))
                }
            }
        }
    }
}

// Thứ + số ngày cùng màu ngày (chỉnh được cả hai ở tab Ngày); thời tiết cạnh ngày, kiểu chỉnh ở Tùy chỉnh như các chủ đề khác.
@Composable
private fun BezelDateRow(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    val config = state.appearance.theme.config
    val components = state.appearance.components
    val isWeatherShown = AodComponentValue.WEATHER in state.appearance.theme.value.spec.components

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(visible = state.extras.isDateEnabled) {
            BezelDate(
                nowMillis = state.nowMillis,
                color = config[BezelThemeSpec.DateColor],
                modifier = Modifier.padding(end = if (isWeatherShown && state.weather != null) BezelMetrics.WeatherGap else 0.dp),
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
private fun BezelDate(
    nowMillis: Long,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val now = rememberZonedDateTime(nowMillis)
    val weekday = now.format(rememberFormatter(R.string.aod_bezel_weekday_pattern)).uppercase()
    val day = now.format(rememberFormatter(R.string.aod_bezel_day_pattern))

    Row(modifier = modifier) {
        BezelDateText(text = weekday, color = color)

        BezelDateText(text = day, color = color, modifier = Modifier.padding(start = BezelMetrics.DateGap))
    }
}

@Composable
private fun BezelDateText(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = BezelMetrics.DateSize,
        fontWeight = FontWeight.Bold,
        fontFamily = BezelThemeSpec.Font,
        maxLines = 1,
        softWrap = false,
    )
}

@Composable
private fun BezelStatus(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    AodStatusLine(
        battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
        isFlashlightOn = state.isFlashlightOn,
        notifications = state.notifications,
        color = state.appearance.theme.config[BezelThemeSpec.StatusColor],
        glyphHeight = BezelMetrics.StatusGlyph,
        fontFamily = BezelThemeSpec.Font,
        modifier = modifier,
    )
}

// Đặt khối theo điểm neo trong cả vùng: x là mép trái (anchor 0), giữa (0,5) hay mép phải (1) của khối, centerY là giữa khối theo chiều dọc.
private fun Modifier.anchorAt(x: Dp, centerY: Dp, anchor: Float): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
    layout(width = constraints.maxWidth, height = constraints.maxHeight) {
        placeable.place(
            x = (x.toPx() - placeable.width * anchor).roundToInt(),
            y = (centerY.toPx() - placeable.height / 2f).roundToInt(),
        )
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodBezelPortraitPreview() {
    AodBezelPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.BEZEL)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodBezelLandscapePreview() {
    AodBezelLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.BEZEL)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
        ),
        onIntent = {},
    )
}
