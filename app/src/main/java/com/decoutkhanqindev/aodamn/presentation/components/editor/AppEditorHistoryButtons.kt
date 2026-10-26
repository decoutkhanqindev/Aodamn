package com.decoutkhanqindev.aodamn.presentation.components.editor

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
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.theme.Theme

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
                modifier = Modifier.size(26.dp),
            )
        }

        IconButton(onClick = onRedo, enabled = canRedo, modifier = Modifier.size(40.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_u_up_right),
                contentDescription = stringResource(R.string.customize_redo),
                modifier = Modifier.size(26.dp),
            )
        }

        IconButton(onClick = onReset, enabled = canReset, modifier = Modifier.size(40.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_counter_clockwise),
                contentDescription = stringResource(R.string.customize_reset),
                modifier = Modifier.size(26.dp),
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
