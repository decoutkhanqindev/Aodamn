package com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R

@Immutable
enum class ChargingRuleValue(
    val code: Int,
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
) {
    ALWAYS(code = 0, labelRes = R.string.charging_rule_always, iconRes = R.drawable.ic_infinity),
    PLUGGED(code = 1, labelRes = R.string.charging_rule_plugged, iconRes = R.drawable.ic_battery_charging),
    ON_BATTERY(code = 2, labelRes = R.string.charging_rule_on_battery, iconRes = R.drawable.ic_battery_full);

    // Không đọc được trạng thái cắm nguồn (null) thì không chặn AOD.
    fun allows(isPlugged: Boolean?): Boolean = when (this) {
        ALWAYS -> true
        PLUGGED -> isPlugged != false
        ON_BATTERY -> isPlugged != true
    }

    companion object {
        fun fromCode(code: Int?): ChargingRuleValue = entries.find { it.code == code } ?: ALWAYS
    }
}
