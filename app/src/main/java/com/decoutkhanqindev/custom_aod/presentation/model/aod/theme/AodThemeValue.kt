package com.decoutkhanqindev.custom_aod.presentation.model.aod.theme

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.custom_aod.R
import com.decoutkhanqindev.custom_aod.data.local.datastore.DataStoreManager

// Các chủ đề AOD. id lưu trong DataStore nên không đổi; thêm chủ đề = một mục ở đây + đặc tả AodThemeSpec + phần vẽ ở
// components/aod/theme/ (AodClockFace chọn theo mục này).
@Immutable
enum class AodThemeValue(
    val id: String,
    @param:StringRes val nameRes: Int,
    val spec: AodThemeSpec,
) {
    CLASSIC(id = DataStoreManager.DEFAULT_AOD_THEME_ID, nameRes = R.string.theme_classic, spec = ClassicThemeSpec),
    PEBBLE(id = "pebble", nameRes = R.string.theme_pebble, spec = PebbleThemeSpec);

    companion object {
        val DEFAULT = CLASSIC

        fun fromId(id: String?): AodThemeValue = entries.find { it.id == id } ?: DEFAULT
    }
}
