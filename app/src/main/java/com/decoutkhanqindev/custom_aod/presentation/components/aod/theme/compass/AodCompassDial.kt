package com.decoutkhanqindev.custom_aod.presentation.components.aod.theme.compass

import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.decoutkhanqindev.custom_aod.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.custom_aod.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.compass.AodCompassStyleValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.compass.CompassThemeSpec
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.min

// Mặt số La bàn giữa vùng được cho: đường chỉ cố định, các vòng số xoay về đường chỉ, hai vòng tròn mảnh. Chạy theo giờ thật (isLive: AOD thật, và
// khung xem trước vì La bàn bật isLiveInPreview) như app mẫu: kiểu có vòng giây cập nhật mỗi khung hình (vòng giây quay một vòng mỗi phút, vòng phút
// và giờ trôi liên tục); kiểu không vòng giây cập nhật đầu mỗi phút, vòng phút và giờ xoay mượt tới chỗ mới (app mẫu nhảy). Đổi kiểu thì vòng giây mờ
// dần và đường chỉ lùi ra 0,28u trong 300ms như app mẫu. Mỗi vòng vẽ một lần vào GraphicsLayer (chỉ vẽ lại khi đổi cỡ / màu), mỗi khung hình chỉ xoay
// layer nên không recompose, không vẽ lại chữ.
@Composable
fun AodCompassDial(
    nowMillis: Long,
    isLive: Boolean,
    config: AodThemeConfig,
    paints: CompassPaints,
    modifier: Modifier = Modifier,
) {
    val hasSeconds = config[CompassThemeSpec.Style] == AodCompassStyleValue.SECONDS
    val sizePercent = config[CompassThemeSpec.Size]
    val hourColor = config[CompassThemeSpec.HourColor]
    val halfHourColor = config[CompassThemeSpec.HalfHourColor]
    val minuteColor = config[CompassThemeSpec.MinuteColor]
    val secondColor = config[CompassThemeSpec.SecondColor]
    val description = rememberZonedDateTime(nowMillis).format(rememberTimeFormatter())
    val clockMillis = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val secondsFraction = animateFloatAsState(
        targetValue = if (hasSeconds) 1f else 0f,
        animationSpec = tween(durationMillis = CompassMetrics.STYLE_MILLIS),
        label = "CompassSeconds",
    )

    if (isLive) {
        LaunchedEffect(hasSeconds) {
            while (true) {
                if (hasSeconds) {
                    withFrameMillis { clockMillis.longValue = System.currentTimeMillis() }
                } else {
                    val now = System.currentTimeMillis()
                    clockMillis.longValue = now
                    delay(CompassTime.millisToNextMinute(now))
                }
            }
        }
    }

    // Kiểu không vòng giây: góc đích là giờ lúc cập nhật (đầu mỗi phút, như setTime của app mẫu), đọc trong composition nên mỗi phút recompose
    // một lần; góc vẽ đi theo bằng animation.
    val stepMillis = if (hasSeconds) 0L else if (isLive) clockMillis.longValue else nowMillis
    val stepped = remember { CompassTime(if (isLive) System.currentTimeMillis() else nowMillis) }
    val minuteDegrees = remember { Animatable(stepped.minutes * 6f) }
    val hourDegrees = remember { Animatable(stepped.hours * 30f) }
    val wasSeconds = remember { mutableStateOf(hasSeconds) }

    LaunchedEffect(hasSeconds, stepMillis) {
        if (hasSeconds) {
            wasSeconds.value = true
            return@LaunchedEffect
        }
        // Vừa đổi từ kiểu có vòng giây: đặt góc vẽ về chỗ các vòng đang đứng (góc cũ có thể đã cũ từ lâu) để không quay một vòng dài.
        if (wasSeconds.value) {
            val current = CompassTime(if (isLive) System.currentTimeMillis() else nowMillis)
            minuteDegrees.snapTo(current.minutes * 6f)
            hourDegrees.snapTo(current.hours * 30f)
            wasSeconds.value = false
        }
        val target = CompassTime(stepMillis)
        launch { minuteDegrees.turnTo(target.minutes * 6f) }
        launch { hourDegrees.turnTo(target.hours * 30f) }
    }

    Spacer(
        modifier = modifier
            .fillMaxSize()
            .clearAndSetSemantics { contentDescription = description }
            .drawWithCache {
                val center = size.center
                val unit = min(size.width, size.height) * CompassMetrics.DIAL_FRACTION * sizePercent / 100f / 2f
                val ringSize = ceil(unit * 2f).toInt().coerceAtLeast(1).let { side -> IntSize(side, side) }
                val lineWidth = CompassMetrics.LineWidth.toPx()
                val secondRing = obtainGraphicsLayer().apply {
                    record(size = ringSize) { drawCompassSecondRing(paints, unit, secondColor) }
                }
                val minuteRing = obtainGraphicsLayer().apply {
                    record(size = ringSize) { drawCompassMinuteRing(paints, unit, minuteColor, lineWidth) }
                }
                val hourRing = obtainGraphicsLayer().apply {
                    record(size = ringSize) { drawCompassHourRing(paints, unit, hourColor, halfHourColor) }
                }
                val pointerEnd = Offset(center.x + unit, center.y)
                val circleStroke = Stroke(width = CompassMetrics.CircleWidth.toPx())

                onDrawBehind {
                    val fraction = secondsFraction.value
                    val time = CompassTime(if (isLive) clockMillis.longValue else nowMillis)
                    val pointerStart = Offset(center.x + (1f - fraction) * CompassMetrics.POINTER_GAP * unit, center.y)
                    val headEnd = pointerStart.x + CompassMetrics.POINTER_HEAD * unit
                    val headSpread = CompassMetrics.POINTER_SPREAD * unit

                    drawLine(hourColor, pointerStart, pointerEnd, strokeWidth = lineWidth, cap = StrokeCap.Round)
                    drawLine(hourColor, pointerStart, Offset(headEnd, center.y - headSpread), strokeWidth = lineWidth, cap = StrokeCap.Round)
                    drawLine(hourColor, pointerStart, Offset(headEnd, center.y + headSpread), strokeWidth = lineWidth, cap = StrokeCap.Round)
                    if (fraction > 0f) {
                        secondRing.alpha = fraction
                        drawRing(secondRing, ringSize, time.seconds * 6f)
                    }
                    drawRing(minuteRing, ringSize, if (hasSeconds) time.minutes * 6f else minuteDegrees.value)
                    drawRing(hourRing, ringSize, if (hasSeconds) time.hours * 30f else hourDegrees.value)
                    drawCircle(minuteColor, CompassMetrics.CIRCLE_OUTER * unit, alpha = CompassMetrics.CIRCLE_ALPHA, style = circleStroke)
                    drawCircle(minuteColor, CompassMetrics.CIRCLE_INNER * unit, alpha = CompassMetrics.CIRCLE_ALPHA, style = circleStroke)
                }
            },
    )
}

