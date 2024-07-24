package com.artilearn.happygolfgo.data.ticketmanager.source.remote

import com.artilearn.happygolfgo.data.ticketmanager.source.model.RemoteExpiredTicketModel
import com.artilearn.happygolfgo.data.ticketmanager.source.model.RemotePasueTicketModel
import io.reactivex.Single

interface TicketManagerRemoteDataSource {
    fun getPauseTickets(): Single<List<RemotePasueTicketModel>>
    fun getExpiredTickets(): Single<List<RemoteExpiredTicketModel>>
}