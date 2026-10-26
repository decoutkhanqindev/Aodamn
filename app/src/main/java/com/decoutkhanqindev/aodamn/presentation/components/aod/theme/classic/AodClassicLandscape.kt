package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.classic

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodHeroDetails
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodLandscapeLayout
import com.decoutkhanqindev.aodamn.presentation.components.aod.landscapePlan
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodClassicLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    Crossfade(targetState = state.landscapePlan(), label = "AodClassicLandscape") { plan ->
        AodLandscapeLayout(plan = plan, state = state, onIntent = onIntent) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AodClassicClock(nowMillis = state.nowMillis, config = state.appearance.theme.config)

                AodHeroDetails(state = state, isWeatherShown = plan.isWeatherBesideDate)
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
