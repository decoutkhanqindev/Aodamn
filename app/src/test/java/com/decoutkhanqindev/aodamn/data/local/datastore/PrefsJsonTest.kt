package com.decoutkhanqindev.aodamn.data.local.datastore

import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodAppearancePrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodExtrasPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodInteractionPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodNotificationOptionsPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodOptionsPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodRulesPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AppShellPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.WeatherCachePrefs
import kotlinx.serialization.KSerializer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PrefsJsonTest {

    @Test
    fun `empty json gives the defaults for every group`() {
        assertEquals(AppShellPrefs.DEFAULT, decode(AppShellPrefs.serializer(), "{}"))
        assertEquals(AodOptionsPrefs.DEFAULT, decode(AodOptionsPrefs.serializer(), "{}"))
        assertEquals(AodRulesPrefs.DEFAULT, decode(AodRulesPrefs.serializer(), "{}"))
        assertEquals(AodNotificationOptionsPrefs.DEFAULT, decode(AodNotificationOptionsPrefs.serializer(), "{}"))
        assertEquals(AodInteractionPrefs.DEFAULT, decode(AodInteractionPrefs.serializer(), "{}"))
        assertEquals(AodExtrasPrefs.DEFAULT, decode(AodExtrasPrefs.serializer(), "{}"))
        assertEquals(AodAppearancePrefs.DEFAULT, decode(AodAppearancePrefs.serializer(), "{}"))
        assertEquals(WeatherCachePrefs(), decode(WeatherCachePrefs.serializer(), "{}"))
    }

    @Test
    fun `defaults match the product decisions`() {
        assertEquals(AppShellPrefs("en", isFirstOpen = true, isCustomizeGuideShown = false, lastWakeCode = 0), AppShellPrefs.DEFAULT)
        assertEquals(
            AodOptionsPrefs(
                isEnabled = true,
                isCustomBrightness = true,
                brightnessPercent = 1,
                isProximityEnabled = true,
                timeoutMinutes = 0,
                autoHideSeconds = 30,
            ),
            AodOptionsPrefs.DEFAULT,
        )
        assertEquals(
            AodRulesPrefs(
                minBatteryPercent = 0,
                chargingRule = "always",
                isScheduleEnabled = false,
                scheduleStartMinute = 420,
                scheduleEndMinute = 1_380,
            ),
            AodRulesPrefs.DEFAULT,
        )
        assertEquals(
            AodNotificationOptionsPrefs(
                isIconsEnabled = true,
                isContentEnabled = false,
                isEdgeGlowEnabled = true,
                isMediaControlsEnabled = true,
            ),
            AodNotificationOptionsPrefs.DEFAULT,
        )
        assertEquals(
            AodExtrasPrefs(
                memo = "",
                isCalendarEnabled = false,
                isWeatherEnabled = false,
                isWeatherFahrenheit = false,
                isDateEnabled = true,
                isBatteryEnabled = true,
            ),
            AodExtrasPrefs.DEFAULT,
        )
        assertEquals("classic", AodAppearancePrefs.DEFAULT.themeId)
    }

    @Test
    fun `app shell round trips through json`() {
        val prefs = AppShellPrefs(selectedLangCode = "vi", isFirstOpen = false, isCustomizeGuideShown = true, lastWakeCode = 2)

        assertEquals(prefs, roundTrip(prefs, AppShellPrefs.serializer()))
    }

    @Test
    fun `options round trip through json`() {
        val prefs = AodOptionsPrefs(
            isEnabled = false,
            isCustomBrightness = false,
            brightnessPercent = 42,
            isProximityEnabled = false,
            timeoutMinutes = 15,
            autoHideSeconds = 10,
        )

        assertEquals(prefs, roundTrip(prefs, AodOptionsPrefs.serializer()))
    }

    @Test
    fun `rules round trip through json`() {
        val prefs = AodRulesPrefs(
            minBatteryPercent = 25,
            chargingRule = "on_battery",
            isScheduleEnabled = true,
            scheduleStartMinute = 22 * 60,
            scheduleEndMinute = 6 * 60,
        )

        assertEquals(prefs, roundTrip(prefs, AodRulesPrefs.serializer()))
    }

    @Test
    fun `notification options round trip through json`() {
        val prefs = AodNotificationOptionsPrefs(
            isIconsEnabled = false,
            isContentEnabled = true,
            isEdgeGlowEnabled = false,
            isMediaControlsEnabled = false,
        )

        assertEquals(prefs, roundTrip(prefs, AodNotificationOptionsPrefs.serializer()))
    }

    @Test
    fun `interaction round trips the action map`() {
        val prefs = AodInteractionPrefs(
            actions = mapOf("double_tap" to "go_dark", "volume_up" to "next_track"),
            isAutoDimEnabled = false,
            isRaiseToWakeEnabled = false,
        )

        assertEquals(prefs, roundTrip(prefs, AodInteractionPrefs.serializer()))
    }

    @Test
    fun `extras round trip text with quotes and unicode`() {
        val prefs = AodExtrasPrefs(
            memo = "Mua sữa \"tươi\"",
            isCalendarEnabled = true,
            isWeatherEnabled = true,
            isWeatherFahrenheit = true,
            isDateEnabled = false,
            isBatteryEnabled = false,
        )

        assertEquals(prefs, roundTrip(prefs, AodExtrasPrefs.serializer()))
    }

    @Test
    fun `appearance round trips nested theme configs and component styles`() {
        val prefs = AodAppearancePrefs(
            themeId = "compass",
            themeConfigs = mapOf(
                "classic" to mapOf("face" to 2, "size" to 120),
                "compass" to mapOf("color" to -16_711_936),
            ),
            componentStyles = mapOf("date_size" to 110, "weather_color" to -1),
            isLandscape = true,
            wallpaperCode = 7,
        )

        assertEquals(prefs, roundTrip(prefs, AodAppearancePrefs.serializer()))
    }

    @Test
    fun `weather cache round trips and keeps missing values null`() {
        val prefs = WeatherCachePrefs(temperatureCelsius = -3.5, weatherCode = 71, isDay = false, updatedAtMillis = 1_700_000_000_000)

        assertEquals(prefs, roundTrip(prefs, WeatherCachePrefs.serializer()))
        assertEquals("{}", PrefsJson.encodeToString(WeatherCachePrefs.serializer(), WeatherCachePrefs()))
    }

    @Test
    fun `missing fields keep the stored ones and default the rest`() {
        val prefs = decode(AodOptionsPrefs.serializer(), """{"brightnessPercent":60}""")

        assertEquals(AodOptionsPrefs.DEFAULT.copy(brightnessPercent = 60), prefs)
    }

    @Test
    fun `unknown json keys are ignored`() {
        val prefs = decode(AodExtrasPrefs.serializer(), """{"memo":"hi","removedField":true,"other":{"a":1}}""")

        assertEquals(AodExtrasPrefs.DEFAULT.copy(memo = "hi"), prefs)
    }

    @Test
    fun `null values decode to the field default`() {
        val prefs = decode(AodOptionsPrefs.serializer(), """{"isEnabled":null,"timeoutMinutes":5}""")

        assertEquals(AodOptionsPrefs.DEFAULT.copy(timeoutMinutes = 5), prefs)
    }

    @Test
    fun `broken json or wrong types decode to null`() {
        assertNull(PrefsJson.decodeOrNull(AodOptionsPrefs.serializer(), "{not json"))
        assertNull(PrefsJson.decodeOrNull(AodOptionsPrefs.serializer(), ""))
        assertNull(PrefsJson.decodeOrNull(AodOptionsPrefs.serializer(), """{"brightnessPercent":"bright"}"""))
        assertNull(PrefsJson.decodeOrNull(AodAppearancePrefs.serializer(), """{"themeConfigs":[1,2]}"""))
        assertNull(PrefsJson.decodeOrNull(AodInteractionPrefs.serializer(), """{"actions":{"back":1}}"""))
    }

    @Test
    fun `every field is written, including the ones still at their default`() {
        val json = PrefsJson.encodeToString(AppShellPrefs.serializer(), AppShellPrefs.DEFAULT.copy(selectedLangCode = "vi"))

        listOf("selectedLangCode", "isFirstOpen", "isCustomizeGuideShown", "lastWakeCode").forEach { field ->
            assertTrue(json, json.contains("\"$field\":"))
        }
    }

    @Test
    fun `a saved choice equal to today's default survives a later default change`() {
        val json = PrefsJson.encodeToString(AodOptionsPrefs.serializer(), AodOptionsPrefs.DEFAULT)

        assertTrue(json, json.contains("\"autoHideSeconds\":30"))
        assertFalse(json, json.contains("null"))
    }

    private fun <T> decode(serializer: KSerializer<T>, raw: String): T =
        requireNotNull(PrefsJson.decodeOrNull(serializer, raw)) { raw }

    private fun <T> roundTrip(value: T, serializer: KSerializer<T>): T =
        decode(serializer, PrefsJson.encodeToString(serializer, value))
}
