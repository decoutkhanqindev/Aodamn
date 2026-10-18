package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.jersey

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPanelColumn
import com.decoutkhanqindev.aodamn.presentation.components.aod.block.AodStatusLine
import com.decoutkhanqindev.aodamn.presentation.components.aod.panelBlocks
import com.decoutkhanqindev.aodamn.presentation.components.aod.shownPanelBlocks
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.jersey.JerseyThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

// Số áo, màn dọc: hai hàng chữ số rất cao chỉ có nét viền nằm giữa màn hình, ngày treo bên phải hàng giờ, dòng pin + icon thông báo ở dưới,
// rồi các khối thêm (thời tiết, sự kiện, ghi nhớ, nét vẽ, nhạc) theo kiểu user chọn ở Tùy chỉnh. Ngày và dòng trạng thái không ảnh hưởng vị
// trí số (hàng số luôn giữa màn hình, ô cố định nên giờ nào cũng vậy). Kích thước theo R như ảnh mẫu; có khối thêm thì hàng số nhỏ lại cho cả
// cột vừa màn hình, ngày và dòng trạng thái giữ cỡ.
@Composable
fun AodJerseyPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val spec = state.appearance.theme.value.spec
    val blocks = remember(spec) { spec.panelBlocks() }
    val scale = state.appearance.theme.config[JerseyThemeSpec.Size] / 100f

    BoxWithConstraints {
        val unit = minOf(maxHeight * JerseyMetrics.ROW_HEIGHT_RATIO, maxWidth * JerseyMetrics.MAX_WIDTH_RATIO) * scale
        val fill = maxHeight * JerseyMetrics.PORTRAIT_HEIGHT_FILL - unit * JerseyMetrics.STATUS_GLYPH_HEIGHT
        val screenWidth = maxWidth

        JerseyWithExtras(
            extras = { AodPanelColumn(blocks = blocks, state = state, onIntent = onIntent) },
        ) { extrasHeight ->
            val rowHeight = ((fill - extrasHeight) / (2 + JerseyMetrics.ROW_GAP + JerseyMetrics.STATUS_TOP))
                .coerceIn(unit * JerseyMetrics.MIN_ROW_RATIO, unit)

            JerseyPortraitClock(state = state, rowHeight = rowHeight, unit = unit, width = screenWidth)
        }
    }
}

// Số áo, màn ngang: một cột giữa màn hình, ngày nằm ngang → bốn chữ số một hàng → pin + icon thông báo → các khối thêm xếp cạnh nhau (xuống
// dòng khi không đủ bề ngang). Ngày và dòng trạng thái cỡ như màn dọc; hàng số lấp phần chiều cao còn lại (tắt ngày thì số to ra).
@Composable
fun AodJerseyLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val blocks = state.shownPanelBlocks()
    val scale = state.appearance.theme.config[JerseyThemeSpec.Size] / 100f

    BoxWithConstraints {
        val unit = maxOf(maxWidth, maxHeight) * JerseyMetrics.ROW_HEIGHT_RATIO * scale
        val dateHeight = if (state.extras.isDateEnabled) unit * (JerseyMetrics.DATE_WIDTH + JerseyMetrics.DATE_GAP) else 0.dp
        val fill = maxHeight * JerseyMetrics.LANDSCAPE_HEIGHT_FILL - dateHeight -
            unit * (JerseyMetrics.LANDSCAPE_STATUS_TOP + JerseyMetrics.STATUS_GLYPH_HEIGHT)
        val maxRow = minOf(maxHeight * JerseyMetrics.LANDSCAPE_ROW_HEIGHT_RATIO * scale, maxWidth * JerseyMetrics.LANDSCAPE_MAX_WIDTH_RATIO)
        val minRow = minOf(maxHeight * JerseyMetrics.LANDSCAPE_MIN_ROW_RATIO, maxRow)
        val screenWidth = maxWidth

        JerseyWithExtras(
            extras = {
                FlowRow(
                    modifier = Modifier
                        .widthIn(max = screenWidth - 48.dp)
                        .animateContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                    itemVerticalAlignment = Alignment.CenterVertically,
                ) {
                    blocks.forEach { block ->
                        key(block) {
                            AodPanelBlock(block = block, state = state, onIntent = onIntent)
                        }
                    }
                }
            },
        ) { extrasHeight ->
            JerseyLandscapeClock(
                state = state,
                rowHeight = (fill - extrasHeight).coerceIn(minRow, maxRow),
                unit = unit,
                width = screenWidth,
            )
        }
    }
}

