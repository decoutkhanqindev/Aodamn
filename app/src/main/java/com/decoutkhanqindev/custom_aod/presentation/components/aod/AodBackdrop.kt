package com.decoutkhanqindev.custom_aod.presentation.components.aod

import android.graphics.Bitmap
import android.hardware.display.DisplayManager
import android.view.Display
import android.view.Surface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.WallpaperValue

// Ảnh nền (có sẵn hay ảnh từ máy) gắn với chiều dọc tự nhiên của máy như hình nền điện thoại: màn hình xoay ngang (AOD ngang, khung xem trước
// xoay) thì nền không xoay theo mà xoay ngược rotationZ trong khung đúng cỡ màn dọc, nên vẫn đứng như lúc cầm dọc; chỉ đồng hồ và các khối đổi
// sang bố cục ngang. rotationZ null: theo góc xoay thật của màn hình (AOD); khung xem trước / bản thu nhỏ truyền góc cố định.
@Composable
fun AodBackdrop(
    rotationZ: Float?,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val angle = rotationZ ?: rememberDisplayCounterRotation(isLandscape = maxWidth > maxHeight)
        val isQuarterTurn = angle % 180f != 0f

        Box(
            modifier = Modifier
                .requiredSize(
                    width = if (isQuarterTurn) maxHeight else maxWidth,
                    height = if (isQuarterTurn) maxWidth else maxHeight,
                )
                .graphicsLayer { this.rotationZ = angle },
        ) {
            content()
        }
    }
}

// Góc xoay ngược với màn hình: Surface.ROTATION_90 là nội dung đã xoay 90° theo chiều kim đồng hồ so với máy, nên nền xoay -90° để đứng yên.
// Góc phải khớp với cửa sổ đang vẽ: lúc DisplayListener báo đổi, cửa sổ chưa nhận hướng mới và Display của cửa sổ (view.display, theo cấu hình
// của Activity) vẫn trả góc cũ, đọc lúc đó là trễ một nhịp (xoay ngang thì nền xoay theo, xoay về dọc thì nền vẫn ngang). Nên đọc lại cả khi
// cửa sổ đổi hướng (isLandscape) lẫn khi màn hình báo đổi (lật ngang 180° không đổi kích thước; đọc thêm một lần sau 500 ms phòng khi máy báo
// trước lúc cửa sổ xoay xong), và chỉ lấy góc thật của màn hình (Display của app, không theo cấu hình Activity) khi nó khớp hướng cửa sổ; chưa
// khớp (đang chuyển, hay vừa mở AOD ngang mà màn hình chưa xoay) thì lấy góc theo cấu hình của cửa sổ.
@Composable
private fun rememberDisplayCounterRotation(isLandscape: Boolean): Float {
    val view = LocalView.current
    val displayManager = remember(view) { view.context.applicationContext.getSystemService(DisplayManager::class.java) }
    val displayId = view.display?.displayId ?: Display.DEFAULT_DISPLAY
    var displayChanges by remember { mutableIntStateOf(0) }

    DisposableEffect(view, displayManager, displayId) {
        val reread = Runnable { displayChanges++ }
        val listener = object : DisplayManager.DisplayListener {
            override fun onDisplayChanged(changedDisplayId: Int) {
                if (changedDisplayId != displayId) return
                displayChanges++
                view.removeCallbacks(reread)
                view.postDelayed(reread, 500)
            }

            override fun onDisplayAdded(addedDisplayId: Int) = Unit

            override fun onDisplayRemoved(removedDisplayId: Int) = Unit
        }
        displayManager?.registerDisplayListener(listener, null)
        onDispose {
            view.removeCallbacks(reread)
            displayManager?.unregisterDisplayListener(listener)
        }
    }

    val rotation = remember(displayManager, displayId, isLandscape, displayChanges) {
        val display = displayManager?.getDisplay(displayId)
        val mode = display?.mode
        val isNaturalLandscape = mode != null && mode.physicalWidth > mode.physicalHeight
        val realRotation = display?.rotation
        val isRealQuarterTurn = realRotation == Surface.ROTATION_90 || realRotation == Surface.ROTATION_270

        if (realRotation != null && isRealQuarterTurn == (isLandscape != isNaturalLandscape)) {
            realRotation
        } else {
            view.display?.rotation ?: Surface.ROTATION_0
        }
    }

    return when (rotation) {
        Surface.ROTATION_90 -> -90f
        Surface.ROTATION_180 -> 180f
        Surface.ROTATION_270 -> 90f
        else -> 0f
    }
}

@Composable
fun AodWallpaper(wallpaper: WallpaperValue?) {
    val alpha = 0.5f

    Crossfade(
        targetState = wallpaper,
        modifier = Modifier.fillMaxSize(),
        label = "AodWallpaper",
    ) { target ->
        if (target != null) {
            Image(
                painter = painterResource(target.drawableRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                alignment = target.alignment,
                contentScale = ContentScale.Crop,
                alpha = alpha,
            )
        }
    }
}

@Composable
fun AodBackground(background: Bitmap?) {
    val alpha = 0.5f

    Crossfade(
        targetState = background,
        modifier = Modifier.fillMaxSize(),
        label = "AodBackground",
    ) { bitmap ->
        if (bitmap != null) {
            Image(
                bitmap = remember(bitmap) { bitmap.asImageBitmap() },
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = alpha,
            )
        }
    }
}

@Composable
fun AodEdgeGlow(
    isGlowing: Boolean,
    color: Color,
) {
    AnimatedVisibility(
        visible = isGlowing,
        modifier = Modifier.fillMaxSize(),
        enter = fadeIn(animationSpec = tween(durationMillis = 400)),
        exit = fadeOut(animationSpec = tween(durationMillis = 800)),
    ) {
        val transition = rememberInfiniteTransition(label = "AodEdgeGlow")
        val pulse = transition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 900),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "AodEdgeGlowPulse",
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val glowWidth = 24.dp.toPx()
            val edgeColor = color.copy(alpha = pulse.value)
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(edgeColor, Color.Transparent),
                    startY = 0f,
                    endY = glowWidth,
                ),
                size = Size(width = size.width, height = glowWidth),
            )
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, edgeColor),
                    startY = size.height - glowWidth,
                    endY = size.height,
                ),
                topLeft = Offset(x = 0f, y = size.height - glowWidth),
                size = Size(width = size.width, height = glowWidth),
            )
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(edgeColor, Color.Transparent),
                    startX = 0f,
                    endX = glowWidth,
                ),
                size = Size(width = glowWidth, height = size.height),
            )
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, edgeColor),
                    startX = size.width - glowWidth,
                    endX = size.width,
                ),
                topLeft = Offset(x = size.width - glowWidth, y = 0f),
                size = Size(width = glowWidth, height = size.height),
            )
        }
    }
}
