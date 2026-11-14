package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.blinky

import androidx.compose.animation.core.Easing
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.blinky.BlinkyLineValue
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.random.Random

object BlinkyMetrics {
    const val SIZE_BASE = 1080f
    const val OPEN = 0.7f
    const val LOWER_LID = 0.1f
    const val EYE_CORNER = 0.1f
    const val CLOCK_SCALE = 1.3f
    const val CLUSTER_ZONE = 0.3f
    const val LINE_DARKEN = 0.2f
    const val PAUSED_ALPHA = 0.6f
    const val BLINK_MILLIS = 300
    const val BLINK_GAP_MIN_MILLIS = 200
    const val BLINK_GAP_SPAN_MILLIS = 5_000
    const val SLEEP_WAIT_MILLIS = 2_000L
    const val SLEEP_MILLIS = 12_000
    const val DAY_SLEEP_MIN_SECONDS = 60
    const val DAY_SLEEP_SPAN_SECONDS = 300
    const val NIGHT_SLEEP_MIN_SECONDS = 11
    const val NIGHT_SLEEP_SPAN_SECONDS = 50
    const val NIGHT_START_HOUR = 22
    const val NIGHT_END_HOUR = 6
    const val LINE_FADE_MILLIS = 300
    const val EMPTY_MEDIA_HOLD_MILLIS = 1_000L

    val LineIconSize = 16.dp
    val LineIconGap = 7.dp
    val LinePowerGap = 2.dp
    val LineVerticalPadding = 12.dp
    val LineHorizontalPadding = 50.dp
    val LineTextSize = 16.sp
    val IconRowHeight = 14.dp
    val IconRowLift = 8.dp

    val BlinkEasing = Easing { fraction -> (cos((fraction + 1f) * PI) / 2.0 + 0.5).toFloat() }

    private val SLEEP_FRAMES = floatArrayOf(0.7f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.2f, 0.2f, 0.2f, 0.1f, 0.1f, 0.05f, 0.05f, 0.7f, 0f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.7f, 0.65f, 0.6f, 0.55f, 0.5f, 0.45f, 0.4f, 0.4f, 0.4f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.6f, 0.2f, 0.3f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, 0.4f, 0.375f, 0.35f, 0.325f, 0.3f, 0.275f, 0.25f, 0.225f, 0.2f, 0.175f, 0.15f, 0.125f, 0.1f, 0.075f, 0.05f, 0.025f, 0f)

    fun eyeSize(shortSide: Dp, option: Int): Dp = shortSide * (option / SIZE_BASE)

    fun clockTextSize(option: Int): TextUnit = (option * CLOCK_SCALE).sp

    fun blinkOpenness(progress: Float): Float = OPEN * abs(1f - progress * 2f)

    fun sleepOpenness(progress: Float): Float {
        val lastIndex = SLEEP_FRAMES.lastIndex
        val position = progress.coerceIn(0f, 1f) * lastIndex
        val index = min(position.toInt(), lastIndex - 1)
        val fraction = position - index
        return SLEEP_FRAMES[index] + (SLEEP_FRAMES[index + 1] - SLEEP_FRAMES[index]) * fraction
    }

    fun sleepDelaySeconds(hour: Int, random: Random = Random.Default): Int =
        if (hour >= NIGHT_START_HOUR || hour <= NIGHT_END_HOUR) {
            NIGHT_SLEEP_MIN_SECONDS + random.nextInt(NIGHT_SLEEP_SPAN_SECONDS)
        } else {
            DAY_SLEEP_MIN_SECONDS + random.nextInt(DAY_SLEEP_SPAN_SECONDS)
        }

    fun blinkGapMillis(random: Random = Random.Default): Long =
        (BLINK_GAP_MIN_MILLIS + random.nextInt(BLINK_GAP_SPAN_MILLIS)).toLong()

    fun lineHoldMillis(line: BlinkyLineValue, hasMedia: Boolean, holdSeconds: Int): Long? = when (line) {
        BlinkyLineValue.STATUS -> null
        BlinkyLineValue.NOTIFICATION -> if (holdSeconds == 0) null else holdSeconds * 1_000L
        BlinkyLineValue.MEDIA -> if (hasMedia) {
            if (holdSeconds == 0) null else holdSeconds * 1_000L
        } else {
            EMPTY_MEDIA_HOLD_MILLIS
        }
    }
}
