package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.annotation.StringRes
import com.decoutkhanqindev.aodamn.R

enum class AodZoneRegionValue(
    val prefix: String,
    @param:StringRes val labelRes: Int,
) {
    AFRICA("Africa", R.string.zone_region_africa),
    AMERICA("America", R.string.zone_region_america),
    ANTARCTICA("Antarctica", R.string.zone_region_antarctica),
    ASIA("Asia", R.string.zone_region_asia),
    ATLANTIC("Atlantic", R.string.zone_region_atlantic),
    AUSTRALIA("Australia", R.string.zone_region_australia),
    EUROPE("Europe", R.string.zone_region_europe),
    INDIAN("Indian", R.string.zone_region_indian),
    PACIFIC("Pacific", R.string.zone_region_pacific),
    ;

    companion object {
        fun of(id: String): AodZoneRegionValue? = id
            .takeIf { '/' in it }
            ?.substringBefore('/')
            ?.let { prefix -> entries.find { it.prefix == prefix } }
    }
}
