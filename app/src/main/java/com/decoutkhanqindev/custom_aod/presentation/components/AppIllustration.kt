package com.decoutkhanqindev.custom_aod.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.presentation.theme.Black
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.Neutral12

// Hình minh hoạ của sheet / hộp thoại: điện thoại viền sáng như icon app (img_spot_phone, sinh bằng tools/brand/generate_illustrations.py,
// khung 120 x 96) và huy hiệu tròn theo ngữ cảnh ở góc dưới phải điện thoại. isOffline: bản xám (mất mạng).
@Composable
fun AppIllustration(
    @DrawableRes badgeIconRes: Int?,
    modifier: Modifier = Modifier,
    badgeColor: Color = Mint,
    isOffline: Boolean = false,
    width: Dp = 128.dp,
) {
    val unit = width / 120

    Box(modifier = modifier.size(width = width, height = unit * 96)) {
        Image(
            painter = painterResource(if (isOffline) R.drawable.img_spot_phone_offline else R.drawable.img_spot_phone),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )

        // Tâm huy hiệu ở (76, 64) của khung 120 x 96: sát góc dưới phải điện thoại, vòng ngoài màu nền sheet tách nó khỏi viền sáng.
        if (badgeIconRes != null) {
            Box(
                modifier = Modifier
                    .offset(x = unit * 62, y = unit * 50)
                    .size(unit * 28)
                    .background(color = Neutral12, shape = CircleShape)
                    .padding(unit * 2.5f)
                    .background(color = badgeColor, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(badgeIconRes),
                    contentDescription = null,
                    modifier = Modifier.size(unit * 14),
                    tint = Black,
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppIllustrationPreview() {
    Box(modifier = Modifier.background(Neutral12)) {
        AppIllustration(badgeIconRes = R.drawable.ic_shield_check_fill)
    }
}
