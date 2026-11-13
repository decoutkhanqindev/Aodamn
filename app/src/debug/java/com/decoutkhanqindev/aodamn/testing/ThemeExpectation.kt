package com.decoutkhanqindev.aodamn.testing

import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.GradientSwatchValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodBlockValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodChoiceOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodColorOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodRangeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeConfig
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodThemeValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodToggleOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.AodZoneOption
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.component.AodComponentValue
import com.decoutkhanqindev.aodamn.presentation.model.aod.theme.options

enum class ThemeRenderProfile {
    REFERENCE,
    MINIMUM,
    DEFAULT,
    MAXIMUM,
}

data class ThemeExpectation(
    val theme: AodThemeValue,
    val optionKeys: List<String>,
    val components: Set<AodComponentValue>,
    val ownBlocks: Set<AodBlockValue>,
    val hasNotificationContent: Boolean,
    val isLiveInPreview: Boolean,
    val motion: ThemeMotionExpectation? = null,
) {
    override fun toString(): String = theme.id
}

data class ThemeMotionExpectation(
    val fromMinute: Int,
    val toMinute: Int,
    val checkpointsMillis: List<Long>,
)

data class ThemeScreenshotCase(
    val theme: AodThemeValue,
    val profile: ThemeRenderProfile,
    val isLandscape: Boolean,
) {
    val name: String
        get() = listOf(theme.id, if (isLandscape) "landscape" else "portrait", profile.name.lowercase()).joinToString("_")

    override fun toString(): String = name
}

object ThemeTestCatalog {
    const val SHARED_ROOT_TAG = "shared_block_test_root"
    const val OPTION_ROOT_TAG = "theme_option_test_root"
    const val EDITOR_ROOT_TAG = "theme_editor_test_root"

    val expectations = listOf(
        ThemeExpectation(
            theme = AodThemeValue.GENTLE,
            optionKeys = listOf("size", "weight", "hour_color", "minute_color", "date_color", "status_color", "media_color"),
            components = setOf(
                AodComponentValue.DATE,
                AodComponentValue.BATTERY,
                AodComponentValue.WEATHER,
                AodComponentValue.NOTIFICATIONS,
                AodComponentValue.MEDIA,
            ),
            ownBlocks = setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA),
            hasNotificationContent = true,
            isLiveInPreview = false,
        ),
        ThemeExpectation(
            theme = AodThemeValue.BLINKY,
            optionKeys = listOf("character_size", "character_color", "clock_size", "clock_color", "date_color", "status_color", "media_color"),
            components = setOf(
                AodComponentValue.DATE,
                AodComponentValue.BATTERY,
                AodComponentValue.WEATHER,
                AodComponentValue.NOTIFICATIONS,
                AodComponentValue.MEDIA,
            ),
            ownBlocks = setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA),
            hasNotificationContent = true,
            isLiveInPreview = true,
        ),
        ThemeExpectation(
            theme = AodThemeValue.FLIP,
            optionKeys = listOf("style", "size", "character_size", "card_color", "digit_color"),
            components = setOf(
                AodComponentValue.DATE,
                AodComponentValue.BATTERY,
                AodComponentValue.WEATHER,
                AodComponentValue.NOTIFICATIONS,
            ),
            ownBlocks = emptySet(),
            hasNotificationContent = false,
            isLiveInPreview = true,
            motion = ThemeMotionExpectation(
                fromMinute = 30,
                toMinute = 31,
                checkpointsMillis = listOf(400L, 800L),
            ),
        ),
        ThemeExpectation(
            theme = AodThemeValue.ECLIPSE,
            optionKeys = listOf("ring_color", "shadow_color", "rotation", "peek", "character_color", "date_color", "status_color", "media_color"),
            components = setOf(
                AodComponentValue.DATE,
                AodComponentValue.BATTERY,
                AodComponentValue.WEATHER,
                AodComponentValue.NOTIFICATIONS,
                AodComponentValue.MEDIA,
            ),
            ownBlocks = setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA),
            hasNotificationContent = true,
            isLiveInPreview = true,
            motion = ThemeMotionExpectation(
                fromMinute = 30,
                toMinute = 31,
                checkpointsMillis = listOf(1_000L, 2_000L),
            ),
        ),
        ThemeExpectation(
            theme = AodThemeValue.MOON_PHASE,
            optionKeys = listOf("clock_color", "clock_size", "character_color", "character_size", "peek", "rotation", "glow_color", "particle_color", "date_color", "status_color", "media_color"),
            components = setOf(AodComponentValue.DATE, AodComponentValue.BATTERY, AodComponentValue.WEATHER, AodComponentValue.NOTIFICATIONS, AodComponentValue.MEDIA),
            ownBlocks = setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA),
            hasNotificationContent = true,
            isLiveInPreview = true,
            motion = ThemeMotionExpectation(fromMinute = 30, toMinute = 31, checkpointsMillis = listOf(1600L, 3200L)),
        ),
        ThemeExpectation(
            theme = AodThemeValue.POSTER,
            optionKeys = listOf("clock_size", "hour_color", "minute_color", "day_color", "date_color", "status_color", "media_color"),
            components = setOf(AodComponentValue.DATE, AodComponentValue.BATTERY, AodComponentValue.WEATHER, AodComponentValue.NOTIFICATIONS, AodComponentValue.MEDIA),
            ownBlocks = setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA),
            hasNotificationContent = true,
            isLiveInPreview = false,
        ),
        ThemeExpectation(
            theme = AodThemeValue.PIXEL,
            optionKeys = listOf("style", "clock_size", "clock_color", "date_color", "status_color", "media_color"),
            components = setOf(AodComponentValue.DATE, AodComponentValue.BATTERY, AodComponentValue.WEATHER, AodComponentValue.NOTIFICATIONS, AodComponentValue.MEDIA),
            ownBlocks = setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA),
            hasNotificationContent = true,
            isLiveInPreview = false,
        ),
        ThemeExpectation(
            theme = AodThemeValue.MERIDIAN,
            optionKeys = listOf("style", "card_color", "dial_color", "zone", "date_color", "status_color", "media_color"),
            components = setOf(AodComponentValue.DATE, AodComponentValue.BATTERY, AodComponentValue.WEATHER, AodComponentValue.NOTIFICATIONS, AodComponentValue.MEDIA),
            ownBlocks = setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA),
            hasNotificationContent = true,
            isLiveInPreview = false,
        ),
        ThemeExpectation(
            theme = AodThemeValue.ORBIT,
            optionKeys = listOf("size", "digital", "hour_color", "minute_color", "second_color", "clock_color", "date_color", "status_color", "media_color"),
            components = setOf(
                AodComponentValue.DATE,
                AodComponentValue.BATTERY,
                AodComponentValue.WEATHER,
                AodComponentValue.EVENTS,
                AodComponentValue.MEMO,
                AodComponentValue.NOTIFICATIONS,
                AodComponentValue.MEDIA,
            ),
            ownBlocks = setOf(AodBlockValue.DATE, AodBlockValue.STATUS, AodBlockValue.MEDIA),
            hasNotificationContent = true,
            isLiveInPreview = true,
        ),
    )
}

