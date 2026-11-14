package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.moonphase

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.decoutkhanqindev.aodamn.R
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodLatestLineStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodTextPlayerStyle
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.moonphase.MoonPhaseThemeSpec
import com.decoutkhanqindev.aodamn.presentation.theme.BlackAlpha90
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.random.Random

object MoonPhaseMetrics {
    const val STILL_SEED = 4319
    const val MINUTE_MILLIS = 60_000L
    const val TICK_SLACK_MILLIS = 50L
    const val SWAP_MILLIS = 200
    const val FADE_MILLIS = 300
    const val OPEN_MILLIS = 1_000
    const val SHOOTING_FRAME_MILLIS = 33L
    const val SMALL_BUCKETS = 4
    const val NEW_MOON_JULIAN = 2451550.1
    const val NEW_MOON_CYCLE_DAYS = 29.530588853
    const val UNIX_JULIAN = 2440587.5
    const val LINE_BOX = 1.2f
    const val CLOCK_SPACING = .55f
    const val DATE_SPACING = .3f
    const val GAP_SCALE = .75f
    const val ARTIST_DARKEN = .3f
    const val PORTRAIT_HEADER_BOTTOM = .3f
    const val PORTRAIT_BAND_TOP = 1f - .55f / 3f
    const val LANDSCAPE_HEADER_X = .65f
    const val LANDSCAPE_HEADER_Y = .35f
    const val LANDSCAPE_BAND_Y = .85f

    val ClockEm = 33.sp
    val DateEm = 15.sp
    val BlockPadding = 15.dp
    val PortraitGap = 7.dp
    val LandscapeGap = 8.dp
    val SkyMargin = 60.dp
    val LandscapeBandExtra = 60.dp
    val OpenShift = 17.dp
    val SwapSlide = 17.dp
    val StatusGlyph = 14.2.dp
    val DateWeatherGap = 12.dp
    val LatestMargin = 25.dp
    val PlayerMargin = 40.dp
    val Craters = floatArrayOf(
        -.5f,
        -.5f,
        .09f,
        -.85f,
        -.5f,
        .13f,
        -.15f,
        -.23f,
        .13f,
        .35f,
        -.23f,
        .07f,
        .35f,
        .27f,
        .05f,
        .65f,
        .75f,
        .15f,
        -.55f,
        .15f,
        .124f,
        .2f,
        .6f,
        .09f,
    )
    val SmallStrokeFractions = floatArrayOf(.7f, .25f, .95f, .5f)
    val ScrimBrush = Brush.verticalGradient(
        listOf(Color.Transparent, BlackAlpha90, Color.Transparent),
    )
    val TextPlayer = AodTextPlayerStyle(
        fontFamily = MoonPhaseThemeSpec.Font,
        titleSize = 13.sp,
        artistSize = 11.sp,
        artistGap = 8.dp,
        buttonSize = 42.dp,
        buttonIconSize = 14.dp,
        pausedAlpha = .6f,
        textGap = 15.dp,
        letterSpacing = .3.em,
        isUppercase = true,
        previousIconRes = R.drawable.ic_caret_left_light,
        nextIconRes = R.drawable.ic_caret_right_light,
    )
    val LatestLine = AodLatestLineStyle(
        font = MoonPhaseThemeSpec.Font,
        iconSize = 17.dp,
        iconGap = 10.dp,
        textSize = 13.sp,
        letterSpacing = .2.em,
        isUppercase = true,
    )

    class MoonGeometry(
        val diameter: Float,
        val orbit: Float,
        val centerX: Float,
        val centerY: Float,
    )

    class StarField(
        val smallX: FloatArray,
        val smallY: FloatArray,
        val smallBucket: IntArray,
        val bigX: FloatArray,
        val bigY: FloatArray,
        val bigSize: FloatArray,
        val bigAlpha: IntArray,
        val bigTwinkles: BooleanArray,
    )

    class ShootingRun(
        val startFraction: Float,
        val lengthFraction: Float,
        val angleDegrees: Float,
        val durationMillis: Long,
        val waitMillis: Long,
    )

    fun moonGeometry(
        width: Float,
        height: Float,
        marginPx: Float,
        characterSize: Int,
        isLandscape: Boolean,
    ): MoonGeometry {
        val span = if (isLandscape) {
            minOf(width * .7f - marginPx, height - marginPx)
        } else {
            minOf(width - marginPx, height * 1.55f / 3f - marginPx)
        }
        val diameter = span * .25f * characterSize / 100f
        return MoonGeometry(
            diameter = diameter,
            orbit = (span - 2f * diameter) / 3f,
            centerX = if (isLandscape) width * .35f else width / 2f,
            centerY = if (isLandscape) {
                height / 2f
            } else {
                height * .3f + height * 1.55f / 6f
            },
        )
    }

