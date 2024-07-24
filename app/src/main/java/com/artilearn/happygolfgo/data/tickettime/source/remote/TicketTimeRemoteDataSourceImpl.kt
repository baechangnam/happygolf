package com.artilearn.happygolfgo.data.tickettime.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.tickettime.TicketTimeResponse
import io.reactivex.Single
import retrofit2.Response

class TicketTimeRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : TicketTimeRemoteDataSource {

    override fun remoteSelectPlateTime(
        startDate: String,
        ticketId: Int
    ): Single<Response<TicketTimeResponse>> {
        return apiInterface.selectPlateTime(startDate, ticketId)
    }

    override fun remoteSelectLessonTime(
        startDate: String,
        ticketId: Int
    ): Single<Response<TicketTimeResponse>> {
        return apiInterface.selectLessonTime(startDate, ticketId)
    }
}