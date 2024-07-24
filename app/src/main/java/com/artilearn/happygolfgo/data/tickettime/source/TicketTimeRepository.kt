package com.artilearn.happygolfgo.data.tickettime.source

import com.artilearn.happygolfgo.data.tickettime.TicketTimeResponse
import io.reactivex.Single
import retrofit2.Response

interface TicketTimeRepository {

    fun selectPlateTime(
        startDate: String,
        ticketId: Int
    ): Single<Response<TicketTimeResponse>>

    fun selectLessonTime(
        startDate: String,
        ticketId: Int
    ): Single<Response<TicketTimeResponse>>
}