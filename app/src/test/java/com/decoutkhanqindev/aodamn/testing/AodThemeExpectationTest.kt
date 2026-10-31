package com.decoutkhanqindev.aodamn.testing

import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodToggleOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.isDefault
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.isSame
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.options
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.toAodConfigString
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.toAodConfigValues
import kotlinx.collections.immutable.toPersistentMap
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AodThemeExpectationTest {
    private val expectations: List<ThemeExpectation>
        get() {
            val themeId = System.getProperty("aod.theme.test.id")
            return themeId?.let { id ->
                ThemeTestCatalog.expectations.filter { expectation -> expectation.theme.id == id }
            } ?: ThemeTestCatalog.expectations
        }

    @Test
    fun `each selected theme matches its one expectation`() {
        expectations.forEach { expectation ->
            val spec = expectation.theme.spec
            assertEquals(expectation.optionKeys, spec.options.map { option -> option.key })
            assertEquals(expectation.components, spec.components)
            assertEquals(expectation.ownBlocks, spec.ownBlocks)
            assertEquals(expectation.hasNotificationContent, spec.hasNotificationContent)
            assertEquals(expectation.isLiveInPreview, spec.isLiveInPreview)
        }
    }

    @Test
    fun `theme spec generates every cheap option case`() {
        expectations.forEach { expectation ->
            expectation.theme.spec.options.forEach { option ->
                val cases = ThemeOptionCases.all(option)
                val expectedCount = when (option) {
                    is AodChoiceOption<*> -> option.entries.size
                    is AodRangeOption -> listOf(option.range.first, option.default, option.range.last).distinct().size
                    is AodToggleOption -> 2
                    is AodColorOption -> 2
                }
                assertEquals("${expectation.theme.id}: ${option.key}", expectedCount, cases.size)
                cases.forEach { raws ->
                    val config = AodThemeConfig().withRaws(raws)
                    val stored = config.values.toAodConfigString()
                    val restored = AodThemeConfig(stored.toAodConfigValues().toPersistentMap())
                    assertTrue("${expectation.theme.id}: ${option.key}", expectation.theme.spec.isSame(config, restored))
                }
                assertFalse("${expectation.theme.id}: ${option.key} không đổi", option.isDefault(AodThemeConfig().withRaws(ThemeOptionCases.changed(option))))
            }
        }
    }

    @Test
    fun `every option pair survives together and reset returns defaults`() {
        expectations.forEach { expectation ->
            val options = expectation.theme.spec.options
            options.forEachIndexed { firstIndex, first ->
                options.drop(firstIndex + 1).forEach { second ->
                    val changed = AodThemeConfig()
                        .withRaws(ThemeOptionCases.changed(first))
                        .withRaws(ThemeOptionCases.changed(second))
                    assertFalse("${expectation.theme.id}: ${first.key}", first.isDefault(changed))
                    assertFalse("${expectation.theme.id}: ${second.key}", second.isDefault(changed))
                    val stored = changed.values.toAodConfigString()
                    val restored = AodThemeConfig(stored.toAodConfigValues().toPersistentMap())
                    assertTrue(expectation.theme.id, expectation.theme.spec.isSame(changed, restored))
                    assertTrue(expectation.theme.id, expectation.theme.spec.isDefault(changed.without(options)))
                }
            }
        }
    }

    @Test
    fun `selected theme contract differs from every earlier registered theme`() {
        expectations.forEach { expectation ->
            val signature = listOf(
                expectation.optionKeys,
                expectation.components.map { component -> component.name }.sorted(),
                expectation.ownBlocks.map { block -> block.name }.sorted(),
                expectation.hasNotificationContent,
                expectation.isLiveInPreview,
            )
            ThemeTestCatalog.expectations
                .takeWhile { earlier -> earlier.theme != expectation.theme }
                .forEach { earlier ->
                    val earlierSignature = listOf(
                        earlier.optionKeys,
                        earlier.components.map { component -> component.name }.sorted(),
                        earlier.ownBlocks.map { block -> block.name }.sorted(),
                        earlier.hasNotificationContent,
                        earlier.isLiveInPreview,
                    )
                    assertNotEquals(expectation.theme.id, earlierSignature, signature)
                }
        }
    }
}