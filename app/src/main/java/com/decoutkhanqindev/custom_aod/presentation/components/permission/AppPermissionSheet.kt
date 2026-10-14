package com.decoutkhanqindev.custom_aod.presentation.components.permission

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppPrimaryButton
import com.decoutkhanqindev.custom_aod.presentation.components.AppTextButton
import com.decoutkhanqindev.custom_aod.presentation.components.sheet.AppSheet
import com.decoutkhanqindev.custom_aod.presentation.components.sheet.AppSheetHeroHeader
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.permission.PermissionValue
import com.decoutkhanqindev.custom_aod.presentation.model.permission.nextPermission
import com.decoutkhanqindev.custom_aod.presentation.theme.Black
import kotlinx.collections.immutable.ImmutableList

// Sheet xin quyền (tab AOD, áp dụng ở Tùy chỉnh / màn chủ đề, mục cần quyền ở Tùy chỉnh): hình điện thoại với khiên, các quyền theo thứ tự
// và nút "Cấp quyền tiếp theo" mở đúng quyền còn thiếu kế tiếp, để user chỉ việc bấm một nút cho tới khi đủ. Một quyền thì nút là "Cho phép".
@Composable
fun AppPermissionSheet(
    isVisible: Boolean,
    permissions: ImmutableList<PermissionUiModel>,
    title: String,
    subtitle: String,
    onAllowClick: (PermissionValue) -> Unit,
    onDismiss: () -> Unit,
) {
    AppSheet(value = permissions.takeIf { isVisible }, onDismissRequest = onDismiss) { shown ->
        val next = shown.nextPermission()

        AppSheetHeroHeader(
            title = title,
            description = subtitle,
            badgeIconRes = R.drawable.ic_shield_check_fill,
            illustrationWidth = 112.dp,
        )

        AppPermissionSteps(
            permissions = shown,
            onAllowClick = onAllowClick,
            modifier = Modifier.padding(top = 16.dp),
            containerColor = Black,
        )

        if (next != null) {
            AppPrimaryButton(
                text = stringResource(if (shown.size > 1) R.string.permission_allow_next else R.string.action_allow),
                onClick = { onAllowClick(next) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                iconRes = R.drawable.ic_caret_right,
            )
        }

        AppTextButton(
            text = stringResource(R.string.action_later),
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        )
    }
}
