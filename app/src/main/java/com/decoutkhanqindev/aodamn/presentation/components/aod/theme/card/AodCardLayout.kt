package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.card

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
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodSpreadMetrics
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodWeather
import com.decoutkhanqindev.aodamn.presentation.components.aod.bottomPanelBlocks
import com.decoutkhanqindev.aodamn.presentation.components.aod.landscapePlan
import com.decoutkhanqindev.aodamn.presentation.components.aod.topPanelBlocks
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.card.CardThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

// Thẻ, màn dọc giãn trên - dưới (AodSpreadMetrics, mock đã duyệt): thẻ → ngày → các khối thêm (thời tiết, sự kiện, ghi nhớ, nét vẽ) ở trên;
// thông báo mới nhất, nhạc rồi pin + icon thông báo neo đáy. Kích thước theo R (CardMetrics); nhiều khối thì R nhỏ lại cho hai cụm vừa phần
// giữa TOP_RATIO và BOTTOM_RATIO.
@Composable
fun AodCardPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val spec = state.appearance.theme.value.spec
    val topBlocks = remember(spec) { spec.topPanelBlocks() }
    val bottomBlocks = remember(spec) { spec.bottomPanelBlocks() }
    val config = state.appearance.theme.config
    val glyphs = rememberCardGlyphs()

    BoxWithConstraints {
        val unit = CardMetrics.unit(maxWidth, maxHeight, config[CardThemeSpec.Size] / 100f)
        val room = maxHeight * AodSpreadMetrics.ROOM_RATIO

        CardSpread(
            extras = { AodPanelColumn(blocks = topBlocks, state = state, onIntent = onIntent) },
            footer = { AodPanelColumn(blocks = bottomBlocks, state = state, onIntent = onIntent) },
            unitFor = { blocksHeight ->
                ((room - blocksHeight) / CardMetrics.COLUMN_HEIGHT).coerceIn(unit * CardMetrics.MIN_SCALE, unit)
            },
            head = { r -> CardClockAndDate(state = state, glyphs = glyphs, unit = r, isWeatherShown = false) },
            status = { r -> CardStatus(state = state, unit = r) },
        )
    }
}

// Thẻ, màn ngang: bố cục chung theo các khối đang hiện (AodLandscapeLayout), hero là thẻ + ngày (thời tiết cạnh ngày khi không có sự kiện) +
// dòng pin; thẻ thu còn 0,9 như app mẫu và không cao quá màn hình. Khối bật / tắt hay nhạc phát / dừng thì đổi bố cục bằng mờ chéo.
@Composable
fun AodCardLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val config = state.appearance.theme.config
    val glyphs = rememberCardGlyphs()

    BoxWithConstraints {
        val unit = minOf(
            CardMetrics.unit(maxWidth, maxHeight, config[CardThemeSpec.Size] / 100f) * CardMetrics.LANDSCAPE_SCALE,
            maxHeight * CardMetrics.HEIGHT_FILL / CardMetrics.LANDSCAPE_COLUMN_HEIGHT,
        )

        Crossfade(targetState = state.landscapePlan(), label = "AodCardLandscape") { plan ->
            AodLandscapeLayout(plan = plan, state = state, onIntent = onIntent) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CardClockAndDate(state = state, glyphs = glyphs, unit = unit, isWeatherShown = plan.isWeatherBesideDate)

                    CardStatus(
                        state = state,
                        unit = unit,
                        modifier = Modifier.padding(top = unit * CardMetrics.LANDSCAPE_STATUS_TOP),
                    )
                }
            }
        }
    }
}

// Đo khối thêm và cụm đáy trước để biết R (unitFor), rồi xếp thẻ + ngày → khối thêm từ trên, khối cụm đáy → dòng trạng thái thẳng đáy
// (AodSpreadMetrics), mỗi phần giữa bề ngang. Khối đổi cỡ có animation nên thẻ co giãn theo chứ không nhảy; phần nào không có thì bỏ luôn
// khoảng cách cạnh nó.
@Composable
private fun CardSpread(
    extras: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    unitFor: (blocksHeight: Dp) -> Dp,
    head: @Composable (unit: Dp) -> Unit,
    status: @Composable (unit: Dp) -> Unit,
) {
    SubcomposeLayout(modifier = Modifier.fillMaxSize()) { constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val extrasPlaceables = subcompose("extras", extras).map { measurable -> measurable.measure(loose) }
        val footerPlaceables = subcompose("footer", footer).map { measurable -> measurable.measure(loose) }
        val extrasHeight = extrasPlaceables.sumOf { placeable -> placeable.height }
        val footerHeight = footerPlaceables.sumOf { placeable -> placeable.height }
        val unit = unitFor((extrasHeight + footerHeight).toDp())
        val headPlaceables = subcompose("head") { head(unit) }.map { measurable -> measurable.measure(loose) }
        val statusPlaceables = subcompose("status") { status(unit) }.map { measurable -> measurable.measure(loose) }
        val statusHeight = statusPlaceables.sumOf { placeable -> placeable.height }
        val blocksTop = if (extrasHeight > 0) (unit * CardMetrics.BLOCKS_TOP).roundToPx() else 0
        val statusTop = if (footerHeight > 0 && statusHeight > 0) (unit * CardMetrics.STATUS_TOP).roundToPx() else 0
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val bottomY = AodSpreadMetrics.bottomY(
            height = height,
            topHeight = headPlaceables.sumOf { placeable -> placeable.height } + blocksTop + extrasHeight,
            bottomHeight = footerHeight + statusTop + statusHeight,
            minGap = (unit * CardMetrics.STATUS_TOP).roundToPx(),
        )

        layout(width = width, height = height) {
            var y = AodSpreadMetrics.topY(height)
            headPlaceables.forEach { placeable ->
                placeable.place(x = (width - placeable.width) / 2, y = y)
                y += placeable.height
            }
            y += blocksTop
            extrasPlaceables.forEach { placeable ->
                placeable.place(x = (width - placeable.width) / 2, y = y)
                y += placeable.height
            }
            y = bottomY
            footerPlaceables.forEach { placeable ->
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
private fun CardClockAndDate(
    state: AodState,
    glyphs: CardGlyphs,
    unit: Dp,
    isWeatherShown: Boolean,
) {
    val config = state.appearance.theme.config
    val components = state.appearance.components

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AodCardClock(nowMillis = state.nowMillis, config = config, glyphs = glyphs, unit = unit)

        Row(
            modifier = Modifier.padding(top = unit * CardMetrics.DATE_TOP),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = state.extras.isDateEnabled) {
                CardDate(
                    nowMillis = state.nowMillis,
                    brush = config.brushOf(CardThemeSpec.DateColor),
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
private fun CardStatus(
    state: AodState,
    unit: Dp,
    modifier: Modifier = Modifier,
) {
    AodStatusLine(
        battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
        isFlashlightOn = state.isFlashlightOn,
        notifications = state.notifications,
        color = state.appearance.theme.config[CardThemeSpec.StatusColor],
        brush = state.appearance.theme.config.gradientOf(CardThemeSpec.StatusColor)?.toBrush(),
        glyphHeight = unit * CardMetrics.STATUS_GLYPH,
        fontFamily = CardThemeSpec.Font,
        modifier = modifier,
        gapScale = CardMetrics.STATUS_GAP_SCALE,
    )
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodCardPortraitPreview() {
    AodCardPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.CARD)),
            battery = BatteryUiModel(percent = 42, isCharging = true),
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodCardLandscapePreview() {
    AodCardLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.CARD)),
            battery = BatteryUiModel(percent = 42, isCharging = true),
        ),
        onIntent = {},
    )
}
