package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4

// Khối nhiều dòng trên AOD (thông báo mới nhất, sự kiện, ghi nhớ, nhạc): không viền, không nền để màn ít sáng. Cùng bề ngang để các khối
// xếp chồng thẳng hàng; cỡ chữ của khối đổi trong khối chứ không đổi bề ngang, để cỡ lớn không tràn màn hình.
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
