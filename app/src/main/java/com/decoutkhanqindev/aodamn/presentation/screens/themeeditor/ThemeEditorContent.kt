package com.decoutkhanqindev.aodamn.presentation.screens.themeeditor

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppBottomAdSlot
import com.decoutkhanqindev.aodamn.presentation.components.AppDangerButton
import com.decoutkhanqindev.aodamn.presentation.components.AppPrimaryButton
import com.decoutkhanqindev.aodamn.presentation.components.AppTextButton
import com.decoutkhanqindev.aodamn.presentation.components.aod.AodPreviewFrame
import com.decoutkhanqindev.aodamn.presentation.components.aod.editor.AodThemeOptionList
import com.decoutkhanqindev.aodamn.presentation.components.aod.rememberAodPreview
import com.decoutkhanqindev.aodamn.presentation.components.editor.AppApplyingOverlay
import com.decoutkhanqindev.aodamn.presentation.components.editor.AppEditorPanel
import com.decoutkhanqindev.aodamn.presentation.components.editor.AppEditorResetButton
import com.decoutkhanqindev.aodamn.presentation.components.editor.AppPreviewOrientationButton
import com.decoutkhanqindev.aodamn.presentation.components.onLongClick
import com.decoutkhanqindev.aodamn.presentation.components.permission.AppPermissionSheet
import com.decoutkhanqindev.aodamn.presentation.components.sheet.AppConfirmSheet
import com.decoutkhanqindev.aodamn.presentation.components.sheet.AppSheet
import com.decoutkhanqindev.aodamn.presentation.components.sheet.AppSheetHeroHeader
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.isDefault
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.components.ThemeEditorTabBar
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.components.ThemeEditorTopBar
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.state.ThemeEditorIntent
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.state.ThemeEditorState
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.Theme

@Composable
fun ThemeEditorContent(
    state: ThemeEditorState,
    onIntent: (ThemeEditorIntent) -> Unit,
    bannerAd: @Composable () -> Unit,
) {
    val preview = rememberAodPreview(draft = state.preview)

    // Nhấn giữ khung xem trước: ẩn thanh trên, panel, thanh tab và banner để xem AOD toàn màn hình, thả tay thì hiện lại.
    var isPeeking by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
    ) {
        AodPreviewFrame(state = preview, isLandscape = state.isLandscapePreview)

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
                ThemeEditorTopBar(
                    themeNameRes = state.theme.nameRes,
                    isEditing = state.isEditing,
                    isActive = state.isActive,
                    isLoading = state.isLoading,
                    hasChanges = state.hasChanges,
                    history = state.history,
                    onIntent = onIntent,
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
        ) {
            AnimatedVisibility(
                visible = !isPeeking,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp, bottom = 24.dp),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                AppPreviewOrientationButton(
                    isLandscape = state.isLandscapePreview,
                    onClick = { onIntent(ThemeEditorIntent.ToggleLandscapePreview) },
                )
            }

            AnimatedVisibility(
                visible = state.isEditing && !isPeeking,
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

            // Banner cả lúc xem (chưa Sửa): không có thanh tab bên trên thì nền trong suốt.
            AppBottomAdSlot(
                isHidden = isPeeking,
                background = if (state.isEditing) Neutral12 else Color.Transparent,
                content = bannerAd,
            )
        }

        AnimatedVisibility(
            visible = state.isApplying,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            AppApplyingOverlay(text = stringResource(R.string.customize_applying))
        }
    }

    AppPermissionSheet(
        isVisible = state.isPermissionSheetVisible,
        permissions = state.requiredPermissions,
        title = stringResource(R.string.permission_apply_title),
        subtitle = stringResource(R.string.permission_apply_desc),
        onAllowClick = { permission -> onIntent(ThemeEditorIntent.Permission.OpenPermissionSettings(permission)) },
        onDismiss = { onIntent(ThemeEditorIntent.Permission.DismissPermissionSheet) },
    )

    AppConfirmSheet(
        isVisible = state.history.isResetConfirmVisible,
        iconRes = R.drawable.ic_arrow_counter_clockwise,
        title = stringResource(R.string.customize_reset_title),
        description = stringResource(R.string.customize_reset_desc),
        confirmLabel = stringResource(R.string.customize_reset),
        onConfirm = { onIntent(ThemeEditorIntent.History.ResetTheme) },
        onDismiss = { onIntent(ThemeEditorIntent.History.DismissResetConfirm) },
    )

    // Thoát sửa khi còn thay đổi: việc nên làm là lưu (nút chính), bỏ thay đổi là nút đỏ nhạt, ở lại sửa bằng chữ.
    AppSheet(
        isVisible = state.history.isDiscardConfirmVisible,
        onDismissRequest = { onIntent(ThemeEditorIntent.Edit.DismissDiscard) },
    ) {
        AppSheetHeroHeader(
            title = stringResource(R.string.theme_discard_title),
            description = stringResource(R.string.theme_discard_desc),
            badgeIconRes = R.drawable.ic_pencil_simple,
        )

        AppPrimaryButton(
            text = stringResource(R.string.theme_save_and_exit),
            onClick = { onIntent(ThemeEditorIntent.SaveTheme) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            iconRes = R.drawable.ic_check,
        )

        AppDangerButton(
            text = stringResource(R.string.theme_discard_confirm),
            onClick = { onIntent(ThemeEditorIntent.Edit.ConfirmDiscard) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )

        AppTextButton(
            text = stringResource(R.string.theme_keep_editing),
            onClick = { onIntent(ThemeEditorIntent.Edit.DismissDiscard) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
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
            bannerAd = {},
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
            bannerAd = {},
        )
    }
}
