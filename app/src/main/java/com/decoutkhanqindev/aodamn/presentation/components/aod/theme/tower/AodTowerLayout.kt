package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.tower

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodLandscapeLayout
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelColumn
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.landscapePlan
import com.decoutkhanqindev.aodamn.presentation.components.aod.panelBlocks
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tower.TowerThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import kotlinx.collections.immutable.toImmutableList

// Tháp, màn dọc như layout app mẫu: hàng số sát đỉnh màn hình (TowerMetrics.TOP_RATIO) → ngày (thời tiết cạnh ngày khi không có sự kiện); khoảng
// còn lại chia 0,8 / 0,2, các khối thêm (thời tiết, sự kiện, ghi nhớ) → nhạc → pin + icon thông báo neo đáy phần 0,8. Nhiều khối không vừa thì
// hàng số nhỏ lại (đo khối thêm trước rồi mới chọn R).
@Composable
fun AodTowerPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val spec = state.appearance.theme.value.spec
    val isWeatherBesideDate = state.landscapePlan().isWeatherBesideDate
    val blocks = remember(spec, isWeatherBesideDate) {
        spec.panelBlocks()
            .filter { block -> !(block == AodPanelBlockValue.WEATHER && isWeatherBesideDate) }
            .toImmutableList()
    }
    val config = state.appearance.theme.config
    val glyphs = rememberTowerGlyphs()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val height = maxHeight
        val unit = minOf(
            TowerMetrics.unit(maxWidth, maxHeight, config[TowerThemeSpec.Size] / 100f),
            maxWidth * TowerMetrics.MAX_WIDTH_RATIO / TowerMetrics.CLOCK_WIDTH,
        )
        val room = height * (1f - TowerMetrics.TOP_RATIO) * TowerMetrics.BOTTOM_WEIGHT

        TowerPortraitColumn(
            top = height * TowerMetrics.TOP_RATIO,
            extras = { AodPanelColumn(blocks = blocks, state = state, onIntent = onIntent) },
            unitFor = { extrasHeight ->
                ((room - extrasHeight) / (TowerMetrics.BOTTOM_FIXED + TowerMetrics.BOTTOM_WEIGHT * TowerMetrics.HEAD_HEIGHT))
                    .coerceIn(unit * TowerMetrics.MIN_SCALE, unit)
            },
            head = { r ->
                TowerClockAndDate(
                    state = state,
                    glyphs = glyphs,
                    unit = r,
                    dateGap = r * TowerMetrics.DATE_GAP,
                    dateFont = r * TowerMetrics.DATE_FONT,
                    isWeatherShown = isWeatherBesideDate,
                )
            },
            status = { r -> TowerStatus(state = state, glyphHeight = r * TowerMetrics.STATUS_GLYPH) },
        )
    }
}

// Tháp, màn ngang: bố cục chung theo các khối đang hiện (AodLandscapeLayout), hero là hàng số + ngày (thời tiết cạnh ngày khi không có sự kiện)
// + dòng pin; R theo min(0,3 cạnh dài, cạnh ngắn) như app mẫu và không cao quá màn hình. Khối bật / tắt hay nhạc phát / dừng thì đổi bố cục bằng
// mờ chéo.
@Composable
fun AodTowerLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val config = state.appearance.theme.config
    val glyphs = rememberTowerGlyphs()

    BoxWithConstraints {
        val unit = minOf(
            TowerMetrics.landscapeUnit(maxWidth, maxHeight, config[TowerThemeSpec.Size] / 100f),
            maxHeight * TowerMetrics.LANDSCAPE_HEIGHT_FILL / TowerMetrics.LANDSCAPE_COLUMN_HEIGHT,
        )

        Crossfade(targetState = state.landscapePlan(), label = "AodTowerLandscape") { plan ->
            AodLandscapeLayout(plan = plan, state = state, onIntent = onIntent) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TowerClockAndDate(
                        state = state,
                        glyphs = glyphs,
                        unit = unit,
                        dateGap = unit * TowerMetrics.LANDSCAPE_DATE_GAP,
                        dateFont = unit * TowerMetrics.LANDSCAPE_DATE_FONT,
                        isWeatherShown = plan.isWeatherBesideDate,
                    )

                    TowerStatus(
                        state = state,
                        glyphHeight = unit * TowerMetrics.LANDSCAPE_STATUS_GLYPH,
                        modifier = Modifier.padding(top = unit * TowerMetrics.LANDSCAPE_STATUS_TOP),
                    )
                }
            }
        }
    }
}

