package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.compass.AodCompassStyleValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.compass.CompassThemeSpec
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.custom_aod.presentation.screens.aod.state.AodState
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

    // Chủ đề mới thêm khối tự vẽ / bật lại nội dung thông báo không được đổi hành vi chủ đề cũ: chỉ Chuỗi tự vẽ ghi nhớ, sự kiện; chỉ Chuỗi và La
    // bàn hiện nội dung thông báo dù tự vẽ dòng trạng thái (tự vẽ thông báo mới nhất như app mẫu); các chủ đề khác giữ như trước (nội dung thông
    // báo chỉ khi không tự vẽ dòng trạng thái).
    @Test
    fun `only chain owns memo and events and only chain and compass keep notification content with their status line`() {
        AodThemeValue.entries.forEach { theme ->
            val isChain = theme == AodThemeValue.CHAIN
            val drawsLatest = isChain || theme == AodThemeValue.COMPASS
            assertEquals("${theme.id}: ghi nhớ", isChain, AodBlockValue.MEMO in theme.spec.ownBlocks)
            assertEquals("${theme.id}: sự kiện", isChain, AodBlockValue.EVENTS in theme.spec.ownBlocks)
            assertEquals(
                "${theme.id}: nội dung thông báo",
                drawsLatest || AodBlockValue.STATUS !in theme.spec.ownBlocks,
                theme.spec.hasNotificationContent,
            )
        }
    }

    // Vành chỉ có các khối vừa vùng trống quanh kim (mock đã duyệt): ghi nhớ, sự kiện, nét vẽ, nội dung thông báo bỏ hẳn, không lẻn lại khi thêm
    // khối dùng chung mới.
    @Test
    fun `bezel only has the blocks that fit around its hands`() {
        val spec = AodThemeValue.BEZEL.spec
        assertEquals(
            setOf(
                AodComponentValue.DATE,
                AodComponentValue.BATTERY,
                AodComponentValue.WEATHER,
                AodComponentValue.NOTIFICATIONS,
                AodComponentValue.MEDIA,
            ),
            spec.components,
        )
        assertEquals(false, spec.hasNotificationContent)
    }

    // La bàn chỉ có các khối đặt cân quanh mặt số tròn (mock đã duyệt): ghi nhớ, sự kiện, nét vẽ bỏ hẳn; thông báo mới nhất do chủ đề tự vẽ.
    @Test
    fun `compass only has the blocks that fit around its dial and draws the latest alert`() {
        val spec = AodThemeValue.COMPASS.spec
        assertEquals(
            setOf(
                AodComponentValue.DATE,
                AodComponentValue.BATTERY,
                AodComponentValue.WEATHER,
                AodComponentValue.NOTIFICATIONS,
                AodComponentValue.MEDIA,
            ),
            spec.components,
        )
        assertEquals(true, spec.hasNotificationContent)
        // Đủ tuỳ chọn của app mẫu: Clock Style 1 (có vòng giây, mặc định) / 2 (không vòng giây).
        assertEquals(AodCompassStyleValue.entries, CompassThemeSpec.Style.entries)
        assertEquals(AodCompassStyleValue.SECONDS, CompassThemeSpec.Style.default)
    }

    // Khung xem trước dùng giờ mẫu; chỉ chủ đề có chuyển động theo giờ thật như app mẫu (La bàn) chạy theo giờ thật cả ở xem trước, chủ đề cũ
    // không đổi.
    @Test
    fun `only compass runs in real time in previews`() {
        AodThemeValue.entries.forEach { theme ->
            assertEquals("${theme.id}: xem trước theo giờ thật", theme == AodThemeValue.COMPASS, theme.spec.isLiveInPreview)
            val preview = AodState(appearance = AodAppearanceUiModel(theme = AodThemeUiModel(value = theme)), isPreview = true)
            assertEquals("${theme.id}: mặt số khung xem trước", theme.spec.isLiveInPreview, preview.isClockLive)
            assertEquals("${theme.id}: mặt số AOD thật", true, preview.copy(isPreview = false).isClockLive)
            assertEquals("${theme.id}: thẻ ở tab AOD", false, preview.copy(isStill = true).isClockLive)
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
