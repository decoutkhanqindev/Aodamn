package com.decoutkhanqindev.aodamn.presentation.components.aod

object AodSpreadMetrics {
    const val TOP_RATIO = 0.12f
    const val BOTTOM_RATIO = 0.1f
    const val ROOM_RATIO = 1f - TOP_RATIO - BOTTOM_RATIO

    fun topY(height: Int): Int = (height * TOP_RATIO).toInt()

    fun bottomY(
        height: Int,
        topHeight: Int,
        bottomHeight: Int,
        minGap: Int,
    ): Int = maxOf((height * (1f - BOTTOM_RATIO)).toInt() - bottomHeight, topY(height) + topHeight + minGap)
}
