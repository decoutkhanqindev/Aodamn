package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import com.decoutkhanqindev.aodamn.data.local.datastore.model.AodAppearancePrefs
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.toUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.classic.ClassicThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.toComponentsUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.withComponents
import kotlinx.collections.immutable.persistentMapOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AodAppearanceMappingTest {

    @Test
    fun `theme without a stored config gets an empty config`() {
        val config = AodAppearancePrefs().themeConfigOf(AodThemeValue.CLASSIC)

        assertEquals(AodThemeConfig(), config)
        assertEquals(ClassicThemeSpec.Size.default, config[ClassicThemeSpec.Size])
    }

    @Test
    fun `stored config is read for its own theme only`() {
        val appearance = AodAppearancePrefs(themeConfigs = mapOf(AodThemeValue.CLASSIC.id to mapOf(ClassicThemeSpec.Size.key to 120)))

        assertEquals(120, appearance.themeConfigOf(AodThemeValue.CLASSIC)[ClassicThemeSpec.Size])
        val configs = appearance.themeConfigs()
        assertEquals(AodThemeValue.entries.toSet(), configs.keys)
        assertTrue(configs.filterKeys { theme -> theme != AodThemeValue.CLASSIC }.values.all { config -> config == AodThemeConfig() })
    }

    @Test
    fun `saving a theme config replaces that theme and keeps the others`() {
        val other = AodThemeValue.entries.first { theme -> theme != AodThemeValue.CLASSIC }
        val appearance = AodAppearancePrefs(
            themeConfigs = mapOf(
                AodThemeValue.CLASSIC.id to mapOf("size" to 120),
                other.id to mapOf("x" to 1),
            ),
        )
        val config = AodThemeConfig(persistentMapOf("size" to 90))

        val updated = appearance.withThemeConfig(AodThemeValue.CLASSIC, config)

        assertEquals(mapOf("size" to 90), updated.themeConfigs.getValue(AodThemeValue.CLASSIC.id))
        assertEquals(mapOf("x" to 1), updated.themeConfigs.getValue(other.id))
        assertEquals(appearance.themeId, updated.themeId)
    }

    @Test
    fun `active theme maps with its config`() {
        val other = AodThemeValue.entries.last()
        val appearance = AodAppearancePrefs(themeId = other.id, themeConfigs = mapOf(other.id to mapOf("k" to 3)))

        val theme = appearance.toThemeUiModel()

        assertEquals(other, theme.value)
        assertEquals(AodThemeConfig(persistentMapOf("k" to 3)), theme.config)
    }

    @Test
    fun `unknown theme id falls back to the default theme`() {
        val theme = AodAppearancePrefs(themeId = "removed_theme").toThemeUiModel()

        assertEquals(AodThemeValue.DEFAULT, theme.value)
    }

    @Test
    fun `component styles round trip through the appearance`() {
        val components = AodComponentsUiModel(AodThemeConfig(persistentMapOf("date_size" to 110)))

        val appearance = AodAppearancePrefs().withComponents(components)

        assertEquals(mapOf("date_size" to 110), appearance.componentStyles)
        assertEquals(components, appearance.toComponentsUiModel())
    }

    @Test
    fun `appearance maps theme, components and landscape`() {
        val appearance = AodAppearancePrefs(componentStyles = mapOf("date_size" to 90), isLandscape = true)

        val ui = appearance.toUiModel()

        assertEquals(AodThemeValue.DEFAULT, ui.theme.value)
        assertEquals(AodThemeConfig(persistentMapOf("date_size" to 90)), ui.components.config)
        assertTrue(ui.isLandscape)
    }
}
