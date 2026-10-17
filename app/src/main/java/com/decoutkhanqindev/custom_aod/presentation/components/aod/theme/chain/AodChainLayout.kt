package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.chain

import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.presentation.components.aod.AodPanelBlock
import com.decoutkhanqindev.custom_aod.presentation.components.aod.AodSplitRow
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodPanelBlockValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey3A

// Chuỗi, màn dọc như layout app mẫu: khoảng trống TOP_RATIO chiều cao màn hình → viên thuốc đồng hồ sát lề trái → chuỗi dòng (ngày, pin, thông
// báo, ghi nhớ, sự kiện); trình phát neo đáy.
@Composable
fun AodChainPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val glyphs = rememberChainGlyphs()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val top = maxHeight * ChainMetrics.TOP_RATIO

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(top))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ChainMetrics.SideMargin),
            ) {
                AodChainClock(nowMillis = state.nowMillis, config = state.appearance.theme.config, glyphs = glyphs)

                AodChainList(
                    state = state,
                    isStatusShown = true,
                    isExtrasShown = true,
                    modifier = Modifier.padding(top = ChainMetrics.ClockGap),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.padding(
                    start = ChainMetrics.PlayerMargin,
                    end = ChainMetrics.PlayerMargin,
                    bottom = ChainMetrics.PlayerBottom,
                ),
            ) {
                AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent)
            }
        }
    }
}

// Chuỗi, màn ngang (mock đã duyệt): cụm đồng hồ + chuỗi ngày, pin, thông báo; ghi nhớ, sự kiện là chuỗi riêng ở cột sau; nhạc ở cột cuối. Thứ
// tự cột trái sang phải: Chuỗi → ghi nhớ, sự kiện → nhạc, không dùng cột thông tin chung của AodLandscapeLayout để giữ dạng chuỗi như màn dọc.
// Khối bật / tắt hay nhạc phát / dừng thì đổi bố cục bằng mờ chéo.
@Composable
fun AodChainLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val glyphs = rememberChainGlyphs()
    val hasExtras = state.hasChainMemo() || state.hasChainEvents()
    val hasMedia = state.media != null && AodComponentValue.MEDIA in state.appearance.theme.value.spec.components

    Crossfade(targetState = hasExtras to hasMedia, label = "AodChainLandscape") { (isExtrasShown, isMediaShown) ->
        val hero: @Composable () -> Unit = { ChainHero(state = state, glyphs = glyphs) }
        val extras: @Composable () -> Unit = {
            AodChainList(
                state = state,
                isStatusShown = false,
                isExtrasShown = true,
                modifier = Modifier.widthIn(max = ChainMetrics.InfoMaxWidth),
            )
        }
        val media: @Composable () -> Unit = { AodPanelBlock(block = AodPanelBlockValue.MEDIA, state = state, onIntent = onIntent) }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when {
                isExtrasShown && isMediaShown -> ChainThreeColumns(start = hero, middle = extras, end = media)
                isExtrasShown -> AodSplitRow(start = hero, end = extras)
                isMediaShown -> AodSplitRow(start = hero, end = media)
                else -> hero()
            }
        }
    }
}

@Composable
private fun ChainHero(
    state: AodState,
    glyphs: ChainGlyphs,
) {
    Column {
        AodChainClock(nowMillis = state.nowMillis, config = state.appearance.theme.config, glyphs = glyphs)

        AodChainList(
            state = state,
            isStatusShown = true,
            isExtrasShown = false,
            modifier = Modifier.padding(top = ChainMetrics.ClockGap),
        )
    }
}

// Ba cột nối tiếp ngăn bằng vạch mảnh như AodSplitRow; cột cuối (nhạc) chỉ lấy chỗ còn lại.
@Composable
private fun ChainThreeColumns(
    start: @Composable () -> Unit,
    middle: @Composable () -> Unit,
    end: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.padding(horizontal = ChainMetrics.LandscapeMargin),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        start()

        ChainDivider()

        middle()

        ChainDivider()

        Box(modifier = Modifier.weight(1f, fill = false)) {
            end()
        }
    }
}

@Composable
private fun ChainDivider() {
    Box(
        modifier = Modifier
            .padding(horizontal = ChainMetrics.DividerGap)
            .size(width = 1.dp, height = ChainMetrics.DividerHeight)
            .background(Grey3A),
    )
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodChainPortraitPreview() {
    AodChainPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.CHAIN)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodChainLandscapePreview() {
    AodChainLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.CHAIN)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
        ),
        onIntent = {},
    )
}
