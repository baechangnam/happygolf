package com.artilearn.happygolfgo.data.reserveconfirm.source.remote

import com.artilearn.happygolfgo.api.ApiInterface
import com.artilearn.happygolfgo.data.reserveconfirm.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response

class ReserveConfirmRemoteDataSourceImpl(
    private val apiInterface: ApiInterface
) : ReserveConfirmRemoteDataSource, ReservationRemoteDataSource, CancelRemoteDataSource, TimeChangeRemoteSource {

    override fun remoteRequestPolicy(): Single<Response<PolicyResponse>> {
        return apiInterface.requestPolicy()
    }

    override fun remoteReservationPlate(
        data: HashMap<String, ReservePlateModel>
    ): Single<Response<ReservePlateResponse>> {
        return apiInterface.reservationPlate(data)
    }

    override fun remoteReservationThreeOnTimePlate(data: HashMap<String, ReserveThreeOneTimePlateModel>): Single<Response<ReservePlateResponse>> {
        return apiInterface.reservationThreeOneTimePlate(data)
    }

    override fun remoteReservationLesson(
        data: HashMap<String, ReserveLessonModel>
    ): Single<Response<ReserveLessonResponse>> {
        return apiInterface.reservationLesson(data)
    }

    override fun remoteReservationPlateCancel(plateId: Int): Completable {
        return apiInterface.reservationPlateCancel(plateId)
    }

    override fun remoteReservationLessonCancel(lessonId: Int): Completable {
        return apiInterface.reservationLessonCancel(lessonId)
    }

    override fun remoteReservationLessonDetail(
        lessonId: Int
    ): Single<Response<ReservationLessonDetail>> {
        return apiInterface.getReservationLessonDetail(lessonId)
    }

    override fun remoteReservationPlateDetail(
        plateId: Int
    ): Single<Response<ReservationPlateDetail>> {
        return apiInterface.getReservationPlateDetail(plateId)
    }

    override fun plateTimeChange(
        data: HashMap<String, PlateTimeChangeModel>
    ): Single<Response<PlateTimeChangeResponse>> {
        return apiInterface.plateTimeChange(data)
    }
}