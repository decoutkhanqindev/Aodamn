package com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppPrimaryButton
import com.decoutkhanqindev.custom_aod.presentation.components.AppTextButton
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorHistoryState
import com.decoutkhanqindev.custom_aod.presentation.screens.themeeditor.state.ThemeEditorIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme

// Cùng kiểu thanh công cụ của Tùy chỉnh. Xem trước: Sửa · Áp dụng (chủ đề đang dùng thì Tùy chỉnh, sang phần chung); đang sửa:
// hoàn tác, làm lại, đặt lại · Lưu.
@Composable
fun ThemeEditorToolbar(
    isEditing: Boolean,
    isActive: Boolean,
    hasChanges: Boolean,
    history: ThemeEditorHistoryState,
    onIntent: (ThemeEditorIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = Neutral12,
        contentColor = GreyED,
    ) {
        AnimatedContent(
            targetState = isEditing,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "ThemeEditorToolbar",
        ) { editing ->
            Row(
                modifier = Modifier.padding(start = 4.dp, end = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (editing) {
                    IconButton(
                        onClick = { onIntent(ThemeEditorIntent.History.UndoChange) },
                        enabled = history.canUndo,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_u_up_left),
                            contentDescription = stringResource(R.string.customize_undo),
                        )
                    }

                    IconButton(
                        onClick = { onIntent(ThemeEditorIntent.History.RedoChange) },
                        enabled = history.canRedo,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_u_up_right),
                            contentDescription = stringResource(R.string.customize_redo),
                        )
                    }

                    IconButton(
                        onClick = { onIntent(ThemeEditorIntent.History.ShowResetConfirm) },
                        enabled = !history.isDefault,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_counter_clockwise),
                            contentDescription = stringResource(R.string.customize_reset),
                        )
                    }
                } else {
                    AppTextButton(
                        text = stringResource(R.string.theme_edit),
                        onClick = { onIntent(ThemeEditorIntent.Edit.StartEditing) },
                        iconRes = R.drawable.ic_pencil_simple,
                    )
                }

                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 4.dp),
                    color = NeutralVariant30,
                )

                AppPrimaryButton(
                    text = stringResource(
                        when {
                            editing -> R.string.action_save
                            isActive -> R.string.customize_title
                            else -> R.string.customize_apply
                        },
                    ),
                    onClick = {
                        onIntent(
                            when {
                                editing -> ThemeEditorIntent.SaveTheme
                                isActive -> ThemeEditorIntent.NavigateToCustomize
                                else -> ThemeEditorIntent.ApplyTheme
                            },
                        )
                    },
                    modifier = Modifier.padding(start = 4.dp),
                    isEnabled = !editing || hasChanges,
                )
            }
        }
    }
}

@Preview
@Composable
private fun ThemeEditorToolbarPreview() {
    Theme {
        ThemeEditorToolbar(
            isEditing = false,
            isActive = false,
            hasChanges = false,
            history = ThemeEditorHistoryState(),
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun ThemeEditorToolbarEditingPreview() {
    Theme {
        ThemeEditorToolbar(
            isEditing = true,
            isActive = true,
            hasChanges = true,
            history = ThemeEditorHistoryState(canUndo = true, isDefault = false),
            onIntent = {},
        )
    }
}
