package com.decoutkhanqindev.aodamn.presentation.model.aod.settings

import com.decoutkhanqindev.aodamn.data.local.datastore.PrefsJson
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodAppearancePrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodExtrasPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodInteractionPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodNotificationOptionsPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodOptionsPrefs
import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodRulesPrefs
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.toUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodActionValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodGestureValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.AodInteractionUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.toUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.interaction.withAction
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules.AodRulesUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules.AodScheduleUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules.ChargingRuleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules.toUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AodSettingsUiMapperTest {

    @Test
    fun `ui model defaults match the mapped prefs defaults`() {
        assertEquals(AodOptionsPrefs.DEFAULT.toUiModel(), AodOptionsUiModel())
        assertEquals(AodNotificationOptionsPrefs.DEFAULT.toUiModel(), AodNotificationOptionsUiModel())
        assertEquals(AodExtrasPrefs.DEFAULT.toUiModel(), AodExtrasUiModel())
        assertEquals(AodRulesPrefs.DEFAULT.toUiModel(), AodRulesUiModel())
        assertEquals(AodInteractionPrefs.DEFAULT.toUiModel(), AodInteractionUiModel())
        assertEquals(AodAppearancePrefs.DEFAULT.toUiModel(), AodAppearanceUiModel())
    }

    @Test
    fun `options map every field`() {
        val prefs = AodOptionsPrefs(
            isEnabled = false,
            isCustomBrightness = false,
            brightnessPercent = 77,
            isProximityEnabled = false,
            timeoutMinutes = 30,
            autoHideSeconds = 50,
        )

        val expected = AodOptionsUiModel(
            isEnabled = false,
            isCustomBrightness = false,
            brightnessPercent = 77,
            isProximityEnabled = false,
            timeoutMinutes = 30,
            autoHideSeconds = 50,
        )
        assertEquals(expected, prefs.toUiModel())
    }

    @Test
    fun `notification options map every field`() {
        val prefs = AodNotificationOptionsPrefs(
            isIconsEnabled = false,
            isContentEnabled = true,
            isEdgeGlowEnabled = false,
            isMediaControlsEnabled = false,
        )

        val expected = AodNotificationOptionsUiModel(
            isIconsEnabled = false,
            isContentEnabled = true,
            isEdgeGlowEnabled = false,
            isMediaControlsEnabled = false,
        )
        assertEquals(expected, prefs.toUiModel())
    }

    @Test
    fun `extras map every field`() {
        val prefs = AodExtrasPrefs(
            memo = "Gọi mẹ",
            isCalendarEnabled = true,
            isWeatherEnabled = true,
            isWeatherFahrenheit = true,
            isDateEnabled = false,
            isBatteryEnabled = false,
        )

        val expected = AodExtrasUiModel(
            memo = "Gọi mẹ",
            isCalendarEnabled = true,
            isWeatherEnabled = true,
            isWeatherFahrenheit = true,
            isDateEnabled = false,
            isBatteryEnabled = false,
        )
        assertEquals(expected, prefs.toUiModel())
    }

    @Test
    fun `rules map battery, charging rule and schedule`() {
        val prefs = AodRulesPrefs(
            minBatteryPercent = 20,
            chargingRule = "plugged",
            isScheduleEnabled = true,
            scheduleStartMinute = 60,
            scheduleEndMinute = 120,
        )

        val expected = AodRulesUiModel(
            minBattery = 20,
            chargingRule = ChargingRuleValue.PLUGGED,
            schedule = AodScheduleUiModel(isEnabled = true, startMinute = 60, endMinute = 120),
        )
        assertEquals(expected, prefs.toUiModel())
    }

    @Test
    fun `charging rule ids are stable, unique and read back to the same entry`() {
        assertEquals(listOf("always", "plugged", "on_battery"), ChargingRuleValue.entries.map { rule -> rule.id })
        ChargingRuleValue.entries.forEach { rule -> assertEquals(rule, ChargingRuleValue.fromId(rule.id)) }
    }

    @Test
    fun `unknown or missing charging rule id falls back to always`() {
        assertEquals(ChargingRuleValue.ALWAYS, ChargingRuleValue.fromId("solar"))
        assertEquals(ChargingRuleValue.ALWAYS, ChargingRuleValue.fromId(null))
        assertEquals(ChargingRuleValue.ALWAYS, AodRulesPrefs(chargingRule = "solar", minBatteryPercent = 10).toUiModel().chargingRule)
    }

    @Test
    fun `gesture ids are stable and unique`() {
        assertEquals(
            listOf("double_tap", "swipe_up", "swipe_down", "volume_up", "volume_down", "back"),
            AodGestureValue.entries.map { gesture -> gesture.id },
        )
    }

    @Test
    fun `action ids are stable, unique and read back to the same entry`() {
        assertEquals(
            listOf("none", "close", "go_dark", "flashlight", "play_pause", "previous_track", "next_track"),
            AodActionValue.entries.map { action -> action.id },
        )
        AodActionValue.entries.forEach { action -> assertEquals(action, AodActionValue.fromIdOrNull(action.id)) }
        assertNull(AodActionValue.fromIdOrNull("explode"))
        assertNull(AodActionValue.fromIdOrNull(null))
    }

    @Test
    fun `double tap and back close the aod by default, other gestures do nothing`() {
        val ui = AodInteractionPrefs.DEFAULT.toUiModel()

        AodGestureValue.entries.forEach { gesture ->
            val expected = if (gesture == AodGestureValue.DOUBLE_TAP || gesture == AodGestureValue.BACK) {
                AodActionValue.CLOSE
            } else {
                AodActionValue.NONE
            }
            assertEquals(gesture.id, expected, ui.actionOf(gesture))
        }
    }

    @Test
    fun `stored gesture actions override defaults and unknown entries are skipped`() {
        val prefs = AodInteractionPrefs(
            actions = mapOf("swipe_up" to "flashlight", "pinch" to "close", "back" to "explode"),
            isAutoDimEnabled = false,
            isRaiseToWakeEnabled = false,
        )

        val ui = prefs.toUiModel()

        assertEquals(AodActionValue.FLASHLIGHT, ui.actionOf(AodGestureValue.SWIPE_UP))
        assertEquals(AodActionValue.CLOSE, ui.actionOf(AodGestureValue.BACK))
        assertEquals(AodGestureValue.entries.toSet(), ui.actions.keys)
        assertFalse(ui.isAutoDimEnabled)
        assertFalse(ui.isRaiseToWakeEnabled)
    }

    @Test
    fun `saving a gesture action writes ids and keeps the other gestures`() {
        val prefs = AodInteractionPrefs(actions = mapOf("swipe_up" to "flashlight"))

        val updated = prefs.withAction(AodGestureValue.VOLUME_UP, AodActionValue.NEXT_TRACK)

        assertEquals(mapOf("swipe_up" to "flashlight", "volume_up" to "next_track"), updated.actions)
        val json = PrefsJson.encodeToString(AodInteractionPrefs.serializer(), updated)
        assertTrue(json, json.contains("\"volume_up\":\"next_track\""))
    }

    @Test
    fun `every gesture and action survive a save and reload`() {
        val pairs = AodGestureValue.entries.zip(AodActionValue.entries.reversed())
        val prefs = pairs.fold(AodInteractionPrefs.DEFAULT) { acc, (gesture, action) -> acc.withAction(gesture, action) }

        val ui = prefs.toUiModel()

        pairs.forEach { (gesture, action) -> assertEquals(gesture.id, action, ui.actionOf(gesture)) }
    }
}
