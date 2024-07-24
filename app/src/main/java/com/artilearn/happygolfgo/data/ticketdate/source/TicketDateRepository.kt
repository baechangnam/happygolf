package com.artilearn.happygolfgo.data.ticketdate.source

import com.artilearn.happygolfgo.data.ticketdate.TicketDateResponse
import io.reactivex.Single
import retrofit2.Response

interface TicketDateRepository {
    var getUsed: Boolean

    fun selectPlateDate(
        ticketId: Int
    ): Single<Response<TicketDateResponse>>

    fun selectLessonDate(
        ticketId: Int
    ): Single<Response<TicketDateResponse>>
}