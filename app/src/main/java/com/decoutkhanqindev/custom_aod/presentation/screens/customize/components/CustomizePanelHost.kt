package com.decoutkhanqindev.custom_aod.presentation.screens.customize.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.custom_aod.presentation.components.AppEditorPanel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeTabValue
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeDecorState
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeIntent

@Composable
fun CustomizePanelHost(
    selectedTab: CustomizeTabValue?,
    draft: CustomizeDraftUiModel,
    decor: CustomizeDecorState,
    onIntent: (CustomizeIntent) -> Unit,
) {
    AppEditorPanel(
        selected = selectedTab,
        title = { tab -> stringResource(tab.labelRes) },
        initial = CustomizeTabValue.DATE,
    ) { tab ->
        CustomizePanel(
            tab = tab,
            draft = draft,
            decor = decor,
            onIntent = onIntent,
        )
    }
}
