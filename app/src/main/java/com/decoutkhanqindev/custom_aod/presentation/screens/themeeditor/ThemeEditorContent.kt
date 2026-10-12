package com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppBadge
import com.decoutkhanqindev.custom_aod.presentation.components.AppTopBar
import com.decoutkhanqindev.custom_aod.presentation.components.aod.AodContent
import com.decoutkhanqindev.custom_aod.presentation.components.aod.editor.AodThemeOptionList
import com.decoutkhanqindev.custom_aod.presentation.components.aod.rememberAodPreview
import com.decoutkhanqindev.custom_aod.presentation.components.editor.AppApplyingOverlay
import com.decoutkhanqindev.custom_aod.presentation.components.editor.AppEditorPanel
import com.decoutkhanqindev.custom_aod.presentation.components.editor.AppEditorResetButton
import com.decoutkhanqindev.custom_aod.presentation.components.onLongClick
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.isDefault
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.components.ThemeEditorTabBar
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.components.ThemeEditorToolbar
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorState
import com.decoutkhanqindev.custom_aod.presentation.theme.Black
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme

@Composable
fun ThemeEditorContent(
    state: ThemeEditorState,
    onIntent: (ThemeEditorIntent) -> Unit,
) {
    val preview = rememberAodPreview(draft = state.preview)

    // Nhấn giữ khung xem trước: ẩn thanh trên, thanh công cụ, panel và thanh tab để xem AOD toàn màn hình, thả tay thì hiện lại.
    var isPeeking by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
    ) {
        AodContent(state = preview, onIntent = {}, isInteractive = false)

        // Chạm vào khung xem trước: đang xem thì vào sửa, đang sửa mà panel mở thì đóng panel; nhấn giữ thì xem toàn màn hình.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onLongClick(
                    onRelease = { isPeeking = false },
                    onClick = {
                        when {
                            state.isLoading -> Unit
                            !state.isEditing -> onIntent(ThemeEditorIntent.Edit.StartEditing)
                            state.selectedTab != null -> onIntent(ThemeEditorIntent.Edit.ClosePanel)
                        }
                    },
                ) {
                    isPeeking = true
                },
        )

        AnimatedVisibility(
            visible = !isPeeking,
            modifier = Modifier.align(Alignment.TopCenter),
            enter = fadeIn() + slideInVertically { height -> -height },
            exit = fadeOut() + slideOutVertically { height -> -height },
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AppTopBar(
                    title = stringResource(state.theme.nameRes),
                    onNavigateBack = { onIntent(ThemeEditorIntent.NavigateBack) },
                    actions = {
                        AnimatedVisibility(
                            visible = state.isActive,
                            modifier = Modifier.padding(end = 16.dp),
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            AppBadge(text = stringResource(R.string.theme_in_use))
                        }
                    },
                )

                AnimatedVisibility(
                    visible = !state.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    ThemeEditorToolbar(
                        isEditing = state.isEditing,
                        isActive = state.isActive,
                        hasChanges = state.hasChanges,
                        history = state.history,
                        onIntent = onIntent,
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = state.isEditing && !isPeeking,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically { height -> height } + fadeIn(),
            exit = slideOutVertically { height -> height } + fadeOut(),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                AppEditorPanel(
                    selected = state.selectedTab,
                    title = { tab -> stringResource(tab.labelRes) },
                    initial = state.theme.spec.tabs.first(),
                    action = { tab ->
                        AppEditorResetButton(
                            isVisible = !tab.isDefault(state.config),
                            onClick = { onIntent(ThemeEditorIntent.Option.ResetTab(tab)) },
                        )
                    },
                ) { tab ->
                    AodThemeOptionList(
                        tab = tab,
                        config = state.config,
                        onOptionPreview = { key, raw -> onIntent(ThemeEditorIntent.Option.PreviewOption(key, raw)) },
                        onOptionChange = { key, raw -> onIntent(ThemeEditorIntent.Option.ChangeOption(key, raw)) },
                    )
                }

                ThemeEditorTabBar(
                    tabs = state.theme.spec.tabs,
                    selectedTab = state.selectedTab,
                    onIntent = onIntent,
                )
            }
        }

        AnimatedVisibility(
            visible = state.isApplying,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppApplyingOverlay(text = stringResource(R.string.customize_applying))
        }
    }

    if (state.history.isResetConfirmVisible) {
        AlertDialog(
            onDismissRequest = { onIntent(ThemeEditorIntent.History.DismissResetConfirm) },
            confirmButton = {
                TextButton(onClick = { onIntent(ThemeEditorIntent.History.ResetTheme) }) {
                    Text(text = stringResource(R.string.customize_reset))
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(ThemeEditorIntent.History.DismissResetConfirm) }) {
                    Text(text = stringResource(R.string.action_cancel))
                }
            },
            title = { Text(text = stringResource(R.string.customize_reset_title)) },
            text = { Text(text = stringResource(R.string.customize_reset_desc)) },
        )
    }

    if (state.history.isDiscardConfirmVisible) {
        AlertDialog(
            onDismissRequest = { onIntent(ThemeEditorIntent.Edit.DismissDiscard) },
            confirmButton = {
                TextButton(onClick = { onIntent(ThemeEditorIntent.Edit.ConfirmDiscard) }) {
                    Text(text = stringResource(R.string.theme_discard_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { onIntent(ThemeEditorIntent.Edit.DismissDiscard) }) {
                    Text(text = stringResource(R.string.action_cancel))
                }
            },
            title = { Text(text = stringResource(R.string.theme_discard_title)) },
            text = { Text(text = stringResource(R.string.theme_discard_desc)) },
        )
    }
}

@Preview(widthDp = 360, heightDp = 780)
@Composable
private fun ThemeEditorContentPreview() {
    Theme {
        ThemeEditorContent(
            state = ThemeEditorState(
                theme = AodThemeValue.PEBBLE,
                isLoading = false,
            ),
            onIntent = {},
        )
    }
}

@Preview(widthDp = 360, heightDp = 780)
@Composable
private fun ThemeEditorContentEditingPreview() {
    Theme {
        ThemeEditorContent(
            state = ThemeEditorState(
                theme = AodThemeValue.PEBBLE,
                isLoading = false,
                isEditing = true,
                selectedTab = AodThemeValue.PEBBLE.spec.tabs.first(),
            ),
            onIntent = {},
        )
    }
}
