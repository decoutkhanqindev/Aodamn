package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.moonphase

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

class MoonPhaseMetricsTest {

    @Test
    fun `lunar phase recognizes known new and full moons`() {
        assertEquals(
            0f,
            MoonPhaseMetrics.lunarPhase(
                millis = 947_182_560_000L,
                tzOffsetMinutes = 0,
            ),
            .02f,
        )
        assertEquals(
            1f,
            MoonPhaseMetrics.lunarPhase(
                millis = 948_458_880_000L,
                tzOffsetMinutes = 0,
            ),
            .02f,
        )
    }

    @Test
    fun `resolve phase uses peek and floors live phase`() {
        assertEquals(
            .6f,
            MoonPhaseMetrics.resolvePhase(lunarPhase = .1234f, peek = 30),
            0f,
        )
        assertEquals(
            .678f,
            MoonPhaseMetrics.resolvePhase(lunarPhase = .6789f, peek = 0),
            0f,
        )
    }

    @Test
    fun `minute of hour stays positive for negative local time`() {
        assertEquals(
            30f,
            MoonPhaseMetrics.minuteOfHour(-1_800_000L),
            0f,
        )
    }

    @Test
    fun `tilt follows local twelve hour time`() {
        assertEquals(
            8.5f,
            MoonPhaseMetrics.tiltDegrees(30_600_000L),
            0f,
        )
    }

    @Test
    fun `orbit repeats every four minutes`() {
        assertEquals(90f, MoonPhaseMetrics.orbitDegrees(1f), 0f)
        assertEquals(90f, MoonPhaseMetrics.orbitDegrees(5f), 0f)
    }

    @Test
    fun `sky rotates through a quarter turn in two minutes`() {
        assertEquals(90f, MoonPhaseMetrics.skyDegrees(2f), 0f)
    }

    @Test
    fun `twinkle rises then falls`() {
        assertEquals(0f, MoonPhaseMetrics.twinkle(0L), 0f)
        assertEquals(1f, MoonPhaseMetrics.twinkle(250L), 0f)
        assertEquals(0f, MoonPhaseMetrics.twinkle(500L), 0f)
    }

    @Test
    fun `moon geometry follows portrait and landscape regions`() {
        val portrait = MoonPhaseMetrics.moonGeometry(
            width = 393f,
            height = 873f,
            marginPx = 60f,
            characterSize = 100,
            isLandscape = false,
        )
        val landscape = MoonPhaseMetrics.moonGeometry(
            width = 873f,
            height = 393f,
            marginPx = 60f,
            characterSize = 100,
            isLandscape = true,
        )

        assertEquals(83.25f, portrait.diameter, .001f)
        assertEquals(55.5f, portrait.orbit, .001f)
        assertEquals(196.5f, portrait.centerX, .001f)
        assertEquals(487.425f, portrait.centerY, .001f)
        assertEquals(83.25f, landscape.diameter, .001f)
        assertEquals(55.5f, landscape.orbit, .001f)
        assertEquals(305.55f, landscape.centerX, .001f)
        assertEquals(196.5f, landscape.centerY, .001f)
    }

    @Test
    fun `fixed star seed repeats every field and keeps approved counts`() {
        val first = MoonPhaseMetrics.buildStars(Random(MoonPhaseMetrics.STILL_SEED))
        val second = MoonPhaseMetrics.buildStars(Random(MoonPhaseMetrics.STILL_SEED))

        assertTrue(first.smallX.size in 80..128)
        assertTrue(first.bigX.size in 12..24)
        assertArrayEquals(first.smallX, second.smallX, 0f)
        assertArrayEquals(first.smallY, second.smallY, 0f)
        assertArrayEquals(first.smallBucket, second.smallBucket)
        assertArrayEquals(first.bigX, second.bigX, 0f)
        assertArrayEquals(first.bigY, second.bigY, 0f)
        assertArrayEquals(first.bigSize, second.bigSize, 0f)
        assertArrayEquals(first.bigAlpha, second.bigAlpha)
        assertArrayEquals(first.bigTwinkles, second.bigTwinkles)
    }

    @Test
    fun `small bucket points contain every small star coordinate`() {
        val stars = MoonPhaseMetrics.buildStars(Random(MoonPhaseMetrics.STILL_SEED))
        val points = MoonPhaseMetrics.smallBucketPoints(
            stars = stars,
            maxSize = 1_000f,
        )

        assertEquals(
            stars.smallX.size * 2,
            points.sumOf { bucket -> bucket.size },
        )
    }

    @Test
    fun `shooting head starts above the field and reaches its path end`() {
        val run = MoonPhaseMetrics.ShootingRun(
            startFraction = .75f,
            lengthFraction = .25f,
            angleDegrees = 135f,
            durationMillis = 2_000L,
            waitMillis = 0L,
        )

        assertEquals(
            300f,
            MoonPhaseMetrics.shootingHeadX(
                run = run,
                maxSize = 400f,
                height = 300f,
                eased = 0f,
            ),
            .001f,
        )
        assertEquals(
            -100f,
            MoonPhaseMetrics.shootingHeadY(
                run = run,
                maxSize = 400f,
                height = 300f,
                eased = 0f,
            ),
            .001f,
        )
        assertEquals(
            -53.553f,
            MoonPhaseMetrics.shootingHeadX(
                run = run,
                maxSize = 400f,
                height = 300f,
                eased = 1f,
            ),
            .001f,
        )
        assertEquals(
            253.553f,
            MoonPhaseMetrics.shootingHeadY(
                run = run,
                maxSize = 400f,
                height = 300f,
                eased = 1f,
            ),
            .001f,
        )
    }

    @Test
    fun `shooting run points left when it starts in the first half`() {
        val random = Random(MoonPhaseMetrics.STILL_SEED)
        var run = MoonPhaseMetrics.shootingRun(random)
        while (run.startFraction >= .5f) {
            run = MoonPhaseMetrics.shootingRun(random)
        }

        assertTrue(run.angleDegrees < 0f)
    }
}
