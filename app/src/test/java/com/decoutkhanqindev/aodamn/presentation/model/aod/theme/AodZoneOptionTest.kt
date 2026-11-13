package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import com.decoutkhanqindev.aodamn.R
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.TimeZone

class AodZoneOptionTest {
    private val option = AodZoneOption(
        key = "zone",
        labelRes = R.string.theme_opt_zone,
        default = "Asia/Ho_Chi_Minh",
    )

    @Test
    fun `decode returns default for null and unknown values`() {
        assertEquals(option.default, option.decode(null))

        val zoneCodes = TimeZone.getAvailableIDs().map { id -> id.hashCode() }.toSet()
        val unknownCode = generateSequence(0) { code -> code + 1 }.first { code -> code !in zoneCodes }

        assertEquals(option.default, option.decode(unknownCode))
    }

    @Test
    fun `decode returns encoded time zones`() {
        assertEquals("Asia/Tokyo", option.decode(option.encode("Asia/Tokyo")))
        assertEquals("America/New_York", option.decode(option.encode("America/New_York")))
    }

    @Test
    fun `available time zone IDs have distinct hash codes`() {
        val zoneIds = TimeZone.getAvailableIDs()

        assertEquals(zoneIds.size, zoneIds.map { id -> id.hashCode() }.distinct().size)
    }

    @Test
    fun `offset text formats whole and fractional hour offsets`() {
        assertEquals("GMT", AodZoneUiModel.offsetTextOf(0))
        assertEquals("GMT+7", AodZoneUiModel.offsetTextOf(420))
        assertEquals("GMT−5", AodZoneUiModel.offsetTextOf(-300))
        assertEquals("GMT+5:30", AodZoneUiModel.offsetTextOf(330))
        assertEquals("GMT+5:45", AodZoneUiModel.offsetTextOf(345))
        assertEquals("GMT−3:30", AodZoneUiModel.offsetTextOf(-210))
    }

    @Test
    fun `search key ignores case, Vietnamese marks and minus sign`() {
        assertEquals("tp ho chi minh", AodZoneUiModel.searchKeyOf("TP Hồ Chí Minh"))
        assertEquals("da nang", AodZoneUiModel.searchKeyOf("Đà Nẵng"))
        assertEquals("gmt-5", AodZoneUiModel.searchKeyOf("GMT−5"))
        assertEquals("new york", AodZoneUiModel.searchKeyOf("  New York "))
    }

    @Test
    fun `zone region derives recognized prefixes`() {
        assertEquals(AodZoneRegionValue.ASIA, AodZoneRegionValue.of("Asia/Tokyo"))
        assertEquals(AodZoneRegionValue.AMERICA, AodZoneRegionValue.of("America/Argentina/Buenos_Aires"))
        assertEquals(null, AodZoneRegionValue.of("Etc/GMT+3"))
        assertEquals(null, AodZoneRegionValue.of("UTC"))
    }
}
