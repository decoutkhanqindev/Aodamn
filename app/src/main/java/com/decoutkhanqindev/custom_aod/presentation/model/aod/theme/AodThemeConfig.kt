package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf

// Giá trị các thuộc tính của một chủ đề theo khoá; khoá chưa có thì thuộc tính lấy giá trị mặc định, nên cấu hình rỗng là mặc định.
@Immutable
data class AodThemeConfig(val values: PersistentMap<String, Int> = persistentMapOf()) {
    operator fun <T> get(option: AodThemeOption<T>): T = option.decode(values[option.key])

    fun <T> with(option: AodThemeOption<T>, value: T): AodThemeConfig = withRaw(option.key, option.encode(value))

    fun withRaw(key: String, raw: Int): AodThemeConfig = copy(values = values.put(key, raw))
}
