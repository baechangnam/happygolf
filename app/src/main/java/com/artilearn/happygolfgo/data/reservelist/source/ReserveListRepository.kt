package com.artilearn.happygolfgo.data.reservelist.source

import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import io.reactivex.Single
import retrofit2.Response

interface ReserveListRepository {
    fun selectReservationList(): Single<Response<ReserveListResponse>>
    fun selectReservationListInMultiBranch(): Single<Response<ReserveListResponse>>
}