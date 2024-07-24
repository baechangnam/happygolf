package com.artilearn.happygolfgo.data.ticketmanager.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.ticketmanager.source.remote.TicketManagerRemoteDataSource
import com.artilearn.happygolfgo.ui.ticketmanager.mapper.ExpiredTicketMapper
import com.artilearn.happygolfgo.ui.ticketmanager.mapper.PauseTicketMapper
import com.artilearn.happygolfgo.ui.ticketmanager.model.ExpiredModel
import com.artilearn.happygolfgo.ui.ticketmanager.model.PauseModel
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single

class TicketManagerRepositoryImpl(
    private val remoteDataSource: TicketManagerRemoteDataSource,
    private val network: NetworkManager
) : TicketManagerRepository {
    override fun getPauseTickets(): Single<List<PauseModel>> {
        return if (network.networkState()) {
            remoteDataSource.getPauseTickets()
                .map {
                    it.map(PauseTicketMapper::mapper)
                }
        } else {
            Single.error(NetworkErrorException("네트워크 환경이 좋지 않습니다"))
        }
    }

    override fun getExpiredTickets(): Single<List<ExpiredModel>> {
        return if (network.networkState()) {
            remoteDataSource.getExpiredTickets()
                .map {
                    it.map(ExpiredTicketMapper::mapper)
                }
        } else {
            Single.error(NetworkErrorException("네트워크 환경이 좋지 않습니다"))
        }
    }

}