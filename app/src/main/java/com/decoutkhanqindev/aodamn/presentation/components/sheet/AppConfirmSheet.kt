package com.decoutkhanqindev.aodamn.presentation.components.sheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppPrimaryButton
import com.decoutkhanqindev.aodamn.presentation.components.AppTextButton

// Xác nhận một việc (đặt lại...): hình minh hoạ với huy hiệu cùng icon nút xác nhận, nút xác nhận mint ở trên, Hủy bằng chữ ở dưới.
@Composable
fun AppConfirmSheet(
    isVisible: Boolean,
    @DrawableRes iconRes: Int,
    title: String,
    description: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AppSheet(isVisible = isVisible, onDismissRequest = onDismiss) {
        AppSheetHeroHeader(title = title, description = description, badgeIconRes = iconRes)

        AppPrimaryButton(
            text = confirmLabel,
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            iconRes = iconRes,
        )

        AppTextButton(
            text = stringResource(R.string.action_cancel),
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        )
    }
}
