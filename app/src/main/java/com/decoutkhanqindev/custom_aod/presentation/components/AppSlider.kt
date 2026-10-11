package com.decoutkhanqindev.custom_aod.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.presentation.theme.Blue
import com.decoutkhanqindev.custom_aod.presentation.theme.BodyMedium
import com.decoutkhanqindev.custom_aod.presentation.theme.GreyED
import com.decoutkhanqindev.custom_aod.presentation.theme.Mint
import com.decoutkhanqindev.custom_aod.presentation.theme.NeutralVariant30
import com.decoutkhanqindev.custom_aod.presentation.theme.Purple
import com.decoutkhanqindev.custom_aod.presentation.theme.Theme
import com.decoutkhanqindev.custom_aod.utils.collectCatching
import com.decoutkhanqindev.custom_aod.utils.collectLatestCatching
import timber.log.Timber
import kotlin.math.roundToInt

// Thanh theo số nguyên có bước (giá trị = min + vị trí * step). onProgressChange = null: chỉ hiển thị tiến trình (splash, thanh nhạc
// trên AOD), không đi qua Slider của Material vì SliderImpl đọc state.value ngay trong composition (tham số slideOnKeyEvents) nên
// recompose mỗi lần tiến trình đổi; progress được gọi ở pha vẽ của track và pha layout của thumb nên tiến trình đổi chỉ vẽ lại, cao
// đúng bằng thumb / track. isThumbVisible = false: không vẽ thumb, track trải hết chiều ngang. track thay thanh mặc định (vd thanh nhạc
// gợn sóng của chủ đề AOD), nhận fraction đọc ở pha vẽ.
@Composable
fun AppSlider(
    progress: () -> Int,
    valueRange: IntRange,
    step: Int,
    modifier: Modifier = Modifier,
    onProgressChange: ((Int) -> Unit)? = null,
    label: String? = null,
    valueText: String? = null,
    isEnabled: Boolean = true,
    isThumbVisible: Boolean = true,
    track: @Composable (fraction: () -> Float) -> Unit = { fraction -> AppSliderTrack(fraction = fraction, isEnabled = isEnabled) },
) {
    if (onProgressChange == null) {
        AppProgressBar(
            progress = progress,
            valueRange = valueRange,
            modifier = modifier,
            label = label,
            valueText = valueText,
            isEnabled = isEnabled,
            isThumbVisible = isThumbVisible,
            track = track,
        )
    } else {
        AppInteractiveSlider(
            progress = progress,
            valueRange = valueRange,
            step = step,
            onProgressChange = onProgressChange,
            modifier = modifier,
            label = label,
            valueText = valueText,
            isEnabled = isEnabled,
            isThumbVisible = isThumbVisible,
            track = track,
        )
    }
}

@Composable
private fun AppProgressBar(
    progress: () -> Int,
    valueRange: IntRange,
    modifier: Modifier,
    label: String?,
    valueText: String?,
    isEnabled: Boolean,
    isThumbVisible: Boolean,
    track: @Composable (fraction: () -> Float) -> Unit,
) {
    val currentProgress by rememberUpdatedState(progress)
    val fraction = remember(valueRange) {
        {
            val span = (valueRange.last - valueRange.first).coerceAtLeast(1)
            ((currentProgress() - valueRange.first).toFloat() / span).coerceIn(0f, 1f)
        }
    }

    Box(
        modifier = modifier.semantics {
            label?.let { contentDescription = it }
            valueText?.let { stateDescription = it }
            progressBarRangeInfo = ProgressBarRangeInfo(
                current = currentProgress().toFloat(),
                range = valueRange.first.toFloat()..valueRange.last.toFloat(),
            )
        },
        contentAlignment = Alignment.CenterStart,
    ) {
        track(fraction)

        if (isThumbVisible) {
            Box(
                modifier = Modifier.layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
                    val width = if (constraints.hasBoundedWidth) constraints.maxWidth else placeable.width
                    layout(width, placeable.height) {
                        placeable.placeRelative(x = ((width - placeable.width) * fraction()).roundToInt(), y = 0)
                    }
                },
            ) {
                AppSliderThumb(pressProgress = { 0f }, isEnabled = isEnabled)
            }
        }
    }
}

