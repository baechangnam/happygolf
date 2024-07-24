package com.artilearn.happygolfgo.data.reservelist.source.remote

import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import io.reactivex.Single
import retrofit2.Response

interface ReserveListRemoteDataSource {

    fun remoteSelectReservationList(): Single<Response<ReserveListResponse>>
    fun remoteSelectReservationListInMultiBranche(): Single<Response<ReserveListResponse>>
}