// Cụm số ở trên, khối thêm ở dưới, cả hai nằm giữa bề ngang. Đo khối thêm trước để biết còn bao nhiêu chiều cao cho hàng số; khối thêm đổi
// cỡ có animation nên hàng số co giãn theo chứ không nhảy.
@Composable
private fun JerseyWithExtras(
    extras: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    clock: @Composable (extrasHeight: Dp) -> Unit,
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val extrasPlaceables = subcompose("extras", extras).map { measurable -> measurable.measure(loose) }
        val extrasHeight = extrasPlaceables.sumOf { placeable -> placeable.height }
        val clockPlaceables = subcompose("clock") { clock(extrasHeight.toDp()) }.map { measurable -> measurable.measure(loose) }
        val width = constraints.maxWidth

        layout(width = width, height = clockPlaceables.sumOf { placeable -> placeable.height } + extrasHeight) {
            var y = 0
            (clockPlaceables + extrasPlaceables).forEach { placeable ->
                placeable.place(x = (width - placeable.width) / 2, y = y)
                y += placeable.height
            }
        }
    }
}

@Composable
private fun JerseyPortraitClock(
    state: AodState,
    rowHeight: Dp,
    unit: Dp,
    width: Dp,
) {
    val config = state.appearance.theme.config
    val glyphs = rememberJerseyGlyphs()

    Layout(
        content = {
            JerseyTimeRow(nowMillis = state.nowMillis, isHour = true, glyphs = glyphs, rowHeight = rowHeight, color = config[JerseyThemeSpec.HourColor])

            JerseyTimeRow(nowMillis = state.nowMillis, isHour = false, glyphs = glyphs, rowHeight = rowHeight, color = config[JerseyThemeSpec.MinuteColor])

            // Bọc Box: khối ẩn hẳn (AnimatedVisibility) thì không có node, Layout đọc con theo thứ tự nên luôn cần đủ 4 con.
            Box {
                AnimatedVisibility(visible = state.extras.isDateEnabled, enter = fadeIn(), exit = fadeOut()) {
                    JerseyDateVertical(nowMillis = state.nowMillis, color = config[JerseyThemeSpec.DateColor], rowHeight = unit)
                }
            }

            Box {
                JerseyStatus(state = state, unit = unit)
            }
        },
        modifier = Modifier.width(width),
    ) { measurables, constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val hourRow = measurables[0].measure(loose)
        val minuteRow = measurables[1].measure(loose)
        val date = measurables[2].measure(loose)
        val status = measurables[3].measure(loose)
        val rowGap = (rowHeight * JerseyMetrics.ROW_GAP).roundToPx()
        val dateGap = (unit * JerseyMetrics.DATE_GAP).roundToPx()
        val statusGap = if (status.height > 0) (rowHeight * JerseyMetrics.STATUS_TOP).roundToPx() else 0
        val digitsLeft = (constraints.maxWidth - hourRow.width) / 2
        val minuteTop = hourRow.height + rowGap

        layout(width = constraints.maxWidth, height = minuteTop + minuteRow.height + statusGap + status.height) {
            hourRow.place(x = digitsLeft, y = 0)
            minuteRow.place(x = digitsLeft + (hourRow.width - minuteRow.width) / 2, y = minuteTop)
            date.place(x = digitsLeft + hourRow.width + dateGap, y = 0)
            status.place(x = digitsLeft + (hourRow.width - status.width) / 2, y = minuteTop + minuteRow.height + statusGap)
        }
    }
}