object ThemeOptionCases {
    fun all(option: AodThemeOption<*>): List<Map<String, Int?>> = when (option) {
        is AodChoiceOption<*> -> option.entries.map { entry -> mapOf(option.key to entry.code) }
        is AodRangeOption -> listOf(option.range.first, option.default, option.range.last)
            .distinct()
            .map { value -> mapOf(option.key to value) }
        is AodToggleOption -> listOf(false, true).map { value -> mapOf(option.key to option.encode(value)) }
        is AodColorOption -> listOf(
            option.encodeSolid(option.default),
            option.encodeGradient(gradientOf(option)),
        )
        is AodZoneOption -> listOf(option.default, otherZone(option)).map { id -> mapOf(option.key to option.encode(id)) }
    }

    fun changed(option: AodThemeOption<*>): Map<String, Int?> = when (option) {
        is AodChoiceOption<*> -> {
            val entry = option.entries.firstOrNull { value -> value != option.default } ?: option.default
            mapOf(option.key to entry.code)
        }
        is AodRangeOption -> mapOf(option.key to if (option.default != option.range.last) option.range.last else option.range.first)
        is AodToggleOption -> mapOf(option.key to option.encode(!option.default))
        is AodColorOption -> option.encodeGradient(gradientOf(option))
        is AodZoneOption -> mapOf(option.key to option.encode(otherZone(option)))
    }

    private fun otherZone(option: AodZoneOption): String =
        if (option.default == "Asia/Tokyo") "Europe/London" else "Asia/Tokyo"

    private fun gradientOf(option: AodColorOption) =
        if (option.key.contains("card") || option.key.contains("background") || option.key.contains("face")) {
            GradientSwatchValue.LILAC_SEA.gradient
        } else {
            GradientSwatchValue.SUNSET.gradient
        }

    fun minimum(theme: AodThemeValue): AodThemeConfig = theme.spec.options
        .filterIsInstance<AodRangeOption>()
        .fold(AodThemeConfig()) { config, option -> config.with(option, option.range.first) }

    fun maximum(theme: AodThemeValue): AodThemeConfig = theme.spec.options
        .fold(AodThemeConfig()) { config, option -> config.withRaws(changed(option)) }
}