// Đo khối thêm trước để biết R (unitFor), rồi đặt hàng số + ngày cách đỉnh top; khối thêm + dòng trạng thái neo đáy phần BOTTOM_WEIGHT của khoảng
// dưới ngày (không lấn lên ngày). Khối thêm đổi cỡ có animation nên hàng số co giãn theo chứ không nhảy; phần nào không có thì bỏ khoảng cách
// phía trên nó.
@Composable
private fun TowerPortraitColumn(
    top: Dp,
    extras: @Composable () -> Unit,
    unitFor: (extrasHeight: Dp) -> Dp,
    head: @Composable (unit: Dp) -> Unit,
    status: @Composable (unit: Dp) -> Unit,
) {
    SubcomposeLayout(modifier = Modifier.fillMaxSize()) { constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val extrasPlaceables = subcompose("extras", extras).map { measurable -> measurable.measure(loose) }
        val extrasHeight = extrasPlaceables.sumOf { placeable -> placeable.height }
        val unit = unitFor(extrasHeight.toDp())
        val headPlaceables = subcompose("head") { head(unit) }.map { measurable -> measurable.measure(loose) }
        val statusPlaceables = subcompose("status") { status(unit) }.map { measurable -> measurable.measure(loose) }
        val statusHeight = statusPlaceables.sumOf { placeable -> placeable.height }
        val statusTop = if (extrasHeight > 0 && statusHeight > 0) (unit * TowerMetrics.STATUS_TOP).roundToPx() else 0
        val blocksTop = (unit * TowerMetrics.BLOCKS_TOP).roundToPx()
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val headTop = top.roundToPx()
        val headBottom = headTop + headPlaceables.sumOf { placeable -> placeable.height }
        val groupHeight = extrasHeight + statusTop + statusHeight
        val groupBottom = (headBottom + (height - headBottom) * TowerMetrics.BOTTOM_WEIGHT).toInt()
            .coerceAtLeast(headBottom + blocksTop + groupHeight)
            .coerceAtMost(height)

        layout(width = width, height = height) {
            var y = headTop
            headPlaceables.forEach { placeable ->
                placeable.place(x = (width - placeable.width) / 2, y = y)
                y += placeable.height
            }
            y = groupBottom - groupHeight
            extrasPlaceables.forEach { placeable ->
                placeable.place(x = (width - placeable.width) / 2, y = y)
                y += placeable.height
            }
            y += statusTop
            statusPlaceables.forEach { placeable ->
                placeable.place(x = (width - placeable.width) / 2, y = y)
                y += placeable.height
            }
        }
    }
}

@Composable
private fun TowerClockAndDate(
    state: AodState,
    glyphs: TowerGlyphs,
    unit: Dp,
    dateGap: Dp,
    dateFont: Dp,
    isWeatherShown: Boolean,
) {
    val config = state.appearance.theme.config
    val components = state.appearance.components

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AodTowerClock(nowMillis = state.nowMillis, config = config, glyphs = glyphs, unit = unit)

        Row(
            modifier = Modifier.padding(top = dateGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = state.extras.isDateEnabled) {
                TowerDate(
                    nowMillis = state.nowMillis,
                    color = config[TowerThemeSpec.DateColor],
                    fontSize = dateFont,
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
private fun TowerStatus(
    state: AodState,
    glyphHeight: Dp,
    modifier: Modifier = Modifier,
) {
    AodStatusLine(
        battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
        isFlashlightOn = state.isFlashlightOn,
        notifications = state.notifications,
        color = state.appearance.theme.config[TowerThemeSpec.StatusColor],
        glyphHeight = glyphHeight,
        fontFamily = TowerThemeSpec.Font,
        modifier = modifier,
    )
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodTowerPortraitPreview() {
    AodTowerPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.TOWER)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodTowerLandscapePreview() {
    AodTowerLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.TOWER)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
        ),
        onIntent = {},
    )
}
