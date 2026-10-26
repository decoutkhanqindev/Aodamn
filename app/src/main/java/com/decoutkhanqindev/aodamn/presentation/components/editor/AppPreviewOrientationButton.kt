package com.decoutkhanqindev.aodamn.presentation.components.editor

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.theme.Theme

@Composable
fun AppPreviewOrientationButton(
    isLandscape: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppEditorFab(
        iconRes = R.drawable.ic_device_rotate,
        contentDescription = stringResource(if (isLandscape) R.string.preview_show_portrait else R.string.preview_show_landscape),
        onClick = onClick,
        modifier = modifier,
        isActive = isLandscape,
    )
}

@Preview
@Composable
private fun AppPreviewOrientationButtonPreview() {
    Theme {
        AppPreviewOrientationButton(isLandscape = true, onClick = {})
    }
}
