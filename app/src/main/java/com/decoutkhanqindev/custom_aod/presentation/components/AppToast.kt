package com.decoutkhanqindev.custom_aod.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.model.ToastUiModel
import com.decoutkhanqindev.custom_aod.presentation.theme.Black
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.EdgeGlowGradient
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.Red
import com.decoutkhanqindev.custom_aod.presentation.theme.RedLight

// Toast của app thay cho Toast hệ thống: Screen giữ SnackbarHostState và hiện bằng showSnackbar(ToastUiModel(...)).
@Composable
fun AppToastHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(hostState = hostState, modifier = modifier) { data ->
        AppToast(
            message = data.visuals.message,
            isError = (data.visuals as? ToastUiModel)?.isError == true,
        )
    }
}

// Viên thuốc nền Neutral12, viền sáng như icon app (lỗi: viền đỏ, icon cảnh báo).
@Composable
fun AppToast(
    message: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .background(color = Neutral12, shape = CircleShape)
            .border(width = 1.2.dp, brush = if (isError) SolidColor(Red) else EdgeGlowGradient, shape = CircleShape)
            .padding(start = 12.dp, end = 18.dp, top = 10.dp, bottom = 10.dp)
            .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(if (isError) R.drawable.ic_warning_circle_fill else R.drawable.ic_check_circle_fill),
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = if (isError) RedLight else Mint,
        )

        Text(
            text = message,
            color = GreyED,
            fontWeight = FontWeight.SemiBold,
            style = BodyMedium,
        )
    }
}

@Preview
@Composable
private fun AppToastPreview() {
    Box(modifier = Modifier.background(Black)) {
        AppToast(message = "Đã áp dụng chủ đề")
    }
}
