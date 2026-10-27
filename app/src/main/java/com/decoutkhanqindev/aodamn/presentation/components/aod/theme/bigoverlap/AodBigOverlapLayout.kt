package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bigoverlap

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.AodHeroInfoLandscape
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.AodHeroInfoPortrait
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.BatteryUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap.BigOverlapThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodIntent
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

@Composable
fun AodBigOverlapPortrait(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    AodHeroInfoPortrait(state = state, onIntent = onIntent, style = BigOverlapMetrics.HeroInfo) { modifier ->
        BigOverlapClock(state = state, modifier = modifier)
    }
}

@Composable
fun AodBigOverlapLandscape(
    state: AodState,
    onIntent: (AodIntent) -> Unit,
) {
    AodHeroInfoLandscape(
        state = state,
        onIntent = onIntent,
        style = BigOverlapMetrics.HeroInfo,
        heroHalfWidth = { maxWidth, maxHeight ->
            val scale = state.appearance.theme.config[BigOverlapThemeSpec.Size] / 100f
            val textSize = minOf(maxWidth, maxHeight) * BigOverlapMetrics.TEXT_SIZE * scale
            textSize * BigOverlapMetrics.ZERO_WIDTH * (1f - BigOverlapMetrics.OVERLAP_X_FILLED)
        },
    ) { modifier ->
        BigOverlapClock(state = state, modifier = modifier)
    }
}

@Composable
private fun BigOverlapClock(
    state: AodState,
    modifier: Modifier = Modifier,
) {
    AodBigOverlapClock(
        nowMillis = state.nowMillis,
        config = state.appearance.theme.config,
        modifier = modifier,
    )
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodBigOverlapPortraitPreview() {
    AodBigOverlapPortrait(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.BIG_OVERLAP)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}

@Preview(widthDp = 873, heightDp = 393)
@Composable
private fun AodBigOverlapLandscapePreview() {
    AodBigOverlapLandscape(
        state = AodState(
            nowMillis = System.currentTimeMillis(),
            appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = AodThemeValue.BIG_OVERLAP)),
            battery = BatteryUiModel(percent = 72, isCharging = false),
            isPreview = true,
        ),
        onIntent = {},
    )
}