@Composable
private fun JerseyLandscapeClock(
    state: AodState,
    rowHeight: Dp,
    unit: Dp,
    width: Dp,
) {
    val config = state.appearance.theme.config
    val glyphs = rememberJerseyGlyphs()

    Layout(
        content = {
            JerseyTimeRow(nowMillis = state.nowMillis, isHour = true, glyphs = glyphs, rowHeight = rowHeight, color = config[JerseyThemeSpec.HourColor])

            JerseyTimeRow(nowMillis = state.nowMillis, isHour = false, glyphs = glyphs, rowHeight = rowHeight, color = config[JerseyThemeSpec.MinuteColor])

            // Bọc Box: khối ẩn hẳn (AnimatedVisibility) thì không có node, Layout đọc con theo thứ tự nên luôn cần đủ 4 con.
            Box {
                AnimatedVisibility(visible = state.extras.isDateEnabled) {
                    JerseyDateHorizontal(nowMillis = state.nowMillis, color = config[JerseyThemeSpec.DateColor], unit = unit)
                }
            }

            Box {
                JerseyStatus(state = state, unit = unit)
            }
        },
        modifier = Modifier.width(width),
    ) { measurables, constraints ->
        val loose = constraints.copy(minWidth = 0, minHeight = 0)
        val hourRow = measurables[0].measure(loose)
        val minuteRow = measurables[1].measure(loose)
        val date = measurables[2].measure(loose)
        val status = measurables[3].measure(loose)
        val pairGap = (rowHeight * JerseyMetrics.LANDSCAPE_PAIR_GAP).roundToPx()
        val digitsLeft = (constraints.maxWidth - hourRow.width - pairGap - minuteRow.width) / 2
        val digitsTop = if (date.height > 0) date.height + (unit * JerseyMetrics.DATE_GAP).roundToPx() else 0
        val statusTop = digitsTop + hourRow.height +
            if (status.height > 0) (unit * JerseyMetrics.LANDSCAPE_STATUS_TOP).roundToPx() else 0

        layout(width = constraints.maxWidth, height = statusTop + status.height) {
            date.place(x = (constraints.maxWidth - date.width) / 2, y = 0)
            hourRow.place(x = digitsLeft, y = digitsTop)
            minuteRow.place(x = digitsLeft + hourRow.width + pairGap, y = digitsTop)
            status.place(x = (constraints.maxWidth - status.width) / 2, y = statusTop)
        }
    }
}

// Hàng giờ hoặc phút; hàng giờ mang cả giờ cho TalkBack.
@Composable
private fun JerseyTimeRow(
    nowMillis: Long,
    isHour: Boolean,
    glyphs: JerseyGlyphs,
    rowHeight: Dp,
    color: Color,
) {
    val now = rememberZonedDateTime(nowMillis)
    val formatter = rememberFormatter(
        when {
            !isHour -> R.string.aod_minute_pattern
            is24HourFormat() -> R.string.aod_hour_pattern_24h
            else -> R.string.aod_hour_pattern_12h
        },
    )
    val description = now.format(rememberTimeFormatter())

    JerseyDigitRow(
        text = now.format(formatter),
        glyphs = glyphs,
        rowHeight = rowHeight,
        color = color,
        modifier = Modifier.clearAndSetSemantics { if (isHour) contentDescription = description },
    )
}

@Composable
private fun JerseyStatus(
    state: AodState,
    unit: Dp,
) {
    AodStatusLine(
        battery = state.battery?.takeIf { state.extras.isBatteryEnabled },
        isFlashlightOn = state.isFlashlightOn,
        notifications = state.notifications,
        color = state.appearance.theme.config[JerseyThemeSpec.StatusColor],
        glyphHeight = unit * JerseyMetrics.STATUS_GLYPH_HEIGHT,
        fontFamily = JerseyThemeSpec.Font,
    )
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodJerseyPortraitPreview() {
    AodJerseyPortrait(state = AodState(nowMillis = System.currentTimeMillis()), onIntent = {})
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodJerseyLandscapePreview() {
    AodJerseyLandscape(state = AodState(nowMillis = System.currentTimeMillis()), onIntent = {})
}
