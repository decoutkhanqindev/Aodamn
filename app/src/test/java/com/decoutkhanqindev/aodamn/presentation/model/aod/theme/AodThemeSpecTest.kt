package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodAppearanceUiModel
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradientDirection
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.GradientSwatchValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap.AodBigOverlapStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.bigoverlap.BigOverlapThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.blinky.BlinkyThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.clip.ClipThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.AodCompassStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.compass.CompassThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.eclipse.EclipseThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.fade.AodFadeWeightValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.fade.FadeThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.flip.AodFlipStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.flip.FlipThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.frame.FrameThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gradientline.GradientLineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gentle.AodGentleWeightValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.gentle.GentleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.halfmoon.HalfMoonThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.mono.MonoThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.moonphase.MoonPhaseThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.orbit.OrbitThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.pebble.AodTileStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.quarters.AodQuartersDigitStyleValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.quarters.QuartersThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.ripple.RippleThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.sector.SectorThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.spine.SpineThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.tick.TickThemeSpec
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.words.AodWordsWeightValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.words.WordsThemeSpec
import com.decoutkhanqindev.aodamn.presentation.screens.aod.state.AodState
import com.decoutkhanqindev.aodamn.presentation.theme.Amber
import com.decoutkhanqindev.aodamn.presentation.theme.Aquamarine
import com.decoutkhanqindev.aodamn.presentation.theme.Brick
import com.decoutkhanqindev.aodamn.presentation.theme.Cream
import com.decoutkhanqindev.aodamn.presentation.theme.EclipseHalo
import com.decoutkhanqindev.aodamn.presentation.theme.EclipseShadow
import com.decoutkhanqindev.aodamn.presentation.theme.Grey9A
import com.decoutkhanqindev.aodamn.presentation.theme.GreyB4
import com.decoutkhanqindev.aodamn.presentation.theme.GreyCE
import com.decoutkhanqindev.aodamn.presentation.theme.GreyE4
import com.decoutkhanqindev.aodamn.presentation.theme.GreyED
import com.decoutkhanqindev.aodamn.presentation.theme.Ink
import com.decoutkhanqindev.aodamn.presentation.theme.Midnight
import com.decoutkhanqindev.aodamn.presentation.theme.Mist
import com.decoutkhanqindev.aodamn.presentation.theme.Orchid
import com.decoutkhanqindev.aodamn.presentation.theme.Peach
import com.decoutkhanqindev.aodamn.presentation.theme.Ruby
import com.decoutkhanqindev.aodamn.presentation.theme.Sage
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
    fun `only chain owns memo and events and notification content follows theme support`() {
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
                    AodThemeValue.SECTOR,
                    AodThemeValue.SPINE,
                    AodThemeValue.CLIP,
                    AodThemeValue.FADE,
                    AodThemeValue.WORDS,
                    AodThemeValue.FRAME,
                    AodThemeValue.MONO,
                    AodThemeValue.ORBIT,
                    AodThemeValue.GENTLE,
                    AodThemeValue.BLINKY,
                )
            val hasSharedLatest = AodBlockValue.STATUS !in theme.spec.ownBlocks && theme != AodThemeValue.FLIP
            assertEquals("${theme.id}: ghi nhớ", isChain, AodBlockValue.MEMO in theme.spec.ownBlocks)
            assertEquals("${theme.id}: sự kiện", isChain, AodBlockValue.EVENTS in theme.spec.ownBlocks)
            assertEquals(
                "${theme.id}: nội dung thông báo",
                drawsLatest || hasSharedLatest,
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
    fun `sector only has the blocks that fit around its hands and keeps every reference option`() {
        val spec = AodThemeValue.SECTOR.spec
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
        assertEquals(60..120, SectorThemeSpec.Size.range)
        assertEquals(100, SectorThemeSpec.Size.default)
        assertEquals(GreyED, SectorThemeSpec.HourColor.default)
        assertEquals(GreyED, SectorThemeSpec.MinuteColor.default)
        assertEquals(Ruby, SectorThemeSpec.SecondColor.default)
        assertEquals(Ink, SectorThemeSpec.SectorColor.default)
        assertEquals(
            listOf(
                SectorThemeSpec.Size,
                SectorThemeSpec.HourColor,
                SectorThemeSpec.MinuteColor,
                SectorThemeSpec.SecondColor,
                SectorThemeSpec.SectorColor,
                SectorThemeSpec.DateColor,
                SectorThemeSpec.NotificationsColor,
                SectorThemeSpec.BatteryColor,
                SectorThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `spine draws everything but the drawing and keeps every reference option with one color per part`() {
        val spec = AodThemeValue.SPINE.spec
        assertEquals(
            setOf(
                AodComponentValue.DATE,
                AodComponentValue.BATTERY,
                AodComponentValue.WEATHER,
                AodComponentValue.EVENTS,
                AodComponentValue.MEMO,
                AodComponentValue.NOTIFICATIONS,
                AodComponentValue.MEDIA,
            ),
            spec.components,
        )
        assertEquals(setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA), spec.ownBlocks)
        assertEquals(true, spec.hasNotificationContent)
        assertEquals(false, spec.isLiveInPreview)
        assertEquals(60..150, SpineThemeSpec.Size.range)
        assertEquals(100, SpineThemeSpec.Size.default)
        assertEquals(Amber, SpineThemeSpec.ClockColor.default)
        assertEquals(GreyE4, SpineThemeSpec.DateColor.default)
        assertEquals(GreyE4, SpineThemeSpec.StatusColor.default)
        assertEquals(GreyE4, SpineThemeSpec.MediaColor.default)
        assertEquals(
            listOf(
                SpineThemeSpec.Size,
                SpineThemeSpec.ClockColor,
                SpineThemeSpec.DateColor,
                SpineThemeSpec.StatusColor,
                SpineThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `clip keeps the reference size and center dot with one color per part and no memo, events or drawing`() {
        val spec = AodThemeValue.CLIP.spec
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
        assertEquals(setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA), spec.ownBlocks)
        assertEquals(true, spec.hasNotificationContent)
        assertEquals(false, spec.isLiveInPreview)
        assertEquals(60..100, ClipThemeSpec.Size.range)
        assertEquals(80, ClipThemeSpec.Size.default)
        assertEquals(GreyED, ClipThemeSpec.HourColor.default)
        assertEquals(Aquamarine, ClipThemeSpec.MinuteColor.default)
        assertEquals(Aquamarine, ClipThemeSpec.DotColor.default)
        assertEquals(GreyE4, ClipThemeSpec.DateColor.default)
        assertEquals(GreyB4, ClipThemeSpec.StatusColor.default)
        assertEquals(GreyE4, ClipThemeSpec.MediaColor.default)
        assertEquals(
            listOf(
                ClipThemeSpec.Size,
                ClipThemeSpec.HourColor,
                ClipThemeSpec.MinuteColor,
                ClipThemeSpec.DotColor,
                ClipThemeSpec.DateColor,
                ClipThemeSpec.StatusColor,
                ClipThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `fade keeps the reference size and four weights with a fading pastel sky clock and no memo, events or drawing`() {
        val spec = AodThemeValue.FADE.spec
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
        assertEquals(setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA), spec.ownBlocks)
        assertEquals(true, spec.hasNotificationContent)
        assertEquals(false, spec.isLiveInPreview)
        assertEquals(30..100, FadeThemeSpec.Size.range)
        assertEquals(60, FadeThemeSpec.Size.default)
        assertEquals(
            listOf(AodFadeWeightValue.THIN, AodFadeWeightValue.LIGHT, AodFadeWeightValue.REGULAR, AodFadeWeightValue.BOLD),
            FadeThemeSpec.Weight.entries,
        )
        assertEquals(AodFadeWeightValue.BOLD, FadeThemeSpec.Weight.default)
        assertEquals(true, FadeThemeSpec.Fade.default)
        val sky = GradientSwatchValue.PASTEL_SKY.gradient.copy(direction = AodGradientDirection.VERTICAL)
        assertEquals(sky, FadeThemeSpec.HourColor.defaultGradient)
        assertEquals(sky, FadeThemeSpec.MinuteColor.defaultGradient)
        assertEquals(GreyE4, FadeThemeSpec.DateColor.default)
        assertEquals(GreyE4, FadeThemeSpec.NotificationsColor.default)
        assertEquals(Grey9A, FadeThemeSpec.BatteryColor.default)
        assertEquals(GreyE4, FadeThemeSpec.MediaColor.default)
        assertEquals(
            listOf(
                FadeThemeSpec.Size,
                FadeThemeSpec.Weight,
                FadeThemeSpec.Fade,
                FadeThemeSpec.HourColor,
                FadeThemeSpec.MinuteColor,
                FadeThemeSpec.DateColor,
                FadeThemeSpec.NotificationsColor,
                FadeThemeSpec.BatteryColor,
                FadeThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `words keeps the reference line size and four weights with a lilac sea minute line and no memo, events or drawing`() {
        val spec = AodThemeValue.WORDS.spec
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
        assertEquals(setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA), spec.ownBlocks)
        assertEquals(true, spec.hasNotificationContent)
        assertEquals(false, spec.isLiveInPreview)
        assertEquals(30..60, WordsThemeSpec.Size.range)
        assertEquals(40, WordsThemeSpec.Size.default)
        assertEquals(
            listOf(AodWordsWeightValue.THIN, AodWordsWeightValue.LIGHT, AodWordsWeightValue.REGULAR, AodWordsWeightValue.BOLD),
            WordsThemeSpec.Weight.entries,
        )
        assertEquals(AodWordsWeightValue.BOLD, WordsThemeSpec.Weight.default)
        assertEquals(GreyED, WordsThemeSpec.HourColor.default)
        assertEquals(true, WordsThemeSpec.HourColor.isGradientAllowed)
        assertEquals(GradientSwatchValue.LILAC_SEA.gradient, WordsThemeSpec.MinuteColor.defaultGradient)
        assertEquals(AodGradientDirection.HORIZONTAL, WordsThemeSpec.MinuteColor.defaultGradient?.direction)
        assertEquals(GreyE4, WordsThemeSpec.DateColor.default)
        assertEquals(GreyE4, WordsThemeSpec.NotificationsColor.default)
        assertEquals(Grey9A, WordsThemeSpec.BatteryColor.default)
        assertEquals(GreyE4, WordsThemeSpec.MediaColor.default)
        assertEquals(
            listOf(
                WordsThemeSpec.Size,
                WordsThemeSpec.Weight,
                WordsThemeSpec.HourColor,
                WordsThemeSpec.MinuteColor,
                WordsThemeSpec.DateColor,
                WordsThemeSpec.NotificationsColor,
                WordsThemeSpec.BatteryColor,
                WordsThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `frame keeps the reference size with a diagonal orchid gold box and no drawing`() {
        val spec = AodThemeValue.FRAME.spec
        assertEquals(
            setOf(
                AodComponentValue.DATE,
                AodComponentValue.BATTERY,
                AodComponentValue.WEATHER,
                AodComponentValue.EVENTS,
                AodComponentValue.MEMO,
                AodComponentValue.NOTIFICATIONS,
                AodComponentValue.MEDIA,
            ),
            spec.components,
        )
        assertEquals(setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA), spec.ownBlocks)
        assertEquals(true, spec.hasNotificationContent)
        assertEquals(false, spec.isLiveInPreview)
        assertEquals(50..100, FrameThemeSpec.Size.range)
        assertEquals(65, FrameThemeSpec.Size.default)
        assertEquals(
            GradientSwatchValue.ORCHID_GOLD.gradient.copy(direction = AodGradientDirection.DIAGONAL_DOWN),
            FrameThemeSpec.ClockColor.defaultGradient,
        )
        assertEquals(GreyE4, FrameThemeSpec.DateColor.default)
        assertEquals(GreyB4, FrameThemeSpec.StatusColor.default)
        assertEquals(GreyE4, FrameThemeSpec.MediaColor.default)
        assertEquals(
            listOf(FrameThemeSpec.Size, FrameThemeSpec.ClockColor, FrameThemeSpec.DateColor, FrameThemeSpec.StatusColor, FrameThemeSpec.MediaColor),
            spec.options,
        )
    }

    @Test
    fun `mono keeps the reference size with an autumn hour and no drawing`() {
        val spec = AodThemeValue.MONO.spec
        assertEquals(
            setOf(
                AodComponentValue.DATE,
                AodComponentValue.BATTERY,
                AodComponentValue.WEATHER,
                AodComponentValue.EVENTS,
                AodComponentValue.MEMO,
                AodComponentValue.NOTIFICATIONS,
                AodComponentValue.MEDIA,
            ),
            spec.components,
        )
        assertEquals(setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA), spec.ownBlocks)
        assertEquals(true, spec.hasNotificationContent)
        assertEquals(false, spec.isLiveInPreview)
        assertEquals(60..100, MonoThemeSpec.Size.range)
        assertEquals(80, MonoThemeSpec.Size.default)
        assertEquals(GradientSwatchValue.AUTUMN.gradient, MonoThemeSpec.HourColor.defaultGradient)
        assertEquals(GreyED, MonoThemeSpec.MinuteColor.default)
        assertEquals(true, MonoThemeSpec.MinuteColor.isGradientAllowed)
        assertEquals(GreyE4, MonoThemeSpec.DateColor.default)
        assertEquals(GreyE4, MonoThemeSpec.StatusColor.default)
        assertEquals(GreyE4, MonoThemeSpec.MediaColor.default)
        assertEquals(
            listOf(
                MonoThemeSpec.Size,
                MonoThemeSpec.HourColor,
                MonoThemeSpec.MinuteColor,
                MonoThemeSpec.DateColor,
                MonoThemeSpec.StatusColor,
                MonoThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `orbit keeps the reference size, digital clock and planet colors, runs live and has no drawing`() {
        val spec = AodThemeValue.ORBIT.spec
        assertEquals(
            setOf(
                AodComponentValue.DATE,
                AodComponentValue.BATTERY,
                AodComponentValue.WEATHER,
                AodComponentValue.EVENTS,
                AodComponentValue.MEMO,
                AodComponentValue.NOTIFICATIONS,
                AodComponentValue.MEDIA,
            ),
            spec.components,
        )
        assertEquals(setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA), spec.ownBlocks)
        assertEquals(true, spec.hasNotificationContent)
        assertEquals(true, spec.isLiveInPreview)
        assertEquals(65..90, OrbitThemeSpec.Size.range)
        assertEquals(80, OrbitThemeSpec.Size.default)
        assertEquals(true, OrbitThemeSpec.Digital.default)
        assertEquals(Brick, OrbitThemeSpec.HourColor.default)
        assertEquals(Sage, OrbitThemeSpec.MinuteColor.default)
        assertEquals(Grey9A, OrbitThemeSpec.SecondColor.default)
        assertEquals(GreyED, OrbitThemeSpec.ClockColor.default)
        assertEquals(GreyE4, OrbitThemeSpec.DateColor.default)
        assertEquals(GreyB4, OrbitThemeSpec.StatusColor.default)
        assertEquals(GreyE4, OrbitThemeSpec.MediaColor.default)
        assertEquals(
            listOf(
                OrbitThemeSpec.Size,
                OrbitThemeSpec.Digital,
                OrbitThemeSpec.HourColor,
                OrbitThemeSpec.MinuteColor,
                OrbitThemeSpec.SecondColor,
                OrbitThemeSpec.ClockColor,
                OrbitThemeSpec.DateColor,
                OrbitThemeSpec.StatusColor,
                OrbitThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `gentle keeps every Jul22 option and draws its own date, status and media`() {
        val spec = AodThemeValue.GENTLE.spec
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
        assertEquals(setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA), spec.ownBlocks)
        assertEquals(true, spec.hasNotificationContent)
        assertEquals(false, spec.isLiveInPreview)
        assertEquals(
            listOf(AodGentleWeightValue.EXTRA_LIGHT, AodGentleWeightValue.REGULAR, AodGentleWeightValue.BOLD),
            GentleThemeSpec.Weight.entries,
        )
        assertEquals(AodGentleWeightValue.BOLD, GentleThemeSpec.Weight.default)
        assertEquals(70..130, GentleThemeSpec.Size.range)
        assertEquals(100, GentleThemeSpec.Size.default)
        assertEquals(Mist, GentleThemeSpec.HourColor.default)
        assertEquals(GreyED, GentleThemeSpec.MinuteColor.default)
        assertEquals(GreyE4, GentleThemeSpec.DateColor.default)
        assertEquals(GreyB4, GentleThemeSpec.StatusColor.default)
        assertEquals(Mist, GentleThemeSpec.MediaColor.default)
        assertEquals(
            listOf(
                GentleThemeSpec.Size,
                GentleThemeSpec.Weight,
                GentleThemeSpec.HourColor,
                GentleThemeSpec.MinuteColor,
                GentleThemeSpec.DateColor,
                GentleThemeSpec.StatusColor,
                GentleThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `blinky keeps every reference option and fixed detail blocks`() {
        val spec = AodThemeValue.BLINKY.spec
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
        assertEquals(setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA), spec.ownBlocks)
        assertEquals(true, spec.hasNotificationContent)
        assertEquals(true, spec.isLiveInPreview)
        assertEquals(70..140, BlinkyThemeSpec.CharacterSize.range)
        assertEquals(100, BlinkyThemeSpec.CharacterSize.default)
        assertEquals(17..30, BlinkyThemeSpec.ClockSize.range)
        assertEquals(22, BlinkyThemeSpec.ClockSize.default)
        listOf(
            BlinkyThemeSpec.CharacterColor,
            BlinkyThemeSpec.ClockColor,
            BlinkyThemeSpec.DateColor,
            BlinkyThemeSpec.StatusColor,
            BlinkyThemeSpec.MediaColor,
        ).forEach { option -> assertEquals(option.key, Orchid, option.default) }
        assertEquals(
            listOf(
                BlinkyThemeSpec.CharacterSize,
                BlinkyThemeSpec.CharacterColor,
                BlinkyThemeSpec.ClockSize,
                BlinkyThemeSpec.ClockColor,
                BlinkyThemeSpec.DateColor,
                BlinkyThemeSpec.StatusColor,
                BlinkyThemeSpec.MediaColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `moon phase keeps every approved reference option and fixed compact blocks`() {
        val spec = AodThemeValue.MOON_PHASE.spec
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
        assertEquals(setOf(AodBlockValue.DATE), spec.ownBlocks)
        assertEquals(true, spec.hasNotificationContent)
        assertEquals(true, spec.isLiveInPreview)
        assertEquals(80..120, MoonPhaseThemeSpec.ClockSize.range)
        assertEquals(100, MoonPhaseThemeSpec.ClockSize.default)
        assertEquals(60..140, MoonPhaseThemeSpec.CharacterSize.range)
        assertEquals(100, MoonPhaseThemeSpec.CharacterSize.default)
        assertEquals(0..50, MoonPhaseThemeSpec.Peek.range)
        assertEquals(45, MoonPhaseThemeSpec.Rotation.default)
        assertEquals(
            listOf(
                MoonPhaseThemeSpec.ClockColor,
                MoonPhaseThemeSpec.ClockSize,
                MoonPhaseThemeSpec.CharacterColor,
                MoonPhaseThemeSpec.CharacterSize,
                MoonPhaseThemeSpec.Peek,
                MoonPhaseThemeSpec.Rotation,
                MoonPhaseThemeSpec.GlowColor,
                MoonPhaseThemeSpec.ParticleColor,
                MoonPhaseThemeSpec.DateColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `flip keeps every reference option and only compact blocks`() {
        val spec = AodThemeValue.FLIP.spec
        assertEquals(
            setOf(
                AodComponentValue.DATE,
                AodComponentValue.BATTERY,
                AodComponentValue.WEATHER,
                AodComponentValue.NOTIFICATIONS,
            ),
            spec.components,
        )
        assertEquals(emptySet<AodBlockValue>(), spec.ownBlocks)
        assertEquals(false, spec.hasNotificationContent)
        assertEquals(true, spec.isLiveInPreview)
        assertEquals(
            listOf(
                AodFlipStyleValue.BEBAS_NEUE,
                AodFlipStyleValue.BIG_SHOULDERS_BOLD,
                AodFlipStyleValue.BIG_SHOULDERS_LIGHT,
                AodFlipStyleValue.DOUBLE_OUTLINE,
                AodFlipStyleValue.TOMORROW,
                AodFlipStyleValue.RUBIK,
                AodFlipStyleValue.BODONI_MODA,
                AodFlipStyleValue.ANTON,
                AodFlipStyleValue.RAJDHANI,
            ),
            FlipThemeSpec.Style.entries,
        )
        assertEquals(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8), FlipThemeSpec.Style.entries.map { it.code })
        assertEquals(AodFlipStyleValue.BEBAS_NEUE, FlipThemeSpec.Style.default)
        assertEquals(50..100, FlipThemeSpec.Size.range)
        assertEquals(65, FlipThemeSpec.Size.default)
        assertEquals(35..75, FlipThemeSpec.CharacterSize.range)
        assertEquals(55, FlipThemeSpec.CharacterSize.default)
        assertEquals(Midnight, FlipThemeSpec.CardColor.default)
        assertEquals(GreyED, FlipThemeSpec.DigitColor.default)
        assertEquals(true, AodFlipStyleValue.DOUBLE_OUTLINE.isOutlined)
        assertEquals(
            listOf(
                FlipThemeSpec.Style,
                FlipThemeSpec.Size,
                FlipThemeSpec.CharacterSize,
                FlipThemeSpec.CardColor,
                FlipThemeSpec.DigitColor,
            ),
            spec.options,
        )
    }

    @Test
    fun `eclipse keeps five reference options and only the blocks that fit`() {
        val spec = AodThemeValue.ECLIPSE.spec
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
        assertEquals(emptySet<AodBlockValue>(), spec.ownBlocks)
        assertEquals(true, spec.hasNotificationContent)
        assertEquals(true, spec.isLiveInPreview)
        assertEquals(AodRangeOption.AUTO, EclipseThemeSpec.Rotation.default)
        assertEquals(AodRangeOption.AUTO, EclipseThemeSpec.Peek.default)
        assertEquals(EclipseHalo, EclipseThemeSpec.RingColor.default)
        assertEquals(EclipseShadow, EclipseThemeSpec.ShadowColor.default)
        assertEquals(GreyED, EclipseThemeSpec.CharacterColor.default)
        assertEquals(
            listOf(
                EclipseThemeSpec.RingColor,
                EclipseThemeSpec.ShadowColor,
                EclipseThemeSpec.Rotation,
                EclipseThemeSpec.Peek,
                EclipseThemeSpec.CharacterColor,
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
            AodThemeValue.SECTOR,
            AodThemeValue.ORBIT,
            AodThemeValue.FLIP,
            AodThemeValue.ECLIPSE,
            AodThemeValue.BLINKY,
            AodThemeValue.MOON_PHASE,
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
