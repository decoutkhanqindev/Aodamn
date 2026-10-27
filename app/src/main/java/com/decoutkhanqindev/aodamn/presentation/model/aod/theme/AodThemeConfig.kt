package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.lerp
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
import com.decoutkhanqindev.aodamn.presentation.theme.Black
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList

fun String?.toAodConfigValues(): Map<String, Int> = orEmpty().split(',').mapNotNull { pair ->
    val value = pair.substringAfter('=', missingDelimiterValue = "").toIntOrNull() ?: return@mapNotNull null
    pair.substringBefore('=') to value
}.toMap()

fun Map<String, Int>.toAodConfigString(): String = entries.joinToString(separator = ",") { (key, value) -> "$key=$value" }

@Immutable
data class AodThemeConfig(val values: PersistentMap<String, Int> = persistentMapOf()) {
    operator fun <T> get(option: AodThemeOption<T>): T = option.decode(values[option.key])

    fun <T> with(option: AodThemeOption<T>, value: T): AodThemeConfig = withRaw(option.key, option.encode(value))

    fun withRaw(key: String, raw: Int): AodThemeConfig = copy(values = values.put(key, raw))

    fun withRaws(raws: Map<String, Int?>): AodThemeConfig = copy(
        values = raws.entries.fold(values) { map, (key, raw) -> if (raw == null) map.remove(key) else map.put(key, raw) },
    )

    fun gradientOf(option: AodColorOption): AodGradient? = option.decodeGradient(values)

    fun brushOf(option: AodColorOption): Brush = gradientOf(option)?.toBrush() ?: SolidColor(get(option))

    fun colorsOf(option: AodColorOption): List<Color> = gradientOf(option)?.colors ?: listOf(get(option))

    fun darkBrushOf(option: AodColorOption, fraction: Float): Brush {
        val gradient = gradientOf(option) ?: return SolidColor(lerp(get(option), Black, fraction))
        return gradient.copy(colors = gradient.colors.map { color -> lerp(color, Black, fraction) }.toImmutableList()).toBrush()
    }

    fun without(options: List<AodThemeOption<*>>): AodThemeConfig =
        copy(values = options.flatMap { option -> option.storedKeys }.fold(values) { map, key -> map.remove(key) })
}

val AodThemeOption<*>.storedKeys: List<String>
    get() = if (this is AodColorOption) listOf(key) + gradientKeys else listOf(key)

fun AodThemeOption<*>.isSame(first: AodThemeConfig, second: AodThemeConfig): Boolean =
    decode(first.values[key]) == decode(second.values[key]) &&
        (this !is AodColorOption || first.gradientOf(this) == second.gradientOf(this))

fun AodThemeOption<*>.isDefault(config: AodThemeConfig): Boolean = isSame(config, AodThemeConfig())
