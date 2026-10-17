package com.decoutkhanqindev.custom_aod.presentation.components.editor

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.onClick
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.LabelLarge
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.MintAlpha12
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme

// Một tab của thanh tab thấp (Tùy chỉnh, sửa chủ đề, thanh tab Main): chỉ có icon để thanh không che AOD xem trước, tab đang chọn nền mint
// nhạt và hiện thêm tên để biết đang ở đâu. TalkBack luôn đọc tên.
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

    Row(
        modifier = modifier
            .height(40.dp)
            .widthIn(min = 44.dp)
            .onClick(shape = RoundedCornerShape12dp, action = onClick)
            .drawBehind {
                drawRoundRect(color = containerColor, cornerRadius = CornerRadius(12.dp.toPx()))
            }
            .semantics {
                role = Role.Tab
                selected = isSelected
                contentDescription = label
            }
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = contentColor,
        )

        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally(),
        ) {
            Text(
                text = label,
                modifier = Modifier
                    .padding(start = 6.dp)
                    .clearAndSetSemantics { },
                color = contentColor,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                style = LabelLarge,
            )
        }
    }
}

@Preview
@Composable
private fun AppEditorTabPreview() {
    Theme {
        Row {
            AppEditorTab(iconRes = R.drawable.ic_clock, label = stringResource(R.string.customize_tab_clock), isSelected = true, onClick = {})
            AppEditorTab(iconRes = R.drawable.ic_calendar_blank, label = stringResource(R.string.customize_tab_date), isSelected = false, onClick = {})
        }
    }
}
