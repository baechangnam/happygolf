package com.artilearn.happygolfgo.data.reserveconfirm.source.remote

import com.artilearn.happygolfgo.data.reserveconfirm.*
import io.reactivex.Single
import retrofit2.Response

interface ReservationRemoteDataSource {

    fun remoteReservationPlate(
        data: HashMap<String, ReservePlateModel>
    ): Single<Response<ReservePlateResponse>>

    fun remoteReservationThreeOnTimePlate(
        data: HashMap<String, ReserveThreeOneTimePlateModel>
    ): Single<Response<ReservePlateResponse>>

    fun remoteReservationLesson(
        data: HashMap<String, ReserveLessonModel>
    ): Single<Response<ReserveLessonResponse>>


}