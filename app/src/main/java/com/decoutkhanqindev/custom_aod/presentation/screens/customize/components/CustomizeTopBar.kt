package com.decoutkhanqindev.custom_aod.presentation.screens.customize.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppPrimaryButton
import com.decoutkhanqindev.custom_aod.presentation.components.AppTextButton
import com.decoutkhanqindev.custom_aod.presentation.components.AppTopBar
import com.decoutkhanqindev.custom_aod.presentation.components.editor.AppEditorHistoryButtons
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeHistoryState
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme

// Thanh trên thay cho tiêu đề và thanh công cụ nổi cũ, để AOD xem trước không bị che: quay lại · hoàn tác, làm lại, đặt lại · (Bỏ qua lúc
// onboarding) · Áp dụng. Không có ô bước onboarding: thanh không đủ chỗ ở máy hẹp; xoay khung và hướng dẫn nằm cuối thanh tab.
@Composable
fun CustomizeTopBar(
    isFirstOpen: Boolean,
    isApplying: Boolean,
    history: CustomizeHistoryState,
    onIntent: (CustomizeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppTopBar(
        title = {
            AppEditorHistoryButtons(
                canUndo = history.canUndo,
                canRedo = history.canRedo,
                canReset = !history.isDefault,
                onUndo = { onIntent(CustomizeIntent.History.UndoChange) },
                onRedo = { onIntent(CustomizeIntent.History.RedoChange) },
                onReset = { onIntent(CustomizeIntent.History.ShowResetConfirm) },
            )
        },
        modifier = modifier,
        onNavigateBack = { onIntent(CustomizeIntent.NavigateBack) },
        isTitleCentered = false,
        actions = {
            if (isFirstOpen) {
                AppTextButton(
                    text = stringResource(R.string.action_skip),
                    onClick = { onIntent(CustomizeIntent.SkipCustomization) },
                    isEnabled = !isApplying,
                    isCompact = true,
                )
            }

            AppPrimaryButton(
                text = stringResource(R.string.customize_apply),
                onClick = { onIntent(CustomizeIntent.ConfirmCustomization) },
                modifier = Modifier.padding(start = 4.dp, end = 8.dp),
                isEnabled = history.hasChanges && !isApplying,
                isCompact = true,
            )
        },
    )
}

@Preview(widthDp = 360)
@Composable
private fun CustomizeTopBarPreview() {
    Theme {
        CustomizeTopBar(
            isFirstOpen = false,
            isApplying = false,
            history = CustomizeHistoryState(canUndo = true, hasChanges = true, isDefault = false),
            onIntent = {},
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun CustomizeTopBarFirstOpenPreview() {
    Theme {
        CustomizeTopBar(
            isFirstOpen = true,
            isApplying = false,
            history = CustomizeHistoryState(),
            onIntent = {},
        )
    }
}
