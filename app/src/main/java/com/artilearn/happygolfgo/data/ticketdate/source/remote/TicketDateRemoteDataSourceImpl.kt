package com.artilearn.happygolfgo.data.ticketdate.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.ticketdate.TicketDateResponse
import io.reactivex.Single
import retrofit2.Response

class TicketDateRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : TicketDateRemoteDataSource {

    override fun remoteSelectPlateDate(
        ticketId: Int
    ): Single<Response<TicketDateResponse>> {
        return apiInterface.selectPlateDate(ticketId)
    }

    override fun remoteSelectLessonDate(
        ticketId: Int
    ): Single<Response<TicketDateResponse>> {
        return apiInterface.selectLessonDate(ticketId)
    }
}