package com.decoutkhanqindev.aodamn.presentation.screens.aod.state

import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tabOf
import com.decoutkhanqindev.aodamn.presentation.screens.themeeditor.state.ThemeEditorState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AodStatePreviewTest {
    @Test
    fun `real aod never forces preview blocks`() {
        val state = AodState(isPreview = false, previewComponent = AodComponentValue.MEDIA)

        AodComponentValue.entries.forEach { component ->
            assertFalse(state.isPreviewShown(component))
            assertFalse(state.isPreviewFocused(component))
        }
    }

    @Test
    fun `still thumbnails never force preview blocks`() {
        val state = AodState(isPreview = true, isStill = true, previewComponent = AodComponentValue.MEDIA)

        AodComponentValue.entries.forEach { component ->
            assertFalse(state.isPreviewShown(component))
            assertFalse(state.isPreviewFocused(component))
        }
    }

    @Test
    fun `preview without a focused tab shows blocks but focuses none`() {
        val state = AodState(isPreview = true)

        assertTrue(state.isPreviewShown(AodComponentValue.MEDIA))
        assertFalse(state.isPreviewFocused(AodComponentValue.MEDIA))
        assertFalse(state.isPreviewFocused(AodComponentValue.NOTIFICATIONS))
    }

    @Test
    fun `preview with a focused tab shows and focuses only that block`() {
        val state = AodState(isPreview = true, previewComponent = AodComponentValue.MEDIA)

        assertTrue(state.isPreviewShown(AodComponentValue.MEDIA))
        assertTrue(state.isPreviewFocused(AodComponentValue.MEDIA))
        assertFalse(state.isPreviewShown(AodComponentValue.BATTERY))
        assertFalse(state.isPreviewFocused(AodComponentValue.NOTIFICATIONS))
    }

    @Test
    fun `theme editor focuses the block of the selected tab only while editing`() {
        val theme = AodThemeValue.PIXEL
        val mediaTab = theme.spec.tabOf(AodBlockValue.MEDIA)

        assertEquals(AodComponentValue.MEDIA, ThemeEditorState(theme = theme, isEditing = true, selectedTab = mediaTab).previewComponent)
        assertNull(ThemeEditorState(theme = theme, isEditing = false, selectedTab = mediaTab).previewComponent)
        assertNull(ThemeEditorState(theme = theme, isEditing = true, selectedTab = theme.spec.tabs.first()).previewComponent)
    }
}
