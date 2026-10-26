package com.decoutkhanqindev.aodamn.presentation.components.aod.block

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.theme.GreyB4

@Composable
fun AodBlock(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.width(296.dp),
        content = content,
    )
}

@Preview
@Composable
private fun AodBlockPreview() {
    AodBlock {
        Text(text = "Alice", color = GreyB4, fontSize = 14.sp)
    }
}
