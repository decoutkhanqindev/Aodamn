package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.sheet.AppConfirmSheet
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.components.AodTabDesignCard
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.components.AodTabPermissionSheet
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.components.AodTabPermissionWarning
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.components.aodTabThemesSection
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabIntent
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabPermissionState
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabState
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyLarge
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.MintAlpha12
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AodTabContent(
    state: AodTabState,
    onIntent: (AodTabIntent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        AnimatedVisibility(
            visible = !state.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            // Cả tab là một lưới 3 cột (lưới chủ đề lazy không lồng được trong verticalScroll): phần đầu chiếm cả hàng, mỗi chủ đề một ô.
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item(
                    key = "header",
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = "header",
                ) {
                    Column {
                        AodSwitchBar(
                            isEnabled = state.isAodEnabled,
                            onIntent = onIntent,
                        )

                        AodTabPermissionWarning(
                            isVisible = state.isPermissionWarningVisible,
                            onIntent = onIntent,
                        )

                        AodTabDesignCard(
                            design = state.design,
                            isAodEnabled = state.isAodEnabled,
                            onIntent = onIntent,
                            modifier = Modifier.padding(top = 16.dp),
                        )
                    }
                }

                aodTabThemesSection(
                    activeTheme = state.activeTheme,
                    onIntent = onIntent,
                )
            }
        }
    }

    AodTabPermissionSheet(
        isVisible = state.isPermissionSheetVisible,
        permissions = state.permission.requiredPermissions,
        onIntent = onIntent,
    )

    AppConfirmSheet(
        isVisible = state.isThemeResetConfirmVisible,
        iconRes = R.drawable.ic_arrow_counter_clockwise,
        title = stringResource(R.string.aod_tab_theme_reset_title),
        description = stringResource(R.string.aod_tab_theme_reset_desc, stringResource(AodThemeValue.DEFAULT.nameRes)),
        confirmLabel = stringResource(R.string.customize_reset),
        onConfirm = { onIntent(AodTabIntent.ResetTheme) },
        onDismiss = { onIntent(AodTabIntent.DismissThemeResetConfirm) },
    )
}

@Composable
private fun AodSwitchBar(
    isEnabled: Boolean,
    onIntent: (AodTabIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor by animateColorAsState(
        targetValue = if (isEnabled) MintAlpha12 else Neutral12,
        label = "AodSwitchBarColor",
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .clip(CircleShape)
            .background(containerColor)
            .toggleable(
                value = isEnabled,
                role = Role.Switch,
                onValueChange = { onIntent(AodTabIntent.ToggleAod(it)) },
            )
            .padding(
                start = 24.dp,
                end = 16.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.opt_enabled),
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            color = GreyED,
            fontWeight = FontWeight.Medium,
            style = BodyLarge,
        )

        Switch(
            checked = isEnabled,
            onCheckedChange = null,
        )
    }
}

@Preview(widthDp = 360, heightDp = 1000)
@Composable
private fun AodTabContentPreview() {
    Theme {
        AodTabContent(
            state = AodTabState(
                isLoading = false,
                isAodEnabled = true,
            ),
            onIntent = {},
        )
    }
}

@Preview(widthDp = 360, heightDp = 1000)
@Composable
private fun AodTabContentMissingPermissionPreview() {
    Theme {
        AodTabContent(
            state = AodTabState(
                isLoading = false,
                permission = AodTabPermissionState(
                    requiredPermissions = persistentListOf(
                        PermissionUiModel(permission = PermissionValue.OVERLAY, isGranted = false),
                    ),
                    isSheetDismissed = true,
                ),
            ),
            onIntent = {},
        )
    }
}
