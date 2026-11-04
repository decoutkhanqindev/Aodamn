package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.flip

import android.graphics.Paint
import android.graphics.Path as AndroidPath
import android.graphics.Rect as AndroidRect
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.resolveTypeface
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.components.is24HourFormat
import com.decoutkhanqindev.aodamn.presentation.components.rememberFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberTimeFormatter
import com.decoutkhanqindev.aodamn.presentation.components.rememberZonedDateTime
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.flip.AodFlipStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.flip.FlipThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import com.decoutkhanqindev.aodamn.presentation.theme.White
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun AodFlipClock(
    modifier: Modifier = Modifier,
    nowMillis: Long,
    isLandscape: Boolean,
    isPreviewLive: Boolean,
    isAppearing: Boolean,
    cardSize: Dp,
    config: AodThemeConfig,
) {
    var liveNowMillis by remember { mutableLongStateOf(nowMillis) }

    SideEffect(nowMillis, isPreviewLive) {
        if (!isPreviewLive) liveNowMillis = nowMillis
    }

    LaunchedEffect(isPreviewLive) {
        while (isPreviewLive) {
            val current = System.currentTimeMillis()
            liveNowMillis = current
            delay(FlipMetrics.MINUTE_MILLIS - current % FlipMetrics.MINUTE_MILLIS + FlipMetrics.TICK_SLACK_MILLIS)
        }
    }

    val shownMillis = if (isPreviewLive) liveNowMillis else nowMillis
    val now = rememberZonedDateTime(shownMillis)
    val hour = now.format(
        rememberFormatter(if (is24HourFormat()) R.string.aod_hour_pattern_24h else R.string.aod_hour_pattern_12h),
    )
    val minute = now.format(rememberFormatter(R.string.aod_minute_pattern))
    val description = now.format(rememberTimeFormatter())
    val style = config[FlipThemeSpec.Style]
    val characterSize = config[FlipThemeSpec.CharacterSize]
    val panelColor = config[FlipThemeSpec.CardColor]
    val panelGradient = config.gradientOf(FlipThemeSpec.CardColor)
    val digitColor = config[FlipThemeSpec.DigitColor]
    val digitGradient = config.gradientOf(FlipThemeSpec.DigitColor)

    val content = @Composable {
        AodFlipCard(
            value = hour,
            cardSize = cardSize,
            characterSize = characterSize,
            style = style,
            panelColor = panelColor,
            panelGradient = panelGradient,
            digitColor = digitColor,
            digitGradient = digitGradient,
            isAppearing = isAppearing,
        )
        AodFlipCard(
            value = minute,
            cardSize = cardSize,
            characterSize = characterSize,
            style = style,
            panelColor = panelColor,
            panelGradient = panelGradient,
            digitColor = digitColor,
            digitGradient = digitGradient,
            isAppearing = isAppearing,
        )
    }

    if (isLandscape) {
        Row(
            modifier = modifier.semantics { contentDescription = description },
            horizontalArrangement = Arrangement.spacedBy(FlipMetrics.CardGap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content()
        }
    } else {
        Column(
            modifier = modifier.semantics { contentDescription = description },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(FlipMetrics.CardGap),
        ) {
            content()
        }
    }
}

@Composable
private fun AodFlipCard(
    modifier: Modifier = Modifier,
    value: String,
    cardSize: Dp,
    characterSize: Int,
    style: AodFlipStyleValue,
    panelColor: Color,
    panelGradient: AodGradient?,
    digitColor: Color,
    digitGradient: AodGradient?,
    isAppearing: Boolean,
) {
    var shown by remember { mutableStateOf(if (isAppearing) "" else value) }
    var previous by remember { mutableStateOf(if (isAppearing) "" else value) }
    var next by remember { mutableStateOf(if (isAppearing) "" else value) }
    var isFlipping by remember { mutableStateOf(false) }
    val topRotation = remember { Animatable(0f) }
    val bottomRotation = remember { Animatable(90f) }
    val halfSize = (cardSize - FlipMetrics.PieceGap) / 2

    LaunchedEffect(value) {
        if (value == shown && !isFlipping) return@LaunchedEffect
        previous = shown
        next = value
        isFlipping = true
        topRotation.snapTo(0f)
        bottomRotation.snapTo(90f)
        topRotation.animateTo(
            -90f,
            tween(durationMillis = FlipMetrics.ANIMATION_MILLIS, easing = FlipMetrics.FlipEasing),
        )
        bottomRotation.animateTo(
            0f,
            tween(durationMillis = FlipMetrics.ANIMATION_MILLIS, easing = FlipMetrics.FlipEasing),
        )
        shown = value
        isFlipping = false
    }

    Box(modifier = modifier.size(cardSize)) {
        AodFlipHalf(
            value = if (isFlipping) next else shown,
            isTop = true,
            cardSize = cardSize,
            halfSize = halfSize,
            characterSize = characterSize,
            style = style,
            panelColor = panelColor,
            panelGradient = panelGradient,
            digitColor = digitColor,
            digitGradient = digitGradient,
            modifier = Modifier.align(Alignment.TopCenter),
        )
        AodFlipHalf(
            value = if (isFlipping) previous else shown,
            isTop = false,
            cardSize = cardSize,
            halfSize = halfSize,
            characterSize = characterSize,
            style = style,
            panelColor = panelColor,
            panelGradient = panelGradient,
            digitColor = digitColor,
            digitGradient = digitGradient,
            modifier = Modifier.align(Alignment.BottomCenter),
        )

        if (isFlipping) {
            AodFlipHalf(
                value = previous,
                isTop = true,
                cardSize = cardSize,
                halfSize = halfSize,
                characterSize = characterSize,
                style = style,
                panelColor = panelColor,
                panelGradient = panelGradient,
                digitColor = digitColor,
                digitGradient = digitGradient,
                shadeAlpha = { topRotation.value.absoluteValue / 90f * FlipMetrics.FLAP_SHADE },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .graphicsLayer {
                        rotationX = topRotation.value
                        transformOrigin = TransformOrigin(0.5f, 1f)
                    },
            )
            AodFlipHalf(
                value = next,
                isTop = false,
                cardSize = cardSize,
                halfSize = halfSize,
                characterSize = characterSize,
                style = style,
                panelColor = panelColor,
                panelGradient = panelGradient,
                digitColor = digitColor,
                digitGradient = digitGradient,
                shadeAlpha = { bottomRotation.value.absoluteValue / 90f * FlipMetrics.FLAP_SHADE },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        rotationX = bottomRotation.value
                        transformOrigin = TransformOrigin(0.5f, -(FlipMetrics.PieceGap / halfSize))
                    },
            )
        }
    }
}

