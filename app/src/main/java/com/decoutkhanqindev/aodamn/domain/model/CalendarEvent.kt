package com.decoutkhanqindev.aodamn.domain.model

data class CalendarEvent(
    val title: String,
    val beginMillis: Long,
    val endMillis: Long,
    val isAllDay: Boolean,
)
