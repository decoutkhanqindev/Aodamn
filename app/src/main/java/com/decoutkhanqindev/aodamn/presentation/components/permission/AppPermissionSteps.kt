package com.decoutkhanqindev.aodamn.presentation.components.permission

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionStatusValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.nextPermission
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.BodySmall
import com.decoutkhanqindev.aodamn.presentation.theme.Grey3A
import com.decoutkhanqindev.aodamn.presentation.theme.Grey5A
import com.decoutkhanqindev.aodamn.presentation.theme.Grey6E
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.LabelLarge
import com.decoutkhanqindev.aodamn.presentation.theme.LabelMedium
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral12
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.aodamn.presentation.theme.TitleMedium
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AppPermissionSteps(
    permissions: ImmutableList<PermissionUiModel>,
    onAllowClick: (PermissionValue) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Neutral12,
) {
    val next = permissions.nextPermission()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (permissions.size > 1) {
            AppPermissionProgress(
                grantedCount = permissions.count { permission -> permission.status == PermissionStatusValue.GRANTED },
                total = permissions.size,
                modifier = Modifier.padding(bottom = 4.dp),
            )
        }

        permissions.forEachIndexed { index, permission ->
            AppPermissionStep(
                permission = permission,
                number = index + 1,
                isNext = permission.permission == next,
                onClick = { onAllowClick(permission.permission) },
                containerColor = containerColor,
            )
        }
    }
}

@Composable
private fun AppPermissionProgress(
    grantedCount: Int,
    total: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        Text(
            text = stringResource(R.string.permission_progress, grantedCount, total),
            color = Mint,
            fontWeight = FontWeight.Bold,
            style = LabelLarge,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            repeat(total) { index ->
                val color by animateColorAsState(if (index < grantedCount) Mint else Grey3A, label = "AppPermissionProgress")
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(color = color, shape = CircleShape),
                )
            }
        }
    }
}

@Composable
private fun AppPermissionStep(
    permission: PermissionUiModel,
    number: Int,
    isNext: Boolean,
    onClick: () -> Unit,
    containerColor: Color,
) {
    val isGranted = permission.status == PermissionStatusValue.GRANTED
    val border by animateColorAsState(if (isNext) Mint else Color.Transparent, label = "AppPermissionStepBorder")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape16dp)
            .background(containerColor)
            .border(width = 1.dp, color = border, shape = RoundedCornerShape16dp)
            .then(
                if (isGranted) {
                    Modifier
                } else {
                    Modifier
                        .onClick(shape = RoundedCornerShape16dp, action = onClick)
                        .semantics { role = Role.Button }
                },
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedContent(
            targetState = isGranted,
            contentAlignment = Alignment.Center,
            label = "AppPermissionStepMark",
        ) { granted ->
            if (granted) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(color = Mint, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Black,
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .border(width = 1.5.dp, color = if (isNext) Mint else Grey5A, shape = CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = number.toString(),
                        color = if (isNext) Mint else Grey9A,
                        fontWeight = FontWeight.Bold,
                        style = LabelLarge,
                    )
                }
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(permission.permission.nameRes),
                color = GreyED,
                style = TitleMedium,
            )

            Text(
                text = stringResource(permission.permission.descriptionRes),
                modifier = Modifier.padding(top = 2.dp),
                color = Grey9A,
                style = BodySmall,
            )

            AnimatedVisibility(visible = permission.status == PermissionStatusValue.UNKNOWN) {
                Text(
                    text = stringResource(R.string.permission_unknown_hint),
                    modifier = Modifier.padding(top = 4.dp),
                    color = Mint,
                    style = BodySmall,
                )
            }
        }

        if (isGranted) {
            Text(
                text = stringResource(R.string.permission_state_granted),
                color = Mint,
                fontWeight = FontWeight.SemiBold,
                style = LabelMedium,
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.ic_caret_right),
                contentDescription = stringResource(R.string.action_allow),
                modifier = Modifier.size(20.dp),
                tint = if (isNext) Mint else Grey6E,
            )
        }
    }
}

@Preview(widthDp = 360)
@Composable
private fun AppPermissionStepsPreview() {
    Box(
        modifier = Modifier
            .background(Black)
            .padding(16.dp),
    ) {
        AppPermissionSteps(
            permissions = persistentListOf(
                PermissionUiModel(permission = PermissionValue.OVERLAY, isGranted = true),
                PermissionUiModel(permission = PermissionValue.MIUI_LOCK_SCREEN, isGranted = false),
                PermissionUiModel(permission = PermissionValue.NOTIFICATIONS, isGranted = false),
            ),
            onAllowClick = {},
        )
    }
}
