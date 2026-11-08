package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.moonphase

import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Path as AndroidPath
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.semantics.clearAndSetSemantics
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.rememberLiveClockMillis
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.moonphase.MoonPhaseThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import java.util.TimeZone
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun AodMoonPhaseSky(
    nowMillis: Long,
    isLive: Boolean,
    isStill: Boolean,
    isLandscape: Boolean,
    config: AodThemeConfig,
    modifier: Modifier = Modifier,
) {
    val clockMillis = rememberLiveClockMillis(isLive)
    val phaseMillis = if (isLive) System.currentTimeMillis() else nowMillis
    val tzOffsetMillis = TimeZone.getDefault().getOffset(phaseMillis)
    val currentNowMillis by rememberUpdatedState(nowMillis)
    val currentOffsetMillis by rememberUpdatedState(tzOffsetMillis)
    val currentPhase by rememberUpdatedState(
        MoonPhaseMetrics.resolvePhase(
            lunarPhase = MoonPhaseMetrics.lunarPhase(
                millis = phaseMillis,
                tzOffsetMinutes = tzOffsetMillis / 60_000,
            ),
            peek = config[MoonPhaseThemeSpec.Peek],
        ),
    )
    val stars = remember(isStill) {
        MoonPhaseMetrics.buildStars(
            if (isStill) Random(MoonPhaseMetrics.STILL_SEED) else Random.Default,
        )
    }
    val shootingProgress = remember { mutableFloatStateOf(-1f) }
    val shootingRun = remember { mutableStateOf<MoonPhaseMetrics.ShootingRun?>(null) }

    if (isLive) {
        LaunchedEffect(Unit) {
            while (true) {
                val run = MoonPhaseMetrics.shootingRun(Random.Default)
                delay(run.waitMillis)
                shootingRun.value = run
                val startMillis = System.currentTimeMillis()
                var elapsedMillis = 0L
                while (elapsedMillis < run.durationMillis) {
                    shootingProgress.floatValue = elapsedMillis.toFloat() / run.durationMillis
                    delay(MoonPhaseMetrics.SHOOTING_FRAME_MILLIS)
                    elapsedMillis = System.currentTimeMillis() - startMillis
                }
                shootingProgress.floatValue = -1f
            }
        }
    }

    Spacer(
        modifier = modifier
            .fillMaxSize()
            .clearAndSetSemantics {}
            .drawWithCache {
                val geometry = MoonPhaseMetrics.moonGeometry(
                    width = size.width,
                    height = size.height,
                    marginPx = MoonPhaseMetrics.SkyMargin.toPx(),
                    characterSize = config[MoonPhaseThemeSpec.CharacterSize],
                    isLandscape = isLandscape,
                )
                val maxSize = maxOf(size.width, size.height)
                val minSize = minOf(size.width, size.height)
                val pivot = Offset(maxSize / 2f, maxSize / 2f)
                val rotation = config[MoonPhaseThemeSpec.Rotation].toFloat()
                val moon = renderMoon(
                    diameter = geometry.diameter,
                    colors = config.colorsOf(MoonPhaseThemeSpec.CharacterColor),
                    gradient = config.gradientOf(MoonPhaseThemeSpec.CharacterColor),
                    phase = currentPhase,
                )
                val smallPoints = MoonPhaseMetrics.smallBucketPoints(
                    stars = stars,
                    maxSize = maxSize,
                )
                val particleColors = config.colorsOf(MoonPhaseThemeSpec.ParticleColor)
                val particleGradient = config.gradientOf(MoonPhaseThemeSpec.ParticleColor)
                val smallPaints = Array(MoonPhaseMetrics.SMALL_BUCKETS) { bucket ->
                    Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        strokeCap = Paint.Cap.ROUND
                        strokeWidth = MoonPhaseMetrics.smallStrokeWidth(
                            bucket = bucket,
                            minSize = minSize,
                        )
                        color = particleColors[0].toArgb()
                        shader = particleGradient?.toShader(
                            left = 0f,
                            top = 0f,
                            right = maxSize,
                            bottom = maxSize,
                        )
                        alpha = MoonPhaseMetrics.smallBucketAlpha(bucket)
                    }
                }
                val bigCenterX = FloatArray(stars.bigX.size) { index ->
                    stars.bigX[index] * size.width
                }
                val bigCenterY = FloatArray(stars.bigY.size) { index ->
                    stars.bigY[index] * size.height
                }
                val bigSizes = FloatArray(stars.bigSize.size) { index ->
                    MoonPhaseMetrics.bigStarSize(
                        fraction = stars.bigSize[index],
                        minSize = minSize,
                    )
                }
                val sprites = Array(stars.bigX.size) { index ->
                    val color = if (particleGradient == null) {
                        particleColors[0]
                    } else {
                        colorAtFraction(
                            colors = particleColors,
                            fraction = stars.bigX[index],
                        )
                    }
                    renderStarSprite(
                        starSize = bigSizes[index],
                        blur = if (stars.bigTwinkles[index]) .02f * minSize else 0f,
                        color = color.toArgb(),
                    )
                }
                val glowColor = config[MoonPhaseThemeSpec.GlowColor]
                val glowBrush = config.gradientOf(MoonPhaseThemeSpec.GlowColor)?.toBrush()
                val glowPath = Path()
                val corePath = Path()

                onDrawBehind {
                    val millis = if (isLive) clockMillis.longValue else currentNowMillis
                    val localMillis = millis + currentOffsetMillis
                    val minute = MoonPhaseMetrics.minuteOfHour(localMillis)
                    val twinkle = if (isLive) MoonPhaseMetrics.twinkle(millis) else 0f

                    rotate(
                        degrees = MoonPhaseMetrics.skyDegrees(minute),
                        pivot = pivot,
                    ) {
                        val canvas = drawContext.canvas.nativeCanvas
                        for (bucket in smallPoints.indices) {
                            canvas.drawPoints(smallPoints[bucket], smallPaints[bucket])
                        }
                        for (index in sprites.indices) {
                            val sprite = sprites[index]
                            val scale = if (stars.bigTwinkles[index]) {
                                (bigSizes[index] + twinkle) / bigSizes[index]
                            } else {
                                1f
                            }
                            val width = (sprite.width * scale).roundToInt()
                            val height = (sprite.height * scale).roundToInt()
                            drawImage(
                                image = sprite,
                                dstOffset = IntOffset(
                                    x = (bigCenterX[index] - width / 2f).roundToInt(),
                                    y = (bigCenterY[index] - height / 2f).roundToInt(),
                                ),
                                dstSize = IntSize(width = width, height = height),
                                alpha = if (stars.bigTwinkles[index]) {
                                    1f
                                } else {
                                    stars.bigAlpha[index] / 255f
                                },
                            )
                        }
                    }

                    val progress = shootingProgress.floatValue
                    val run = shootingRun.value
                    if (progress >= 0f && run != null) {
                        val eased = MoonPhaseMetrics.shootingEase(progress)
                        val headX = MoonPhaseMetrics.shootingHeadX(
                            run = run,
                            maxSize = maxSize,
                            height = size.height,
                            eased = eased,
                        )
                        val headY = MoonPhaseMetrics.shootingHeadY(
                            run = run,
                            maxSize = maxSize,
                            height = size.height,
                            eased = eased,
                        )
                        val tail = eased * run.lengthFraction * maxSize
                        shootingTriangle(
                            path = glowPath,
                            headX = headX,
                            headY = headY,
                            angleDegrees = run.angleDegrees,
                            spreadDegrees = .6f,
                            tail = tail,
                        )
                        shootingTriangle(
                            path = corePath,
                            headX = headX,
                            headY = headY,
                            angleDegrees = run.angleDegrees,
                            spreadDegrees = .2f,
                            tail = tail,
                        )
                        if (glowBrush != null) {
                            drawPath(path = glowPath, brush = glowBrush, alpha = .3f)
                            drawPath(path = corePath, brush = glowBrush)
                        } else {
                            drawPath(path = glowPath, color = glowColor, alpha = .3f)
                            drawPath(path = corePath, color = glowColor)
                        }
                    }

                    val theta = MoonPhaseMetrics.orbitDegrees(minute)
                    withTransform(
                        transformBlock = {
                            translate(left = geometry.centerX, top = geometry.centerY)
                            rotate(degrees = theta, pivot = Offset.Zero)
                            translate(left = -geometry.orbit, top = -geometry.orbit)
                            rotate(
                                degrees = -theta - MoonPhaseMetrics.tiltDegrees(localMillis) - rotation,
                                pivot = Offset.Zero,
                            )
                        },
                    ) {
                        drawImage(
                            image = moon,
                            topLeft = Offset(
                                x = -moon.width / 2f,
                                y = -moon.height / 2f,
                            ),
                        )
                    }
                }
            },
    )
}

