package com.decoutkhanqindev.aodamn.presentation.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppIconTile
import com.decoutkhanqindev.aodamn.presentation.components.AppTextButton
import com.decoutkhanqindev.aodamn.presentation.model.aod.info.WakeResultValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionStatusValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.aodamn.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.aodamn.presentation.theme.BodySmall
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.MintAlpha12
import com.decoutkhanqindev.aodamn.presentation.theme.Red
import com.decoutkhanqindev.aodamn.presentation.theme.RedAlpha12
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SettingsPermissionsSection(
    permissions: ImmutableList<PermissionUiModel>,
    lastWake: WakeResultValue,
    onIntent: (SettingsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SettingsGroup(title = stringResource(R.string.section_permissions)) {
            permissions.forEachIndexed { index, permission ->
                if (index > 0) SettingsDivider()

                PermissionItem(
                    permission = permission,
                    onOpen = { onIntent(SettingsIntent.Permission.OpenPermissionSettings(permission.permission)) },
                )
            }
        }

        LastWakeRow(
            lastWake = lastWake,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}

@Composable
private fun PermissionItem(
    permission: PermissionUiModel,
    onOpen: () -> Unit,
) {
    val status = permission.status
    val isMissingRequired = status == PermissionStatusValue.MISSING_REQUIRED

    ListItem(
        headlineContent = { Text(text = stringResource(permission.permission.titleRes)) },
        supportingContent = {
            Text(
                text = stringResource(status.descriptionRes),
                color = if (isMissingRequired) Red else Grey9A,
            )
        },
        leadingContent = {
            AppIconTile(
                iconRes = permission.permission.iconRes,
                tint = if (isMissingRequired) Red else Mint,
                containerColor = if (isMissingRequired) RedAlpha12 else MintAlpha12,
            )
        },
        trailingContent = {
            if (status == PermissionStatusValue.GRANTED) {
                Icon(
                    painter = painterResource(R.drawable.ic_check_circle_fill),
                    contentDescription = null,
                    tint = Mint,
                )
            } else {
                AppTextButton(
                    text = stringResource(R.string.action_open),
                    onClick = onOpen,
                    contentColor = Mint,
                )
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
    )
}

@Composable
private fun LastWakeRow(
    lastWake: WakeResultValue,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.padding(horizontal = 16.dp)) {
        Icon(
            painter = painterResource(
                when (lastWake) {
                    WakeResultValue.OK -> R.drawable.ic_check_circle_fill
                    WakeResultValue.FAILED -> R.drawable.ic_warning_circle
                    WakeResultValue.UNKNOWN -> R.drawable.ic_info
                },
            ),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = when (lastWake) {
                WakeResultValue.OK -> Mint
                WakeResultValue.FAILED -> Red
                WakeResultValue.UNKNOWN -> Grey9A
            },
        )

        Text(
            text = stringResource(lastWake.messageRes),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            color = Grey9A,
            style = BodySmall,
        )
    }
}
