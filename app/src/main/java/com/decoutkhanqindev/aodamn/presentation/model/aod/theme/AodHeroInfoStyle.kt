package com.decoutkhanqindev.aodamn.presentation.model.aod.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Immutable
data class AodHeroInfoStyle(
    val font: FontFamily,
    val dateColor: AodColorOption,
    val notificationsColor: AodColorOption,
    val batteryColor: AodColorOption,
    val topMinHeight: Dp,
    val sideMargin: Dp,
    val topSpaceWeight: Float,
    val heroWeight: Float,
    val bottomSpaceWeight: Float,
    val batteryPadding: Dp,
    val dateSize: TextUnit,
    val landscapeDateSize: TextUnit,
    val weatherGap: Dp,
    val detailsGap: Dp,
    val iconRow: Dp,
    val iconGapScale: Float,
    val latestIcon: Dp,
    val latestIconGap: Dp,
    val latestSize: TextUnit,
    val latestGap: Dp,
    val textDarken: Float,
    val batteryGlyph: Dp,
    val landscapeBatteryGlyph: Dp,
    val idleBatteryAlpha: Float,
    val landscapeGap: Dp,
    val landscapeItemGap: Dp,
)
