package com.decoutkhanqindev.aodamn.domain.repository

import com.decoutkhanqindev.aodamn.domain.model.CalendarEvent

interface CalendarRepository {
    suspend fun getEvents(fromMillis: Long, toMillis: Long, limit: Int): List<CalendarEvent>
}
