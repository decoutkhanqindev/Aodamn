package com.decoutkhanqindev.custom_aod.presentation.screens.customize.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.decoutkhanqindev.custom_aod.presentation.components.AppEditorPanel
import com.decoutkhanqindev.custom_aod.presentation.components.AppEditorResetButton
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodComponentSpec
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.ClassicThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.isDefault
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeDraftUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeTabValue
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeDecorState
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeIntent

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

// Đặt lại kiểu riêng của tab đang mở: khối dùng chung (kiểu, font, cỡ, màu) hay khối Clock lúc onboarding. Tab không có kiểu (nền,
// hiệu ứng...) hay khối do chủ đề đang dùng tự vẽ thì không có nút.
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