private fun renderMoon(
    diameter: Float,
    colors: List<Color>,
    gradient: AodGradient?,
    phase: Float,
): ImageBitmap {
    val side = ceil(diameter).toInt() + 4
    val bitmap = Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val inset = (side - diameter) / 2f
    val rect = RectF(inset, inset, inset + diameter, inset + diameter)
    val center = side / 2f
    val radius = diameter / 2f
    val half = AndroidPath().apply {
        addArc(rect, 90f, 180f)
        close()
    }
    val halfWidth = diameter * abs(.5f - phase)
    val oval = AndroidPath().apply {
        addOval(
            RectF(
                center - halfWidth,
                rect.top,
                center + halfWidth,
                rect.bottom,
            ),
            AndroidPath.Direction.CW,
        )
    }
    val lit = AndroidPath().apply {
        op(
            half,
            oval,
            if (phase < .5f) AndroidPath.Op.DIFFERENCE else AndroidPath.Op.UNION,
        )
    }
    val moonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colors[0].toArgb()
        shader = gradient?.toShader(
            left = rect.left,
            top = rect.top,
            right = rect.right,
            bottom = rect.bottom,
        )
    }
    val craterGradient = gradient?.copy(
        colors = colors.map { color ->
            MoonPhaseMetrics.mix(
                from = color,
                to = Black,
                fraction = .05f,
            )
        }.toPersistentList(),
    )
    val craterPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = MoonPhaseMetrics.mix(
            from = colors[0],
            to = Black,
            fraction = .05f,
        ).toArgb()
        shader = craterGradient?.toShader(
            left = rect.left,
            top = rect.top,
            right = rect.right,
            bottom = rect.bottom,
        )
        setShadowLayer(
            1f,
            0f,
            0f,
            MoonPhaseMetrics.mix(
                from = colors[0],
                to = Black,
                fraction = .07f,
            ).toArgb(),
        )
    }

    canvas.save()
    canvas.clipPath(lit)
    canvas.drawCircle(center, center, radius, moonPaint)
    var index = 0
    while (index < MoonPhaseMetrics.Craters.size) {
        canvas.drawCircle(
            center + MoonPhaseMetrics.Craters[index] * radius,
            center + MoonPhaseMetrics.Craters[index + 1] * radius,
            MoonPhaseMetrics.Craters[index + 2] * radius,
            craterPaint,
        )
        index += 3
    }
    canvas.restore()
    return bitmap.asImageBitmap()
}

