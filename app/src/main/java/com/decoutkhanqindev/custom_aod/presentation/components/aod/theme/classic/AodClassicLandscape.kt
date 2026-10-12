package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.classic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.presentation.components.aod.AodDetails
import com.decoutkhanqindev.custom_aod.presentation.components.aod.block.AodDate
import com.decoutkhanqindev.custom_aod.presentation.components.aod.hasDetails
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey3A

// Màn ngang của Cổ điển: hai cột ngăn bằng vạch mảnh, trái là đồng hồ và dòng ngày, phải là các khối thông tin còn lại. Không có khối nào
// đang hiện thì chỉ còn cột đồng hồ giữa màn hình.
@Composable
fun AodClassicLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    val components = state.appearance.components

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AodClassicClock(nowMillis = state.nowMillis, config = state.appearance.theme.config)

            AnimatedVisibility(visible = state.extras.isDateEnabled) {
                AodDate(
                    nowMillis = state.nowMillis,
                    style = components.styleOf(AodComponentValue.DATE),
                    layout = components.dateLayout,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }

        AnimatedVisibility(visible = state.hasDetails(isDateShown = false)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 36.dp)
                        .size(width = 1.dp, height = 140.dp)
                        .background(Grey3A),
                )

                AodDetails(state = state, onIntent = onIntent, isDateShown = false)
            }
        }
    }
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodClassicLandscapePreview() {
    AodClassicLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            battery = BatteryUiModel(percent = 72, isCharging = false),
        ),
        onIntent = {},
    )
}