@Composable
fun rememberCompassPaints(): CompassPaints {
    val typeface = LocalFontFamilyResolver.current
        .resolve(fontFamily = CompassThemeSpec.NumberFont, fontWeight = FontWeight.Normal)
        .value as? Typeface ?: Typeface.DEFAULT
    return remember(typeface) { CompassPaints(typeface) }
}

// Góc cộng dồn theo chiều ngắn nhất: 59 → 0 phút xoay tiếp 6°, không quay ngược cả vòng.
private suspend fun Animatable<Float, *>.turnTo(degrees: Float) {
    val delta = ((degrees - value) % 360f + 540f) % 360f - 180f
    animateTo(targetValue = value + delta, animationSpec = tween(durationMillis = CompassMetrics.STEP_MILLIS))
}

// Vòng xoay cùng chiều kim đồng hồ đúng bằng phần đã trôi qua: số đang chỉ luôn nằm ngang trên đường chỉ.
private fun DrawScope.drawRing(ring: GraphicsLayer, ringSize: IntSize, degrees: Float) {
    rotate(degrees = degrees, pivot = center) {
        translate(left = center.x - ringSize.width / 2f, top = center.y - ringSize.height / 2f) {
            drawLayer(ring)
        }
    }
}

@Preview(widthDp = 393, heightDp = 873)
@Composable
private fun AodCompassDialPreview() {
    AodCompassDial(
        nowMillis = System.currentTimeMillis(),
        isLive = false,
        config = AodThemeConfig(),
        paints = rememberCompassPaints(),
    )
}
