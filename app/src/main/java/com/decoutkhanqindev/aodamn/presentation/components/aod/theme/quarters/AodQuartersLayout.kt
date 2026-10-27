package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.quarters

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.AodHeroInfoLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.AodHeroInfoPortrait
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.quarters.QuartersThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodQuartersPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    AodHeroInfoPortrait(state = state, onIntent = onIntent, style = QuartersMetrics.HeroInfo) { modifier ->
        QuartersDial(state = state, modifier = modifier)
    }
}

@Composable
fun AodQuartersLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    AodHeroInfoLandscape(
        state = state,
        onIntent = onIntent,
        style = QuartersMetrics.HeroInfo,
        heroHalfWidth = { maxWidth, maxHeight ->
            minOf(maxWidth, maxHeight) / 2 * (state.appearance.theme.config[QuartersThemeSpec.Size] / 100f)
        },
    ) { modifier ->
        QuartersDial(state = state, modifier = modifier)
    }
}

@Composable
private fun QuartersDial(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    AodQuartersDial(
        nowMillis = state.nowMillis,
        isLive = state.isClockLive,
        config = state.appearance.theme.config,
        modifier = modifier,
    )
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodQuartersPortraitPreview() {
    AodQuartersPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.QUARTERS)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodQuartersLandscapePreview() {
    AodQuartersLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.QUARTERS)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
