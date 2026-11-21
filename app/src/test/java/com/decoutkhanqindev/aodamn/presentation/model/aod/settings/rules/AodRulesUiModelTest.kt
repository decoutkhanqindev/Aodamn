package com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AodRulesUiModelTest {

    @Test
    fun `disabled schedule is always active`() {
        val schedule = AodScheduleUiModel(isEnabled = false, startMinute = 60, endMinute = 120)

        assertTrue(schedule.isActiveAt(0))
        assertTrue(schedule.isActiveAt(600))
    }

    @Test
    fun `same start and end means the whole day`() {
        val schedule = AodScheduleUiModel(isEnabled = true, startMinute = 300, endMinute = 300)

        assertTrue(schedule.isActiveAt(0))
        assertTrue(schedule.isActiveAt(1_439))
    }

    @Test
    fun `daytime schedule includes the start and excludes the end`() {
        val schedule = AodScheduleUiModel(isEnabled = true, startMinute = 7 * 60, endMinute = 23 * 60)

        assertTrue(schedule.isActiveAt(7 * 60))
        assertTrue(schedule.isActiveAt(23 * 60 - 1))
        assertFalse(schedule.isActiveAt(23 * 60))
        assertFalse(schedule.isActiveAt(6 * 60 + 59))
    }

    @Test
    fun `overnight schedule wraps past midnight`() {
        val schedule = AodScheduleUiModel(isEnabled = true, startMinute = 22 * 60, endMinute = 6 * 60)

        assertTrue(schedule.isActiveAt(22 * 60))
        assertTrue(schedule.isActiveAt(0))
        assertTrue(schedule.isActiveAt(6 * 60 - 1))
        assertFalse(schedule.isActiveAt(6 * 60))
        assertFalse(schedule.isActiveAt(12 * 60))
    }

    @Test
    fun `charging rule follows the plug state and allows unknown plug state`() {
        assertTrue(ChargingRuleValue.ALWAYS.allows(isPlugged = false))
        assertTrue(ChargingRuleValue.PLUGGED.allows(isPlugged = true))
        assertFalse(ChargingRuleValue.PLUGGED.allows(isPlugged = false))
        assertTrue(ChargingRuleValue.PLUGGED.allows(isPlugged = null))
        assertTrue(ChargingRuleValue.ON_BATTERY.allows(isPlugged = false))
        assertFalse(ChargingRuleValue.ON_BATTERY.allows(isPlugged = true))
        assertTrue(ChargingRuleValue.ON_BATTERY.allows(isPlugged = null))
    }

    @Test
    fun `low battery blocks the aod unless it is charging`() {
        val rules = AodRulesUiModel(minBattery = 20)

        assertFalse(rules.allows(batteryPercent = 19, isCharging = false, isPlugged = false, minuteOfDay = 0))
        assertTrue(rules.allows(batteryPercent = 19, isCharging = true, isPlugged = true, minuteOfDay = 0))
        assertTrue(rules.allows(batteryPercent = 20, isCharging = false, isPlugged = false, minuteOfDay = 0))
        assertTrue(rules.allows(batteryPercent = null, isCharging = false, isPlugged = false, minuteOfDay = 0))
    }

    @Test
    fun `rules need the charging rule, the schedule and the battery to agree`() {
        val rules = AodRulesUiModel(
            chargingRule = ChargingRuleValue.PLUGGED,
            schedule = AodScheduleUiModel(isEnabled = true, startMinute = 60, endMinute = 120),
        )

        assertTrue(rules.allows(batteryPercent = 80, isCharging = true, isPlugged = true, minuteOfDay = 90))
        assertFalse(rules.allows(batteryPercent = 80, isCharging = false, isPlugged = false, minuteOfDay = 90))
        assertFalse(rules.allows(batteryPercent = 80, isCharging = true, isPlugged = true, minuteOfDay = 200))
    }
}
