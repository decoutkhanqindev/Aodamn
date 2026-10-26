package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.AodCompassStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.CompassThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.GradientSwatchValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gradientline.GradientLineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.halfmoon.HalfMoonThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.ripple.RippleThemeSpec
// TODO(coum-face): import spec
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradientDirection
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tick.TickThemeSpec
// TODO(big-overlap): import spec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
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

    // Chủ đề mới thêm khối tự vẽ / bật lại nội dung thông báo không được đổi hành vi chủ đề cũ: chỉ Chuỗi tự vẽ ghi nhớ, sự kiện; chỉ Chuỗi và
    // các chủ đề trong drawsLatest hiện nội dung thông báo dù tự vẽ dòng trạng thái (tự vẽ thông báo mới nhất như app mẫu); các chủ đề khác
    // giữ như trước (nội dung thông báo chỉ khi không tự vẽ dòng trạng thái).
    @Test
    fun `only chain owns memo and events and only themes drawing their latest alert keep notification content with their status line`() {
        AodThemeValue.entries.forEach { theme ->
            val isChain = theme == AodThemeValue.CHAIN
            val drawsLatest = isChain ||
                theme in setOf(
                    AodThemeValue.COMPASS,
                    AodThemeValue.HALF_MOON,
                    AodThemeValue.GRADIENT_LINE,
                    AodThemeValue.RIPPLE,
                    // TODO(coum-face): thêm nếu chủ đề tự vẽ thông báo mới nhất
                    AodThemeValue.TICK,
                    // TODO(big-overlap): thêm nếu chủ đề tự vẽ thông báo mới nhất
                )
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

    // Bán nguyệt chỉ có các khối đặt cân quanh nửa mặt số (mock đã duyệt): ghi nhớ, sự kiện, nét vẽ bỏ hẳn; thông báo mới nhất do chủ đề tự vẽ
    // dưới dòng pin. Đủ 6 màu của app mẫu, màu điều khiển tách thành dòng pin và nhạc.
    @Test
    fun `half moon only has the blocks that fit around its half dial and keeps every reference color`() {
        val spec = AodThemeValue.HALF_MOON.spec
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
        assertEquals(
            listOf(
                HalfMoonThemeSpec.HourColor,
                HalfMoonThemeSpec.MinuteColor,
                HalfMoonThemeSpec.RingColor,
                HalfMoonThemeSpec.DialColor,
                HalfMoonThemeSpec.DateColor,
                HalfMoonThemeSpec.StatusColor,
                HalfMoonThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }

    // Dải màu chỉ có các khối đặt được trên / dưới vạch (mock đã duyệt): ghi nhớ, sự kiện, nét vẽ bỏ hẳn; thông báo mới nhất do chủ đề tự vẽ trên
    // dòng pin. Đủ tuỳ chọn của app mẫu: cỡ 80–150% (mặc định 110%), vị trí mặc định theo giờ, vạch mặc định gradient của app mẫu.
    @Test
    fun `gradient line only has the blocks that fit along its line and keeps every reference option`() {
        val spec = AodThemeValue.GRADIENT_LINE.spec
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
        assertEquals(80..150, GradientLineThemeSpec.Size.range)
        assertEquals(110, GradientLineThemeSpec.Size.default)
        assertEquals(AodRangeOption.AUTO, AodThemeConfig()[GradientLineThemeSpec.Position])
        assertEquals(GradientSwatchValue.SUNSET.gradient, AodThemeConfig().gradientOf(GradientLineThemeSpec.LineColor))
    }

    // Vị trí ghim kéo được trong 0–100%; giá trị tự động chỉ hợp lệ ở thuộc tính có chế độ tự động, thuộc tính cũ vẫn kẹp vào range như trước.
    @Test
    fun `auto range keeps the auto value and old ranges still clamp it`() {
        val position = GradientLineThemeSpec.Position
        assertEquals(AodRangeOption.AUTO, position.decode(AodRangeOption.AUTO))
        assertEquals(35, position.decode(35))
        assertEquals(100, position.decode(140))
        assertEquals(80, GradientLineThemeSpec.Size.decode(AodRangeOption.AUTO))
    }

    // Gợn sóng chỉ có các khối đặt cân quanh mặt số giữa màn (mock đã duyệt): ghi nhớ, sự kiện, nét vẽ bỏ hẳn; thông báo mới nhất do chủ đề tự vẽ
    // dưới hàng icon. Đủ tuỳ chọn của app mẫu: kiểu đặc / viền (mặc định đặc), cỡ 60–110% (mặc định 80%), màu kim giờ, kim phút, chấm giây, mặt số,
    // ngày; màu chủ đạo tách thành thông báo, pin, nhạc, nền thẻ nhạc.
    @Test
    fun `ripple only has the blocks that fit around its dial and keeps every reference option`() {
        val spec = AodThemeValue.RIPPLE.spec
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
        assertEquals(AodTileStyleValue.entries, RippleThemeSpec.Style.entries)
        assertEquals(AodTileStyleValue.FILLED, RippleThemeSpec.Style.default)
        assertEquals(60..110, RippleThemeSpec.Size.range)
        assertEquals(80, RippleThemeSpec.Size.default)
        assertEquals(
            listOf(
                RippleThemeSpec.Style,
                RippleThemeSpec.Size,
                RippleThemeSpec.HourColor,
                RippleThemeSpec.MinuteColor,
                RippleThemeSpec.SecondColor,
                RippleThemeSpec.FaceColor,
                RippleThemeSpec.DateColor,
                RippleThemeSpec.NotificationsColor,
                RippleThemeSpec.BatteryColor,
                RippleThemeSpec.MediaColor,
                RippleThemeSpec.MediaBackgroundColor,
            ),
            spec.options,
        )
    }

    // TODO(coum-face): test riêng của chủ đề
    // Nhịp giây chỉ có các khối đặt cân quanh vòng (mock đã duyệt): ghi nhớ, sự kiện, nét vẽ bỏ hẳn. Giữ đủ tuỳ chọn của app mẫu: Clock Size (55–90,
    // mặc định 70), màu vòng (gradient dọc mặc định, màu nhấn Tropic), màu đồng hồ; màu đồng hồ / điều khiển tách thành ngày, thông báo, pin, nhạc.
    @Test
    fun `tick only has the blocks that fit around its ring and keeps every reference option`() {
        val spec = AodThemeValue.TICK.spec
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
        assertEquals(55..90, TickThemeSpec.Size.range)
        assertEquals(70, TickThemeSpec.Size.default)
        assertEquals(GradientSwatchValue.TROPIC.gradient.colors, TickThemeSpec.RingColor.defaultGradient?.colors)
        assertEquals(AodGradientDirection.VERTICAL, TickThemeSpec.RingColor.defaultGradient?.direction)
        assertEquals(
            listOf(
                TickThemeSpec.Size,
                TickThemeSpec.ClockColor,
                TickThemeSpec.RingColor,
                TickThemeSpec.DateColor,
                TickThemeSpec.NotificationsColor,
                TickThemeSpec.BatteryColor,
                TickThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }
    // TODO(big-overlap): test riêng của chủ đề

    // Khung xem trước dùng giờ mẫu; chỉ chủ đề có chuyển động theo giờ thật như app mẫu (liveThemes: La bàn, Bán nguyệt, chấm giây của Gợn
    // sóng…) chạy theo giờ thật cả ở xem trước, chủ đề cũ không đổi.
    @Test
    fun `only themes with real time motion run in real time in previews`() {
        val liveThemes = setOf(
            AodThemeValue.COMPASS,
            AodThemeValue.HALF_MOON,
            AodThemeValue.RIPPLE,
            // TODO(coum-face): thêm nếu chủ đề chạy theo giờ thật ở xem trước
            AodThemeValue.TICK,
            // TODO(big-overlap): thêm nếu chủ đề chạy theo giờ thật ở xem trước
        )
        AodThemeValue.entries.forEach { theme ->
            assertEquals("${theme.id}: xem trước theo giờ thật", theme in liveThemes, theme.spec.isLiveInPreview)
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
