package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.custom_aod.presentation.theme.AccentGradient
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey3A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyB4
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape20dp

// Bộ khung của các khối mặc định trên AOD: không tô nền để màn ít sáng, chỉ nét mảnh. Thẻ (thông báo, sự kiện, ghi nhớ, nhạc) viền
// gradient nhận diện của app, cùng bề ngang để xếp chồng thẳng hàng; viên (thời tiết, pin) và vòng icon viền xám.
@Composable
fun AodCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .width(296.dp)
            .border(width = 1.dp, brush = AccentGradient, shape = RoundedCornerShape20dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        content = content,
    )
}

@Composable
fun AodPill(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .border(width = 1.dp, color = Grey3A, shape = CircleShape)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

// isHighlighted: viền gradient (vd app có thông báo mới nhất), còn lại viền xám.
@Composable
fun AodIconRing(
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    isHighlighted: Boolean = false,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .size(size)
            .then(
                if (isHighlighted) {
                    Modifier.border(width = 1.dp, brush = AccentGradient, shape = CircleShape)
                } else {
                    Modifier.border(width = 1.dp, color = Grey3A, shape = CircleShape)
                },
            ),
        contentAlignment = Alignment.Center,
        content = content,
    )
}

@Preview
@Composable
private fun AodCardPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AodCard {
            Text(text = "Alice", color = GreyB4, fontSize = 14.sp)
        }

        AodPill {
            Text(text = "72%", color = GreyB4, fontSize = 13.sp)
        }

        AodIconRing(isHighlighted = true) {
            Text(text = "+2", color = GreyB4, fontSize = 11.sp)
        }
    }
}
