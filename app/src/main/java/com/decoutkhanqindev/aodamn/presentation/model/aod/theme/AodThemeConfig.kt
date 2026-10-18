package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf

// Giá trị các thuộc tính của một chủ đề theo khoá; khoá chưa có thì thuộc tính lấy giá trị mặc định, nên cấu hình rỗng là mặc định.
// Cấu hình chủ đề / kiểu khối lưu thành chuỗi "khoá=giá trị,khoá=giá trị" (khoá chỉ có chữ thường, số, gạch dưới); phần hỏng thì bỏ qua,
// thuộc tính đó về mặc định. Tự mã hoá để kotlinx-serialization chỉ nằm ở data/network/api.
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

    // Bỏ khoá của các thuộc tính này để chúng về mặc định (đặt lại một khối).
    fun without(options: List<AodThemeOption<*>>): AodThemeConfig =
        copy(values = options.fold(values) { map, option -> map.remove(option.key) })
}
