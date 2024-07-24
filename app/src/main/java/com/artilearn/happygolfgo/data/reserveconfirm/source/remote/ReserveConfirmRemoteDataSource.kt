package com.artilearn.happygolfgo.data.reserveconfirm.source.remote

import com.artilearn.happygolfgo.data.reserveconfirm.PolicyResponse
import com.artilearn.happygolfgo.data.reserveconfirm.ReservationLessonDetail
import com.artilearn.happygolfgo.data.reserveconfirm.ReservationPlateDetail
import io.reactivex.Single
import retrofit2.Response

interface ReserveConfirmRemoteDataSource {

    fun remoteRequestPolicy(): Single<Response<PolicyResponse>>

    fun remoteReservationLessonDetail(
        lessonId: Int
    ): Single<Response<ReservationLessonDetail>>

    fun remoteReservationPlateDetail(
        plateId: Int
    ): Single<Response<ReservationPlateDetail>>
}