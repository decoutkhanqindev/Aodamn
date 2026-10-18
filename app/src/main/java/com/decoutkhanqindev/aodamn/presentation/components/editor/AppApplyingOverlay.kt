package com.decoutkhanqindev.aodamn.presentation.components.editor

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.AppLottie
import com.decoutkhanqindev.aodamn.presentation.theme.BlackAlpha80
import com.decoutkhanqindev.aodamn.presentation.theme.BodyLarge
import com.decoutkhanqindev.aodamn.presentation.theme.EdgeGlowGradient
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Neutral17

// Lớp phủ chặn chạm lúc đang ghi (Áp dụng ở Tùy chỉnh, Lưu / Áp dụng ở màn chủ đề): animation viền sáng của icon app và một vệt sáng chạy
// qua lại trên thanh mảnh thay vòng xoay Material; Back do màn tự chặn.
@Composable
fun AppApplyingOverlay(
    text: String,
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "AppApplyingOverlay")
    val sweep = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "AppApplyingOverlaySweep",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BlackAlpha80)
            .pointerInput(Unit) { detectTapGestures { } },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppLottie(
                resId = R.raw.lottie_device_edge_light,
                modifier = Modifier.size(180.dp),
            )

            Text(
                text = text,
                color = GreyED,
                fontWeight = FontWeight.SemiBold,
                style = BodyLarge,
            )

            Box(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .size(width = 128.dp, height = 3.dp)
                    .clip(CircleShape)
                    .background(Neutral17)
                    .drawWithContent {
                        val segment = size.width * 0.4f
                        drawRoundRect(
                            brush = EdgeGlowGradient,
                            topLeft = Offset((size.width - segment) * sweep.value, 0f),
                            size = Size(segment, size.height),
                            cornerRadius = CornerRadius(size.height / 2),
                        )
                    },
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 640)
@Composable
private fun AppApplyingOverlayPreview() {
    AppApplyingOverlay(text = "Đang áp dụng…")
}
