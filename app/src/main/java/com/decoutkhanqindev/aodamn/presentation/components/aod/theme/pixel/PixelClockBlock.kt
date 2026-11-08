package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.pixel

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.Typeface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient

class PixelClockBlock(typeface: Typeface) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.typeface = typeface
        letterSpacing = PixelMetrics.LETTER_SPACING
    }
    private val bounds = Rect()
    private val shaderMatrix = Matrix()

    var width = 0f
        private set
    var height = 0f
        private set
    private var x = 0f
    private var y = 0f
    private var shader: Shader? = null
    private var placedLeft = Float.NaN
    private var placedTop = Float.NaN
    private var layoutVersion by mutableIntStateOf(0)
    private var drawnVersion = 0

    fun measure(
        time: String,
        widthPx: Float,
        heightPx: Float,
        isLandscape: Boolean,
        sizePercent: Int,
        isOutline: Boolean,
        gradient: AodGradient?,
    ) {
        paint.textSize = PixelMetrics.clockSizePx(widthPx, heightPx, isLandscape, sizePercent)
        paint.style = if (isOutline) Paint.Style.STROKE else Paint.Style.FILL
        paint.strokeWidth = if (isOutline) paint.textSize * PixelMetrics.OUTLINE_FRACTION else 0f
        paint.getTextBounds(time, 0, time.length, bounds)
        width = bounds.right.toFloat()
        height = bounds.height().toFloat()
        shader = gradient?.toShader(0f, bounds.top.toFloat(), width, bounds.bottom.toFloat())
        layoutVersion++
    }

    fun place(
        left: Float,
        top: Float,
    ) {
        x = left
        y = top - bounds.top
        shaderMatrix.setTranslate(left, y)
        shader?.setLocalMatrix(shaderMatrix)
        if (left != placedLeft || top != placedTop) {
            placedLeft = left
            placedTop = top
            layoutVersion++
        }
    }

    fun draw(
        canvas: Canvas,
        time: String,
        color: Int,
    ) {
        drawnVersion = layoutVersion
        paint.color = color
        paint.shader = shader
        canvas.drawText(time, x, y, paint)
    }
}
