package com.decoutkhanqindev.aodamn.presentation.model.aod.theme.blinky

import androidx.compose.runtime.Immutable

@Immutable
enum class BlinkyLineValue {
    STATUS,
    NOTIFICATION,
    MEDIA;

    val next: BlinkyLineValue
        get() = entries[(ordinal + 1) % entries.size]
}
