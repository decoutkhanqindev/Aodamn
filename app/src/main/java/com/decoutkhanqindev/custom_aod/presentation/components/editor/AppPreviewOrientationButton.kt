package com.decoutkhanqindev.custom_aod.presentation.components.editor

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme

// Nút ở thanh trên của màn sửa: đổi khung xem trước AOD giữa dọc và ngang (AodPreviewFrame); đang xem ngang thì icon tô Mint.
@Composable
fun AppPreviewOrientationButton(
    isLandscape: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.ic_device_rotate),
            contentDescription = stringResource(if (isLandscape) R.string.preview_show_portrait else R.string.preview_show_landscape),
            tint = if (isLandscape) Mint else LocalContentColor.current,
        )
    }
}

@Preview
@Composable
private fun AppPreviewOrientationButtonPreview() {
    Theme {
        AppPreviewOrientationButton(isLandscape = true, onClick = {})
    }
}
