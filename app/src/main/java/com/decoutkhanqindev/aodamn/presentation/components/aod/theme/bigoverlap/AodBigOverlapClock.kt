package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.bigoverlap

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap.BigOverlapThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlin.math.min

// Bốn chữ số Chồng số giữa vùng được cho, cỡ theo cạnh ngắn của vùng như BigPixel2Clock: vẽ lần lượt giờ chục, giờ đơn vị, phút chục, phút đơn vị,
// số nào cũng bị khoét theo hình các số vẽ sau. Kiểu đặc: nét đen dày quanh số (nửa ngoài thành khe đen giữa hai số) rồi tô màu; kiểu viền: nét màu
// mảnh, ruột trong (khoét thật nên ảnh nền vẫn thấy qua ruột số). Đổi đặc ↔ viền chạy 500ms như app mẫu, độ lấn trượt theo (app mẫu nhảy); đổi
// góc vuông ↔ bo tròn mờ chéo (app mẫu nhảy). Mỗi số tô gradient theo khung của chính nó.
@Composable
fun AodBigOverlapClock(
    nowMillis: Long,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val style = config[BigOverlapThemeSpec.Style]
    val scale = config[BigOverlapThemeSpec.Size] / 100f
    val outline = animateFloatAsState(
        targetValue = if (style.isOutline) 1f else 0f,
        animationSpec = tween(durationMillis = BigOverlapMetrics.STYLE_MILLIS),
        label = "BigOverlapOutline",
    )
    val time = rememberZonedDateTime(nowMillis)
    val hours = time.format(rememberFormatter(if (is24HourFormat()) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h))
    val minutes = time.format(rememberFormatter(R.string.aod_minute_pattern))
    val description = time.format(rememberTimeFormatter())
    val sharpPaint = rememberBigOverlapPaint(BigOverlapThemeSpec.DigitFont)
    val roundedPaint = rememberBigOverlapPaint(BigOverlapThemeSpec.RoundedDigitFont)

    Crossfade(
        targetState = style.isRounded,
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
        animationSpec = tween(durationMillis = BigOverlapMetrics.STYLE_MILLIS),
        label = "BigOverlapRounded",
    ) { isRounded ->
        BigOverlapDigits(
            text = hours + minutes,
            paint = if (isRounded) roundedPaint else sharpPaint,
            scale = scale,
            outline = { outline.value },
            config = config,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun BigOverlapDigits(
    text: String,
    paint: Paint,
    scale: Float,
    outline: () -> Float,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val colors = listOf(
        BigOverlapThemeSpec.HourFirstColor,
        BigOverlapThemeSpec.HourSecondColor,
        BigOverlapThemeSpec.MinuteFirstColor,
        BigOverlapThemeSpec.MinuteSecondColor,
    )

    Spacer(
        modifier = modifier
            .drawWithCache {
                val textSize = BigOverlapMetrics.TEXT_SIZE * min(size.width, size.height) * scale
                val fraction = outline()
                paint.textSize = textSize
                val digits = paint.bigOverlapDigits(text = text, center = size.center, outline = fraction)
                val shapes = digits.map { digit -> digit.path.asComposePath() }
                val covers = digits.indices.map { index -> digits.coverAbove(index)?.asComposePath() }
                val stroke = Stroke(width = BigOverlapMetrics.STROKE * textSize / (1f + BigOverlapMetrics.OUTLINE_THINNING * fraction))
                val strokeBrushes = digits.mapIndexed { index, digit ->
                    config.digitBrushOf(colors[index], digit.bounds.toComposeRect()) { color -> lerp(color, Black, 1f - fraction) }
                }
                val fillBrushes = digits.mapIndexed { index, digit ->
                    config.digitBrushOf(colors[index], digit.bounds.toComposeRect()) { color -> color.copy(alpha = color.alpha * (1f - fraction)) }
                }

                onDrawBehind {
                    shapes.forEachIndexed { index, shape ->
                        val cover = covers[index]
                        if (cover == null) {
                            drawPath(shape, strokeBrushes[index], style = stroke)
                            drawPath(shape, fillBrushes[index])
                        } else {
                            clipPath(cover, clipOp = ClipOp.Difference) {
                                drawPath(shape, strokeBrushes[index], style = stroke)
                                drawPath(shape, fillBrushes[index])
                            }
                        }
                    }
                }
            },
    )
}

// Bút chữ số bằng Paint của Android (lấy hình chữ số để khoét); Typeface là file TTF tĩnh Google Sans Flex, không đặt trục (máy Xiaomi thay font
// khi có trục).
@Composable
private fun rememberBigOverlapPaint(fontFamily: FontFamily): Paint {
    val typeface = LocalFontFamilyResolver.current
        .resolve(fontFamily = fontFamily, fontWeight = BigOverlapThemeSpec.DigitWeight)
        .value as? Typeface ?: Typeface.DEFAULT_BOLD
    return remember(typeface) {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.typeface = typeface
        }
    }
}

private fun android.graphics.RectF.toComposeRect(): Rect = Rect(left = left, top = top, right = right, bottom = bottom)

@Preview(widthDp = 393, heightDp = 470)
@Composable
private fun AodBigOverlapClockPreview() {
    AodBigOverlapClock(
        nowMillis = System.currentTimeMillis(),
        config = AodThemeConfig(),
    )
}
