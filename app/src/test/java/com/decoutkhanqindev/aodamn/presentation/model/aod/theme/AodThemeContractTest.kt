package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import com.decoutkhanqindev.aodamn.testing.ThemeOptionCases
import kotlinx.collections.immutable.toPersistentMap
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AodThemeContractTest {
    private val selectedThemes: List<AodThemeValue>
        get() {
            val themeId = System.getProperty("aod.theme.test.id")
            return themeId?.let { id -> AodThemeValue.entries.filter { theme -> theme.id == id } } ?: AodThemeValue.entries
        }

    @Test
    fun `theme ids and option storage keys stay unique`() {
        assertEquals(AodThemeValue.entries.size, AodThemeValue.entries.map { theme -> theme.id }.distinct().size)

        selectedThemes.forEach { theme ->
            val keys = theme.spec.options.flatMap { option -> option.storedKeys }
            assertEquals("${theme.id}: khóa trùng", keys.size, keys.distinct().size)
            keys.forEach { key ->
                assertTrue("${theme.id}: khóa sai $key", key.matches(Regex("[a-z0-9_]+")))
            }
        }
    }

    @Test
    fun `every option decodes its encoded default`() {
        selectedThemes.forEach { theme ->
            theme.spec.options.forEach { option ->
                assertEquals("${theme.id}: ${option.key}", option.default, option.decode(option.encodeDefault()))
            }
        }
    }

    @Test
    fun `every changed theme config survives storage round trip`() {
        selectedThemes.forEach { theme ->
            val changed = ThemeOptionCases.maximum(theme)
            val stored = changed.values.toAodConfigString()
            val restored = AodThemeConfig(stored.toAodConfigValues().toPersistentMap())

            assertTrue("${theme.id}: cấu hình không khôi phục được", theme.spec.isSame(changed, restored))
        }
    }

    private fun AodThemeOption<*>.encodeDefault(): Int = when (this) {
        is AodChoiceOption<*> -> default.code
        is AodColorOption -> encode(default)
        is AodRangeOption -> encode(default)
        is AodToggleOption -> encode(default)
    }
}