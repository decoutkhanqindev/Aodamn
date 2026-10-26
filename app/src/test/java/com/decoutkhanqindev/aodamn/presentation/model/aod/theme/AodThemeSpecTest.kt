package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradientDirection
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.GradientSwatchValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap.AodBigOverlapStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap.BigOverlapThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.AodCompassStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.CompassThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gradientline.GradientLineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.halfmoon.HalfMoonThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.quarters.AodQuartersDigitStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.quarters.QuartersThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.ripple.RippleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tick.TickThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Cream
import com.decoutkhanqindev.aodamn.presentation.theme.GreyCE
import com.decoutkhanqindev.aodamn.presentation.theme.Orchid
import com.decoutkhanqindev.aodamn.presentation.theme.Peach
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

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
    fun `only chain owns memo and events and only themes drawing their latest alert keep notification content with their status line`() {
        AodThemeValue.entries.forEach { theme ->
            val isChain = theme == AodThemeValue.CHAIN
            val drawsLatest = isChain ||
                theme in setOf(
                    AodThemeValue.COMPASS,
                    AodThemeValue.HALF_MOON,
                    AodThemeValue.GRADIENT_LINE,
                    AodThemeValue.RIPPLE,
                    AodThemeValue.TICK,
                    AodThemeValue.BIG_OVERLAP,
                    AodThemeValue.QUARTERS,
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
        assertEquals(AodCompassStyleValue.entries, CompassThemeSpec.Style.entries)
        assertEquals(AodCompassStyleValue.SECONDS, CompassThemeSpec.Style.default)
    }

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

    @Test
    fun `auto range keeps the auto value and old ranges still clamp it`() {
        val position = GradientLineThemeSpec.Position
        assertEquals(AodRangeOption.AUTO, position.decode(AodRangeOption.AUTO))
        assertEquals(35, position.decode(35))
        assertEquals(100, position.decode(140))
        assertEquals(80, GradientLineThemeSpec.Size.decode(AodRangeOption.AUTO))
    }

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

    @Test
    fun `big overlap only has the blocks that fit around its digits and keeps every reference option`() {
        val spec = AodThemeValue.BIG_OVERLAP.spec
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
        assertEquals(AodBigOverlapStyleValue.entries, BigOverlapThemeSpec.Style.entries)
        assertEquals(AodBigOverlapStyleValue.FILLED_ROUND, BigOverlapThemeSpec.Style.default)
        assertEquals(50..100, BigOverlapThemeSpec.Size.range)
        assertEquals(85, BigOverlapThemeSpec.Size.default)
        assertEquals(Peach, BigOverlapThemeSpec.HourFirstColor.default)
        assertEquals(GreyCE, BigOverlapThemeSpec.HourSecondColor.default)
        assertEquals(BigOverlapThemeSpec.HourSecondColor.default, BigOverlapThemeSpec.MinuteFirstColor.default)
        assertEquals(BigOverlapThemeSpec.HourFirstColor.default, BigOverlapThemeSpec.MinuteSecondColor.default)
        assertEquals(
            listOf(
                BigOverlapThemeSpec.Style,
                BigOverlapThemeSpec.Size,
                BigOverlapThemeSpec.HourFirstColor,
                BigOverlapThemeSpec.HourSecondColor,
                BigOverlapThemeSpec.MinuteFirstColor,
                BigOverlapThemeSpec.MinuteSecondColor,
                BigOverlapThemeSpec.DateColor,
                BigOverlapThemeSpec.NotificationsColor,
                BigOverlapThemeSpec.BatteryColor,
                BigOverlapThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `quarters only has the blocks that fit around its dial and keeps every reference option`() {
        val spec = AodThemeValue.QUARTERS.spec
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
        assertEquals(AodQuartersDigitStyleValue.entries, QuartersThemeSpec.DigitStyle.entries)
        assertEquals(AodQuartersDigitStyleValue.FILLED_ROUND, QuartersThemeSpec.DigitStyle.default)
        assertEquals(AodTileStyleValue.entries, QuartersThemeSpec.HandStyle.entries)
        assertEquals(AodTileStyleValue.FILLED, QuartersThemeSpec.HandStyle.default)
        assertEquals(50..90, QuartersThemeSpec.Size.range)
        assertEquals(70, QuartersThemeSpec.Size.default)
        assertEquals(Orchid, QuartersThemeSpec.DigitColor.default)
        assertEquals(GreyCE, QuartersThemeSpec.HourColor.default)
        assertEquals(Cream, QuartersThemeSpec.MinuteColor.default)
        assertEquals(
            listOf(
                QuartersThemeSpec.DigitStyle,
                QuartersThemeSpec.HandStyle,
                QuartersThemeSpec.Size,
                QuartersThemeSpec.DigitColor,
                QuartersThemeSpec.HourColor,
                QuartersThemeSpec.MinuteColor,
                QuartersThemeSpec.DateColor,
                QuartersThemeSpec.NotificationsColor,
                QuartersThemeSpec.BatteryColor,
                QuartersThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `only themes with real time motion run in real time in previews`() {
        val liveThemes = setOf(
            AodThemeValue.COMPASS,
            AodThemeValue.HALF_MOON,
            AodThemeValue.RIPPLE,
            AodThemeValue.TICK,
            AodThemeValue.QUARTERS,
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
