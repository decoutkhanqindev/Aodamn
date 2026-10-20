package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.halfmoon

import android.graphics.Typeface
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.halfmoon.HalfMoonThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlin.math.ceil
import kotlin.math.min

// Nửa mặt số Bán nguyệt sát mép trái vùng được cho, giữa chiều cao. Chạy theo giờ thật (isLive: AOD thật, và khung xem trước vì Bán nguyệt bật
// isLiveInPreview) mỗi khung hình như app mẫu: vòng giây quay một vòng mỗi phút, vòng phút trôi liên tục, số đứng thẳng chạy theo vòng. Luôn ở
// trạng thái thức của app mẫu (viền viên phút kéo tới mép mặt số, số giây rõ): user bỏ trạng thái nghỉ để vòng giây luôn hiện.
// Hai vòng vạch vẽ một lần vào GraphicsLayer (chỉ vẽ lại khi đổi cỡ / màu) rồi chỉ xoay; số và viên đặt lại mỗi khung hình trong pha vẽ nên
// không recompose.
@Composable
fun AodHalfMoonDial(
    nowMillis: Long,
    isLive: Boolean,
    radiusFraction: Float,
    config: AodThemeConfig,
    paints: HalfMoonPaints,
    modifier: Modifier = Modifier,
) {
    val hourColor = config[HalfMoonThemeSpec.HourColor]
    val minuteColor = config[HalfMoonThemeSpec.MinuteColor]
    val ringColor = config[HalfMoonThemeSpec.RingColor]
    val dialColor = config[HalfMoonThemeSpec.DialColor]
    val is24Hour = is24HourFormat()
    val description = rememberZonedDateTime(nowMillis).format(rememberTimeFormatter())
    val clockMillis = remember { mutableLongStateOf(System.currentTimeMillis()) }

    if (isLive) {
        LaunchedEffect(Unit) {
            while (true) {
                withFrameMillis { clockMillis.longValue = System.currentTimeMillis() }
            }
        }
    }

    Spacer(
        modifier = modifier
            .fillMaxSize()
            .clearAndSetSemantics { contentDescription = description }
            .drawWithCache {
                val radius = min(size.width, size.height) * radiusFraction
                val center = Offset(radius * (1f - HalfMoonMetrics.CENTER_SHIFT), size.height / 2f)
                val ringSize = ceil(radius * 2f).toInt().coerceAtLeast(1).let { side -> IntSize(side, side) }
                val secondTicks = obtainGraphicsLayer().apply {
                    record(size = ringSize) {
                        drawHalfMoonTicks(
                            radius = radius,
                            major = HalfMoonMetrics.SECOND_TICK_MAJOR,
                            minor = HalfMoonMetrics.SECOND_TICK_MINOR,
                            end = HalfMoonMetrics.SECOND_TICK_END,
                            color = dialColor,
                            width = HalfMoonMetrics.TICK_WIDTH_PX,
                        )
                    }
                }
                val minuteTicks = obtainGraphicsLayer().apply {
                    record(size = ringSize) {
                        drawHalfMoonTicks(
                            radius = radius,
                            major = HalfMoonMetrics.MINUTE_TICK_MAJOR,
                            minor = HalfMoonMetrics.MINUTE_TICK_MINOR,
                            end = HalfMoonMetrics.MINUTE_TICK_END,
                            color = dialColor,
                            width = HalfMoonMetrics.TICK_WIDTH_PX,
                        )
                    }
                }
                val vignette = Brush.radialGradient(
                    HalfMoonMetrics.VIGNETTE_START to Color.Transparent,
                    1f to Black,
                    center = Offset(center.x + HalfMoonMetrics.VIGNETTE_SHIFT * radius, center.y),
                    radius = HalfMoonMetrics.VIGNETTE_RADIUS * radius,
                )
                val ringStroke = Stroke(width = HalfMoonMetrics.RING_WIDTH_PX)
                val minuteSize = HalfMoonMetrics.MINUTE_SIZE * radius
                val pillX = center.x + HalfMoonMetrics.PILL_OFFSET * radius
                val pill = Path()

                onDrawBehind {
                    val time = HalfMoonTime(if (isLive) clockMillis.longValue else nowMillis)
                    val textHeight = paints.textHeight(time.minuteText, minuteSize)
                    val halfHeight = HalfMoonMetrics.PILL_HALF_HEIGHT * textHeight
                    val halfWidth = HalfMoonMetrics.PILL_HALF_WIDTH * textHeight

                    pill.reset()
                    pill.addRoundRect(
                        RoundRect(
                            left = pillX - halfHeight,
                            top = center.y - halfHeight,
                            right = pillX + halfHeight,
                            bottom = center.y + halfHeight,
                            cornerRadius = CornerRadius(HalfMoonMetrics.PILL_CLIP_CORNER * halfHeight),
                        ),
                    )

                    drawCircle(color = Black, radius = radius, center = center, alpha = HalfMoonMetrics.DISC_ALPHA)
                    clipPath(path = pill, clipOp = ClipOp.Difference) {
                        drawRing(secondTicks, ringSize, center, time.seconds * 6f)
                        drawRing(minuteTicks, ringSize, center, time.minutes * 6f)
                        drawHalfMoonLabels(
                            paints = paints,
                            center = center,
                            value = time.seconds,
                            radius = HalfMoonMetrics.SECOND_LABEL_RADIUS * radius,
                            size = HalfMoonMetrics.SECOND_LABEL_SIZE * radius,
                            color = dialColor,
                        )
                        drawHalfMoonLabels(
                            paints = paints,
                            center = center,
                            value = time.minutes,
                            radius = HalfMoonMetrics.MINUTE_LABEL_RADIUS * radius,
                            size = HalfMoonMetrics.MINUTE_LABEL_SIZE * radius,
                            color = dialColor,
                        )
                    }

                    paints.drawLabel(
                        scope = this,
                        label = time.hourText(is24Hour),
                        center = Offset(center.x + HalfMoonMetrics.HOUR_OFFSET * radius, center.y),
                        size = HalfMoonMetrics.HOUR_SIZE * radius,
                        color = hourColor,
                    )
                    paints.drawLabel(this, time.minuteText, Offset(pillX, center.y), minuteSize, minuteColor)

                    val ringLeft = pillX - halfWidth
                    val ringRight = center.x + radius - HalfMoonMetrics.RING_WIDTH_PX / 2f
                    drawRoundRect(
                        color = ringColor,
                        topLeft = Offset(ringLeft, center.y - halfHeight),
                        size = Size(ringRight - ringLeft, halfHeight * 2f),
                        cornerRadius = CornerRadius(min(halfWidth, halfHeight)),
                        style = ringStroke,
                    )
                    drawCircle(brush = vignette, radius = radius, center = center)
                }
            },
    )
}

@Composable
fun rememberHalfMoonPaints(): HalfMoonPaints {
    val typeface = LocalFontFamilyResolver.current
        .resolve(fontFamily = HalfMoonThemeSpec.Font, fontWeight = FontWeight.Normal)
        .value as? Typeface ?: Typeface.DEFAULT
    return remember(typeface) { HalfMoonPaints(typeface) }
}

// Vòng vạch xoay cùng chiều kim đồng hồ đúng bằng phần đã trôi qua: vạch của giá trị hiện tại luôn nằm ngang bên phải tâm.
private fun DrawScope.drawRing(ring: GraphicsLayer, ringSize: IntSize, pivot: Offset, degrees: Float) {
    rotate(degrees = degrees, pivot = pivot) {
        translate(left = pivot.x - ringSize.width / 2f, top = pivot.y - ringSize.height / 2f) {
            drawLayer(ring)
        }
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodHalfMoonDialPreview() {
    AodHalfMoonDial(
        nowMillis = System.currentTimeMillis(),
        isLive = false,
        radiusFraction = HalfMoonMetrics.PORTRAIT_RADIUS,
        config = AodThemeConfig(),
        paints = rememberHalfMoonPaints(),
    )
}
