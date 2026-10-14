package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AodLandscapePlanTest {

    @Test
    fun noBlocks_isEmpty() {
        val plan = AodLandscapePlan.of(emptyList())

        assertTrue(plan.isEmpty)
        assertFalse(plan.hasMediaWithOthers)
    }

    @Test
    fun weatherWithoutEvents_staysBesideDate() {
        val plan = AodLandscapePlan.of(listOf(AodPanelBlockValue.WEATHER))

        assertTrue(plan.isWeatherBesideDate)
        assertTrue(plan.isEmpty)
    }

    @Test
    fun weatherWithEvents_leadsInfoColumn() {
        val plan = AodLandscapePlan.of(listOf(AodPanelBlockValue.EVENTS, AodPanelBlockValue.WEATHER, AodPanelBlockValue.MEMO))

        assertFalse(plan.isWeatherBesideDate)
        assertEquals(listOf(AodPanelBlockValue.WEATHER, AodPanelBlockValue.EVENTS, AodPanelBlockValue.MEMO), plan.info)
    }

    @Test
    fun latestNotification_comesBeforeMemo() {
        val plan = AodLandscapePlan.of(listOf(AodPanelBlockValue.MEMO, AodPanelBlockValue.NOTIFICATION))

        assertEquals(listOf(AodPanelBlockValue.NOTIFICATION, AodPanelBlockValue.MEMO), plan.info)
    }

    @Test
    fun mediaAlone_isMediaOnly() {
        val plan = AodLandscapePlan.of(listOf(AodPanelBlockValue.MEDIA, AodPanelBlockValue.WEATHER))

        assertTrue(plan.isMediaOnly)
        assertFalse(plan.hasMediaWithOthers)
        assertTrue(plan.isWeatherBesideDate)
    }

    @Test
    fun mediaWithDrawing_hasMediaWithOthers() {
        val plan = AodLandscapePlan.of(listOf(AodPanelBlockValue.DRAWING, AodPanelBlockValue.MEDIA))

        assertTrue(plan.hasMediaWithOthers)
        assertTrue(plan.info.isEmpty())
    }

    @Test
    fun drawingWithEvents_keepsDrawingOutOfInfo() {
        val plan = AodLandscapePlan.of(listOf(AodPanelBlockValue.DRAWING, AodPanelBlockValue.EVENTS))

        assertTrue(plan.hasDrawing)
        assertFalse(plan.hasMedia)
        assertEquals(listOf(AodPanelBlockValue.EVENTS), plan.info)
    }
}
