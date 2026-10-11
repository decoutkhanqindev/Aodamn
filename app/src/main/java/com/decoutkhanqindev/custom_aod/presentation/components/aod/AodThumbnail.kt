package com.decoutkhanqindev.custom_aod.presentation.components.aod

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.custom_aod.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape8dp

// AOD thu nhỏ: vẽ AodContent ở đúng cỡ màn hình rồi thu theo chiều cao nên giống hệt AOD khi bật; lớp phủ trên cùng chặn chạm để
// click rơi về khung bọc ngoài.
@Composable
fun AodThumbnail(
    state: AodState,
    height: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape8dp,
    borderColor: Color = NeutralVariant30,
) {
    val density = LocalDensity.current
    val windowSize = LocalWindowInfo.current.containerSize
    val screenWidth = with(density) { windowSize.width.coerceAtLeast(1).toDp() }
    val screenHeight = with(density) { windowSize.height.coerceAtLeast(1).toDp() }
    val scale = height / screenHeight

    Box(
        modifier = modifier
            .size(
                width = screenWidth * scale,
                height = height,
            )
            .clip(shape)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape,
            )
            .clearAndSetSemantics { },
        contentAlignment = Alignment.Center,
    ) {
        AodContent(
            state = state,
            onIntent = {},
            modifier = Modifier
                .requiredSize(
                    width = screenWidth,
                    height = screenHeight,
                )
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            isInteractive = false,
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .pointerInput(Unit) { },
        )
    }
}
