package com.decoutkhanqindev.custom_aod.presentation.components.editor

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme

// Hoàn tác, làm lại, đặt lại ở thanh trên của màn sửa (Tùy chỉnh, màn chủ đề), cạnh nút quay lại. Nút 40dp để thanh trên của
// onboarding (thêm hướng dẫn, Bỏ qua, Áp dụng) vẫn vừa màn 393dp.
@Composable
fun AppEditorHistoryButtons(
    canUndo: Boolean,
    canRedo: Boolean,
    canReset: Boolean,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onUndo, enabled = canUndo, modifier = Modifier.size(40.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_u_up_left),
                contentDescription = stringResource(R.string.customize_undo),
            )
        }

        IconButton(onClick = onRedo, enabled = canRedo, modifier = Modifier.size(40.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_u_up_right),
                contentDescription = stringResource(R.string.customize_redo),
            )
        }

        IconButton(onClick = onReset, enabled = canReset, modifier = Modifier.size(40.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_counter_clockwise),
                contentDescription = stringResource(R.string.customize_reset),
            )
        }
    }
}

@Preview
@Composable
private fun AppEditorHistoryButtonsPreview() {
    Theme {
        AppEditorHistoryButtons(
            canUndo = true,
            canRedo = false,
            canReset = true,
            onUndo = {},
            onRedo = {},
            onReset = {},
        )
    }
}
