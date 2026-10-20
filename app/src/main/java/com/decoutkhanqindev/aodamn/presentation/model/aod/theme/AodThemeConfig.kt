package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.decoutkhanqindev.aodamn.presentation.model.aod.appearance.AodGradient
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

    // Một lần đổi có thể ghi nhiều khoá (gradient: màu Đầu + khoá phụ); null là xoá khoá.
    fun withRaws(raws: Map<String, Int?>): AodThemeConfig = copy(
        values = raws.entries.fold(values) { map, (key, raw) -> if (raw == null) map.remove(key) else map.put(key, raw) },
    )

    // null là một màu.
    fun gradientOf(option: AodColorOption): AodGradient? = option.decodeGradient(values)

    // Tô chữ / hình theo khung đang vẽ: gradient của thuộc tính hoặc màu đơn.
    fun brushOf(option: AodColorOption): Brush = gradientOf(option)?.toBrush() ?: SolidColor(get(option))

    // Màu để tự dựng gradient theo hình (vd dọc theo kim, theo vạch): một màu thì danh sách một phần tử.
    fun colorsOf(option: AodColorOption): List<Color> = gradientOf(option)?.colors ?: listOf(get(option))

    // Bỏ khoá của các thuộc tính này (cả khoá phụ gradient) để chúng về mặc định (đặt lại một khối).
    fun without(options: List<AodThemeOption<*>>): AodThemeConfig =
        copy(values = options.flatMap { option -> option.storedKeys }.fold(values) { map, key -> map.remove(key) })
}

val AodThemeOption<*>.storedKeys: List<String>
    get() = if (this is AodColorOption) listOf(key) + gradientKeys else listOf(key)

// So theo giá trị đọc ra chứ không theo map đã lưu: khoá thiếu và khoá ghi giá trị mặc định là như nhau; màu so cả gradient.
fun AodThemeOption<*>.isSame(first: AodThemeConfig, second: AodThemeConfig): Boolean =
    decode(first.values[key]) == decode(second.values[key]) &&
        (this !is AodColorOption || first.gradientOf(this) == second.gradientOf(this))

fun AodThemeOption<*>.isDefault(config: AodThemeConfig): Boolean = isSame(config, AodThemeConfig())
