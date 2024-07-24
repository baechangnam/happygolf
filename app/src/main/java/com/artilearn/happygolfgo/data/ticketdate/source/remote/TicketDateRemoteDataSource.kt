package com.artilearn.happygolfgo.data.ticketdate.source.remote

import com.artilearn.happygolfgo.data.ticketdate.TicketDateResponse
import io.reactivex.Single
import retrofit2.Response

interface TicketDateRemoteDataSource {

    fun remoteSelectPlateDate(
        ticketId: Int
    ): Single<Response<TicketDateResponse>>

    fun remoteSelectLessonDate(
        ticketId: Int
    ): Single<Response<TicketDateResponse>>
}