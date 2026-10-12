package com.decoutkhanqindev.custom_aod.presentation.components.editor

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerTopShape16dp
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleMedium

// Tấm chỉnh trượt lên trên thanh tab của màn sửa (Tùy chỉnh, sửa chủ đề): tiêu đề tab giữa, action của tab (vd AppEditorResetButton)
// bên phải tiêu đề, nội dung cuộn. Đổi tab thì mờ chéo, đóng thì trượt xuống mà vẫn giữ nội dung tab cuối cho tới hết animation.
@Composable
fun <T : Any> AppEditorPanel(
    selected: T?,
    title: @Composable (T) -> String,
    initial: T,
    action: @Composable (T) -> Unit = {},
    content: @Composable ColumnScope.(T) -> Unit,
) {
    var last by remember { mutableStateOf(selected ?: initial) }

    SideEffect(selected) {
        selected?.let { tab -> last = tab }
    }

    AnimatedVisibility(
        visible = selected != null,
        enter = slideInVertically(
            animationSpec = spring(
                dampingRatio = 0.9f,
                stiffness = 700f,
                visibilityThreshold = IntOffset.VisibilityThreshold,
            ),
            initialOffsetY = { height -> height },
        ),
        exit = slideOutVertically(
            animationSpec = spring(
                dampingRatio = 1f,
                stiffness = 3800f,
                visibilityThreshold = IntOffset.VisibilityThreshold,
            ),
            targetOffsetY = { height -> height },
        ),
        label = "AppEditorPanel",
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerTopShape16dp,
            color = Neutral12,
            contentColor = GreyED,
        ) {
            AnimatedContent(
                targetState = selected ?: last,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "AppEditorPanelContent",
            ) { tab ->
                Column(modifier = Modifier.heightIn(max = 360.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                            .heightIn(min = 48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = title(tab),
                            modifier = Modifier.padding(horizontal = 48.dp),
                            textAlign = TextAlign.Center,
                            style = TitleMedium,
                        )

                        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                            action(tab)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 8.dp),
                    ) {
                        content(tab)
                    }
                }
            }
        }
    }
}

// Đặt lại riêng tab đang mở về mặc định, ở thanh tiêu đề của AppEditorPanel; chỉ hiện khi tab đã khác mặc định (nút Đặt lại trên thanh
// công cụ đặt lại tất cả).
@Composable
fun AppEditorResetButton(
    isVisible: Boolean,
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_counter_clockwise),
                contentDescription = stringResource(R.string.customize_style_reset),
            )
        }
    }
}
