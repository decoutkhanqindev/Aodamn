package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

// Tùy chỉnh › tab của khối chủ đề tự vẽ dẫn sang màn chủ đề ở tab chỉnh khối đó: chủ đề mới quên tab (như Số áo lúc đầu thiếu Pin & thông
// báo) thì test này báo.
class AodThemeSpecTest {

    @Test
    fun `every own block has exactly one tab`() {
        AodThemeValue.entries.forEach { theme ->
            theme.spec.ownBlocks.forEach { block ->
                val count = theme.spec.tabs.count { tab -> tab.block == block }
                assertEquals("${theme.id}: tab của $block", 1, count)
            }
        }
    }

    @Test
    fun `tabs only point to own blocks`() {
        AodThemeValue.entries.forEach { theme ->
            theme.spec.tabs.mapNotNull { tab -> tab.block }.forEach { block ->
                assertTrue("${theme.id}: $block không phải khối tự vẽ", block in theme.spec.ownBlocks)
            }
        }
    }

    @Test
    fun `tabOf finds the tab of an own block and nothing for null`() {
        val spec = AodThemeValue.JERSEY.spec
        assertEquals(AodBlockValue.STATUS, spec.tabOf(AodBlockValue.STATUS)?.block)
        assertEquals(null, spec.tabOf(null))
        assertEquals(null, AodThemeValue.CLASSIC.spec.tabOf(AodBlockValue.DATE))
    }
}