    fun lunarPhase(
        millis: Long,
        tzOffsetMinutes: Int,
    ): Float {
        val cycle = (
            (millis / 86_400_000.0 - tzOffsetMinutes / 1440.0) +
                UNIX_JULIAN -
                NEW_MOON_JULIAN
        ) / NEW_MOON_CYCLE_DAYS
        val age = cycle - floor(cycle)
        return ((if (age > .5) 1.0 - age else age) * 2.0).toFloat()
    }

    fun resolvePhase(
        lunarPhase: Float,
        peek: Int,
    ): Float = floor((if (peek > 0) peek / 50f else lunarPhase) * 1000f) / 1000f

    fun minuteOfHour(localMillis: Long): Float =
        Math.floorMod(localMillis, 3_600_000L) / 60_000f

    fun tiltDegrees(localMillis: Long): Float =
        Math.floorMod(localMillis, 43_200_000L) / 3_600_000f

    fun orbitDegrees(minute: Float): Float = minute % 4f / 4f * 360f

    fun skyDegrees(minute: Float): Float = minute % 8f / 8f * 360f

    fun twinkle(millis: Long): Float = 1f - abs((millis % 500L) / 250f - 1f)

    fun shootingEase(progress: Float): Float =
        cos((progress + 1f) * PI).toFloat() / 2f + .5f

    fun buildStars(random: Random): StarField {
        val smallCount = 80 + random.nextInt(49)
        val bigCount = 12 + random.nextInt(13)
        val smallX = FloatArray(smallCount)
        val smallY = FloatArray(smallCount)
        val smallBucket = IntArray(smallCount)
        val bigX = FloatArray(bigCount)
        val bigY = FloatArray(bigCount)
        val bigSize = FloatArray(bigCount)
        val bigAlpha = IntArray(bigCount)
        val bigTwinkles = BooleanArray(bigCount)

        for (index in 0 until smallCount) {
            val alpha = 55 + random.nextInt(150)
            smallX[index] = random.nextFloat()
            smallY[index] = random.nextFloat()
            smallBucket[index] = (alpha - 55) * SMALL_BUCKETS / 150
        }
        for (index in 0 until bigCount) {
            bigX[index] = random.nextFloat()
            bigY[index] = random.nextFloat()
            bigSize[index] = random.nextFloat()
            bigAlpha[index] = 55 + random.nextInt(200)
            bigTwinkles[index] = random.nextBoolean()
        }
        return StarField(
            smallX = smallX,
            smallY = smallY,
            smallBucket = smallBucket,
            bigX = bigX,
            bigY = bigY,
            bigSize = bigSize,
            bigAlpha = bigAlpha,
            bigTwinkles = bigTwinkles,
        )
    }

    fun smallBucketAlpha(bucket: Int): Int =
        (55 + (bucket + .5f) * 150f / SMALL_BUCKETS).toInt()

    fun smallBucketPoints(
        stars: StarField,
        maxSize: Float,
    ): Array<FloatArray> {
        val counts = IntArray(SMALL_BUCKETS)
        for (bucket in stars.smallBucket) {
            counts[bucket]++
        }
        val points = Array(SMALL_BUCKETS) { bucket ->
            FloatArray(counts[bucket] * 2)
        }
        val offsets = IntArray(SMALL_BUCKETS)
        for (index in stars.smallX.indices) {
            val bucket = stars.smallBucket[index]
            points[bucket][offsets[bucket]++] = stars.smallX[index] * maxSize
            points[bucket][offsets[bucket]++] = stars.smallY[index] * maxSize
        }
        return points
    }

    fun smallStrokeWidth(
        bucket: Int,
        minSize: Float,
    ): Float = 1.5f * (1f + SmallStrokeFractions[bucket] * .004f * minSize)

    fun bigStarSize(
        fraction: Float,
        minSize: Float,
    ): Float = 1f + fraction * .0112f * minSize

    fun shootingRun(random: Random): ShootingRun {
        val start = random.nextFloat()
        return ShootingRun(
            startFraction = start,
            lengthFraction = .25f + random.nextFloat() * .25f,
            angleDegrees = if (start < .5f) -135f else 135f,
            durationMillis = 2_000L + random.nextInt(1_500),
            waitMillis = random.nextInt(3_000).toLong(),
        )
    }

    fun shootingHeadX(
        run: ShootingRun,
        maxSize: Float,
        height: Float,
        eased: Float,
    ): Float = run.startFraction * maxSize -
        sin(run.angleDegrees * PI / 180.0).toFloat() *
        (height + 100f + run.lengthFraction * maxSize) * eased

    fun shootingHeadY(
        run: ShootingRun,
        maxSize: Float,
        height: Float,
        eased: Float,
    ): Float = -100f -
        cos(run.angleDegrees * PI / 180.0).toFloat() *
        (height + 100f + run.lengthFraction * maxSize) * eased

    fun mix(
        from: Color,
        to: Color,
        fraction: Float,
    ): Color = Color(
        red = from.red + (to.red - from.red) * fraction,
        green = from.green + (to.green - from.green) * fraction,
        blue = from.blue + (to.blue - from.blue) * fraction,
        alpha = from.alpha + (to.alpha - from.alpha) * fraction,
    )
}
