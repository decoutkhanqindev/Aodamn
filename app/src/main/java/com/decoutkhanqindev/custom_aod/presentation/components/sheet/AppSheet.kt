package com.decoutkhanqindev.custom_aod.presentation.components.sheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.components.AppIllustration
import com.decoutkhanqindev.custom_aod.presentation.theme.BlackAlpha70
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.BodySmall
import com.decoutkhanqindev.custom_aod.presentation.theme.EdgeGlowGradient
import com.decoutkhanqindev.custom_aod.presentation.theme.Grey9A
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.MintAlpha12
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerShape12dp
import com.decoutkhanqindev.custom_aod.presentation.theme.RoundedCornerTopShape24dp
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleLarge
import com.decoutkhanqindev.custom_aod.presentation.theme.TitleMedium

// Bottom sheet của app thay cho sheet / hộp thoại Material: nền Neutral12, thanh kéo là dải màu mint → xanh → tím như icon app. Không vẽ
// gì qua modifier của ModalBottomSheet: modifier nằm ngoài offset của sheet nên hình vẽ rơi lên mép trên màn hình. Hiện khi value khác
// null và giữ value cũ trong lúc trượt xuống (nội dung không biến mất giữa animation). Kéo / chạm nền / Back gọi onDismissRequest;
// isDragEnabled = false cho sheet có thao tác kéo bên trong (bảng vẽ, bánh xe chọn giờ).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> AppSheet(
    value: T?,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    isDragEnabled: Boolean = true,
    content: @Composable ColumnScope.(T) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var shown by remember { mutableStateOf(value) }

    LaunchedEffect(value) {
        if (value != null) {
            shown = value
        } else if (shown != null) {
            sheetState.hide()
            shown = null
        }
    }

    shown?.let { current ->
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            sheetState = sheetState,
            sheetGesturesEnabled = isDragEnabled,
            shape = RoundedCornerTopShape24dp,
            containerColor = Neutral12,
            contentColor = GreyED,
            scrimColor = BlackAlpha70,
            dragHandle = { AppSheetHandle() },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content(current)
            }
        }
    }
}

@Composable
fun AppSheet(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    isDragEnabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    AppSheet(
        value = if (isVisible) Unit else null,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        isDragEnabled = isDragEnabled,
    ) {
        content()
    }
}

// Đầu sheet quyết định (xác nhận, quyền): hình minh hoạ với huy hiệu, tiêu đề và mô tả căn giữa.
@Composable
fun AppSheetHeroHeader(
    title: String,
    description: String,
    @DrawableRes badgeIconRes: Int?,
    modifier: Modifier = Modifier,
    badgeColor: Color = Mint,
    isOffline: Boolean = false,
    illustrationWidth: Dp = 128.dp,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppIllustration(
            badgeIconRes = badgeIconRes,
            badgeColor = badgeColor,
            isOffline = isOffline,
            width = illustrationWidth,
        )

        Text(
            text = title,
            modifier = Modifier
                .padding(top = 4.dp)
                .semantics { heading() },
            color = GreyED,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = TitleLarge,
        )

        Text(
            text = description,
            modifier = Modifier.padding(top = 6.dp),
            color = Grey9A,
            textAlign = TextAlign.Center,
            style = BodyMedium,
        )
    }
}

// Đầu sheet nhập / chọn: ô icon, tiêu đề + một dòng giải thích. Không có nút X: sheet đóng bằng nút chữ Hủy ở cuối (AppTextButton) như các sheet
// quyết định, hoặc vuốt xuống / chạm nền / Back.
@Composable
fun AppSheetToolHeader(
    @DrawableRes iconRes: Int,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(color = MintAlpha12, shape = RoundedCornerShape12dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Mint,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
        ) {
            Text(
                text = title,
                modifier = Modifier.semantics { heading() },
                color = GreyED,
                fontWeight = FontWeight.Bold,
                style = TitleMedium,
            )

            if (description != null) {
                Text(
                    text = description,
                    modifier = Modifier.padding(top = 2.dp),
                    color = Grey9A,
                    style = BodySmall,
                )
            }
        }
    }
}

@Composable
private fun AppSheetHandle() {
    Box(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .size(width = 36.dp, height = 4.dp)
            .background(brush = EdgeGlowGradient, shape = CircleShape, alpha = 0.75f),
    )
}

@Preview(widthDp = 360)
@Composable
private fun AppSheetHeaderPreview() {
    Column(
        modifier = Modifier
            .background(Neutral12)
            .padding(20.dp),
    ) {
        AppSheetToolHeader(iconRes = R.drawable.ic_note_pencil, title = "Ghi nhớ", description = "Hiện dưới đồng hồ")

        AppSheetHeroHeader(
            title = "Đặt lại chủ đề mặc định?",
            description = "AOD sẽ dùng lại Cổ điển.",
            badgeIconRes = R.drawable.ic_arrow_counter_clockwise,
        )
    }
}
