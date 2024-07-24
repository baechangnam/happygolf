package com.artilearn.happygolfgo.data.ticketmanager.source

import com.artilearn.happygolfgo.ui.ticketmanager.model.ExpiredModel
import com.artilearn.happygolfgo.ui.ticketmanager.model.PauseModel
import io.reactivex.Single

interface TicketManagerRepository {
    fun getPauseTickets(): Single<List<PauseModel>>
    fun getExpiredTickets(): Single<List<ExpiredModel>>
}