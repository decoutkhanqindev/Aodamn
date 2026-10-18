package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bezel

import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bezel.BezelThemeSpec

// Mặt số Vành phủ cả vùng được cho: vạch + số (BezelFace, tính lại khi đổi cỡ hay kiểu vạch) và hai kim. Kim quay 400ms theo chiều ngắn nhất khi
// sang phút mới; góc kim chỉ đọc lúc vẽ nên quay kim không recompose.
@Composable
fun AodBezelDial(
    nowMillis: Long,
    config: AodThemeConfig,
    paints: BezelPaints,
    modifier: Modifier = Modifier,
) {
    val ticks = config[BezelThemeSpec.Ticks]
    val handColor = config[BezelThemeSpec.HandColor]
    val numberColor = config[BezelThemeSpec.NumberColor]
    val now = rememberZonedDateTime(nowMillis)
    val description = now.format(rememberTimeFormatter())
    val minuteDegrees = rememberHandDegrees(degrees = now.minute * 6f)
    val hourDegrees = rememberHandDegrees(degrees = (now.hour % 12 + now.minute / 60f) * 30f)

    Spacer(
        modifier = modifier
            .fillMaxSize()
            .clearAndSetSemantics { contentDescription = description }
            .drawWithCache {
                val face = BezelFace(size = size, ticks = ticks)
                onDrawBehind {
                    drawBezelFace(face = face, paints = paints, handColor = handColor, numberColor = numberColor)
                    drawBezelHands(
                        face = face,
                        paints = paints,
                        minuteDegrees = minuteDegrees.value,
                        hourDegrees = hourDegrees.value,
                        handColor = handColor,
                        accentColor = numberColor,
                    )
                }
            },
    )
}

@Composable
fun rememberBezelPaints(): BezelPaints {
    val typeface = LocalFontFamilyResolver.current
        .resolve(fontFamily = BezelThemeSpec.NumberFont, fontWeight = FontWeight.SemiBold)
        .value as? Typeface ?: Typeface.DEFAULT_BOLD
    return remember(typeface) { BezelPaints(typeface) }
}

// Góc kim cộng dồn theo chiều ngắn nhất: 59 → 0 phút quay tiếp 6°, không quay ngược cả vòng.
@Composable
private fun rememberHandDegrees(degrees: Float): State<Float> {
    val angle = remember { Animatable(degrees) }

    LaunchedEffect(degrees) {
        val delta = ((degrees - angle.value) % 360f + 540f) % 360f - 180f
        angle.animateTo(targetValue = angle.value + delta, animationSpec = tween(durationMillis = BezelMetrics.HAND_MILLIS))
    }

    return angle.asState()
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodBezelDialPreview() {
    AodBezelDial(
        nowMillis = System.currentTimeMillis(),
        config = AodThemeConfig(),
        paints = rememberBezelPaints(),
    )
}
