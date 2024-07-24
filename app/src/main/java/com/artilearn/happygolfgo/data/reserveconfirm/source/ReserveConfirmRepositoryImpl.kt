package com.artilearn.happygolfgo.data.reserveconfirm.source

import android.accounts.NetworkErrorException
import com.artilearn.happygolfgo.data.reserveconfirm.*
import com.artilearn.happygolfgo.data.reserveconfirm.source.remote.CancelRemoteDataSource
import com.artilearn.happygolfgo.data.reserveconfirm.source.remote.ReservationRemoteDataSource
import com.artilearn.happygolfgo.data.reserveconfirm.source.remote.ReserveConfirmRemoteDataSource
import com.artilearn.happygolfgo.data.reserveconfirm.source.remote.TimeChangeRemoteSource
import com.artilearn.happygolfgo.util.NetworkManager
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Response

class ReserveConfirmRepositoryImpl(
    private val reserveConfirmRemoteDataSource: ReserveConfirmRemoteDataSource,
    private val reservationRemoteDataSource: ReservationRemoteDataSource,
    private val cancelRemoteDataSource: CancelRemoteDataSource,
    private val timeChangeRemoteSource: TimeChangeRemoteSource,
    private val networkManager: NetworkManager
) : ReserveConfirmRepository, ReservationRepository, CancelRepository, TimeChangeRepository {

    override fun requestPolicy(): Single<Response<PolicyResponse>> {
        return if (networkManager.networkState()) {
            reserveConfirmRemoteDataSource.remoteRequestPolicy()
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun reservationPlate(
        data: HashMap<String, ReservePlateModel>
    ): Single<Response<ReservePlateResponse>> {
        return if (networkManager.networkState()) {
            reservationRemoteDataSource.remoteReservationPlate(data)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun reservationThreeOneTimePlate(
        data: HashMap<String, ReserveThreeOneTimePlateModel>
    ): Single<Response<ReservePlateResponse>> {
        return if (networkManager.networkState()) {
            reservationRemoteDataSource.remoteReservationThreeOnTimePlate(data)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun reservationLesson(
        data: HashMap<String, ReserveLessonModel>
    ): Single<Response<ReserveLessonResponse>> {
        return if (networkManager.networkState()) {
            reservationRemoteDataSource.remoteReservationLesson(data)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun reservationPlateCancel(plateId: Int): Completable {
        return if (networkManager.networkState()) {
            cancelRemoteDataSource.remoteReservationPlateCancel(plateId)
        } else {
            Completable.error(NetworkErrorException("Network Error"))
        }
    }

    override fun reservationLessonCancel(lessonId: Int): Completable {
        return if (networkManager.networkState()) {
            cancelRemoteDataSource.remoteReservationLessonCancel(lessonId)
        } else {
            Completable.error(NetworkErrorException("Network Error"))
        }
    }

    override fun getReservationLessonDetail(
        lessonId: Int
    ): Single<Response<ReservationLessonDetail>> {
        return if (networkManager.networkState()) {
            reserveConfirmRemoteDataSource.remoteReservationLessonDetail(lessonId)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun getReservationPlateDetail(
        lessonId: Int
    ): Single<Response<ReservationPlateDetail>> {
        return if (networkManager.networkState()) {
            reserveConfirmRemoteDataSource.remoteReservationPlateDetail(lessonId)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }

    override fun timeChnage(
        hashMap: HashMap<String, PlateTimeChangeModel>
    ): Single<PlateTimeChangeResponse.TimePlateReservation> {
        return if (networkManager.networkState()) {
            timeChangeRemoteSource.plateTimeChange(hashMap)
                .flatMap {
                    if (it.isSuccessful) {
                        Single.just(it)
                    } else {
                        Single.error(HttpException(it))
                    }
                }
                .map { it.body()?.data?.plateReservation }
        } else {
            Single.error(NetworkErrorException("Network Error"))
        }
    }
}