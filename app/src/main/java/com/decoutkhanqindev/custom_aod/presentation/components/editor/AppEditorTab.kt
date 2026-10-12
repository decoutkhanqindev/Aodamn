package com.decoutkhanqindev.custom_aod.presentation.components.editor

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.LabelMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.MintAlpha12
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape12dp

// Một tab của thanh tab màn sửa (Tùy chỉnh, sửa chủ đề): icon trên nhãn, tab đang chọn nền mint nhạt.
@Composable
fun AppEditorTab(
    @DrawableRes iconRes: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Mint else Grey9A,
        label = "AppEditorTabContent",
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MintAlpha12 else Color.Transparent,
        label = "AppEditorTabContainer",
    )

    Column(
        modifier = modifier
            .widthIn(min = 64.dp)
            .onClick(shape = RoundedCornerShape12dp, action = onClick)
            .drawBehind {
                drawRoundRect(color = containerColor, cornerRadius = CornerRadius(12.dp.toPx()))
            }
            .semantics {
                role = Role.Tab
                selected = isSelected
            }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = contentColor,
        )

        Text(
            text = label,
            modifier = Modifier.padding(top = 4.dp),
            color = contentColor,
            maxLines = 1,
            style = LabelMedium,
        )
    }
}
