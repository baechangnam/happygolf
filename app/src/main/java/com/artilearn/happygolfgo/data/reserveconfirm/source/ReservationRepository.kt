package com.artilearn.happygolfgo.data.reserveconfirm.source

import com.artilearn.happygolfgo.data.reserveconfirm.*
import io.reactivex.Single
import retrofit2.Response

interface ReservationRepository {

    fun reservationPlate(
        data: HashMap<String, ReservePlateModel>
    ): Single<Response<ReservePlateResponse>>

    fun reservationLesson(
        data: HashMap<String, ReserveLessonModel>
    ): Single<Response<ReserveLessonResponse>>

    //featue/2022/0216/multipleSelectoin
    fun reservationThreeOneTimePlate(
        data: HashMap<String, ReserveThreeOneTimePlateModel>
    ): Single<Response<ReservePlateResponse>>
}