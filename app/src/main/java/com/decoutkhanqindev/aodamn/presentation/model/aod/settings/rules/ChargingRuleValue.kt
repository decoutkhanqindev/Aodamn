package com.decoutkhanqindev.aodamn.presentation.model.aod.settings.rules

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.aodamn.R

@Immutable
enum class ChargingRuleValue(
    val id: String,
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
) {
    ALWAYS(id = "always", labelRes = R.string.charging_rule_always, iconRes = R.drawable.ic_infinity),
    PLUGGED(id = "plugged", labelRes = R.string.charging_rule_plugged, iconRes = R.drawable.ic_battery_charging),
    ON_BATTERY(id = "on_battery", labelRes = R.string.charging_rule_on_battery, iconRes = R.drawable.ic_battery_full);

    fun allows(isPlugged: Boolean?): Boolean = when (this) {
        ALWAYS -> true
        PLUGGED -> isPlugged != false
        ON_BATTERY -> isPlugged != true
    }

    companion object {
        fun fromId(id: String?): ChargingRuleValue = entries.find { it.id == id } ?: ALWAYS
    }
}