// Thumb theo ngón tay bằng SliderState cục bộ, chỉ gọi onProgressChange khi sang bước khác nên không phụ thuộc độ trễ ghi DataStore /
// ViewModel; giá trị đổi từ ngoài (hoàn tác, đặt lại, bị từ chối) kéo thumb theo khi không kéo. progress chỉ được gọi trong snapshotFlow,
// track và hiệu ứng nhấn đọc ở pha vẽ nên kéo không làm AppSlider recompose (Slider của Material bên trong vẫn recompose theo value).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppInteractiveSlider(
    progress: () -> Int,
    valueRange: IntRange,
    step: Int,
    onProgressChange: (Int) -> Unit,
    modifier: Modifier,
    label: String?,
    valueText: String?,
    isEnabled: Boolean,
    isThumbVisible: Boolean,
    track: @Composable (fraction: () -> Float) -> Unit,
) {
    val tag = LocalTag.current
    val stepCount = (valueRange.last - valueRange.first) / step
    val currentProgress by rememberUpdatedState(progress)
    val currentOnProgressChange by rememberUpdatedState(onProgressChange)
    val interactionSource = remember { MutableInteractionSource() }
    val isDragged = interactionSource.collectIsDraggedAsState()
    val pressProgress = remember { Animatable(0f) }
    val sliderState = remember(valueRange, step) {
        SliderState(
            value = Snapshot.withoutReadObservation { (progress() - valueRange.first) / step }.toFloat(),
            steps = (stepCount - 1).coerceAtLeast(0),
            valueRange = 0f..stepCount.toFloat(),
        )
    }

    LaunchedEffect(sliderState) {
        snapshotFlow { if (isDragged.value) null else currentProgress() }.collectCatching(
            block = { value ->
                val position = value?.let { (it - valueRange.first) / step } ?: return@collectCatching
                if (sliderState.value.roundToInt() != position) sliderState.value = position.toFloat()
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LaunchedEffect(sliderState) {
        snapshotFlow { sliderState.value.roundToInt() }.collectCatching(
            block = { position ->
                val value = valueRange.first + position * step
                if (value != currentProgress()) currentOnProgressChange(value)
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    LaunchedEffect(interactionSource) {
        val active = mutableSetOf<Interaction>()
        interactionSource.interactions.collectLatestCatching(
            block = { interaction ->
                when (interaction) {
                    is PressInteraction.Press, is DragInteraction.Start -> active += interaction
                    is PressInteraction.Release -> active -= interaction.press
                    is PressInteraction.Cancel -> active -= interaction.press
                    is DragInteraction.Stop -> active -= interaction.start
                    is DragInteraction.Cancel -> active -= interaction.start
                }
                pressProgress.animateTo(
                    targetValue = if (active.isEmpty()) 0f else 1f,
                    animationSpec = tween(durationMillis = 150),
                )
            },
            catch = { e -> Timber.tag(tag).e(e.stackTraceToString()) },
        )
    }

    Slider(
        state = sliderState,
        modifier = modifier.semantics {
            label?.let { contentDescription = it }
            valueText?.let { stateDescription = it }
        },
        enabled = isEnabled,
        interactionSource = interactionSource,
        thumb = {
            if (isThumbVisible) {
                AppSliderThumb(
                    pressProgress = { pressProgress.value },
                    isEnabled = isEnabled,
                )
            }
        },
        track = {
            track {
                val range = sliderState.valueRange
                ((sliderState.value - range.start) / (range.endInclusive - range.start)).coerceIn(0f, 1f)
            }
        },
    )
}

@Composable
private fun AppSliderThumb(
    pressProgress: () -> Float,
    isEnabled: Boolean,
) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .graphicsLayer { alpha = if (isEnabled) 1f else 0.38f }
            .drawBehind {
                val progress = pressProgress()
                drawCircle(
                    color = Mint,
                    radius = size.minDimension / 2 + 8.dp.toPx() * progress,
                    alpha = 0.18f * progress,
                )
                drawCircle(
                    color = GreyED,
                    radius = size.minDimension / 2 * (1f + 0.15f * progress),
                )
            },
    )
}

@Composable
private fun AppSliderTrack(
    fraction: () -> Float,
    isEnabled: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .graphicsLayer { alpha = if (isEnabled) 1f else 0.38f }
            .drawWithCache {
                val isRtl = layoutDirection == LayoutDirection.Rtl
                val brush = Brush.horizontalGradient(
                    colors = listOf(Mint, Blue, Purple),
                    startX = 0f,
                    endX = size.width,
                )
                val radius = CornerRadius(size.height / 2)

                onDrawBehind {
                    val activeWidth = size.width * fraction()

                    withTransform({ if (isRtl) scale(scaleX = -1f, scaleY = 1f, pivot = center) }) {
                        drawRoundRect(color = NeutralVariant30, cornerRadius = radius)
                        drawRoundRect(
                            brush = brush,
                            size = Size(width = activeWidth, height = size.height),
                            cornerRadius = radius,
                        )
                    }
                }
            },
    )
}

@Preview(widthDp = 360)
@Composable
private fun AppSliderPreview() {
    Theme {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "30%",
                color = GreyED,
                style = BodyMedium,
            )

            AppSlider(
                progress = { 30 },
                valueRange = 0..60,
                step = 5,
                onProgressChange = {},
            )

            AppSlider(
                progress = { 70 },
                valueRange = 1..100,
                step = 1,
                onProgressChange = {},
            )
        }
    }
}