private fun renderStarSprite(
    starSize: Float,
    blur: Float,
    color: Int,
): ImageBitmap {
    val extent = ceil(starSize * 1.25f + blur * 2f + 2f).toInt()
    val side = extent * 2
    val bitmap = Bitmap.createBitmap(side, side, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val center = extent.toFloat()
    val fraction = starSize / 4f
    val path = AndroidPath().apply {
        moveTo(center - fraction, center - fraction)
        lineTo(center, center - fraction - starSize)
        lineTo(center + fraction, center - fraction)
        lineTo(center + fraction + starSize, center)
        lineTo(center + fraction, center + fraction)
        lineTo(center, center + fraction + starSize)
        lineTo(center - fraction, center + fraction)
        lineTo(center - fraction - starSize, center)
        close()
    }
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        this.color = color
        if (blur > 0f) {
            maskFilter = BlurMaskFilter(blur, BlurMaskFilter.Blur.SOLID)
        }
    }
    canvas.drawPath(path, paint)
    return bitmap.asImageBitmap()
}

private fun shootingTriangle(
    path: Path,
    headX: Float,
    headY: Float,
    angleDegrees: Float,
    spreadDegrees: Float,
    tail: Float,
) {
    val firstRadians = (angleDegrees - spreadDegrees) * PI / 180.0
    val secondRadians = (angleDegrees + spreadDegrees) * PI / 180.0
    path.rewind()
    path.moveTo(headX, headY)
    path.lineTo(
        headX - sin(firstRadians).toFloat() * tail,
        headY - cos(firstRadians).toFloat() * tail,
    )
    path.lineTo(
        headX - sin(secondRadians).toFloat() * tail,
        headY - cos(secondRadians).toFloat() * tail,
    )
    path.close()
}

private fun colorAtFraction(
    colors: List<Color>,
    fraction: Float,
): Color {
    if (colors.size == 1) return colors[0]
    val position = fraction.coerceIn(0f, 1f) * (colors.size - 1)
    val index = position.toInt().coerceAtMost(colors.lastIndex - 1)
    return lerp(
        start = colors[index],
        stop = colors[index + 1],
        fraction = position - index,
    )
}
