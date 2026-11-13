package com.decoutkhanqindev.aodamn.presentation.components.aod.theme.meridian

import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.meridian.AodMeridianStyleValue
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId

class MeridianMetricsTest {

    @Test
    fun `scale follows the reference side`() {
        assertEquals(1f, MeridianMetrics.scale(393f), 0.001f)
        assertEquals(2f, MeridianMetrics.scale(786f), 0.001f)
    }

    @Test
    fun `hand angles follow the time`() {
        assertEquals(180f, MeridianMetrics.minuteAngle(30), 0.001f)
        assertEquals(255f, MeridianMetrics.hourAngle(8, 30), 0.001f)
        assertEquals(285f, MeridianMetrics.hourAngle(21, 30), 0.001f)
        assertEquals(0f, MeridianMetrics.hourAngle(0, 0), 0.001f)
    }

    @Test
    fun `zone id falls back to the system default`() {
        assertEquals(ZoneId.of("Asia/Tokyo"), MeridianMetrics.zoneIdOf("Asia/Tokyo"))
        assertEquals(ZoneId.systemDefault(), MeridianMetrics.zoneIdOf("Bad/Zone"))
    }

    @Test
    fun `card styles expose fill and stroke rules`() {
        assertEquals(true, AodMeridianStyleValue.FILLED.isFilled(1))
        assertEquals(true, AodMeridianStyleValue.FIRST.isFilled(0))
        assertEquals(false, AodMeridianStyleValue.FIRST.isFilled(1))
        assertEquals(false, AodMeridianStyleValue.OUTLINE.isFilled(0))
        assertEquals(1f, AodMeridianStyleValue.BOLD.strokeAlpha, 0.001f)
        assertEquals(.196f, AodMeridianStyleValue.OUTLINE.strokeAlpha, 0.001f)
    }
}
