package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFaceValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.ClockFontValue
import com.decoutkhanqindev.custom_aod.presentation.model.aod.appearance.clockColorOf
import com.decoutkhanqindev.custom_aod.presentation.model.aod.theme.classic.ClassicThemeSpec
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

@Immutable
data class AodThemeUiModel(
    val value: AodThemeValue = AodThemeValue.DEFAULT,
    val config: AodThemeConfig = AodThemeConfig(),
)

// Cấu hình mọi chủ đề lưu chung một chuỗi "id:khoá=giá trị,khoá=giá trị;id:..." (id và khoá chỉ có chữ thường, số, gạch dưới),
// tự mã hoá để kotlinx-serialization chỉ nằm ở data/network/api. Phần hỏng thì bỏ qua, thuộc tính đó về mặc định.
private fun decodeThemeConfigs(raw: String?): Map<String, Map<String, Int>> =
    raw.orEmpty().split(';').mapNotNull { entry ->
        val id = entry.substringBefore(':', missingDelimiterValue = "").takeIf { it.isNotBlank() } ?: return@mapNotNull null
        id to entry.substringAfter(':').toAodConfigValues()
    }.toMap()

private fun encodeThemeConfigs(configs: Map<String, Map<String, Int>>): String =
    configs.entries.joinToString(separator = ";") { (id, values) ->
        id + ":" + values.toAodConfigString()
    }

// Chủ đề Cổ điển chưa lưu lần nào thì dựng từ mặt / font / màu / cỡ đồng hồ đã chọn ở bản trước khi có chủ đề.
private fun legacyClassicConfig(face: Int?, font: Int?, color: Int?, sizePercent: Int?): AodThemeConfig = AodThemeConfig()
    .with(ClassicThemeSpec.Face, ClockFaceValue.fromCode(face))
    .with(ClassicThemeSpec.Font, ClockFontValue.fromCode(font))
    .with(ClassicThemeSpec.HourColor, clockColorOf(color))
    .with(ClassicThemeSpec.MinuteColor, clockColorOf(color))
    .with(ClassicThemeSpec.Size, sizePercent ?: ClassicThemeSpec.Size.default)

private fun buildThemeConfigs(raw: String?, legacyClassic: AodThemeConfig): ImmutableMap<AodThemeValue, AodThemeConfig> {
    val stored = decodeThemeConfigs(raw)
    return AodThemeValue.entries.associateWith { theme ->
        stored[theme.id]?.let { values -> AodThemeConfig(values.toPersistentMap()) }
            ?: if (theme == AodThemeValue.CLASSIC) legacyClassic else AodThemeConfig()
    }.toImmutableMap()
}

fun DataStoreManager.currentAodThemeConfigs(): ImmutableMap<AodThemeValue, AodThemeConfig> = buildThemeConfigs(
    raw = aodThemeConfigs.value,
    legacyClassic = legacyClassicConfig(
        face = aodClockFace.value,
        font = aodClockFont.value,
        color = aodClockColor.value,
        sizePercent = aodClockSizePercent.value,
    ),
)

fun DataStoreManager.currentAodTheme(): AodThemeUiModel {
    val theme = AodThemeValue.fromId(aodThemeId.value)
    return AodThemeUiModel(value = theme, config = currentAodThemeConfigs().getValue(theme))
}

fun DataStoreManager.observeAodThemeConfigs(): Flow<ImmutableMap<AodThemeValue, AodThemeConfig>> = combine(
    aodThemeConfigs.filterNotNull(),
    combine(
        aodClockFace.filterNotNull(),
        aodClockFont.filterNotNull(),
        aodClockColor.filterNotNull(),
        aodClockSizePercent.filterNotNull(),
        ::legacyClassicConfig,
    ),
    ::buildThemeConfigs,
)

fun DataStoreManager.observeAodTheme(): Flow<AodThemeUiModel> = combine(
    aodThemeId.filterNotNull(),
    observeAodThemeConfigs(),
) { id, configs ->
    val theme = AodThemeValue.fromId(id)
    AodThemeUiModel(value = theme, config = configs.getValue(theme))
}

fun DataStoreManager.saveAodThemeConfig(theme: AodThemeValue, config: AodThemeConfig): Job =
    updateAodThemeConfigs { raw ->
        val configs = decodeThemeConfigs(raw).toMutableMap()
        configs[theme.id] = config.values
        encodeThemeConfigs(configs)
    }
