package com.decoutkhanqindev.aodamn.presentation.components.aod.block

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentStyle

// Khối đang là gradient: phần vẽ bằng style.color (chữ, icon, chấm) vẽ ra lớp riêng rồi phủ gradient SrcIn, giữ nguyên hình và độ trong,
// trải theo khung của chính phần đó. Đặt trước background trong chuỗi modifier để lớp gồm cả nền. Khối một màu thì không làm gì.
fun Modifier.aodTint(style: AodComponentStyle): Modifier = aodTint(style.gradient?.toBrush())

// Như trên cho chỗ nhận Brush thẳng (dòng trạng thái, chủ đề tự vẽ); null là một màu.
fun Modifier.aodTint(brush: Brush?): Modifier {
    if (brush == null) return this
    return graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.SrcIn)
        }
}
