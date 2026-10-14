package com.decoutkhanqindev.custom_aod.presentation.screens.customize

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.aod.AodPreviewFrame
import com.decoutkhanqindev.custom_aod.presentation.components.aod.editor.DrawingPadSheet
import com.decoutkhanqindev.custom_aod.presentation.components.aod.editor.MemoEditorSheet
import com.decoutkhanqindev.custom_aod.presentation.components.aod.rememberAodPreview
import com.decoutkhanqindev.custom_aod.presentation.components.editor.AppApplyingOverlay
import com.decoutkhanqindev.custom_aod.presentation.components.onLongClick
import com.decoutkhanqindev.custom_aod.presentation.components.permission.AppPermissionSheet
import com.decoutkhanqindev.custom_aod.presentation.components.sheet.AppConfirmSheet
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeGuideStepValue
import com.decoutkhanqindev.custom_aod.presentation.model.customize.CustomizeTabValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.components.CustomizeGuideOverlay
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.components.CustomizePanelHost
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.components.CustomizeTabBar
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.components.CustomizeToolbar
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.components.CustomizeTopBar
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.customize.state.CustomizeState
import com.decoutkhanqindev.custom_aod.presentation.theme.Black
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CustomizeContent(
    state: CustomizeState,
    onIntent: (CustomizeIntent) -> Unit,
) {
    val preview = rememberAodPreview(
        draft = state.draft,
        isGlowing = state.effects.isGlowPreviewing,
        shownComponent = state.selectedTab?.component,
    )
    val guideTargets = remember { mutableStateMapOf<CustomizeGuideStepValue, Rect>() }
    val onTargetPositioned = remember(guideTargets) {
        { step: CustomizeGuideStepValue, bounds: Rect ->
            if (guideTargets[step] != bounds) guideTargets[step] = bounds
        }
    }
    var lastGuideStep by remember { mutableStateOf(CustomizeGuideStepValue.PREVIEW) }

    // Nhấn giữ khung xem trước: ẩn thanh trên, thanh công cụ, panel và thanh tab để xem AOD toàn màn hình, thả tay thì hiện lại.
    var isPeeking by remember { mutableStateOf(false) }

    SideEffect(state.guideStep) {
        state.guideStep?.let { step -> lastGuideStep = step }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),
    ) {
        AodPreviewFrame(state = preview, isLandscape = state.isLandscapePreview)

        // Chạm vào khung xem trước (ngoài panel, thanh tab, thanh trên) khi panel đang mở thì đóng panel; nhấn giữ thì xem toàn màn hình.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onLongClick(
                    onRelease = { isPeeking = false },
                    onClick = { if (state.selectedTab != null) onIntent(CustomizeIntent.Panel.ClosePanel) },
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
                CustomizeTopBar(
                    isFirstOpen = state.isFirstOpen,
                    isApplying = state.isApplying,
                    isLandscapePreview = state.isLandscapePreview,
                    onIntent = onIntent,
                    onGuideTargetPositioned = onTargetPositioned,
                )

                CustomizeToolbar(
                    history = state.history,
                    isApplying = state.isApplying || state.isLoading,
                    onIntent = onIntent,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        onTargetPositioned(
                            CustomizeGuideStepValue.ACTIONS,
                            coordinates.boundsInWindow()
                        )
                    },
                )
            }
        }

        AnimatedVisibility(
            visible = !isPeeking,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn() + slideInVertically { height -> height },
            exit = fadeOut() + slideOutVertically { height -> height },
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                CustomizePanelHost(
                    selectedTab = state.selectedTab,
                    isFirstOpen = state.isFirstOpen,
                    draft = state.draft,
                    decor = state.decor,
                    onIntent = onIntent,
                )

                CustomizeTabBar(
                    selectedTab = state.selectedTab,
                    spec = state.draft.appearance.clock.theme.value.spec,
                    guideStep = state.guideStep,
                    onIntent = onIntent,
                    onClusterPositioned = onTargetPositioned,
                )
            }
        }

        AnimatedVisibility(
            visible = state.guideStep != null,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            CustomizeGuideOverlay(
                step = state.guideStep ?: lastGuideStep,
                targets = guideTargets,
                onIntent = onIntent,
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

    CustomizePermissionSheet(permission = state.permissionRequest, onIntent = onIntent)

    AppPermissionSheet(
        isVisible = state.isApplyPermissionSheetVisible,
        permissions = state.requiredPermissions,
        title = stringResource(R.string.permission_apply_title),
        subtitle = stringResource(R.string.permission_apply_desc),
        onAllowClick = { permission -> onIntent(CustomizeIntent.Permission.OpenPermissionSettings(permission)) },
        onDismiss = { onIntent(CustomizeIntent.Permission.DismissApplyPermissionSheet) },
    )

    AppConfirmSheet(
        isVisible = state.history.isResetConfirmVisible,
        iconRes = R.drawable.ic_arrow_counter_clockwise,
        title = stringResource(R.string.customize_reset_title),
        description = stringResource(R.string.customize_reset_desc),
        confirmLabel = stringResource(R.string.customize_reset),
        onConfirm = { onIntent(CustomizeIntent.History.ResetCustomization) },
        onDismiss = { onIntent(CustomizeIntent.History.DismissResetConfirm) },
    )

    DrawingPadSheet(
        isVisible = state.decor.isDrawingPadVisible,
        onConfirm = { drawing -> onIntent(CustomizeIntent.Decor.ChangeDrawing(drawing)) },
        onDismiss = { onIntent(CustomizeIntent.Decor.DismissDrawingPad) },
    )

    MemoEditorSheet(
        isVisible = state.decor.isMemoEditorVisible,
        memo = state.draft.decor.memo,
        onConfirm = { memo -> onIntent(CustomizeIntent.Decor.ChangeMemo(memo)) },
        onDismiss = { onIntent(CustomizeIntent.Decor.DismissMemoEditor) },
    )
}

@Composable
private fun CustomizePermissionSheet(
    permission: PermissionValue?,
    onIntent: (CustomizeIntent) -> Unit,
) {
    val permissions = remember(permission) {
        permission?.let { persistentListOf(PermissionUiModel(permission = it, isGranted = false)) }
            ?: persistentListOf()
    }

    AppPermissionSheet(
        isVisible = permission != null,
        permissions = permissions,
        title = stringResource(R.string.customize_permission_title),
        subtitle = stringResource(R.string.customize_permission_desc),
        onAllowClick = { onIntent(CustomizeIntent.Permission.OpenPermissionSettings(it)) },
        onDismiss = { onIntent(CustomizeIntent.Permission.DismissPermissionSheet) },
    )
}

@Preview(widthDp = 360, heightDp = 760)
@Composable
private fun CustomizeContentPreview() {
    Theme {
        CustomizeContent(
            state = CustomizeState(
                isLoading = false,
                selectedTab = CustomizeTabValue.DATE,
            ),
            onIntent = {},
        )
    }
}

@Preview(widthDp = 360, heightDp = 760)
@Composable
private fun CustomizeContentOnboardingClockPreview() {
    Theme {
        CustomizeContent(
            state = CustomizeState(
                isLoading = false,
                isFirstOpen = true,
                selectedTab = CustomizeTabValue.CLOCK,
            ),
            onIntent = {},
        )
    }
}

@Preview(widthDp = 360, heightDp = 760)
@Composable
private fun CustomizeContentGuidePreview() {
    Theme {
        CustomizeContent(
            state = CustomizeState(
                isLoading = false,
                guideStep = CustomizeGuideStepValue.INFO,
            ),
            onIntent = {},
        )
    }
}
