package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.capsule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodLandscapeLayout
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelColumn
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPortraitSpread
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.bottomPanelBlocks
import com.decoutkhanqindev.aodamn.presentation.components.aod.landscapePlan
import com.decoutkhanqindev.aodamn.presentation.components.aod.topPanelBlocks
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.capsule.CapsuleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

// Viên nang, màn dọc giãn trên - dưới (AodPortraitSpread, mock đã duyệt): viên nang → ngày → các khối thêm (thời tiết, sự kiện, ghi nhớ, nét
// vẽ) ở trên; thông báo mới nhất, nhạc rồi pin + icon thông báo neo đáy. Kích thước theo R (CapsuleMetrics), khối thêm theo kiểu user chọn ở
// Tùy chỉnh.
@Composable
fun AodCapsulePortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val spec = state.appearance.theme.value.spec
    val topBlocks = remember(spec) { spec.topPanelBlocks() }
    val bottomBlocks = remember(spec) { spec.bottomPanelBlocks() }
    val config = state.appearance.theme.config

    BoxWithConstraints {
        val unit = CapsuleMetrics.unit(maxWidth, maxHeight, config[CapsuleThemeSpec.Size] / 100f)

        AodPortraitSpread(
            top = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CapsuleClockAndDate(state = state, unit = unit, isWeatherShown = false)

                    AodPanelColumn(
                        blocks = topBlocks,
                        state = state,
                        onIntent = onIntent,
                        modifier = Modifier.padding(top = unit * CapsuleMetrics.BLOCKS_TOP),
                    )
                }
            },
            bottom = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AodPanelColumn(blocks = bottomBlocks, state = state, onIntent = onIntent)

                    CapsuleStatus(
                        state = state,
                        unit = unit,
                        modifier = Modifier.padding(top = unit * CapsuleMetrics.STATUS_TOP),
                    )
                }
            },
            minGap = unit * CapsuleMetrics.STATUS_TOP,
        )
    }
}

// Viên nang, màn ngang: bố cục chung theo các khối đang hiện (AodLandscapeLayout), hero là viên nang + ngày (thời tiết cạnh ngày khi không có
// sự kiện) + dòng pin; viên nang cỡ như màn dọc. Khối bật / tắt hay nhạc phát / dừng thì đổi bố cục bằng mờ chéo.
@Composable
fun AodCapsuleLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val config = state.appearance.theme.config

    BoxWithConstraints {
        val unit = CapsuleMetrics.unit(maxWidth, maxHeight, config[CapsuleThemeSpec.Size] / 100f)

        Crossfade(targetState = state.landscapePlan(), label = "AodCapsuleLandscape") { plan ->
            AodLandscapeLayout(plan = plan, state = state, onIntent = onIntent) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CapsuleClockAndDate(state = state, unit = unit, isWeatherShown = plan.isWeatherBesideDate)

                    CapsuleStatus(
                        state = state,
                        unit = unit,
                        modifier = Modifier.padding(top = unit * CapsuleMetrics.LANDSCAPE_STATUS_TOP),
                    )
                }
            }
        }
    }
}

@Composable
private fun CapsuleClockAndDate(
    state: AodState,
    unit: Dp,
    isWeatherShown: Boolean,
) {
    val config = state.appearance.theme.config
    val components = state.appearance.components

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AodCapsuleClock(nowMillis = state.nowMillis, config = config, unit = unit)

        Row(
            modifier = Modifier.padding(top = unit * CapsuleMetrics.DATE_TOP),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = state.extras.isDateEnabled) {
                CapsuleDate(
                    nowMillis = state.nowMillis,
                    brush = config.brushOf(CapsuleThemeSpec.DateColor),
                    unit = unit,
                    modifier = Modifier.padding(end = if (isWeatherShown) 12.dp else 0.dp),
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
}

@Composable
private fun CapsuleStatus(
    state: AodState,
    unit: Dp,
    modifier: Modifier = Modifier,
) {
    AodStatusLine(
        battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
        isFlashlightOn = state.isFlashlightOn,
        notifications = state.notifications,
        color = state.appearance.theme.config[CapsuleThemeSpec.StatusColor],
        brush = state.appearance.theme.config.gradientOf(CapsuleThemeSpec.StatusColor)?.toBrush(),
        glyphHeight = unit * CapsuleMetrics.STATUS_GLYPH,
        fontFamily = CapsuleThemeSpec.Font,
        modifier = modifier,
        gapScale = CapsuleMetrics.STATUS_GAP_SCALE,
    )
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodCapsulePortraitPreview() {
    AodCapsulePortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.CAPSULE)),
            battery = BatteryUiModel(percent = 42, isCharging = true),
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodCapsuleLandscapePreview() {
    AodCapsuleLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.CAPSULE)),
            battery = BatteryUiModel(percent = 42, isCharging = true),
        ),
        onIntent = {},
    )
}
