package com.artilearn.happygolfgo.data.tickettime.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.tickettime.TicketTimeResponse
import com.artilearn.happygolfgo.data.tickettime.source.remote.TicketTimeRemoteDataSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Response

class TicketTimeRepositoryImpl(
    private val ticketTimeRemoteDataSource: TicketTimeRemoteDataSource,
    private val networkManager: NetworkManager
) : TicketTimeRepository {

    override fun selectPlateTime(
        startDate: String,
        ticketId: Int
    ): Single<Response<TicketTimeResponse>> {
        return if (networkManager.networkState()) {
            ticketTimeRemoteDataSource.remoteSelectPlateTime(startDate, ticketId)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun selectLessonTime(
        startDate: String,
        ticketId: Int
    ): Single<Response<TicketTimeResponse>> {
        return if (networkManager.networkState()) {
            ticketTimeRemoteDataSource.remoteSelectLessonTime(startDate, ticketId)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }
}