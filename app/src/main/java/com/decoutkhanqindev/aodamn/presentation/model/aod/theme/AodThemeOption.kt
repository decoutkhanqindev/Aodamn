package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradientDirection
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.toStoredClockColor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

interface AodThemeChoice {
    val code: Int

    @get:StringRes
    val labelRes: Int
}

@Immutable
sealed interface AodThemeOption<T> {
    val key: String

    @get:StringRes
    val labelRes: Int
    val default: T

    fun encode(value: T): Int

    fun decode(raw: Int?): T
}

@Immutable
class AodChoiceOption<E : AodThemeChoice>(
    override val key: String,
    @param:StringRes override val labelRes: Int,
    val entries: ImmutableList<E>,
    override val default: E,
) : AodThemeOption<E> {
    override fun encode(value: E): Int = value.code

    override fun decode(raw: Int?): E = entries.find { it.code == raw } ?: default
}

@Immutable
class AodColorOption(
    override val key: String,
    @param:StringRes override val labelRes: Int,
    override val default: Color,
    val defaultGradient: AodGradient? = null,
    isGradientAllowed: Boolean = false,
) : AodThemeOption<Color> {
    val isGradientAllowed: Boolean = isGradientAllowed || defaultGradient != null

    val middleKey: String = "${key}_gradient_2"
    val endKey: String = "${key}_gradient_3"
    val directionKey: String = "${key}_gradient_direction"

    val gradientKeys: List<String> = listOf(middleKey, endKey, directionKey)

    init {
        require(defaultGradient == null || defaultGradient.start == default) { "default must be the first color of defaultGradient" }
    }

    override fun encode(value: Color): Int = value.toStoredClockColor()

    override fun decode(raw: Int?): Color = raw?.let { Color(it) } ?: default

    fun decodeGradient(values: Map<String, Int>): AodGradient? {
        if (key !in values) return defaultGradient
        val middle = values[middleKey] ?: return null
        val end = values[endKey] ?: return null
        val direction = AodGradientDirection.fromCode(values[directionKey]) ?: return null
        return AodGradient(persistentListOf(decode(values[key]), Color(middle), Color(end)), direction)
    }

    fun encodeSolid(color: Color): Map<String, Int?> =
        mapOf(key to encode(color)) + gradientKeys.associateWith { null }

    fun encodeGradient(gradient: AodGradient): Map<String, Int?> = mapOf(
        key to encode(gradient.colors[0]),
        middleKey to encode(gradient.colors[1]),
        endKey to encode(gradient.colors[2]),
        directionKey to gradient.direction.code,
    )
}

@Immutable
class AodRangeOption(
    override val key: String,
    @param:StringRes override val labelRes: Int,
    val range: IntRange,
    val step: Int,
    override val default: Int,
    @param:StringRes val autoLabelRes: Int? = null,
) : AodThemeOption<Int> {
    override fun encode(value: Int): Int = value

    override fun decode(raw: Int?): Int = when {
        raw == null -> default
        autoLabelRes != null && raw == AUTO -> AUTO
        else -> raw.coerceIn(range)
    }

    companion object {
        const val AUTO = -1
    }
}

@Immutable
class AodToggleOption(
    override val key: String,
    @param:StringRes override val labelRes: Int,
    override val default: Boolean,
) : AodThemeOption<Boolean> {
    override fun encode(value: Boolean): Int = if (value) 1 else 0

    override fun decode(raw: Int?): Boolean = raw?.let { it != 0 } ?: default
}
