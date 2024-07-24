package com.artilearn.happygolfgo.data.ticketdate.source.local

import com.artilearn.happygolfgo.util.PreferenceManager

class TicketDateLocalDataSourceImpl(
    private val preferenceManager: PreferenceManager
) : TicketDateLocalDataSource {
    override var calendarGetUsed: Boolean
        get() = preferenceManager.calendarUsed
        set(value) {
            preferenceManager.calendarUsed = value
        }
}