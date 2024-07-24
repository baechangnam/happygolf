package com.artilearn.happygolfgo.data.ticketmanager.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.ticketmanager.source.mapper.RemoteExpiredMapper
import com.artilearn.happygolfgo.data.ticketmanager.source.mapper.RemotePauseMapper
import com.artilearn.happygolfgo.data.ticketmanager.source.model.RemoteExpiredTicketModel
import com.artilearn.happygolfgo.data.ticketmanager.source.model.RemotePasueTicketModel
import io.reactivex.Single

class TicketManagerRemoteDataSourceImpl(
    private val api: ApiInterface
) : TicketManagerRemoteDataSource {
    override fun getPauseTickets(): Single<List<RemotePasueTicketModel>> {
        return api.getPauseTickets()
            .map { it.data.tickets.map(RemotePauseMapper::mapper) }
    }

    override fun getExpiredTickets(): Single<List<RemoteExpiredTicketModel>> {
        return api.getExpiredTickets()
            .map { it.data.tickets.map(RemoteExpiredMapper::mapper) }
    }
}