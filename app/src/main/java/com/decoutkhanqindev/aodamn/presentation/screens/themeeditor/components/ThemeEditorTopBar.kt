package com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppPrimaryButton
import com.decoutkhanqindev.aodamn.presentation.components.AppTextButton
import com.decoutkhanqindev.aodamn.presentation.components.AppTopBar
import com.decoutkhanqindev.aodamn.presentation.components.editor.AppEditorHistoryButtons
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.state.ThemeEditorHistoryState
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.state.ThemeEditorIntent
import com.decoutkhanqindev.aodamn.presentation.theme.LabelMedium
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.Theme

@Composable
fun ThemeEditorTopBar(
    @StringRes themeNameRes: Int,
    isEditing: Boolean,
    isActive: Boolean,
    isLoading: Boolean,
    hasChanges: Boolean,
    history: ThemeEditorHistoryState,
    onIntent: (ThemeEditorIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppTopBar(
        title = {
            AnimatedContent(
                targetState = isEditing,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ThemeEditorTitle",
            ) { editing ->
                if (editing) {
                    AppEditorHistoryButtons(
                        canUndo = history.canUndo,
                        canRedo = history.canRedo,
                        canReset = !history.isDefault,
                        onUndo = { onIntent(ThemeEditorIntent.History.UndoChange) },
                        onRedo = { onIntent(ThemeEditorIntent.History.RedoChange) },
                        onReset = { onIntent(ThemeEditorIntent.History.ShowResetConfirm) },
                    )
                } else {
                    Column {
                        Text(
                            text = stringResource(themeNameRes),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )

                        AnimatedVisibility(
                            visible = isActive,
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            Text(
                                text = stringResource(R.string.theme_in_use),
                                color = Mint,
                                style = LabelMedium,
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier,
        onNavigateBack = { onIntent(ThemeEditorIntent.NavigateBack) },
        isTitleCentered = false,
        actions = {
            AnimatedVisibility(
                visible = !isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                AnimatedContent(
                    targetState = isEditing,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "ThemeEditorActions",
                ) { editing ->
                    Row(
                        modifier = Modifier.padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (!editing) {
                            AppTextButton(
                                text = stringResource(R.string.theme_edit),
                                onClick = { onIntent(ThemeEditorIntent.Edit.StartEditing) },
                                isCompact = true,
                            )
                        }

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
                            isCompact = true,
                        )
                    }
                }
            }
        },
    )
}

@Preview(widthDp = 360)
@Composable
private fun ThemeEditorTopBarPreview() {
    Theme {
        ThemeEditorTopBar(
            themeNameRes = AodThemeValue.CARD.nameRes,
            isEditing = false,
            isActive = true,
            isLoading = false,
            hasChanges = false,
            history = ThemeEditorHistoryState(),
            onIntent = {},
        )
    }
}

@Preview(widthDp = 360)
@Composable
private fun ThemeEditorTopBarEditingPreview() {
    Theme {
        ThemeEditorTopBar(
            themeNameRes = AodThemeValue.CARD.nameRes,
            isEditing = true,
            isActive = true,
            isLoading = false,
            hasChanges = true,
            history = ThemeEditorHistoryState(canUndo = true, isDefault = false),
            onIntent = {},
        )
    }
}
