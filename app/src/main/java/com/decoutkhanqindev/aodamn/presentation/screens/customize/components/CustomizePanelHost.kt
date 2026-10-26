package com.decoutkhanqindev.aodamn.presentation.screens.customize.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.aodamn.presentation.components.editor.AppEditorPanel
import com.decoutkhanqindev.aodamn.presentation.components.editor.AppEditorResetButton
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.classic.ClassicThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.isDefault
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.aodamn.presentation.model.customize.CustomizeTabValue
import com.decoutkhanqindev.aodamn.presentation.screens.customize.state.CustomizeDecorState
import com.decoutkhanqindev.aodamn.presentation.screens.customize.state.CustomizeIntent

@Composable
fun CustomizePanelHost(
    selectedTab: CustomizeTabValue?,
    isFirstOpen: Boolean,
    draft: CustomizeDraftUiModel,
    decor: CustomizeDecorState,
    onIntent: (CustomizeIntent) -> Unit,
) {
    AppEditorPanel(
        selected = selectedTab,
        title = { tab -> stringResource(tab.labelRes) },
        initial = CustomizeTabValue.DATE,
        action = { tab ->
            StyleResetAction(
                tab = tab,
                isFirstOpen = isFirstOpen,
                clock = draft.appearance.clock,
                onIntent = onIntent,
            )
        },
    ) { tab ->
        CustomizePanel(
            tab = tab,
            isFirstOpen = isFirstOpen,
            draft = draft,
            decor = decor,
            onIntent = onIntent,
        )
    }
}

@Composable
private fun StyleResetAction(
    tab: CustomizeTabValue,
    isFirstOpen: Boolean,
    clock: AodAppearanceUiModel,
    onIntent: (CustomizeIntent) -> Unit,
) {
    val component = tab.component
    val ownBlock = component?.ownBlock

    when {
        tab == CustomizeTabValue.CLOCK && isFirstOpen -> AppEditorResetButton(
            isVisible = !ClassicThemeSpec.ClockTab.isDefault(clock.theme.config),
            onClick = { onIntent(CustomizeIntent.Appearance.ResetClockStyle) },
        )

        component != null && (ownBlock == null || ownBlock !in clock.theme.value.spec.ownBlocks) -> AppEditorResetButton(
            isVisible = !AodComponentSpec.optionsOf(component).tab.isDefault(clock.components.config),
            onClick = { onIntent(CustomizeIntent.Appearance.ResetComponentStyle(component)) },
        )
    }
}
