package com.decoutkhanqindev.aodamn.presentation.components.aod

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState

// Khung xem trước AOD ở màn sửa (Tùy chỉnh, màn chủ đề), nằm sau các thanh. Dọc: AodContent vẽ trọn màn hình. Ngang: màn hình app vẫn dọc (máy
// khoá dọc, các thanh không xoay), chỉ AOD đổi sang bố cục ngang: vẽ ở cỡ đầy đủ của màn hình xoay ngang (rộng = chiều cao màn hình dọc, AodContent
// tự chọn bố cục ngang theo kích thước) rồi xoay 90° cho vừa khít màn hình dọc; ảnh nền xoay ngược lại nên vẫn đứng như màn dọc (AodBackdrop). User
// tự xoay máy sang ngang để xem đúng chiều (nhấn giữ khung xem trước để ẩn các thanh).
@Composable
fun AodPreviewFrame(
    state: AodState,
    isLandscape: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!isLandscape) {
        AodContent(state = state, onIntent = {}, modifier = modifier, isInteractive = false, backdropRotationZ = 0f)
        return
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .requiredSize(width = maxHeight, height = maxWidth)
                .graphicsLayer { rotationZ = 90f },
        ) {
            AodContent(
                state = state,
                onIntent = {},
                modifier = Modifier.fillMaxSize(),
                isInteractive = false,
                backdropRotationZ = -90f,
            )
        }
    }
}
