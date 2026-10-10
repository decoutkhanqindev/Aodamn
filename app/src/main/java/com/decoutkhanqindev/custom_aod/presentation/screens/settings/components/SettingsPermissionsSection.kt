package com.decoutkhanqindev.custom_aod.presentation.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppIconTile
import com.decoutkhanqindev.custom_aod.presentation.model.aod.info.WakeResultValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionStatusValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.custom_aod.presentation.screens.settings.state.SettingsIntent
import com.decoutkhanqindev.custom_aod.presentation.theme.BodySmall
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.MintAlpha12
import com.decoutkhanqindev.custom_aod.presentation.theme.Red
import com.decoutkhanqindev.custom_aod.presentation.theme.RedAlpha12
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
                icon = permission.permission.icon,
                tint = if (isMissingRequired) Red else Mint,
                containerColor = if (isMissingRequired) RedAlpha12 else MintAlpha12,
            )
        },
        trailingContent = {
            if (status == PermissionStatusValue.GRANTED) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = Mint,
                )
            } else {
                OutlinedButton(onClick = onOpen) {
                    Text(text = stringResource(R.string.action_open))
                }
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
            imageVector = when (lastWake) {
                WakeResultValue.OK -> Icons.Outlined.CheckCircle
                WakeResultValue.FAILED -> Icons.Outlined.ErrorOutline
                WakeResultValue.UNKNOWN -> Icons.Outlined.Info
            },
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
