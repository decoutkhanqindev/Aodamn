package com.decoutkhanqindev.aodamn.domain.usecase

import com.decoutkhanqindev.aodamn.domain.model.CalendarEvent
import com.decoutkhanqindev.aodamn.domain.repository.CalendarRepository
import com.decoutkhanqindev.aodamn.utils.suspendRunCatching

class GetUpcomingEventsUseCase(private val repository: CalendarRepository) {
    suspend operator fun invoke(
        nowMillis: Long,
        endOfDayMillis: Long,
        limit: Int
    ): Result<List<CalendarEvent>> =
        suspendRunCatching {
            repository.getEvents(
                fromMillis = nowMillis,
                toMillis = endOfDayMillis,
                limit = limit
            )
        }
}
