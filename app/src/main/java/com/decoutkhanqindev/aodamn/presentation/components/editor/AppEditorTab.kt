package com.decoutkhanqindev.aodamn.presentation.components.editor

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
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.onClick
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.Mint
import com.decoutkhanqindev.aodamn.presentation.theme.MintAlpha12
import com.decoutkhanqindev.aodamn.presentation.theme.RoundedCornerShape16dp
import com.decoutkhanqindev.aodamn.presentation.theme.Theme
import com.decoutkhanqindev.aodamn.presentation.theme.TitleMedium

// Một tab của thanh tab thấp (Tùy chỉnh, sửa chủ đề, thanh tab Main): chỉ có icon để thanh không che AOD xem trước, tab đang chọn nền mint
// nhạt, icon đặc (như thanh tab Main) và hiện thêm tên để biết đang ở đâu. TalkBack luôn đọc tên.
@Composable
fun AppEditorTab(
    @DrawableRes iconRes: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes selectedIconRes: Int = filledIconOf(iconRes),
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
            .height(48.dp)
            .widthIn(min = 48.dp)
            .onClick(shape = RoundedCornerShape16dp, action = onClick)
            .drawBehind {
                drawRoundRect(color = containerColor, cornerRadius = CornerRadius(16.dp.toPx()))
            }
            .semantics {
                role = Role.Tab
                selected = isSelected
                contentDescription = label
            }
            .padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(if (isSelected) selectedIconRes else iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
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
                    .padding(start = 8.dp)
                    .clearAndSetSemantics { },
                color = contentColor,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                style = TitleMedium,
            )
        }
    }
}

// Bản fill của icon tab ở Tùy chỉnh và màn chủ đề (tools/icons/icons.txt); icon chưa có bản fill thì giữ nguyên.
@DrawableRes
private fun filledIconOf(@DrawableRes iconRes: Int): Int = when (iconRes) {
    R.drawable.ic_clock -> R.drawable.ic_clock_fill
    R.drawable.ic_calendar_blank -> R.drawable.ic_calendar_blank_fill
    R.drawable.ic_battery_full -> R.drawable.ic_battery_full_fill
    R.drawable.ic_image -> R.drawable.ic_image_fill
    R.drawable.ic_paint_brush -> R.drawable.ic_paint_brush_fill
    R.drawable.ic_note_pencil -> R.drawable.ic_note_pencil_fill
    R.drawable.ic_bell -> R.drawable.ic_bell_fill
    R.drawable.ic_calendar_check -> R.drawable.ic_calendar_check_fill
    R.drawable.ic_cloud_sun -> R.drawable.ic_cloud_sun_fill
    R.drawable.ic_music_notes -> R.drawable.ic_music_notes_fill
    R.drawable.ic_sparkle -> R.drawable.ic_sparkle_fill
    R.drawable.ic_timer -> R.drawable.ic_timer_fill
    R.drawable.ic_text_aa -> R.drawable.ic_text_aa_fill
    R.drawable.ic_magic_wand -> R.drawable.ic_magic_wand_fill
    R.drawable.ic_line_vertical -> R.drawable.ic_line_vertical_fill
    else -> iconRes
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
