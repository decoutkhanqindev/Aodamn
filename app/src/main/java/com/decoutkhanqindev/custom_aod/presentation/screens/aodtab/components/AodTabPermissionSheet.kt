package com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppTextButton
import com.decoutkhanqindev.custom_aod.presentation.components.permission.AppPermissionSheet
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.custom_aod.presentation.screens.aodtab.state.AodTabIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyLarge
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.Red
import com.decoutkhanqindev.custom_aod.presentation.theme.RedAlpha12
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AodTabPermissionSheet(
    isVisible: Boolean,
    permissions: ImmutableList<PermissionUiModel>,
    onIntent: (AodTabIntent) -> Unit,
) {
    AppPermissionSheet(
        isVisible = isVisible,
        permissions = permissions,
        title = stringResource(R.string.permission_sheet_title),
        subtitle = stringResource(R.string.permission_sheet_desc),
        onAllowClick = { permission -> onIntent(AodTabIntent.OpenPermissionSettings(permission)) },
        onDismiss = { onIntent(AodTabIntent.DismissPermissionSheet) },
    )
}

@Composable
fun AodTabPermissionWarning(
    isVisible: Boolean,
    onIntent: (AodTabIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(visible = isVisible, modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clip(RoundedCornerShape16dp)
                .background(RedAlpha12)
                .padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_warning_circle),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Red,
            )

            Text(
                text = stringResource(R.string.permission_sheet_title),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                color = GreyED,
                style = BodyLarge,
            )

            AppTextButton(
                text = stringResource(R.string.action_allow),
                onClick = { onIntent(AodTabIntent.ShowPermissionSheet) },
                contentColor = Mint,
            )
        }
    }
}

@Preview(widthDp = 360)
@Composable
private fun AodTabPermissionWarningPreview() {
    Theme {
        AodTabPermissionWarning(isVisible = true, onIntent = {})
    }
}
