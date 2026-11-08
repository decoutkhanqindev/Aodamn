package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.poster

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.Typeface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.decoutkhanqindev.aodamn.presentation.components.aod.theme.toShader
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient

class PosterColors(
    val hour: Int,
    val minute: Int,
    val day: Int,
    val date: Int,
    val hourGradient: AodGradient?,
    val minuteGradient: AodGradient?,
    val dayGradient: AodGradient?,
    val dateGradient: AodGradient?,
)

class PosterTextBlock(typeface: Typeface) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.typeface = typeface
        letterSpacing = PosterMetrics.LETTER_SPACING
    }
    private val hourBounds = Rect()
    private val minuteBounds = Rect()
    private val dayBounds = Rect()
    private val dateBounds = Rect()

    private var hourSize = 0f
    private var daySize = 0f
    private var dateSize = 0f
    private var hourX = 0f
    private var hourY = 0f
    private var minuteX = 0f
    private var minuteY = 0f
    private var dayX = 0f
    private var dayY = 0f
    private var dateX = 0f
    private var dateY = 0f
    private var hourShader: Shader? = null
    private var minuteShader: Shader? = null
    private var dayShader: Shader? = null
    private var dateShader: Shader? = null
    var dateRight = 0f
        private set
    var dateCenterY = 0f
        private set
    var dayBottom = 0f
        private set
    var padding = 0f
        private set
    private var hasDate = false
    private var placedTop = Float.NaN
    private var layoutVersion by mutableIntStateOf(0)
    private var drawnVersion = 0

    fun measure(
        hour: String,
        minute: String,
        day: String,
        date: String?,
        widthPx: Int,
        heightPx: Int,
        isLandscape: Boolean,
        sizePercent: Int,
        density: Float,
        extraHeight: Float,
        colors: PosterColors?,
    ) {
        val baseSize = PosterMetrics.baseSizePx(widthPx.toFloat(), heightPx.toFloat(), isLandscape, sizePercent)
        padding = (if (isLandscape) PosterMetrics.LandscapePadding else PosterMetrics.PortraitPadding).value * density
        val available = (if (isLandscape) widthPx * PosterMetrics.LANDSCAPE_COLUMN else widthPx.toFloat()) - 2f * padding
        val gap = PosterMetrics.HourMinuteGap.value * density
        val lineGap = PosterMetrics.LineGap.value * density

        paint.textSize = baseSize * PosterMetrics.HOUR_SCALE
        measureText(hour, hourBounds)
        measureText(minute, minuteBounds)
        val hourMinuteScale = PosterMetrics.fitScale(hourBounds.right + gap + minuteBounds.right, available)
        if (hourMinuteScale < 1f) {
            paint.textSize *= hourMinuteScale
            measureText(hour, hourBounds)
            measureText(minute, minuteBounds)
        }
        val measuredHourSize = paint.textSize

        paint.textSize = baseSize
        measureText(day, dayBounds)
        val dayScale = PosterMetrics.fitScale(dayBounds.right.toFloat(), available)
        if (dayScale < 1f) {
            paint.textSize *= dayScale
            measureText(day, dayBounds)
        }
        val measuredDaySize = paint.textSize

        hasDate = date != null
        var measuredDateSize = 0f
        if (date != null) {
            paint.textSize = baseSize
            measureText(date, dateBounds)
            val dateScale = PosterMetrics.fitScale(dateBounds.right.toFloat(), available)
            if (dateScale < 1f) {
                paint.textSize *= dateScale
                measureText(date, dateBounds)
            }
            measuredDateSize = paint.textSize
        }

        val dateHeight = if (hasDate) dateBounds.height() + lineGap else 0f
        val blockHeight = hourBounds.height() + lineGap + dayBounds.height() + dateHeight + extraHeight
        var top = PosterMetrics.centeredTop(if (isLandscape) heightPx.toFloat() else heightPx * PosterMetrics.PORTRAIT_TEXT_REGION, blockHeight)
        if (top != placedTop) {
            placedTop = top
            layoutVersion++
        }
        hourX = padding
        hourY = top - hourBounds.top
        minuteX = hourBounds.right + gap + padding
        minuteY = top - minuteBounds.top
        top += hourBounds.height() + lineGap
        dayX = padding
        dayY = top - dayBounds.top
        dayBottom = top + dayBounds.height()
        top += dayBounds.height() + lineGap
        if (hasDate) {
            dateX = padding
            dateY = top - dateBounds.top
            dateRight = padding + dateBounds.right
            dateCenterY = top + dateBounds.height() / 2f
        } else {
            dateRight = 0f
            dateCenterY = 0f
        }

        hourSize = measuredHourSize
        daySize = measuredDaySize
        dateSize = measuredDateSize
        paint.textSize = hourSize
        hourShader = colors?.hourGradient?.toShader(padding, hourY + hourBounds.top, padding + hourBounds.right, hourY + hourBounds.bottom)
        minuteShader = colors?.minuteGradient?.toShader(minuteX, minuteY + minuteBounds.top, minuteX + minuteBounds.right, minuteY + minuteBounds.bottom)
        paint.textSize = daySize
        dayShader = colors?.dayGradient?.toShader(padding, dayY + dayBounds.top, padding + dayBounds.right, dayY + dayBounds.bottom)
        if (hasDate) {
            paint.textSize = dateSize
            dateShader = colors?.dateGradient?.toShader(padding, dateY + dateBounds.top, padding + dateBounds.right, dateY + dateBounds.bottom)
        } else {
            dateShader = null
        }
    }

    fun draw(
        canvas: Canvas,
        hour: String,
        minute: String,
        day: String,
        date: String?,
        colors: PosterColors,
    ) {
        drawnVersion = layoutVersion
        paint.textSize = hourSize
        paint.color = colors.hour
        paint.shader = hourShader
        canvas.drawText(hour, hourX, hourY, paint)
        paint.color = colors.minute
        paint.shader = minuteShader
        canvas.drawText(minute, minuteX, minuteY, paint)
        paint.textSize = daySize
        paint.color = colors.day
        paint.shader = dayShader
        canvas.drawText(day, dayX, dayY, paint)
        if (date != null && hasDate) {
            paint.textSize = dateSize
            paint.color = colors.date
            paint.shader = dateShader
            canvas.drawText(date, dateX, dateY, paint)
        }
    }

    private fun measureText(
        text: String,
        bounds: Rect,
    ) {
        paint.getTextBounds(text, 0, text.length, bounds)
    }
}
