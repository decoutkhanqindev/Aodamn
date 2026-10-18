package com.decoutkhanqindev.aodamn.presentation.screens.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppIllustration
import com.decoutkhanqindev.aodamn.presentation.components.AppTextButton
import com.decoutkhanqindev.aodamn.presentation.components.onboarding.AppOnboardingFooter
import com.decoutkhanqindev.aodamn.presentation.components.onboarding.AppOnboardingHeader
import com.decoutkhanqindev.aodamn.presentation.components.onboarding.AppOnboardingTopBar
import com.decoutkhanqindev.aodamn.presentation.components.permission.AppPermissionSteps
import com.decoutkhanqindev.aodamn.presentation.model.onboarding.OnboardingStepValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.aodamn.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.aodamn.presentation.model.permission.nextPermission
import com.decoutkhanqindev.aodamn.presentation.screens.permission.state.PermissionIntent
import com.decoutkhanqindev.aodamn.presentation.screens.permission.state.PermissionState
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Theme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PermissionContent(
    state: PermissionState,
    onIntent: (PermissionIntent) -> Unit,
    bannerAd: @Composable () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Bỏ qua vào Main mà không nhắc quyền ngay (chỉ đòi lúc Áp dụng); đủ quyền thì nút dưới đã là Bắt đầu nên ẩn đi.
            AppOnboardingTopBar(
                step = OnboardingStepValue.PERMISSION,
                onNavigateBack = { onIntent(PermissionIntent.NavigateBack) },
                actions = {
                    if (!state.isConfirmEnabled) {
                        AppTextButton(
                            text = stringResource(R.string.action_skip),
                            onClick = { onIntent(PermissionIntent.SkipPermissions) },
                        )
                    }
                },
            )
        },
        containerColor = Color.Transparent,
        contentColor = GreyED,
        bottomBar = {
            // Thiếu quyền thì nút không mờ đi mà mở đúng quyền còn thiếu kế tiếp; đủ thì thành Bắt đầu.
            val next = state.permissions.nextPermission()
            val remaining = state.permissions.count { permission -> permission.isGranted == false }
            AppOnboardingFooter(
                actionLabel = stringResource(if (state.isConfirmEnabled) R.string.action_get_started else R.string.permission_allow_next),
                onAction = {
                    if (state.isConfirmEnabled || next == null) {
                        onIntent(PermissionIntent.ConfirmPermissions)
                    } else {
                        onIntent(PermissionIntent.OpenPermissionSettings(next))
                    }
                },
                hint = if (state.isConfirmEnabled) null else pluralStringResource(R.plurals.permission_remaining, remaining, remaining),
                actionIconRes = if (state.isConfirmEnabled) null else R.drawable.ic_caret_right,
                ad = bannerAd,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AppIllustration(
                badgeIconRes = R.drawable.ic_shield_check_fill,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                width = 112.dp,
            )

            AppOnboardingHeader(
                title = stringResource(R.string.permission_title),
                subtitle = stringResource(R.string.permission_subtitle),
                modifier = Modifier.padding(bottom = 8.dp),
            )

            AppPermissionSteps(
                permissions = state.permissions,
                onAllowClick = { permission -> onIntent(PermissionIntent.OpenPermissionSettings(permission)) },
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 720)
@Composable
private fun PermissionContentMissingPreview() {
    Theme {
        PermissionContent(
            state = PermissionState(
                permissions = persistentListOf(
                    PermissionUiModel(permission = PermissionValue.OVERLAY, isGranted = true),
                    PermissionUiModel(
                        permission = PermissionValue.MIUI_LOCK_SCREEN,
                        isGranted = false
                    ),
                ),
            ),
            onIntent = {},
            bannerAd = {},
        )
    }
}

@Preview(widthDp = 360, heightDp = 720)
@Composable
private fun PermissionContentReadyPreview() {
    Theme {
        PermissionContent(
            state = PermissionState(
                permissions = persistentListOf(
                    PermissionUiModel(permission = PermissionValue.OVERLAY, isGranted = true),
                ),
            ),
            onIntent = {},
            bannerAd = {},
        )
    }
}
