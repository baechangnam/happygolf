package com.artilearn.happygolfgo.data.reservelist.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.reservelist.ReserveListResponse
import io.reactivex.Single
import retrofit2.Response

class ReserveListRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : ReserveListRemoteDataSource {

    override fun remoteSelectReservationList(): Single<Response<ReserveListResponse>> {
        return apiInterface.reservationList()
    }

    override fun remoteSelectReservationListInMultiBranche(): Single<Response<ReserveListResponse>> {
        return apiInterface.reservationListInMultiBranch()
    }
}