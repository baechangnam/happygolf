package com.artilearn.happygolfgo.data.reserveconfirm.source

import com.artilearn.happygolfgo.data.reserveconfirm.PolicyResponse
import com.artilearn.happygolfgo.data.reserveconfirm.ReservationLessonDetail
import com.artilearn.happygolfgo.data.reserveconfirm.ReservationPlateDetail
import io.reactivex.Single
import retrofit2.Response

interface ReserveConfirmRepository {

    fun requestPolicy(): Single<Response<PolicyResponse>>

    fun getReservationLessonDetail(
        lessonId: Int
    ): Single<Response<ReservationLessonDetail>>

    fun getReservationPlateDetail(
        plateId: Int
    ): Single<Response<ReservationPlateDetail>>
}