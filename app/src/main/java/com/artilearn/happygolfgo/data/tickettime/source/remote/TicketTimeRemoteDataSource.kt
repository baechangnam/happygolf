package com.artilearn.happygolfgo.data.tickettime.source.remote

import com.artilearn.happygolfgo.data.tickettime.TicketTimeResponse
import io.reactivex.Single
import retrofit2.Response

interface TicketTimeRemoteDataSource {

    fun remoteSelectPlateTime(
        startDate: String,
        ticketId: Int
    ): Single<Response<TicketTimeResponse>>

    fun remoteSelectLessonTime(
        startDate: String,
        ticketId: Int
    ): Single<Response<TicketTimeResponse>>
}