@Composable
private fun AodFlipHalf(
    modifier: Modifier = Modifier,
    value: String,
    isTop: Boolean,
    cardSize: Dp,
    halfSize: Dp,
    characterSize: Int,
    style: AodFlipStyleValue,
    panelColor: Color,
    panelGradient: AodGradient?,
    digitColor: Color,
    digitGradient: AodGradient?,
    shadeAlpha: () -> Float = { 0f },
) {
    val typeface = resolveTypeface(style.fontFamily, style.fontWeight, Typeface.DEFAULT)
    val textPaint = remember(typeface) {
        Paint().apply {
            isAntiAlias = true
            isDither = true
            this.typeface = typeface
        }
    }

    Spacer(
        modifier = modifier
            .size(width = cardSize, height = halfSize)
            .drawWithCache {
                val fullHeight = cardSize.toPx()
                val segmentTop = if (isTop) 0f else halfSize.toPx() + FlipMetrics.PieceGap.toPx()
                val outer = FlipMetrics.outerCorner(cardSize).toPx()
                val inner = FlipMetrics.innerCorner(cardSize).toPx()
                val shape = Path().apply {
                    addRoundRect(
                        RoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width,
                            bottom = size.height,
                            topLeftCornerRadius = CornerRadius(if (isTop) outer else inner),
                            topRightCornerRadius = CornerRadius(if (isTop) outer else inner),
                            bottomRightCornerRadius = CornerRadius(if (isTop) inner else outer),
                            bottomLeftCornerRadius = CornerRadius(if (isTop) inner else outer),
                        ),
                    )
                }
                val fullBounds = Rect(left = 0f, top = -segmentTop, right = size.width, bottom = fullHeight - segmentTop)
                val panelBrush = panelGradient?.toBrush(fullBounds) ?: SolidColor(panelColor)
                textPaint.textSize = FlipMetrics.textSize(fullHeight, characterSize, style.textScale)
                textPaint.typeface = typeface
                val textBounds = AndroidRect().also { textPaint.getTextBounds(value, 0, value.length, it) }
                val textX = (size.width - textBounds.width()) / 2f - textBounds.left
                val textY = (fullHeight - textBounds.height()) / 2f - textBounds.top - segmentTop
                val digitShader = digitGradient?.toShader(
                    textX + textBounds.left,
                    textY + textBounds.top,
                    textX + textBounds.right,
                    textY + textBounds.bottom,
                )
                val merged = if (style.isOutlined && value.isNotEmpty()) {
                    val raw = AndroidPath()
                    textPaint.getTextPath(value, 0, value.length, textX, textY, raw)
                    AndroidPath().apply { op(raw, AndroidPath(), AndroidPath.Op.UNION) }
                } else {
                    null
                }

                onDrawBehind {
                    clipPath(shape) {
                        drawPath(path = shape, brush = panelBrush)
                        if (value.isNotEmpty()) {
                            drawIntoCanvas { canvas ->
                                val native = canvas.nativeCanvas
                                textPaint.shader = digitShader
                                textPaint.color = if (digitShader == null) digitColor.toArgb() else White.toArgb()
                                if (style.isOutlined) {
                                    textPaint.style = Paint.Style.STROKE
                                    textPaint.strokeWidth = fullHeight * FlipMetrics.OUTLINE
                                    textPaint.strokeJoin = Paint.Join.ROUND
                                    merged?.let { native.drawPath(it, textPaint) }
                                } else {
                                    textPaint.style = Paint.Style.FILL
                                    native.drawText(value, textX, textY, textPaint)
                                }
                                textPaint.shader = null
                            }
                        }
                        val shade = shadeAlpha()
                        if (shade > 0f) drawPath(path = shape, color = Black, alpha = shade)
                    }
                }
            },
    )
}
