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

    // Chủ đề mới thêm khối tự vẽ / bật lại nội dung thông báo không được đổi hành vi chủ đề cũ: chỉ Chuỗi tự vẽ ghi nhớ, sự kiện và hiện nội dung
    // thông báo dù tự vẽ dòng trạng thái; các chủ đề khác giữ như trước (nội dung thông báo chỉ khi không tự vẽ dòng trạng thái).
    @Test
    fun `only chain owns memo and events and keeps notification content with its status line`() {
        AodThemeValue.entries.forEach { theme ->
            val isChain = theme == AodThemeValue.CHAIN
            assertEquals("${theme.id}: ghi nhớ", isChain, AodBlockValue.MEMO in theme.spec.ownBlocks)
            assertEquals("${theme.id}: sự kiện", isChain, AodBlockValue.EVENTS in theme.spec.ownBlocks)
            assertEquals(
                "${theme.id}: nội dung thông báo",
                isChain || AodBlockValue.STATUS !in theme.spec.ownBlocks,
                theme.spec.hasNotificationContent,
            )
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
